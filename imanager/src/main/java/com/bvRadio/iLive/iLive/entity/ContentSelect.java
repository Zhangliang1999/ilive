package com.bvRadio.iLive.iLive.entity;

public class ContentSelect {

	/**
	 * 主键 id
	 */
	private Integer id;
	
	
	/**
	 * 企业id
	 */
	private Integer enterpriseId;
	
	/**
	 * 选择内容的类型	1直播间	2视频		3专题	
	 */
	private Integer contentType;
	
	/**
	 * 选择内容的id
	 */
	private Integer contentId;
	
	/**
	 * 选择内容的名称
	 */
	private String contentName;
	
	/**
	 * 选择内容的图片
	 */
	private String contentImg;
	
	/**
	 * 选择内容简介
	 */
	private String contentBrief;
	
	/**
	 * 直播类型   1 直播	2 回顾	3 专题
	 */
	private Integer type;
	
	//显示方式   1为背景色      2为一行一个的标题       3为一行一个的内容      4为一个两个的标题     5为一行两个的内容
	private Integer shows;
	
	/**
	 * 选择内容排序
	 */
	private Integer indexs;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getContentImg() {
		return contentImg;
	}

	public void setContentImg(String contentImg) {
		this.contentImg = contentImg;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIndexs() {
		return indexs;
	}

	public void setIndexs(Integer indexs) {
		this.indexs = indexs;
	}

	public Integer getShows() {
		return shows;
	}

	public void setShows(Integer shows) {
		this.shows = shows;
	}

	public String getContentBrief() {
		return contentBrief;
	}

	public void setContentBrief(String contentBrief) {
		this.contentBrief = contentBrief;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
}
