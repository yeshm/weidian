package org.ofbiz.ext.product

import com.juweitu.bonn.elasticsearch.ElasticSearchTypeName
import com.juweitu.bonn.elasticsearch.ElasticSearchWorker
import groovy.transform.Field
import org.elasticsearch.index.query.IdsQueryBuilder
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.index.query.QueryStringQueryBuilder
import org.ofbiz.base.util.Debug
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.service.LocalDispatcher

@Field
def typeName = ElasticSearchTypeName.PRODUCT

public Map searchByKeyword() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    Map parameters = parameters

    def productStoreId = parameters.productStoreId
    def keyword = parameters.keyword
    def productCategoryId = parameters.productCategoryId
    def orderBy = parameters.orderBy
    def order = parameters.order
    def pageIndex = parameters.pageIndex
    def pageSize = parameters.pageSize
    def explain = parameters.explain
    def isHighlight = parameters.isHighlight

    if (UtilValidate.isEmpty(pageIndex)) pageIndex = 0
    if (UtilValidate.isEmpty(pageSize)) pageSize = 10
    if (UtilValidate.isEmpty(explain)) explain = false
    if (UtilValidate.isEmpty(isHighlight)) isHighlight = true

    def indexName = ElasticSearchWorker.getIndexName(delegator, typeName)

    def salesDiscontinuationDateQueryBuilder = QueryBuilders.boolQuery()
    salesDiscontinuationDateQueryBuilder.should(QueryBuilders.missingQuery("salesDiscontinuationDate"))
    salesDiscontinuationDateQueryBuilder.should(QueryBuilders.rangeQuery("salesDiscontinuationDate").gte("now"))
    salesDiscontinuationDateQueryBuilder.minimumNumberShouldMatch(1)

    def queryBuilder = QueryBuilders.boolQuery()
    queryBuilder.must(salesDiscontinuationDateQueryBuilder)
    queryBuilder.must(QueryBuilders.rangeQuery("releaseDate").lte("now"))
    queryBuilder.must(QueryBuilders.termsQuery("isShow", true))

    if (UtilValidate.isNotEmpty(keyword)) {
        def keywordQueryBuilder = QueryBuilders.queryStringQuery(keyword)
        keywordQueryBuilder.defaultOperator(QueryStringQueryBuilder.Operator.AND)
        keywordQueryBuilder.field("searchText")

        queryBuilder.must(keywordQueryBuilder)

        queryBuilder.must(QueryBuilders.termsQuery("productStoreId", productStoreId))

        if (!UtilValidate.isEmpty(productCategoryId)) {
            queryBuilder.must(QueryBuilders.termsQuery("productCategoryId", productCategoryId))
        }
    } else {
        queryBuilder.must(QueryBuilders.termQuery("productStoreId", productStoreId))
        if (!UtilValidate.isEmpty(productCategoryId)) {
            queryBuilder.must(QueryBuilders.termQuery("productCategoryId", productCategoryId))
        }
    }
    Debug.logInfo(queryBuilder.toString(), "")

    orderBy = orderBy != null ? ("desc".equals(order.toLowerCase()) ? "-" + orderBy : orderBy) : orderBy
    if (UtilValidate.isNotEmpty(orderBy) && orderBy.contains("defaultPrice")) orderBy = orderBy

    def esResult = dispatcher.runSync("bizPerformEsFindPage", [
            indexName   : indexName,
            typeName    : typeName,
            queryBuilder: queryBuilder,
            orderBy     : orderBy,
            pageIndex   : pageIndex,
            pageSize    : pageSize,
            explain     : explain,
            isHighlight : isHighlight
    ])

    return esResult
}

public Map searchByIds() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    Map parameters = parameters

    def pageIndex = parameters.pageIndex
    def pageSize = parameters.pageSize
    def productIds = parameters.productIds

    def indexName = ElasticSearchWorker.getIndexName(delegator, typeName)
    QueryBuilder queryBuilder = IdsQueryBuilder.newInstance()
    for (def id : productIds) {
        queryBuilder.addIds(id)
    }

    Debug.logInfo(queryBuilder.toString(), "")

    def esResult = dispatcher.runSync("bizPerformEsFindPage", [
            indexName   : indexName,
            typeName    : typeName,
            queryBuilder: queryBuilder,
            pageIndex   : pageIndex,
            pageSize    : pageSize
    ])

    return esResult
}