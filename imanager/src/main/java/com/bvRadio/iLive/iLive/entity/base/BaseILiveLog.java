package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

@SuppressWarnings("serial")
public abstract class BaseILiveLog implements java.io.Serializable {

	private Integer id;
	private String event;//时间
	private Timestamp createTime;//发生时间
	private Integer status;//记录日志是事件的状态
	private Integer roomId;//房间号
	private Integer liveId;//直播Id
	public BaseILiveLog(Integer id, String event, Timestamp createTime,
			Integer status, Integer roomId, Integer liveId) {
		super();
		this.id = id;
		this.event = event;
		this.createTime = createTime;
		this.status = status;
		this.roomId = roomId;
		this.liveId = liveId;
	}
	public BaseILiveLog() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Integer getLiveId() {
		return liveId;
	}
	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}


}