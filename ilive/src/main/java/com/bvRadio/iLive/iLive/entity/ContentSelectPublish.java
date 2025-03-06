package com.bvRadio.iLive.iLive.entity;

public class ContentSelectPublish{

	/**
	 * 主键 id
	 */
	private Integer id;
	
	/**
	 * 企业id
	 */
	private Integer enterpriseId;
	
	/**
	 * 结构id
	 */
	private Integer structureId;
	
	/**
	 * 选择内容的类型	1直播间	2视频		3专题	   4链接
	 */
	private Integer contentType;
	
	/**
	 * 选择内容的id
	 */
	private Integer contentId;
	
	/**
	 * 内容的名称
	 */
	private String contentName;
	
	/**
	 * 自定义的名称
	 */
	private String defindName;
	
	/**
	 * 内容的图片
	 */
	private String contentImg;
	
	/**
	 * 选择内容简介
	 */
	private String contentBrief;
	
	
	//////////////////////////////////////////
	//显示方式   1为背景色      2为一行一个的标题       3为一行一个的内容      4为一个两个的标题     5为一行两个的内容
	private Integer shows;
	
	/**
	 * 选择内容排序
	 */
	private Integer indexs;
	
	/**
	 * 直播状态 0 预告 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
	 */
	private Integer liveStatus;
	
	/**
	 * 地址
	 */
	private String contentUrl;
	
	/**
	 * 链接地址名称
	 */
	private String urlName;

	public String getDefindName() {
		return defindName;
	}

	public void setDefindName(String defindName) {
		this.defindName = defindName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getStructureId() {
		return structureId;
	}

	public void setStructureId(Integer structureId) {
		this.structureId = structureId;
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

	public String getContentBrief() {
		return contentBrief;
	}

	public void setContentBrief(String contentBrief) {
		this.contentBrief = contentBrief;
	}

	public Integer getShows() {
		return shows;
	}

	public void setShows(Integer shows) {
		this.shows = shows;
	}

	public Integer getIndexs() {
		return indexs;
	}

	public void setIndexs(Integer indexs) {
		this.indexs = indexs;
	}

	public Integer getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(Integer liveStatus) {
		this.liveStatus = liveStatus;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	
	
}
