package org.weidian.merchant.order

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.ExtEntityUtil
import org.weidian.merchant.MerchantWorker

import javax.servlet.http.HttpServletRequest

def listOrder(Map context) {
    HttpServletRequest request = request
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)

    def agentList = ExtEntityUtil.findListSorted(delegator, "BizAgent", [
            statusId      : "COMMON_ENABLED",
            productStoreId: productStoreId
    ], ["-createdDate"])

    context.agentList = agentList
}
