package org.weidian.admin.setting

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.MessageUtil
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest

public String saveQiniuCloudConfig() {
    LocalDispatcher dispatcher = dispatcher
    HttpServletRequest request = request
    def requestParameters = UtilHttp.getParameterMap(request)
    Delegator delegator = delegator
    GenericValue userLogin = userLogin

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizSaveQiniuCloudConfig", "IN", requestParameters)
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync("bizSaveQiniuCloudConfig", serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String saveBaiduMapConfig() {
    LocalDispatcher dispatcher = dispatcher
    HttpServletRequest request = request
    def requestParameters = UtilHttp.getParameterMap(request)
    Delegator delegator = delegator
    GenericValue userLogin = userLogin

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizSaveBaiduMapConfig", "IN", requestParameters)
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync("bizSaveBaiduMapConfig", serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String saveExpressApiConfig() {
    LocalDispatcher dispatcher = dispatcher
    HttpServletRequest request = request
    def requestParameters = UtilHttp.getParameterMap(request)
    Delegator delegator = delegator
    GenericValue userLogin = userLogin

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizSaveExpressApiConfig", "IN", requestParameters)
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync("bizSaveExpressApiConfig", serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String saveEmailConfig() {
    LocalDispatcher dispatcher = dispatcher
    HttpServletRequest request = request
    def requestParameters = UtilHttp.getParameterMap(request)
    Delegator delegator = delegator
    GenericValue userLogin = userLogin

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizSaveEmailConfig", "IN", requestParameters)
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync("bizSaveEmailConfig", serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}
