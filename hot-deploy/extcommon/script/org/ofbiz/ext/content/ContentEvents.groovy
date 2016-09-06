package org.ofbiz.ext.content

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.condition.EntityOperator
import org.ofbiz.ext.biz.ExtUserOperateLogUtil
import org.ofbiz.ext.util.MessageUtil
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest

public String gridContent() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    def requestParameters = UtilHttp.getParameterMap(request)

    def viewIndex = Integer.valueOf(requestParameters.pageIndex)
    def viewSize = Integer.valueOf(requestParameters.limit)

    def entityConditionList = [
            EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "COMMON_ENABLED")
    ]

    def parentTypeId = requestParameters.parentTypeId
    if (UtilValidate.isNotEmpty(parentTypeId)) {
        entityConditionList.add(EntityCondition.makeCondition("parentTypeId", parentTypeId))
    } else {
        entityConditionList.add(EntityCondition.makeCondition("contentTypeId", requestParameters.contentTypeId))
    }

    entityConditionList.add(EntityCondition.makeCondition("partyId", requestParameters.ownerPartyId))

    def result = dispatcher.runSync("performFindPage", [
            inputFields        : requestParameters,
            entityName         : "ExtContentView",
            entityConditionList: EntityCondition.makeCondition(entityConditionList, EntityOperator.AND),
            orderBy            : "sequenceNum|-createdDate",
            viewIndex          : viewIndex,
            viewSize           : viewSize,
            timeZone           : timeZone
    ])
    request.setAttribute("result", result)

    return "success"
}

public String saveExtendContent() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    if (UtilValidate.isEmpty(requestParameters.partyId) || (UtilValidate.isEmpty(requestParameters.contentTypeId) && UtilValidate.isEmpty(requestParameters.contentParentTypeId))) {
        MessageUtil.saveErrorMessage(request, "非法操作")
    }

    def contentId = requestParameters.contentId

    def serviceName
    if (UtilValidate.isNotEmpty(contentId)) {
        serviceName = "updateExtendContent"
        ExtUserOperateLogUtil.logUpdate(request, "编辑内容：" + contentId)
    } else {
        serviceName = "createExtendContent"
        ExtUserOperateLogUtil.logCreate(request, "创建内容：" + contentId)
    }

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext(serviceName, "IN", requestParameters)
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync(serviceName, serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String deleteExtendContent() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("deleteExtendContent", "IN", requestParameters)
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync("deleteExtendContent", serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}