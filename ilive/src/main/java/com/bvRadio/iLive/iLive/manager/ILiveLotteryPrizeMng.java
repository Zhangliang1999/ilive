package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveLotteryPrize;

public interface ILiveLotteryPrizeMng {

	public Long save(ILiveLotteryPrize prize);
		
	public List<ILiveLotteryPrize> getListByLotteryId(Long lotteryId);
	
	public ILiveLotteryPrize getById(Long id);
	
	public void update(ILiveLotteryPrize iLiveLotteryPrize);
	
	void deleteAllByLotteryId(Long lotteryId);
}
