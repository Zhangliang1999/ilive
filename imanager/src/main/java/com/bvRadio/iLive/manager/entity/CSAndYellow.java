package com.bvRadio.iLive.manager.entity;

import java.sql.Timestamp;

/**
 * CS 反恐精英  和鉴黄
 * @author 
 *
 */
public class CSAndYellow {

	/**
	 * 主键
	 */
	private String taskId;
	
	/**
	 * 直播间id
	 */
	private Integer roomId;
	
	/**
	 * 直播场次id
	 */
	private Long eventId;
	
	/**
	 * 图片地址
	 */
	private String picUrl;
	
	/**
	 * 反恐鉴黄信息
	 */
	private String replyMsg;
	
	/**
	 * 1 需要审核   2建议禁止  3已忽略
	 */
	private Integer monitorLevel;
	
	/**
	 * 发生时间
	 */
	private Timestamp checkTime;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getReplyMsg() {
		return replyMsg;
	}

	public void setReplyMsg(String replyMsg) {
		this.replyMsg = replyMsg;
	}

	public Integer getMonitorLevel() {
		return monitorLevel;
	}

	public void setMonitorLevel(Integer monitorLevel) {
		this.monitorLevel = monitorLevel;
	}

	public Timestamp getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Timestamp checkTime) {
		this.checkTime = checkTime;
	}
	
}
