package com.bvRadio.iLive.iLive.action.front.vo;

/**
 * 直播间修改VO
 * 
 * @author administrator
 */
public class RoomEditVo extends RoomCreateVo {

	// 直播间状态
	private Integer liveSwitch = 0;

	// 直播间ID
	private Integer roomId;

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getLiveSwitch() {
		
		return liveSwitch;
	}

	public void setLiveSwitch(Integer liveSwitch) {
		this.liveSwitch = liveSwitch;
	}
	
	
	

}
