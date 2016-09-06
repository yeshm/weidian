package org.ofbiz.ext.account

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.condition.EntityOperator
import org.ofbiz.ext.biz.ExtCommonWorker
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest

public String gridUserOperateLog() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    def requestParameters = UtilHttp.getParameterMap(request)
    TimeZone timeZone = timeZone

    def viewIndex = Integer.valueOf(requestParameters.pageIndex)
    def viewSize = Integer.valueOf(requestParameters.limit)

    def entityConditionList = [
            EntityCondition.makeCondition("bizPartyId", ExtCommonWorker.getCurrentBizPartyId(request))
    ]

    def result = dispatcher.runSync("performFindPage", [
            inputFields        : requestParameters,
            entityName         : "ExtUserOperateLog",
            entityConditionList: EntityCondition.makeCondition(entityConditionList, EntityOperator.AND),
            noConditionFind    : "Y",
            orderBy            : "-operateLogId",
            viewIndex          : viewIndex,
            viewSize           : viewSize,
            timeZone           : timeZone
    ])

    request.setAttribute("result", result)

    return "success"
}