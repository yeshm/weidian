package org.weidian.merchant.setting

import com.juweitu.bonn.merchant.MerchantWorker
import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.condition.EntityOperator
import org.ofbiz.entity.util.EntityUtil
import org.ofbiz.ext.util.ExtEntityUtil

import javax.servlet.http.HttpServletRequest

def editSecurityGroup(Map context) {
    HttpServletRequest request = request
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def groupId = requestParameters.groupId

    def selectedPermissionIdList = []
    def securityGroup = ExtEntityUtil.findOneCache(delegator, "SecurityGroup", [groupId: groupId])
    def securityPermissionList = ExtEntityUtil.findList(delegator, "SecurityGroupPermission", ["permissionId"] as Set, [groupId: groupId])

    for (def securityPermission : securityPermissionList) {
        def permissionId = securityPermission.permissionId
        selectedPermissionIdList.add(permissionId)
    }

    context.securityGroup = securityGroup
    context.selectedPermissionIdList = selectedPermissionIdList
}

def editUserLogin(Map context) {
    HttpServletRequest request = request
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def merchantPartyId = MerchantWorker.getCurrentMerchantPartyId(request)

    def userLoginId = requestParameters.userLoginId

    if (!UtilValidate.isWhitespace(userLoginId)) {
        def adminUserLogin = delegator.findOne("UserLogin", [userLoginId: userLoginId], true)

        def entityCondition = EntityCondition.makeCondition([
                EntityCondition.makeCondition("userLoginId", userLoginId),
                EntityUtil.getFilterByDateExpr()
        ])
        def userLoginSecurityGroup = ExtEntityUtil.getOnly(delegator, "UserLoginSecurityGroup", entityCondition)

        context.adminUserLogin = adminUserLogin
        context.userLoginSecurityGroup = userLoginSecurityGroup
    }

    def groupId = "%ADMIN_GROUP_%"
    if (MerchantWorker.isMerchant(request)) {
        groupId = "%MERCHANT_" + merchantPartyId + "_%"
    }
    def entityCondition = EntityCondition.makeCondition("groupId", EntityOperator.LIKE, groupId)
    def securityGroupList = ExtEntityUtil.findListSortedCache(delegator, "SecurityGroup", entityCondition, ["-createdStamp"])

    context.securityGroupList = securityGroupList
}