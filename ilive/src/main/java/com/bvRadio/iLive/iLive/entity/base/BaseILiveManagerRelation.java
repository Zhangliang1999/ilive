package com.bvRadio.iLive.iLive.entity.base;

/**
 * @author administrator
 * 用户关联
 */
public abstract class BaseILiveManagerRelation {
	
	/**
	 * 关联ID
	 */
	private Long relateId;
	
	
	/**
	 * 管理员ID
	 */
	private Long managerId;
	
	/**
	 * 直播间ID
	 */
	private Long enterpriseId;
	
	/**
	 * 标识
	 */
	private Integer managerType;

	public Long getRelateId() {
		return relateId;
	}

	public void setRelateId(Long relateId) {
		this.relateId = relateId;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getManagerType() {
		return managerType;
	}

	public void setManagerType(Integer managerType) {
		this.managerType = managerType;
	}
	

}
