package org.ofbiz.ext.common

import com.juweitu.bonn.constant.party.PartyStatus
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map updatePartyProfile() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def partyId = parameters.partyId

    if (UtilValidate.isWhitespace(partyId)) return ServiceUtil.returnError("参数不完整")

    def person = ExtEntityUtil.findOne(delegator, "Person", [partyId: partyId])
    if (UtilValidate.isEmpty(person)) return ServiceUtil.returnError("修改失败")

    person.setNonPKFields(parameters)
    person.store()

    return ServiceUtil.returnSuccess()
}

public Map togglePartyStatus() {
    Delegator delegator = delegator
    Map parameters = parameters

    def partyId = parameters.partyId

    if (UtilValidate.isWhitespace(partyId)) return ServiceUtil.returnError("参数不完整")

    def party = ExtEntityUtil.findOne(delegator, "Party", [partyId: partyId])
    if (UtilValidate.isEmpty(party)) return ServiceUtil.returnError("操作失败")

    def userLoginList = ExtEntityUtil.findList(delegator, "UserLogin", EntityCondition.makeCondition("partyId", partyId))
    if (UtilValidate.isEmpty(userLoginList)) return ServiceUtil.returnError("操作失败")

    def partyStatusId = party.statusId
    for (def userLogin : userLoginList) {
        if (partyStatusId.equals(PartyStatus.ENABLED)) {
            userLogin.enabled = "N"
        } else {
            userLogin.enabled = "Y"
        }
        userLogin.store()
    }

    if (partyStatusId.equals(PartyStatus.ENABLED)) {
        party.statusId = PartyStatus.DISABLED
    } else {
        party.statusId = PartyStatus.ENABLED
    }
    party.store()

    def results = ServiceUtil.returnSuccess()
    results.partyId = partyId
    results.statusId = partyStatusId

    return results
}