package com.bvRadio.iLive.iLive.action.front.vo;

/**
 * 直播创建VO
 * 
 * @author administrator
 *
 */
public class RoomCreateVo {


	// 直播标题
	private String liveTitle;
	
	//直播描述
	private String liveDesc;

	// 企业名称
	private String hostname;

	// 直播开始时间
	private String liveStartTime;

	// 封面图地址
	private String converAddr;

	// 鉴权类型   1公开观看 2密码观看 3付费观看 4白名单 5登录观看
	private String viewAuthorized;

	// 观看密码
	private String viewPassword;
	
	//是否开启logo 1是开启 0是关闭
	private Integer openLogoSwitch;
	
	/**
	 * 是否开启向粉丝推送 1是开启 0是关闭 
	 */
	private Integer pushMsgSwitch;

	// 台标位置
	private String logoPosition;

	// 台标图片地址
	private String logoUrl;

	// 直播间创建人
	private String userName;

	private Long userId;
	
	//是否开启连麦直播 1：关闭 2：开启
	private Integer liveType;
	
	//直播延迟
	private Integer liveDelay;
	//强制登录
	private Integer needLogin;
    //会议1  直播0
	private Integer roomType;
	//会议创建成功能力房间id
	private Integer meetId;
	
	public Integer getRoomType() {
		return roomType;
	}

	public void setRoomType(Integer roomType) {
		this.roomType = roomType;
	}

	public Integer getMeetId() {
		return meetId;
	}

	public void setMeetId(Integer meetId) {
		this.meetId = meetId;
	}

	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getLiveStartTime() {
		return liveStartTime;
	}

	public void setLiveStartTime(String liveStartTime) {
		this.liveStartTime = liveStartTime;
	}

	public String getConverAddr() {
		return converAddr;
	}

	public void setConverAddr(String converAddr) {
		this.converAddr = converAddr;
	}

	public String getViewAuthorized() {
		return viewAuthorized;
	}

	public void setViewAuthorized(String viewAuthorized) {
		this.viewAuthorized = viewAuthorized;
	}

	public String getViewPassword() {
		return viewPassword;
	}

	public void setViewPassword(String viewPassword) {
		this.viewPassword = viewPassword;
	}

	public String getLogoPosition() {
		return logoPosition;
	}

	public void setLogoPosition(String logoPosition) {
		this.logoPosition = logoPosition;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLiveDesc() {
		return liveDesc;
	}

	public void setLiveDesc(String liveDesc) {
		this.liveDesc = liveDesc;
	}

	public Integer getOpenLogoSwitch() {
		return openLogoSwitch;
	}

	public void setOpenLogoSwitch(Integer openLogoSwitch) {
		this.openLogoSwitch = openLogoSwitch;
	}

	public Integer getPushMsgSwitch() {
		return pushMsgSwitch;
	}

	public void setPushMsgSwitch(Integer pushMsgSwitch) {
		this.pushMsgSwitch = pushMsgSwitch;
	}

	public Integer getLiveType() {
		return liveType;
	}

	public void setLiveType(Integer liveType) {
		this.liveType = liveType;
	}

	public Integer getLiveDelay() {
		return liveDelay;
	}

	public void setLiveDelay(Integer liveDelay) {
		this.liveDelay = liveDelay;
	}

	public Integer getNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(Integer needLogin) {
		this.needLogin = needLogin;
	}
	
	
	

}
