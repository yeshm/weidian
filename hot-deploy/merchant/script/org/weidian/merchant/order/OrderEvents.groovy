package org.miniofbiz.order.order

import com.weidian.open.sdk.DefaultWeidianClient
import com.weidian.open.sdk.request.order.VdianOrderListGetRequest
import com.weidian.open.sdk.response.order.ListOrder
import com.weidian.open.sdk.response.order.VdianOrderListGetResponse
import org.apache.commons.lang.StringUtils
import org.ofbiz.base.util.Debug
import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.util.EntityUtil
import org.ofbiz.ext.util.*
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil
import org.weidian.constant.order.OrderStatus
import org.weidian.merchant.MerchantWorker

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public String gridOrder() {
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
            entityName         : "BizOrder",
            entityConditionList: entityCondition,
            orderBy            : "-orderDate",
            viewIndex          : viewIndex,
            viewSize           : viewSize,
            timeZone           : timeZone
    ])

    def tempList = []
    result.list.each { order ->
        Map temp = [:]
        def orderItemInfoList = []
        def orderId = order.orderId
        def agentId = order.agentId

        def orderItemList = ExtEntityUtil.findList(delegator, "BizOrderItem", [orderId: orderId])
        for (GenericValue orderItem : orderItemList) {
            def skuMerchantCode = orderItem.skuMerchantCode
            def enumeration = ExtEntityUtil.findOneCache(delegator, "Enumeration", [enumId: "SKU_" + skuMerchantCode])
            orderItemInfoList.add(((enumeration?.description) ?: skuMerchantCode) + "*" + orderItem.getBigDecimal("quantity").intValue())
        }

        temp.putAll(order)
        temp.orderItemInfo = StringUtils.join(orderItemInfoList, "  ")
        temp.statusDesc = OrderStatus.getDesc(order.statusId)
        temp.buyerFullAddress = order.buyerName + " " + order.buyerPhone + " " + order.buyerAddress

        if (UtilValidate.isNotEmpty(agentId)) {
            def agent = ExtEntityUtil.findOneCache(delegator, "BizAgent", [agentId: agentId])
            temp.agentName = agent.name
        }

        tempList.add(temp)
    }
    result.put("list", tempList)

    request.setAttribute("result", result)

    return "success"
}

public String syncWeiDianOrder() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def ge_createdDate = requestParameters.ge_createdDate
    def le_createdDate = requestParameters.le_createdDate

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)

    def weiDian = ExtEntityUtil.findOne(delegator, "BizWeiDian", [productStoreId: productStoreId])
    def accessToken = weiDian.accessToken

    def totalNum = Integer.MAX_VALUE
    VdianOrderListGetRequest vdianOrderListGetRequest = new VdianOrderListGetRequest(accessToken)
    vdianOrderListGetRequest.setAddStart(ge_createdDate + " 00:00:00")
    vdianOrderListGetRequest.setAddEnd(le_createdDate + " 23:59:59")

    while (totalNum > vdianOrderListGetRequest.getPageSize() * (vdianOrderListGetRequest.getPageNum() - 1)) {
        VdianOrderListGetResponse response = DefaultWeidianClient.getInstance().executePost(vdianOrderListGetRequest)
        if (response.getStatus().getStatusCode() != 0) {
            return MessageUtil.returnError(request, response.getStatus().getStatusReason())
        }

        VdianOrderListGetResponse.VdianOrderListGetResult result = response.getResult()
        ListOrder[] orders = result.getOrders()
        //订单状态编号
//        0：下单
//        1：未付款
//        2：已付款
//        3：已发货
//        7：退款
//        8：订单关闭

        if (UtilValidate.isNotEmpty(orders)) {
            for (def order : orders) {
                order.getOrderId()
                Debug.logInfo("sync order:%s", "", AppUtil.toJson(order))

                def results = dispatcher.runSync("bizSyncWeiDianOrder", [
                        productStoreId: productStoreId,
                        weiDianOrder  : order,
                        userLogin     : userLogin
                ])
                if (!ServiceUtil.isSuccess(results)) return MessageUtil.handleServiceResults(request, results)
            }
        }

        totalNum = response.getResult().totalNum
        vdianOrderListGetRequest.setPageNum(vdianOrderListGetRequest.getPageNum() + 1)
    }

    return "success"
}

public String updateOrder() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)
    def serviceName = "bizUpdateOrder"

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext(serviceName, "IN", requestParameters)
    serviceCtx.productStoreId = productStoreId
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync(serviceName, serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String createManualOrder() {
    HttpServletRequest request = request
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)
    def serviceName = "bizCreateManualOrder"

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext(serviceName, "IN", requestParameters)
    serviceCtx.productStoreId = productStoreId
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync(serviceName, serviceCtx)

    return MessageUtil.handleServiceResults(request, results)
}

