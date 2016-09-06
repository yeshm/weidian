package org.ofbiz.ext.common

import javolution.util.FastList
import org.ofbiz.base.util.Debug
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.ext.util.AppUtil
import org.ofbiz.ext.util.ExtUtilHttp
import org.ofbiz.ext.util.MessageUtil

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public String jsonResponseFromRequestAttributes() {
    HttpServletRequest request = request
    HttpServletResponse response = response

    List<String> skipRequestAttributeKeys = FastList.newInstance()
    skipRequestAttributeKeys.add("_CONTEXT_ROOT_")
    skipRequestAttributeKeys.add("_CONTROL_PATH_")
    skipRequestAttributeKeys.add("_ERROR_MESSAGE_")
    skipRequestAttributeKeys.add("_ERROR_MESSAGE_LIST_")
    skipRequestAttributeKeys.add("_FORWARDED_FROM_SERVLET_")
    skipRequestAttributeKeys.add("_SERVER_ROOT_URL_")
    skipRequestAttributeKeys.add("javax.servlet.request.cipher_suite")
    skipRequestAttributeKeys.add("javax.servlet.request.key_size")
    skipRequestAttributeKeys.add("javax.servlet.request.ssl_session")
    skipRequestAttributeKeys.add("javax.servlet.request.ssl_session_id")
    skipRequestAttributeKeys.add("multiPartMap")
    skipRequestAttributeKeys.add("targetRequestUri")
    skipRequestAttributeKeys.add("thisRequestUri")
    skipRequestAttributeKeys.add("_ERROR_MESSAGE_CODE_")

    // pull out the service response from the request attribute
    Map<String, Object> attrMap = ExtUtilHttp.getJSONAttributeMap(request)

    // create a JSON Object for return
    Map json = new HashMap()

    if ("error".equals(attrMap.get("responseMessage")) || attrMap.containsKey("_ERROR_MESSAGE_LIST_") || attrMap.containsKey("_ERROR_MESSAGE_") || attrMap.containsKey("_ERROR_MESSAGE_CODE_")) {
        json.put("result", "error")
        if (attrMap.containsKey("_ERROR_MESSAGE_LIST_")) {
            def messageList = attrMap.get("_ERROR_MESSAGE_LIST_") as List
            def jsonArray = new ArrayList<String>();
            jsonArray.addAll(messageList)
            json.put("message", messageList.size() > 1 ? jsonArray : messageList.get(0))
        }

        if (attrMap.containsKey("_ERROR_MESSAGE_")) {
            json.put("message", attrMap.get("_ERROR_MESSAGE_"))
        }
        if (attrMap.containsKey("_ERROR_MESSAGE_CODE_")) {
            json.put("code", attrMap.get("_ERROR_MESSAGE_CODE_"))
        }

        //默认错误提示
        if (!json.containsKey("message")) json.put("message", "系统错误")
    } else {
        json.put("result", "success")
        for (String k : skipRequestAttributeKeys) {
            attrMap.remove(k)
        }
        if (UtilValidate.isNotEmpty(attrMap)) {
            if (UtilValidate.isNotEmpty(attrMap.get(MessageUtil.JSON_RESPONSE_DATA_KEY))) {
                json.put("data", attrMap.get(MessageUtil.JSON_RESPONSE_DATA_KEY))
            } else {
                json.put("data", attrMap)
            }
        }
    }

    writeJSONtoResponse(json, response)

    return "success"
}

public String gridResponseFromRequestAttributes() {
    HttpServletRequest request = request
    HttpServletResponse response = response

    // pull out the service response from the request attribute
    Map<String, Object> attrMap = ExtUtilHttp.getJSONAttributeMap(request)
    // create a JSON Object for return
    Map json = new HashMap()
    Map<String, Object> result = attrMap.get("result")

    if (attrMap.containsKey("_ERROR_MESSAGE_LIST_") || attrMap.containsKey("_ERROR_MESSAGE_")) {
        json.put("result", "error")
    } else {
        def list = result ? result.get("list") : null
        if (UtilValidate.isNotEmpty(list)) {
            json.put("rows", list)
            json.put("results", result.get("listSize"))

            def summary = result.get("summary")
            if (UtilValidate.isNotEmpty(summary)) {
                json.put("summary", summary)
            }
        } else {
            json.put("rows", [])
            json.put("results", 0)
        }
    }
    writeJSONtoResponse(json, response)

    return "success"
}

