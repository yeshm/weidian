package org.weidian.merchant.weidian

import com.ibm.icu.util.Calendar
import com.weidian.open.sdk.response.oauth.OAuthResponse
import com.weidian.open.sdk.util.SystemConfig
import org.ofbiz.base.util.Debug
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.AppUtil
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.ExtHttpClient
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil
import org.weidian.api.WeiDianApiHelper

public Map saveWeiDian() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def productStoreId = parameters.productStoreId

    def weiDian = ExtEntityUtil.findOne(delegator, "BizWeiDian", [productStoreId: productStoreId])

    if (UtilValidate.isEmpty(weiDian)) {
        weiDian = delegator.makeValue("BizWeiDian", [
                productStoreId    : productStoreId,
                createdDate       : UtilDateTime.nowTimestamp(),
                createdByUserLogin: userLogin.userLoginId
        ])
    }

    weiDian.setNonPKFields(parameters)
    weiDian.lastModifiedDate = UtilDateTime.nowTimestamp()
    weiDian.lastModifiedByUserLogin = userLogin.userLoginId

    delegator.createOrStore(weiDian)

    def results = ServiceUtil.returnSuccess()
    results.productStoreId = weiDian.productStoreId

    return results
}

public Map refreshWeiDianAccessToken() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    Map parameters = parameters

    def productStoreId = parameters.productStoreId

    def weiDian = ExtEntityUtil.getOnly(delegator, "BizWeiDian", ["productStoreId": productStoreId])
    if (UtilValidate.isEmpty(weiDian)) return ServiceUtil.returnError("找不到微店")

    def results = ServiceUtil.returnSuccess()
    results.productStoreId = productStoreId

    String appkey = weiDian.getString("appkey")
    String secret = weiDian.getString("secret")

    if (UtilValidate.isWhitespace(appkey) || UtilValidate.isWhitespace(secret)) {
        Debug.logError("appkey or secret is empty, appkey:${appkey}, secret:${secret}", "refreshWeixinAccessToken")
        return results
    }

    String url = String.format(SystemConfig.PERSONAL_TOKEN_URL_TEMPLATE, appkey, secret)
    def httpResult = ExtHttpClient.get(url)

    if (!WeiDianApiHelper.isSuccess(httpResult)) return WeiDianApiHelper.transToErrorResults(httpResult)

    String responseString = httpResult.get("responseString");
    OAuthResponse oAuthResponse = AppUtil.fromJson(responseString, OAuthResponse.class)

    if(oAuthResponse.getStatus().getStatusCode() != 0){
        Debug.logError(oAuthResponse.getStatus().getStatusReason(), "")
        return results
    }

    def accessTokenExpirationDate = UtilDateTime.adjustTimestamp(UtilDateTime.nowTimestamp(), Calendar.SECOND, oAuthResponse.getResult().getExpire_in())

    weiDian.accessToken = oAuthResponse.getResult().getAccessToken()
    weiDian.accessTokenExpirationDate = accessTokenExpirationDate
    weiDian.lastModifiedDate = UtilDateTime.nowTimestamp()
    weiDian.store()

    return results
}
