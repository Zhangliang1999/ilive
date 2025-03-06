package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;

public class IliveSubRoom implements Serializable{

private static final long serialVersionUID = -8946654035012790141L;
private Integer id;	
private Long subAccountId;
private Integer liveRoomId;

public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public Long getSubAccountId() {
	return subAccountId;
}
public void setSubAccountId(Long subAccountId) {
	this.subAccountId = subAccountId;
}
public Integer getLiveRoomId() {
	return liveRoomId;
}
public void setLiveRoomId(Integer liveRoomId) {
	this.liveRoomId = liveRoomId;
}

}
