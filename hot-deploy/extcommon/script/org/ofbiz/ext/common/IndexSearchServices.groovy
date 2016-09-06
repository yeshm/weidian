package org.ofbiz.ext.common

import com.juweitu.bonn.elasticsearch.ElasticSearchWorker
import org.elasticsearch.index.query.QueryBuilders
import org.ofbiz.base.util.Debug
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map indexGridSearch() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    Map parameters = parameters

    def typeName = parameters.typeName
    def inputFields = parameters.inputFields
    def indexConditionMap = parameters.indexConditionMap
    def orderBy = parameters.orderBy

    def indexName = ElasticSearchWorker.getIndexName(delegator, typeName)

    def queryBuilders = QueryBuilders.boolQuery()

    def queryBuilder = ElasticSearchWorker.createConditionFromInputFields(delegator, typeName, inputFields)
    if (UtilValidate.isNotEmpty(queryBuilder)) queryBuilders.must(queryBuilder)

    queryBuilder = ElasticSearchWorker.createConditionFromInputFields(delegator, typeName, indexConditionMap)
    if (UtilValidate.isNotEmpty(queryBuilder)) queryBuilders.must(queryBuilder)

    Debug.logInfo(queryBuilders.toString(), "")

    orderBy = ElasticSearchWorker.prepareOrderByParameters(inputFields, orderBy)

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizPerformIndexGridSearch", "IN", parameters)
    serviceCtx.indexName = indexName
    serviceCtx.queryBuilder = queryBuilders
    serviceCtx.orderBy = orderBy

    def results = dispatcher.runSync("bizPerformIndexGridSearch", serviceCtx)
    return results
}

public Map indexPagingSearch() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    Map parameters = parameters

    def typeName = parameters.typeName
    def indexConditionMap = parameters.indexConditionMap
    def boolQueryBuilder = parameters.boolQueryBuilder

    def indexName = ElasticSearchWorker.getIndexName(delegator, typeName)
    def queryBuilder = ElasticSearchWorker.createConditionFromInputFields(delegator, typeName, indexConditionMap)
    if (UtilValidate.isNotEmpty(boolQueryBuilder)) queryBuilder.must(boolQueryBuilder)

    Debug.logInfo(queryBuilder.toString(), "")

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizPerformEsFindPage", "IN", parameters)
    serviceCtx.indexName = indexName
    serviceCtx.queryBuilder = queryBuilder

    def results = dispatcher.runSync("bizPerformEsFindPage", serviceCtx)
    return results
}

public Map indexAllListSearch() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    Map parameters = parameters

    def typeName = parameters.typeName
    def indexConditionMap = parameters.indexConditionMap

    def indexName = ElasticSearchWorker.getIndexName(delegator, typeName)
    def queryBuilder = ElasticSearchWorker.createConditionFromInputFields(delegator, typeName, indexConditionMap)

    Debug.logInfo(queryBuilder.toString(), "")

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizPerformIndexAllListSearch", "IN", parameters)
    serviceCtx.indexName = indexName
    serviceCtx.queryBuilder = queryBuilder

    def results = dispatcher.runSync("bizPerformIndexAllListSearch", serviceCtx)
    return results
}

public Map checkIndexChange() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    Map parameters = parameters

    def typeName = parameters.typeName
    def indexConditionMap = parameters.indexConditionMap
    def message = parameters.message
    if (UtilValidate.isWhitespace(parameters.message)) {
        message = "操作成功"
    }

    //由于是消息队列处理,有延迟,页面等数据处理完毕后再刷新
    for (int i = 0; i < 20; i++) {
        def results = dispatcher.runSync("bizIndexAllListSearch", [
                typeName         : typeName,
                indexConditionMap: indexConditionMap
        ])
        def list = results.list
        if (list.size > 0) {
            return ServiceUtil.returnSuccess(message)
        }
        Thread.sleep(500)
    }
    return ServiceUtil.returnSuccess("操作成功")
}