package org.weidian.merchant.fileupload

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.ext.util.MessageUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil
import org.weidian.merchant.MerchantWorker

import javax.servlet.http.HttpServletRequest

public String gridFileUpload() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    def requestParameters = UtilHttp.getParameterMap(request)

    def viewIndex = Integer.valueOf(requestParameters.pageIndex)
    def viewSize = Integer.valueOf(requestParameters.limit)

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)

    def entityCondition = EntityCondition.makeCondition("productStoreId", productStoreId)

    def result = dispatcher.runSync("performFindPage", [
            inputFields        : requestParameters,
            entityName         : "BizFileUpload",
            entityConditionList: entityCondition,
            orderBy            : "-createdDate",
            viewIndex          : viewIndex,
            viewSize           : viewSize,
            timeZone           : timeZone
    ])

    def tempList = []
    result.list.each { fileUpload ->
        Map temp = [:]
        def fileUploadItemInfoList = []
//        def fileUploadId = fileUpload.fileUploadId
//
//        def fileUploadItemList = ExtEntityUtil.findList(delegator, "BizFileUploadItem", [fileUploadId: fileUploadId])
//        for (GenericValue fileUploadItem : fileUploadItemList) {
//            def skuTitle = fileUploadItem.get("skuTitle")
//            def enumeration = ExtEntityUtil.findOneCache(delegator, "Enumeration", [enumId: "SKU_" + skuTitle])
//            fileUploadItemInfoList.add(((enumeration?.description) ?: skuTitle) + "*" + fileUploadItem.getBigDecimal("quantity").intValue())
//        }

        temp.putAll(fileUpload)
//        temp.fileUploadItemInfo = StringUtils.join(fileUploadItemInfoList, ",")

        tempList.add(temp)
    }
    result.put("list", tempList)

    request.setAttribute("result", result)

    return "success"
}

public String saveFileUpload() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def fileUploadId = requestParameters.fileUploadId

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)
    def serviceName = (UtilValidate.isWhitespace(fileUploadId)) ? "bizCreateFileUpload" : "bizUpdateFileUpload"

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext(serviceName, "IN", requestParameters)
    serviceCtx.productStoreId = productStoreId
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync(serviceName, serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String deleteFileUpload() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def ids = requestParameters.get("ids")

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)

    if (UtilValidate.isNotEmpty(ids)) {
        if (ids instanceof String) ids = [ids]
        for (def id : ids) {
            def results = dispatcher.runSync("bizDeleteFileUpload", [
                    fileUploadId       : id,
                    productStoreId: productStoreId,
                    userLogin     : userLogin
            ])
            if (!ServiceUtil.isSuccess(results)) return MessageUtil.handleServiceResults(request, results)
        }
    }

    return "success"
}
