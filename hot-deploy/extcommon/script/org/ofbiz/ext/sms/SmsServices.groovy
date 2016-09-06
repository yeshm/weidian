package org.ofbiz.ext.sms

import com.juweitu.bonn.SystemConfigWorker
import com.juweitu.bonn.constant.common.CaptchaStatus
import com.juweitu.bonn.constant.common.CaptchaTypeEnum
import com.juweitu.bonn.constant.common.SystemConfigTypeEnum
import com.juweitu.bonn.constant.sms.SmsContentTypeEnum
import com.juweitu.bonn.sms.chanzor.ChanzorSmsClient
import com.juweitu.bonn.sms.emay.EmaySmsClient
import org.apache.commons.lang.RandomStringUtils
import org.ofbiz.base.util.*
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.condition.EntityOperator
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map createSmsLog() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    Map parameters = parameters

    //注意 主键是外部传过来的
    def smsLog = delegator.makeValidValue("ExtSmsLog", parameters)
    smsLog.createdDate = UtilDateTime.nowTimestamp()
    smsLog.create()

    def results = ServiceUtil.returnSuccess()
    results.smsLogId = smsLog.smsLogId
    return results
}

public Map updateSmsLog() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    Map parameters = parameters

    def taskId = parameters.taskId

    def smsLog = ExtEntityUtil.getOnly(delegator, "ExtSmsLog", [taskId: taskId])
    smsLog.setNonPKFields(parameters)
    smsLog.store()

    def results = ServiceUtil.returnSuccess()
    results.smsLogId = smsLog.smsLogId
    return results
}

public Map sendSms() {
    Map parameters = parameters
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher

    def productStoreId = parameters.productStoreId
    def senderPartyId = parameters.senderPartyId
    def businessModuleTypeEnumId = parameters.businessModuleTypeEnumId
    def receiverMobile = parameters.receiverMobile
    def sendContent = parameters.sendContent
    def smsTypeEnumId = parameters.smsTypeEnumId

    if (UtilValidate.isWhitespace(productStoreId) || UtilValidate.isWhitespace(businessModuleTypeEnumId) || UtilValidate.isWhitespace(receiverMobile) || UtilValidate.isWhitespace(sendContent) || UtilValidate.isWhitespace(smsTypeEnumId)) return ServiceUtil.returnError("参数不完整")

    def smsStatus = "0"
    def smsLogId = delegator.getNextSeqId("ExtSmsLog")

    //添加 短信内容前置签名【短信内容需带签名前缀，否则收不到短信】
    def smsSignature = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.SMS_SIGNATURE)
