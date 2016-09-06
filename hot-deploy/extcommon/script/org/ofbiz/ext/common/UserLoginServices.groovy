package org.ofbiz.ext.common

import org.ofbiz.base.crypto.HashCrypt
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.common.login.LoginServices
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map resetPassword() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def userLoginId = parameters.userLoginId
    def newPassword = parameters.newPassword
    def newPasswordVerify = parameters.newPasswordVerify

    if (UtilValidate.isWhitespace(userLoginId) || UtilValidate.isWhitespace(newPassword) || UtilValidate.isWhitespace(newPasswordVerify)) return ServiceUtil.returnError("参数不完整")
    if (!newPassword.equals(newPasswordVerify)) return ServiceUtil.returnError("两次输入的密码不一致")

    def userLoginEntity = delegator.findOne("UserLogin", [userLoginId: userLoginId], false)
    if (UtilValidate.isEmpty(userLoginEntity)) return ServiceUtil.returnError("修改失败")

    userLoginEntity.currentPassword = HashCrypt.digestHash(LoginServices.getHashType(), null, newPassword)
    userLoginEntity.store()

    return ServiceUtil.returnSuccess()
}

public Map updatePassword() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def userLoginId = parameters.userLoginId
    def currentPassword = parameters.currentPassword
    def newPassword = parameters.newPassword
    def newPasswordVerify = parameters.newPasswordVerify

    if (UtilValidate.isWhitespace(userLoginId) || UtilValidate.isWhitespace(currentPassword) || UtilValidate.isWhitespace(newPassword) || UtilValidate.isWhitespace(newPasswordVerify)) return ServiceUtil.returnError("参数不完整")
    if (!newPassword.equals(newPasswordVerify)) return ServiceUtil.returnError("两次输入的密码不一致")

    def userLoginEntity = delegator.findOne("UserLogin", [userLoginId: userLoginId], false)
    if (UtilValidate.isEmpty(userLoginEntity)) return ServiceUtil.returnError("修改失败")
    if (!HashCrypt.comparePassword(userLoginEntity.currentPassword, LoginServices.getHashType(), currentPassword)) return ServiceUtil.returnError("您输入的旧密码不正确")

    userLoginEntity.currentPassword = HashCrypt.digestHash(LoginServices.getHashType(), null, newPassword)
    userLoginEntity.store()

    return ServiceUtil.returnSuccess()
}

public Map createUserOperateLog() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def bizPartyId = parameters.bizPartyId
    def comments = parameters.comments
    def operateIp = parameters.operateIp
    def enumId = parameters.enumId
    def operateUserLoginId = parameters.operateUserLoginId

    def userOperateLog = delegator.makeValue("ExtUserOperateLog", [
            bizPartyId        : bizPartyId,
            operateUserLoginId: operateUserLoginId,
            operateDate       : UtilDateTime.nowTimestamp(),
            comments          : comments,
            operateIp         : operateIp,
            enumId            : enumId
    ])
    userOperateLog.setNextSeqId()
    userOperateLog.create()

    def results = ServiceUtil.returnSuccess()
    results.operateLogId = userOperateLog.operateLogId

    return results
}