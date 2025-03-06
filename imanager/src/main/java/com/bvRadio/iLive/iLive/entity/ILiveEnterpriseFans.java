package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

public class ILiveEnterpriseFans {

	//主键id
	private Long id;
	
	//企业id
	private Integer enterpriseId;
	
	//用户id
	private Long userId;
	
	//是否删除		1为删除
	private Integer isDel;
	
	//  1为加入黑名单
	private Integer isBlacklist;
	
	// 置顶标识
	private Integer topFlag;

	// 关注时间
	private Timestamp concernTime;
	
	/**
	 * 禁言状态
	 * @return
	 */
	private Integer forbidState;
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getIsBlacklist() {
		return isBlacklist;
	}

	public void setIsBlacklist(Integer isBlacklist) {
		this.isBlacklist = isBlacklist;
	}

	public Integer getTopFlag() {
		return topFlag;
	}

	public void setTopFlag(Integer topFlag) {
		this.topFlag = topFlag;
	}

	public Timestamp getConcernTime() {
		return concernTime;
	}

	public void setConcernTime(Timestamp concernTime) {
		this.concernTime = concernTime;
	}

	public Integer getForbidState() {
		return forbidState;
	}

	public void setForbidState(Integer forbidState) {
		this.forbidState = forbidState;
	}
	
	
	
}
