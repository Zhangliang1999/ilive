package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveRollingAdvertising;

/**
 * 滚动广告    DAO
 * @author YanXL
 *
 */
public interface ILiveRollingAdvertisingDao {
	/**
	 * 根据主键获取信息
	 * @param roomId 直播间ID
	 * @return
	 * @throws Exception
	 */
	public ILiveRollingAdvertising selectILiveRollingAdvertising(Integer roomId) throws Exception;
	/**
	 * 新增数据
	 * @param iLiveRollingAdvertising
	 * @throws Exception
	 */
	public void insertILiveRollingAdvertising(ILiveRollingAdvertising iLiveRollingAdvertising) throws Exception;
	/**
	 * 修改数据
	 * @param iLiveRollingAdvertising
	 * @throws Exception
	 */
	public void updateILiveRollingAdvertising(ILiveRollingAdvertising iLiveRollingAdvertising) throws Exception;
}
