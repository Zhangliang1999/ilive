package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.ILiveIpAddress;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;

/**
 * @author administrator 直播互动消息
 */
@SuppressWarnings("serial")
public abstract class BaseILiveMessage implements java.io.Serializable {

	/**
	 * 消息ID
	 */
	private Long msgId;

	/**
	 * 直播场次ID
	 */
	private ILiveEvent live;

	/**
	 * 直播间ID
	 */
	private Integer liveRoomId;

	/**
	 * 消息IP
	 */
	private ILiveIpAddress iLiveIpAddress;

	/**
	 * 发送人名称
	 */
	private String senderName;

	/**
	 * 发送人头像
	 */
	private String senderImage;

	/**
	 * 发送人级别
	 */
	private Integer senderLevel;

	/**
	 * 消息源正文
	 */
	private String msgOrginContent;

	/**
	 * 消息正文
	 */
	private String msgContent;

	/**
	 * 消息类型
	 */
	private Integer msgType;

	/**
	 * 创建时间
	 */
	private Timestamp createTime;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 删除标识
	 */
	private boolean deleted;

	/**
	 * 审核状态
	 */
	private boolean checked;

	/**
	 * 消息类型
	 */
	private Integer liveMsgType;

	/**
	 * 字体颜色
	 */
	private String fontColor;

	/**
	 * 
	 */
	private String IMEI;

	/**
	 * 
	 */
	private Integer width;

	/**
	 * 
	 */
	private Integer height;

	// 消息业务类型
	// 1为互动聊天室消息（原聊天业务不变）
	// 2为送礼物
	// 3 为连麦申请
	// 4 连麦同意
	// 5 观众进入房间
	// 6 观众离开房间
	// 7直播结束
	// 8 连麦结束
	private Integer serviceType;

	private String extra; // 为连麦等复杂消息类型封装信息

	/**
	 * 是否置顶 0 否 1 置顶
	 */
	private Integer top;
	/**
	 * 不存库 处理类型 10 禁言 11 解禁 12 关闭互动 13开启互动
	 */
	private Integer opType;
	/**
	 * 用户ID
	 */
	private Long senderId;
	/**
	 * 用户类型： 0 个人用户 1 管理用户
	 */
	private Integer senderType;
	/**
	 * 回答标示 0 回答 1 已回答
	 */
	private Integer replyType;
	/**
	 * 回答内容
	 */
	private String replyContent;
	/**
	 * 回答人名称
	 */
	private String replyName;
	/**
	 * 点赞数
	 */
	private Long praiseNumber;
	public BaseILiveMessage() {
		super();
	}

	public BaseILiveMessage(Long msgId, ILiveEvent live, ILiveIpAddress iLiveIpAddress) {
		super();
		this.msgId = msgId;
		this.live = live;
		this.iLiveIpAddress = iLiveIpAddress;
	}

	public BaseILiveMessage(Long msgId, ILiveEvent live, ILiveIpAddress iLiveIpAddress, String senderName,
			String msgOrginContent, String msgContent, Integer msgType, Timestamp createTime, Integer liveMsgType,
			String iMEI, Integer serviceType, String extra, Integer top, Integer opType, Long senderId,
			Integer senderType, Integer replyType, String replyContent,String replyName,Long praiseNumber) {
		super();
		this.msgId = msgId;
		this.live = live;
		this.iLiveIpAddress = iLiveIpAddress;
		this.senderName = senderName;
		this.msgOrginContent = msgOrginContent;
		this.msgContent = msgContent;
		this.msgType = msgType;
		this.createTime = createTime;
		this.liveMsgType = liveMsgType;
		this.IMEI = iMEI;
		this.serviceType = serviceType;
		this.extra = extra;
		this.top = top;
		this.opType = opType;
		this.senderId = senderId;
		this.senderType = senderType;
		this.replyType = replyType;
		this.replyContent = replyContent;
		this.replyName = replyName;
		this.praiseNumber = praiseNumber;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public ILiveEvent getLive() {
		return live;
	}

	public void setLive(ILiveEvent live) {
		this.live = live;
	}

	public ILiveIpAddress getiLiveIpAddress() {
		return iLiveIpAddress;
	}

	public void setiLiveIpAddress(ILiveIpAddress iLiveIpAddress) {
		this.iLiveIpAddress = iLiveIpAddress;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderImage() {
		return senderImage;
	}

	public void setSenderImage(String senderImage) {
		this.senderImage = senderImage;
	}

	public Integer getSenderLevel() {
		return senderLevel;
	}

	public void setSenderLevel(Integer senderLevel) {
		this.senderLevel = senderLevel;
	}

	public String getMsgOrginContent() {
		return msgOrginContent;
	}

	public void setMsgOrginContent(String msgOrginContent) {
		this.msgOrginContent = msgOrginContent;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Integer getLiveMsgType() {
		return liveMsgType;
	}

	public void setLiveMsgType(Integer liveMsgType) {
		this.liveMsgType = liveMsgType;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public Integer getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(Integer liveRoomId) {
		this.liveRoomId = liveRoomId;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getOpType() {
		return opType;
	}

	public void setOpType(Integer opType) {
		this.opType = opType;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Integer getSenderType() {
		return senderType;
	}

	public void setSenderType(Integer senderType) {
		this.senderType = senderType;
	}

	public Integer getReplyType() {
		return replyType;
	}

	public void setReplyType(Integer replyType) {
		this.replyType = replyType;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getReplyName() {
		return replyName;
	}

	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}

	public Long getPraiseNumber() {
		return praiseNumber;
	}

	public void setPraiseNumber(Long praiseNumber) {
		this.praiseNumber = praiseNumber;
	}

}