package com.jwzt.billing.entity;

import java.sql.Timestamp;

import com.jwzt.billing.entity.base.BaseSettlementLogAndRevenueFlow;

@SuppressWarnings("serial")
public class SettlementLogAndRevenueFlow extends BaseSettlementLogAndRevenueFlow {

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public SettlementLogAndRevenueFlow() {
		super();
	}

}
