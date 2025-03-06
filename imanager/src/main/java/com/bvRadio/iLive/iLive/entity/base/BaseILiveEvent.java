package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.web.ConfigUtils;

/**
 * @author administrator 直播房间下的场次
 */
@SuppressWarnings("serial")
public abstract class BaseILiveEvent implements java.io.Serializable {

	/**
	 * 直播场次ID
	 */
	private Long liveEventId;

	/**
	 * 直播标题
	 */
	private String liveTitle;

	/**
	 * 直播简介
	 */
	private String liveDesc;

	/**
	 * 直播开始时间
	 */
	private Timestamp liveStartTime;
	
	
	/**
	 * 直播命令开始时间
	 */
	private Timestamp realStartTime;
	
	
	/**
	 * 直播命令结束时间
	 */
	private Timestamp realEndTime;

	/**
	 * 直播结束时间
	 */
	private Timestamp liveEndTime;

	/**
	 * 主办方名称
	 */
	private String hostName;

	/**
	 * 直播场次LOGO
	 */
	private String logoUrl;

	/**
	 * LOGO位置 1:左上角 2:右上角 3:左下角 4:右下角
	 */
	private Integer logoPosition;

	/**
	 * LOGO位置的类
	 */
	private String logoClass;

	/**
	 * 直播状态 0 预告 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
	 */
	private Integer liveStatus;

	// ------------------------------------------------------------------

	/**
	 * logo显示开关 0为关闭 1为开启
	 */
	private Integer openLogoSwitch;

	/**
	 * 备播流地址开关
	 */
	private Integer openReadyPlayAddressSwitch;

	/**
	 * 直播报名表单开关 0为关闭 1为开启
	 */
	private Integer openFormSwitch;

	/**
	 * 引导图开关 0为关闭 1为开启
	 */
	private Integer openGuideSwitch;

	/**
	 * 倒计时开关 0为关闭 1为开启
	 */
	private Integer openCountdownSwitch;

	/**
	 * 观看人数显示开关 0为关闭 1为开启
	 */
	private Integer openViewNumSwitch;

	/**
	 * 自动审核秒数 如果为负值则为关闭
	 */
	private Integer autoCheckSecond;

	/**
	 * 是否开启审核
	 */
	private Integer openCheckSwitch;

	/**
	 * 是否开启弹幕互动
	 */
	private Integer openBarrageSwitch;

	/**
	 * 是否开启敏感字过滤
	 */
	private Integer openSensitivewordSwitch;

	/**
	 * 一键禁言开关 0为关闭 1为开启
	 */
	private Integer openForbidTalkSwitch;

	/**
	 * 数据美化开关
	 */
	private Integer openDataBeautifySwitch;

	// ------------------------------------------------------------------

	// 推流地址和观看地址是通过规则计算出来的 有的不需记录下来
	private String pushStreamAddr;

	/**
	 * 倒计时标题
	 */
	private String countdownTitle;

	/**
	 * 引导图地址
	 */
	private String guideAddr;

	/**
	 * 直播间封面地址
	 */
	private String converAddr;

	/**
	 * 播放器背景图片地址
	 */
	private String playBgAddr;

	/**
	 * 备播流地址
	 */
	private String readyPlayAddress;

	/**
	 * 直播间观看地址
	 */
	private String viewAddr;

	/**
	 * 直播播放器地址
	 */
	private String playerAddr;

	// ------------------------------------------------------------------

	/**
	 * 直播控制播放类型 : 1 推流直播 2 拉流直播 3 列表直播 4 PC手机桌面直播 5 手机直播 6 导播台
	 */
	private Integer playType;

	/**
	 * 拉流地址
	 */
	private String pollStreamAddr;

	/**
	 * 观看授权方式 1公开观看 2密码观看 3付费观看 4白名单 5登录观看
	 */
	private Integer viewAuthorized;

	/**
	 * 观看密码
	 * 
	 * @return
	 */
	private String viewPassword;

	/**
	 * 观看金额
	 * 
	 * @return
	 */
	private Double viewMoney;

	/**
	 * 是否启用F码观看
	 */
	private Integer openFCodeSwitch;

	/**
	 * 欢迎语
	 * 
	 * @return
	 */
	private String welcomeMsg;

	/**
	 * 文档直播是否同步直播
	 * 
	 * @return
	 */
	private Integer openSysnPlaySwitch;

