package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;

public interface ILiveViewAuthBillMng {

	/**
	 * 新增直播鉴权关系
	 * 
	 * @param iLiveViewAuthBill
	 */
	public void addILiveViewAuthBill(ILiveViewAuthBill iLiveViewAuthBill);

	/**
	 * 修改直播鉴权关系
	 */
	public void updateILiveViewAuthBill(ILiveViewAuthBill iLiveViewAuthBill);

	/**
	 * 校验直播鉴权关系
	 */
	public ILiveViewAuthBill checkILiveViewAuthBill(String userId, Long liveEventId);

}
