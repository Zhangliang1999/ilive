package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

public class BaseILiveUserMeetRole {
	
	private Long id;
	private Integer roomId;
	
	private String userId;
	
	private Timestamp createTime;
	
	/**
	 * 1 主持人 2创建者 3与会者
	 */
	private Integer roleType;
	
	private String nickName;
	
	
	
	public String getNickName() {
		return nickName;
	}





	public void setNickName(String nickName) {
		this.nickName = nickName;
	}





	public Long getId() {
		return id;
	}





	public void setId(Long id) {
		this.id = id;
	}





	public Integer getRoomId() {
		return roomId;
	}





	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}





	public String getUserId() {
		return userId;
	}





	public void setUserId(String userId) {
		this.userId = userId;
	}





	public Timestamp getCreateTime() {
		return createTime;
	}





	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}





	public Integer getRoleType() {
		return roleType;
	}





	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}





	public BaseILiveUserMeetRole() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
