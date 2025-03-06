package com.bvRadio.iLive.iLive.action.front.vo;

import javax.servlet.http.HttpServletRequest;

import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthentication;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

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
		/*报名不再需要登陆了
		if (openSignupSwitch == 1) {
			return true;
		}
		*/
		Integer need=liveEvent.getNeedLogin();
		need = need == null ? 0 : need;
		if(need==0) {
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
			//F码观看
			if (liveEvent.getViewAuthorized() == 6) {
				return true;
			}
			//第三方授权
			if (liveEvent.getViewAuthorized() == 7) {
				return true;
			}
		}else if(need==1){
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
		/*报名不再需要登陆了
		if (openSignupSwitch == 1) {
			return true;
		}
		*/
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
	 * 文件观看是否需要的登录
	 * 
	 * @param liveEvent
	 * @return
	 */
	public boolean needLoginByEvent(ILiveFileAuthentication fileAuthen) {
		if(null!=fileAuthen) {
			Integer needLogin = fileAuthen.getNeedLogin();
			if (needLogin == null) {
				needLogin = 0;
			}
			if(needLogin==1) {
				return true;
			}else {
				// 登录观看开启
				if (fileAuthen.getAuthType() == 5) {
					return true;
				}
				if (fileAuthen.getAuthType() == 6) {
					return true;
				}
				// 收费观看
				if (fileAuthen.getAuthType() == 3) {
					return true;
				}
				// 白名单观看
				if (fileAuthen.getAuthType() == 4) {
					return true;
				}
			}
			
		}
		return false;
	}

	/**
	 * 判断用户是否在线
	 */
	public boolean jungeUserSession(HttpServletRequest request) {
		UserBean userBean = ILiveUtils.getAppUser(request);
		if (userBean == null) {
			return false;
		} else {
			return true;
		}
	}

}
