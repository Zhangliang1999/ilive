package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;

public class BaseILiveMediaFileComments {
	/**
	 * 主键
	 */
	private Long commentsId;

	/**
	 * 评论内容
	 */
	private String comments;
	/**
	 * 评论的用户
	 */
	private String commentsUser;
	
	/**
	 * 创建时间（第一次创建时改动）
	 */
	private Timestamp createTime;	
	/**
	 * 更新时间（每次有记录更新时都改动）
	 */
	private Timestamp updateTime;	
	/**
	 * 点赞数
	 */
	private Long appreciateCount;
	
	
	/**
	 * 审核状态
	 */
	private Integer checkState;
	
	/**
	 * 点灭数
	 */
	private Long objectionCount;
	
	private ILiveMediaFile iLiveMediaFile;
	
	/**
	 * 关联的用户Id
	 */
	private Long userId;
	
	/**
	 * 删除标识
	 * @return
	 */
	private Integer delFlag;

	public Long getCommentsId() {
		return commentsId;
	}

	public void setCommentsId(Long commentsId) {
		this.commentsId = commentsId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCommentsUser() {
		return commentsUser;
	}

	public void setCommentsUser(String commentsUser) {
		this.commentsUser = commentsUser;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Long getAppreciateCount() {
		return appreciateCount;
	}

	public void setAppreciateCount(Long appreciateCount) {
		this.appreciateCount = appreciateCount;
	}

	public Long getObjectionCount() {
		return objectionCount;
	}

	public void setObjectionCount(Long objectionCount) {
		this.objectionCount = objectionCount;
	}

	public ILiveMediaFile getiLiveMediaFile() {
		return iLiveMediaFile;
	}

	public void setiLiveMediaFile(ILiveMediaFile iLiveMediaFile) {
		this.iLiveMediaFile = iLiveMediaFile;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	

}
