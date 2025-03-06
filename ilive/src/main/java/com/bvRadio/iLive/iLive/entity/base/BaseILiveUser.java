package com.bvRadio.iLive.iLive.entity.base;

/**
 * @author administrator 基础直播用户 观看端用户
 */
public abstract class BaseILiveUser {

	/**
	 * 用户ID 主键
	 */
	private Integer userId;

	/**
	 * 用户账号
	 */
	private String userName;

	/**
	 * 用户类型
	 */
	private Integer userType;

	/**
	 * 是否被禁用
	 */
	private Boolean disabled;

	/**
	 * 用户头像
	 */
	private String userImg;

	/**
	 * 用户级别
	 */
	private Integer userLevel;

	/**
	 * 昵称
	 */
	private String nailName;

	/**
	 * 用户手机号
	 */
	private String userPhone;

	/**
	 * 极光推送ID
	 */
	private String jpushId;

	public BaseILiveUser() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getNailName() {
		return nailName;
	}

	public void setNailName(String nailName) {
		this.nailName = nailName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getJpushId() {
		return jpushId;
	}

	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}
	
	

}
