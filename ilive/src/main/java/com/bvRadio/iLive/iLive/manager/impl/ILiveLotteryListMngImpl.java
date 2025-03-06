package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveLotteryListDao;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryList;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryListMng;

@Service
@Transactional
public class ILiveLotteryListMngImpl implements ILiveLotteryListMng {

	@Autowired
	private ILiveLotteryListDao iLiveLotteryListDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键

	@Override
	public void save(ILiveLotteryList iLiveLotteryList) {
		Long nextId = fieldIdMng.getNextId("ilive_lottery_list", "id", 1);
		iLiveLotteryList.setId(nextId);
		iLiveLotteryListDao.save(iLiveLotteryList);
	}

	@Override
	public List<ILiveLotteryList> getWhiteListByPrizeId(Long prizeId) {
		return iLiveLotteryListDao.getWhiteListByPrizeId(prizeId);
	}

	@Override
	public List<ILiveLotteryList> getBlackListByLotteryId(Long lotteryId) {
		return iLiveLotteryListDao.getBlackListByLotteryId(lotteryId);
	}

	@Override
	public boolean isWhiteList(Long prizeId, String phone) {
		return iLiveLotteryListDao.isWhiteList(prizeId,phone);
	}

	@Override
	public boolean isBlackList(Long lotteryId, String phone) {
		return iLiveLotteryListDao.isBlackList(lotteryId,phone);
	}
}
