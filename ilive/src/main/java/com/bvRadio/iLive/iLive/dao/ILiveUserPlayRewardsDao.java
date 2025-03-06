package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveUserPlayRewards;

/**
 * 打赏   
 * @author YanXL
 *
 */
public interface ILiveUserPlayRewardsDao {
	/**
	 * 打赏分页数据
	 * @param userContent
	 * @param roomContent
	 * @param roomId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination selectILiveUserPlayRewardsByPage(String userContent,
			String roomContent, Integer roomId, Integer pageNo, Integer pageSize) throws Exception;
	/**
	 * 新增数据记录
	 * @param iLiveUserPlayRewards
	 * @throws Exception
	 */
	public void insertILiveUserPlayRewards(
			ILiveUserPlayRewards iLiveUserPlayRewards) throws Exception;
	/**
	 * 查询数据集合
	 * @param userContent
	 * @param roomContent
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public List<ILiveUserPlayRewards> selectILiveUserPlayRewardsByExcel(
			String userContent, String roomContent, Integer roomId) throws Exception;

}
