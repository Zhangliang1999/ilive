package com.bvRadio.iLive.manager.entity;

public class InstationMessageUser {

	private Long id;
	
	/**
	 * 站内信id
	 */
	private Long messageId;
	
	/**
	 * 接收者账号
	 */
	private Long recUserId;
	
	/**
	 * 接收者手机号
	 */
	private String mobile;
	
	private Integer type;
	
	private String title;
	
	private String content;
	
	private String enterpriseName;
	
	private Integer enterpriseId;
	
	private String enterpriseImg;
	
	

	public String getEnterpriseImg() {
		return enterpriseImg;
	}

	public void setEnterpriseImg(String enterpriseImg) {
		this.enterpriseImg = enterpriseImg;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Long getRecUserId() {
		return recUserId;
	}

	public void setRecUserId(Long recUserId) {
		this.recUserId = recUserId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
