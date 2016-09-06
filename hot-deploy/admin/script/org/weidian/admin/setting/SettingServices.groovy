package org.weidian.admin.setting

import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil
import org.weidian.constant.common.SystemConfigTypeEnum

public Map saveQiniuCloudConfig() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def accessKey = parameters.accessKey
    def secretKey = parameters.secretKey
    def bucketName = parameters.bucketName
    def urlPrefix = parameters.urlPrefix
    def enable = parameters.enable

    if (UtilValidate.isWhitespace(accessKey) || UtilValidate.isWhitespace(secretKey) || UtilValidate.isWhitespace(bucketName) || UtilValidate.isWhitespace(urlPrefix) || UtilValidate.isWhitespace(enable)) return ServiceUtil.returnError("七牛云存储接口配置失败，参数不完整")

    def results = dispatcher.runSync("bizSaveSystemConfig", [
            configTypeEnumId: SystemConfigTypeEnum.QN_ACCESS_KEY,
            configValue     : accessKey,
            userLogin       : userLogin

    ])
    if (!ServiceUtil.isSuccess(results)) return results

    results = dispatcher.runSync("bizSaveSystemConfig", [
            configTypeEnumId: SystemConfigTypeEnum.QN_SECRET_KEY,
            configValue     : secretKey,
            userLogin       : userLogin

    ])
    if (!ServiceUtil.isSuccess(results)) return results

    results = dispatcher.runSync("bizSaveSystemConfig", [
            configTypeEnumId: SystemConfigTypeEnum.QN_BUCKET_NAME,
            configValue     : bucketName,
            userLogin       : userLogin

    ])
    if (!ServiceUtil.isSuccess(results)) return results

    results = dispatcher.runSync("bizSaveSystemConfig", [
            configTypeEnumId: SystemConfigTypeEnum.QN_URL_PREFIX,
            configValue     : urlPrefix,
            userLogin       : userLogin

    ])
    if (!ServiceUtil.isSuccess(results)) return results

    results = dispatcher.runSync("bizSaveSystemConfig", [
            configTypeEnumId: SystemConfigTypeEnum.QN_ENABLE,
            configValue     : enable,
            userLogin       : userLogin

    ])
    if (!ServiceUtil.isSuccess(results)) return results

    return ServiceUtil.returnSuccess()
}
