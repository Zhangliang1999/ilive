package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 图片库中的图片
 * @author Wei
 *
 */
public class PictureInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9048484143228177335L;
	/**
	 * 图片id
	 */
	private Long id;
	/**
	 * 图片库id
	 */
	private Long galleryId;
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
	 * 图片上传人
	 */
	private String userName;
	
	/**
	 * 最后修改人
	 */
	private String editName;
	
	private Integer width;
	
	private Integer height;
	
	/**
	 * 企业id
	 */
	private Integer enterpriseId;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;
	
	/**
	 * 上传人userId
	 */
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(Long galleryId) {
		this.galleryId = galleryId;
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
