package org.weidian.merchant.setting

import com.juweitu.bonn.merchant.MerchantWorker
import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.base.util.string.FlexibleStringExpander
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.condition.EntityOperator
import org.ofbiz.entity.util.EntityUtil
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.MessageUtil
import org.ofbiz.ext.util.RequestUtil
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest

public String gridSecurityGroup() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def viewIndex = RequestUtil.getRequestPageIndex(request)
    def viewSize = RequestUtil.getRequestPageSize(request)

    def merchantPartyId = MerchantWorker.getCurrentMerchantPartyId(request)

    def groupId = "%ADMIN_GROUP_%"
    if (MerchantWorker.isMerchant(request)) {
        groupId = "%MERCHANT_" + merchantPartyId + "_%"
    }

    def entityCondition = EntityCondition.makeCondition("groupId", EntityOperator.LIKE, groupId)

    def result = dispatcher.runSync("performFindPage", [
            inputFields        : requestParameters,
            entityName         : "SecurityGroup",
            entityConditionList: entityCondition,
            orderBy            : "-createdStamp",
            viewIndex          : viewIndex,
            viewSize           : viewSize,
            timeZone           : timeZone
    ])
    request.setAttribute("result", result)

    return "success"
}

public String editSecurityGroupQueryPermission() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def entityCondition = EntityCondition.makeCondition([
            EntityCondition.makeCondition(
                    EntityCondition.makeCondition("permissionEnumId", "SECURITY_PUBLIC"),
                    EntityOperator.OR,
                    EntityCondition.makeCondition("permissionEnumId", "SECURITY_ADMIN"),
            )
    ])

    if (MerchantWorker.isMerchant(request)) {
        entityCondition = EntityCondition.makeCondition([
                EntityCondition.makeCondition(
                        EntityCondition.makeCondition("permissionEnumId", "SECURITY_PUBLIC"),
                        EntityOperator.OR,
                        EntityCondition.makeCondition("permissionEnumId", "SECURITY_MERCHANT"),
                )
        ])
    }

//    def fieldsToSelect = ["description", "parentPermissionId", "permissionEnumId","permissionId", "permissionLevel"] as Set;
    def allPermissionList = ExtEntityUtil.findListSortedCache(delegator, "SecurityPermission", entityCondition, ["permissionLevel", "sequenceNum"])
    def allPermissionListSorted = []
    def firstLevelPermissionList = []

    entityCondition = EntityCondition.makeCondition("permissionLevel", 1l)
    def permissionList = ExtEntityUtil.filterByCondition(allPermissionList, entityCondition)

    if (UtilValidate.isNotEmpty(permissionList)) {
        for(GenericValue permission : permissionList){
            def temp = [:]
            temp.putAll(permission)
            temp.description = FlexibleStringExpander.expandString(permission.description, context)
            firstLevelPermissionList.add(temp)
        }
    }

    //通过一级菜单获取二级菜单
    for (def firstLevelPermission : firstLevelPermissionList) {
        entityCondition = EntityCondition.makeCondition("parentPermissionId", firstLevelPermission.permissionId)
        def secondLevelPermissionList = ExtEntityUtil.filterByCondition(allPermissionList, entityCondition)

        def temp = [:]
        temp.putAll(firstLevelPermission)
        temp.description = FlexibleStringExpander.expandString(firstLevelPermission.description, context)
        allPermissionListSorted.add(temp)

        for (def secondLevelPermission : secondLevelPermissionList) {
            temp = [:]
            temp.putAll(secondLevelPermission)
            temp.description = FlexibleStringExpander.expandString(secondLevelPermission.description, context)

            allPermissionListSorted.add(temp)

            entityCondition = EntityCondition.makeCondition("parentPermissionId", secondLevelPermission.permissionId)
            def thirdLevelPermissionList = ExtEntityUtil.filterByCondition(allPermissionList, entityCondition)

            for (def thirdLevelPermission : thirdLevelPermissionList) {
                temp = [:]
                temp.putAll(thirdLevelPermission)
                temp.description = FlexibleStringExpander.expandString(thirdLevelPermission.description, context)

                allPermissionListSorted.add(temp)
            }
        }
    }
    request.setAttribute("permissionList", allPermissionListSorted)

    return "success"
}

public String saveSecurityGroup() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def groupId = requestParameters.groupId
    def permissionId = requestParameters.permissionId

    def permissionIdList = []
    if (UtilValidate.isNotEmpty(permissionId)) {
        if (permissionId instanceof String) {
            permissionIdList.add(permissionId)
        } else {
            permissionIdList.addAll(permissionId)
        }
    }

    def serviceName = UtilValidate.isEmpty(groupId) ? "bizCreateSecurityGroup" : "bizUpdateSecurityGroup"
    def serviceCtx = dispatcher.getDispatchContext().makeValidContext(serviceName, "IN", requestParameters)
    serviceCtx.merchantPartyId = MerchantWorker.getCurrentMerchantPartyId(request)
    serviceCtx.userLogin = userLogin
    serviceCtx.permissionIdList = permissionIdList
    def results = dispatcher.runSync(serviceName, serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String deleteSecurityGroup() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizDeleteSecurityGroup", "IN", requestParameters)
    serviceCtx.userLogin = userLogin
    def results = dispatcher.runSync("bizDeleteSecurityGroup", serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String gridUserLogin() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def viewIndex = RequestUtil.getRequestPageIndex(request)
    def viewSize = RequestUtil.getRequestPageSize(request)

    def merchantPartyId = MerchantWorker.getCurrentMerchantPartyId(request)

    def groupId = "%ADMIN_GROUP_%"
    if (MerchantWorker.isMerchant(request)) {
        groupId = "%MERCHANT_" + merchantPartyId + "_%"
    }
    def entityConditionList = [
            EntityCondition.makeCondition("groupId", EntityOperator.LIKE, groupId),
            EntityCondition.makeCondition("enabled", EntityOperator.NOT_EQUAL, "N"),
            EntityUtil.getFilterByDateExpr()
    ]

    def result = dispatcher.runSync("performFindPage", [
            inputFields        : requestParameters,
            entityName         : "BizUserLoginListView",
            entityConditionList: EntityCondition.makeCondition(entityConditionList),
            orderBy            : "-createdStamp",
            viewIndex          : viewIndex,
            viewSize           : viewSize,
            timeZone           : timeZone
    ])
    request.setAttribute("result", result)

    return "success"
}

public String saveUserLogin() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def partyId = MerchantWorker.getCurrentMerchantPartyId(request)

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizSaveUserLogin", "IN", requestParameters)
    serviceCtx.partyId = partyId
    serviceCtx.userLogin = userLogin
    def results = dispatcher.runSync("bizSaveUserLogin", serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String deleteUserLogin() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizDeleteUserLogin", "IN", requestParameters)
    serviceCtx.userLogin = userLogin
    def results = dispatcher.runSync("bizDeleteUserLogin", serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}