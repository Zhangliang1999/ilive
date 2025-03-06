package com.jwzt.billing.entity.base;

import java.sql.Timestamp;

/**
 * @author ysf
 */

@SuppressWarnings("serial")
public class BaseSettlementLogAndRevenueFlow implements java.io.Serializable {

	private String id;
	private Long settlementLogId;
	private Long revenueFlowId;
	private Timestamp createTime;

	public BaseSettlementLogAndRevenueFlow() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getSettlementLogId() {
		return settlementLogId;
	}

	public void setSettlementLogId(Long settlementLogId) {
		this.settlementLogId = settlementLogId;
	}

	public Long getRevenueFlowId() {
		return revenueFlowId;
	}

	public void setRevenueFlowId(Long revenueFlowId) {
		this.revenueFlowId = revenueFlowId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
