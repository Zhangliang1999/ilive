package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILivePlayRewards;
/**
 * 打赏事务  
 * @author YanXL
 *
 */
public interface ILivePlayRewardsMng {
	/**
	 * 打赏数据查询
	 * @param roomId 直播间ID
	 * @return
	 * @throws Exception 
	 */
	public ILivePlayRewards selectILivePlayRewardsById(Integer roomId) throws Exception;
	/**
	 * 添加打赏设置数据
	 * @param iLivePlayRewards
	 * @throws Exception 
	 */
	public void addILivePlayRewards(ILivePlayRewards iLivePlayRewards) throws Exception;
	/**
	 * 修改打赏设置
	 * @param iLivePlayRewards
	 */
	public void updateILivePlayRewards(ILivePlayRewards iLivePlayRewards) throws Exception;

}
