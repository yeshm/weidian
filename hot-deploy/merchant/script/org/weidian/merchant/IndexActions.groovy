package org.weidian.merchant

import org.weidian.SystemConfigWorker
import org.weidian.constant.common.CommonStatus
import org.weidian.constant.order.OrderStatus
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.base.util.string.FlexibleStringExpander
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.condition.EntityOperator
import org.ofbiz.entity.util.EntityFindOptions
import org.ofbiz.entity.util.EntityUtil
import org.ofbiz.ext.util.AppUtil
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.ExtUtilDateTime
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest


def main(Map context) {
    HttpServletRequest request = request
    Delegator delegator = context.delegator
    LocalDispatcher dispatcher = dispatcher
    def requestParameters = UtilHttp.getParameterMap(request)

}

def menu(Map context) {
    HttpServletRequest request = request
    Delegator delegator = context.delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def userLoginId = userLogin.userLoginId

    def firstLevelMenuList = [] //一级菜单
    def menuJsonArray = AppUtil.getMapper().getMapper().createArrayNode()

    def entityCondition = EntityCondition.makeCondition(
            EntityCondition.makeCondition("userLoginId", userLoginId),
            EntityCondition.makeCondition("groupId", EntityOperator.LIKE, "MERCHANT_%"),
            EntityUtil.getFilterByDateExpr()
    )
    def userLoginSecurityGroupList = ExtEntityUtil.findListCache(delegator, "UserLoginSecurityGroup", entityCondition)
    def userLoginSecurityGroupIdList = []
    for (GenericValue userLoginSecurityGroup in userLoginSecurityGroupList) {
        userLoginSecurityGroupIdList.add(userLoginSecurityGroup.groupId)
    }

    entityCondition = EntityCondition.makeCondition([
            EntityCondition.makeCondition("componentId", "merchant"),
            EntityCondition.makeCondition("groupId", EntityOperator.IN, userLoginSecurityGroupIdList)
    ])

    def allSecurityGroupPermissionList = ExtEntityUtil.findListSortedCache(delegator, "BizSecurityGroupPermissionView", entityCondition, ["permissionLevel", "sequenceNum"])
    entityCondition = EntityCondition.makeCondition("permissionLevel", 1l)
    def securityGroupPermissionList = ExtEntityUtil.filterByCondition(allSecurityGroupPermissionList, entityCondition)

    if (UtilValidate.isNotEmpty(securityGroupPermissionList)) {
        firstLevelMenuList.addAll(securityGroupPermissionList)
    }

    //通过一级菜单获取二级菜单
    for (def firstMenu : firstLevelMenuList) {
        entityCondition = EntityCondition.makeCondition("parentPermissionId", firstMenu.permissionId)
        def secondLevelPermissionList = ExtEntityUtil.filterByCondition(allSecurityGroupPermissionList, entityCondition)

        def firstMenuJson = AppUtil.getMapper().getMapper().createObjectNode()
        firstMenuJson.put("id", firstMenu.permissionId)

        def secondLevelMenuJsonArray = AppUtil.getMapper().getMapper().createArrayNode()
        for (def secondLevelPermission : secondLevelPermissionList) {
            def secondMenuJson = AppUtil.getMapper().getMapper().createObjectNode()
            def description = secondLevelPermission.description
            def text = FlexibleStringExpander.expandString(description, context)
            secondMenuJson.put("text", text)

            entityCondition = EntityCondition.makeCondition("parentPermissionId", secondLevelPermission.permissionId)
            def thirdLevelPermissionList = ExtEntityUtil.filterByCondition(allSecurityGroupPermissionList, entityCondition)
            def thirdLevelMenuJsonArray = AppUtil.getMapper().getMapper().createArrayNode()

            for (def thirdLevelPermission : thirdLevelPermissionList) {
                def thirdMenuJson = AppUtil.getMapper().getMapper().createObjectNode()
                description = thirdLevelPermission.description
                text = FlexibleStringExpander.expandString(description, context)

                thirdMenuJson.put("id", thirdLevelPermission.permissionId)
                thirdMenuJson.put("text", text)
                if (!UtilValidate.isWhitespace(thirdLevelPermission.menuLinkUrl)) {
                    if (thirdLevelPermission.menuLinkUrl.contains("#") && thirdLevelPermission.menuLinkUrl.contains("/")) {
                        def modeText = thirdLevelPermission.menuLinkUrl.split('#')[1].split('/')
                        def moduleId = modeText[0]
                        def id = modeText[1]

                        thirdMenuJson.put("isCustomerMenu", true)
                        thirdMenuJson.put("targetMenuModuleId", moduleId)
                        thirdMenuJson.put("targetMenuId", id)
                    } else {
                        thirdMenuJson.put("href", (thirdLevelPermission.menuLinkUrl) ?: "")
                    }
                }
                thirdLevelMenuJsonArray.add(thirdMenuJson)
            }

            secondMenuJson.set("items", thirdLevelMenuJsonArray)
            secondLevelMenuJsonArray.add(secondMenuJson)

            if (!firstMenuJson.has("homePage") && UtilValidate.isNotEmpty(thirdLevelPermissionList)) {
                firstMenuJson.put("homePage", thirdLevelPermissionList?.get(0)?.permissionId)
            }
        }
        firstMenuJson.set("menu", secondLevelMenuJsonArray)
        menuJsonArray.add(firstMenuJson)
    }

    context.firstLevelMenuList = firstLevelMenuList
    context.menuJsonArray = menuJsonArray
}