package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;

public class IliveRoomThird implements Serializable{

private static final long serialVersionUID = -8946654035012790141L;
private Integer id;	
/**
 * 房间ID
 */
private Integer roomId;
/**
 * 第三方推流名称
 */
private String thirdName;
/**
 * 第三方推流RTMP地址
 */
private String rtmpAddr;
/**
 * 直播码
 */
private String liveCode;
/**
 * 推流状态   1 未开启 2 正在推流 3 推流结束
 */
private Integer tstatus;
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public Integer getRoomId() {
	return roomId;
}
public void setRoomId(Integer roomId) {
	this.roomId = roomId;
}
public String getThirdName() {
	return thirdName;
}
public void setThirdName(String thirdName) {
	this.thirdName = thirdName;
}
public String getRtmpAddr() {
	return rtmpAddr;
}
public void setRtmpAddr(String rtmpAddr) {
	this.rtmpAddr = rtmpAddr;
}
public String getLiveCode() {
	return liveCode;
}
public void setLiveCode(String liveCode) {
	this.liveCode = liveCode;
}
public Integer getTstatus() {
	return tstatus;
}
public void setTstatus(Integer tstatus) {
	this.tstatus = tstatus;
}



}
