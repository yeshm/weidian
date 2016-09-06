package org.weidian.constant.common;

import org.ofbiz.ext.util.AppUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传类型
 */
public class FileUploadTypeEnum {

    private static Map<String, String> map = new LinkedHashMap<String, String>();

    /**
     * 订单批量发货
     */
    public static String ORDER_BATCH_SEND = "FLUD_ORDER_BTCH_SEND";

    static {
        map = new LinkedHashMap<String, String>();

        map.put(ORDER_BATCH_SEND, "订单批量发货");
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
