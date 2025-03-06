package com.bvRadio.iLive.iLive.manager;

import java.util.HashMap;
import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;

public interface ILivePrizeMng {

	public Pagination getPage(String prizeName,Integer roomId,Integer pageNo,Integer pageSize);
	
	public Pagination getPageByEnterpriseId(String prizeName,Integer enterpriseId,Integer pageNo,Integer pageSize);
	
	public Long createPrizeContent(ILiveLottery lottery);
	
	public ILiveLottery getPrize(Long id);
	
	public void update(ILiveLottery lottery);
	
	public void closeOrStart(Long id,Integer isSwitch);
	
	/**
	 * 获取该直播间所有列表
	 * @param roomId
	 * @return
	 */
	public List<ILiveLottery> getListByRoomId(Integer roomId);
	
	/**
	 * 查询当前开启和还没有开启过得抽奖活动
	 * @param lottery
	 * @return
	 */
	public List<ILiveLottery> getListUserH5ByRoomId(Integer roomId);
	public List<ILiveLottery> getListUserH5ByEnterpriseId(Integer enterpriseId);
	public List<ILiveLottery> getListUserH5ByUserId(Long userId);
	Pagination getpageByUserId(Long userId,String prizeName,Integer pageNo,Integer pageSize);
	
	/**
	 * 获取开始的抽奖活动
	 * @param roomId
	 * @return
	 */
	public ILiveLottery isStartPrize(Integer roomId);
	public ILiveLottery isStartPrize2(Integer enterpriseId);
	
	public ILiveLottery isEnterpriseStartPrize(Integer enterpriseId);
	
	
	/**
	 * 抽奖
	 * @param userId
	 * @param lotteryId
	 * @return
	 */
	public Long lottery(Long userId,Long lotteryId);
	
	/**
	 * 创建或更新一个抽奖内容  包括抽奖活动内容、奖品信息、白名单信息
	 * @param lottery
	 * @param lotteryPrize
	 * @return
	 */
	public boolean createOrUpdateLotteryPrize(ILiveLottery lottery,String lotteryPrize,HashMap<String, String> hashMap);
	
}
