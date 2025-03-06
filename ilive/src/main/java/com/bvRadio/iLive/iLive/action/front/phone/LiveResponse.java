package com.bvRadio.iLive.iLive.action.front.phone;

public class LiveResponse {
	private Integer uid;
	private String name;
	private String roomId;
	private String rtmpUrl;
	private String playUrl;
	private Integer status;
	private Integer type;
	private Integer isMixReady;
	private Integer isMixed;
	private String description;
	private String liveId;
	
	public LiveResponse() {
		super();
	}

	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getRtmpUrl() {
		return rtmpUrl;
	}
	public void setRtmpUrl(String rtmpUrl) {
		this.rtmpUrl = rtmpUrl;
	}
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getIsMixReady() {
		return isMixReady;
	}
	public void setIsMixReady(Integer isMixReady) {
		this.isMixReady = isMixReady;
	}
	public Integer getIsMixed() {
		return isMixed;
	}
	public void setIsMixed(Integer isMixed) {
		this.isMixed = isMixed;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getLiveId() {
		return liveId;
	}

	public void setLiveid(String liveId) {
		this.liveId = liveId;
	}
	

}
