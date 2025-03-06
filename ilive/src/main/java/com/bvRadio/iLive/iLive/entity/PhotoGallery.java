package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
/**
 * 图片库
 * @author Wei
 *
 */
import java.sql.Timestamp;
public class PhotoGallery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4810813223635650442L;

	/**
	 * 图片库id
	 */
	private Long id;
	
	/**
	 * 图片库名称
	 */
	private String name;
	
	/**
	 * 图片库背景
	 */
	private String url;
	
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
