package org.weidian.constant.common;

import org.ofbiz.ext.util.AppUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共状态
 */
public class CommonStatus {

    private static Map<String, String> map = new LinkedHashMap<String, String>();

    public static String DISABLED = "COMMON_DISABLED";
    public static String ENABLED = "COMMON_ENABLED";
    public static String DELETED = "COMMON_DELETED";

    static {
        map = new LinkedHashMap<String, String>();
        map.put(DISABLED, "禁用");
        map.put(ENABLED, "启用");
        map.put(DELETED, "删除");
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
