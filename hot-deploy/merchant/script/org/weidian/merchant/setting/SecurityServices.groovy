package org.weidian.merchant.setting

import com.juweitu.bonn.constant.common.CommonStatus
import com.juweitu.bonn.merchant.MerchantWorker
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilMisc
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.util.EntityUtil
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.SecurityUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map createSecurityGroup() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def description = parameters.description
    List permissionIdList = parameters.permissionIdList
    def merchantPartyId = parameters.merchantPartyId

    if (UtilValidate.isWhitespace(description)) return ServiceUtil.returnError("权限组名称不能为空")
    if (UtilValidate.isEmpty(permissionIdList)) return ServiceUtil.returnError("权限id不能为空")

    def securityGroup = delegator.makeValidValue("SecurityGroup")
    securityGroup.description = description
    securityGroup.statusId = CommonStatus.ENABLED

    def groupName = "ADMIN_GROUP_"
    if (MerchantWorker.isMerchant(delegator, merchantPartyId)) {
        groupName = "MERCHANT_" + merchantPartyId + "_"
    }

    securityGroup.setNextSeqId()
    securityGroup.groupId = groupName + securityGroup.groupId
    securityGroup.create()

    def groupId = securityGroup.groupId

    for (int i = 0; i < permissionIdList.size(); i++) {
        def permissionId = permissionIdList.get(i);
        def securityGroupPermission = delegator.makeValidValue("SecurityGroupPermission")
        securityGroupPermission.groupId = groupId
        securityGroupPermission.permissionId = permissionId
        securityGroupPermission.create()
    }

    def results = ServiceUtil.returnSuccess()
    results.groupId = groupId
    return results
}

public Map updateSecurityGroup() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def groupId = parameters.groupId
    def description = parameters.description
    List permissionIdList = parameters.permissionIdList

    if (UtilValidate.isWhitespace(description)) return ServiceUtil.returnError("权限组名称不能为空")
    if (UtilValidate.isEmpty(permissionIdList)) return ServiceUtil.returnError("权限id不能为空")

    def securityGroup = delegator.findOne("SecurityGroup", [groupId: groupId], false)
    if (UtilValidate.isEmpty(securityGroup)) return ServiceUtil.returnError("找不到权限组")

    securityGroup.description = description
    securityGroup.store()

    delegator.removeByAnd("SecurityGroupPermission", [groupId: groupId])
    for (int i = 0; i < permissionIdList.size(); i++) {
        def permissionId = permissionIdList.get(i);
        def securityGroupPermission = delegator.makeValidValue("SecurityGroupPermission")
        securityGroupPermission.groupId = groupId
        securityGroupPermission.permissionId = permissionId
        securityGroupPermission.create()
    }

    def results = ServiceUtil.returnSuccess()
    results.groupId = groupId
    return results
}

public Map deleteSecurityGroup() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def groupId = parameters.groupId

    def securityGroup = delegator.findOne("SecurityGroup", [groupId: groupId], false)

    if (UtilValidate.isEmpty(securityGroup)) return ServiceUtil.returnError("找不到该权限组")

    def entityConditionList = [
            EntityCondition.makeCondition("groupId", groupId),
            EntityUtil.getFilterByDateExpr()
    ]

    def count = ExtEntityUtil.findCountByCondition(delegator, "UserLoginSecurityGroup", EntityCondition.makeCondition(entityConditionList))
    if (count > 0) return ServiceUtil.returnError("当前权限组被其他用户使用，不能删除")

    delegator.removeByAnd("SecurityGroupPermission", [groupId: groupId])
    delegator.removeByAnd("UserLoginSecurityGroup", [groupId: groupId])
    securityGroup.remove()

    def results = ServiceUtil.returnSuccess()
    results.groupId = groupId

    return results
}

public Map saveUserLogin() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def partyId = parameters.partyId
    def userLoginId = parameters.userLoginId
    def currentPassword = parameters.currentPassword
    def currentPasswordVerify = parameters.currentPasswordVerify
    def groupId = parameters.groupId

    if (UtilValidate.isEmpty(userLoginId)) return ServiceUtil.returnError("用户名不能为空！")
    if (!currentPassword.equals(currentPasswordVerify)) return ServiceUtil.returnError("两次输入的密码不一致")
    if (UtilValidate.isWhitespace(groupId)) return ServiceUtil.returnError("请选择一个权限组")

    def adminUserLogin = ExtEntityUtil.findOne(delegator, "UserLogin", [userLoginId: userLoginId])

    if (SecurityUtil.isSystemUserLoginId(userLoginId) || (UtilValidate.isNotEmpty(adminUserLogin) && !partyId.equals(adminUserLogin.partyId))) {
        return ServiceUtil.returnError("用户名已经被使用，请使用别的用户名")
    }

    if (UtilValidate.isEmpty(adminUserLogin)) {
        if (UtilValidate.isWhitespace(currentPassword)) return ServiceUtil.returnError("密码不能为空！")

        def serviceCtx = dispatcher.getDispatchContext().makeValidContext("createUserLogin", "IN", parameters)
        serviceCtx.partyId = partyId
        serviceCtx.enabled = "Y"
        def results = dispatcher.runSync("createUserLogin", serviceCtx)
        if (!ServiceUtil.isSuccess(results)) return results

        //创建账户权限
        def userLoginSecurityGroup = delegator.makeValue("UserLoginSecurityGroup")
        userLoginSecurityGroup.userLoginId = userLoginId
        userLoginSecurityGroup.groupId = groupId
        userLoginSecurityGroup.fromDate = UtilDateTime.nowTimestamp()
        userLoginSecurityGroup.create()
    } else {
        def entityConditionList = [
                EntityCondition.makeCondition("userLoginId", userLoginId),
                EntityUtil.getFilterByDateExpr()
        ]
        def oldUserLoginSecurityGroup = ExtEntityUtil.getOnly(delegator, "UserLoginSecurityGroup", EntityCondition.makeCondition(entityConditionList))
        if (UtilValidate.isEmpty(oldUserLoginSecurityGroup)) return ServiceUtil.returnError("修改失败，找不到原有账户")

        def oldGroupId = oldUserLoginSecurityGroup.groupId
        if (!groupId.equals(oldGroupId)) {
            oldUserLoginSecurityGroup.thruDate = UtilDateTime.nowTimestamp()
            oldUserLoginSecurityGroup.store()

            def newUserLoginSecurityGroup = delegator.makeValue("UserLoginSecurityGroup", [
                    userLoginId: userLoginId,
                    groupId    : groupId,
                    fromDate   : UtilDateTime.nowTimestamp()
            ])

            newUserLoginSecurityGroup.create()
        }
    }

    def results = ServiceUtil.returnSuccess()
    results.userLoginId = userLoginId

    return results
}

public Map deleteUserLogin() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def userLoginId = parameters.userLoginId

    def adminUserLogin = ExtEntityUtil.findOne(delegator, "UserLogin", [userLoginId: userLoginId])

    if (UtilValidate.isEmpty(adminUserLogin)) return ServiceUtil.returnError("当前账号不存在")

    adminUserLogin.enabled = "N"
    adminUserLogin.store()

    def entityConditionList = [
            EntityCondition.makeCondition("userLoginId", userLoginId),
            EntityUtil.getFilterByDateExpr()
    ]
    delegator.storeByCondition("UserLoginSecurityGroup", UtilMisc.toMap("thruDate", UtilDateTime.nowTimestamp()), EntityCondition.makeCondition(entityConditionList))

    def results = ServiceUtil.returnSuccess()
    results.userLoginId = userLoginId

    return results
}