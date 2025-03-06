package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * @author administrator 话题评论
 */
public abstract class BaseILiveTopicComment {

	/**
	 * 评论Id
	 */
	private Long commentId;

	/**
	 * 话题ID
	 */
	private Long topicId;

	/**
	 * 评论人ID
	 */
	private Long userId;

	/**
	 * 评论人名称
	 */
	private String userName;

	/**
	 * 评论时间
	 */
	private Timestamp commentTime;

	/**
	 * 评论内容
	 */
	private String commentContent;

	/**
	 * 审核状态
	 * 
	 * @return
	 */
	private Boolean checkState;

	/**
	 * 评论是否删除
	 * 
	 * @return
	 */
	private Boolean deleteState;

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Timestamp getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Boolean getCheckState() {
		return checkState;
	}

	public void setCheckState(Boolean checkState) {
		this.checkState = checkState;
	}

	public Boolean getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(Boolean deleteState) {
		this.deleteState = deleteState;
	}

}
