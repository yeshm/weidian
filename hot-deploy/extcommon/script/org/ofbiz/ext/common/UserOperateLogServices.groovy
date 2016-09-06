package org.ofbiz.ext.common

import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.entity.Delegator
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map createUserOperateLog() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
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
