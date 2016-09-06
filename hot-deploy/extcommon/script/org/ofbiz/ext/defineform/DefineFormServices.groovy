package org.ofbiz.ext.defineform

import com.juweitu.bonn.constant.common.CommonStatus
import javolution.util.FastMap
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.SecurityUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

import java.util.regex.Pattern

public Map saveDefineForm() {
    Delegator delegator = delegator
    Map parameters = parameters
    LocalDispatcher dispatcher = dispatcher

    def defineFormFieldList = parameters.defineFormFieldList
    Map defineFormm = parameters.defineForm

    def system = SecurityUtil.getSystemUserLogin(delegator)
    def defineFormId = defineFormm.formId

    if (UtilValidate.isNotEmpty(defineFormId)) {
        //编辑表单
        //修改BizDefineForm   表单项不做修改
        def defineForm = delegator.findOne("BizDefineForm", [formId: defineFormId], false)
        defineFormm.phoneSubmitTimes = new Long(defineFormm.phoneSubmitTimes)
        defineForm.setNonPKFields(defineFormm)
        defineForm.store()

//        def defineFormForCreate = delegator.findOne("BizDefineForm",[formId:defineFormm.formId],true)
//        def surveyId = defineFormForCreate.surveyId
//        List SurveyQuestionAndApplList = delegator.findByAnd("SurveyQuestionAndAppl",[surveyId:surveyId],null,true)
//
//        for(def defineFormFieldMap : defineFormFieldList){
//            defineFormFieldMap.put("userLogin",system)
//            if(UtilValidate.isNotEmpty(defineFormFieldMap.surveyQuestionId)){
//                //修改字段
//                SurveyQuestionAndApplList.remove(ExtEntityUtil.getOnly(delegator,"SurveyQuestionAndAppl",[surveyQuestionId: defineFormFieldMap.surveyQuestionId]))
//
//                defineFormFieldMap.put("question",defineFormFieldMap.fieldIntro );
//                results = dispatcher.runSync("updateSurveyQuestion",defineFormFieldMap)
//                if (!ServiceUtil.isSuccess(results)) return results
//
//                def surveyQuestionAppl = ExtEntityUtil.getOnly(delegator,"SurveyQuestionAppl",[surveyId : surveyId,surveyQuestionId : defineFormFieldMap.surveyQuestionId])
//
//                defineFormFieldMap.put("surveyId",surveyId)
//                defineFormFieldMap.put("fromDate",surveyQuestionAppl.fromDate)
//                results = dispatcher.runSync("updateSurveyQuestionAppl",defineFormFieldMap)
//                if (!ServiceUtil.isSuccess(results)) return results
//            }else{
//                //新增字段
//
//                def results = dispatcher.runSync("createSurveyQuestion",[
//                        surveyQuestionTypeId : defineFormFieldMap.surveyQuestionTypeId,
//                        userLogin : system,
//                        question : defineFormFieldMap.fieldIntro
//                ])
//
//                if (!ServiceUtil.isSuccess(results)) return results
//                def surveyQuestionId = results.surveyQuestionId
//
//                //将字段与表单关联
//                defineFormFieldMap.put("surveyQuestionId",surveyQuestionId)
//                defineFormFieldMap.put("userLogin",system)
//                defineFormFieldMap.put("surveyId",surveyId)
//                results = dispatcher.runSync("createSurveyQuestionAppl",defineFormFieldMap)
//
//                if (!ServiceUtil.isSuccess(results)) return results
//
//            }
//        }
//
//        //删除
//        for(def SurveyQuestionAndAppl : SurveyQuestionAndApplList){
//
//            //删除SurveyQuestionAppl
//            def results = dispatcher.runSync("deleteSurveyQuestionAppl",[
//                    surveyId : SurveyQuestionAndAppl.surveyId,
//                    surveyQuestionId: SurveyQuestionAndAppl.surveyQuestionId,
//                    fromDate : SurveyQuestionAndAppl.fromDate,
//                    userLogin: system
//            ])
//            if (!ServiceUtil.isSuccess(results)) return results
//
//            //删除SurveyQuestion
//            results = dispatcher.runSync("deleteSurveyQuestion",[surveyQuestionId : SurveyQuestionAndAppl.surveyQuestionId,userLogin: system])
//            if (!ServiceUtil.isSuccess(results)) return results
//        }
    } else {
        //新建表单
        //创建一个Survey
        def results = dispatcher.runSync("createSurvey", [
                surveyName : "Create a define form",
                description: "Create a define form",
                userLogin  : system,
                allowUpdate: "Y"
        ])

        if (!ServiceUtil.isSuccess(results)) return results

        def surveyId = results.surveyId

        //创建BizDefineForm
        defineFormm.createdDate = UtilDateTime.nowTimestamp()
        defineFormm.statusId = CommonStatus.ENABLED
        defineFormm.surveyId = surveyId
        defineFormm.phoneSubmitTimes = new Long(defineFormm.phoneSubmitTimes)

        def defineForm = delegator.makeValue("BizDefineForm")
        defineForm.setNextSeqId()
        defineForm.setNonPKFields(defineFormm)
        defineForm.create()

        defineFormId = defineForm.formId

        for (def defineFormFieldMap : defineFormFieldList) {
            //创建字段
            results = dispatcher.runSync("createSurveyQuestion", [
                    surveyQuestionTypeId: defineFormFieldMap.surveyQuestionTypeId,
                    userLogin           : system,
                    question            : defineFormFieldMap.fieldIntro
            ])

            if (!ServiceUtil.isSuccess(results)) return results
            def surveyQuestionId = results.surveyQuestionId

            //将字段与表单关联
            defineFormFieldMap.surveyQuestionId = surveyQuestionId
            defineFormFieldMap.surveyId = surveyId
            defineFormFieldMap.userLogin = system
            results = dispatcher.runSync("createSurveyQuestionAppl", defineFormFieldMap)

            if (!ServiceUtil.isSuccess(results)) return results
        }
    }

    def results = ServiceUtil.returnSuccess()
    results.defineFormId = defineFormId

    return results
}


