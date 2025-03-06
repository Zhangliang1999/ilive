package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

public class ILiveEnterpriseTerminalUser {
	
	//主键id
	private Long id;
	
	//企业id
	private Integer enterpriseId;
	
	//用户id
	private Long userId;
	
	//用户账号
	private String userName;
	
	//用户头像
	private String userImg;
	
	//用户昵称
	private String nailName;
	
	//登录方式   0为app登录 1为web登录
	private Integer loginType;
	
	//最后一次登录时间
	private Timestamp lastLoginTime;
	
	//是否删除		1为删除
	private Integer isDel;
	
	// 粉丝类型  0、普通观看用户   1、关注用户 
	private Integer fansType;
	
	
	//  1为加入黑名单
	private Integer isBlacklist;
	
	/**
	 * 用户账号
	 */
	private String mobile;
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getNailName() {
		return nailName;
	}

	public void setNailName(String nailName) {
		this.nailName = nailName;
	}

	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getIsBlacklist() {
		return isBlacklist;
	}

	public void setIsBlacklist(Integer isBlacklist) {
		this.isBlacklist = isBlacklist;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public Integer getFansType() {
		return fansType;
	}

	public void setFansType(Integer fansType) {
		this.fansType = fansType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
	
}
