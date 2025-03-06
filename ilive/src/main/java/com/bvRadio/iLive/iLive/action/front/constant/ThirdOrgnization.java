package com.bvRadio.iLive.iLive.action.front.constant;

public enum ThirdOrgnization {

	/**
	 * 微信类型
	 */
	WeiXin("weixin"),

	/**
	 * QQ类型
	 */
	QQ("qq");

	String type;

	ThirdOrgnization(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	
	
}
