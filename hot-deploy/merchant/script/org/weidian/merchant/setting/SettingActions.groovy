package org.weidian.merchant.setting

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.util.EntityUtil
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.service.LocalDispatcher
import org.weidian.merchant.MerchantWorker

import javax.servlet.http.HttpServletRequest

def editProfile(Map context) {
    Delegator delegator = delegator
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def partyId = userLogin.partyId
    def person = delegator.findOne("Person", [partyId: partyId], true)

    context.person = person
}

def editWeiDian(Map context) {
    Delegator delegator = delegator
    HttpServletRequest request = request
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)
    def weidian = ExtEntityUtil.findOne(delegator, "BizWeiDian", [productStoreId: productStoreId])

    context.weidian = weidian
}

def editDefaultSenderInfo(Map context) {
    Delegator delegator = delegator
    HttpServletRequest request = request
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)

    EntityCondition entityCondition = EntityCondition.makeCondition([
            EntityCondition.makeCondition("productStoreId", productStoreId),
            EntityUtil.getFilterByDateExpr()
    ])
    def defaultSenderInfo = ExtEntityUtil.getOnly(delegator, "BizDefaultSenderInfo", entityCondition)

    context.defaultSenderInfo = defaultSenderInfo
}
