package org.weidian.constant.order;

import org.ofbiz.ext.util.AppUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderType {
    private static Map<String, String> map = new LinkedHashMap<String, String>();

    public static final String WEI_DIAN = "WEI_DIAN";
    public static final String MANUAL = "MANUAL";

    static {
        map.put(WEI_DIAN, "微店订单");
        map.put(MANUAL, "线下订单");
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
