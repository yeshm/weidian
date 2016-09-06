package org.ofbiz.demo

import com.juweitu.bonn.constant.common.SystemConfigTypeEnum
import org.ofbiz.base.util.Debug
import org.ofbiz.base.util.UtilMisc
import org.ofbiz.base.util.UtilProperties
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.util.EntityUtil
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.LockWorker
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

//外键锁演示 begin
public Map pkLockOne() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator

    def partyIdentification = delegator.makeValue("PartyIdentification", [
            partyId                  : "system",
            partyIdentificationTypeId: "INVOICE_EXPORT"
    ])
//    通过外键获得了Party的共享锁
    partyIdentification.create()

//    开启新的事务执行另一个service
    def results = dispatcher.runSync("bizDemoPKLockTwo", [:], 1000, true)
    if (!ServiceUtil.isSuccess(results)) return results

    return ServiceUtil.returnSuccess()
}

public Map pkLockTwo() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator

//    要获取Party的排它锁，需等待别的地方共享锁和排它锁的释放，导致死锁
    LockWorker.lockEntity(delegator, "Party", [partyId: "system"])

    return ServiceUtil.returnSuccess()
}

public Map pkLockThree() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator

//    要获取Party的排它锁，需等待别的地方共享锁和排它锁的释放，导致死锁
    LockWorker.lockEntity(delegator, "Party", [partyId: "system"])

    //    开启新的事务执行另一个service
    def results = dispatcher.runSync("bizDemoPKLockFour", [:], 1000, true)
    if (!ServiceUtil.isSuccess(results)) return results

    return ServiceUtil.returnSuccess()
}

public Map pkLockFour() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator

    def partyIdentification = delegator.makeValue("PartyIdentification", [
            partyId                  : "system",
            partyIdentificationTypeId: "INVOICE_EXPORT"
    ])
//    通过外键获得了Party的共享锁
    partyIdentification.create()

    return ServiceUtil.returnSuccess()
}
//外键锁演示 end

/**
 * properties vs entity cache 性能比较
 * @return
 */
public Map propertiesVsEntityCache() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator

    for(int a=0;a<100;a++) {

        def startTime = System.currentTimeMillis()

        EntityCondition entityCondition = EntityCondition.makeCondition(UtilMisc.toList(
                EntityCondition.makeCondition("configTypeEnumId", SystemConfigTypeEnum.BAIDU_MAP_AK),
                EntityUtil.getFilterByDateExpr()
        ));

        GenericValue systemConfig
        for (int i = 0; i < 10000; i++) {
            systemConfig = ExtEntityUtil.getOnlyCache(delegator, "ExtSystemConfig", entityCondition)
        }
        Debug.logInfo("entity cache get systemConfig: ${systemConfig}", "")
        Debug.logInfo("entity cache use ${System.currentTimeMillis() - startTime}", "")

        def mDomain
        for (int i = 0; i < 10000; i++) {
            mDomain = UtilProperties.getPropertyValue("biz", "m.domain")
        }
        Debug.logInfo("properties get mDomain:${mDomain}, use ${System.currentTimeMillis() - startTime}", "")

        Thread.sleep(1000)
    }

    return ServiceUtil.returnSuccess()
}