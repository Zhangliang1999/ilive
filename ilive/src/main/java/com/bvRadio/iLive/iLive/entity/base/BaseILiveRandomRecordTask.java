package com.bvRadio.iLive.iLive.entity.base;

/**
 * 直播随机收录任务
 * @author administrator
 *
 */
public class BaseILiveRandomRecordTask {
	// 任务Id
	private Long taskId;
	
	//用户uId
	private Long userId;
	
	//直播间ID
	private Integer roomId;
	
	//直播场次Id
	private Long liveEventId;
	
	//收录开始时间
	private Long startTime;
	
	// 收录结束时间
	private Long endTime;
	
	private String phoneNum;
	
	//直播状态
	private Integer liveStatus;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public Integer getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(Integer liveStatus) {
		this.liveStatus = liveStatus;
	}
	
	
	

}
