package com.bvRadio.iLive.iLive.constants;

/**
 * 企业认证状态
 * 
 * @author administrator
 */
public final class ILiveCertStatus {

	/**
	 * 注册完毕后信息状态
	 */
	public static final Integer REG_FINISH = 0;

	/**
	 * 简单信息完善 第二版设计已废弃 直接跳过改值
	 */
	public static final Integer SIMPLE_CERT = 1;

	/**
	 * 点击试用完毕后状态
	 */
	public static final Integer TRY_USE = 2;

	/**
	 * 提交认证信息完毕状态   认证中
	 */
	public static final Integer CERT_ING = 3;

	/**
	 * 认证通过状态
	 */
	public static final Integer CERT_PASS = 4;

	/**
	 * 认证不通过状态
	 */
	public static final Integer CERT_NO_PASS = 5;
}
