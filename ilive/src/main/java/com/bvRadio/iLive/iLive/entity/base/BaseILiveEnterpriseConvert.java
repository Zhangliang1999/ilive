package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;
import java.util.Set;

import com.bvRadio.iLive.iLive.entity.BBSDiyformData;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

/**
 * 
 * @author zpn
 * 转码任务表
 */
@SuppressWarnings("serial")
public abstract class BaseILiveEnterpriseConvert implements java.io.Serializable {
	
	
	/**
	 * 企业ID
	 */
	private Long enterpriseId;
	
	
	
	/**
	 * 转码模板ID
	 */
	private Integer templetId;



	public Long getEnterpriseId() {
		return enterpriseId;
	}



	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}



	public Integer getTempletId() {
		return templetId;
	}



	public void setTempletId(Integer templetId) {
		this.templetId = templetId;
	}
	
	
	

	
	
	

}