package com.bvRadio.iLive.iLive.entity.base;

@SuppressWarnings("serial")
public abstract class BaseUserRoomRelation implements java.io.Serializable {

	// Fields
	private Integer id;
	private Integer uid;
	private Integer roomId;
	public BaseUserRoomRelation(Integer id, Integer uid, Integer roomId) {
		super();
		this.id = id;
		this.uid = uid;
		this.roomId = roomId;
	}
	public BaseUserRoomRelation() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	
}