package org.weidian;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.ext.util.ExtEntityUtil;

import java.util.List;

/**
 * 系统业务处理助手类
 */
public class BusinessWorker {


    /**
     * 根据productStoreId获取partyId
     */
    public static String getOwnerPartyIdByProductStoreId(Delegator delegator, String productStoreId) throws GenericEntityException {
        // 判断当前是供应商还是商家的
        EntityCondition entityCondition = EntityCondition.makeCondition(
                EntityCondition.makeCondition("productStoreId", productStoreId),
                EntityCondition.makeCondition("roleTypeId", "OWNER")
        );

        GenericValue productStoreRole = ExtEntityUtil.getOnlyCache(delegator, "ProductStoreRole", entityCondition);
        return UtilValidate.isEmpty(productStoreRole) ? null : productStoreRole.getString("partyId");
    }

    /**
     * 根据partyId获取productStoreId
     */
    public static String getProductStoreIdByOwnerPartyId(Delegator delegator, String partyId) throws GenericEntityException {
        // 判断当前是供应商还是商家的
        EntityCondition entityCondition = EntityCondition.makeCondition(
                EntityCondition.makeCondition("partyId", partyId),
                EntityCondition.makeCondition("roleTypeId", "OWNER")
        );
        GenericValue productStoreRole = ExtEntityUtil.getOnlyCache(delegator, "ProductStoreRole", entityCondition);

        if (UtilValidate.isNotEmpty(productStoreRole)) {
            return (String) productStoreRole.get("productStoreId");
        } else {
            return null;
        }
    }

    /**
     * 判断是否是VIP
     */
    public static boolean isVipMember(Delegator delegator, String partyId) throws GenericEntityException {
        GenericValue vipMember = ExtEntityUtil.findOneCache(delegator, "BizVipMember", UtilMisc.toMap("partyId", partyId));
        return UtilValidate.isNotEmpty(vipMember);
    }

    public static String getVipMemberPartyIdByCustomerPartyIdAndPsid(Delegator delegator, String customerPartyId, String productStoreId) throws GenericEntityException {
        EntityCondition entityCondition = EntityCondition.makeCondition(UtilMisc.toList(
                EntityCondition.makeCondition("customerPartyId", customerPartyId),
                EntityCondition.makeCondition("productStoreId", productStoreId),
                EntityUtil.getFilterByDateExpr()
        ));

        GenericValue vipMemberPartyProductStore = ExtEntityUtil.getOnlyCache(delegator, "BizVipMemberPartyProductStore", entityCondition);
        return UtilValidate.isEmpty(vipMemberPartyProductStore) ? null : (String) vipMemberPartyProductStore.get("vipMemberPartyId");
    }

    public static String getCustomerPartyIdByVipMemberPartyIdAndPsid(Delegator delegator, String vipMemberPartyId, String productStoreId) throws GenericEntityException {
        EntityCondition entityCondition = EntityCondition.makeCondition(UtilMisc.toList(
                EntityCondition.makeCondition("vipMemberPartyId", vipMemberPartyId),
                EntityCondition.makeCondition("productStoreId", productStoreId),
                EntityUtil.getFilterByDateExpr()
        ));

        GenericValue vipMemberPartyProductStore = ExtEntityUtil.getOnlyCache(delegator, "BizVipMemberPartyProductStore", entityCondition);
        return UtilValidate.isEmpty(vipMemberPartyProductStore) ? null : (String) vipMemberPartyProductStore.get("customerPartyId");
    }

    public static String getVipMemberPartyIdByCustomerPartyId(Delegator delegator, String customerPartyId) throws GenericEntityException {
        GenericValue vipMember = ExtEntityUtil.findOneCache(delegator, "BizVipMember", UtilMisc.toMap("partyId", customerPartyId));
        return UtilValidate.isEmpty(vipMember) ? null : (String) vipMember.get("partyId");
    }

    //根据店铺ID获取店铺主目录
    public static String getMainProdCatalogIdByStoreId(Delegator delegator, String productStoreId) throws GenericEntityException {
        GenericValue prodCatalog = ExtEntityUtil.getOnlyCache(delegator, "ProductStoreCatalog", UtilMisc.toMap("productStoreId", productStoreId));
        return UtilValidate.isEmpty(prodCatalog) ? null : (String) prodCatalog.get("prodCatalogId");
    }

    //判断是否是代理商
    public static boolean isAgent(String productStoreId, String partyId, Delegator delegator) throws GenericEntityException {
        GenericValue dealer = getDealerInfoByDealerId(productStoreId, partyId, delegator);
        if (UtilValidate.isEmpty(dealer)) return false;

        String isAgent = dealer.getString("isAgent");
        return "Y".equals(isAgent);
    }

    //判断是否是经销商
    public static boolean isDealer(String productStoreId, String partyId, Delegator delegator) throws GenericEntityException {
        GenericValue dealer = getDealerInfoByDealerId(productStoreId, partyId, delegator);
        if (UtilValidate.isEmpty(dealer)) return false;

        String isAgent = dealer.getString("isAgent");
        return "N".equals(isAgent);
    }

    //判断是否是渠道
    public static boolean isDistributionChannel(String productStoreId, String partyId, Delegator delegator) throws GenericEntityException {
        if (isDealer(productStoreId, partyId, delegator) || isAgent(productStoreId, partyId, delegator)) {
            return true;
        }
        return false;
    }

    public static GenericValue getDealerInfoByDealerId(String productStoreId, String partyId, Delegator delegator) throws GenericEntityException {
        List entityConditionList = UtilMisc.toList(
                EntityCondition.makeCondition("productStoreId", productStoreId),
                EntityCondition.makeCondition("dealerPartyId", partyId)
        );
        return ExtEntityUtil.getOnlyCache(delegator, "BizDealer", EntityCondition.makeCondition(entityConditionList));
    }

    //根据partyId获取person名字
    public static String getPersonFirstNameByPartyId(String partyId, Delegator delegator) throws GenericEntityException {
        GenericValue person = ExtEntityUtil.getOnlyCache(delegator, "Person", UtilMisc.toMap("partyId", partyId));
        String firstName = "";
        if (UtilValidate.isNotEmpty(person)) {
            firstName = person.getString("firstName");
        }

        return firstName;
    }

}