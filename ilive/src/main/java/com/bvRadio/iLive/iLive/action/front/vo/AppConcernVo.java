package com.bvRadio.iLive.iLive.action.front.vo;

public class AppConcernVo {

	/**
	 * 企业
	 */
	ILiveAppEnterprise enterprise;

	/**
	 * 直播间
	 */
	AppILiveRoom iliveRoom;

	/**
	 * 查出来的房间个数
	 */
	private Integer roomNum;

	public ILiveAppEnterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(ILiveAppEnterprise enterprise) {
		this.enterprise = enterprise;
	}

	public AppILiveRoom getIliveRoom() {
		return iliveRoom;
	}

	public void setIliveRoom(AppILiveRoom iliveRoom) {
		this.iliveRoom = iliveRoom;
	}

	public Integer getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(Integer roomNum) {
		this.roomNum = roomNum;
	}

}
