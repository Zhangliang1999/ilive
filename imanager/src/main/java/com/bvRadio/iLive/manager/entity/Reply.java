package com.bvRadio.iLive.manager.entity;

import java.sql.Timestamp;

/**
 * 回复
 * @author Wei
 *
 */
public class Reply {
	
	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 举报或投诉id
	 */
	private Long reportId;
	
	/**
	 * 类型    1举报  2投诉
	 */
	private Integer type;
	
	/**
	 * 回复内容
	 */
	private String replayContent;
	
	/**
	 * 回复人id
	 */
	private Long userId;
	
	/**
	 * 回复人账号
	 */
	private Long userName;
	
	/**
	 * 回复人昵称
	 */
	private Long userNailName;
	
	/**
	 * 回复渠道
	 */
	private Integer replyType;
	
	/**
	 * 回复时间
	 */
	private Timestamp createTime;
	
	/**
	 * 是否删除 0未删除  1删除
	 */
	private Integer isDel;

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getReplayContent() {
		return replayContent;
	}

	public void setReplayContent(String replayContent) {
		this.replayContent = replayContent;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserName() {
		return userName;
	}

	public void setUserName(Long userName) {
		this.userName = userName;
	}

	public Long getUserNailName() {
		return userNailName;
	}

	public void setUserNailName(Long userNailName) {
		this.userNailName = userNailName;
	}

	public Integer getReplyType() {
		return replyType;
	}

	public void setReplyType(Integer replyType) {
		this.replyType = replyType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
