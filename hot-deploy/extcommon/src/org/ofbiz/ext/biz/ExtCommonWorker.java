package org.ofbiz.ext.biz;

import javolution.util.FastMap;
import org.ofbiz.base.util.GeneralRuntimeException;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public class ExtCommonWorker {

    private static String module = ExtCommonWorker.class.getName();

    public static String getCurrentBizPartyId(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String currentBizPartyId = (String) session.getAttribute(SessionKeys.CURRENT_BIZ_PARTY_ID);
        if (UtilValidate.isNotEmpty(currentBizPartyId)) {
            return currentBizPartyId;
        }

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        if (UtilValidate.isNotEmpty(userLogin)) {
            if (UtilValidate.isNotEmpty(userLogin.getString("partyId"))) {
                return userLogin.getString("partyId");
            } else {
                return "Company";
            }
        }

        throw new GeneralRuntimeException("找不到行使权的partyId！");
    }

    /**
     * 获取Enumeration表中的value值，key：enumId value：description
     *
     * @param delegator
     * @param enumTypeId
     * @return
     * @throws org.ofbiz.entity.GenericEntityException
     */
    public static Map<String, String> getEnumValueMapByType(Delegator delegator, String enumTypeId) throws GenericEntityException {
        Map<String, String> result = FastMap.newInstance();
        List<GenericValue> valueList = delegator.findByAndCache("Enumeration", UtilMisc.toMap("enumTypeId", enumTypeId), UtilMisc.toList("sequenceId"));
        if (UtilValidate.isNotEmpty(valueList)) {
            for (GenericValue value : valueList) {
                result.put(value.getString("enumId"), value.getString("description"));
            }
        }
        return result;
    }

    /**
     * 获取Enumeration表中的code值，key：enumId value：enumCode
     *
     * @param delegator
     * @param enumTypeId
     * @return
     * @throws GenericEntityException
     */
    public static Map<String, String> getEnumCodeMapByType(Delegator delegator, String enumTypeId) throws GenericEntityException {
        Map<String, String> result = FastMap.newInstance();
        List<GenericValue> valueList = delegator.findByAndCache("Enumeration", UtilMisc.toMap("enumTypeId", enumTypeId), UtilMisc.toList("sequenceId"));
        if (UtilValidate.isNotEmpty(valueList)) {
            for (GenericValue value : valueList) {
                result.put(value.getString("enumId"), value.getString("enumCode"));
            }
        }
        return result;
    }
}