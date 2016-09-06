package org.ofbiz.ext.account

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.biz.ExtUserOperateLogUtil
import org.ofbiz.ext.util.ExtServiceUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

import javax.servlet.http.HttpServletRequest

public String doUpdatePartyProfile() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def partyId = requestParameters.partyId

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("updatePartyProfile", "IN", requestParameters)
    serviceCtx.userLogin = userLogin
    serviceCtx.partyId = partyId
    serviceCtx.put("photoUrl", requestParameters.picUrl)

    def results = dispatcher.runSync("updatePartyProfile", serviceCtx)
    if (ServiceUtil.isError(results)) {
        ExtServiceUtil.getMessages(request, results)
    }
    ExtUserOperateLogUtil.logUpdate(request, "我的资料修改")

    return "success"
}