package org.ofbiz.ext.biz;


import org.ofbiz.entity.GenericEntityException;

import javax.servlet.http.HttpServletRequest;

public class ExtUserOperateLogUtil extends  BaseUserOperateLogUtil{
    private static String module = ExtUserOperateLogUtil.class.getName();
    private static ExtUserOperateLogUtil instance = new ExtUserOperateLogUtil();
    
    @Override
    protected String getBizPartyId(HttpServletRequest request) {
        return ExtCommonWorker.getCurrentBizPartyId(request);
    }

    public static void logCreate(HttpServletRequest request, String comments) {
        instance.doLogCreate(request, comments);
    }

    public static void logDelete(HttpServletRequest request, String comments) {
        instance.doLogDelete(request, comments);
    }

    public static void logUpdate(HttpServletRequest request, String comments) {
        instance.doLogUpdate(request, comments);
    }

    public static void logLogin(HttpServletRequest request, String comments) throws GenericEntityException {
        instance.doLogLogin(request, comments);
    }

    public static void logLogout(HttpServletRequest request, String comments) {
        instance.doLogLogout(request, comments);
    }

    public static void logUpload(HttpServletRequest request, String comments) {
        instance.doLogUpload(request, comments);
    }

    public static void logDownload(HttpServletRequest request, String comments) {
        instance.doLogDownload(request, comments);
    }
}
