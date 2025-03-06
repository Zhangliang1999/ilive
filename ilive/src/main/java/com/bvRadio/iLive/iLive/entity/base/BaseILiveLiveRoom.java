package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.ILiveEvent;

/**
 * @author administrator 直播间Bean
 */
@SuppressWarnings("serial")
public abstract class BaseILiveLiveRoom implements java.io.Serializable {

	/**
	 * 房间ID
	 */
	private Integer roomId;

	/**
	 * 房间开关状态 0关闭 1开启
	 */
	private Integer openStatus;

	/**
	 * 禁用标识 0正常 1禁用
	 */
	private Integer disabled;

	/**
	 * 房间创建时间
	 */
	private Timestamp createTime;

	/**
	 * 房间对应场次
	 */
	private ILiveEvent liveEvent;

	/**
	 * 是否开启直播余额限制
	 */
	private Boolean moneyLimitSwitch;

	/**
	 * 直播间消费金额上限
	 */
	private Double affordLimit;

	/**
	 * 直播消费余额
	 */
	private Double affordMoney;

	/**
	 * 直播创建人
	 * 
	 */
	private Long managerId;

	/**
	 * 直播创建人名称
	 */
	private String createPerson;

	/**
	 * 直播观看人数
	 */
	private Long liveViewNum;

	/**
	 * 企业ID
	 */
	private Integer enterpriseId;

	/**
	 * 直播间对应的服务器组ID 可做软负载
	 */
	private Integer serverGroupId;

	/**
	 * 删除状态
	 */
	private Integer deleteStatus;

	/**
	 * 关联的垫片文件ID
	 */
	private Long relateSlimFileId;
	
	/**
	 * 关联的垫片文件的url
	 */
	private String relateSlimFileUrl;
	
	/**
	 * 关联垫片文件的服务器本地地址
	 */
	private String relateSlimFileLocalUrl;

	/**
	 * 垫片文件是否打开 0关闭 1打开
	 */
	private Integer openSlimModel;
	/**
	 * 签到是否打开 0关闭 1打开
	 */
	private Integer openSign;
	/**
	 * 需要绑定手机是否打开 0关闭 1打开
	 */
	private Integer openNeedMobile;
	/**
	 * 是否启动礼物 0 关闭  1启动
	 */
	private Integer isSystemGift;
	/**
	 * 是否启动个人礼物  0 关闭  1启动
	 */
	private Integer isUserGift;
	/**
	 * 直播间结束是否录制 1 开启   0关闭
	 */
	private Integer endRecordingVal;
	/**
	 * 虚拟人数
	 */
	private Integer roomFictitious;
	private String subAccountId;
	/**
	 * 直播间logo图片
	 */
	private String logoImg;
	/**
	 * 直播间背景图片
	 */
    private String bgdImg;
    /**
     * 转码器任务id
     */
    private Integer convertTaskId;
    /**
     * 转码器地址
     */
    private String convertTaskIp;
    /**
     * 水印logo
     */
    private String convertLogo;
    /**
     * 
     */
    private String converLogoFtp;
    /**
     * 水印标记
     */
	private Integer logoPosition;
	/**
     * 水印开关
     */
	private Integer openLogoSwitch;
	/**
     * 水印位置
     */
	private String logoClass;
	/**
	 * 图片宽
	 */
	private Integer logoWidth;
	/**
	 * 图片高
	 */
	private Integer logoHight;
	/**
	 * 是否开启备流
	 */
	private Integer openBackupStream;
	
	/**
	 * 主备流状态
	 */
	private Integer streamStatus;
	
	private Integer roomType;
	
	private String meetId;
	
	/**
	 * 会议转推房间
	 * @return
	 */
	private Integer meetPushRoomId;
	/**
	 * 是否开启相关视频推荐
	 * @return
	 */
	private Integer relatedVideo=1;
	public Integer getMeetPushRoomId() {
		return meetPushRoomId;
	}

	public void setMeetPushRoomId(Integer meetPushRoomId) {
		this.meetPushRoomId = meetPushRoomId;
	}

	public String getMeetId() {
		return meetId;
	}

	public void setMeetId(String meetId) {
		this.meetId = meetId;
	}

	public Integer getRoomType() {
		return roomType;
	}

	public void setRoomType(Integer roomType) {
		this.roomType = roomType;
	}

