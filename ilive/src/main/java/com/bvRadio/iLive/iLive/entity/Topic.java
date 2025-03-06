package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * 专题
 * @author Wei
 *
 */
public class Topic {

	/**
	 * 专题id
	 */
	private Long id;
	
	/**
	 * 专题的logo
	 */
	private String logo;
	
	/**
	 * 用户id
	 */
	private long userId;
	
	/**
	 * 用户名称
	 */
	private String userName;
	
	/**
	 * 封面图
	 */
	private String banner;
	
	/**
	 * 专题模板id
	 */
	private Long templateId;
	
	/**
	 * 专题名称
	 */
	private String name;
	
	/**
	 * 专题描述
	 */
	private String descript;
	
	/**
	 * 是否发布 0未发布  1发布
	 */
	private Integer isPublish;
	
	/**
	 * 是否上架  0未上架  1上架
	 */
	private Integer isPut;
	
	/**
	 * 企业id
	 */
	private Integer enterpriseId;
	
	/**
	 * 包含的内容
	 */
	private List<TopicInnerType> listType;
	
	/**
	 * 使用的模板
	 */
	private TopicTemplate topicTemplate;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	/**
	 * 专题域名
	 */
	private String domain;
	
	/**
	 * 正式
	 */
	//public final static String prefixDoamin = "http://zb.tv189.com/topic/topic.html?id=";
	
	/**
	 * 测试
	 */
	public final static String prefixDoamin = "http://zbt.tv189.net/topic/topic.html?id=";
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Integer getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Integer isPublish) {
		this.isPublish = isPublish;
	}

	public Integer getIsPut() {
		return isPut;
	}

	public void setIsPut(Integer isPut) {
		this.isPut = isPut;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public TopicTemplate getTopicTemplate() {
		return topicTemplate;
	}

	public void setTopicTemplate(TopicTemplate topicTemplate) {
		this.topicTemplate = topicTemplate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public List<TopicInnerType> getListType() {
		return listType;
	}

	public void setListType(List<TopicInnerType> listType) {
		this.listType = listType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
