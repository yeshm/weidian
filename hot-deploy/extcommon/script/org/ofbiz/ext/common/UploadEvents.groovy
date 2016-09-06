package org.ofbiz.ext.common

import javolution.util.FastList
import javolution.util.FastMap
import net.sf.json.JSONObject
import org.apache.commons.fileupload.FileItemFactory
import org.apache.commons.fileupload.FileItemIterator
import org.apache.commons.fileupload.FileItemStream
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.io.IOUtils
import org.ofbiz.base.util.Debug
import org.ofbiz.base.util.UtilProperties
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.ext.bean.OmUploadFile
import org.ofbiz.ext.util.AppUtil
import org.ofbiz.ext.util.MessageUtil
import org.ofbiz.ext.util.RenderUtil
import org.ofbiz.ext.util.UploadUtil

import javax.servlet.http.HttpServletRequest

/**
 * 单个文件上传
 * @return
 */
public String omUpload() {
    HttpServletRequest request=request

    FileItemFactory factory = new DiskFileItemFactory();
    ServletFileUpload upload = new ServletFileUpload(factory);
    upload.setHeaderEncoding("UTF-8");
    List<OmUploadFile> uploadFileList = FastList.newInstance();
    Map<String, String> thumbMap = FastMap.newInstance()

    def tempFileName,savePath

    if (ServletFileUpload.isMultipartContent(request)) {
        FileItemIterator iter = upload.getItemIterator(request);

        while (iter.hasNext()) {
            FileItemStream item = iter.next();

            if (!item.isFormField()) {
                tempFileName = UploadUtil.getTempFileName(item.getName());
                String tempPath = UploadUtil.getTempPath(context)

                UploadUtil.copy(item.openStream(), tempPath+tempFileName)
            }else{
                //post post_params: {"thumbMap" : "{'isThumb':'Y','width':'320'}"},
//                println "name:"+item.getFieldName()+",value:"+value

                def value = convertStreamToString(item.openStream())

                if("key".equals(item.getFieldName())) savePath=value;
            }

            if ("thumbMap".equals(item.getFieldName())) {
               try {
                   def value = convertStreamToString(item.openStream())
                   thumbMap = AppUtil.fromJson(value, Map.class);
               } catch (Throwable e) {
                   Debug.logError("omUpload: 解析压缩参数异常： " + e.getMessage(), "")
               }
            }
        }

        //将临时文件夹的文件copy到正式文件夹
        UploadUtil.saveUploadWithSavePath(tempFileName,savePath,context);

        OmUploadFile omUploadFile = new OmUploadFile();
        omUploadFile.setFileName(savePath);
        omUploadFile.setFileUrl(UtilProperties.getPropertyValue("extcommon", "upload.view.path")+savePath);
        uploadFileList.add(omUploadFile);

    }
    if (UtilValidate.isNotEmpty(uploadFileList) && UtilValidate.isNotEmpty(thumbMap) && "Y" ==thumbMap.isThumb) {
        OmUploadFile omUploadFile = uploadFileList.get(0);
        try {
            int width = Integer.valueOf(thumbMap.width)

            String tempPath = UploadUtil.getTempPath(context)

            String omUploadFileName = omUploadFile.getFileName()
            String thumbFileName = omUploadFileName.substring(omUploadFileName.lastIndexOf("/") + 1)
            String thumbFilePath = omUploadFileName.substring(0, omUploadFileName.lastIndexOf("/")+1)

            String tempThumbFileName =  thumbFilePath + "thumb_" + thumbFileName;
            String tempThumbFileUrl = UploadUtil.getViewFileUrl(tempThumbFileName);

            println " fileName: " + tempThumbFileName + " tempThumbFileUrl: " + tempThumbFileUrl
            UploadUtil.thumbImageByWidth(tempPath+omUploadFile.getFileName(), tempPath + tempThumbFileName, width)

            omUploadFile.setThumbFileName(tempThumbFileName);
            omUploadFile.setThumbFileUrl(tempThumbFileUrl);
        } catch (Throwable e) {
            Debug.logError("omUpload: 根据压缩参数压缩异常： " + e.getMessage(), "")

            omUploadFile.setThumbFileName(omUploadFile.getFileName());
            omUploadFile.setThumbFileUrl(omUploadFile.getFileUrl());
        }

    }

    if (UtilValidate.isNotEmpty(uploadFileList)) {
        MessageUtil.saveSuccessMessage(request, uploadFileList);
    } else {
        MessageUtil.saveErrorMessage(request, "请选择上传文件！")
    }
    return "success";
}


/**
 * 多个文件上传
 * @return
 */
public String multiUploadFile() {
    ServletFileUpload upload = new ServletFileUpload();
    upload.setHeaderEncoding("UTF-8");
    List<OmUploadFile> uploadFileList = FastList.newInstance();

    if (ServletFileUpload.isMultipartContent(request)) {
        FileItemIterator iter = upload.getItemIterator(request);

        while (iter.hasNext()) {
            FileItemStream item = iter.next();
            if (!item.isFormField()) {
                String tempFileName = UploadUtil.getTempFileName(item.getName());
                String tempPath = UploadUtil.getTempPath(context)
                String tempfileUrl = UploadUtil.getViewFileUrl(tempFileName);
                println "xxxxxxxxxxxxxxxxxxxxxxx: " + tempFileName
                UploadUtil.copy(item.openStream(), tempPath+tempFileName)

                OmUploadFile omUploadFile = new OmUploadFile();
                omUploadFile.setFileName(tempFileName);
                omUploadFile.setFileUrl(tempfileUrl);
                uploadFileList.add(omUploadFile);
            }else{
                //post post_params: {"thumbMap" : "{'isThumb':'Y','width':'320'}"},
                //def value = convertStreamToString(item.openStream())
                //println "name:"+item.getFieldName()+",value:"+value
            }

        }
    }

    if (UtilValidate.isNotEmpty(uploadFileList)) {
        MessageUtil.saveSuccessMessage(request, uploadFileList);
    } else {
        MessageUtil.saveErrorMessage(request, "请选择上传文件！")
    }
    return "success";
}

private String convertStreamToString(InputStream is) {
    StringWriter writer = new StringWriter();
    IOUtils.copy(is, writer, "utf-8");
    return writer.toString();
}

public String buiUpload() {
    ServletFileUpload upload = new ServletFileUpload();
    upload.setHeaderEncoding("UTF-8");

    if (ServletFileUpload.isMultipartContent(request)) {
        JSONObject json = new JSONObject();
        FileItemIterator iter = upload.getItemIterator(request);
        int i = 0;
        while (iter.hasNext()) {
            FileItemStream item = iter.next();
            if (!item.isFormField()) {
                String tempFileName = UploadUtil.getTempFileName(item.getName());
                String tempPath = UploadUtil.getTempPath(context)
                String tempfileUrl = UploadUtil.getViewFileUrl(tempFileName);

                UploadUtil.copy(item.openStream(), tempPath+tempFileName)

                i++;
                if(i>1){
                    Debug.logError("目前只支持单文件上传","")
                    return "error"
                }
                json.put("url",tempfileUrl);
                json.put("fileName",tempFileName);

                RenderUtil.renderJson(response, json)
                return "success"
            }
        }
    }

    return "success";
}