	public Integer getStreamStatus() {
		return streamStatus;
	}

	public void setStreamStatus(Integer streamStatus) {
		this.streamStatus = streamStatus;
	}

	public Integer getOpenBackupStream() {
		return openBackupStream;
	}

	public void setOpenBackupStream(Integer openBackupStream) {
		this.openBackupStream = openBackupStream;
	}

	public String getConverLogoFtp() {
		return converLogoFtp;
	}

	public void setConverLogoFtp(String converLogoFtp) {
		this.converLogoFtp = converLogoFtp;
	}

	public Integer getLogoWidth() {
		return logoWidth;
	}

	public void setLogoWidth(Integer logoWidth) {
		this.logoWidth = logoWidth;
	}

	public Integer getLogoHight() {
		return logoHight;
	}

	public void setLogoHight(Integer logoHight) {
		this.logoHight = logoHight;
	}

	public String getLogoClass() {
		return logoClass;
	}

	public void setLogoClass(String logoClass) {
		this.logoClass = logoClass;
	}

	public String getConvertLogo() {
		return convertLogo;
	}

	public void setConvertLogo(String convertLogo) {
		this.convertLogo = convertLogo;
	}

	public Integer getLogoPosition() {
		return logoPosition;
	}

	public void setLogoPosition(Integer logoPosition) {
		this.logoPosition = logoPosition;
	}

	public Integer getOpenLogoSwitch() {
		return openLogoSwitch;
	}

	public void setOpenLogoSwitch(Integer openLogoSwitch) {
		this.openLogoSwitch = openLogoSwitch;
	}

	public Integer getConvertTaskId() {
		return convertTaskId;
	}
	
	public void setConvertTaskId(Integer convertTaskId) {
		this.convertTaskId = convertTaskId;
	}

	public String getConvertTaskIp() {
		return convertTaskIp;
	}

	public void setConvertTaskIp(String convertTaskIp) {
		this.convertTaskIp = convertTaskIp;
	}

    /**
	 * 当前状态是否为立刻插播视频
	 */
	private Integer isNowInsert = 0;
   

