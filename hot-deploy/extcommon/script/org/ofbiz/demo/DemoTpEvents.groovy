import org.ofbiz.base.util.UtilHttp
import org.ofbiz.entity.GenericValue
import org.ofbiz.service.LocalDispatcher

import javax.servlet.http.HttpServletRequest

public String doDemoTpUEditor() {
    HttpServletRequest request = request
    LocalDispatcher dispatcher = dispatcher
    GenericValue userLogin = userLogin
    def requestParameters = UtilHttp.getParameterMap(request)

    println "xxx: " + requestParameters

    return "success"
}
