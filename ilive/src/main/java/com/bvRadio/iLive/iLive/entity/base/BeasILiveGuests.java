package com.bvRadio.iLive.iLive.entity.base;


/**
 * 直播间嘉宾
 * @author YanXL
 *
 */
public abstract class BeasILiveGuests implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 嘉宾ID
	 */
	private Integer roomId;
	/**
	 * 嘉宾名称
	 */
	private String guestsName;
	/**
	 * 标签
	 */
	private String guestsLabel;
	public String getGuestsName() {
		return guestsName;
	}
	public void setGuestsName(String guestsName) {
		this.guestsName = guestsName;
	}
	public String getGuestsLabel() {
		return guestsLabel;
	}
	public void setGuestsLabel(String guestsLabel) {
		this.guestsLabel = guestsLabel;
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public BeasILiveGuests() {
		super();
	}
	public BeasILiveGuests(Integer roomId, String guestsName, String guestsLabel) {
		super();
		this.roomId = roomId;
		this.guestsName = guestsName;
		this.guestsLabel = guestsLabel;
	}
	
}
