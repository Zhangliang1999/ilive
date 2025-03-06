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
	 * 禁用标识  0正常  1禁用
	 */
	private Integer disabled;
	
	/**
	 * 显示标识，不需要入库
	 * 房间状态标识  0关闭  1开启  2删除 3禁用
	 */
	private Integer roomState;
	

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
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 直播间对应的服务器组ID 可做软负载
	 */
	private Integer serverGroupId;

	/**
	 * 删除状态
	 */
	private Integer deleteStatus;

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

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Integer getRoomState() {
		return roomState;
	}

	public void setRoomState(Integer roomState) {
		this.roomState = roomState;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	
}