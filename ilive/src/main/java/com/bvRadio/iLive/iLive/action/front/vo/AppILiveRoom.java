package com.bvRadio.iLive.iLive.action.front.vo;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;

public class AppILiveRoom {
	
	ILiveAppEnterprise appEnterprise;
	
	/**
	 * 直播间ID
	 */
	private Integer roomId;
	
	/**
	 * 直播间描述
	 */
	private String roomDesc;
	
	/**
	 * 直播间名称
	 */
	private String roomName;
	
	/**
	 * 直播间状态
	 */
	private Integer liveStatus;
	
	/**
	 * 直播间封面图
	 * @return
	 */
	private  String roomImg;
     /**
      * 直播开始时间
      * @return
      */
    private Timestamp startTime;
    private ILiveLiveRoom room;
    private String message;
    private Integer roomType;
    
	public Integer getRoomType() {
		return roomType;
	}


	public void setRoomType(Integer roomType) {
		this.roomType = roomType;
	}


	public Integer getRoomId() {
		return roomId;
	}


	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
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


	public Integer getLiveStatus() {
		return liveStatus;
	}


	public void setLiveStatus(Integer liveStatus) {
		this.liveStatus = liveStatus;
	}


	public String getRoomImg() {
		return roomImg;
	}


	public void setRoomImg(String roomImg) {
		this.roomImg = roomImg;
	}


	public ILiveAppEnterprise getAppEnterprise() {
		return appEnterprise;
	}


	public void setAppEnterprise(ILiveAppEnterprise appEnterprise) {
		this.appEnterprise = appEnterprise;
	}


	public Timestamp getStartTime() {
		return startTime;
	}


	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}


	public ILiveLiveRoom getRoom() {
		return room;
	}


	public void setRoom(ILiveLiveRoom room) {
		this.room = room;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	
	

}
