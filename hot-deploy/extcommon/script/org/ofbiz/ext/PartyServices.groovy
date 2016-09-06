package org.ofbiz.ext

import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.ext.biz.ConstantParameter
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.service.DispatchContext
import org.ofbiz.service.ServiceUtil

public Map createPartyRelationship() {
    DispatchContext ctx = dctx
    Map parameters = parameters
    GenericValue userLogin = userLogin
    Delegator delegator = delegator

    if (UtilValidate.isWhitespace(parameters.roleTypeIdFrom)) parameters.roleTypeIdFrom = ConstantParameter.NA
    if (UtilValidate.isWhitespace(parameters.roleTypeIdTo)) parameters.roleTypeIdTo = ConstantParameter.NA
    if (UtilValidate.isWhitespace(parameters.partyIdFrom)) parameters.partyIdFrom = userLogin.partyId
    if (UtilValidate.isWhitespace(parameters.fromDate)) parameters.fromDate = UtilDateTime.nowTimestamp()

    def entityCondition = EntityCondition.makeCondition([
            EntityCondition.makeCondition("partyIdFrom", parameters.partyIdFrom),
            EntityCondition.makeCondition("roleTypeIdFrom", parameters.roleTypeIdFrom),
            EntityCondition.makeCondition("partyIdTo", parameters.partyIdTo),
            EntityCondition.makeCondition("roleTypeIdTo", parameters.roleTypeIdTo),
            EntityCondition.makeCondition("partyRelationshipTypeId", parameters.partyRelationshipTypeId),
            ExtEntityUtil.getFilterByDateExpr()
    ])
    def partyRels = ExtEntityUtil.findList(delegator, "PartyRelationship", entityCondition)

    if (UtilValidate.isEmpty(partyRels)) {
        def newEntity = delegator.makeValidValue("PartyRelationship", parameters)
        newEntity.create()
    }

    return ServiceUtil.returnSuccess()
}