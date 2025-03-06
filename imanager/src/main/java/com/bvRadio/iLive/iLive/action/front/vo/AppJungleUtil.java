package com.bvRadio.iLive.iLive.action.front.vo;

import javax.servlet.http.HttpServletRequest;

import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.UserBean;

public enum AppJungleUtil {

	INSTANCE;

	/**
	 * 是否需要登录
	 * 
	 * @return
	 */
	public boolean needLogin(ILiveLiveRoom iliveRoom) {
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		Integer openSignupSwitch = liveEvent.getOpenSignupSwitch();
		openSignupSwitch = openSignupSwitch == null ? 0 : openSignupSwitch;
		// 报名开启
		if (openSignupSwitch == 1) {
			return true;
		}
		// 登录观看开启
		if (liveEvent.getViewAuthorized() == 5) {
			return true;
		}
		// 收费观看
		if (liveEvent.getViewAuthorized() == 3) {
			return true;
		}
		// 白名单观看
		if (liveEvent.getViewAuthorized() == 4) {
			return true;
		}

		return false;
	}
	
	
	/**
	 * 是否需要登录
	 * 
	 * @return
	 */
	public boolean needLoginByEvent(ILiveEvent liveEvent) {
		Integer openSignupSwitch = liveEvent.getOpenSignupSwitch();
		openSignupSwitch = openSignupSwitch == null ? 0 : openSignupSwitch;
		// 报名开启
		if (openSignupSwitch == 1) {
			return true;
		}
		// 登录观看开启
		if (liveEvent.getViewAuthorized() == 5) {
			return true;
		}
		// 收费观看
		if (liveEvent.getViewAuthorized() == 3) {
			return true;
		}
		// 白名单观看
		if (liveEvent.getViewAuthorized() == 4) {
			return true;
		}

		return false;
	}

	/**
	 * 判断用户是否在线
	 */
	public boolean jungeUserSession(HttpServletRequest request) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
		if (userBean == null) {
			return false;
		}else {
			return true;
		}
//		if (userBean.getUserId().equals(request.getParameter("userId")) ) {
//			return true;
//		}
	}


}
