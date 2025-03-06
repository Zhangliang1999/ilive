package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

public class ILiveAppointment {

	/**
	 * 主键id
	 */
	private Integer id;
	
	/**
	 * user id
	 */
	private String userid;
	
	/**
	 * 直播间id
	 */
	private Integer roomId;
	
	/**
	 * 直播场次id
	 */
	private Long liveEventId;
	
	/**
	 * 直播标题
	 */
	private String liveTitle;
	
	/**
	 * 直播开始时间
	 */
	private Timestamp startTime;
	
	/**
	 * 直播结束时间
	 */
	private Timestamp endTime;
	
	/**
	 * 预约时间
	 */
	private Timestamp createTime;
	
	/**
	 * 预约标记    0、为预约     1为取消预约  
	 */
	private Integer mark;
	
	private ILiveManager iLiveManager;
	
	public ILiveManager getiLiveManager() {
		return iLiveManager;
	}

	public void setiLiveManager(ILiveManager iLiveManager) {
		this.iLiveManager = iLiveManager;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}


	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}

	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

}
