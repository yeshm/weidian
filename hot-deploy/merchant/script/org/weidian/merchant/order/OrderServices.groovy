package org.miniofbiz.order.order

import com.weidian.open.sdk.DefaultWeidianClient
import com.weidian.open.sdk.request.order.VdianOrderGetRequest
import com.weidian.open.sdk.response.order.ListOrder
import com.weidian.open.sdk.response.order.VdianOrderGetResponse
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.ExtUtilDateTime
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil
import org.weidian.constant.order.OrderStatus
import org.weidian.constant.order.OrderType

public Map updateOrder() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def productStoreId = parameters.productStoreId
    def orderId = parameters.orderId
    def agentId = parameters.agentId
    def buyerProvince = parameters.buyerProvince
    def buyerCity = parameters.buyerCity
    def buyerRegion = parameters.buyerRegion
    def buyerSelfAddress = parameters.buyerSelfAddress
    def buyerPost = parameters.buyerPost
    def buyerName = parameters.buyerName
    def buyerPhone = parameters.buyerPhone

    def now = UtilDateTime.nowTimestamp()
    def results = ServiceUtil.returnSuccess()

    def order = ExtEntityUtil.getOnly(delegator, "BizOrder", [
            orderId       : orderId,
            productStoreId: productStoreId,
    ])

    if (!UtilValidate.areEqual(order.agentId, agentId)) {
        order.agentId = agentId
        order.lastModifiedDate = now
        order.lastModifiedByUserLogin = userLogin.userLoginId

        order.store()
    }

    if (!UtilValidate.areEqual(order.buyerProvince, buyerProvince)
            || !UtilValidate.areEqual(order.buyerCity, buyerCity)
            || !UtilValidate.areEqual(order.buyerRegion, buyerRegion)
            || !UtilValidate.areEqual(order.buyerSelfAddress, buyerSelfAddress)
            || !UtilValidate.areEqual(order.buyerPost, buyerPost)
            || !UtilValidate.areEqual(order.buyerName, buyerName)
            || !UtilValidate.areEqual(order.buyerPhone, buyerPhone)) {
        order.buyerProvince = buyerProvince
        order.buyerCity = buyerCity
        order.buyerRegion = buyerRegion
        order.buyerSelfAddress = buyerSelfAddress
        order.buyerPost = buyerPost
        order.buyerName = buyerName
        order.buyerPhone = buyerPhone

        order.buyerAddress = buyerProvince + " " + buyerCity + " " + buyerRegion + " " + buyerSelfAddress

        order.lastModifiedDate = now
        order.lastModifiedByUserLogin = userLogin.userLoginId

        order.store()
    }

    results.orderId = order.orderId
    return results
}

public Map createOrderStatus() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def orderId = parameters.orderId
    def nowTimestamp = UtilDateTime.nowTimestamp()

    def orderStatus = delegator.makeValue("BizOrderStatus")
    orderStatus.orderId = orderId
    orderStatus.setNonPKFields(parameters)
    orderStatus.statusDatetime = nowTimestamp
    orderStatus.statusUserLogin = userLogin.userLoginId
    orderStatus.setNextSeqId()
    orderStatus.create()

    def results = ServiceUtil.returnSuccess()
    results.orderStatusId = orderStatus.orderStatusId
    return results
}

