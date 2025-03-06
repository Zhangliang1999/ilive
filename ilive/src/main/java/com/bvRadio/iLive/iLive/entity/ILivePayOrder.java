package com.bvRadio.iLive.iLive.entity;

import com.bvRadio.iLive.iLive.entity.base.BaseILivePayOrder;

@SuppressWarnings("serial")
public class ILivePayOrder extends BaseILivePayOrder {
	/**
	 * 订单是否使用
	 */
	private Integer orderUp;
	public Integer getOrderUp() {
		return orderUp;
	}
	public void setOrderUp(Integer orderUp) {
		this.orderUp = orderUp;
	}
	/**
	 * 订单是否使用 0 未使用
	 */
	public static final Integer ORDER_UP_NO=0;
	/**
	 * 订单是否使用 1 使用
	 */
	public static final Integer ORDER_UP_YES=1;
	/**
	 * 支付状态 0 已支付  
	 */
	public static final Integer WX_PAY_STATUS_YES=0;
	/**
	 * 支付状态  1未支付
	 */
	public static final Integer WX_PAY_STATUS_NO=1;
	/**
	 * 订单类型  1 礼物
	 */
	public static final Integer ORDER_TYPE_GIFT=1;
	/**
	 * 订单类型  2 打赏
	 */
	public static final Integer ORDER_TYPE_PLAY_REWARDS=2;
	/**
	 * 订单类型 3 直播付费观看
	 */
	public static final Integer ORDER_TYPE_LIVE = 3;
	/**
	 * 订单类型 4 点播付费观看
	 */
	public static final Integer ORDER_TYPE_MEDIA = 4;
	
}
