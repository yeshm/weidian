package org.ofbiz.ext.shipment

import com.fasterxml.jackson.databind.JsonNode
import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.ext.util.AppUtil
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.RenderUtil
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//快递100回调执行的操作
public kuaidi100() {
    HttpServletRequest request = request
    HttpServletResponse response = response
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    def requestParameters = UtilHttp.getParameterMap(request)

    //得到快递100回传的json
    String data = requestParameters.param
    def result
    Map JsonMap = [:]
    //解释并获取Jason里面的数据
    JsonMap = AppUtil.fromJson(data, Map);
    def pushMessage = JsonMap.message
    def pushStatus = JsonMap.status
    def pushBillStatus = JsonMap.billstatus
    Map lasResult = JsonMap.lastResult
    def expressMessage = lasResult.message
    def expressStatus = lasResult.status
    def expressState = lasResult.state
    def expressCom = lasResult.com
    def expressNu = lasResult.nu
    def expressIscheck = lasResult.ischeck
    List dataList = lasResult.data

    //创建或更新快递状态
    def serviceName
    def extExpressStatus = ExtEntityUtil.getOnly(
            delegator, "ExtExpressStatus", ["expressNu": expressNu, "expressCom": expressCom]
    )
    if (UtilValidate.isEmpty(extExpressStatus)) {
        serviceName = "BizCreateExtExpressStatus"
    } else {
        serviceName = "BizUpdateExtExpressStatus"
    }

    result = dispatcher.runSync(serviceName, [
            pushMessage   : pushMessage,
            pushStatus    : pushStatus,
            pushBillStatus: pushBillStatus,
            expressMessage: expressMessage,
            expressStatus : expressStatus,
            expressState  : expressState,
            expressCom    : expressCom,
            expressNu     : expressNu,
            expressIscheck: expressIscheck,
    ])
    if (!result.containsValue("success"))
        return;
    def expressStatusId = result.expressStatusId

    if (UtilValidate.isWhitespace(expressStatusId)) {
        return;
    }
    //每次只保存最新传回来的数据，并把以前的废弃
    def ExtExpressQueryLogList = ExtEntityUtil.findList(
            delegator, "ExtExpressQueryLog", ["expressStatusId": expressStatusId]
    )
    //把现有数据废除
    if (!UtilValidate.isEmpty(ExtExpressQueryLogList)) {
        serviceName = "BizUpdateExtExpressQueryLog"
        result = dispatcher.runSync(serviceName, [
                expressStatusId: expressStatusId
        ])
    }
    //保存新的数据
    serviceName = "BizCreateExtExpressQueryLog"
    result = dispatcher.runSync(serviceName, [
            expressCom     : expressCom,
            expressNu      : expressNu,
            dataList       : dataList,
            expressStatusId: expressStatusId
    ])

    //把最新的快递查询存到数据库

    if (!result.containsValue("success")) {
        return;
    }
    JsonNode json = AppUtil.getMapper().getMapper().createObjectNode()
    json.put("result", "true")
    json.put("returnCode", "200")
    json.put("message", "成功")

    RenderUtil.renderJson(response, json.toString())
    return "success"
}