package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveLotteryPrize;

public interface ILiveLotteryPrizeDao {

	public void save(ILiveLotteryPrize prize);
	
	public List<ILiveLotteryPrize> getListByLotteryId(Long lotteryId);
	
	public ILiveLotteryPrize getById(Long id);
	
	public void update(ILiveLotteryPrize iLiveLotteryPrize);
	
	public void deleteAllByLotteryId(Long lotteryId);
}
