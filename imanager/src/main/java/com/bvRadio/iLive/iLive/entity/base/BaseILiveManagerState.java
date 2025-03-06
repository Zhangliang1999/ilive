package com.bvRadio.iLive.iLive.entity.base;

/**
 * 用户状态表
 * @author administrator
 *
 */
public class BaseILiveManagerState {
	
	/**
	 *用户ID
	 */
	private Long managerId;
	
	/**
	 * 认证状态
	 */
	private Integer certStatus = 0;


	public Long getManagerId() {
		return managerId;
	}


	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}


	public Integer getCertStatus() {
		return certStatus;
	}


	public void setCertStatus(Integer certStatus) {
		this.certStatus = certStatus;
	}

}