public String appJsonResponseFromRequestAttributes() {
    HttpServletRequest request = request
    HttpServletResponse response = response

    List<String> skipRequestAttributeKeys = FastList.newInstance()
    skipRequestAttributeKeys.add("_CONTEXT_ROOT_")
    skipRequestAttributeKeys.add("_CONTROL_PATH_")
    skipRequestAttributeKeys.add("_ERROR_MESSAGE_")
    skipRequestAttributeKeys.add("_ERROR_MESSAGE_LIST_")
    skipRequestAttributeKeys.add("_FORWARDED_FROM_SERVLET_")
    skipRequestAttributeKeys.add("_SERVER_ROOT_URL_")
    skipRequestAttributeKeys.add("javax.servlet.request.cipher_suite")
    skipRequestAttributeKeys.add("javax.servlet.request.key_size")
    skipRequestAttributeKeys.add("javax.servlet.request.ssl_session")
    skipRequestAttributeKeys.add("javax.servlet.request.ssl_session_id")
    skipRequestAttributeKeys.add("multiPartMap")
    skipRequestAttributeKeys.add("targetRequestUri")
    skipRequestAttributeKeys.add("thisRequestUri")
    skipRequestAttributeKeys.add("_ERROR_MESSAGE_CODE_")

    // pull out the service response from the request attribute
    Map<String, Object> attrMap = ExtUtilHttp.getJSONAttributeMap(request)

    for (String k : skipRequestAttributeKeys) {
        attrMap.remove(k)
    }

    // create a JSON Object for return
    Map json = new HashMap()
    Map<String, Object> result = attrMap.get("result")

    if (attrMap.containsKey("_ERROR_MESSAGE_LIST_") || attrMap.containsKey("_ERROR_MESSAGE_")) {
        json.put("result", "error")
        json.put("status_code", -1)
        json.put("status_msg", result.errorMessage)
        attrMap.remove("result")

        def dataObject = [:]
        for (String key : attrMap.keySet()) {
            dataObject.put(key, attrMap.get(key))
        }
    } else {
        if (UtilValidate.isNotEmpty(result)) {
            json.put("result", result.responseMessage)

            if ("error".equals(result.responseMessage)) {
                json.put("status_code", -1)
                json.put("status_msg", result.errorMessage)
            } else if ("success".equals(result.responseMessage)) {
                json.put("status_code", result.status_code == null ? 0 : result.status_code)
                json.put("status_msg", result.status_msg == null ? "成功" : result.status_msg)
            } else {
                json.put("status_code", result.status_code)
                json.put("status_msg", result.status_msg)
            }
            attrMap.remove("result")

            def dataObject = [:]
            for (String key : attrMap.keySet()) {
                dataObject.put(key, attrMap.get(key))
            }

            json.put("dataObject", dataObject)
        } else {
            json.put("status_code", "-1")
            json.put("status_msg", "系统错误")
        }
    }
    writeJSONtoResponse(json, response)

    return "success"
}

public String statisticsJsonResponse() {
    HttpServletRequest request = request
    HttpServletResponse response = response

    List<String> skipRequestAttributeKeys = FastList.newInstance()
    skipRequestAttributeKeys.add("_CONTEXT_ROOT_")
    skipRequestAttributeKeys.add("_CONTROL_PATH_")
    skipRequestAttributeKeys.add("_ERROR_MESSAGE_")
    skipRequestAttributeKeys.add("_ERROR_MESSAGE_LIST_")
    skipRequestAttributeKeys.add("_FORWARDED_FROM_SERVLET_")
    skipRequestAttributeKeys.add("_SERVER_ROOT_URL_")
    skipRequestAttributeKeys.add("javax.servlet.request.cipher_suite")
    skipRequestAttributeKeys.add("javax.servlet.request.key_size")
    skipRequestAttributeKeys.add("javax.servlet.request.ssl_session")
    skipRequestAttributeKeys.add("javax.servlet.request.ssl_session_id")
    skipRequestAttributeKeys.add("multiPartMap")
    skipRequestAttributeKeys.add("targetRequestUri")
    skipRequestAttributeKeys.add("thisRequestUri")
    skipRequestAttributeKeys.add("_ERROR_MESSAGE_CODE_")

    // pull out the service response from the request attribute
    Map<String, Object> attrMap = ExtUtilHttp.getJSONAttributeMap(request)

    for (String k : skipRequestAttributeKeys) {
        attrMap.remove(k)
    }
    // create a JSON Object for return
    def result = attrMap.get("result")

    writeJSONtoResponse(result, response)

    return "success"
}

private static void writeJSONtoResponse(Map json, HttpServletResponse response) {
    String jsonStr = AppUtil.getMapper().getMapper().writeValueAsString(json)
    if (jsonStr == null) {
        Debug.logError("JSON Object was empty fatal error!", CommonEvents.getClass().name)
        return
    }

    response.setContentType("text/plain")
    try {
        response.setContentLength(jsonStr.getBytes("UTF8").length)
    } catch (UnsupportedEncodingException e) {
        Debug.logError("Problems with Json encoding: " + e, CommonEvents.getClass().name)
    }

    Writer out
    try {
        out = response.getWriter()
        out.write(jsonStr)
        out.flush()
    } catch (IOException e) {
        Debug.logError(e, CommonEvents.getClass().name)
    }
}

private static void writeJSONtoResponse(Object json, HttpServletResponse response) {
    String jsonStr = AppUtil.getMapper().getMapper().writeValueAsString(json)
    if (jsonStr == null) {
        Debug.logError("JSON Object was empty fatal error!", CommonEvents.getClass().name)
        return
    }

    response.setContentType("text/plain")
    try {
        response.setContentLength(jsonStr.getBytes("UTF8").length)
    } catch (UnsupportedEncodingException e) {
        Debug.logError("Problems with Json encoding: " + e, CommonEvents.getClass().name)
    }

    Writer out
    try {
        out = response.getWriter()
        out.write(jsonStr)
        out.flush()
    } catch (IOException e) {
        Debug.logError(e, CommonEvents.getClass().name)
    }
}
