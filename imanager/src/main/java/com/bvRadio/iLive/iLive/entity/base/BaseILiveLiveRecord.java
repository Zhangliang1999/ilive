package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

@SuppressWarnings("serial")
public abstract class BaseILiveLiveRecord implements java.io.Serializable {
	
	private Integer id;//id
	private Integer userId;//用户Id
	private Integer roomId;//房间Id
	private Timestamp beginTime;//开始时间
	private Timestamp endTime;//结束时间
	private Integer income;//直播收益
	private Integer totalNumber;//总人数
	public BaseILiveLiveRecord(Integer id, Integer userId, Integer roomId,
			Timestamp beginTime, Timestamp endTime, Integer income,
			Integer totalNumber) {
		super();
		this.id = id;
		this.userId = userId;
		this.roomId = roomId;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.income = income;
		this.totalNumber = totalNumber;
	}
	public BaseILiveLiveRecord() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Timestamp getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public Integer getIncome() {
		return income;
	}
	public void setIncome(Integer income) {
		this.income = income;
	}
	public Integer getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}
	
}