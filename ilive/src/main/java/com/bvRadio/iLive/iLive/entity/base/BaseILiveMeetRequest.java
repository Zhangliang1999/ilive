package com.bvRadio.iLive.iLive.entity.base;

public class BaseILiveMeetRequest {
	
	private String id;
	private Integer roomId;
	private String password;
	private String roomName;
	private String pic;
	/**
	 * 登陆地址
	 */
	private String loginUrl;
	/**
	 * 邀请信息
	 */
	private String message;
	
	private Integer type;
	



	public String getRoomName() {
		return roomName;
	}




	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}




	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public Integer getRoomId() {
		return roomId;
	}




	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public String getPic() {
		return pic;
	}




	public void setPic(String pic) {
		this.pic = pic;
	}




	public String getLoginUrl() {
		return loginUrl;
	}




	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}




	public String getMessage() {
		return message;
	}




	public void setMessage(String message) {
		this.message = message;
	}




	public Integer getType() {
		return type;
	}




	public void setType(Integer type) {
		this.type = type;
	}




	public BaseILiveMeetRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
}
