package org.weidian.merchant.order

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.ext.util.MessageUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil
import org.weidian.merchant.MerchantWorker

import javax.servlet.http.HttpServletRequest

public String gridAgent() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    def requestParameters = UtilHttp.getParameterMap(request)

    def viewIndex = Integer.valueOf(requestParameters.pageIndex)
    def viewSize = Integer.valueOf(requestParameters.limit)

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)

    def entityCondition = EntityCondition.makeCondition("productStoreId", productStoreId)

    def result = dispatcher.runSync("performFindPage", [
            inputFields        : requestParameters,
            entityName         : "BizAgent",
            entityConditionList: entityCondition,
            orderBy            : "-createdDate",
            viewIndex          : viewIndex,
            viewSize           : viewSize,
            timeZone           : timeZone
    ])

    def tempList = []
    result.list.each { agent ->
        Map temp = [:]
        def agentItemInfoList = []
//        def agentId = agent.agentId
//
//        def agentItemList = ExtEntityUtil.findList(delegator, "BizAgentItem", [agentId: agentId])
//        for (GenericValue agentItem : agentItemList) {
//            def skuTitle = agentItem.get("skuTitle")
//            def enumeration = ExtEntityUtil.findOneCache(delegator, "Enumeration", [enumId: "SKU_" + skuTitle])
//            agentItemInfoList.add(((enumeration?.description) ?: skuTitle) + "*" + agentItem.getBigDecimal("quantity").intValue())
//        }

        temp.putAll(agent)
//        temp.agentItemInfo = StringUtils.join(agentItemInfoList, ",")

        tempList.add(temp)
    }
    result.put("list", tempList)

    request.setAttribute("result", result)

    return "success"
}

public String saveAgent() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def agentId = requestParameters.agentId

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)
    def serviceName = (UtilValidate.isWhitespace(agentId)) ? "bizCreateAgent" : "bizUpdateAgent"

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext(serviceName, "IN", requestParameters)
    serviceCtx.productStoreId = productStoreId
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync(serviceName, serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String deleteAgent() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def ids = requestParameters.get("ids")

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)

    if (UtilValidate.isNotEmpty(ids)) {
        if (ids instanceof String) ids = [ids]
        for (def id : ids) {
            def results = dispatcher.runSync("bizDeleteAgent", [
                    agentId       : id,
                    productStoreId: productStoreId,
                    userLogin     : userLogin
            ])
            if (!ServiceUtil.isSuccess(results)) return MessageUtil.handleServiceResults(request, results)
        }
    }

    return "success"
}
