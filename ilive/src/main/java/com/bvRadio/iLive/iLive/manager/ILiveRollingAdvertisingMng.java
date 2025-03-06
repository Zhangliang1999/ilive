package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveRollingAdvertising;

/**
 * 滚动广告
 * @author YanXL
 *
 */
public interface ILiveRollingAdvertisingMng {
	/**
	 * 根据直播间ID获取数据
	 * @param roomId 
	 * @return
	 */
	public ILiveRollingAdvertising selectILiveRollingAdvertising(Integer roomId);
	/**
	 * 修改数据
	 * @param iLiveRollingAdvertising
	 * @return
	 */
	public ILiveRollingAdvertising updateILiveRollingAdvertising(ILiveRollingAdvertising iLiveRollingAdvertising);
}
