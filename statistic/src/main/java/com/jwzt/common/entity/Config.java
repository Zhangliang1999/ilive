package com.jwzt.common.entity;

import com.jwzt.common.entity.base.BaseConfig;

@SuppressWarnings("serial")
public class Config extends BaseConfig {

	/**
	 * 最后同步企业信息ID
	 */
	public static final String LAST_SYNC_ENTERPRISE_ID = "last_sync_enterprise_id";
	/**
	 * 最后同步回看信息ID
	 */
	public static final String LAST_SYNC_VIDEO_ID = "last_sync_video_id";

	public Config() {
		super();
	}

	public Config(String id, String value) {
		super(id, value);
	}

}