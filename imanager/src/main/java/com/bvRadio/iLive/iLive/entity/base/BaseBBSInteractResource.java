package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;

@SuppressWarnings("serial")
public class BaseBBSInteractResource implements java.io.Serializable {
	private Integer id;
	private Integer interactId;
	private Integer interactType;
	private String filePath;
	private String thumbImgFilePath;
	private Timestamp createTime;
	private ILiveUploadServer iLiveUploadServer;
	private Integer duration;
	private Integer width;
	private Integer height;

	public BaseBBSInteractResource() {
		super();
	}

	public BaseBBSInteractResource(Integer id) {
		super();
		this.id = id;
	}

	

	public BaseBBSInteractResource(Integer id, Integer interactId,
			Integer interactType, String filePath, Timestamp createTime,
			com.bvRadio.iLive.iLive.entity.ILiveUploadServer iLiveUploadServer) {
		super();
		this.id = id;
		this.interactId = interactId;
		this.interactType = interactType;
		this.filePath = filePath;
		this.createTime = createTime;
		this.iLiveUploadServer = iLiveUploadServer;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getInteractId() {
		return interactId;
	}

	public void setInteractId(Integer interactId) {
		this.interactId = interactId;
	}

	public Integer getInteractType() {
		return interactType;
	}

	public void setInteractType(Integer interactType) {
		this.interactType = interactType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getThumbImgFilePath() {
		return thumbImgFilePath;
	}

	public void setThumbImgFilePath(String thumbImgFilePath) {
		this.thumbImgFilePath = thumbImgFilePath;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	
	public ILiveUploadServer getiLiveUploadServer() {
		return iLiveUploadServer;
	}

	public void setiLiveUploadServer(ILiveUploadServer iLiveUploadServer) {
		this.iLiveUploadServer = iLiveUploadServer;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
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

}
