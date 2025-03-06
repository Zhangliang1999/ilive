package com.bvRadio.iLive.iLive.entity.base;

public class BaseILiveFileAuthentication {
	
	/**
	 * 文件ID
	 */
	private Long fileId;
	
	/**
	 * 鉴权类型   
	 */
	private Integer authType;
	
	/**
	 * 观看密码
	 */
	private String viewPassword;
	
	/**
	 * 观看密码
	 */
	private String welcomeMsg;
	/**
	 * 付费金额 单位：元
	 */
	private Double viewMoney;
	/**
	 * 是否开启F码 1 开启 0 关闭
	 */
	private Integer openFCodeSwitch;
	/**
	 * 外链地址
	 */
     private String outLinkUrl;
     /**
      * 是否开启F码外链地址
      */
     private Integer fopenStatus;
	private Integer needLogin;
	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Integer getAuthType() {
		return authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
	}

	public String getViewPassword() {
		return viewPassword;
	}

	public void setViewPassword(String viewPassword) {
		this.viewPassword = viewPassword;
	}

	public String getWelcomeMsg() {
		return welcomeMsg;
	}

	public void setWelcomeMsg(String welcomeMsg) {
		this.welcomeMsg = welcomeMsg;
	}

	public Double getViewMoney() {
		return viewMoney;
	}

	public void setViewMoney(Double viewMoney) {
		this.viewMoney = viewMoney;
	}

	public Integer getOpenFCodeSwitch() {
		return openFCodeSwitch;
	}

	public void setOpenFCodeSwitch(Integer openFCodeSwitch) {
		this.openFCodeSwitch = openFCodeSwitch;
	}

	public Integer getNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(Integer needLogin) {
		this.needLogin = needLogin;
	}

	public String getOutLinkUrl() {
		return outLinkUrl;
	}

	public void setOutLinkUrl(String outLinkUrl) {
		this.outLinkUrl = outLinkUrl;
	}

	public Integer getFopenStatus() {
		return fopenStatus;
	}

	public void setFopenStatus(Integer fopenStatus) {
		this.fopenStatus = fopenStatus;
	}
	

}
