package com.bvRadio.iLive.sms;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.PostMan;

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
		System.out.println("POST DOWNLoadURL:" + downloadUrl);
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

}
