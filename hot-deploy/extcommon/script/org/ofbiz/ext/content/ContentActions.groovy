package org.ofbiz.ext.content

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator

def editContent(Map context) {
    Delegator delegator = delegator
    def requestParameters = UtilHttp.getParameterMap(request)

    def contentId = requestParameters.contentId
    def contentTypeId = requestParameters.contentTypeId
    def parentTypeId = requestParameters.parentTypeId
    def listContentMenuId = requestParameters.listContentMenuId
    def ownerPartyId = requestParameters.ownerPartyId

    def content = delegator.findOne("ExtContent", [contentId: contentId], true)

    if (UtilValidate.isNotEmpty(parentTypeId)) {
        def subContentTypeList = delegator.findByAnd("ExtContentType", [parentTypeId: parentTypeId], null, true)
        context.subContentTypeList = subContentTypeList
    }

    context.content = content
    context.contentTypeId = contentTypeId
    context.parentTypeId = parentTypeId
    context.ownerPartyId = ownerPartyId
    context.listContentMenuId = listContentMenuId
}