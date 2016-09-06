package org.weidian.merchant.order

import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil
import org.weidian.constant.common.CommonStatus

public Map createAgent() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def now = UtilDateTime.nowTimestamp()

    GenericValue agent = delegator.makeValue("BizAgent")
    agent.setNonPKFields(parameters)
    agent.statusId = CommonStatus.ENABLED
    agent.createdDate = now
    agent.createdByUserLogin = userLogin.userLoginId
    agent.lastModifiedDate = now
    agent.lastModifiedByUserLogin = userLogin.userLoginId
    agent.setNextSeqId()
    agent.create()

    def results = ServiceUtil.returnSuccess()
    results.agentId = agent.agentId

    return results
}

public Map updateAgent() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def agentId = parameters.agentId
    def productStoreId = parameters.productStoreId

    def now = UtilDateTime.nowTimestamp()
    def results = ServiceUtil.returnSuccess()

    def agent = ExtEntityUtil.getOnly(delegator, "BizAgent", [
            productStoreId: productStoreId,
            agentId       : agentId
    ])

    results.agentId = agent.agentId
    results.oldStatusId = agent.statusId

    agent.setNonPKFields(parameters)
    agent.lastModifiedDate = now
    agent.lastModifiedByUserLogin = userLogin.userLoginId
    agent.store()

    return results
}

public Map deleteAgent() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def agentId = parameters.agentId
    def productStoreId = parameters.productStoreId

    def agent = ExtEntityUtil.getOnly(delegator, "BizAgent", [
            productStoreId: productStoreId,
            agentId       : agentId
    ])
    def now = UtilDateTime.nowTimestamp()

    agent.statusId = CommonStatus.DELETED
    agent.lastModifiedDate = now
    agent.lastModifiedByUserLogin = userLogin.userLoginId
    agent.store()

    def results = ServiceUtil.returnSuccess()
    results.agentId = agentId
    return results
}
