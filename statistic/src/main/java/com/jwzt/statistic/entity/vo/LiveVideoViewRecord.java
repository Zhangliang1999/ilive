package com.jwzt.statistic.entity.vo;

public class LiveVideoViewRecord {
	
	private String id;
	
	private Integer roomId;
	
	private Long videoId;
	
	private String userId;
	
	private String startTime;
	
	private String endTime;
	
	private Long startTimetemp;
	
	private Long endTimeTemp;

	/**
	 * 时长
	 */
	private Float duration;
	
	//时长，保留两位小数
	private String durationStr;
	
	private String ip;
	
	private String ipArea;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIpArea() {
		return ipArea;
	}

	public void setIpArea(String ipArea) {
		this.ipArea = ipArea;
	}

	public Long getStartTimetemp() {
		return startTimetemp;
	}

	public void setStartTimetemp(Long startTimetemp) {
		this.startTimetemp = startTimetemp;
	}

	public Long getEndTimeTemp() {
		return endTimeTemp;
	}

	public void setEndTimeTemp(Long endTimeTemp) {
		this.endTimeTemp = endTimeTemp;
	}

	public Float getDuration() {
		return duration;
	}

	public void setDuration(Float duration) {
		this.duration = duration;
	}

	public String getDurationStr() {
		return durationStr;
	}

	public void setDurationStr(String durationStr) {
		this.durationStr = durationStr;
	}
	
}