public Map createManualOrder() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def productStoreId = parameters.productStoreId
    def buyerProvince = parameters.buyerProvince
    def buyerCity = parameters.buyerCity
    def buyerRegion = parameters.buyerRegion
    def buyerAddress = parameters.buyerAddress
    def buyerPost = parameters.buyerPost
    def buyerName = parameters.buyerName
    def buyerPhone = parameters.buyerPhone
    def skuMerchantCode = parameters.skuMerchantCode
    def totalQuantity = parameters.totalQuantity
    def price = parameters.price
    def agentId = parameters.agentId

    def now = UtilDateTime.nowTimestamp()

    if (UtilValidate.isWhitespace(skuMerchantCode)) {
        return ServiceUtil.returnError("请选择商品")
    }

    def order = delegator.makeValue("BizOrder", [
            productStoreId         : productStoreId,
            orderTypeId            : OrderType.MANUAL,
            orderDate              : now,
            statusId               : OrderStatus.PAY,
            createdBy              : userLogin.userLoginId,
            price                  : price * totalQuantity,
            totalPrice             : price * totalQuantity,
            discountAmount         : BigDecimal.ZERO,
            expressType            : "",
            expressNo              : "",

            buyerProvince          : buyerProvince,
            buyerCity              : buyerCity,
            buyerRegion            : buyerRegion,
            buyerSelfAddress       : buyerAddress,
            buyerAddress           : buyerProvince + " " + buyerCity + " " + buyerRegion + " " + buyerAddress,
            buyerPost              : buyerPost,
            buyerName              : buyerName,
            buyerPhone             : buyerPhone,
            buyerNote              : "",

            sellerNote             : "",

            createdDate            : now,
            createdByUserLogin     : userLogin.userLoginId,
            lastModifiedDate       : now,
            lastModifiedByUserLogin: userLogin.userLoginId
    ])
    order.setNextSeqId()
    order.externalId = order.orderId
    order.totalQuantity = totalQuantity
    order.agentId = agentId
    order.create()

    def skuEnumeration = ExtEntityUtil.getOnly(delegator, "Enumeration", [
            enumCode  : skuMerchantCode,
            enumTypeId: "SKU_NAME"
    ])

    def orderItem = delegator.makeValue("BizOrderItem", [
            orderId        : order.orderId,
            externalId     : order.orderId,
            productId      : "",
            quantity       : totalQuantity,
            unitPrice      : price,
            itemDescription: skuEnumeration.description,
            skuId          : "",
            skuTitle       : "",
            skuMerchantCode: skuMerchantCode,
            productCode    : "",
            url            : "",
            imgUrl         : ""
    ])
    delegator.setNextSubSeqId(orderItem, "orderItemSeqId", 5, 1)
    orderItem.create()

    def results = dispatcher.runSync("bizCreateOrderStatus", [
            orderId  : order.get("orderId"),
            statusId : order.get("statusId"),
            userLogin: userLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    results = ServiceUtil.returnSuccess()
    results.orderId = order.orderId
    return results
}

public Map syncWeiDianOrder() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def productStoreId = parameters.productStoreId
    ListOrder weiDianOrder = parameters.weiDianOrder

    def weiDianOrderId = weiDianOrder.getOrderId()

    def order = ExtEntityUtil.getOnly(delegator, "BizOrder", [
            productStoreId: productStoreId,
            orderTypeId   : OrderType.WEI_DIAN,
            externalId    : weiDianOrderId
    ])
    def now = UtilDateTime.nowTimestamp()

    if (UtilValidate.isEmpty(order)) {
        //新建订单
        def weiDian = ExtEntityUtil.findOne(delegator, "BizWeiDian", [productStoreId: productStoreId])
        def accessToken = weiDian.accessToken

        VdianOrderGetResponse response = DefaultWeidianClient.getInstance().executePost(new VdianOrderGetRequest(accessToken, weiDianOrderId))
        VdianOrderGetResponse.VdianOrderGetResult weiDianOrderDetail = response.getResult()

        order = delegator.makeValue("BizOrder", [
                productStoreId         : productStoreId,
                orderTypeId            : OrderType.WEI_DIAN,
                externalId             : weiDianOrderId,
                orderDate              : ExtUtilDateTime.parseTimestamp(weiDianOrder.getTime()),
                statusId               : OrderStatus.weiDianStatusToStatusId(weiDianOrder.getStatus()),
                createdBy              : userLogin.userLoginId,
                price                  : new BigDecimal(weiDianOrderDetail.getPrice()),
                totalPrice             : new BigDecimal(weiDianOrderDetail.getTotal()),
                discountAmount         : new BigDecimal(weiDianOrderDetail.getDiscountAmount()),
                expressType            : weiDianOrderDetail.getExpressType(),
                expressNo              : weiDianOrderDetail.getExpressNo(),

                buyerSelfAddress       : weiDianOrderDetail.getBuyerInfo().getSelfAddress(),
                buyerCity              : weiDianOrderDetail.getBuyerInfo().getCity(),
                buyerName              : weiDianOrderDetail.getBuyerInfo().getName(),
                buyerProvince          : weiDianOrderDetail.getBuyerInfo().getProvince(),
                buyerAddress           : weiDianOrderDetail.getBuyerInfo().getAddress(),
                buyerRegion            : weiDianOrderDetail.getBuyerInfo().getRegion(),
                buyerPost              : weiDianOrderDetail.getBuyerInfo().getPost(),
                buyerPhone             : weiDianOrderDetail.getBuyerInfo().getPhone(),
                buyerNote              : weiDianOrderDetail.getNote(),

                sellerNote             : weiDianOrder.getSellerNote(),

                createdDate            : now,
                createdByUserLogin     : userLogin.userLoginId,
                lastModifiedDate       : now,
                lastModifiedByUserLogin: userLogin.userLoginId
        ])
        order.setNextSeqId()
        order.create()

        def totalQuantity = BigDecimal.ZERO
        for (VdianOrderGetResponse.OrderItem weiDianOrderItem : weiDianOrderDetail.getItems()) {
            def orderItem = delegator.makeValue("BizOrderItem", [
                    orderId        : order.get("orderId"),
                    externalId     : weiDianOrderId,
                    productId      : weiDianOrderItem.getItemId(),
                    quantity       : new BigDecimal(weiDianOrderItem.getQuantity()),
                    unitPrice      : new BigDecimal(weiDianOrderItem.getPrice()),
                    itemDescription: weiDianOrderItem.getItemName(),
                    skuId          : weiDianOrderItem.getSkuId(),
                    skuTitle       : weiDianOrderItem.getSkuTitle(),
                    skuMerchantCode: weiDianOrderItem.getSkuMerchantCode(),
                    productCode    : weiDianOrderItem.getMerchantCode(),
                    url            : weiDianOrderItem.getUrl(),
                    imgUrl         : weiDianOrderItem.getImg()
            ])
            delegator.setNextSubSeqId(orderItem, "orderItemSeqId", 5, 1)
            orderItem.create()

            totalQuantity = totalQuantity.add(orderItem.getBigDecimal("quantity"))
        }

        order.totalQuantity = totalQuantity
        order.store()

        def results = dispatcher.runSync("bizCreateOrderStatus", [
                orderId  : order.get("orderId"),
                statusId : order.get("statusId"),
                userLogin: userLogin
        ])
        if (!ServiceUtil.isSuccess(results)) return results
    } else {
        //看看订单是否有信息变动
        def statusId = OrderStatus.weiDianStatusToStatusId(weiDianOrder.getStatus())
        def sellerNote = weiDianOrder.getSellerNote()

        if (!UtilValidate.areEqual(statusId, order.statusId)) {
            //状态有变动
            order.statusId = statusId
            order.expressType = weiDianOrder.getExpressType()
            order.expressNo = weiDianOrder.getExpressNo()
            order.store()

            def results = dispatcher.runSync("bizCreateOrderStatus", [
                    orderId  : order.orderId,
                    statusId : order.statusId,
                    userLogin: userLogin
            ])
            if (!ServiceUtil.isSuccess(results)) return results
        }

        if (!UtilValidate.areEqual(sellerNote, order.sellerNote)) {
            order.sellerNote = sellerNote
            order.store()
        }
    }

    def results = ServiceUtil.returnSuccess()
    results.orderId = order.get("orderId")
    return results
}