package org.ofbiz.ext.common

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.MessageUtil

import javax.servlet.http.HttpServletRequest

/**
 * 验证账号是否可用（目前就校验没有被使用）
 * @return
 */
public String validateUserLoginId(){
    HttpServletRequest request = request
    Delegator delegator = delegator
    def requestParameters = UtilHttp.getParameterMap(request)

    def userLoginId = requestParameters.userLoginId

    if(UtilValidate.isEmpty(userLoginId)){
        MessageUtil.saveErrorMessage(request, "账号不能为空")
        return "error"
    }else{
        def existUserLogin = ExtEntityUtil.findOne(delegator, "UserLogin", [userLoginId: userLoginId])
        if(UtilValidate.isNotEmpty(existUserLogin)){
            MessageUtil.saveErrorMessage(request, "账号已存在")
            return "error"
        }
    }

    return "success"
}