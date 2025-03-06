package com.bvRadio.iLive.manager.entity.base;

import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

public class BaseILiveRemEnterprise {
	
	private Long id;
	private ILiveEnterprise enterprise;
	private Long orderNum;
	private Integer topNum;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ILiveEnterprise getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(ILiveEnterprise enterprise) {
		this.enterprise = enterprise;
	}
	
	public Integer getTopNum() {
		return topNum;
	}
	public void setTopNum(Integer topNum) {
		this.topNum = topNum;
	}
	public Long getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Long orderNum) {
		this.orderNum = orderNum;
	}
	public BaseILiveRemEnterprise() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
