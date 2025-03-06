package com.jwzt.billing.entity.base;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseRevenueFlow implements Serializable{
	/**
	 * id
	 */
	private Long flowId;
	/**
	 * 时间
	 */
	private Timestamp flowTime;
	/**
	 * 金额
	 */
	private Double flowPrice;
	/**
	 * 余额
	 */
	private Double flowBalance;
	/**
	 * 收益类型
	 */
	private Integer flowType;
	/**
	 * 类型对应的内容id
	 */
	private Long flagId;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 企业ID
	 */
	private Integer enterpriseId;
	/**
	 * 企业名
	 */
	private String enterpriseName;
	/**
	 * 直播房间ID
	 */
	private Integer roomId;
	/**
	 * 直播场次ID
	 */
	private Long evenId;
	/**
	 * 平台费
	 */
	private String platformPrice;
	/**
	 * 收益事项
	 */
	private String flowDesc;
	/**
	 * 结算状态
	 */
	private Integer settlementStatus;
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	public Timestamp getFlowTime() {
		return flowTime;
	}
	public void setFlowTime(Timestamp flowTime) {
		this.flowTime = flowTime;
	}
	public Double getFlowPrice() {
		return flowPrice;
	}
	public void setFlowPrice(Double flowPrice) {
		this.flowPrice = flowPrice;
	}
	public Double getFlowBalance() {
		return flowBalance;
	}
	public void setFlowBalance(Double flowBalance) {
		this.flowBalance = flowBalance;
	}
	public Integer getFlowType() {
		return flowType;
	}
	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
	}
	public Long getFlagId() {
		return flagId;
	}
	public void setFlagId(Long flagId) {
		this.flagId = flagId;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
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
	public String getPlatformPrice() {
		return platformPrice;
	}
	public void setPlatformPrice(String platformPrice) {
		this.platformPrice = platformPrice;
	}
	public String getFlowDesc() {
		return flowDesc;
	}
	public void setFlowDesc(String flowDesc) {
		this.flowDesc = flowDesc;
	}
	public Integer getSettlementStatus() {
		return settlementStatus;
	}
	public void setSettlementStatus(Integer settlementStatus) {
		this.settlementStatus = settlementStatus;
	}
	public BaseRevenueFlow() {
		super();
	}
	
}
