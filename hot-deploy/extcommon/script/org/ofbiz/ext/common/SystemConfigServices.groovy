package org.ofbiz.ext.common

import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.base.util.cache.UtilCache
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

/**
 * 保存系统配置
 */
public Map saveSystemConfig() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    Map parameters = parameters

    def configTypeEnumId = parameters.configTypeEnumId
    def configValue = parameters.configValue

    GenericValue config = ExtEntityUtil.getOnly(delegator, "ExtSystemConfig", [configTypeEnumId: configTypeEnumId])
    if (UtilValidate.isEmpty(config)) {
        config = delegator.makeValue("ExtSystemConfig", [
                configTypeEnumId: configTypeEnumId,
                configValue     : configValue,
                fromDate        : UtilDateTime.nowTimestamp(),
                createdDate     : UtilDateTime.nowTimestamp(),
                lastModifiedDate: UtilDateTime.nowTimestamp(),
        ])
        config.setNextSeqId()
        config.setNonPKFields(parameters)
        config.create()
    } else {
        config.configValue = configValue
        config.lastModifiedDate = UtilDateTime.nowTimestamp()
        config.setNonPKFields(parameters)
        config.store()
    }

    def results = ServiceUtil.returnSuccess()
    results.configId = config.configId

    return results
}
/**
 * 清除系统全部缓存
 */
public Map cleanAllCache() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    Map parameters = parameters

    //清除系统全部缓存
    UtilCache.clearAllCaches();

    return ServiceUtil.returnSuccess()
}