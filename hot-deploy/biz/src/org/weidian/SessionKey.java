package org.weidian;

import javax.servlet.http.HttpSession;

public class SessionKey {

	/**
	 * 校验码session key
	 */
	public static final String CAPTCHA_CODE_SESSION_KEY = "CAPTCHA_CODE_SESSION_KEY";

    /**当前操作的业务partyId，在模拟登陆的情况下，会跟userLogin的partyId不一致，因此要单独记录**/
    public static final String CURRENT_BIZ_PARTY_ID = "CURRENT_BIZ_PARTY_ID";

	public static final String CUSTOMER_PARTY_ID = "CUSTOMER_PARTY_ID"; //用户Party Id
	public static final String MERCHANT_PUBLIC_ACCOUNT = "MERCHANT_PUBLIC_ACCOUNT"; //商家公众号信息
	public static final String MERCHANT_PUBLIC_ALIAS = "MERCHANT_PUBLIC_ALIAS"; //商家微信号
	public static final String MERCHANT_PRODUCT_STORE_ID = "MERCHANT_PRODUCT_STORE_ID"; //商家店铺ID
	public static final String DEALER_PRODUCT_STORE_ID = "DEALER_PRODUCT_STORE_ID"; //经销商店铺ID
	public static final String DEALER_PARTY_ID = "DEALER_PARTY_ID"; //经销商partyId
	public static final String DEALER_WEIXIN_OPEN_ID = "DEALER_WEIXIN_OPEN_ID"; //经销商weiixnopenId
	public static final String DEALER_SHOPKEEPER_PARTY_ID = "DEALER_SHOPKEEPER_PARTY_ID"; //店长partyId
	public static final String MOCKD_DEALER = "MOCKD_DEALER"; //是否是admin模拟登陆
	public static final String MERCHANT_PRODUCT_STORE = "MERCHANT_PRODUCT_STORE"; //商家店铺信息
	public static final String SESSION_DOMAIN_BIND = "SESSION_DOMAIN_BIND";
	public static final String MERCHANT_PARTY_ID = "MERCHANT_PARTY_ID";
	public static final String VIP_MEMBER_PARTY_ID = "VIP_MEMBER_PARTY_ID";
	public static final String PUBLIC_ACCOUNT_ID = "PUBLIC_ACCOUNT_ID";

	public static final String FACILITY_MAN_PARTY_ID = "FACILITY_MAN_PARTY_ID";
	public static final String FACILITY_MAN_PRODUCT_STORE_ID = "FACILITY_MAN_PRODUCT_STORE_ID";
	public static final String FACILITY_ID = "FACILITY_ID";
	public static final String FACILITY_NAME = "FACILITY_NAME";

	public static final String DISTRIBUTE_KEY = "_DISTRIBUTE_KEY_";
	public static final String DISTRIBUTE_URL = "_DISTRIBUTE_URL_";
	
	public static void setSessionCaptcha(HttpSession session, String captcha) {
		session.setAttribute(CAPTCHA_CODE_SESSION_KEY, captcha);
	}
	
	public static String getSessionCaptcha(HttpSession session) {
		return (String)session.getAttribute(CAPTCHA_CODE_SESSION_KEY);
	}
	
}
