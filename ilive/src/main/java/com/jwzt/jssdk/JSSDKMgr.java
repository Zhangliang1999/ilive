package com.jwzt.jssdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.jwzt.comm.StringUtils;

import net.sf.json.JSONObject;

public class JSSDKMgr {

	private static String jsapiTicket = null;
	private static long ticketDate = -1;

	private static final String openIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID1&secret=SECRET1&code=CODE1&grant_type=authorization_code";
	private static final String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID1&secret=SECRET1";
	private static final String ticketUrl = "http://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=ACCESS_TOKEN1";
	private static final String wechatUserUrl = "https://api.weixin.qq.com/sns/userinfo?";
	public static String appId = "";
	public static String appSecret = "";
	public static final int baseLife = 3;
	public static final String EndTime = "2099-11-31 00:00:01";

	public JSSDKMgr(String appId, String appSecret) {
		this.appId = appId;
		this.appSecret = appSecret;
	}

	public static String[] getOpenId(HttpServletRequest request) throws Exception {
		String openId = null;
		String accessToken = null;
		String code = request.getParameter("code");
		if (!StringUtils.isEmpty(code)) {
			String url = openIdUrl.replace("APPID1", appId).replace("SECRET1", appSecret).replace("CODE1", code);
			String result = getURLContent(url);
			// System.out.println("result==============>"+result);
			// System.out.println(result);
			openId = result.split("openid")[1].split("scope")[0].replaceAll("\"", "").replaceAll(",", "")
					.replaceAll(" ", "").replaceAll(":", "");
			accessToken = result.split("access_token")[1].split("expires_in")[0].replaceAll("\"", "")
					.replaceAll(",", "").replaceAll(" ", "").replaceAll(":", "");
		}
		String[] ret = { openId, accessToken };
		return ret;
	}

	public WechatUserInfo getWechatUserInfo(HttpServletRequest request) throws Exception {
		String[] openIdAccessToken = getOpenId(request);
		return getWechatUserInfo(openIdAccessToken[0], openIdAccessToken[1]);

	}

	public static WechatUserInfo getWechatUserInfo(String openId, String accessToken) throws Exception {
		WechatUserInfo wechatUserInfo = null;
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
		String url = wechatUserUrl;
		String params = "access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
		url = url + params;
		String result = getURLContent(url);
//		System.out.println("userInfo UNINONID##############______________>" + result);
		JSONObject jsonObject = JSONObject.fromObject(result);
		wechatUserInfo = new WechatUserInfo();
		wechatUserInfo.setOpenId(jsonObject.getString("openid"));
		wechatUserInfo.setUnionId(jsonObject.getString("unionid"));
		wechatUserInfo.setNickName(jsonObject.getString("nickname"));
		wechatUserInfo.setSex(String.valueOf(jsonObject.getInt("sex")));
		wechatUserInfo.setCountry(jsonObject.getString("country"));
		wechatUserInfo.setProvince(jsonObject.getString("province"));
		wechatUserInfo.setCity(jsonObject.getString("city"));
		wechatUserInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
		return wechatUserInfo;
	}

	public static JSSDKInfo setShareConfig(String url) throws Exception {

		JSSDKInfo info = new JSSDKInfo();
		// ServletContext context = request.getSession().getServletContext();
		// String accessToken = (String) context.getAttribute("accessToken");
		Date nowDate = new Date();
		if (jsapiTicket == null || nowDate.getTime() - ticketDate > 7200000) {
			String accessToken = getAccessToken();
			jsapiTicket = getTicket(accessToken);
			ticketDate = nowDate.getTime();
		}
		Map<String, String> ret = sign(jsapiTicket, url);
		info.setAppId(appId);
		info.setSignature(ret.get("signature"));
		info.setNonceStr(ret.get("nonceStr"));
		info.setTimestamp(ret.get("timestamp"));
		return info;
	};

	private static String getTicket(String accessToken) throws Exception {
		String ticket = null;
		String url = ticketUrl.replace("ACCESS_TOKEN1", accessToken);
		String result = getURLContent(url);
		// System.out.println(result);
		ticket = result.split("ticket")[1].split("expires_in")[0].replaceAll("\"", "").replaceAll(",", "")
				.replaceAll(" ", "").replaceAll(":", "");
		// System.out.println("ticket:" + ticket);
		return ticket;
	}

	private static String getAccessToken() throws Exception {
		String accessToken = null;
		String url = accessTokenUrl.replace("APPID1", appId).replace("SECRET1", appSecret);
		String result = getURLContent(url);
		// System.out.println(result);
		accessToken = result.split("access_token")[1].split("expires_in")[0].replaceAll("\"", "").replaceAll(",", "")
				.replaceAll(" ", "").replaceAll(":", "");
		// System.out.println("accessToken:" + accessToken);
		return accessToken;
	}

	private static String getURLContent(String url) throws MalformedURLException, IOException {
		String result = "";
		BufferedReader in = null;
		URL realUrl = new URL(url);
		URLConnection connection = realUrl.openConnection();
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		connection.connect();
		Map<String, List<String>> map = connection.getHeaderFields();
		in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}
		if (in != null) {
			in.close();
		}
		return result;
	}

	private static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16).toLowerCase();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String string1;
		String signature = "";
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
			// System.out.println("key--" + string1);
			// System.out.println("url--" + url);
			// System.out.println("jsapi_ticket--" + jsapi_ticket);
			// System.out.println("nonceStr--" + nonce_str);
			// System.out.println("timestamp--" + timestamp);
			// System.out.println("signature--" + signature);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * 
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}
}
