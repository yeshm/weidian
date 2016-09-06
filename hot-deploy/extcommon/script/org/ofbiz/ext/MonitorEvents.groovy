package org.ofbiz.ext

import com.juweitu.bonn.constant.Company
import org.ofbiz.base.util.UtilFormatOut
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.base.util.cache.UtilCache
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.connection.DBCPConnectionFactory
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.RenderUtil
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public String http() {
    HttpServletRequest request = request
    HttpServletResponse response = response
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher

    RenderUtil.renderText(response, "ok")

    return "success"
}

public String db() {
    HttpServletRequest request = request
    HttpServletResponse response = response
    Delegator delegator = delegator

    def count = ExtEntityUtil.findCountByFields(delegator, "Party", [partyId: Company.PARTY_ID])

    if (count == 1) {
        RenderUtil.renderText(response, "ok")
    } else {
        RenderUtil.renderText(response, "db access error")
    }
    return "success"
}

public String job() {
    HttpServletRequest request = request
    HttpServletResponse response = response
    Delegator delegator = delegator

    def count = ExtEntityUtil.findCountByFields(delegator, "JobSandbox", [:])

    //任务数量
    if (count >= 10000) {
        RenderUtil.renderText(response, "JobSandbox count ${count}")
        return "success"
    }

    //任务失败数量
    count = ExtEntityUtil.findCountByFields(delegator, "JobSandbox", [statusId: "SERVICE_FAILED"])
    if (count > 0) {
        RenderUtil.renderText(response, "JobSandbox FAILED count ${count}")
        return "success"
    }

    RenderUtil.renderText(response, "ok")

    return "success"
}

public String status() {
    HttpServletRequest request = request
    HttpServletResponse response = response
    Delegator delegator = delegator

    def status = [:]

    //memory
    def totalCacheMemory = 0.0
    names = new TreeSet(UtilCache.getUtilCacheTableKeySet())
    names.each { cacheName ->
        utilCache = UtilCache.findCache(cacheName)
        totalCacheMemory += utilCache.getSizeInBytes()
    }

    rt = Runtime.getRuntime()
    memoryInfo = [:]
    memoryInfo.totalMemory = UtilFormatOut.formatQuantity(rt.totalMemory())
    memoryInfo.freeMemory = UtilFormatOut.formatQuantity(rt.freeMemory())
    memoryInfo.usedMemory = UtilFormatOut.formatQuantity((rt.totalMemory() - rt.freeMemory()))
    memoryInfo.maxMemory = UtilFormatOut.formatQuantity(rt.maxMemory())
    memoryInfo.totalCacheMemory = totalCacheMemory
    status.memory = memoryInfo

    //connectionPool
    def dataSourceInfo = DBCPConnectionFactory.getDataSourceInfo("localpostnew")
    def connectionPool = [:]
    if (UtilValidate.isNotEmpty(dataSourceInfo)) {
        connectionPool.numActive = dataSourceInfo.poolNumActive
        connectionPool.numIdle = dataSourceInfo.poolNumIdle
        connectionPool.numTotal = dataSourceInfo.poolNumTotal
        connectionPool.maxActive = dataSourceInfo.poolMaxActive
        connectionPool.maxIdle = dataSourceInfo.poolMaxIdle
        connectionPool.minIdle = dataSourceInfo.poolMinIdle
        connectionPool.minEvictableIdleTimeMillis = dataSourceInfo.poolMinEvictableIdleTimeMillis
        connectionPool.maxWait = dataSourceInfo.poolMaxWait
    }
    status.connectionPool = connectionPool

    RenderUtil.renderJson(response, status)

    return "success"
}