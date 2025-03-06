package com.bvRadio.iLive.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.util.HMACSHA1;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.PostMan;
import com.bvRadio.iLive.iLive.util.Base64;
import com.jwzt.common.Md5Util;

public class ILiveSmgServerCenter {

	/**
	 * 发送消息
	 */
	public static Map<String, Object> sendMsg(String phone, Map<String, Object> sendParam) throws Exception {
		String postUrl = "";
		String smg_ip = ConfigUtils.get("smg_ip");
		smg_ip = smg_ip + "/message/send";
		String svcid = ConfigUtils.get("svcid");
		String svcpass = ConfigUtils.get("svcpass");
		postUrl = postUrl + smg_ip + "?svcid=" + svcid + "&svcpass=" + svcpass + "&phone=" + phone;
		if (sendParam.get("msg") == null) {
			Integer biztype = (Integer) sendParam.get("biztype");
			if (biztype != null) {
				postUrl += "&biztype=" + biztype;
			}
			String params = (String) sendParam.get("params");
			if (params != null) {
				postUrl += "&params=" + params;
			}

		} else {
			Integer biztype = (Integer) sendParam.get("biztype");
			if (biztype != null) {
				postUrl += "&biztype=" + biztype;
			}
			postUrl += "&msg=" + sendParam.get("msg");
		}
		String downloadUrl = PostMan.downloadUrl(postUrl);
		// System.out.println("POST DOWNLoadURL:" + downloadUrl);
		if (downloadUrl != null && !downloadUrl.trim().isEmpty()) {
			Map<String, Object> map = new HashMap<>();
			JSONObject jsonObj = new JSONObject(downloadUrl.trim());
			Integer code = (Integer) jsonObj.get("code");
			if (code.equals(0)) {
				map.put("code", code);
				JSONObject jsonSon = (JSONObject) jsonObj.get("info");
				Integer msgId = (Integer) jsonSon.get("id");
				map.put("msgId", msgId);
			} else {
				map.put("code", code);
				map.put("message", jsonObj.get("message"));
			}
			return map;
		}
		return null;
	}
	/**
	 * 发送消息(新版) 2019-2-20 zhangliang
	 */
	public static Map<String, Object> sendMsgNew(List<String> phonelist, Map<String, Object> sendParam) throws Exception {
		String postUrl =  ConfigUtils.get("msg_ip");
		JSONObject resultJson = new JSONObject();
		resultJson.put("mobile", phonelist);
		resultJson.put("time", System.currentTimeMillis());
		resultJson.put("params", sendParam.get("params"));
		Map<String, Object> sendParam1 = new HashMap<>();
		sendParam1.put("time", System.currentTimeMillis());
		sendParam1.put("msgid", (Integer) sendParam.get("biztype"));
		String sign=creatSign(sendParam1);
		resultJson.put("sign", sign);
		if (sendParam.get("msg") != null) {
			Integer biztype = (Integer) sendParam.get("biztype");
			if (biztype != null) {
				resultJson.put("msgid", biztype);
			}
			List params = (List) sendParam.get("params");
			if (params != null) {
				resultJson.put("Param", params);
			}

		} else {
			Integer biztype = (Integer) sendParam.get("biztype");
			if (biztype != null) {
				resultJson.put("msgid", biztype);
			}
			resultJson.put("msg", sendParam.get("msg")); 
		}
		System.out.println("resultJson==========:"+resultJson.toString());
		System.out.println("postUrl===============:"+postUrl);
		String downloadUrl = PostMan.postJson(postUrl,"post","UTF-8",resultJson.toString());
		 System.out.println("POST DOWNLoadURL:" + downloadUrl);
		if (downloadUrl != null && !downloadUrl.trim().isEmpty()) {
			Map<String, Object> map = new HashMap<>();
			JSONObject jsonObj = new JSONObject(downloadUrl.trim());
			Integer code = (Integer) jsonObj.get("code");
			if (code.equals(0)) {
				map.put("code", code);
				JSONObject jsonSon = (JSONObject) jsonObj.get("data");
				Integer msgId = (Integer) jsonSon.get("id");
				map.put("msgId", msgId);
			} else {
				map.put("code", code);
				map.put("msg", jsonObj.get("msg"));
			}
			return map;
		}
		return null;
	}
	
	public static String creatSign(Map<String, Object> sendParam){
		String key=Md5Util.encode("TYSXMSG");
		String QueryString = "";
		Ksort(sendParam);
		 for (Entry<String, Object> entry : sendParam.entrySet()) {
		      QueryString=QueryString+"&"+entry.getKey() + "=" + entry.getValue();
		      System.out.println(QueryString);
		    }
		String stringToSign = QueryString.substring(1);
		System.out.println("stringToSign:"+stringToSign);
		String singature=null;
		//HMAC-SHA1 加密
		String hmacSign;
		try {
			hmacSign = HMACSHA1.getSignature(stringToSign, key+"&");
			System.out.println("hmacSign:"+hmacSign);
			singature =Base64.encode(hmacSign,"UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(singature);
		return singature;
		
	}
	
	public static void Ksort(Map<String, Object> map){
		String sb = "";
		String[] key = new String[map.size()];
		int index = 0;
		for (String k : map.keySet()) {
		key[index] = k;
		index++;
		}
		Arrays.sort(key);
		for (String s : key) {
		sb += s + "=" + map.get(s) + "&";
		}
		sb = sb.substring(0, sb.length() - 1);
		// 将得到的字符串进行处理得到目标格式的字符串
		try {
		sb = URLEncoder.encode(sb, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}// 使用常见的UTF-8编码
		sb = sb.replace("%3D", "=").replace("%26", "&");
		System.out.println(sb);
	}
}
