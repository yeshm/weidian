package org.ofbiz

import org.ofbiz.ext.web.AutoLoginWorker

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public String clearAutoLogin() {
    HttpServletRequest request = request
    HttpServletResponse response = response

    AutoLoginWorker.clearAutoLogin(request, response, "Official", "OfficialMd5")

    return "success"
}