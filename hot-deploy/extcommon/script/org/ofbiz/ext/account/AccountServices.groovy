package org.ofbiz.ext.account

import com.juweitu.bonn.constant.accounting.FinAccountStatus
import com.juweitu.bonn.constant.accounting.FinAccountTransType
import com.juweitu.bonn.constant.accounting.FinAccountType
import com.juweitu.bonn.order.ExtOrderReadHelper
import org.apache.commons.lang.StringUtils
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilProperties
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.ExtEntityUtil
import org.ofbiz.ext.util.LockWorker
import org.ofbiz.ext.util.SequenceHelper
import org.ofbiz.order.finaccount.FinAccountHelper
import org.ofbiz.service.LocalDispatcher
import org.ofbiz.service.ServiceUtil

public Map createBusinessTrans() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    BigDecimal businessTransAmount = parameters.businessTransAmount

    if (UtilValidate.isEmpty(businessTransAmount)) {
        return ServiceUtil.returnError("金额错误")
    }

    def businessTrans = delegator.makeValue("ExtBusinessTrans", [
            businessTransDate      : UtilDateTime.nowTimestamp(),
            createdDate            : UtilDateTime.nowTimestamp(),
            createdByUserLogin     : userLogin.userLoginId,
            lastModifiedDate       : UtilDateTime.nowTimestamp(),
            lastModifiedByUserLogin: userLogin.userLoginId
    ])
    businessTrans.setNonPKFields(parameters)
    businessTrans.setNextSeqId()
    businessTrans.create()

    def results = ServiceUtil.returnSuccess()
    results.businessTransId = businessTrans.businessTransId

    return results
}

public Map updateBusinessTrans() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def businessTransId = parameters.businessTransId

    def businessTrans = ExtEntityUtil.findOne(delegator, "ExtBusinessTrans", [businessTransId: businessTransId])
    businessTrans.setNonPKFields(parameters)
    businessTrans.lastModifiedDate = UtilDateTime.nowTimestamp()
    businessTrans.lastModifiedByUserLogin = userLogin.userLoginId
    businessTrans.store()

    def results = ServiceUtil.returnSuccess()
    results.businessTransId = businessTrans.businessTransId

    return results
}

public Map createBusinessFinAccountTrans() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("bizCreateFinAccountTrans", "IN", parameters)
    serviceCtx.userLogin = userLogin

    def results = dispatcher.runSync("bizCreateFinAccountTrans", serviceCtx)
    return results
}

/**
 * 创建账户交易记录
 */
