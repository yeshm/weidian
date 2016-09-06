package org.ofbiz.ext

import groovy.transform.Field
import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.lang.StringEscapeUtils
import org.ofbiz.base.util.UtilDateTime
import org.ofbiz.base.util.UtilHttp
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.ext.util.ExtHttpClient
import org.ofbiz.ext.util.ExtStringUtil
import org.ofbiz.ext.util.RenderUtil
import org.ofbiz.ext.util.XmlUtil
import org.ofbiz.thirdparty.weixin.SignUtil

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Field paramList

def initParamList() {
    paramList = []

    //天安旗舰店
    paramList.add([
            host          : "localhost:8080",
            productStoreId: "8000",
            token         : "pMYiKC",
            ToUserName    : "gh_ebb050060962",
            FromUserName  : "oRKlLt53jjy9y9Ytl1GE4ttWZKKM"
    ])

//        paramList.add([
//            host          : "localhost:8080",
//            productStoreId: "8000",
//            token         : "vQHyLM",
//            ToUserName    : "gh_ebb050060962",
//            FromUserName  : "oRKlLtydwiFek8JdD3vdpGCosxCo"
//    ])

    //广州恒宝店
//    paramList.add([
//            host          : "localhost:8080",
//            productStoreId: "10032",
//            token         : "AINCtb",
//            ToUserName    : "gh_ebb050060962",
//            FromUserName  : "oRKlLtydwiFek8JdD3vdpGCosxCo"
//    ])
}

//@Field def url = "http://localhost:8080/wxapi/weixin?psid=10004"
//@Field def token = "vQHyLM"
@Field def QRSCENE = "qrscene_"
@Field def USCENE = "SCENE_"

//@Field def ToUserName = "gh_880f592f9e0f"
//@Field def FromUserName = "o40IYuHMbpEglGv_cS19IWxflXn4"

//微信数据调用参考 /wxapi/weixin?id=10010&signature=9cfbfb83d848421d354d3f2c1c12080162359806&timestamp=1392718176&nonce=1392791325

//取消订阅消息模板
/*
<?xml version="1.0" encoding="UTF-8"?>
<xml><ToUserName><![CDATA[gh_2b217f304927]]></ToUserName>
<FromUserName><![CDATA[oFlVvuOAL8HroEhxBS9gh7SxZFw8]]></FromUserName>
<CreateTime>1393410809</CreateTime>
<MsgType><![CDATA[event]]></MsgType>
<Event><![CDATA[unsubscribe]]></Event>
<EventKey><![CDATA[]]></EventKey>
<Ticket><![CDATA[]]></Ticket>
</xml>
 */
@Field String unsubscribeMessageTemple = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<xml><ToUserName><![CDATA[\${ToUserName}]]></ToUserName>\n" +
        "<FromUserName><![CDATA[\${FromUserName}]]></FromUserName>\n" +
        "<CreateTime>\${CreateTime}</CreateTime>\n" +
        "<MsgType><![CDATA[event]]></MsgType>\n" +
        "<Event><![CDATA[unsubscribe]]></Event>\n" +
        "<EventKey><![CDATA[]]></EventKey>\n" +
        "<Ticket><![CDATA[]]></Ticket>\n" +
        "</xml>"

//订阅消息
/*
<?xml version="1.0" encoding="UTF-8"?>
<xml><ToUserName><![CDATA[gh_a26bb08f8d88]]></ToUserName>
<FromUserName><![CDATA[on_1Yt_50njnQuTdOaSWTDCXO024]]></FromUserName>
<CreateTime>1393411185</CreateTime>
<MsgType><![CDATA[event]]></MsgType>
<Event><![CDATA[subscribe]]></Event>
<EventKey><![CDATA[]]></EventKey>
<Ticket><![CDATA[]]></Ticket>
</xml>
 */
@Field String subscribeMessageTemple = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<xml><ToUserName><![CDATA[\${ToUserName}]]></ToUserName>\n" +
        "<FromUserName><![CDATA[\${FromUserName}]]></FromUserName>\n" +
        "<CreateTime>\${CreateTime}</CreateTime>\n" +
        "<MsgType><![CDATA[event]]></MsgType>\n" +
        "<Event><![CDATA[subscribe]]></Event>\n" +
        "<EventKey><![CDATA[\${EventKey}]]></EventKey>\n" +
        "<Ticket><![CDATA[]]></Ticket>\n" +
        "</xml>"

//菜单点击事件
/**
 <?xml version="1.0" encoding="UTF-8"?>
 <xml><ToUserName><![CDATA[gh_a26bb08f8d88]]></ToUserName>
 <FromUserName><![CDATA[on_1Yt_50njnQuTdOaSWTDCXO024]]></FromUserName>
 <CreateTime>1393481634</CreateTime>
 <MsgType><![CDATA[event]]></MsgType>
 <Event><![CDATA[CLICK]]></Event>
 <EventKey><![CDATA[menu_10002]]></EventKey>
 </xml>
 */
