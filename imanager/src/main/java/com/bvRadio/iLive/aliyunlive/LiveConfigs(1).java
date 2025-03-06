package com.bvRadio.iLive.aliyunlive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;



public class LiveConfigs {
	private static final Logger log = LoggerFactory.getLogger(LiveConfigs.class);
	private static Map<String, String> configMap = new HashMap<String, String>();
	public static final String APPNAME = "app_name";
	public static final String MAINKEY = "main_key";
	public static final String DOMAINNAME = "domain_name";
	public static final String PLAYNAME = "play_name";
	public static final String HOUR = "hours";
	public static final String FOUNFLIVEURL = "foundLiveurl";
	public static final String ROOMLIST = "roomList";
	public static final String GETROOMPLAYURL = "getRoom";
	public static String get(String key) {
		String returnStr = configMap.get(key);
		// if (null == returnStr) {
		loadConfig();
		returnStr = configMap.get(key);
		// }
		return returnStr;
	}
	private static void loadConfig() {
		Properties props = new Properties();
		try {
			props = PropertiesLoaderUtils.loadAllProperties("config.properties");
			for (Object key : props.keySet()) {
				configMap.put((String) key, (String) props.get(key));
			}
		} catch (IOException e) {
			log.error("加载配置文件出错。", e);
		}
	}
	public static void main(String[] args) {
		System.out.println(LiveConfigs.HOUR);
	}
}
