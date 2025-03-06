package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * 
 * @author ysf 
 */
public abstract class BaseILiveMediaFileRelated {

	private String id;
	private Long mainFileId;
	private Long relatedFileId;
	private Double orderNum;
	private Timestamp createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getMainFileId() {
		return mainFileId;
	}

	public void setMainFileId(Long mainFileId) {
		this.mainFileId = mainFileId;
	}

	public Long getRelatedFileId() {
		return relatedFileId;
	}

	public void setRelatedFileId(Long relatedFileId) {
		this.relatedFileId = relatedFileId;
	}

	public Double getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Double orderNum) {
		this.orderNum = orderNum;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