@Field String menuClickMessageTemple = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        " <xml><ToUserName><![CDATA[\${ToUserName}]]></ToUserName>\n" +
        " <FromUserName><![CDATA[\${FromUserName}]]></FromUserName>\n" +
        " <CreateTime>\${CreateTime}</CreateTime>\n" +
        " <MsgType><![CDATA[event]]></MsgType>\n" +
        " <Event><![CDATA[CLICK]]></Event>\n" +
        " <EventKey><![CDATA[\${EventKey}]]></EventKey>\n" +
        " </xml>"

/*
<?xml version="1.0" encoding="UTF-8"?>
<xml><ToUserName><![CDATA[gh_a26bb08f8d88]]></ToUserName>
<FromUserName><![CDATA[on_1Yt_50njnQuTdOaSWTDCXO024]]></FromUserName>
<CreateTime>1392718169</CreateTime>
<MsgType><![CDATA[text]]></MsgType>
<Content><![CDATA[吃早餐]]></Content>
<MsgId>5981678988600024717</MsgId>
</xml>
 */

//文本消息模板
@Field String textMessageTemple = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<xml><ToUserName><![CDATA[\${ToUserName}]]></ToUserName>\n" +
        "<FromUserName><![CDATA[\${FromUserName}]]></FromUserName>\n" +
        "<CreateTime>\${CreateTime}</CreateTime>\n" +
        "<MsgType><![CDATA[text]]></MsgType>\n" +
        "<Content><![CDATA[\${Content}]]></Content>\n" +
        "<MsgId>5981678988600024717</MsgId>\n" +
        "</xml>"

/*
<?xml version="1.0" encoding="UTF-8"?>
<xml><ToUserName><![CDATA[gh_a26bb08f8d88]]></ToUserName>
<FromUserName><![CDATA[on_1Yt_50njnQuTdOaSWTDCXO024]]></FromUserName>
<CreateTime>1392772878</CreateTime>
<MsgType><![CDATA[location]]></MsgType>
<Location_X>22.980537</Location_X>
<Location_Y>113.368530</Location_Y>
<Scale>16</Scale>
<Label><![CDATA[中国广东省广州市番禺区番禺大道北555号]]></Label>
<MsgId>5981913961965822004</MsgId>
</xml>

<?xml version="1.0" encoding="UTF-8"?>
<xml><ToUserName><![CDATA[gh_a26bb08f8d88]]></ToUserName>
<FromUserName><![CDATA[on_1Yt_50njnQuTdOaSWTDCXO024]]></FromUserName>
<CreateTime>1392773079</CreateTime>
<MsgType><![CDATA[location]]></MsgType>
<Location_X>22.979730</Location_X>
<Location_Y>113.367383</Location_Y>
<Scale>18</Scale>
<Label><![CDATA[中国广东省广州市番禺区番禺大道北555号]]></Label>
<MsgId>5981914825254248501</MsgId>
</xml>

冼庄立交
<?xml version="1.0" encoding="UTF-8"?>
<xml><ToUserName><![CDATA[gh_a26bb08f8d88]]></ToUserName>
<FromUserName><![CDATA[on_1Yt_50njnQuTdOaSWTDCXO024]]></FromUserName>
<CreateTime>1392780863</CreateTime>
<MsgType><![CDATA[location]]></MsgType>
<Location_X>22.985281</Location_X>
<Location_Y>113.354144</Location_Y>
<Scale>16</Scale>
<Label><![CDATA[中国广东省广州市番禺区金山大道西]]></Label>
<MsgId>5981948257279680672</MsgId>
</xml>
*/
//地理位置消息模板
@Field String locationMessageTemple = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<xml><ToUserName><![CDATA[\${ToUserName}]]></ToUserName>\n" +
        "<FromUserName><![CDATA[\${FromUserName}]]></FromUserName>\n" +
        "<CreateTime>\${CreateTime}</CreateTime>\n" +
        "<MsgType><![CDATA[location]]></MsgType>\n" +
        "<Location_X>\${Location_X}</Location_X>\n" +
        "<Location_Y>\${Location_Y}</Location_Y>\n" +
        "<Scale>20</Scale>\n" +
        "<Label><![CDATA[\${Label}]]></Label>\n" +
        "<MsgId>1234567890123456</MsgId>\n" +
        "</xml>"

