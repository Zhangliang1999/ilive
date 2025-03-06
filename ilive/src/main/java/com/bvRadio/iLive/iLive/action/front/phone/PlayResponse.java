package com.bvRadio.iLive.iLive.action.front.phone;

import java.util.ArrayList;
import java.util.List;

public class PlayResponse {
	private Integer uid;
	private String name;
	private String roomId;
	private String playUrl;
	private String description;
	private List<UserVO> userList = new ArrayList<UserVO>();
	
	public PlayResponse() {
		super();
	}
	public PlayResponse(Integer uid, String name, String roomId,
			String playUrl /*Mns mns, String description*/) {
		this.uid = uid;
		this.name = name;
		this.roomId = roomId;
		this.playUrl = playUrl;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<UserVO> getUserList() {
		return userList;
	}
	public void setUserList(List<UserVO> userList) {
		this.userList = userList;
	}
	
	
}
