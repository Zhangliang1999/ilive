package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;

/**
 * 直播鉴权管理类
 * 
 * @author administrator
 *
 */
public interface ILiveViewAuthBillDao {

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