//语音消息模板
@Field String voiceMessageTemple = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<xml><ToUserName><![CDATA[\${ToUserName}]]></ToUserName>\n" +
        "<FromUserName><![CDATA[\${FromUserName}]]></FromUserName>\n" +
        "<CreateTime>\${CreateTime}</CreateTime>\n" +
        "<MsgType><![CDATA[voice]]></MsgType>\n" +
        "<MediaID>1234567890123456</MediaID>\n" +
        "<Format>amr</Format>\n" +
        "<Recognition><![CDATA[\${Recognition}]]></Recognition>\n" +
        "<MsgId>1234567890123456</MsgId>\n" +
        "</xml>"

//扫描带参数二维码消息模板(已经关注公众号)
@Field String scaneMessageTemple = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<xml><ToUserName><![CDATA[\${ToUserName}]]></ToUserName>\n" +
        "<FromUserName><![CDATA[\${FromUserName}]]></FromUserName>\n" +
        "<CreateTime>\${CreateTime}</CreateTime>\n" +
        "<MsgType><![CDATA[event]]></MsgType>\n" +
        "<Event><![CDATA[SCAN]]></Event>\n" +
        "<EventKey><![CDATA[\${EventKey}]]></EventKey>\n" +
        "<Ticket><![CDATA[]]></Ticket>\n" +
        "</xml>"

//扫描带参数二维码消息模板(已经关注公众号)
//<xml><ToUserName><![CDATA[gh_723bb742b787]]></ToUserName>
//<FromUserName><![CDATA[ox6UMuIhiS7eEL0Elhpf06mEsRcE]]></FromUserName>
//<CreateTime>1418097627</CreateTime>
//<MsgType><![CDATA[event]]></MsgType>
//<Event><![CDATA[scancode_push]]></Event>
//<EventKey><![CDATA[rselfmenu_0_1]]></EventKey>
//<ScanCodeInfo><ScanType><![CDATA[qrcode]]></ScanType>
//<ScanResult><![CDATA[login|527359650381136772]]></ScanResult>
//</ScanCodeInfo>
//</xml>

//扫码推事件的事件推送
@Field String scancodePushMessageTemple = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<xml><ToUserName><![CDATA[\${ToUserName}]]></ToUserName>\n" +
        "<FromUserName><![CDATA[\${FromUserName}]]></FromUserName>\n" +
        "<CreateTime>\${CreateTime}</CreateTime>\n" +
        "<MsgType><![CDATA[event]]></MsgType>\n" +
        "<Event><![CDATA[scancode_push]]></Event>\n" +
        "<EventKey><![CDATA[\${EventKey}]]></EventKey>\n" +
        "<ScanCodeInfo>" +
        "<ScanType><![CDATA[qrcode]]></ScanType>" +
        "<ScanResult><![CDATA[\${ScanResult}]]></ScanResult>" +
        "</ScanCodeInfo>\n" +
        "</xml>"
//微信支付成功回调消息

@Field String weipaySucceedMessage = "<xml>\n" +
        "<appid><![CDATA[wx33bb40388bcc0a16]]></appid>\n" +
        "<bank_type><![CDATA[CFT]]></bank_type>\n" +
        "<cash_fee><![CDATA[1]]></cash_fee>\n" +
        "<device_info><![CDATA[WEB]]></device_info>\n" +
        "<fee_type><![CDATA[CNY]]></fee_type>\n" +
        "<is_subscribe><![CDATA[Y]]></is_subscribe>\n" +
        "<mch_id><![CDATA[1245667902]]></mch_id>\n" +
        "<nonce_str><![CDATA[xaurxlu82qiwio4je1tksybusa593pj1]]></nonce_str>\n" +
        "<openid><![CDATA[oRKlLt53jjy9y9Ytl1GE4ttWZKKM]]></openid>\n" +
        "<out_trade_no><![CDATA[\${orderId}]]></out_trade_no>\n" +
        "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
        "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
        "<sign><![CDATA[4543D66C80020C99BBC4873E75DC8792]]></sign>\n" +
        "<time_end><![CDATA[20150902165613]]></time_end>\n" +
        "<total_fee>1</total_fee>\n" +
        "<trade_type><![CDATA[JSAPI]]></trade_type>\n" +
        "<transaction_id><![CDATA[1004020589201509020771597181]]></transaction_id>\n" +
        "</xml>"

