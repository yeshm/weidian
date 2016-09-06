package org.weidian.root

import org.ofbiz.entity.Delegator

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public String checkDomainBind() {
    HttpServletRequest request = request
    HttpServletResponse response = response
    Delegator delegator = delegator

    response.sendRedirect("/merchant")
    return "none"
}