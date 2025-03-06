package com.bvRadio.iLive.iLive.util;

import net.sf.json.JSONObject;

public class ResponseJson {

	public static String defaultOK() {
		JSONObject result = new JSONObject();
		result.put("code", "0");
		result.put("msg","操作成功");
		return result.toString();
	}
	
	public static String ok(JSONObject data) {
		JSONObject result = new JSONObject();
		result.put("code", "0");
		result.put("msg","操作成功");
		result.put("data", data);
		return result.toString();
	}
	
	public static String defaultFail() {
		JSONObject result = new JSONObject();
		result.put("code", "1");
		result.put("msg", "操作出现异常，请重试");
		return result.toString();
	}
	
	public static String fail(String msg) {
		JSONObject result = new JSONObject();
		result.put("code", "1");
		result.put("msg", msg);
		return result.toString();
	}
	
}