public Map saveArticleDefineForm() {
    Delegator delegator = delegator
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def defineFormValueData = parameters.defineFormValueData
    def mobile = parameters.mobile
    def formId = parameters.formId
    def ip = parameters.ip
    Map map = FastMap.newInstance()

    for (def defineFormValue : defineFormValueData) {
        def surveyQuestionId = defineFormValue.surveyQuestionId
        def answer = defineFormValue.answerValue
        if (UtilValidate.isEmpty(surveyQuestionId)) {
            continue
        }

        //校验用户输入
        def surveyQuestionAndAppl = ExtEntityUtil.getFirstCache(delegator, "SurveyQuestionAndAppl", [surveyQuestionId: surveyQuestionId])

        if (UtilValidate.isNotEmpty(surveyQuestionAndAppl.regexValue) && !UtilValidate.isInteger(surveyQuestionAndAppl.regexValue) && UtilValidate.isNotEmpty(answer)) {
            if (Pattern.matches(surveyQuestionAndAppl.regexValue, answer) == false) {
                if (UtilValidate.isNotEmpty(surveyQuestionAndAppl.errorHint)) {
                    return ServiceUtil.returnError(surveyQuestionAndAppl.errorHint)
                } else {
                    return ServiceUtil.returnError(surveyQuestionAndAppl.question + "输入格式错误")
                }
            }
        }
        map.put(surveyQuestionId, answer)
    }

    def defineForm = delegator.findOne("BizDefineForm", [formId: formId], true)
    def phoneSubmitTimes = defineForm.phoneSubmitTimes

    def entityCondition = EntityCondition.makeCondition([
            EntityCondition.makeCondition("surveyId", defineForm.surveyId),
            EntityCondition.makeCondition("textResponse", mobile)
    ])

    def defineFormAnswerValueCount = ExtEntityUtil.findCountByCondition(delegator, "SurveyResponseAndAnswer", entityCondition)

    if (defineFormAnswerValueCount >= phoneSubmitTimes) {
        return ServiceUtil.returnError("每个手机号只能提交" + phoneSubmitTimes + "次")
    }

    def results = dispatcher.runSync("bizCreateSurveyResponse", [
            surveyId: defineForm.surveyId,
            partyId : userLogin ? userLogin.partyId : "",
            answers : map,
            ip      : ip
    ])

    if (!ServiceUtil.isSuccess(results)) return results

    return ServiceUtil.returnSuccess()
}