public Map createFinAccountTrans() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def ownerPartyId = parameters.ownerPartyId
    def finAccountId = parameters.finAccountId
    def finAccountTypeId = parameters.finAccountTypeId
    def finAccountTransTypeId = parameters.finAccountTransTypeId
    BigDecimal amount = parameters.amount
    def isAllowNegative = parameters.isAllowNegative
    def comments = parameters.comments

    if (UtilValidate.isWhitespace(finAccountTypeId)) return ServiceUtil.returnError("账户类型ID不能为空")
    if (UtilValidate.isWhitespace(ownerPartyId)) return ServiceUtil.returnError("账户类型ownerPartyId不能为空")

    amount = amount.setScale(FinAccountHelper.decimals, FinAccountHelper.rounding)
    if (UtilValidate.isEmpty(amount)) {
        return ServiceUtil.returnError("充值交易金额错误")
    }
    if (UtilValidate.isEmpty(amount) || amount.compareTo(BigDecimal.ZERO) == 0) {
        return ServiceUtil.returnError("不允许创建金额为0的交易")
    }

    /*首先根据传入的finAccountId判断，如果根据finAccountId可以找到账户，则直接进入交易逻辑处理，如果finAccountId没有传入，则根据账户类型和账户拥有者id进行查找，如果找不到账户，则创建新账户*/
    def finAccount
    if (!UtilValidate.isWhitespace(finAccountId)) {
        finAccount = delegator.findOne("FinAccount", [finAccountId: finAccountId], true)

        if (UtilValidate.isEmpty(finAccount)) return ServiceUtil.returnError("找不账户")
        if (!finAccountTypeId.equals(finAccount.finAccountTypeId)) return ServiceUtil.returnError("传入的账户类型和账户ID对应的账户类型不一致")
    } else {
        finAccount = ExtEntityUtil.getOnly(delegator, "FinAccount", [
                finAccountTypeId: finAccountTypeId,
                ownerPartyId    : ownerPartyId
        ])

        if (UtilValidate.isEmpty(finAccount)) {
            //创建账户前加锁，避免账户重复
            LockWorker.lockEntity(delegator, "Party", [partyId: ownerPartyId])
            finAccount = ExtEntityUtil.getOnly(delegator, "FinAccount", [
                    finAccountTypeId: finAccountTypeId,
                    ownerPartyId    : ownerPartyId
            ])
            if (UtilValidate.isEmpty(finAccount)) {
                //创建新账户
                def serviceCtx = dispatcher.getDispatchContext().makeValidContext("createFinAccount", "IN", parameters)
                serviceCtx.userLogin = userLogin
                if (UtilValidate.isEmpty(serviceCtx.statusId)) serviceCtx.statusId = FinAccountStatus.ACTIVE
                if (UtilValidate.isEmpty(serviceCtx.currencyUomId)) serviceCtx.currencyUomId = UtilProperties.getPropertyValue("general", "currency.uom.id.default")

                def results = dispatcher.runSync("createFinAccount", serviceCtx)

                if (!ServiceUtil.isSuccess(results)) return results

                finAccountId = results.finAccountId
            }
        } else {
            finAccountId = finAccount.finAccountId
        }

        if (UtilValidate.isEmpty(finAccount)) {
            finAccount = ExtEntityUtil.findOne(delegator, "FinAccount", [
                    finAccountId: finAccountId
            ])
        }
    }

    //获取数据库锁来计算当前余额
    //数据库锁的较昂贵，还会导致死锁问题，暂且不加锁，可能会jia
    //def finAccount = ExtFinAccountHelper.lockFinAccount(delegator, finAccountId)
    def snapshotBalance
    def actualBalance = finAccount.get("actualBalance") ?: FinAccountHelper.ZERO
    if (finAccountTransTypeId.equals(FinAccountTransType.DEPOSIT) || finAccountTransTypeId.equals(FinAccountTransType.ADJUSTMENT)) {
        snapshotBalance = actualBalance + amount
    } else if (finAccountTransTypeId.equals(FinAccountTransType.WITHDRAWAL)) {
        snapshotBalance = actualBalance - amount
        if (!isAllowNegative && snapshotBalance.compareTo(FinAccountHelper.ZERO) < 0) {
            return ServiceUtil.returnError("余额不足！")
        }
    } else {
        return ServiceUtil.returnError("交易类型错误")
    }
    snapshotBalance = snapshotBalance.setScale(FinAccountHelper.decimals, FinAccountHelper.rounding)

    def dateStr = UtilDateTime.nowDateString("yyyyMMdd")

    // 创建财务交易明细
    def serviceCtx = dispatcher.getDispatchContext().makeValidContext("createFinAccountTrans", "IN", parameters)
    serviceCtx.finAccountTransNo = "" //TODO yeshm 替换序列号实现 dateStr + StringUtils.leftPad(SequenceHelper.getNextSeq(delegator, "FinAccountTrans" + dateStr).toString(), 5, "0")
    serviceCtx.finAccountId = finAccountId
    serviceCtx.snapshotBalance = snapshotBalance
    serviceCtx.comments = comments
    serviceCtx.userLogin = userLogin
    if (UtilValidate.isEmpty(serviceCtx.partyId)) serviceCtx.partyId = ownerPartyId

    def results = dispatcher.runSync("createFinAccountTrans", serviceCtx)

    if (!ServiceUtil.isSuccess(results)) return results

    def finAccountTransId = results.finAccountTransId

    results = ServiceUtil.returnSuccess()
    results.finAccountTransId = finAccountTransId

    return results
}

public Map setComments4FinAccountTrans() {
    LocalDispatcher dispatcher = dispatcher
    Delegator delegator = delegator
    GenericValue userLogin = userLogin
    Map parameters = parameters

    def finAccountTransId = parameters.finAccountTransId

    def finAccountTrans = delegator.findOne("FinAccountTrans", [finAccountTransId: finAccountTransId], false)
    def finAccount = finAccountTrans.getRelatedOne("FinAccount", true)

    if (FinAccountType.PM_PREPAID.equals(finAccount.finAccountTypeId) && UtilValidate.isEmpty(finAccountTrans.comments)) {
        if (FinAccountTransType.DEPOSIT.equals(finAccountTrans.finAccountTransTypeId)) {
            finAccountTrans.set("comments", "充值")
            finAccountTrans.store()
        } else if (FinAccountTransType.WITHDRAWAL.equals(finAccountTrans.finAccountTransTypeId)) {
            String orderId = finAccountTrans.get("orderId")
            if (UtilValidate.isNotEmpty(orderId)) {
                ExtOrderReadHelper orh = new ExtOrderReadHelper(delegator, orderId)
                finAccountTrans.set("comments", orh.getOrderDesc())
                finAccountTrans.store()
            }
        }
    }

    def results = ServiceUtil.returnSuccess()
    results.finAccountTransId = finAccountTransId
    return results
}