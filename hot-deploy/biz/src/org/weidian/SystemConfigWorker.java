package org.weidian;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.ext.util.ExtEntityUtil;
import org.weidian.constant.common.SystemConfigTypeEnum;

/**
 * 系统配置助手
 */
public class SystemConfigWorker {

    private static String module = SystemConfigWorker.class.getName();

    public static String getSystemConfig(Delegator delegator, String configTypeEnumId) {
        try {
            EntityCondition entityCondition = EntityCondition.makeCondition(UtilMisc.toList(
                    EntityCondition.makeCondition("configTypeEnumId", configTypeEnumId),
                    EntityUtil.getFilterByDateExpr()
            ));
            GenericValue systemConfig = ExtEntityUtil.getOnlyCache(delegator, "ExtSystemConfig", entityCondition);

            if (UtilValidate.isNotEmpty(systemConfig)) return (String) systemConfig.get("configValue");
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否启用
     */
    public static boolean isEnable(Delegator delegator, String configTypeEnumId) {
        String isEnable = getSystemConfig(delegator, configTypeEnumId);
        return "Y".equals(isEnable);
    }


    /**
     * 获取系统名称
     */
    public static String getSystemName(Delegator delegator) {
        return "微店助手";
    }

    /**
     * 获取七牛存储UrlPrefix
     */
    public static String getQiNiuUrlPrefix(Delegator delegator) {
        return getSystemConfig(delegator, SystemConfigTypeEnum.QN_URL_PREFIX);
    }

    /**
     * 获取七牛存储BucketName
     */
    public static String getQiNiuBucketName(Delegator delegator) {
        return getSystemConfig(delegator, SystemConfigTypeEnum.QN_BUCKET_NAME);
    }

    /**
     * 七牛存储是否启用
     */
    public static boolean isQiNiuEnable(Delegator delegator) {
        String isQiNiuEnable = getSystemConfig(delegator, SystemConfigTypeEnum.QN_ENABLE);
        return "Y".equals(isQiNiuEnable);
    }
}