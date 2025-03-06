package com.jwzt.statistic.entity;

import java.sql.Timestamp;

import com.jwzt.statistic.entity.base.BaseUserInfo;

@SuppressWarnings("serial")
public class UserInfo extends BaseUserInfo {
	/**
	 * 注册终端类型：安卓
	 */
	public static final Integer TERMINAL_TYPE_ANDROID = 1;
	/**
	 * 注册终端类型：IOS
	 */
	public static final Integer TERMINAL_TYPE_IOS = 2;
	/**
	 * 注册终端类型：PC
	 */
	public static final Integer TERMINAL_TYPE_PC = 3;
	/**
	 * 注册终端类型：H5
	 */
	public static final Integer TERMINAL_TYPE_H5 = 4;
	/**
	 * 注册终端类型：微信
	 */
	public static final Integer TERMINAL_TYPE_WEIXIN = 5;

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public UserInfo() {
		super();
	}

}
