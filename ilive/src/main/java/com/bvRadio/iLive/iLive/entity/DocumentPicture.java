package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

public class DocumentPicture {

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 对应的文档id
	 */
	private Long docId;
	/**
	 * 图片名称
	 */
	private String name;
	/**
	 * 图片地址
	 */
	private String url;
	/**
	 * 图片描述
	 */
	private String descript;

	/**
	 * 图片类型
	 */
	private String type;
	
	/**
	 * 图片大小
	 */
	private Integer size;
	
	/**
	 * 宽度
	 */
	private Integer width;
	
	/**
	 * 高度
	 */
	private Integer height;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
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

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
}
