package com.bvRadio.iLive.iLive.action.front.vo;

public class IliveAppRetInfo {

	
	/**
	 * 用户ID
	 */
	private Long userId;

	
	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 手机号
	 */
	private String mobile;

	
	/**
	 * 头像
	 */
	private String photo;

	
	/**
	 * 昵称
	 */
	private String nailName;

	
	/**
	 * 登录token
	 */
	private String loginToken;

	/**
	 * 极光ID
	 */
	private String jpushId;
	
	
	/**
	 * 关联的企业
	 */
	private ILiveAppEnterprise enterprise;

	// 认证状态  0未提交认证  1认证中  2认证通过   3认证失败
	private Integer certStatus;
	
	/**
	 * 是否绑定了手机号 0未绑定  1绑定
	 * @return
	 */
	private Integer bindMobile;

	private Integer isIpV6;
	
		
	public Integer getIsIpV6() {
		return isIpV6;
	}

	public void setIsIpV6(Integer isIpV6) {
		this.isIpV6 = isIpV6;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getNailName() {
		return nailName;
	}

	public void setNailName(String nailName) {
		this.nailName = nailName;
	}

	public ILiveAppEnterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(ILiveAppEnterprise enterprise) {
		this.enterprise = enterprise;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getJpushId() {
		return jpushId;
	}

	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}

	public Integer getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(Integer certStatus) {
		this.certStatus = certStatus;
	}

	public Integer getBindMobile() {
		return bindMobile;
	}

	public void setBindMobile(Integer bindMobile) {
		this.bindMobile = bindMobile;
	}
	
	
	

}
