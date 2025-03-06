package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * 观看记录页
 * 
 * @author administrator
 */
public abstract class BaseILiveViewRecord {

	// 记录ID
	private Long recordId;

	// 观看人
	private Long managerId;

	// 记录时间
	private Timestamp recordTime;

	// 观看类型 1直播 2回看
	private Integer viewType;

	// 外部观看的主键ID 直播间ID 或者 回看文件ID
	private Long outerId;
	
	// 观看类型   1 app  2 web
	private Integer viewOrigin;
	
	// 删除标识 1为删除 0为未删除
	private Integer deleteStatus;

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public Timestamp getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Timestamp recordTime) {
		this.recordTime = recordTime;
	}

	public Integer getViewType() {
		return viewType;
	}

	public void setViewType(Integer viewType) {
		this.viewType = viewType;
	}

	public Long getOuterId() {
		return outerId;
	}

	public void setOuterId(Long outerId) {
		this.outerId = outerId;
	}

	public Integer getViewOrigin() {
		return viewOrigin;
	}

	public void setViewOrigin(Integer viewOrigin) {
		this.viewOrigin = viewOrigin;
	}

	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
	
	

}
