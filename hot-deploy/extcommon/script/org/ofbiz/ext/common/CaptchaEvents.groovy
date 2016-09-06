import org.ofbiz.base.util.UtilHttp
import org.ofbiz.ext.biz.SessionKeys
import org.ofbiz.ext.util.ImageUtils

import javax.imageio.ImageIO
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

public String generateCaptcha() {
    HttpServletResponse response = response
    HttpSession session = session

    Map<String, Object> results = ImageUtils.generateCaptcha(70, 30, 4)

    SessionKeys.setSessionCaptcha(session, results.get(ImageUtils.CAPTCHA_RANDOM_CODE_NUMBER));

    // 禁止图像缓存。
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);

    response.setContentType("image/jpeg");

    // 将图像输出到Servlet输出流中。
    ServletOutputStream sos = response.getOutputStream();
    ImageIO.write(results.get(ImageUtils.CAPTCHA_BUFFERED_IMAGE_OBJECT), "jpeg", sos);
    sos.flush();
    sos.close();

    return "success";
}

public String checkCaptcha() {
    HttpServletRequest request = request
    HttpSession session = request.getSession()
    def requestParameters = UtilHttp.getParameterMap(request)

    def captcha = (String) session.getAttribute("CAPTCHA_CODE_SESSION_KEY");
    def inputCaptcha = requestParameters.captcha

    if (inputCaptcha.equals(captcha)) {
        request.setAttribute("isCorrect", "Y")
    } else {
        request.setAttribute("isCorrect", "N")
    }

    return "success"
}


