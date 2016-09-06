package org.weidian.merchant;

import javolution.util.FastMap;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.ext.util.ExtEntityUtil;
import org.weidian.BusinessWorker;
import org.weidian.SessionKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

public class MerchantWorker extends BusinessWorker {
    /**
     * 获取当前管理的公众号id
     */
    public static String getCurrentProductStoreId(HttpServletRequest request) throws GenericEntityException {
        HttpSession session = request.getSession(false);
        String productStoreId = (String) session.getAttribute(SessionKey.MERCHANT_PRODUCT_STORE_ID);

        if (UtilValidate.isWhitespace(productStoreId)) {
            GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

            if (UtilValidate.isNotEmpty(userLogin.get("productStoreId"))) {
                productStoreId = userLogin.getString("productStoreId");
                session.setAttribute(SessionKey.MERCHANT_PRODUCT_STORE_ID, productStoreId);
            }
        }

        return productStoreId;
    }

    public static boolean isMerchant(HttpServletRequest request) throws GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String productStoreId = getCurrentProductStoreId(request);

        return "8000".equals(productStoreId);
    }

    /**
     * 获取当前管理的公众号
     */
    public static GenericValue getCurrentPublicAccount(HttpServletRequest request) throws GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String productStoreId = getCurrentProductStoreId(request);

        GenericValue publicAccount = ExtEntityUtil.findOneCache(delegator, "BizPublicAccount", UtilMisc.toMap("productStoreId", productStoreId));

        return publicAccount;
    }

    /**
     * 获取当前管理的商家partyId
     */
    //TODO yeshm 使用bizMerchantPartyId
    public static String getCurrentMerchantPartyId(HttpServletRequest request) throws GenericEntityException {
        HttpSession session = request.getSession(false);
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        return UtilValidate.isEmpty(userLogin) ? null : userLogin.getString("partyId");
    }

    /**
     * 获取当前管理的店铺实体
     */
    public static GenericValue getCurrentProductStore(HttpServletRequest request) throws GenericEntityException {
        HttpSession session = request.getSession(false);

        GenericValue productStore = (GenericValue) session.getAttribute(SessionKey.MERCHANT_PRODUCT_STORE);

        if (UtilValidate.isEmpty(productStore)) {
            Delegator delegator = (Delegator) request.getAttribute("delegator");

            productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", getCurrentProductStoreId(request)), true);
            session.setAttribute(SessionKey.MERCHANT_PRODUCT_STORE, productStore);
        }

        return productStore;
    }

    /**
     * 获取当前店铺的主目录id
     */
    public static String getMainProdCatalogId(HttpServletRequest request) throws GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String productStoreId = getCurrentProductStoreId(request);

        EntityCondition entityCondition = EntityCondition.makeCondition(UtilMisc.toList(
                EntityCondition.makeCondition("productStoreId", productStoreId),
                EntityUtil.getFilterByDateExpr()
        ));

        GenericValue productStoreCatalog = ExtEntityUtil.getOnlyCache(delegator, "ProductStoreCatalog", entityCondition);
        return productStoreCatalog.getString("prodCatalogId");
    }

    /**
     * 获取当前店铺的商品属性分类根节点id
     */
    public static String getRootProductAttrCategoryProductCategoryId(HttpServletRequest request) throws GenericEntityException {
        return getRootProductCategoryId(request, "BIZ_ATTR_CATE_ROOT");
    }

    /**
     * 获取当前店铺的商品使用分类根节点id
     */
    public static String getRootProductUsageCategoryProductCategoryId(HttpServletRequest request) throws GenericEntityException {
        return getRootProductCategoryId(request, "BIZ_USAGE_CATE_ROOT");
    }

    /**
     * 获取当前店铺的商品品牌根节点id
     */
    public static String getRootProductBrandProductCategoryId(HttpServletRequest request) throws GenericEntityException {
        return getRootProductCategoryId(request, "BIZ_BRAND_ROOT");
    }

    /**
     * 获取当前店铺的商品分组根根节点id
     */
    public static String getRootProductGroupProductCategoryId(HttpServletRequest request) throws GenericEntityException {
        return getRootProductCategoryId(request, "BIZ_GROUP_ROOT");
    }

    /**
     * 获取当前店铺的礼品属性分类根节点id
     */
    public static String getRootGiftAttrCategoryProductCategoryId(HttpServletRequest request) throws GenericEntityException {
        return getRootProductCategoryId(request, "BIZ_GIFT_ATTR_ROOT");
    }

    /**
     * 获取当前店铺的礼品使用分类根节点id
     */
    public static String getRootGiftUsageCategoryProductCategoryId(HttpServletRequest request) throws GenericEntityException {
        return getRootProductCategoryId(request, "BIZ_GIFT_USAGE_ROOT");
    }

    /**
     * 获取当前店铺的礼品品牌根节点id
     */
    public static String getRootGiftBrandProductCategoryId(HttpServletRequest request) throws GenericEntityException {
        return getRootProductCategoryId(request, "BIZ_GIFT_BRAND_ROOT");
    }

    /**
     * 根据类型获取分类根节点id
     */
    private static String getRootProductCategoryId(HttpServletRequest request, String prodCatalogCategoryTypeId) throws GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String prodCatalogId = getMainProdCatalogId(request);

        EntityCondition entityCondition = EntityCondition.makeCondition(UtilMisc.toList(
                EntityCondition.makeCondition("prodCatalogId", prodCatalogId),
                EntityCondition.makeCondition("prodCatalogCategoryTypeId", prodCatalogCategoryTypeId),
                EntityUtil.getFilterByDateExpr()
        ));

        GenericValue productBrand = ExtEntityUtil.getOnlyCache(delegator, "ProdCatalogCategory", entityCondition);
        return productBrand.getString("productCategoryId");
    }

    public static GenericValue getProductProductFeatureCategoryDataResource(HttpServletRequest request, String productId, String productFeatureCategoryId) throws GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Map map = FastMap.newInstance();
        map.put("productStoreId", MerchantWorker.getCurrentProductStoreId(request));
        map.put("productId", productId);
        map.put("productFeatureCategoryId", productFeatureCategoryId);

        GenericValue productProductFeatureDataResource = delegator.findOne("BizProductCategoryDataResource", map, true);

        if (UtilValidate.isNotEmpty(productProductFeatureDataResource)) {
            String dataResourceId = productProductFeatureDataResource.getString("dataResourceId");
            GenericValue dataResource = delegator.findOne("DataResource", UtilMisc.toMap("dataResourceId", dataResourceId), true);
            return dataResource;
        }
        return null;
    }

    /**
     * 获取授信额度
     */
    public static BigDecimal getCurrentAuthorizeAmount(HttpServletRequest request) throws GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String merchantPartyId = getCurrentMerchantPartyId(request);

        GenericValue merchantAuthorize = ExtEntityUtil.getOnly(delegator, "BizMerchantRechargeAuthorize", EntityCondition.makeCondition(
                EntityCondition.makeCondition("merchantPartyId", merchantPartyId),
                ExtEntityUtil.getFilterByDateExpr()
        ));
        return merchantAuthorize == null ? BigDecimal.ZERO : merchantAuthorize.getBigDecimal("amount");
    }

    /**
     * 根据vipMemberPartyId获取公众号的openId
     */
    public static String getWeixinOpenIdByCustomerPartyId(Delegator delegator, String customerPartyId, String productStoreId) throws GenericEntityException {
        EntityCondition entityCondition = EntityCondition.makeCondition(UtilMisc.toList(
                EntityCondition.makeCondition("partyId", customerPartyId),
                EntityCondition.makeCondition("productStoreId", productStoreId)
        ));

        GenericValue publicAccountUser = ExtEntityUtil.getOnlyCache(delegator, "BizPublicAccountUser", entityCondition);
        return UtilValidate.isEmpty(publicAccountUser) ? null : publicAccountUser.getString("weixinOpenId");
    }

    /**
     * 根据customPartyId获取公众号的openId
     */
    public static String getWeixinOpenIdByVipMemberPartyId(Delegator delegator, HttpServletRequest request, String vipMemberPartyId, String productStoreId) throws GenericEntityException {

        String customerPartyId = getCustomerPartyIdByVipMemberPartyIdAndPsid(delegator, vipMemberPartyId, productStoreId);

        if (Debug.infoOn())
            Debug.logInfo("[getCustomerPartyIdByVipMemberPartyIdAndPsid vipMemberPartyId=" + vipMemberPartyId + ",productStoreId=" + productStoreId + " result:customerPartyId=" + customerPartyId + "]", "MerchantWoker");

        if (UtilValidate.isEmpty(customerPartyId)) return null;

        String weixinOpenId = getWeixinOpenIdByCustomerPartyId(delegator, customerPartyId, getCurrentProductStoreId(request));

        return weixinOpenId;
    }

    /**
     * 根据会员PartyId和店铺id获取会员对应的customPartyId
     */
    public static String getCustomerPartyIdByVipMemberPartyIdAndPsid(Delegator delegator, String vipMemberPartyId, String productStoreId) throws GenericEntityException {
        EntityCondition entityCondition = EntityCondition.makeCondition(UtilMisc.toList(
                EntityCondition.makeCondition("vipMemberPartyId", vipMemberPartyId),
                EntityCondition.makeCondition("productStoreId", productStoreId),
                EntityUtil.getFilterByDateExpr()
        ));

        GenericValue vipMemberPartyProductStore = ExtEntityUtil.getFirstCache(delegator, "BizVipMemberPartyProductStore", entityCondition);
        return UtilValidate.isEmpty(vipMemberPartyProductStore) ? null : vipMemberPartyProductStore.getString("customerPartyId");
    }

    /**
     * 获取当前店铺的的微信号
     */
    public static String getCurrentPublicAlias(HttpServletRequest request) throws GenericEntityException {
        HttpSession session = request.getSession();

        String publicAlias = (String) session.getAttribute(SessionKey.MERCHANT_PUBLIC_ALIAS);

        if (UtilValidate.isWhitespace(publicAlias)) {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            String productStoreId = getCurrentProductStoreId(request);

            GenericValue publicAccountOauth = delegator.findOne("BizPublicAccountOauth", UtilMisc.toMap("productStoreId", productStoreId), false);

            if (UtilValidate.isNotEmpty(publicAccountOauth)) {
                publicAlias = publicAccountOauth.getString("publicAccountAlias");
                session.setAttribute(SessionKey.MERCHANT_PUBLIC_ALIAS, publicAlias);
            }
        }

        return publicAlias;
    }


    //根据请求获取经销商名字
    public static String getDealerName(HttpServletRequest request, String dealerPartyId) throws GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        return getPersonFirstNameByPartyId(dealerPartyId, delegator);
    }

    public static List<String> findDates(Date dBegin, Date dEnd) {

        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        while (dEnd.after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }
}