public String exportPaidOrderDeliveryAddress() {
    LocalDispatcher dispatcher = dispatcher
    HttpServletRequest request = request
    HttpServletResponse response = response
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)
    def inputFields = requestParameters

    inputFields.eq_statusId = OrderStatus.PAY

    def inputEntityCondition = EntityConditionWorker.createConditionFromInputFields(delegator, "BizOrder", requestParameters)
    def entityConditionList = [
            EntityCondition.makeCondition("productStoreId", productStoreId)
    ]
    if (UtilValidate.isNotEmpty(inputEntityCondition)) {
        entityConditionList.add(inputEntityCondition)
    }

    def orderList = ExtEntityUtil.findListSorted(delegator, "BizOrder", EntityCondition.makeCondition(entityConditionList), ["orderDate"])

    def excelHeaderList = ["业务单号", "寄件单位", "寄件人姓名", "寄件人电话", "寄件人手机", "寄件人省", "寄件人市", "寄件区/县", "寄件人地址", "寄件人邮编", "收件人姓名", "收件人电话", "收件人手机", "收件人地址", "收件邮政编码", "运费", "订单金额", "商品名称", "商品编码", "销售属性", "商品金额", "数量", "备注"]
    def bodyList = []
    int totalCount = orderList.size() //数据总数量
    int executeCount = 0 //当前处理的数量
    def availableCount = 0 //可导出的数量

    EntityCondition entityCondition = EntityCondition.makeCondition([
            EntityCondition.makeCondition("productStoreId", productStoreId),
            EntityUtil.getFilterByDateExpr()
    ])
    def defaultSenderInfo = ExtEntityUtil.getOnly(delegator, "BizDefaultSenderInfo", entityCondition)

    for (def order : orderList) {
        executeCount++
        def dataList = []

        def orderItemInfoList = []
        def orderId = order.orderId
        def agentId = order.agentId

        if (UtilValidate.isEmpty(order.buyerAddress)) {
            Debug.logError("收货地址为空, 不能导出", "")
        }

        def orderItemList = ExtEntityUtil.findList(delegator, "BizOrderItem", [orderId: orderId])
        for (GenericValue orderItem : orderItemList) {
            def skuMerchantCode = orderItem.skuMerchantCode
            def enumeration = ExtEntityUtil.findOneCache(delegator, "Enumeration", [enumId: "SKU_" + skuMerchantCode])
            orderItemInfoList.add(((enumeration?.description) ?: skuMerchantCode) + "*" + orderItem.getBigDecimal("quantity").intValue())
        }
        def orderItemInfo = StringUtils.join(orderItemInfoList, "  ")

        def agent = ExtEntityUtil.findOneCache(delegator, "BizAgent", [agentId: agentId])

        //"业务单号", "寄件单位"
        dataList.add(order.externalId)
        dataList.add("")

        //代理信息 "寄件人姓名", "寄件人电话", "寄件人手机"
        if (UtilValidate.isEmpty(agent)) {
            dataList.add(defaultSenderInfo.senderName)
            dataList.add("")
            dataList.add(defaultSenderInfo.senderPhone)
        } else {
            dataList.add(agent.name)
            dataList.add("")
            dataList.add(agent.phone)
        }

        //"寄件人省", "寄件人市", "寄件区/县", "寄件人地址", "寄件人邮编"
        dataList.add(defaultSenderInfo.senderProvince)
        dataList.add(defaultSenderInfo.senderCity)
        dataList.add(defaultSenderInfo.senderRegion)
        dataList.add(defaultSenderInfo.senderAddress)
        dataList.add("")    //dataList.add(defaultSenderInfo.senderPost)

        //"收件人姓名", "收件人电话", "收件人手机", "收件人地址", "收件邮政编码",
        dataList.add(order.buyerName)
        dataList.add("")
        dataList.add(order.buyerPhone)
        dataList.add(order.buyerAddress)
        dataList.add("")    //dataList.add(order.buyerPost)

        //"运费", "订单金额", "商品名称", "商品编码", "销售属性", "商品金额", "数量", "备注"
        dataList.add("")
        dataList.add("")    //dataList.add(order.totalPrice)
        dataList.add("")
        dataList.add("")
        dataList.add(orderItemInfo)
        dataList.add("")    //dataList.add(order.price)
        dataList.add("")    //dataList.add(order.totalQuantity)
        dataList.add("")

        availableCount++
        bodyList.add(dataList)
    }

    def fileName = "微店待发货订单" + ExtUtilDateTime.formatDate2String(new Date(), "yyyyMMddHHmmss")

    ExcelUtil.createExcelFileOutputResponse(fileName, "Sheet1", excelHeaderList, bodyList, true, response)

    return "success"
}
