package org.weidian.admin

import com.juweitu.bonn.constant.Company
import com.juweitu.bonn.merchant.MerchantWorker
import org.ofbiz.base.util.UtilHttp
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.MessageUtil
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

public String initAdminData() {
    HttpServletRequest request = request
    HttpServletResponse response = response
    HttpSession session = session
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def partyId = Company.PARTY_ID
    def productStoreId = AdminWorker.getCurrentProductStoreId(request)

    def results = dispatcher.runSync("bizInitAdminData", [
            partyId       : partyId,
            productStoreId: productStoreId,
            userLogin     : userLogin
    ])
    session.setAttribute("isMerchant", MerchantWorker.isMerchant(request))

    return MessageUtil.handleServiceResults(request, results)
}