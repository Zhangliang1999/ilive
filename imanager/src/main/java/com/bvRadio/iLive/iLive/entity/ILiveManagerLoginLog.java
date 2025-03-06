package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

/**
 * 用户信息登录关联表
 * @author administrator
 *
 */
public class ILiveManagerLoginLog {
	
	private Long logId;
	
	private Timestamp logTime;
	
	private String loginUser;
	
	private Long managerId;
	
	private Integer loginType;

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Timestamp getLogTime() {
		return logTime;
	}

	public void setLogTime(Timestamp logTime) {
		this.logTime = logTime;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
	
	

}
