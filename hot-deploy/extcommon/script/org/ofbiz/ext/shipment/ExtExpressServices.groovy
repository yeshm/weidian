package org.ofbiz.ext.shipment

import com.juweitu.bonn.SystemConfigWorker
import com.juweitu.bonn.constant.common.CommonStatus
import com.juweitu.bonn.constant.common.SystemConfigTypeEnum
import com.juweitu.bonn.express.Kuaidi100Helper
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.express.pojo.TaskRequest
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.ExtUtilDateTime
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

//新建快递状态
public Map createExtExpressStatus() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    Map parameters = parameters

    def extExpressStatus = delegator.makeValue("ExtExpressStatus", [
            pushMessage   : parameters.pushMessage,
            pushStatus    : parameters.pushStatus,
            pushBillStatus: parameters.pushBillStatus,
            expressMessage: parameters.expressMessage,
            expressStatus : parameters.expressStatus,
            expressState  : parameters.expressState,
            expressCom    : parameters.expressCom,
            expressNu     : parameters.expressNu,
            expressIscheck: parameters.expressIscheck
    ])
    extExpressStatus.setNextSeqId()
    extExpressStatus.create()

    def results = ServiceUtil.returnSuccess()
    results.expressStatusId = extExpressStatus.expressStatusId

    return results

}
//更新最新的快递状态
public Map updateExtExpressStatus() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    Map parameters = parameters

    def expressCom = parameters.expressCom
    def expressNu = parameters.expressNu
    def extExpressStatus = ExtEntityUtil.getOnly(
            delegator, "ExtExpressStatus", [expressNu: expressNu, expressCom: expressCom]
    )
    if (UtilValidate.isEmpty(extExpressStatus)) {
        return
    } else {
        extExpressStatus.pushMessage = parameters.pushMessage
        extExpressStatus.pushStatus = parameters.pushStatus
        extExpressStatus.pushBillStatus = parameters.pushBillStatus
        extExpressStatus.expressMessage = parameters.expressMessage
        extExpressStatus.expressStatus = parameters.expressStatus
        extExpressStatus.expressState = parameters.expressState
        extExpressStatus.expressIscheck = parameters.expressIscheck
        extExpressStatus.store()
    }

    def results = ServiceUtil.returnSuccess()
    results.expressStatusId = extExpressStatus.expressStatusId

    return results

}

//创建快递查询
public Map createExtExpressQueryLog() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    Map parameters = parameters

    def expressCom = parameters.expressCom
    def expressNu = parameters.expressNu
    def dataList = parameters.dataList
    def expressStatusId = parameters.expressStatusId

    for (Map temp : dataList) {
        def context = temp.context
        def logTime = temp.time
        def logFTime = temp.ftime
        def logStatus = temp.status
        def areaCode = temp.areaCode
        def areaName = temp.areaName
        def ExtExpressQueryLog = delegator.makeValue("ExtExpressQueryLog", [
                expressStatusId: expressStatusId,
                expressCom     : expressCom,
                expressNu      : expressNu,
                context        : context,
                logStatus      : logStatus,
                statusId       : CommonStatus.ENABLED,
                logTime        : ExtUtilDateTime.parseTimestamp(logTime),
                logFTime       : ExtUtilDateTime.parseTimestamp(logFTime),
                areaName       : areaName,
                areaCode       : areaCode,
                createdDate    : UtilDateTime.nowTimestamp()
        ])
        ExtExpressQueryLog.setNextSeqId()
        ExtExpressQueryLog.create()
    }

    return ServiceUtil.returnSuccess()
}
//更新快递查询
public Map updateExtExpressQueryLog() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    Map parameters = parameters

    def expressStatusId = parameters.expressStatusId
    def extExpressQueryLogList = ExtEntityUtil.findList(delegator, "ExtExpressQueryLog",
            [expressStatusId: expressStatusId])
    //废除现有的数据
    for (GenericValue extExpressQueryLog : extExpressQueryLogList) {
        extExpressQueryLog.statusId = CommonStatus.DISABLED
        extExpressQueryLog.store()
    }

    return ServiceUtil.returnSuccess()
}

//新建快递发送记录
public Map createExtExpressSendLog() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    Map parameters = parameters

    def fromAddress = parameters.fromAddress
    def toAddress = parameters.toAddress
    def trackingNumber = parameters.trackingNumber
    def groupNameLocal = parameters.groupNameLocal

    def extExpressSendLog = delegator.makeValue("ExtExpressSendLog", [
            fromAddress   : fromAddress,
            toAddress     : toAddress,
            trackingNumber: trackingNumber,
            groupNameLocal: groupNameLocal,
            statusId      : CommonStatus.ENABLED,
            createdDate   : UtilDateTime.nowTimestamp()
    ])
    extExpressSendLog.setNextSeqId()
    extExpressSendLog.create()
    def sendId = extExpressSendLog.sendId

    //给快递100发送订阅消息
    def key = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.EXPRESS_API_KEY)
    TaskRequest req = new TaskRequest();
    req.setCompany(groupNameLocal);
    req.setFrom(fromAddress);
    req.setTo(toAddress);
    req.setNumber(trackingNumber);
    req.setKey(key);
    //处理返回的参数
    def responseInfo = Kuaidi100Helper.postData(req, delegator)
    //= "{\"result\":\"true\",\"returnCode\":\"200\",\"message\":\"提交成功\"}";
    def results
    def test = responseInfo.result.toString()
    if (UtilValidate.isEmpty(responseInfo)) {
        return ServiceUtil.returnError("快递100订阅失败");
    } else {
        //更新发送记录的状态和回传消息
        results = dispatcher.runSync("bizUpdateExtExpressSendLog", [
                sendId    : sendId,
                result    : test,
                returnCode: responseInfo.returnCode,
                message   : responseInfo.message
        ])
        if (!ServiceUtil.isSuccess(results)) return results
    }

    return ServiceUtil.returnSuccess()
}
//更新发送返回状态
public Map updateExtExpressSendLog() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    Map parameters = parameters

    def sendId = parameters.sendId
    def result = parameters.result
    def returnCode = parameters.returnCode
    def message = parameters.message

    def extExpressSendLog = ExtEntityUtil.findOne(delegator, "ExtExpressSendLog", [sendId: sendId])
    extExpressSendLog.responseResult = result
    extExpressSendLog.returnCode = returnCode
    extExpressSendLog.responseMessage = message
    extExpressSendLog.store()

    return ServiceUtil.returnSuccess()
}

