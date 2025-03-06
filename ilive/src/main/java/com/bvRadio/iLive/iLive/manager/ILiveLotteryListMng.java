package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveLotteryList;

public interface ILiveLotteryListMng {

	public void save(ILiveLotteryList iLiveLotteryList);
	
	/**
	 * 根据奖品id获取白名单
	 */
	public List<ILiveLotteryList> getWhiteListByPrizeId(Long prizeId);
	
	/**
	 * 根据抽奖活动id获取黑名单
	 * @param lotteryId
	 * @return
	 */
	public List<ILiveLotteryList> getBlackListByLotteryId(Long lotteryId);
	
	/**
	 * 该手机号是否在该奖品的白名单中
	 * @param prizeId
	 * @param phone
	 * @return
	 */
	public boolean isWhiteList(Long prizeId,String phone);
	
	/**
	 * 该手机号是否在该次活动的黑名单中
	 * @param lotteryId
	 * @param phone
	 * @return
	 */
	public boolean isBlackList(Long lotteryId,String phone);
}
