package com.bvRadio.iLive.iLive.action.util;

import java.util.Hashtable;
import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILivePayOrder;

public class PayOrderUtil {
	/**
	 * 直播间ID
	 * 订单缓存
	 */
	private static Hashtable<Integer,List<ILivePayOrder>> orderListMap = new Hashtable<Integer,List<ILivePayOrder>>();

	public static Hashtable<Integer, List<ILivePayOrder>> getOrderListMap() {
		return orderListMap;
	}

}
