package com.bvRadio.iLive.iLive.entity.base;

public class BaseAutoLogin {
	
	/**
	 * 主键id
	 */
	private Integer id;
	/**
	 * 直播系统分配给对接系统的ID
	 */
	private String appId;
	
	/**
	 * 直播系统分配给对接系统的secret
	 */
	private String secret;
	
	/**
	 * 系统当前时间
	 */
	private String nowTime;
	
	/**
	 * 加密信息
	 */
	private String token;
	
	/**
	 * 直播间id
	 */
	private Integer roomId;
	
	/**
	 * 回看id
	 */
	private Integer fieldId;
	
	/**
	 * 删除标识
	 */
	private Integer isDel;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getFieldId() {
		return fieldId;
	}

	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
}