	public String getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}

	public String getBgdImg() {
		return bgdImg;
	}

	public void setBgdImg(String bgdImg) {
		this.bgdImg = bgdImg;
	}

	public String getSubAccountId() {
		return subAccountId;
	}

	public void setSubAccountId(String subAccountId) {
		this.subAccountId = subAccountId;
	}

	public Integer getEndRecordingVal() {
		return endRecordingVal;
	}

	public void setEndRecordingVal(Integer endRecordingVal) {
		this.endRecordingVal = endRecordingVal;
	}

	public Integer getRoomFictitious() {
		return roomFictitious;
	}

	public void setRoomFictitious(Integer roomFictitious) {
		this.roomFictitious = roomFictitious;
	}

	/**
	 * 直播延迟时间
	 */
	private Integer liveDelay;
	
	public Integer getIsSystemGift() {
		return isSystemGift;
	}
	
	/**
	 * 直播类型：1普通 2连麦
	 */
	private Integer liveType;
	/**
	 * 是否启动协同助手  0 关闭  1启动
	 */
	private Integer isAssistant;

	/**
	 * 延时时间长 最小 1
	 */
	private Integer isdelayedTimer;
	/**
	 * 是否启动延时  1 启动  0 关闭
	 */
	private Integer isDelayed;
	/**
	 * 绑定CDN ID
	 */
	private Integer cdnId;
	/**
	 * 推流域名
	 */
	private String cdnPushFlow;
	/**
	 * 拉流域名
	 */
	private String cdnPulla;
	/**
	 * web播放流明称
	 */
	private String pcStreamName;
	/**
	 * 手机端播放流明称
	 */
	private String phoneStreamName;
	
	/**
	 * 拉流直播地址
	 */
	private String pullAddr;
	
	private String enterpriseName;

	public String getPullAddr() {
		return pullAddr;
	}

	public void setPullAddr(String pullAddr) {
		this.pullAddr = pullAddr;
	}

	public Integer getIsdelayedTimer() {
		return isdelayedTimer;
	}

	public void setIsdelayedTimer(Integer isdelayedTimer) {
		this.isdelayedTimer = isdelayedTimer;
	}

	public Integer getIsDelayed() {
		return isDelayed;
	}

	public void setIsDelayed(Integer isDelayed) {
		this.isDelayed = isDelayed;
	}

	public Integer getCdnId() {
		return cdnId;
	}

	public void setCdnId(Integer cdnId) {
		this.cdnId = cdnId;
	}

	public String getCdnPushFlow() {
		return cdnPushFlow;
	}

	public void setCdnPushFlow(String cdnPushFlow) {
		this.cdnPushFlow = cdnPushFlow;
	}

	public String getCdnPulla() {
		return cdnPulla;
	}

	public void setCdnPulla(String cdnPulla) {
		this.cdnPulla = cdnPulla;
	}

	public String getPcStreamName() {
		return pcStreamName;
	}

	public void setPcStreamName(String pcStreamName) {
		this.pcStreamName = pcStreamName;
	}

	public String getPhoneStreamName() {
		return phoneStreamName;
	}

	public void setPhoneStreamName(String phoneStreamName) {
		this.phoneStreamName = phoneStreamName;
	}

	public String getRelateSlimFileUrl() {
		return relateSlimFileUrl;
	}

	public void setRelateSlimFileUrl(String relateSlimFileUrl) {
		this.relateSlimFileUrl = relateSlimFileUrl;
	}

	public Integer getIsAssistant() {
		return isAssistant;
	}

	public void setIsAssistant(Integer isAssistant) {
		this.isAssistant = isAssistant;
	}

	public void setIsSystemGift(Integer isSystemGift) {
		this.isSystemGift = isSystemGift;
	}

	public Integer getIsUserGift() {
		return isUserGift;
	}

	public void setIsUserGift(Integer isUserGift) {
		this.isUserGift = isUserGift;
	}
	public BaseILiveLiveRoom() {
		super();
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(Integer openStatus) {
		this.openStatus = openStatus;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Double getAffordMoney() {
		return affordMoney;
	}

	public void setAffordMoney(Double affordMoney) {
		this.affordMoney = affordMoney;
	}

	public Long getLiveViewNum() {
		return liveViewNum;
	}

	public void setLiveViewNum(Long liveViewNum) {
		this.liveViewNum = liveViewNum;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public Integer getServerGroupId() {
		return serverGroupId;
	}

	public void setServerGroupId(Integer serverGroupId) {
		this.serverGroupId = serverGroupId;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public ILiveEvent getLiveEvent() {
		return liveEvent;
	}

	public void setLiveEvent(ILiveEvent liveEvent) {
		this.liveEvent = liveEvent;
	}

	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Boolean getMoneyLimitSwitch() {
		return moneyLimitSwitch;
	}

	public void setMoneyLimitSwitch(Boolean moneyLimitSwitch) {
		this.moneyLimitSwitch = moneyLimitSwitch;
	}

	public Double getAffordLimit() {
		return affordLimit;
	}

	public void setAffordLimit(Double affordLimit) {
		this.affordLimit = affordLimit;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public Long getRelateSlimFileId() {
		return relateSlimFileId;
	}

	public void setRelateSlimFileId(Long relateSlimFileId) {
		this.relateSlimFileId = relateSlimFileId;
	}

	public Integer getOpenSlimModel() {
		return openSlimModel;
	}

	public void setOpenSlimModel(Integer openSlimModel) {
		this.openSlimModel = openSlimModel;
	}

	public Integer getOpenSign() {
		return openSign;
	}

	public void setOpenSign(Integer openSign) {
		this.openSign = openSign;
	}

	public Integer getOpenNeedMobile() {
		return openNeedMobile;
	}

	public void setOpenNeedMobile(Integer openNeedMobile) {
		this.openNeedMobile = openNeedMobile;
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

	public String getRelateSlimFileLocalUrl() {
		return relateSlimFileLocalUrl;
	}

	public void setRelateSlimFileLocalUrl(String relateSlimFileLocalUrl) {
		this.relateSlimFileLocalUrl = relateSlimFileLocalUrl;
	}

	public Integer getIsNowInsert() {
		return isNowInsert;
	}

	public void setIsNowInsert(Integer isNowInsert) {
		this.isNowInsert = isNowInsert;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Integer getRelatedVideo() {
		return relatedVideo;
	}

	public void setRelatedVideo(Integer relatedVideo) {
		this.relatedVideo = relatedVideo;
	}
	
}