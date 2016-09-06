package org.ofbiz.ext.common

import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.biz.ConstantParameter
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map createSurveyResponse() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    Map parameters = parameters

    def surveyId = parameters.surveyId
    Map answers = parameters.answers

    //TODO yeshm 完善校验
    def surveyResponse = delegator.makeValue("SurveyResponse")
    surveyResponse.setNonPKFields(parameters)
    surveyResponse.setNextSeqId()
    surveyResponse.responseDate = UtilDateTime.nowTimestamp()
    surveyResponse.lastModifiedDate = UtilDateTime.nowTimestamp()
    surveyResponse.create()

    //look up the applied questions
    def surveyQuestionAndApplList = delegator.findByAnd("SurveyQuestionAndAppl", [surveyId: surveyId], null, false)
    if (UtilValidate.isEmpty(surveyQuestionAndApplList)) return ServiceUtil.returnError("找不到调查的问题")

    //create the response answers
    for (def surveyQuestionAndAppl : surveyQuestionAndApplList) {
        def currentFieldName = surveyQuestionAndAppl.surveyQuestionId

        if (!answers.containsKey(currentFieldName)) continue

        def answerObjectList = []
        if(UtilValidate.isNotEmpty(answers.get(currentFieldName))){
            if(answers.get(currentFieldName) instanceof String){
                answerObjectList.add(answers.get(currentFieldName))
            }else{
                answerObjectList.addAll(answers.get(currentFieldName))
            }
            for(def surveyOptionSeqId : answerObjectList){

            }
        }else{
            answerObjectList.add(answers.get(currentFieldName))
        }

        for(def answerObject : answerObjectList){
            def responseAnswer = delegator.makeValue("SurveyResponseAnswer", [
                    surveyResponseId    : surveyResponse.surveyResponseId,
                    surveyQuestionId    : surveyQuestionAndAppl.surveyQuestionId,
                    surveyMultiRespColId: ConstantParameter.NA,
                    surveyOptionSeqId   : ConstantParameter.NA
            ])

            if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("BOOLEAN")) {
                responseAnswer.booleanResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("EMAIL")) {
                responseAnswer.textResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("DATE")) {
                responseAnswer.textResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("URL")) {
                responseAnswer.textResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("CREDIT_CARD")) {
                responseAnswer.textResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("GIFT_CARD")) {
                responseAnswer.textResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("PASSWORD")) {
                responseAnswer.textResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("TEXT_SHORT")) {
                responseAnswer.textResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("TEXT_LONG")) {
                responseAnswer.textResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("TEXTAREA")) {
                responseAnswer.textResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("NUMBER_CURRENCY")) {
                responseAnswer.currencyResponse = new BigDecimal(answerObject)
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("NUMBER_FLOAT")) {
                responseAnswer.floatResponse = new Double(answerObject)
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("NUMBER_LONG")) {
                responseAnswer.numericResponse = new Long(answerObject)
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("OPTION")) {
                responseAnswer.surveyOptionSeqId = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("GEO")) {
                responseAnswer.textResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("ENUMERATION")) {
                responseAnswer.textResponse = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("RADIO")) {
                responseAnswer.surveyOptionSeqId = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("CHECKBOX")) {
                responseAnswer.surveyOptionSeqId = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("SELECT_SINGLE")){
                responseAnswer.surveyOptionSeqId = answerObject
            } else if (surveyQuestionAndAppl.surveyQuestionTypeId.equals("SELECT_MUTIL")){
                responseAnswer.surveyOptionSeqId = answerObject
            }

            responseAnswer.answeredDate = UtilDateTime.nowTimestamp()
            if(UtilValidate.isEmpty(responseAnswer.sequenceNum)){
                responseAnswer.sequenceNum = surveyQuestionAndAppl.sequenceNum
            }

            responseAnswer.create()
        }

    }

    def results = ServiceUtil.returnSuccess()
    results.surveyResponseId = surveyResponse.surveyResponseId
    results.surveyId = surveyResponse.surveyId
    return results
}

/**
 * 调整了SurveyResponseAnswer表，添加主键surveyOptionSeqId，如果surveyOptionSeqId为空，这里需要处理一下
 * @return
 */
public Map setSurveyOptionSeqId4SurveyResponseAnswer(){
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def surveyResponseAnswer = parameters.surveyResponseAnswer

    if(UtilValidate.isEmpty(surveyResponseAnswer.surveyOptionSeqId)){
        surveyResponseAnswer.surveyOptionSeqId = ConstantParameter.NA
    }

    return ServiceUtil.returnSuccess()
}