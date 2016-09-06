package org.ofbiz.ext.shipment

import com.juweitu.bonn.constant.common.CommonStatus
import org.ofbiz.base.util.UtilHttp
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

def getLatestExpressContent(Map context) {
    HttpServletRequest request = request
    HttpServletResponse response = response
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def trackingNumber = requestParameters.trackingNumber

    def entityConditionList = [
            EntityCondition.makeCondition(statusId: CommonStatus.ENABLED),
            EntityCondition.makeCondition(expressNu: trackingNumber)
    ]
    def latestExpressContent = ExtEntityUtil.getFirstSorted(delegator, "ExtExpressQueryLog", EntityCondition.makeCondition(entityConditionList), ["logTime DESC"])

    context.latestExpressContent = latestExpressContent
}

def getAllExpressContent(Map context) {
    HttpServletRequest request = request
    HttpServletResponse response = response
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def trackingNumber = requestParameters.trackingNumber
    def carrierPartyId = requestParameters.carrierPartyId

    def partyGroup = ExtEntityUtil.getOnlyCache(delegator, "PartyAndGroup", [partyId: carrierPartyId]);
    String expressName = partyGroup.getString("description");
    String expressLogoImgUrl = partyGroup.getString("logoImageUrl");
    String groupNameLocal = partyGroup.getString("groupNameLocal");

    def entityConditionList = [
            EntityCondition.makeCondition(statusId: CommonStatus.ENABLED),
            EntityCondition.makeCondition(expressNu: trackingNumber),
            EntityCondition.makeCondition(expressCom: groupNameLocal)
    ]
    def expressContentList = ExtEntityUtil.findListSortedCache(delegator, "ExtExpressQueryLog", EntityCondition.makeCondition(entityConditionList), ["logTime DESC"])

    context.expressName = expressName
    context.expressLogoImgUrl = expressLogoImgUrl
    context.expressContentList = expressContentList
}