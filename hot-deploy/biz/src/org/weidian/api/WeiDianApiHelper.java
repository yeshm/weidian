package org.weidian.api;

import com.fasterxml.jackson.databind.JsonNode;
import javolution.util.FastMap;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.ext.util.AppUtil;
import org.ofbiz.ext.util.ExtHttpClient;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class WeiDianApiHelper {

    public static final String module = WeiDianApiHelper.class.getName();

    private static final Map<String, String> ERROR_MAP = FastMap.newInstance();
    private static final Map<String, String> emotionMap = FastMap.newInstance();

    static {
        ERROR_MAP.put("-3", "网络请求错误");
        ERROR_MAP.put("-2", "网络连接异常，请检查网络连接");
        ERROR_MAP.put("-1", "系统繁忙");
        ERROR_MAP.put("0", "请求成功");
        ERROR_MAP.put("40001", "获取access_token时AppSecret错误，或者access_token无效");
        ERROR_MAP.put("40002", "不合法的凭证类型");
        ERROR_MAP.put("40003", "不合法的OpenID");
        ERROR_MAP.put("40004", "不合法的媒体文件类型");
        ERROR_MAP.put("40005", "不合法的文件类型");
        ERROR_MAP.put("40006", "不合法的文件大小");
        ERROR_MAP.put("40007", "不合法的媒体文件id");
        ERROR_MAP.put("40008", "不合法的消息类型");
        ERROR_MAP.put("40009", "不合法的图片文件大小");
        ERROR_MAP.put("40010", "不合法的语音文件大小");
        ERROR_MAP.put("40011", "不合法的视频文件大小");
        ERROR_MAP.put("40012", "不合法的缩略图文件大小");
        ERROR_MAP.put("40013", "不合法的APPID");
        ERROR_MAP.put("40014", "不合法的access_token");
        ERROR_MAP.put("40015", "不合法的菜单类型");
        ERROR_MAP.put("40016", "不合法的按钮个数");
        ERROR_MAP.put("40017", "不合法的按钮个数");
        ERROR_MAP.put("40018", "不合法的按钮名字长度");
        ERROR_MAP.put("40019", "不合法的按钮KEY长度");
        ERROR_MAP.put("40020", "不合法的按钮URL长度");
        ERROR_MAP.put("40021", "不合法的菜单版本号");
        ERROR_MAP.put("40022", "不合法的子菜单级数");
        ERROR_MAP.put("40023", "不合法的子菜单按钮个数");
        ERROR_MAP.put("40024", "不合法的子菜单按钮类型");
        ERROR_MAP.put("40025", "不合法的子菜单按钮名字长度");
        ERROR_MAP.put("40026", "不合法的子菜单按钮KEY长度");
        ERROR_MAP.put("40027", "不合法的子菜单按钮URL长度");
        ERROR_MAP.put("40028", "不合法的自定义菜单使用用户");
        ERROR_MAP.put("40029", "不合法的oauth_code");
        ERROR_MAP.put("40030", "不合法的refresh_token");
        ERROR_MAP.put("40031", "不合法的openid列表");
        ERROR_MAP.put("40032", "不合法的openid列表长度");
        ERROR_MAP.put("40033", "不合法的请求字符，不能包含\\uxxxx格式的字符");
        ERROR_MAP.put("40035", "不合法的参数");
        ERROR_MAP.put("40038", "不合法的请求格式");
        ERROR_MAP.put("40039", "不合法的URL长度");
        ERROR_MAP.put("40050", "不合法的分组id");
        ERROR_MAP.put("40051", "分组名字不合法");
        ERROR_MAP.put("41001", "缺少access_token参数");
        ERROR_MAP.put("41002", "缺少appid参数");
        ERROR_MAP.put("41003", "缺少refresh_token参数");
        ERROR_MAP.put("41004", "缺少secret参数");
        ERROR_MAP.put("41005", "缺少多媒体文件数据");
        ERROR_MAP.put("41006", "缺少media_id参数");
        ERROR_MAP.put("41007", "缺少子菜单数据");
        ERROR_MAP.put("41008", "缺少oauth code");
        ERROR_MAP.put("41009", "缺少openid");
        ERROR_MAP.put("42001", "access_token超时");
        ERROR_MAP.put("42002", "refresh_token超时");
        ERROR_MAP.put("42003", "oauth_code超时");
        ERROR_MAP.put("43001", "需要GET请求");
        ERROR_MAP.put("43002", "需要POST请求");
        ERROR_MAP.put("43003", "需要HTTPS请求");
        ERROR_MAP.put("43004", "需要接收者关注");
        ERROR_MAP.put("43005", "需要好友关系");
        ERROR_MAP.put("44001", "多媒体文件为空");
        ERROR_MAP.put("44002", "POST的数据包为空");
        ERROR_MAP.put("44003", "图文消息内容为空");
        ERROR_MAP.put("44004", "文本消息内容为空");
        ERROR_MAP.put("45001", "多媒体文件大小超过限制");
        ERROR_MAP.put("45002", "消息内容超过限制");
        ERROR_MAP.put("45003", "标题字段超过限制");
        ERROR_MAP.put("45004", "描述字段超过限制");
        ERROR_MAP.put("45005", "链接字段超过限制");
        ERROR_MAP.put("45006", "图片链接字段超过限制");
        ERROR_MAP.put("45007", "语音播放时间超过限制");
        ERROR_MAP.put("45008", "图文消息超过限制");
        ERROR_MAP.put("45009", "接口调用超过限制");
        ERROR_MAP.put("45010", "创建菜单个数超过限制");
        ERROR_MAP.put("45015", "回复时间超过限制");
        ERROR_MAP.put("45016", "系统分组，不允许修改");
        ERROR_MAP.put("45017", "分组名字过长");
        ERROR_MAP.put("45018", "分组数量超过上限");
        ERROR_MAP.put("46001", "不存在媒体数据");
        ERROR_MAP.put("46002", "不存在的菜单版本");
        ERROR_MAP.put("46003", "不存在的菜单数据");
        ERROR_MAP.put("46004", "不存在的用户");
        ERROR_MAP.put("47001", "解析JSON/XML内容错误");
        ERROR_MAP.put("48001", "api功能未授权");
        ERROR_MAP.put("50001", "用户未授权该api");
        ERROR_MAP.put("42005", "api错误");
        ERROR_MAP.put("45157", "标签名非法，请注意不能和其他标签重名");
        ERROR_MAP.put("45158", "标签名非法，标签名长度超过30个字节");
        ERROR_MAP.put("45056", "创建的标签数过多，请注意不能超过100个");
        ERROR_MAP.put("45058", "不能修改0/1/2这三个系统默认保留的标签");
        ERROR_MAP.put("45057", "该标签下粉丝数超过10w，不允许直接删除");
        ERROR_MAP.put("45159", "非法的tag_id");
        ERROR_MAP.put("40032", "每次传入的openid列表个数不能超过50个");
        ERROR_MAP.put("45059", "有粉丝身上的标签数已经超过限制");
        ERROR_MAP.put("49003", "传入的openid不属于此AppID");
        ERROR_MAP.put("65301", "不存在此menuid对应的个性化菜单");
        ERROR_MAP.put("65302", "没有相应的用户");
        ERROR_MAP.put("65303", "没有默认菜单，不能创建个性化菜单");
        ERROR_MAP.put("65304", "MatchRule信息为空");
        ERROR_MAP.put("65305", "个性化菜单数量受限");
        ERROR_MAP.put("65306", "不支持个性化菜单的帐号");
        ERROR_MAP.put("65307", "个性化菜单信息为空");
        ERROR_MAP.put("65308", "包含没有响应类型的button");
        ERROR_MAP.put("65309", "个性化菜单开关处于关闭状态");
        ERROR_MAP.put("653010", "填写了省份或城市信息，国家信息不能为空");
        ERROR_MAP.put("653012", "填写了城市信息，省份信息不能为空");
        ERROR_MAP.put("653013", "不合法的国家信息");
        ERROR_MAP.put("653014", "不合法的省份信息");
        ERROR_MAP.put("653015", "不合法的城市信息");
        ERROR_MAP.put("653016", "该公众号的菜单设置了过多的域名外跳（最多跳转到3个域名的链接）");
        ERROR_MAP.put("653017", "不合法的URL");
    }

    public static Map<String, Object> weixinGet(LocalDispatcher dispatcher, String accessToken, String url, Map<String, String> urlParameters) throws UnsupportedEncodingException, GenericServiceException {
        StringBuffer urlParamterStr = new StringBuffer();

        if (UtilValidate.isNotEmpty(accessToken)) {
            urlParamterStr.append("?access_token=" + accessToken);
        } else {
            urlParamterStr.append("?");
        }

        if (UtilValidate.isNotEmpty(urlParameters)) {
            for (String key : urlParameters.keySet()) {
                urlParamterStr.append("&" + key);
                urlParamterStr.append("=" + urlParameters.get(key));
            }
        }

        Timestamp requestStartDateTime = UtilDateTime.nowTimestamp();

        Map results = ExtHttpClient.get(url + urlParamterStr.toString());
        Map apiResults = handleWeixinHttpResponse(results);

        createWeixinApiLog(dispatcher, accessToken, url, urlParameters, null, requestStartDateTime, results, apiResults);

        return apiResults;
    }

    //上传文件
    public static Map<String, Object> weixinPost(LocalDispatcher dispatcher, String accessToken, String url, Map<String, String> urlParameters, String body, File file) throws UnsupportedEncodingException, GenericServiceException {
        StringBuffer urlParamterStr = new StringBuffer();

        if (UtilValidate.isNotEmpty(accessToken)) {
            urlParamterStr.append("?access_token=" + accessToken);
        } else {
            urlParamterStr.append("?");
        }

        if (UtilValidate.isNotEmpty(urlParameters)) {
            for (String key : urlParameters.keySet()) {
                urlParamterStr.append("&" + key);
                urlParamterStr.append("=" + urlParameters.get(key));
            }
        }

        Timestamp requestStartDateTime = UtilDateTime.nowTimestamp();

        Map results = ExtHttpClient.post(url + urlParamterStr.toString(), body, file);
        Map apiResults = handleWeixinHttpResponse(results);

        createWeixinApiLog(dispatcher, accessToken, url, urlParameters, body + "|" + file.getAbsolutePath(), requestStartDateTime, results, apiResults);

        if (ServiceUtil.isSuccess(results)) {
            file.delete();
        }

        return apiResults;
    }

    public static void createWeixinApiLog(LocalDispatcher dispatcher, String accessToken, String url, Map<String, String> urlParameters, String body, Timestamp requestStartDateTime, Map<String, Object> results, Map<String, Object> apiResults) throws UnsupportedEncodingException, GenericServiceException {
        Map serviceCtx = new HashMap();
        Map requestParameters = new HashMap();
        if (UtilValidate.isNotEmpty(urlParameters)) requestParameters.putAll(urlParameters);
        if (UtilValidate.isNotEmpty(accessToken)) requestParameters.put("accessToken", accessToken);

        serviceCtx.put("requestUrl", url);
        serviceCtx.put("requestBody", body);
        serviceCtx.put("requestParameters", AppUtil.toJson(requestParameters));
        serviceCtx.put("responseContent", results.get("responseString"));
        serviceCtx.put("requestStartDateTime", requestStartDateTime);
        serviceCtx.put("isSuccess", isSuccess(apiResults) ? "Y" : "N");
        serviceCtx.put("runningTimeMillis", System.currentTimeMillis() - requestStartDateTime.getTime());

        dispatcher.runSync("bizCreateWeixinApiLog", serviceCtx);
    }

    public static Map<String, Object> handleWeixinHttpResponse(Map<String, Object> httpResults) throws UnsupportedEncodingException {
        if (!ServiceUtil.isSuccess(httpResults)) {
            String errorString = (String) httpResults.get("errorMessage");

            Map results = ServiceUtil.returnSuccess();
            results.put("errcode", "-3");
            results.put("errorString", errorString);

            return results;
        }

        String result = (String) httpResults.get("responseString");

        JsonNode json = AppUtil.fromJson(result, JsonNode.class);

        if (json.has("errcode") && !"0".equals(json.get("errcode").asText())) {

            String errorString = ERROR_MAP.get(json.get("errcode").asText());

            Debug.logError("weixin api error, errcode:" + json.get("errcode").asText() + ",errorString:" + errorString, module);

            Map results = ServiceUtil.returnSuccess();
            results.put("errcode", json.get("errcode").asText());
            results.put("errorString", errorString);

            if (UtilValidate.isEmpty(errorString) && UtilValidate.isNotEmpty(json.get("errmsg"))) {
                errorString = json.get("errmsg").asText();
                results.put("errorString", errorString);
            }

            return results;
        }

        return httpResults;
    }

    public static boolean isSuccess(Map results) {
        if (!ServiceUtil.isSuccess(results)) {
            return false;
        }
        if (results.containsKey("errcode")) {
            if (!results.get("errcode").toString().equals("0")) {
                return false;
            }
        }

        return true;
    }

    public static Map transToErrorResults(Map results) {
        if (!ServiceUtil.isSuccess(results)) return results;

        Map apiResults = ServiceUtil.returnError((String) results.get("errorString"));
        apiResults.put("errcode", results.get("errcode"));
        return apiResults;
    }

}