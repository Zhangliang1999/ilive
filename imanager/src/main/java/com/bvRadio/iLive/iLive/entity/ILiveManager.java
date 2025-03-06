package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveManager;

public class ILiveManager extends BaseILiveManager {

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
	 * 5是认证失败的
	 * 
	 * @return
	 */
	private Integer certStatus;

	private Timestamp registerStartTime;
	private Timestamp registerEndTime;
	
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

	public Timestamp getRegisterStartTime() {
		return registerStartTime;
	}

	public void setRegisterStartTime(Timestamp registerStartTime) {
		this.registerStartTime = registerStartTime;
	}

	public Timestamp getRegisterEndTime() {
		return registerEndTime;
	}

	public void setRegisterEndTime(Timestamp registerEndTime) {
		this.registerEndTime = registerEndTime;
	}
	
}
