package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;



public class ILiveSubLevel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8946654035012790141L;
	private Long id;
	/**
	 * userid
	 */
	private Long userId;
	/**
	 * 子账号top权限管理 直播间创建：1;查看平台数据：2;粉丝信息管理：4 首页制作：5 ; 专题页制作：6 
	 */
	private String subTop;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getSubTop() {
		return subTop;
	}
	public void setSubTop(String subTop) {
		this.subTop = subTop;
	}
	
}
