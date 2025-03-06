package com.jwzt.billing.entity;

import java.sql.Timestamp;
import java.util.List;

import com.jwzt.billing.entity.base.BaseSettlementLog;

@SuppressWarnings("serial")
public class SettlementLog extends BaseSettlementLog {
	/**
	 * 状态：已结算
	 */
	public final static Integer STATUS_SETTLED = 1;
	/**
	 * 状态：作废
	 */
	public final static Integer STATUS_CANCELED = 2;

	private List<RevenueFlow> revenueFlowList;

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public SettlementLog() {
		super();
	}

	public List<RevenueFlow> getRevenueFlowList() {
		return revenueFlowList;
	}

	public void setRevenueFlowList(List<RevenueFlow> revenueFlowList) {
		this.revenueFlowList = revenueFlowList;
	}

}
