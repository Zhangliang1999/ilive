package com.bvRadio.iLive.iLive.util;

import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

/**
 * 返回工具类
 * @author Wei
 *
 */
public class ResultUtils {
	
	public static JSONObject success(JSONObject data) {
		JSONObject result = new JSONObject();
		result.put("status", 0);
		result.put("message", "操作成功");
		result.put("data", data);
		return result;
	}
	
	public static JSONObject success() {
		JSONObject result = new JSONObject();
		result.put("status", 0);
		result.put("message", "操作成功");
		return result;
	}
	
	public static JSONObject error() {
		JSONObject result = new JSONObject();
		result.put("status", 1);
		result.put("message", "操作失败");
		return result;
	}
}
