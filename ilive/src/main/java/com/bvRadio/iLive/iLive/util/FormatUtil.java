package com.bvRadio.iLive.iLive.util;

import java.net.URLEncoder;

import com.alibaba.fastjson.JSONObject;



public class FormatUtil {
	/**
	 * 处理JSON空值
	 * 
	 * @param jsonObj
	 *            JSON对象
	 */
	public static void dealWithJsonEmptyValue(JSONObject jsonObj) {
		if (jsonObj == null)
			return;

		try {
			for (String key : jsonObj.keySet())
				if (StringUtil.isEmpty(jsonObj.getString(key)))
					jsonObj.put(key, "");
		} catch (Exception e) {
		}
	}

	/**
	 * JSON->URL参数
	 * 
	 * @param json
	 *            JSON
	 * @param isUrlEncoding
	 *            是否进行URL编码
	 * @param charset
	 *            字符编码集（可空，默认：UTF-8）
	 * @return URL参数
	 */
	public static String json2UrlParam(String json, boolean isUrlEncoding,
			String charset) {
		if (StringUtil.isEmpty(json))
			return "";

		try {
			StringBuffer urlParamStrBuffer = new StringBuffer();
			JSONObject jsonObj = JSONObject.parseObject(json);
			if (jsonObj == null)
				return "";
			dealWithJsonEmptyValue(jsonObj);
			for (String key : jsonObj.keySet()) {
				String value = jsonObj.getString(key);
				if (isUrlEncoding)
					value = StringUtil.isEmpty(value) ? ""
							: URLEncoder.encode(value, StringUtil
									.isEmpty(charset) ? LoginboxConfig.CHARSET
									: charset);
				urlParamStrBuffer.append("&");
				urlParamStrBuffer.append(key);
				urlParamStrBuffer.append("=");
				urlParamStrBuffer.append(value);
			}
			return urlParamStrBuffer.substring(1, urlParamStrBuffer.length());
		} catch (Exception e) {
			return "";
		}
	}
}