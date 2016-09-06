package org.weidian.admin.setting

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.weidian.SystemConfigWorker
import org.weidian.constant.common.SystemConfigTypeEnum

import javax.servlet.http.HttpServletRequest

def editQiniuCloudConfig(Map context) {
    Delegator delegator = delegator
    HttpServletRequest request = request
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def accessKey = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.QN_ACCESS_KEY)
    def secretKey = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.QN_SECRET_KEY)
    def bucketName = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.QN_BUCKET_NAME)
    def urlPrefix = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.QN_URL_PREFIX)
    def enable = SystemConfigWorker.getSystemConfig(delegator, SystemConfigTypeEnum.QN_ENABLE)

    context.accessKey = accessKey
    context.secretKey = secretKey
    context.bucketName = bucketName
    context.urlPrefix = urlPrefix
    context.enable = enable
}
