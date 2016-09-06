package org.ofbiz.ext.email

import com.juweitu.bonn.SystemConfigWorker
import com.juweitu.bonn.constant.common.SystemConfigTypeEnum
import javolution.util.FastMap
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map sendEmail() {
    Delegator delegator = delegator
    Map parameters = parameters
    LocalDispatcher dispatcher = dispatcher

    def productStoreId = parameters.productStoreId
    def mailTypeEnumId = parameters.mailTypeEnumId
    def businessModuleTypeEnumId = parameters.businessModuleTypeEnumId
    def sendTo = parameters.sendTo
    def subject = parameters.subject
    def bodyParameters = parameters.bodyParameters
    def bodyScreenUri = parameters.bodyScreenUri

    def enable = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.MAIL_ENABLE)
    def port = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.MAIL_SMTP_PORT)
    def authUser = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.MAIL_USERNAME)
    def authPass = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.MAIL_PASSWD)
    def sendVia = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.MAIL_SMTP_SERVER)

    if (!enable.equals("Y")) return ServiceUtil.returnError("邮件发送功能尚未开启")
    if (UtilValidate.isWhitespace(sendTo) || UtilValidate.isWhitespace(subject) || UtilValidate.isWhitespace(bodyScreenUri) || UtilValidate.isWhitespace(port) || UtilValidate.isWhitespace(authUser) || UtilValidate.isWhitespace(authPass) || UtilValidate.isWhitespace(sendVia) || UtilValidate.isWhitespace(productStoreId) || UtilValidate.isWhitespace(businessModuleTypeEnumId) || UtilValidate.isWhitespace(mailTypeEnumId)) ServiceUtil.returnError("邮件参数不完整")

    Map<String, Object> emailContext = FastMap.newInstance();
    emailContext.put("sendTo", sendTo);
    emailContext.put("sendFrom", SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.MAIL_USERNAME));
    emailContext.put("subject", subject);
    emailContext.put("bodyParameters", bodyParameters);
    emailContext.put("bodyScreenUri", bodyScreenUri);
    emailContext.put("contentType", "text/html");

    emailContext.port = port
    emailContext.authUser = authUser
    emailContext.authPass = authPass
    emailContext.sendVia = sendVia

    def results = dispatcher.runSync("sendMailFromScreen", emailContext)

    //创建邮件发送日志
    def body = results.body
    def errorMessage = ""
    def statusCode = "success"
    if (!ServiceUtil.isSuccess(results)) {
        errorMessage = results.errorMessage
        statusCode = "error"
    }

    def emailSendLog = delegator.makeValue("ExtEmailSendLog", [
            sendFrom    : SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.MAIL_USERNAME),
            body        : body,
            statusCode  : statusCode,
            errorMessage: errorMessage,
            createdDate : UtilDateTime.nowTimestamp()
    ])
    emailSendLog.setNextSeqId()
    emailSendLog.setNonPKFields(parameters)
    emailSendLog.create()

    if (!ServiceUtil.isSuccess(results)) return results

    return ServiceUtil.returnSuccess()
}