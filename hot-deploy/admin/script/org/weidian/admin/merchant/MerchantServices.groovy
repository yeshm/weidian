package org.weidian.admin.merchant

import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.entity.util.EntityUtil
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil
import org.weidian.constant.party.SecurityGroup

public Map createMerchant() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def storeName = parameters.storeName
    def telephone = parameters.telephone
    def currentPassword = parameters.currentPassword
    def currentPasswordVerify = parameters.currentPasswordVerify

    def userLoginNew = ExtEntityUtil.findOne(delegator, "UserLogin", [userLoginId: telephone])
    if (UtilValidate.isNotEmpty(userLoginNew)) return ServiceUtil.returnError("该手机号码已经被注册")

    def productStore = ExtEntityUtil.getOnly(delegator, "BizProductStore", [telephone: telephone])
    if (UtilValidate.isNotEmpty(productStore)) return ServiceUtil.returnError("该手机号码已经被注册")

    productStore = delegator.makeValue("BizProductStore", [
            storeName              : storeName,
            telephone              : telephone,
            createdDate            : UtilDateTime.nowTimestamp(),
            createdByUserLogin     : userLogin.userLoginId,
            lastModifiedDate       : UtilDateTime.nowTimestamp(),
            lastModifiedByUserLogin: userLogin.userLoginId
    ])
    productStore.setNextSeqId()
    productStore.create()

    def productStoreId = productStore.productStoreId

    def results = dispatcher.runSync("createUserLogin", [
            userLoginId          : telephone,
            enabled              : "Y",
            currentPassword      : currentPassword,
            currentPasswordVerify: currentPasswordVerify
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    userLoginNew = ExtEntityUtil.findOne(delegator, "UserLogin", [userLoginId: telephone])
    userLoginNew.productStoreId = productStoreId
    userLoginNew.store()

    results = dispatcher.runSync("addUserLoginToSecurityGroup", [
            userLoginId: telephone,
            groupId    : SecurityGroup.MERCHANT_ADMIN,
            userLogin  : userLogin
    ])
    if (!ServiceUtil.isSuccess(results)) return results

    results = ServiceUtil.returnSuccess()
    results.productStoreId = productStoreId

    return results
}

public Map updatePerson() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def personId = parameters.personId
    def nowTimestamp = UtilDateTime.nowTimestamp()
    def results = ServiceUtil.returnSuccess()

    def person = delegator.findOne("Person", [personId: personId], false)
    results.personId = person.personId
    results.oldStatusId = person.statusId

    person.setNonPKFields(parameters)
    person.lastModifiedDate = nowTimestamp
    person.lastModifiedByUserLogin = userLogin.userLoginId
    person.store()

    return results
}

public Map deletePerson() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def personId = parameters.personId
    def results = ServiceUtil.returnSuccess()

    delegator.removeByAnd("PersonStatus", [personId: personId])

    def person = delegator.findOne("Person", [personId: personId], false)
    person.remove()

    results.personId = personId
    return results
}

public Map createPersonStatus() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def personId = parameters.personId
    def nowTimestamp = UtilDateTime.nowTimestamp()

    //find the most recent status record and set the statusEndDate
    def oldPersonStatusList = delegator.findByAnd("PersonStatus", [personId: personId], ["-statusDate"], false)
    def oldPersonStatus = EntityUtil.getFirst(oldPersonStatusList)
    if (oldPersonStatus) {
        oldPersonStatus.statusEndDate = nowTimestamp
        oldPersonStatus.store()
    }

    def personStatus = delegator.makeValue("PersonStatus")
    personStatus.personId = personId
    personStatus.statusDate = nowTimestamp
    personStatus.setNonPKFields(parameters)
    personStatus.create()

    return ServiceUtil.returnSuccess()
}