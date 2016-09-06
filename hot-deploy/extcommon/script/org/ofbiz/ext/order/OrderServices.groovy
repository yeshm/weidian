package org.ofbiz.ext.order

import org.ofbiz.entity.Delegator
import org.ofbiz.service.DispatchContext
import org.ofbiz.service.ServiceUtil

public Map countProductQuantityOrdered() {
    DispatchContext ctx = dctx
    Map contextMap = context
    Map parameters = parameters
    Delegator delegator = delegator

    return ServiceUtil.returnSuccess()
}
