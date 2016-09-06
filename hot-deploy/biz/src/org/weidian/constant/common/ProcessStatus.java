package org.weidian.constant.common;

import org.ofbiz.ext.util.AppUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProcessStatus {
    private static Map<String, String> map = new LinkedHashMap<String, String>();

    public static final String CREATED = "PROCESS_CREATED";
    public static final String PENDING = "PROCESS_PENDING";
    public static final String FAILED = "PROCESS_FAILED";
    public static final String COMPLETED = "PROCESS_COMPLETED";
    public static final String CANCELLED = "PROCESS_CANCELLED";

    static {
        map.put(CREATED, "等待处理");
        map.put(PENDING, "处理中");
        map.put(FAILED, "处理失败");
        map.put(COMPLETED, "已完成");
        map.put(CANCELLED, "已取消");
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