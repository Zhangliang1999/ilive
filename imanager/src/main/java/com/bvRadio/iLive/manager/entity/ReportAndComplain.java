package com.bvRadio.iLive.manager.entity;

import java.sql.Timestamp;

/**
 * 举报和投诉
 * @author Wei
 *
 */
public class ReportAndComplain {

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 直播间id
	 */
	private Integer roomId;
	
	/**
	 * 当前直播间id
	 */
	private Integer curRoomId;
	
	/**
	 * 当前直播名称
	 */
	private String nowRoomName;
	
	/**
	 * 提交用户id
	 */
	private Long userId;
	
	/**
	 * 提交用户账号
	 */
	private String submitUser;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	/**
	 * 回复状态   0未回复   1已回复
	 */
	private Integer replyStatus;
	
	/**
	 * 投诉建议人联系方式
	 */
	private String userContact;
	
	/**
	 * 类型   1举报   2投诉
	 */
	private Integer type;
	
	/**
	 * 举报理由
	 */
	private String reportReason;

	/**
	 * 举报图片
	 */
	private String reportImg;
	
	/**
	 * 建议内容
	 */
	private String suggest;
	
	/**
	 * 是否删除  0未删除   1删除
	 */
	private Integer isDel;
	
	
	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserContact() {
		return userContact;
	}

	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}

	public String getReportReason() {
		return reportReason;
	}

	public void setReportReason(String reportReason) {
		this.reportReason = reportReason;
	}

	public String getReportImg() {
		return reportImg;
	}

	public void setReportImg(String reportImg) {
		this.reportImg = reportImg;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getCurRoomId() {
		return curRoomId;
	}

	public void setCurRoomId(Integer curRoomId) {
		this.curRoomId = curRoomId;
	}

	public String getNowRoomName() {
		return nowRoomName;
	}

	public void setNowRoomName(String nowRoomName) {
		this.nowRoomName = nowRoomName;
	}

	public String getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(Integer replyStatus) {
		this.replyStatus = replyStatus;
	}
	
}