	/**
	 * 延迟时间
	 */
	private Integer delaySeconds;

	/**
	 * 
	 * 处理中的图片地址
	 */
	private String docLivePicAddr;

	/**
	 * 直播对应的白名单包
	 * 
	 * @return
	 */
	private Long whiteBillPackageId;

	/**
	 * 直播对应的鉴权通过的白名单包ID
	 * 
	 * @return
	 */
	private Long viewAuthPackageId;

	/**
	 * 是否全局禁言 0 禁言开启 1 关闭禁言
	 */
	private Integer estoppelType;

	/**
	 * 当前观看人数
	 */
	private Long currentNum;

	/**
	 * 直播基础人数
	 */
	private Integer baseNum;

	/**
	 * 直播基础人数的倍数
	 */
	private Integer multiple;

	/**
	 * 前端显示的人数
	 */
	private Long showNum;

	/**
	 * 开启报名状态
	 */
	private Integer openSignupSwitch;

	/**
	 * 报名表单ID
	 * 
	 * @return
	 */
	private Integer formId;

	/**
	 * 直播间录制开始时间
	 * 
	 * @return
	 */
	private Timestamp recordStartTime;

	/**
	 * 直播间ID
	 */
	private Integer roomId;
	/**
	 * 是否删除
	 */
	private Boolean isDelete;
	/**
	 * 话题评论是否开启审核   0 关闭   1 启动
	 */
	private Integer commentsAudit;
	/**
	 * 话题是否允许评论    0 不允许    1 允许评论
	 */
	private Integer commentsAllow;
	
	/**
	 * 该场次是否发短信通知用户   默认0 、未通知       1、已通知
	 */
	private Integer isNotify;
	/**
	 * 签到开关： 0 关闭  1 开启
	 */
	private Integer isSign;
	/**
	 * 文档ID
	 */
	private Long documentId;
	/**
	 * 文档页码
	 */
	private Integer documentPageNO;
	/**
	 * 文档是否允许用户下载：0 关闭  1 开启
	 */
	private Integer documentDownload;
	/**
	 * 手动翻页是否开启：0 关闭  1 开启
	 */
	private Integer documentManual;
	
	
	private Integer hours;
	
	private Integer minutes;
	
	private String startDate;
	
	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Integer getDocumentPageNO() {
		return documentPageNO;
	}

	public void setDocumentPageNO(Integer documentPageNO) {
		this.documentPageNO = documentPageNO;
	}

	public Integer getDocumentDownload() {
		return documentDownload;
	}

	public void setDocumentDownload(Integer documentDownload) {
		this.documentDownload = documentDownload;
	}

	public Integer getDocumentManual() {
		return documentManual;
	}

	public void setDocumentManual(Integer documentManual) {
		this.documentManual = documentManual;
	}

	public Integer getIsSign() {
		return isSign;
	}

	public void setIsSign(Integer isSign) {
		this.isSign = isSign;
	}

	/**
	 * 直播开始、关闭时通知服务器的token
	 */
	private String notifyToken;
	
	public String getNotifyToken() {
		return notifyToken;
	}

