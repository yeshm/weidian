package org.weidian.constant.party;

import org.ofbiz.ext.util.AppUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SecurityGroup {
    private static Map<String, String> map = new LinkedHashMap<String, String>();

    public static final String ADMIN_ADMIN = "ADMIN_ADMIN";
    public static final String MERCHANT_ADMIN = "MERCHANT_ADMIN";

    static {
        map.put(ADMIN_ADMIN, "平台安全组");
        map.put(MERCHANT_ADMIN, "商家安全组");
    }

    public static String getDesc(String id) {
        return map.get(id);
    }

    public static String toJson() {
        return AppUtil.toJson(map);
    }

    public static Map getMap() {
        return map;
    }

    public static Map getMap(String... ids) {
        Map<String, String> newMap = new LinkedHashMap<String, String>(map);
        List<String> keys = Arrays.asList(ids);
        newMap.keySet().retainAll(keys);

        return newMap;
    }

    public static String toJson(String... ids) {
        Map<String, String> newMap = getMap(ids);

        return AppUtil.toJson(newMap);
    }
}
