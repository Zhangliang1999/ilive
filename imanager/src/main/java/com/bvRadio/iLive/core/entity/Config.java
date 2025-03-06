package com.bvRadio.iLive.core.entity;

import com.bvRadio.iLive.core.entity.base.BaseConfig;

public class Config extends BaseConfig {
	private static final long serialVersionUID = 1L;

	public static final String IS_CLOSE_COMMUNITY = "is_close_community";
	public static final String IS_NEED_CHECK = "is_need_check";
	public static final String IS_SENSITIVEWORD_ENABLED = "is_sensitiveword_enabled";
	public static final String IS_HOST_CHECK_ENABLED = "is_host_check_enabled";

	public Config() {
		super();
	}

	public Config(java.lang.String id) {
		super(id);
	}

}