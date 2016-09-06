package org.weidian.constant.order;

import org.ofbiz.ext.util.AppUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderStatus {
    private static Map<String, String> map = new LinkedHashMap();

    public static final String PAY = "ORDER_PAY";
    public static final String FINISH = "ORDER_FINISH";
    public static final String REFUND = "ORDER_REFUND";
    public static final String CLOSE = "ORDER_CLOSE";

    static {
        map.put(PAY, "已付款");
        map.put(FINISH, "已发货");
        map.put(REFUND, "退款");
        map.put(CLOSE, "订单关闭");
    }

    private static Map<String, String> weiDianStatusMap = new LinkedHashMap();

    static {
        weiDianStatusMap.put("2", PAY);
        weiDianStatusMap.put("3", FINISH);
        weiDianStatusMap.put("7", REFUND);
        weiDianStatusMap.put("8", CLOSE);
    }

    /**
     * 获取描述
     */
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

    public static String weiDianStatusToStatusId(String status) {
        return weiDianStatusMap.get(status);
    }
}
