package com.bvRadio.iLive.iLive.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ConfigUtils {
	private static final Logger log = LoggerFactory.getLogger(ConfigUtils.class);
	private static Map<String, String> configMap = new HashMap<String, String>();

	public final static String ILive_HOME_URL = "iLive_home_url";
	public final static String BBS_ROOT_HTTP_URL = "bbs_root_http_url";
	public final static String BBS_ROOT_HTTPS_URL = "bbs_root_https_url";
	public final static String BBS_LOGIN_HTTP_URL = "bbs_login_http_url";
	public final static String BBS_LOGIN_HTTPS_URL = "bbs_login_https_url";
	public final static String BBS_TOKEN_LOGIN_HTTP_URL = "bbs_token_login_http_url";
	public final static String BBS_TOKEN_LOGIN_HTTPS_URL = "bbs_token_login_https_url";
	public final static String CMS_GET_CHANNEL_URL = "cms_get_channel_url";
	public final static String CMS_GET_ALL_CHANNEL_URL = "cms_get_all_channel_url";
	public final static String CMS_GET_ALL_STATION_URL = "cms_get_all_station_url";
	public final static String CMS_GET_VOD_FILES_URL = "cms_get_vod_files_url";
	public final static String IP_ADDRESS_URL = "ip_address_url";
	public final static String SMG_IP = "smg_ip";
	public final static String SOCKET_PORT = "socket_port";
	public static final String BBS_TLAP_ROOT_HTTP_URL = "bbs_tlap_root_http_url";
	public static final String BBS_TLAP_ROOT_HTTPS_URL = "bbs_tlap_root_https_url";
	public static final String personal_pwd_url = "personal_pwd_url";
	public static final String BBS_MEDIA_ROOT_HTTP_URL = "BBS_MEDIA_ROOT_HTTP_URL";
	public static final String BBS_MEDIA_ROOT_HTTPS_URL = "BBS_MEDIA_ROOT_HTTPS_URL";
	public final static String TRANSCODE_COMMAND_VIDEO = "transcode_command_video";
	public static String get(String key) {
		String returnStr = configMap.get(key);
		if (null == returnStr) {
			loadConfig();
			returnStr = configMap.get(key);
		}
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
