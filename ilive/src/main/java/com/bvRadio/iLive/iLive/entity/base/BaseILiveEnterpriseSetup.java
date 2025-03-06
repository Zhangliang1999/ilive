package com.bvRadio.iLive.iLive.entity.base;

public class BaseILiveEnterpriseSetup {
	
	private Long id;
	private Integer enterpriseId;
	/**
	 * 广告语
	 */
	private String slogan;
	/**
	 * 提示
	 */
	private String prompt;
	private String advertisementImg;
	
	private Integer isImg;
	
	public Integer getIsImg() {
		return isImg;
	}
	public void setIsImg(Integer isImg) {
		this.isImg = isImg;
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
	public String getSlogan() {
		return slogan;
	}
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public String getAdvertisementImg() {
		return advertisementImg;
	}
	public void setAdvertisementImg(String advertisementImg) {
		this.advertisementImg = advertisementImg;
	}
	public BaseILiveEnterpriseSetup() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
