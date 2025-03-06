package com.jwzt.billing.entity;

import com.jwzt.billing.entity.base.BaseRevenueFlow;


@SuppressWarnings("serial")
public class RevenueFlow extends BaseRevenueFlow{
	/**
	 * 收益类型 1 礼物
	 */
	public static final Integer FLOW_TYPE_GIFT=1;
	/**
	 * 收益类型 2 打赏
	 */
	public static final Integer FLOW_TYPE_PLAY_REWARDS=2;
	/**
	 * 收益类型 3 付费观看
	 */
	public static final Integer FLOW_TYPE_PAY_WATCH=3;
	
	/**
	 * 结算状态   1 未结算  settlementStatus
	 */
	public static final Integer SETTLEMENT_STATUS_NO=1;
	/**
	 * 结算状态   2 已结算
	 */
	public static final Integer SETTLEMENT_STATUS_YES=2;
	
}
