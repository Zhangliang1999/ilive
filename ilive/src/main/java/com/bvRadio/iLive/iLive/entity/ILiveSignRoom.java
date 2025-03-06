package com.bvRadio.iLive.iLive.entity;

public class ILiveSignRoom {
	
	private Long id;
	
	private Long signId;
	
	private Integer roomId;
	
	private Integer isOpen;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSignId() {
		return signId;
	}

	public void setSignId(Long signId) {
		this.signId = signId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}
	
}
