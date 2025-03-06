package com.bvRadio.iLive.iLive.entity;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveFileAuthentication;

/**
 * 文件鉴权
 * @author administrator
 *
 */
public class ILiveFileAuthentication extends BaseILiveFileAuthentication {
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
	 * 鉴权类型：手机观看
	 */
	public final static Integer AUTH_TYPE_MOBILE = 5;
	/**
	 * 鉴权类型：F码观看
	 */
	public final static Integer AUTH_TYPE_FCODE = 6;
}
