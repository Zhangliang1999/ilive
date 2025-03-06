package com.bvRadio.iLive.iLive.util;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.bvRadio.iLive.iLive.web.ConfigUtils;

public class ExpressionUtil {
	private static final Logger log = LoggerFactory.getLogger(ConfigUtils.class);
	private static CopyOnWriteArrayList<ConcurrentHashMap<String, String>> expressionConfigMapList = new CopyOnWriteArrayList<ConcurrentHashMap<String, String>>();

	public static CopyOnWriteArrayList<ConcurrentHashMap<String, String>> getAll() {
		if (null == expressionConfigMapList || expressionConfigMapList.size() <= 0) {
			loadConfig();
		}
		return expressionConfigMapList;
	}

	public static String replaceKeyToImg(String str) {
		if (null == expressionConfigMapList || expressionConfigMapList.size() <= 0) {
			loadConfig();
		}
		for (Map<String, String> expressionConfigMap : expressionConfigMapList) {
			if(expressionConfigMap!=null){
				String key = expressionConfigMap.get("key");
				String value = expressionConfigMap.get("value");
				if (!StringUtils.isBlank(value)) {
					try {
						JSONObject json = new JSONObject(value);
						String imgStr = "<img class=\"expressionImage\" src=\""
								+ json.getString("path") + "\" style=\"position: relative;\">";
						str = str.replace("[" + key + "]", imgStr);
					} catch (JSONException e) {
						log.error("ExpressionUtil>>replaceToImg出错，str = {} ", str, e);
					}
				}
			}
		}
		return str;
	}

	public static String replaceTitleToKey(String str) {
		if (null == expressionConfigMapList || expressionConfigMapList.size() <= 0) {
			log.info("开始加载配置文件的表情:");
			loadConfig();
		}
		for (Map<String, String> expressionConfigMap : expressionConfigMapList) {
			//log.info("expressionConfigMapexpressionConfigMap:" + expressionConfigMap);
			if(expressionConfigMap!=null){
				String key = expressionConfigMap.get("key");
				String value = expressionConfigMap.get("value");
				if (!StringUtils.isBlank(value)) {
					try {
						JSONObject json = new JSONObject(value);
						String title = json.getString("title");
						str = str.replace("[" + title + "]", "[" + key + "]");
					} catch (JSONException e) {
						e.printStackTrace();
						log.error("ExpressionUtil>>replaceToImg出错，str = {} ", str, e);
					}
				}
			}
		}
		return str;
	}

	public static String replaceKeyToTitle(String str) {
		if (null == expressionConfigMapList || expressionConfigMapList.size() <= 0) {
			loadConfig();
		}
		for (Map<String, String> expressionConfigMap : expressionConfigMapList) {
			if(expressionConfigMap!=null){
				String key = expressionConfigMap.get("key");
				String value = expressionConfigMap.get("value");
				if (!StringUtils.isBlank(value)) {
					try {
						JSONObject json = new JSONObject(value);
						String title = json.getString("title");
						str = str.replace("[" + key + "]", "[" + title + "]");
					} catch (JSONException e) {
						log.error("ExpressionUtil>>replaceToImg出错，str = {} ", str, e);
					}
				}
			}
			
		}
		return str;
	}

	private static void loadConfig() {
		Properties props = new Properties();
		try {
			props = PropertiesLoaderUtils.loadAllProperties("wechat_emotion.properties");
			for (Object key : props.keySet()) {
				ConcurrentHashMap<String, String> expressionConfigMap = new ConcurrentHashMap<String, String>();
				expressionConfigMap.put("key", (String) key);
				expressionConfigMap.put("value", (String) props.get(key));
				expressionConfigMapList.add(expressionConfigMap);
			}
		} catch (IOException e) {
			log.error("加载表情配置文件出错。", e);
		}
	}

}
