package com.bvRadio.iLive.iLive.entity.base;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 订单
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public class BaseILivePayOrder implements Serializable{
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 商户订单号
	 */
	private String orderNumber;
	/**
	 * 订单创建时间
	 */
	private Timestamp createTime;
	/**
	 * 第三方支付订单号
	 * 订单号，视讯平台唯一订单号
	 */
	private String tradeNo;
	/**
	 * 金额 ： 分
	 */
	private Long amount;
	/**
	 * 支付状态 0 已支付  1未支付  2 待处理订单
	 */
	private Integer payStatus;
	/**
	 * 订单描述
	 */
	private String payDesc;
	/**
	 * 订单支付时间
	 */
	private Timestamp endTime;
	/**
	 * 订单类型  1 礼物  2 打赏
	 */
	private Integer orderType;
	/**
	 * 直播间ID
	 */
	private Integer roomId;
	/**
	 * 直播场次ID
	 */
	private Long evenId;
	/**
	 * 内容ID  礼物ID 或 0
	 */
	private Long contentId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public BaseILivePayOrder() {
		super();
	}
	public String getPayDesc() {
		return payDesc;
	}
	public void setPayDesc(String payDesc) {
		this.payDesc = payDesc;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Long getEvenId() {
		return evenId;
	}
	public void setEvenId(Long evenId) {
		this.evenId = evenId;
	}
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	
}
