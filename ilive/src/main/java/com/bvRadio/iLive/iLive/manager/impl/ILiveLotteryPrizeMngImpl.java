package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveLotteryPrizeDao;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryPrize;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryPrizeMng;

@Service
@Transactional
public class ILiveLotteryPrizeMngImpl implements ILiveLotteryPrizeMng{

	@Autowired
	private ILiveLotteryPrizeDao iLiveLotteryPrizeDao;	//抽奖奖品
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键

	@Override
	public Long save(ILiveLotteryPrize prize) {
		Long id = fieldIdMng.getNextId("ilive_lottery_prize", "id", 1);
		prize.setId(id);
		iLiveLotteryPrizeDao.save(prize);
		return id;
	}

	@Override
	public List<ILiveLotteryPrize> getListByLotteryId(Long lotteryId) {
		return iLiveLotteryPrizeDao.getListByLotteryId(lotteryId);
	}

	@Override
	@Transactional
	public ILiveLotteryPrize getById(Long id) {
		return iLiveLotteryPrizeDao.getById(id);
	}

	@Override
	public void update(ILiveLotteryPrize iLiveLotteryPrize) {
		iLiveLotteryPrizeDao.update(iLiveLotteryPrize);
	}

	@Override
	public void deleteAllByLotteryId(Long lotteryId) {
		iLiveLotteryPrizeDao.deleteAllByLotteryId(lotteryId);
	}
	
	
}