//scancode_waitmsg：扫码推事件且弹出“消息接收中”提示框的事件推送
//<xml><ToUserName><![CDATA[gh_e136c6e50636]]></ToUserName>
//<FromUserName><![CDATA[oMgHVjngRipVsoxg6TuX3vz6glDg]]></FromUserName>
//<CreateTime>1408090606</CreateTime>
//<MsgType><![CDATA[event]]></MsgType>
//<Event><![CDATA[scancode_waitmsg]]></Event>
//<EventKey><![CDATA[6]]></EventKey>
//<ScanCodeInfo><ScanType><![CDATA[qrcode]]></ScanType>
//<ScanResult><![CDATA[2]]></ScanResult>
//</ScanCodeInfo>
//</xml>
@Field String scancodeWaitMsgMessageTemple = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<xml><ToUserName><![CDATA[\${ToUserName}]]></ToUserName>\n" +
        "<FromUserName><![CDATA[\${FromUserName}]]></FromUserName>\n" +
        "<CreateTime>\${CreateTime}</CreateTime>\n" +
        "<MsgType><![CDATA[event]]></MsgType>\n" +
        "<Event><![CDATA[scancode_waitmsg]]></Event>\n" +
        "<EventKey><![CDATA[\${EventKey}]]></EventKey>\n" +
        "<ScanCodeInfo>" +
        "<ScanType><![CDATA[qrcode]]></ScanType>" +
        "<ScanResult><![CDATA[\${ScanResult}]]></ScanResult>" +
        "</ScanCodeInfo>\n" +
        "</xml>"


@Field Map<String, String> templeMap = [
        text    : textMessageTemple,
        location: locationMessageTemple,
        voice   : voiceMessageTemple,
        event   : [
                unsubscribe     : unsubscribeMessageTemple,
                subscribe       : subscribeMessageTemple,
                CLICK           : menuClickMessageTemple,
                SCAN            : scaneMessageTemple,
                scancode_push   : scancodePushMessageTemple,
                scancode_waitmsg: scancodeWaitMsgMessageTemple
        ]
]

public String simulate() {
    HttpServletRequest request = request
    HttpServletResponse response = response
    def requestParameters = UtilHttp.getParameterMap(request)

    def MsgType = requestParameters.MsgType

    def timestamp = UtilDateTime.nowTimestamp().getTime().toString()
    def nonce = RandomStringUtils.randomNumeric(10)

    initParamList()
//微信回调信息
    if ("weipayCallback".equals(MsgType)) {
        def startDateTime = System.currentTimeMillis()
        def url = "http://localhost:8080/wxapi/orderWeipayNotify"

        def body = ExtStringUtil.renderTemplate(weipaySucceedMessage, requestParameters)
        def result = ExtHttpClient.post(url, body).responseString

        def endDateTime = System.currentTimeMillis()

        println "xxxxxxxxxxxx: " + result;

        RenderUtil.renderText(response, StringEscapeUtils.unescapeHtml(XmlUtil.formatXML(result)))
        RenderUtil.renderHtml(response, "\n\n\n")
        RenderUtil.renderText(response, XmlUtil.formatXML(result))

        RenderUtil.renderText(response, "runSync in ==>[" + (endDateTime - startDateTime) + "]milliseconds")
        RenderUtil.renderHtml(response, "\n")
        RenderUtil.renderText(response, "====================================================================")


    } else {
        for (Map params : paramList) {
            def startDateTime = System.currentTimeMillis()

            def signature = SignUtil.signature(params.token, timestamp, nonce)

            def url = "http://${params.host}/wxapi/weixin?psid=${params.productStoreId}&signature=" + signature + "&timestamp=" + timestamp + "&nonce=" + nonce

            def messageTemplate
            if ("event".equals(MsgType)) {
                def Event = requestParameters.Event
                def EventKey = requestParameters.EventKey
                messageTemplate = templeMap[MsgType][Event]
                requestParameters.putAll([EventKey: EventKey])
            } else {
                messageTemplate = templeMap.get(MsgType)
            }

            requestParameters.putAll([
                    ToUserName  : params.ToUserName,
                    FromUserName: params.FromUserName,
                    CreateTime  : timestamp
            ])

            if (UtilValidate.isNotEmpty(requestParameters.slbeneId)) {
                requestParameters.putAll([EventKey: QRSCENE + requestParameters.sceneId])
            } else if (UtilValidate.isNotEmpty(requestParameters.SCENE_Id)) {
                requestParameters.putAll([EventKey: requestParameters.SCENE_Id])
            }

            def body = ExtStringUtil.renderTemplate(messageTemplate, requestParameters)

            def result = ExtHttpClient.post(url, body).responseString

            def endDateTime = System.currentTimeMillis()

            println "xxxxxxxxxxxx: " + result;

            RenderUtil.renderText(response, StringEscapeUtils.unescapeHtml(XmlUtil.formatXML(result)))
            RenderUtil.renderHtml(response, "\n\n\n")
            RenderUtil.renderText(response, XmlUtil.formatXML(result))

            RenderUtil.renderText(response, "runSync in ==>[" + (endDateTime - startDateTime) + "]milliseconds")
            RenderUtil.renderHtml(response, "\n")
            RenderUtil.renderText(response, "====================================================================")
        }
    }
    return "success"
}
