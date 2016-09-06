package org.weidian.merchant.setting

import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.util.EntityUtil
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map saveDefaultSenderInfo() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def productStoreId = parameters.productStoreId

    def now = UtilDateTime.nowTimestamp()

    EntityCondition entityCondition = EntityCondition.makeCondition([
            EntityCondition.makeCondition("productStoreId", productStoreId),
            EntityUtil.getFilterByDateExpr()
    ])
    def defaultSenderInfo = ExtEntityUtil.getOnly(delegator, "BizDefaultSenderInfo", entityCondition)

    if (UtilValidate.isNotEmpty(defaultSenderInfo)) {
        defaultSenderInfo.thruDate = now
        defaultSenderInfo.store()
    }

    defaultSenderInfo = delegator.makeValue("BizDefaultSenderInfo", [
            productStoreId    : productStoreId,
            createdDate       : now,
            fromDate          : now,
            createdByUserLogin: userLogin.userLoginId
    ])
    defaultSenderInfo.setNonPKFields(parameters)
    defaultSenderInfo.setNextSeqId()
    defaultSenderInfo.create()

    def results = ServiceUtil.returnSuccess()
    results.defaultSenderInfoId = defaultSenderInfo.defaultSenderInfoId

    return results
}
