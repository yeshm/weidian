package org.ofbiz.ext.product

import com.juweitu.bonn.bean.ProductWrapper
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.ExtUtilNumber
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map updateInventory() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def productId = parameters.productId
    def quantity = parameters.quantity
    def ownerPartyId = parameters.ownerPartyId
    def facilityId = parameters.facilityId
    def productStoreId = parameters.productStoreId

    def inventoryItemTypeId = "NON_SERIAL_INV_ITEM"
    def inventoryItemId
    def fields = [
            productId          : productId,
            facilityId         : facilityId,
            inventoryItemTypeId: inventoryItemTypeId
    ]

    if (!UtilValidate.isWhitespace(ownerPartyId)) {
        fields.ownerPartyId = ownerPartyId
    }

    def inventoryItem = ExtEntityUtil.getOnlyCache(delegator, "InventoryItem", fields)

    if (UtilValidate.isEmpty(inventoryItem)) {
        def result = dispatcher.runSync("createInventoryItem", [
                productId          : productId,
                facilityId         : facilityId,
                inventoryItemTypeId: inventoryItemTypeId,
                userLogin          : userLogin
        ])
        if (!ServiceUtil.isSuccess(result)) return result

        inventoryItemId = result.inventoryItemId
    } else {
        inventoryItemId = inventoryItem.inventoryItemId
    }

    def result = dispatcher.runSync("getProductInventoryAvailable", [productId: productId])
    //def availableToPromiseTotal = result.availableToPromiseTotal
    def quantityOnHandTotal = result.quantityOnHandTotal

    def quantityDiff = ExtUtilNumber.subtract(quantity, quantityOnHandTotal)

    if (quantityDiff != 0) {
        result = dispatcher.runSync("createInventoryItemDetail", [
                inventoryItemId       : inventoryItemId,
                quantityOnHandDiff    : quantityDiff,
                availableToPromiseDiff: quantityDiff,
                userLogin             : userLogin
        ])

        if (!ServiceUtil.isSuccess(result)) return result
    }

    //同步更新父产品库存
    def productWrapper = new ProductWrapper(delegator, dispatcher, null, productId, null)
    if (productWrapper.isVariant()) {
        inventoryItem = ExtEntityUtil.getFirstCache(delegator, "InventoryItem", [
                productId          : productWrapper.getParentProductId(),
                facilityId         : facilityId,
                inventoryItemTypeId: inventoryItemTypeId
        ])

        result = dispatcher.runSync("createInventoryItemDetail", [
                inventoryItemId       : inventoryItem.inventoryItemId,
                quantityOnHandDiff    : quantityDiff,
                availableToPromiseDiff: quantityDiff,
                userLogin             : userLogin
        ])

        if (!ServiceUtil.isSuccess(result)) return result
    }

    result = dispatcher.runSync("bizUpdateProductQuantity", [
            productId             : productId,
            availableToPromiseDiff: quantityDiff,
            productStoreId        : productStoreId,
            userLogin             : userLogin
    ])

    if (!ServiceUtil.isSuccess(result)) return result
    return ServiceUtil.returnSuccess()
}