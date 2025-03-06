package com.bvRadio.iLive.iLive.entity;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveManager;

public class ILiveManager extends BaseILiveManager implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 管理账户
	 */
	public static final Integer USER_LEVEL_ADMIN=0;
	/**
	 * 协同管理用户
	 */
	public static final Integer USER_LEVEL_Collaborative=1;
	/**
	 * 助手邀请码用户
	 */
	public static final Integer USER_LEVEL_Assistant=2;
	/**
	 * 子账户
	 */
	public static final Integer USER_LEVEL_SUB=3;
	/**
	 * 子账户启动状态  1 启动
	 */
	public static final Integer SUB_TYPE_ON=1;
	/**
	 *  子账户启动状态  0 关闭
	 */
	public static final Integer SUB_TYPE_OFF=0;

	/**
	 * 登录token值
	 */
	private String loginToken;

	/**
	 * 简单企业名称
	 * 
	 * @return
	 */
	private String simpleEnterpriseName;

	/**
	 * 用户状态 0是普通用户观看端的 1是开通直播填写完简单信息的 2是填完简单信息但是未进行企业认证 3是提交认证信息后认证中的 4是认证成功的
	 * 5是认证失败的 6子账户初始登录
	 * 
	 * @return
	 */
	private Integer certStatus;

	/**
	 * 微信的openID
	 * 
	 * @return
	 */
	private String wxOpenId;

	/**
	 * 微信的多应用统一ID
	 */
	private String wxUnionId;

	/**
	 * 企业ID
	 * 
	 * @return
	 */
	private Long enterpriseId;

	/**
	 * 注册终端类型
	 * 
	 * @return
	 */
	private String terminalType;

	/**
	 * APPSecret
	 * 
	 * @return
	 */
	private String appSecret;

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getSimpleEnterpriseName() {
		return simpleEnterpriseName;
	}

	public void setSimpleEnterpriseName(String simpleEnterpriseName) {
		this.simpleEnterpriseName = simpleEnterpriseName;
	}

	public Integer getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(Integer certStatus) {
		this.certStatus = certStatus;
	}

	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}

	public Long getEnterpriseId() {
		if (null == enterpriseId) {
			if (null != getEnterPrise()) {
				return Long.valueOf(getEnterPrise().getEnterpriseId());
			}
		}
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getWxUnionId() {
		return wxUnionId;
	}

	public void setWxUnionId(String wxUnionId) {
		this.wxUnionId = wxUnionId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

}
