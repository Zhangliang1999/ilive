package com.jwzt.statistic.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ConfigUtils {
	private static final Logger log = LogManager.getLogger();
	
	public static final String GET_USER_INFO_URL = "get_user_info_url";
	
	private static Map<String, String> configMap = new HashMap<String, String>();

	public static List<Map<String, Long>> getMinuteStatisticSeries() {
		try {
			String seriesString = get("minute_statistic_series");
			String[] split = seriesString.split(",");
			List<Map<String, Long>> list = new ArrayList<Map<String, Long>>();
			for (String string : split) {
				String[] split2 = string.split("-");
				Map<String, Long> map = new HashMap<String, Long>();
				map.put("start", Long.parseLong(split2[0]));
				if (split2.length > 1 && null != split2[1]) {
					map.put("end", Long.parseLong(split2[1]));
				}
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			log.warn("getMinuteStatisticSeries error.", e);
			return null;
		}
	}

	public static String get(String key) {
		loadConfig();
		String returnStr = configMap.get(key);
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
}
