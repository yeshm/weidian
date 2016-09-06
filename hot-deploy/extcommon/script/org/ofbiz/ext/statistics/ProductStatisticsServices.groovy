package org.ofbiz.ext.statistics

import com.juweitu.bonn.constant.order.OrderStatus
import com.juweitu.bonn.elasticsearch.ElasticSearchTypeName
import com.juweitu.bonn.elasticsearch.ElasticSearchWorker
import groovy.transform.Field
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.service.LocalDispatcher

@Field
def typeNameProductPvUv = ElasticSearchTypeName.PRODUCT_PV_UV
@Field
def typeNameProductSale = ElasticSearchTypeName.PRODUCT_SALE

public Map productPvUvStatistics() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    Map parameters = parameters

    def productStoreId = parameters.productStoreId
    def startDate = parameters.startDate
    def endDate = parameters.endDate

    startDate = ElasticSearchWorker.transferDateTimeStringToMappingFormat(startDate)
    endDate = ElasticSearchWorker.transferDateTimeStringToMappingFormat(endDate)

    def indexName = ElasticSearchWorker.getIndexName(delegator, typeNameProductPvUv)
    QueryBuilder queryBuilder = QueryBuilders.boolQuery()
    queryBuilder.must(QueryBuilders.rangeQuery("visitDate").gte(startDate).lte(endDate))
    if (!UtilValidate.isWhitespace(productStoreId)) queryBuilder.must(QueryBuilders.termQuery("productStoreId", productStoreId))

//    FilterBuilder filterBuilder
//    if (!UtilValidate.isWhitespace(productStoreId)) filterBuilder = FilterBuilders.termFilter("productStoreId", productStoreId)

    def esResult = dispatcher.runSync("bizPerformEsFindProductPvUvStatistics", [
            indexName   : indexName,
            typeName    : typeNameProductPvUv,
            queryBuilder: queryBuilder,
//            orderBy     : "-quantity"
    ])

    return esResult
}

public Map productSaleStatistics() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    Map parameters = parameters

    def productStoreId = parameters.productStoreId
    def startDate = parameters.startDate
    def endDate = parameters.endDate

    startDate = ElasticSearchWorker.transferDateTimeStringToMappingFormat(startDate)
    endDate = ElasticSearchWorker.transferDateTimeStringToMappingFormat(endDate)

    def indexName = ElasticSearchWorker.getIndexName(delegator, typeNameProductSale)

    QueryBuilder queryBuilder = QueryBuilders.boolQuery()
    queryBuilder.must(QueryBuilders.rangeQuery("orderDate").gte(startDate).lte(endDate))
    queryBuilder.mustNot(QueryBuilders.termQuery("orderStatus", OrderStatus.CANCELLED))
    queryBuilder.must(QueryBuilders.termQuery("isFinishGood", true))

    if (!UtilValidate.isWhitespace(productStoreId)) queryBuilder.must(QueryBuilders.termQuery("productStoreId", productStoreId))

//    FilterBuilder filterBuilder = FilterBuilders.notFilter(FilterBuilders.termFilter("orderStatus", OrderStatus.CANCELLED))

    def esResult = dispatcher.runSync("bizPerformEsFindProductSaleStatistics", [
            indexName   : indexName,
            typeName    : typeNameProductSale,
            queryBuilder: queryBuilder,
            orderBy     : "-quantity"
    ])

    return esResult
}