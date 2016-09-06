package org.ofbiz.ext.util;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.commons.io.FileUtils;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.ext.bean.LoginToken;
import org.ofbiz.ext.util.jackson.CustomObjectSerializer;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtil {

    private static final String module = AppUtil.class.getName();

    private static final JsonMapper mapper;

    private static final XmlMapper xmlMapper = XmlMapper.nonEmptyMapper();

    private static final XmlMapper emptyXmlMapper = XmlMapper.emptyMapper();

    static {
        mapper = JsonMapper.nonEmptyMapper();

        SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1, 0, 0, null, null, null));
        simpleModule.addSerializer(Map.class, new CustomObjectSerializer());
        mapper.getMapper().registerModule(simpleModule);
    }

    /**
     * 生成数字型随机字符串
     *
     * @param length 长度
     * @return 数字型随机字符串
     */
    public static String randomNumberString(int length) {
        StringBuilder sb = new StringBuilder();

        Random rand = new Random();
        int num;

        for (int i = 0; i < length; i++) {
            num = rand.nextInt(10);
            sb.append(num);
        }

        return sb.toString();
    }

    /**
     * 随机数字,字母或其组合
     *
     * @param length
     * @return
     */
    public static String randomCharacterAndNumber(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {// 字符串
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    public static String toJson(final Object o) {
        String json = mapper.toJson(o);
        return json;
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return mapper.fromJson(jsonString, clazz);
    }

    public static <T> T fromJson(String jsonString, JavaType javaType) {
        return mapper.fromJson(jsonString, javaType);
    }

    public static String toXml(final Object o) {
        String xml = xmlMapper.toXml(o);
        return xml;
    }

    public static String toXml(final Object o, boolean nonEmpty) {
        return nonEmpty ? xmlMapper.toXml(o) : emptyXmlMapper.toXml(o);
    }

    public static <T> T fromXml(String xmlString, Class<T> clazz) {
        return xmlMapper.fromXml(xmlString, clazz);
    }

    public static <T> T fromXml(String xmlString, JavaType javaType) {
        return xmlMapper.fromXml(xmlString, javaType);
    }

    public static String getAppPath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("");
    }

    public static String getAppContextPath(HttpServletRequest request) {
        return request.getSession().getServletContext().getContextPath();
    }

    public static JsonMapper getMapper() {
        return mapper;
    }

    public static XmlMapper getXmlMapper() {
        return xmlMapper;
    }

    public static JsonNode readTree(String content) {
        try {
            return getMapper().getMapper().readTree(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateQrCodeUrl(String text, String logoUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append(UtilProperties.getPropertyValue("thirdparty", "qrcode.api.url"));
        sb.append("?text=").append(text);
        sb.append("&logo=").append(logoUrl);
        return sb.toString();
    }

    private static final String[] MOBILE_SPECIFIC_SUBSTRING = {
            "iPad", "iPhone", "Android", "MIDP", "Opera Mobi",
            "Opera Mini", "BlackBerry", "HP iPAQ", "IEMobile",
            "MSIEMobile", "Windows Phone", "HTC", "LG",
            "MOT", "Nokia", "Symbian", "Fennec",
            "Maemo", "Tear", "Midori", "armv",
            "Windows CE", "WindowsCE", "Smartphone", "240x320",
            "176x220", "320x320", "160x160", "webOS",
            "Palm", "Sagem", "Samsung", "SGH",
            "SonyEricsson", "MMP", "UCWEB"};

    public static boolean isMobileBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        for (String mobile : MOBILE_SPECIFIC_SUBSTRING) {
            if (userAgent.contains(mobile)
                    || userAgent.contains(mobile.toUpperCase())
                    || userAgent.contains(mobile.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public static String subString(String str, int len) {
        return subString(str, len, "...");
    }


    /**
     * 根据指定长度，从0开始截取字符串str，截取的长度小于总长用symbol补齐
     * 数字英文一个字符，中文两个字符，所截取的长度不足，则多取一个
     * 列如，我xxx程序员，截取6，和截取7 都是”我xxx程“
     *
     * @param str
     * @param len
     * @param symbol
     * @return
     */
    public static String subString(String str, int len, String symbol) {
        if (str.length() < len / 2) return str;
        int count = 0;
        StringBuffer sb = new StringBuffer();
        String[] array = str.split("");
        for (int i = 1; i < array.length; i++) {
            count += array[i].getBytes().length > 1 ? 2 : 1;
            sb.append(array[i]);
            if (count >= len) break;
        }
        return (sb.toString().length() < str.length()) ? sb.append(symbol == null ? "" : symbol).toString() : str;
    }

    /**
     * 获取不带0的日期
     *
     * @return
     */
    public static String getCurrentDateString() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        return year + "-" + month + "-" + date;
    }

    /**
     * 获取当前星期
     *
     * @return
     */
    public static String getCurrentWeekOfDate() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weekDays[w];
    }

    /**
     * 根据正则式获取匹配的内容
     *
     * @param conent
     * @param regex
     * @return
     */
    public static List<String> extractMessages(String conent, String regex) {
        List<String> messageList = FastList.newInstance();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(conent);
        while (matcher.find()) {
            messageList.add(matcher.group(1));
        }
        return messageList;
    }

    /**
     * 根据正则式获取匹配的内容
     *
     * @param content
     * @param regex
     * @param num     正则式个数
     * @return
     */
    public static Map<Integer, List<String>> extractMessages(String content, String regex, int num) {
        Map<Integer, List<String>> results = FastMap.newInstance();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        int index = 0;
        List<String> messageList = null;
        while (matcher.find()) {
            messageList = FastList.newInstance();
            for (int i = 0; i < num; i++) {
                messageList.add(matcher.group(i + 1));
            }
            results.put(index, messageList);
            index++;
        }

        return results;
    }

    public static void main(String[] args) {
        String s = AppUtil.randomCharacterAndNumber(4);
        System.out.println(s);

        Set<String> array = new HashSet<String>();
        for (int i = 0; i < 1; i++) {
            String tmp = AppUtil.randomCharacterAndNumber(4);
            array.add(tmp);
            System.out.println(tmp);
        }
        System.out.println(array.size() + "|" + 100);

        LoginToken token = new LoginToken();

        token.setCheckString("fasdf");


        System.out.println(AppUtil.toJson(token));

        System.out.println(AppUtil.toXml(token));
    }

    public static String readFile(String path){
        return readFile(path != null ? new File(path) : null);
    }

    public static String readFile(File file){
        try{
            return FileUtils.readFileToString(file);
        }catch(IOException e){
            Debug.logError(e, module);
            e.printStackTrace();
        }
        return null;
    }
}
