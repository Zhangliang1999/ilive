package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * @author administrator
 * 直播工单
 */
public abstract class BaseILiveWorkOrder {
	
	/**
	 * 直播工单
	 */
	private Long orderId;
	
	
	/**
	 * 工单创建时间
	 */
	private Timestamp orderCreateTime;
	
	/**
	 * 工单状态
	 */
	private Integer orderStatus;
	
	
	/**
	 * 工单对应的直播场次
	 * @return
	 */
	private Integer liveEventId;
	
	public Long getOrderId() {
		return orderId;
	}


	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}


	public Timestamp getOrderCreateTime() {
		return orderCreateTime;
	}


	public void setOrderCreateTime(Timestamp orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}


	public Integer getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}


	public Integer getLiveEventId() {
		return liveEventId;
	}


	public void setLiveEventId(Integer liveEventId) {
		this.liveEventId = liveEventId;
	}
	
	
	

}
