package com.bvRadio.iLive.iLive.entity.base;

/**
 * @author administrator 直播场次对应的节目单直播 主要为了记录单场直播使用的哪些节目单
 */
public class BaseILiveEventProgram {

	/**
	 * 单条节目的Id
	 */
	private Long programId;

	/**
	 * 关联文件的ID
	 */
	private Long fileId;
	
	/**
	 * 顺序
	 */
	private Integer order;

	/**
	 * 所属哪个包下
	 * @return
	 */
	private Long packageId;

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

}
