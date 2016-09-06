package org.ofbiz.ext.biz;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.ext.util.ExtEntityUtil;

public class ExtPartyWorker {

    public static GenericValue getFirstUserLogin(Delegator delegator, String partyId) throws GenericEntityException {
        EntityCondition entityCondition = EntityCondition.makeCondition(UtilMisc.toList(
                EntityCondition.makeCondition("partyId", partyId),
                EntityCondition.makeCondition(
                        EntityCondition.makeCondition("enabled", "Y"),
                        EntityOperator.OR,
                        EntityCondition.makeCondition("enabled", null)
                )
        ));

        return ExtEntityUtil.getFirst(delegator, "UserLogin", entityCondition);//使用手机号码绑定第三方登录的帐号，同一个party会产生2个UserLogin数据，这里仅仅需要个UserLogin，这里任意获取其中一个
    }
}
