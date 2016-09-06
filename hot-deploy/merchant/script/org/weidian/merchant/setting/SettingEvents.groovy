package org.weidian.merchant.setting

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.MessageUtil
import org.ofbiz.ext.util.SecurityUtil
import org.ofbiz.service.LocalDispatcher
import org.weidian.merchant.MerchantWorker

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public String saveProfile() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def partyId = MerchantWorker.getCurrentMerchantPartyId(request)

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("updatePerson", "IN", requestParameters)
    serviceCtx.lastName = ""
    serviceCtx.partyId = partyId
    serviceCtx.locale = UtilHttp.getLocale(request)
    serviceCtx.userLogin = SecurityUtil.getSystemUserLogin(delegator)

    def results = dispatcher.runSync("updatePerson", serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String saveWeiDian() {
    LocalDispatcher dispatcher = dispatcher
    HttpServletRequest request = request
    def requestParameters = UtilHttp.getParameterMap(request)
    Delegator delegator = delegator
    GenericValue userLogin = userLogin

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizSaveWeiDian", "IN", requestParameters)
    serviceCtx.productStoreId = productStoreId
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync("bizSaveWeiDian", serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String saveDefaultSenderInfo() {
    LocalDispatcher dispatcher = dispatcher
    HttpServletRequest request = request
    def requestParameters = UtilHttp.getParameterMap(request)
    Delegator delegator = delegator
    GenericValue userLogin = userLogin

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizSaveDefaultSenderInfo", "IN", requestParameters)
    serviceCtx.productStoreId = productStoreId
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync("bizSaveDefaultSenderInfo", serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}
