package org.ofbiz.ext.defineform

import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.condition.EntityOperator
import org.ofbiz.ext.util.ExtEntityUtil

def defineForm(Map context) {
    Delegator delegator = delegator
    def requestParameters = UtilHttp.getParameterMap(request)

    def formId = requestParameters.formId

    def entityCondition = EntityCondition.makeCondition([
            EntityCondition.makeCondition("surveyQuestionTypeId", EntityOperator.IN, ["RADIO", "CHECKBOX", "SELECT_SINGLE", /*"SELECT_MUTIL",*/ "DATE", "PASSWORD", "TEXT_SHORT", "TEXTAREA"])
    ])

    def surveyQuestionTypeList = ExtEntityUtil.findList(delegator, "SurveyQuestionType", entityCondition)
    context.surveyQuestionTypeList = surveyQuestionTypeList

    def limitTypeList = ExtEntityUtil.findAll(delegator, "BizSurveyQuestionLimitType")
    context.limitTypeList = limitTypeList

    if (UtilValidate.isNotEmpty(formId)) {
        def defineForm = delegator.findOne("BizDefineForm", [formId: formId], true)
        def surveyId = defineForm.surveyId

        def surveyQuestionList = delegator.findByAnd("SurveyQuestionAndAppl", [surveyId: surveyId], ["sequenceNum"], true)
        context.surveyQuestionList = surveyQuestionList
        context.defineForm = defineForm
    }
}
