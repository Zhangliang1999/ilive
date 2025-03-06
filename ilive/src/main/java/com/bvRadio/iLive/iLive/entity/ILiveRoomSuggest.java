package com.bvRadio.iLive.iLive.entity;

public class ILiveRoomSuggest {
	
	private Long suggestId;
	
	private Integer roomId;
	
	private Long liveEventId;
	
	private Long userId;
	
	private  String content;


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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getSuggestId() {
		return suggestId;
	}

	public void setSuggestId(Long suggestId) {
		this.suggestId = suggestId;
	}
	
	
	


}
