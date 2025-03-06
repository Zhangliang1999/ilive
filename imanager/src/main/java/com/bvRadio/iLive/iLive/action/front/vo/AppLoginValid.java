package com.bvRadio.iLive.iLive.action.front.vo;

import com.bvRadio.iLive.iLive.entity.ILiveManager;

public class AppLoginValid {

	private Integer roomNeedLogin;

	private ILiveManager userInfo;

	public Integer getRoomNeedLogin() {
		return roomNeedLogin;
	}

	public void setRoomNeedLogin(Integer roomNeedLogin) {
		this.roomNeedLogin = roomNeedLogin;
	}

	public ILiveManager getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(ILiveManager userInfo) {
		this.userInfo = userInfo;
	}

}
