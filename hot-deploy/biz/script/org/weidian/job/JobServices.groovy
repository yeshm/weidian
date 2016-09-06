package org.weidian.job

import groovy.transform.Field
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

import java.sql.Timestamp

@Field
def module = "JobServices.groovy"

/**
 * 刷新微店AccessToken
 */
public Map refreshWeiDianAccessToken() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def weiDianList = ExtEntityUtil.findList(delegator, "BizWeiDian", [:])
    for (GenericValue weiDian : weiDianList) {
        Timestamp accessTokenExpirationDate = weiDian.accessTokenExpirationDate
        def now = UtilDateTime.nowTimestamp()

        if (UtilValidate.isNotEmpty(weiDian.get("accessToken"))
                && UtilValidate.isNotEmpty(accessTokenExpirationDate)
                && (accessTokenExpirationDate.getTime() - now.getTime() > 10 * 60 * 1000)) {
            //未过期，不处理
            continue
        }
        dispatcher.runSync("bizRefreshWeiDianAccessToken", [productStoreId: weiDian.productStoreId])
    }

    return ServiceUtil.returnSuccess()
}
