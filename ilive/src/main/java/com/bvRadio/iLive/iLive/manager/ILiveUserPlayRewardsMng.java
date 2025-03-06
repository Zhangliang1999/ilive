package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveUserPlayRewards;

public interface ILiveUserPlayRewardsMng {
	/**
	 * 获取打赏记录数据
	 * @param userContent
	 * @param roomContent
	 * @param roomId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Pagination selectILiveUserPlayRewardsByPage(String userContent,
			String roomContent, Integer roomId, Integer pageNo, Integer pageSize) throws Exception;
	/**
	 * 添加数据
	 * @param iLiveUserPlayRewards
	 * @throws Exception
	 */
	public void addILiveUserPlayRewards(ILiveUserPlayRewards iLiveUserPlayRewards) throws Exception;
	/**
	 * 获取数据集合
	 * @param userContent
	 * @param roomContent
	 * @param roomId
	 * @return
	 * @throws Exception 
	 */
	public List<ILiveUserPlayRewards> selectILiveUserPlayRewardsByExcel(
			String userContent, String roomContent, Integer roomId) throws Exception;

}
