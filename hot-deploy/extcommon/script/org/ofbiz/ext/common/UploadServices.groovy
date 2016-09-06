package org.ofbiz.ext.common

import org.ofbiz.base.util.Debug
import org.ofbiz.base.util.UtilProperties
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.base.util.string.FlexibleStringExpander
import org.ofbiz.service.ServiceUtil

import java.nio.ByteBuffer

public Map uploadFile() {
    ByteBuffer fileData = (ByteBuffer) context.get("uploadFile")
    String fileName = (String) context.get("_uploadFile_fileName")
//    String contentType = (String) context.get("_uploadFile_contentType")

    if (UtilValidate.isNotEmpty(fileName)) {
        String imageServerPath = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("extcommon", "upload.server.path"), context)

        File file = new File(imageServerPath + "/" + fileName)
        Debug.logInfo("upload file to " + file.getAbsolutePath(), "")

        try {
            RandomAccessFile out = new RandomAccessFile(file, "rw")
            out.write(fileData.array())
            out.close()
        } catch (FileNotFoundException e) {
            Debug.logError(e, "")
            return ServiceUtil.returnError("file can't write, fileName:" + file.getAbsolutePath())
        } catch (IOException e) {
            Debug.logError(e, "")
            return ServiceUtil.returnError("file can't write, fileName:" + file.getAbsolutePath())
        }

    }

    return ServiceUtil.returnSuccess()
}

public Map upload() {
    ByteBuffer fileData = (ByteBuffer) context.get("uploadFile")
    String fileName = (String) context.get("_uploadFile_fileName")
//    String contentType = (String) context.get("_uploadFile_contentType")

    if (UtilValidate.isNotEmpty(fileName)) {
        String imageServerPath = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("extcommon", "upload.server.path"), context)

        File file = new File(imageServerPath + "/" + fileName)
        Debug.logInfo("upload file to " + file.getAbsolutePath(), "")

        try {
            RandomAccessFile out = new RandomAccessFile(file, "rw")
            out.write(fileData.array())
            out.close()
        } catch (FileNotFoundException e) {
            Debug.logError(e, "")
            return ServiceUtil.returnError("file can't write, fileName:" + file.getAbsolutePath())
        } catch (IOException e) {
            Debug.logError(e, "")
            return ServiceUtil.returnError("file can't write, fileName:" + file.getAbsolutePath())
        }

    }

    return ServiceUtil.returnSuccess()
}