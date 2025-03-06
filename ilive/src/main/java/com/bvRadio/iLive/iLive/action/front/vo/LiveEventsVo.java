package com.bvRadio.iLive.iLive.action.front.vo;

public class LiveEventsVo {

	/**
	 * 直播间ID
	 */
	private Integer roomId;

	/**
	 * 场次Id
	 */
	private Long liveEventId;

	/**
	 * 场次数据访问Url
	 */
	private String accessUrl;

	/**
	 * 观看总数
	 */
	private Long viewTotal;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 直播间描述
	 */
	private String roomDesc;

	/**
	 * 直播间名称
	 */
	private String roomName;

	/**
	 * 直播间封面图
	 * @return
	 */
	private String roomImg;

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

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	public Long getViewTotal() {
		return viewTotal;
	}

	public void setViewTotal(Long viewTotal) {
		this.viewTotal = viewTotal;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomImg() {
		return roomImg;
	}

	public void setRoomImg(String roomImg) {
		this.roomImg = roomImg;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	

}
