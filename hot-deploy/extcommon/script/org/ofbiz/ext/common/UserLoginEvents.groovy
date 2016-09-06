package org.ofbiz.ext.common

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.MessageUtil
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest

public String resetPassword() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    def requestParameters = UtilHttp.getParameterMap(request)
    GenericValue userLogin = userLogin

    def results = dispatcher.runSync("bizResetPassword", [
            userLoginId      : requestParameters.userLoginId,
            newPassword      : requestParameters.newPassword,
            newPasswordVerify: requestParameters.newPasswordVerify,
            userLogin        : userLogin
    ])

    return MessageUtil.handleServiceResults(request, results)
}

public String updatePassword() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def results = dispatcher.runSync("bizUpdatePassword", [
            userLoginId      : requestParameters.userLoginId,
            currentPassword  : requestParameters.currentPassword,
            newPassword      : requestParameters.newPassword,
            newPasswordVerify: requestParameters.newPasswordVerify,
            userLogin        : userLogin
    ])

    return MessageUtil.handleServiceResults(request, results)
}