package com.bvRadio.iLive.iLive.entity.vo;

import java.sql.Timestamp;

public class ILiveRoomVo {
	
	private Integer openStatus;
	
	private String liveTitle;
	
	private Timestamp liveStartTime;
	
	private Timestamp liveEndTime;
	
	private Integer liveStatus;
	
	private Long liveEventId;

	public Integer getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(Integer openStatus) {
		this.openStatus = openStatus;
	}

	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}

	public Timestamp getLiveStartTime() {
		return liveStartTime;
	}

	public void setLiveStartTime(Timestamp liveStartTime) {
		this.liveStartTime = liveStartTime;
	}

	public Timestamp getLiveEndTime() {
		return liveEndTime;
	}

	public void setLiveEndTime(Timestamp liveEndTime) {
		this.liveEndTime = liveEndTime;
	}

	public Integer getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(Integer liveStatus) {
		this.liveStatus = liveStatus;
	}

	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}
	
	
	

}
