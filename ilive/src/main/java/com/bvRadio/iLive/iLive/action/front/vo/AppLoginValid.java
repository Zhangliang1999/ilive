package com.bvRadio.iLive.iLive.action.front.vo;

import com.bvRadio.iLive.iLive.entity.ILiveManager;

/**
 * 登录校验返回类
 * 
 * @author administrator
 *
 */
public class AppLoginValid {

	// 1 不需要登录 2需要登录
	private Integer roomNeedLogin;

	/**
	 * 用户信息
	 */
	private ILiveManager userInfo;

	/**
	 * 用户引导图地址
	 * @return
	 */
	private String guideAddr;
	
	/**
	 * 用户引导图地址
	 * @return
	 */
	private String appGuideAddr;
	
	/**
	 * 是否开启引导图
	 * @return
	 */
	private  Integer openGuideSwitch;
	
	/**
	 * 是否登录过引导图页面
	 * @return
	 */
	private Integer repeateGuide = 0;
    /**
     * 直播间标题
     * @return
     */
	private String roomTitle;
	/**
	 * 用户手机号码
	 * @return
	 */
	private String mobile;
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

	public String getGuideAddr() {
		return guideAddr;
	}

	public void setGuideAddr(String guideAddr) {
		this.guideAddr = guideAddr;
	}

	public Integer getOpenGuideSwitch() {
		return openGuideSwitch;
	}

	public void setOpenGuideSwitch(Integer openGuideSwitch) {
		this.openGuideSwitch = openGuideSwitch;
	}

	public String getAppGuideAddr() {
		return appGuideAddr;
	}

	public void setAppGuideAddr(String appGuideAddr) {
		this.appGuideAddr = appGuideAddr;
	}

	public Integer getRepeateGuide() {
		return repeateGuide;
	}

	public void setRepeateGuide(Integer repeateGuide) {
		this.repeateGuide = repeateGuide;
	}

	public String getRoomTitle() {
		return roomTitle;
	}

	public void setRoomTitle(String roomTitle) {
		this.roomTitle = roomTitle;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	

}
