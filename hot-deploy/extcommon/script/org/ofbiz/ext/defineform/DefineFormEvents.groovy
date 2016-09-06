package org.ofbiz.ext.defineform

import com.fasterxml.jackson.databind.JavaType
import com.juweitu.bonn.merchant.MerchantWorker
import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.condition.EntityOperator
import org.ofbiz.ext.util.AppUtil
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.MessageUtil
import org.ofbiz.ext.util.RequestUtil
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest

public String saveDefineForm() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    def array = requestParameters.array
    def productStoreId = MerchantWorker.getCurrentProductStoreId(request)

    def defineForm = AppUtil.fromJson(requestParameters.defineForm, HashMap.class)

    JavaType javaType = AppUtil.getMapper().createCollectionType(ArrayList.class, HashMap.class)
    def defineFormFieldList = AppUtil.fromJson(array, javaType)

    def results = dispatcher.runSync("bizSaveDefineForm", [
            productStoreId    : productStoreId,
            userLogin          : userLogin,
            defineForm         : defineForm,
            defineFormFieldList: defineFormFieldList
    ])

    def formId = results.defineFormId
    request.setAttribute("formId", formId)

    return MessageUtil.handleServiceResults(request, results)
}

public String gridRichMediaDefineFormStatistics(){
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    def requestParameters = UtilHttp.getParameterMap(request)
    Delegator delegator = delegator

    def viewIndex = RequestUtil.getRequestPageIndex(request)
    def viewSize = RequestUtil.getRequestPageSize(request)

    def formId = requestParameters.formId
    def searchMobile = requestParameters.searchMobile
    def surveyId

    def defineForm = delegator.findOne("BizDefineForm",[formId : formId],true)

    if(UtilValidate.isNotEmpty(defineForm)){
        surveyId = defineForm.surveyId
    }else{
        return "error"
    }

    def entityConditionList = [
            EntityCondition.makeCondition("surveyId", surveyId)
    ]

    def result = dispatcher.runSync("performFindPage", [
            inputFields        : requestParameters,
            entityName         : "SurveyResponse",
            entityConditionList: EntityCondition.makeCondition(entityConditionList),
            orderBy            : "-responseDate",
            viewIndex          : viewIndex,
            viewSize           : viewSize,
            timeZone           : timeZone
    ])


    if (UtilValidate.isNotEmpty(result) && UtilValidate.isNotEmpty(result.list)) {
        def tempPagingContent = []
        result.list.each { it ->
            Map temp = [:]

            //获取电话
            def entitycondition = EntityCondition.makeCondition([
                    EntityCondition.makeCondition("surveyId",surveyId),
                    EntityCondition.makeCondition("surveyQuestionTypeId","TEXT_SHORT"),
                    EntityCondition.makeCondition("limitTypeId","PHONE_LIMIT"),
                    EntityCondition.makeCondition("question","手机号码")
            ])

            def surveyQuestionOfMobile = ExtEntityUtil.getFirst(delegator,"SurveyQuestionAndAppl",entitycondition)

            if(UtilValidate.isNotEmpty(surveyQuestionOfMobile)){
                def questionId = surveyQuestionOfMobile.surveyQuestionId
                def responseId = it.surveyResponseId

                def entityconditionListForMobile = [
                        EntityCondition.makeCondition("surveyQuestionId",questionId),
                        EntityCondition.makeCondition("surveyResponseId",responseId)
                ]

                if(UtilValidate.isNotEmpty(searchMobile)){
                    entityconditionListForMobile.add(EntityCondition.makeCondition("textResponse",EntityOperator.LIKE,"%"+searchMobile+"%"))
                }

                def surveyResponseOfMobile = delegator.findList("SurveyResponseAnswer",EntityCondition.makeCondition(entityconditionListForMobile),null,null,null,true)
                if(UtilValidate.isNotEmpty(surveyResponseOfMobile)){
                    def mobile = surveyResponseOfMobile.textResponse

                    //获取图文素材的题目
                    def materialId = defineForm.instanceId
                    def materialRichMedia = delegator.findOne("BizMaterialRichMedia",[materialId : materialId],true)
                    def richMediaName = materialRichMedia.richMediaName

                    temp.put("mobile", mobile)
                    temp.put("richMediaName", richMediaName)
                    temp.putAll(it)
                    tempPagingContent.add(temp)
                }
            }
        }
        result.put("list", tempPagingContent)
    }

    request.setAttribute("result", result)

    return "success"
}
