package com.bvRadio.iLive.iLive.entity;


public class ILiveHomepageStructure {

	private Integer id;
	/**
	 * 结构id
	 */
	private Integer structureId;
	
	/**
	 * 结构类型    1顶部背景图	2企业信息	3分割线行	4内容显示(一行一个)	5内容显示(一行两个)
	 */
	private Integer type;
	
	/**
	 * 显示顺序
	 */
	private Integer orders;
	
	/**
	 * 企业id
	 */
	private Integer enterpriseId;
	
	/**
	 * 显示方针 1、手动选择    2、自动选择
	 */
	private Integer policy;
	
	/**
	 * 显示内容的类型
	 */
	private Integer showContentType;
	
	/**
	 * 显示数量
	 */
	private Integer showNum;

	public Integer getStructureId() {
		return structureId;
	}

	public void setStructureId(Integer structureId) {
		this.structureId = structureId;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getShowNum() {
		return showNum;
	}

	public void setShowNum(Integer showNum) {
		this.showNum = showNum;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPolicy() {
		return policy;
	}

	public void setPolicy(Integer policy) {
		this.policy = policy;
	}

	public Integer getShowContentType() {
		return showContentType;
	}

	public void setShowContentType(Integer showContentType) {
		this.showContentType = showContentType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
