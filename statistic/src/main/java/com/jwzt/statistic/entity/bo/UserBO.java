package com.jwzt.statistic.entity.bo;

/**
 * @author ysf
 */
public class UserBO {
	private Long id;
	private String username;
	private String nickname;
	private String userImg;
	private String roomIds;
	private String liveEventIds;
	private String enterpriseIds;
	private Boolean admin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getRoomIds() {
		return roomIds;
	}

	public void setRoomIds(String roomIds) {
		this.roomIds = roomIds;
	}

	public String getLiveEventIds() {
		return liveEventIds;
	}

	public void setLiveEventIds(String liveEventIds) {
		this.liveEventIds = liveEventIds;
	}

	public String getEnterpriseIds() {
		return enterpriseIds;
	}

	public void setEnterpriseIds(String enterpriseIds) {
		this.enterpriseIds = enterpriseIds;
	}

}
