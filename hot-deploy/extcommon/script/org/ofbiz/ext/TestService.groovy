package org.ofbiz.ext

import org.ofbiz.entity.Delegator
import org.ofbiz.service.DispatchContext
import org.ofbiz.service.ServiceUtil

public Map setSurveyOptionSeqId4SurveyResponseAnswer() {
    DispatchContext dctx = (DispatchContext) context.get("dctx")
    Delegator delegator = dctx.getDelegator()
    def parameters = parameters

    Map<String, Object> result = ServiceUtil.returnSuccess()
    result.result = "success result!"
    return result
}