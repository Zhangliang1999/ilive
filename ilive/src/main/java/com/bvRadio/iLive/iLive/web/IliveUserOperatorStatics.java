package com.bvRadio.iLive.iLive.web;

import java.util.Map;

public enum IliveUserOperatorStatics {

	INSTANCE;

	/**
	 * 开启统计服务
	 */
	final String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");

	/**
	 * 用户关注,取消关注
	 */
	public void userConcernOp(Map<String, Object> concernMap) {

	}

	/**
	 * 用户签到操作
	 */
	public void userSignOp(Map<String, Object> signMap) {

	}

}
