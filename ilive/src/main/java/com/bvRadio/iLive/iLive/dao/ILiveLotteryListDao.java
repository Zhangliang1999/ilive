package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveLotteryList;

public interface ILiveLotteryListDao {

	public void save(ILiveLotteryList iliveLotteryList);
	
	public List<ILiveLotteryList> getWhiteListByPrizeId(Long prizeId);
	
	public List<ILiveLotteryList> getBlackListByLotteryId(Long lotteryId);
	
	public boolean isWhiteList(Long prizeId, String phone);
	
	public boolean isBlackList(Long lotteryId, String phone);
}
