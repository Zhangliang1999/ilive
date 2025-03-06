package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * @author administrator 企业评论信息
 */
public class BaseILiveCertTopic {

	private Long topicId;

	private String topicComment;

	private Timestamp commentTime;

	private Integer enterpriseId;

	private Long managerId;

	private String managerName;

	private Integer certStatus;
	
	/**
	 * 申请人id
	 */
	private Long userId;
	
	/**
	 * 申请人姓名
	 */
	private String userPhone;
	


	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getTopicComment() {
		return topicComment;
	}

	public void setTopicComment(String topicComment) {
		this.topicComment = topicComment;
	}

	public Timestamp getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(Integer certStatus) {
		this.certStatus = certStatus;
	}

}
