package com.bvRadio.iLive.iLive.action.front.vo;

public class AppUserInfo {

	/**
	 * 用户昵称
	 */
	private String nailName;
	
	/**
	 * 用户Id
	 */
	private Long userId;
	
	/**
	 * 用户LoginToken
	 */
	private String loginToken;

	/**
	 * 用户头像
	 */
	private String userImg;
	
	/**
	 * 注册时间
	 * @return
	 */
	private String registerTime;
	
	
	/**
	 * 手机号
	 * @return
	 */
	private String phoneNum;
	
	/**
	 * 终端类型
	 * @return
	 */
	private String terminalType;
	

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getNailName() {
		return nailName;
	}

	public void setNailName(String nailName) {
		this.nailName = nailName;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
	
	
	

}
