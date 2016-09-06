package org.weidian.admin

import com.juweitu.bonn.constant.common.SystemConfigTypeEnum
import com.juweitu.bonn.constant.party.PartyRelationshipType
import com.juweitu.bonn.constant.party.RoleType
import com.juweitu.bonn.constant.party.SecurityGroup
import com.juweitu.bonn.constant.product.ProductPrefixEnum
import com.juweitu.bonn.constant.product.WebSiteTypeEnum
import com.juweitu.bonn.constant.shipment.ShipmentMethodTypeEnum
import org.apache.commons.lang.RandomStringUtils
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.biz.ConstantParameter
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.SecurityUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil
import org.weidian.constant.party.SecurityGroup

public Map initAdminData() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def partyId = parameters.partyId
    def productStoreId = parameters.productStoreId

    def systemUserLogin = SecurityUtil.getSystemUserLogin(delegator)

    def publicAccount = ExtEntityUtil.getOnlyCache(delegator, "BizPublicAccount", [productStoreId: productStoreId])
    if (UtilValidate.isNotEmpty(publicAccount)) {
        return ServiceUtil.returnSuccess()
    }

    // 初始化公众账号
    def results = dispatcher.runSync("bizCreatePublicAccount", [
            partyId       : partyId,
            productStoreId: productStoreId,
            userLogin     : userLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    // 初始化系统签名密钥
    results = dispatcher.runSync("bizSaveSystemConfig", [
            configTypeEnumId: SystemConfigTypeEnum.SYSTEM_SIGN_KEY,
            configValue     : RandomStringUtils.randomAlphabetic(8),
            userLogin       : userLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    //初始化店铺主目录
    results = dispatcher.runSync("createProdCatalog", [
            prodCatalogId: ProductPrefixEnum.MAIN_CATALOG_ID_PREFIX + productStoreId,
            catalogName  : "店铺主目录",
            userLogin    : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results
    def prodCatalogId = results.get("prodCatalogId")

    results = dispatcher.runSync("createProductStoreCatalog", [
            prodCatalogId : prodCatalogId,
            productStoreId: productStoreId,
            fromDate      : UtilDateTime.nowTimestamp(),
            userLogin     : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    // 初始化商品属性分类根节点
    results = dispatcher.runSync("createProductCategory", [
            productCategoryId    : ProductPrefixEnum.ATTR_CATE_ROOT_ID_PREFIX + productStoreId,
            productCategoryTypeId: "CATALOG_CATEGORY",
            categoryName         : "属性分类根节点",
            userLogin            : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results
    def productCategoryId = results.get("productCategoryId")
    def productRootAttrCategoryId = productCategoryId

    results = dispatcher.runSync("addProductCategoryToProdCatalog", [
            prodCatalogId            : prodCatalogId,
            productCategoryId        : productCategoryId,
            prodCatalogCategoryTypeId: "BIZ_ATTR_CATE_ROOT",
            fromDate                 : UtilDateTime.nowTimestamp(),
            userLogin                : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    // 初始化商品使用分类根节点
    results = dispatcher.runSync("createProductCategory", [
            productCategoryId    : ProductPrefixEnum.USAGE_CATE_ROOT_ID_PREFIX + productStoreId,
            productCategoryTypeId: "CATALOG_CATEGORY",
            categoryName         : "使用分类根节点",
            userLogin            : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results
    productCategoryId = results.get("productCategoryId")

    results = dispatcher.runSync("addProductCategoryToProdCatalog", [
            prodCatalogId            : prodCatalogId,
            productCategoryId        : productCategoryId,
            prodCatalogCategoryTypeId: "BIZ_USAGE_CATE_ROOT",
            fromDate                 : UtilDateTime.nowTimestamp(),
            userLogin                : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    // 初始化商品品牌根节点
    results = dispatcher.runSync("createProductCategory", [
            productCategoryId    : ProductPrefixEnum.BRAND_ROOT_ID_PREFIX + productStoreId,
            productCategoryTypeId: "CATALOG_CATEGORY",
            categoryName         : "品牌根节点",
            userLogin            : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results
    def productBrandId = results.productCategoryId

    results = dispatcher.runSync("addProductCategoryToProdCatalog", [
            prodCatalogId            : prodCatalogId,
            productCategoryId        : productBrandId,
            prodCatalogCategoryTypeId: "BIZ_BRAND_ROOT",
            fromDate                 : UtilDateTime.nowTimestamp(),
            userLogin                : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    // 初始化商品分组根节点
    results = dispatcher.runSync("createProductCategory", [
            productCategoryId    : ProductPrefixEnum.GROUP_ROOT_ID_PREFIX + productStoreId,
            productCategoryTypeId: "CATALOG_CATEGORY",
            categoryName         : "分组根节点",
            userLogin            : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results
    def productGroupId = results.productCategoryId

    results = dispatcher.runSync("addProductCategoryToProdCatalog", [
            prodCatalogId            : prodCatalogId,
            productCategoryId        : productGroupId,
            prodCatalogCategoryTypeId: "BIZ_GROUP_ROOT",
            fromDate                 : UtilDateTime.nowTimestamp(),
            userLogin                : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    //PC商城网站
    results = dispatcher.runSync("createWebSite", [
            webSiteId        : delegator.getNextSeqId("WebSite"),
            siteName         : "",
            productStoreId   : productStoreId,
            webSiteTypeEnumId: WebSiteTypeEnum.PC,
            userLogin        : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    //初始化手机端网站站点
    results = dispatcher.runSync("createWebSite", [
            webSiteId        : delegator.getNextSeqId("WebSite"),
            siteName         : "",
            productStoreId   : productStoreId,
            webSiteTypeEnumId: WebSiteTypeEnum.MOBILE,
            userLogin        : systemUserLogin
    ])

    if (!ServiceUtil.isSuccess(results)) return results

    // 初始化角色
    def partyRole = ExtEntityUtil.findOne(delegator, "PartyRole", [
            partyId   : partyId,
            roleTypeId: "OWNER"
    ])
    if (UtilValidate.isEmpty(partyRole)) {
        results = dispatcher.runSync("createPartyRole", [
                partyId   : partyId,
                roleTypeId: "OWNER",
                userLogin : userLogin
        ])
        if (!ServiceUtil.isSuccess(results)) return results
    }

    // 初始化店铺角色
    results = dispatcher.runSync("createProductStoreRole", [
            partyId       : partyId,
            roleTypeId    : "OWNER",
            productStoreId: productStoreId,
            fromDate      : UtilDateTime.nowTimestamp(),
            userLogin     : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    results = dispatcher.runSync("addUserLoginToSecurityGroup", [
            userLoginId: userLogin.userLoginId,
            groupId    : SecurityGroup.MERCHANT_ADMIN,
            userLogin  : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    //Company与Company 也建立一个企业跟商家的关系
    results = dispatcher.runSync("createPartyRelationship", [
            partyIdFrom            : partyId,
            roleTypeIdFrom         : RoleType.NA,
            partyIdTo              : partyId,
            roleTypeIdTo           : RoleType.MERCHANT,
            partyId                : partyId,
            roleTypeId             : RoleType.MERCHANT,
            partyRelationshipTypeId: PartyRelationshipType.MERCHANT,
            userLogin              : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    //初始化物流相关数据
    for (String key in ShipmentMethodTypeEnum.getMap().keySet()) {
        results = dispatcher.runSync("createProductStoreShipMeth", [
                partyId             : ConstantParameter.NA,
                roleTypeId          : RoleType.CARRIER,
                productStoreId      : productStoreId,
                shipmentMethodTypeId: key,
                serviceName         : "bizCalcShipmentCostEstimateV1",
                userLogin           : systemUserLogin
        ])
        if (!ServiceUtil.isSuccess(results)) return results
    }

    //初始化店铺会员等级数据
    results = dispatcher.runSync("bizInitVipLevel", [
            productStoreId: productStoreId,
            userLogin     : userLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    // 初始化会员等级优惠数据
    results = dispatcher.runSync("bizInitVipLevelProductPromo", [
            productStoreId           : productStoreId,
            userLogin                : userLogin,
            productRootAttrCategoryId: productRootAttrCategoryId
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    //初始化店铺销售员等级数据
    results = dispatcher.runSync("bizInitDistributeSellerLevel", [
            productStoreId: productStoreId,
            userLogin     : userLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    //初始化默认关键词
    results = dispatcher.runSync("bizInitAppKeyWord", [
            productStoreId: productStoreId,
            userLogin     : userLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    //将服务类产品分组添加至产品类目
    results = dispatcher.runSync("addProductCategoryToProdCatalog", [
            prodCatalogId            : prodCatalogId,
            productCategoryId        : "PRODUCT_SERVICE",
            prodCatalogCategoryTypeId: "PCCT_BROWSE_ROOT",
            fromDate                 : UtilDateTime.nowTimestamp(),
            userLogin                : systemUserLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    results = dispatcher.runSync("bizInitVipNoticeSetting", [
            productStoreId: productStoreId,
            userLogin     : userLogin
    ])
    if (!ServiceUtil.isSuccess(results)) {
        return results
    }

    return ServiceUtil.returnSuccess()
}