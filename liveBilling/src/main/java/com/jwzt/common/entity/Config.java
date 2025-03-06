package com.jwzt.common.entity;

import com.jwzt.common.entity.base.BaseConfig;

@SuppressWarnings("serial")
public class Config extends BaseConfig {

	/**
	 * 平台费率
	 */
	public static final String PLATFORM_RATE = "platform_rate";
	/**
	 * 最后同步企业信息ID
	 */
	public static final String LAST_SYNC_ENTERPRISE_ID = "last_sync_enterprise_id";
	/**
	 * ftp服务器配置
	 */
	public static final String FTP_SERVER_IP = "ftp_server_ip";
	public static final String FTP_SERVER_PORT = "ftp_server_port";
	public static final String FTP_SERVER_USERNAME = "ftp_server_username";
	public static final String FTP_SERVER_PASSWORD = "ftp_server_password";
	public static final String FTP_SERVER_ENCODING = "ftp_server_encoding";
	public static final String FTP_SERVER_PATH = "ftp_server_path";
	public static final String HTTP_SERVER_HOME_URL = "http_server_home_url";
	/**
	 * oss服务器配置
	 */
	public static final String BUCKET_NAME = "bucket_name";
	public static final String BUCKET_SOURCE = "bucket_source";
	public static final String ACCESS_KEY_ID = "accessKeyId";
	public static final String SECRET_ACCESS_KEY = "secretAccessKey";
	public static final String OSS_SERVER_HOME_URL = "oss_server_home_url";
		/**
	 * 认证用户默认套餐id
	 */
	public static final String CERT_USER_PACKAGE_ID = "cert_user_package_id";
	/**
	 * 试用用户默认套餐id
	 */
	public static final String BETA_USER_PACKAGE_ID = "beta_user_package_id";
	
	public Config() {
		super();
	}

	public Config(String id, String value) {
		super(id, value);
	}

}