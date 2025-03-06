package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.base.BaseBBSDiyformData;


@SuppressWarnings("serial")
public class BBSDiyformData extends BaseBBSDiyformData implements java.io.Serializable {
	
	/**
	 * 报名用户ID
	 */
	private Long managerId;
	
	/**
	 * 手机号码
	 */
	private String phoneNum;
	
	
	/**
	 * 报名问卷问题ID
	 */
	private Integer modelId;
	
	/**
	 * 报名提交ID
	 */
	private Long submitId;
	
	public BBSDiyformData() {
	}

	public BBSDiyformData(Integer id, Integer dataId, String dataTitle, Timestamp createTime, String dataValue, Integer dataOrder,
			BBSDiyform bbsDiyform) {
		super(id, dataId, dataTitle, createTime, dataValue, dataOrder, bbsDiyform);
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public Long getSubmitId() {
		return submitId;
	}

	public void setSubmitId(Long submitId) {
		this.submitId = submitId;
	}
	
	
	
	
}
