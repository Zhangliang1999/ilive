package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILivePlayRewards;

public interface ILivePlayRewardsDao {
	/**
	 * 获取数据
	 * @param roomId 直播间ID
	 * @return
	 * @throws Exception
	 */
	public ILivePlayRewards selectILivePlayRewardsById(Integer roomId) throws Exception;
	/**
	 * 添加数据
	 * @param iLivePlayRewards
	 */
	public void addILivePlayRewards(ILivePlayRewards iLivePlayRewards) throws Exception;
	/**
	 * 修改
	 * @param iLivePlayRewards
	 * @throws Exception
	 */
	public void updateILivePlayRewards(ILivePlayRewards iLivePlayRewards) throws Exception;

}
