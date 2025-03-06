package com.bvRadio.iLive.iLive.entity;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveViewAuthBill;

/**
 * 观看鉴权表
 * @author administrator
 *
 */
public class ILiveViewAuthBill extends BaseILiveViewAuthBill {
	/**
	 * 鉴权类型：公开观看
	 */
	public final static Integer AUTH_TYPE_FREE = 1;
	/**
	 * 鉴权类型：密码观看
	 */
	public final static Integer AUTH_TYPE_PASSWORD = 2;
	/**
	 * 鉴权类型：付费观看
	 */
	public final static Integer AUTH_TYPE_PAY = 3;
	/**
	 * 鉴权类型：白名单观看
	 */
	public final static Integer AUTH_TYPE_WHITE_LIST = 4;
	/**
	 * 鉴权类型：登录观看
	 */
	public final static Integer AUTH_TYPE_LOGIN = 5;
	/**
	 * 鉴权类型：观看码观看
	 */
	public final static Integer AUTH_TYPE_FCODE = 6;
	/**
	 * 鉴权类型：登录公开观看
	 */
	public final static Integer AUTH_TYPE_FREELogin = 15;
	/**
	 * 鉴权类型：登录密码观看
	 */
	public final static Integer AUTH_TYPE_PASSWORDLogin = 25;
}
