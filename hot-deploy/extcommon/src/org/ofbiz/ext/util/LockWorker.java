package org.ofbiz.ext.util;

import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;

import java.util.List;
import java.util.Map;

/**
 * 数据库锁的工具类
 */
public class LockWorker {

    public static final String module = LockWorker.class.getName();

    /**
     * 锁一条数据
     */
    public static GenericValue lockEntity(Delegator delegator, String entityName, Map<String, ? extends Object> fields) throws GenericEntityException {
        Debug.logInfo("lockEntity entityName:["+entityName+"],fields:["+ fields.toString() +"]", module);
//        return delegator.findByPrimaryKeyForUpdate(entityName, fields);
        throw new RuntimeException("no implement");
    }

    /**
     * 锁多条数据
     */
    private static List<GenericValue> lockEntityList(Delegator delegator, String entityName, Map<String, ? extends Object> fields) throws GenericEntityException {
        Debug.logInfo("lockEntityList entityName:["+entityName+"],fields:["+ fields.toString() +"]", module);
//        return delegator.findByAndForUpdate(entityName, fields, true);
        throw new RuntimeException("no implement");
    }
}
