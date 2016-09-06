package org.ofbiz.ext.content

import com.juweitu.bonn.constant.common.CommonStatus
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.ExtUtilDateTime
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map createExtendContent() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def contentTypeId = UtilValidate.isNotEmpty(parameters.contentTypeId) ? parameters.contentTypeId : "_NA_"

    def extendContent = delegator.makeValue("ExtContent", [
            createdDate            : UtilDateTime.nowTimestamp(),
            createdByUserLogin     : userLogin.userLoginId,
            lastModifiedDate       : UtilDateTime.nowTimestamp(),
            lastModifiedByUserLogin: userLogin.userLoginId,
            statusId               : CommonStatus.ENABLED
    ])
    extendContent.setNextSeqId()
    extendContent.setNonPKFields(parameters)
    extendContent.set("fromDate", parameters.fromDate ?: UtilDateTime.nowTimestamp())
    extendContent.create()

    def results = ServiceUtil.returnSuccess()
    results.contentId = extendContent.contentId
    results.contentTypeId = contentTypeId

    return results
}

public Map updateExtendContent() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def contentId = parameters.contentId
    def contentTypeId = UtilValidate.isNotEmpty(parameters.contentTypeId) ? parameters.contentTypeId : "_NA_"

    def extendContent = ExtEntityUtil.findOne(delegator, "ExtContent", [contentId: contentId])

    extendContent.setNonPKFields(parameters)
    extendContent.fromDate = parameters.fromDate ?: UtilDateTime.nowTimestamp()
    extendContent.lastModifiedDate = UtilDateTime.nowTimestamp()
    extendContent.lastModifiedByUserLogin = userLogin.userLoginId
    extendContent.store()

    def results = ServiceUtil.returnSuccess()
    results.contentId = contentId
    results.contentTypeId = contentTypeId

    return results
}

public Map deleteExtendContent() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def contentId = parameters.contentId

    def extendContent = ExtEntityUtil.findOne(delegator, "ExtContent", [contentId: contentId])
    extendContent.statusId = CommonStatus.DELETED
    extendContent.thruDate = ExtUtilDateTime.getDayStart(UtilDateTime.nowTimestamp())
    extendContent.lastModifiedDate = UtilDateTime.nowTimestamp()
    extendContent.lastModifiedByUserLogin = userLogin.userLoginId
    extendContent.store()

    return ServiceUtil.returnSuccess()
}

public Map recordViewCount() {
    Delegator delegator = delegator
    Map parameters = parameters

    def contentId = parameters.contentId

    def content = delegator.findOne("ExtContent", [contentId: contentId], false)
    if (UtilValidate.isEmpty(content.viewCount)) {
        content.viewCount = new BigDecimal(1)
    } else {
        content.viewCount = content.viewCount + new BigDecimal(1)
    }
    content.store()

    def results = ServiceUtil.returnSuccess()
    results.contentId = contentId

    return results
}