	public void setNotifyToken(String notifyToken) {
		this.notifyToken = notifyToken;
	}

	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}

	public String getLiveDesc() {
		return liveDesc;
	}

	public void setLiveDesc(String liveDesc) {
		this.liveDesc = liveDesc;
	}

	public Timestamp getLiveStartTime() {
		return liveStartTime;
	}

	public void setLiveStartTime(Timestamp liveStartTime) {
		this.liveStartTime = liveStartTime;
	}

	public String getCountdownTitle() {
		return countdownTitle;
	}

	public void setCountdownTitle(String countdownTitle) {
		this.countdownTitle = countdownTitle;
	}

	public Integer getOpenFormSwitch() {
		return openFormSwitch;
	}

	public void setOpenFormSwitch(Integer openFormSwitch) {
		this.openFormSwitch = openFormSwitch;
	}

	public Integer getViewAuthorized() {
		return viewAuthorized;
	}

	public void setViewAuthorized(Integer viewAuthorized) {
		this.viewAuthorized = viewAuthorized;
	}

	public String getGuideAddr() {
		return guideAddr;
	}

	public void setGuideAddr(String guideAddr) {
		this.guideAddr = guideAddr;
	}

	public String getConverAddr() {
		if (converAddr == null || "".equals(converAddr)) {
			return ConfigUtils.get("defaultCoverAddr");
		}
		return converAddr;
	}

	public void setConverAddr(String converAddr) {
		this.converAddr = converAddr;
	}

	public String getViewAddr() {
		return viewAddr;
	}

	public void setViewAddr(String viewAddr) {
		this.viewAddr = viewAddr;
	}

	public Integer getPlayType() {
		return playType;
	}

	public void setPlayType(Integer playType) {
		this.playType = playType;
	}

	public Timestamp getLiveEndTime() {
		return liveEndTime;
	}

	public void setLiveEndTime(Timestamp liveEndTime) {
		this.liveEndTime = liveEndTime;
	}

	public String getPlayerAddr() {
		if (this.playBgAddr == null || "".equals(this.playBgAddr)) {
			return ConfigUtils.get("defaultLiveBgAddr");
		}
		return playerAddr;
	}

	public void setPlayerAddr(String playerAddr) {
		this.playerAddr = playerAddr;
	}

	public String getPollStreamAddr() {
		return pollStreamAddr;
	}

	public void setPollStreamAddr(String pollStreamAddr) {
		this.pollStreamAddr = pollStreamAddr;
	}

	public Integer getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(Integer liveStatus) {
		this.liveStatus = liveStatus;
	}

	public Integer getAutoCheckSecond() {
		return autoCheckSecond;
	}

	public void setAutoCheckSecond(Integer autoCheckSecond) {
		this.autoCheckSecond = autoCheckSecond;
	}

	public String getViewPassword() {
		return viewPassword;
	}

	public void setViewPassword(String viewPassword) {
		this.viewPassword = viewPassword;
	}

	public Double getViewMoney() {
		return viewMoney;
	}

	public void setViewMoney(Double viewMoney) {
		this.viewMoney = viewMoney;
	}

	public String getWelcomeMsg() {
		return welcomeMsg;
	}

	public void setWelcomeMsg(String welcomeMsg) {
		this.welcomeMsg = welcomeMsg;
	}

	public Integer getDelaySeconds() {
		return delaySeconds;
	}

	public void setDelaySeconds(Integer delaySeconds) {
		this.delaySeconds = delaySeconds;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Integer getLogoPosition() {
		return logoPosition;
	}

	public void setLogoPosition(Integer logoPosition) {
		this.logoPosition = logoPosition;
	}

	public String getDocLivePicAddr() {
		return docLivePicAddr;
	}

	public void setDocLivePicAddr(String docLivePicAddr) {
		this.docLivePicAddr = docLivePicAddr;
	}

	public Integer getOpenGuideSwitch() {
		return openGuideSwitch;
	}

	public void setOpenGuideSwitch(Integer openGuideSwitch) {
		this.openGuideSwitch = openGuideSwitch;
	}

	public Integer getOpenCountdownSwitch() {
		return openCountdownSwitch;
	}

	public void setOpenCountdownSwitch(Integer openCountdownSwitch) {
		this.openCountdownSwitch = openCountdownSwitch;
	}

	public Integer getOpenViewNumSwitch() {
		return openViewNumSwitch;
	}

	public void setOpenViewNumSwitch(Integer openViewNumSwitch) {
		this.openViewNumSwitch = openViewNumSwitch;
	}

	public Integer getOpenSysnPlaySwitch() {
		return openSysnPlaySwitch;
	}

	public void setOpenSysnPlaySwitch(Integer openSysnPlaySwitch) {
		this.openSysnPlaySwitch = openSysnPlaySwitch;
	}

	public Integer getOpenForbidTalkSwitch() {
		return openForbidTalkSwitch;
	}

	public void setOpenForbidTalkSwitch(Integer openForbidTalkSwitch) {
		this.openForbidTalkSwitch = openForbidTalkSwitch;
	}

	public Integer getOpenSensitivewordSwitch() {
		return openSensitivewordSwitch;
	}

	public void setOpenSensitivewordSwitch(Integer openSensitivewordSwitch) {
		this.openSensitivewordSwitch = openSensitivewordSwitch;
	}

	public Integer getOpenFCodeSwitch() {
		return openFCodeSwitch;
	}

	public void setOpenFCodeSwitch(Integer openFCodeSwitch) {
		this.openFCodeSwitch = openFCodeSwitch;
	}

	public Long getWhiteBillPackageId() {
		return whiteBillPackageId;
	}

	public void setWhiteBillPackageId(Long whiteBillPackageId) {
		this.whiteBillPackageId = whiteBillPackageId;
	}

	public Long getViewAuthPackageId() {
		return viewAuthPackageId;
	}

	public void setViewAuthPackageId(Long viewAuthPackageId) {
		this.viewAuthPackageId = viewAuthPackageId;
	}

	public String getPushStreamAddr() {
		return pushStreamAddr;
	}

	public void setPushStreamAddr(String pushStreamAddr) {
		this.pushStreamAddr = pushStreamAddr;
	}

	public Integer getOpenCheckSwitch() {
		return openCheckSwitch;
	}

	public void setOpenCheckSwitch(Integer openCheckSwitch) {
		this.openCheckSwitch = openCheckSwitch;
	}

	public Integer getOpenBarrageSwitch() {
		return openBarrageSwitch;
	}

	public void setOpenBarrageSwitch(Integer openBarrageSwitch) {
		this.openBarrageSwitch = openBarrageSwitch;
	}

	public String getPlayBgAddr() {
		return playBgAddr;
	}

	public void setPlayBgAddr(String playBgAddr) {
		this.playBgAddr = playBgAddr;
	}

	public Integer getEstoppelType() {
		return estoppelType;
	}

	public void setEstoppelType(Integer estoppelType) {
		this.estoppelType = estoppelType;
	}

	public Integer getBaseNum() {
		return baseNum;
	}

	public void setBaseNum(Integer baseNum) {
		this.baseNum = baseNum;
	}

	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	public Long getShowNum() {
		return showNum;
	}

	public void setShowNum(Long showNum) {
		this.showNum = showNum;
	}

	public Long getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(Long currentNum) {
		this.currentNum = currentNum;
	}

	public Integer getOpenLogoSwitch() {
		return openLogoSwitch;
	}

	public void setOpenLogoSwitch(Integer openLogoSwitch) {
		this.openLogoSwitch = openLogoSwitch;
	}

	public Integer getOpenDataBeautifySwitch() {
		return openDataBeautifySwitch;
	}

	public void setOpenDataBeautifySwitch(Integer openDataBeautifySwitch) {
		this.openDataBeautifySwitch = openDataBeautifySwitch;
	}

	public Integer getOpenReadyPlayAddressSwitch() {
		return openReadyPlayAddressSwitch;
	}

	public void setOpenReadyPlayAddressSwitch(Integer openReadyPlayAddressSwitch) {
		this.openReadyPlayAddressSwitch = openReadyPlayAddressSwitch;
	}

	public String getReadyPlayAddress() {
		return readyPlayAddress;
	}

	public void setReadyPlayAddress(String readyPlayAddress) {
		this.readyPlayAddress = readyPlayAddress;
	}

	public Integer getFormId() {
		return formId;
	}

	public void setFormId(Integer formId) {
		this.formId = formId;
	}

	public String getLogoClass() {
		return logoClass;
	}

	public void setLogoClass(String logoClass) {
		this.logoClass = logoClass;
	}

	public Timestamp getRecordStartTime() {
		return recordStartTime;
	}

	public void setRecordStartTime(Timestamp recordStartTime) {
		this.recordStartTime = recordStartTime;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getOpenSignupSwitch() {
		return openSignupSwitch;
	}

	public void setOpenSignupSwitch(Integer openSignupSwitch) {
		this.openSignupSwitch = openSignupSwitch;
	}

	public Timestamp getRealStartTime() {
		return realStartTime;
	}

	public void setRealStartTime(Timestamp realStartTime) {
		this.realStartTime = realStartTime;
	}

	public Timestamp getRealEndTime() {
		return realEndTime;
	}

	public void setRealEndTime(Timestamp realEndTime) {
		this.realEndTime = realEndTime;
	}

	public Integer getCommentsAudit() {
		return commentsAudit;
	}

	public void setCommentsAudit(Integer commentsAudit) {
		this.commentsAudit = commentsAudit;
	}

	public Integer getCommentsAllow() {
		return commentsAllow;
	}

	public void setCommentsAllow(Integer commentsAllow) {
		this.commentsAllow = commentsAllow;
	}

	public Integer getIsNotify() {
		return isNotify;
	}

	public void setIsNotify(Integer isNotify) {
		this.isNotify = isNotify;
	}

}