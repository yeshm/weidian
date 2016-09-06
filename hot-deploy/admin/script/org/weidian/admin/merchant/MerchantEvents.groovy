package org.weidian.admin.merchant

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.MessageUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

import javax.servlet.http.HttpServletRequest

public String gridMerchant() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    def requestParameters = UtilHttp.getParameterMap(request)

    def viewIndex = Integer.valueOf(requestParameters.pageIndex)
    def viewSize = Integer.valueOf(requestParameters.limit)

    def result = dispatcher.runSync("performFindPage", [
            inputFields    : requestParameters,
            entityName     : "BizProductStore",
            noConditionFind: "Y",
            orderBy        : "-createdDate",
            viewIndex      : viewIndex,
            viewSize       : viewSize,
            timeZone       : timeZone
    ])

    request.setAttribute("result", result)

    return "success"
}

public String saveMerchant() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def productStoreId = requestParameters.productStoreId

    def serviceName

    if (UtilValidate.isWhitespace(productStoreId)) {
        serviceName = "bizCreateMerchant"
    } else {
        serviceName = "bizUpdateMerchant"
    }

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext(serviceName, "IN", requestParameters)
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync(serviceName, serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String syncWeiDianMerchant() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def ids = requestParameters.get("ids[]")

    if (UtilValidate.isNotEmpty(ids)) {
        if (ids instanceof String) ids = [ids]
        for (def id : ids) {
            def results = dispatcher.runSync("deleteMerchant", [
                    orderId : id,
                    userLogin: userLogin
            ])
            if (!ServiceUtil.isSuccess(results)) return MessageUtil.handleServiceResults(request, results)
        }
    }

    return "success"
}

public String deleteMerchant() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def ids = requestParameters.get("ids[]")

    if (UtilValidate.isNotEmpty(ids)) {
        if (ids instanceof String) ids = [ids]
        for (def id : ids) {
            def results = dispatcher.runSync("deleteMerchant", [
                    orderId : id,
                    userLogin: userLogin
            ])
            if (!ServiceUtil.isSuccess(results)) return MessageUtil.handleServiceResults(request, results)
        }
    }

    return "success"
}

public String getMerchant() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def orderId = requestParameters.orderId

    def order = delegator.findOne("Merchant", [orderId: orderId], true)
    request.setAttribute("order", order)

    return "success"
}