//    sendContent = "【" + smsSignature + "】"+sendContent
    sendContent = sendContent + "【" + smsSignature + "】"

    def results = ChanzorSmsClient.sendSMS(delegator, receiverMobile, sendContent, smsLogId)

    if (!ServiceUtil.isSuccess(results)) return results

    results = dispatcher.runSync("bizCreateSmsLog", [
            smsLogId                : smsLogId,
            productStoreId          : productStoreId,
            senderPartyId           : senderPartyId,
            businessModuleTypeEnumId: businessModuleTypeEnumId,
            receiverMobile          : receiverMobile,
            sendContent             : sendContent,
            smsTypeEnumId           : smsTypeEnumId,
            taskId                  : results.taskId,
            smsStatus               : smsStatus,
            receiveContent          : results.receiveContent
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    results = ServiceUtil.returnSuccess()
    results.smsLogId = smsLogId

    return results
}

public Map verifyCaptcha() {
    Delegator delegator = delegator
    Map parameters = parameters

    def captcha = parameters.captcha
    def bindTypeEnumId = parameters.bindTypeEnumId
    def receiverMobile = parameters.receiverMobile
    def receiverPartyId = parameters.receiverPartyId

    def entityConditionList = [
            EntityCondition.makeCondition("captcha", captcha),
            EntityCondition.makeCondition("statusId", CaptchaStatus.NOT_VERIFIED),
            EntityCondition.makeCondition("invalidDate", EntityOperator.GREATER_THAN, UtilDateTime.nowTimestamp())
    ]
    def captchaEntity = null

    if (bindTypeEnumId.equals(CaptchaTypeEnum.MOBILE)) {
        entityConditionList.add(EntityCondition.makeCondition("receiverMobile", receiverMobile))
        if (UtilValidate.isNotEmpty(receiverPartyId)) {
            entityConditionList.add(EntityCondition.makeCondition("receiverPartyId", receiverPartyId))
        }
        captchaEntity = ExtEntityUtil.getFirst(delegator.findList("ExtSmsLogAndCaptchaView", EntityCondition.makeCondition(entityConditionList), null, ["-invalidDate"], null, false))
    }
    //TODO 如果其他方式，在这里继续添加

    if (UtilValidate.isNotEmpty(captchaEntity)) {
        def captchaValue = delegator.findOne("ExtCaptcha", [captchaId: captchaEntity.captchaId], false)
        captchaValue.verifyDate = UtilDateTime.nowTimestamp()
        captchaValue.statusId = CaptchaStatus.VERIFIED

        captchaValue.store()
    } else {//如果为空，说明校验码错误的
        return ServiceUtil.returnError("验证码错误，请重新输入！")
    }

    return ServiceUtil.returnSuccess()
}

public Map sendCaptchaSms() {
    Map parameters = parameters
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher

    def productStoreId = parameters.productStoreId
    def businessModuleTypeEnumId = parameters.businessModuleTypeEnumId
    def receiverMobile = parameters.receiverMobile

    if (UtilValidate.isWhitespace(productStoreId) || UtilValidate.isWhitespace(businessModuleTypeEnumId) || UtilValidate.isWhitespace(receiverMobile)) return ServiceUtil.returnError("参数不完整")

    def createdDate = UtilDateTime.nowTimestamp()
    def invalidDate = UtilDateTime.adjustTimestamp(createdDate, Calendar.MINUTE, 10)
    def captcha = RandomStringUtils.randomNumeric(5);
    def sendContent = captcha + "是您的验证码，10分钟内验证有效"

    //校验用户当天发送验证码短信的条数，如果条数超过条数，则不可再发
    def timesArea = Integer.parseInt(UtilProperties.getPropertyValue("biz.properties", "sms.limit.timesArea"))
    def smsSendNumAtLimitTimesArea = Integer.parseInt(UtilProperties.getPropertyValue("biz.properties", "sms.sendNum.atLimitTimesArea"))
    def nowTime = UtilDateTime.nowTimestamp()

    def entityCondition = EntityCondition.makeCondition([
            EntityCondition.makeCondition("createdDate", EntityOperator.BETWEEN, UtilMisc.toList(UtilDateTime.adjustTimestamp(nowTime, Calendar.HOUR, -timesArea), nowTime)),
            EntityCondition.makeCondition("toBindInfo", receiverMobile),
            EntityCondition.makeCondition("statusId", CaptchaStatus.NOT_VERIFIED),
    ])

    def captchaSmsList = ExtEntityUtil.findList(delegator, "ExtCaptcha", entityCondition)
    if (captchaSmsList.size() >= smsSendNumAtLimitTimesArea) {
        return ServiceUtil.returnError("抱歉，您在" + timesArea + "小时内发送了" + smsSendNumAtLimitTimesArea + "条验证短信，请稍后再试！")
    }

    entityCondition = EntityCondition.makeCondition([
            EntityCondition.makeCondition("createdDate", EntityOperator.GREATER_THAN, UtilDateTime.adjustTimestamp(nowTime, Calendar.MINUTE, -1)),
            EntityCondition.makeCondition("toBindInfo", receiverMobile)
    ])
    captchaSmsList = ExtEntityUtil.findList(delegator, "ExtCaptcha", entityCondition)
    if (captchaSmsList.size() > 0) {
        return ServiceUtil.returnError("请一分钟后重新获取验证码！")
    }

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizSendSms", "IN", parameters)
    serviceCtx.sendContent = sendContent
    serviceCtx.smsTypeEnumId = SmsContentTypeEnum.VERIFY

    def results = dispatcher.runSync("bizSendSms", serviceCtx)
    if (!ServiceUtil.isSuccess(results)) return results

    def captchaEntity = delegator.makeValue("ExtCaptcha", [
            instanceId    : results.smsLogId,
            captcha       : captcha,
            createdDate   : createdDate,
            invalidDate   : invalidDate,
            toBindInfo    : receiverMobile,
            bindTypeEnumId: CaptchaTypeEnum.MOBILE,
            statusId      : CaptchaStatus.NOT_VERIFIED
    ])
    captchaEntity.setNextSeqId()
    captchaEntity.create()

    Debug.logInfo(sendContent, "")

    return ServiceUtil.returnSuccess()
}