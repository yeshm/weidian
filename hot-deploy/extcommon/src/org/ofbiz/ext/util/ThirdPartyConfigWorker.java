package org.ofbiz.ext.util;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;

/**
 * 第三方配置助手
 */
public class ThirdPartyConfigWorker {

    private static String module = ThirdPartyConfigWorker.class.getName();

    public static GenericValue getThirdPartyConfig(Delegator delegator) {
        try {
            return ExtEntityUtil.findOne(delegator, "BizThirdPartyConfig", UtilMisc.toMap("thirdPartyConfigId", "THIRD_PARTY_CONFIG"));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            e.printStackTrace();
        }
        return null;
    }

    public static String getBaiduTongJiCode(Delegator delegator) {
        try {
            GenericValue thirdPartyConfig = ExtEntityUtil.findOne(delegator, "BizThirdPartyConfig", UtilMisc.toMap("thirdPartyConfigId", "THIRD_PARTY_CONFIG"));
            String code = (String)thirdPartyConfig.get("baiduTongJiCode");
            return UtilValidate.isEmpty(code) ? "" : code;
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取第三方资源
     * @param delegator
     * @param key 字段名称
     * @return
     */
    public static String getThirdPartyPropertyBy(Delegator delegator, String key) {
        GenericValue thirdPartyConfig = null;
        try {
            thirdPartyConfig = ExtEntityUtil.findOneCache(delegator, "BizThirdPartyConfig", UtilMisc.toMap("thirdPartyConfigId", "THIRD_PARTY_CONFIG"));
            return UtilValidate.isNotEmpty(thirdPartyConfig.getString(key))?thirdPartyConfig.getString(key):"";
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
        }
        return "";
    }

}
