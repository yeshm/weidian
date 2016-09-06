package org.weidian.constant.common;

import org.ofbiz.ext.util.AppUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置类型
 */
public class SystemConfigTypeEnum {

    private static Map<String, String> map = new LinkedHashMap<String, String>();

    /**
     * 七牛存储AccessKey
     */
    public static String QN_ACCESS_KEY = "QN_ACCESS_KEY";
    /**
     * 七牛存储SecretKey
     */
    public static String QN_SECRET_KEY = "QN_SECRET_KEY";
    /**
     * 七牛存储BucketName
     */
    public static String QN_BUCKET_NAME = "QN_BUCKET_NAME";
    /**
     * 七牛存储UrlPrefix
     */
    public static String QN_URL_PREFIX = "QN_URL_PREFIX";
    /**
     * 七牛存储启用
     */
    public static String QN_ENABLE = "QN_ENABLE";

    static {
        map = new LinkedHashMap<String, String>();

        map.put(QN_ACCESS_KEY, "七牛存储AccessKey");
        map.put(QN_SECRET_KEY, "七牛存储SecretKey");
        map.put(QN_BUCKET_NAME, "七牛存储BucketName");
        map.put(QN_URL_PREFIX, "七牛存储UrlPrefix");
        map.put(QN_ENABLE, "七牛存储启用");
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
