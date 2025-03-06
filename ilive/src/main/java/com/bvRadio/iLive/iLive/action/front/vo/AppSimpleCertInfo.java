package com.bvRadio.iLive.iLive.action.front.vo;

/**
 * 开通直播前
 * 
 * @author administrator
 *
 */
public class AppSimpleCertInfo {

	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 确认密码
	 */
	private String confirmPassword;

	/**
	 * 简单企业信息
	 */
	private String simpleEnterpriseName;

	/**
	 * 手机号码
	 */
	private String phoneNum;

	/**
	 * 账号名称
	 * @return
	 */
	private String userName;
	
	/**
	 * 验证码
	 */
	private String vpassword;
	

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSimpleEnterpriseName() {
		return simpleEnterpriseName;
	}

	public void setSimpleEnterpriseName(String simpleEnterpriseName) {
		this.simpleEnterpriseName = simpleEnterpriseName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVpassword() {
		return vpassword;
	}

	public void setVpassword(String vpassword) {
		this.vpassword = vpassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
	

}
