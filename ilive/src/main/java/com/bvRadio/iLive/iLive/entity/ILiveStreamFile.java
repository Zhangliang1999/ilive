package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 媒资文件
 * 
 * @author wxy
 */
public class ILiveStreamFile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8946654035012790141L;
	
	/**
	 * 流id
	 */
	private Long id;
	/**
	 * 流名称
	 */
	private String name;
	/**
	 * 流地址
	 */
	private String url;

	/**
	 * 流添加人
	 */
	private String userName;
	/**
	 * 添加人userId
	 */
	private Long userId;
	/**
	 * 最后修改人
	 */
	private String editName;
	/**
	 * 企业id
	 */
	private Integer enterpriseId;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEditName() {
		return editName;
	}

	public void setEditName(String editName) {
		this.editName = editName;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
