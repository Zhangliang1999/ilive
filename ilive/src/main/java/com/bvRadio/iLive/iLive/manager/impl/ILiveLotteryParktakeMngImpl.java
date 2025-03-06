package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveLotteryParktakeDao;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryParktake;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryParktakeMng;

@Service
@Transactional
public class ILiveLotteryParktakeMngImpl implements ILiveLotteryParktakeMng {

	@Autowired
	private ILiveLotteryParktakeDao iLiveLotteryParktakeDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public Pagination getPage(Integer isPrize, String search, Long lotteryId,Integer pageNo, Integer pageSize) {
		return iLiveLotteryParktakeDao.getPage(isPrize, search,lotteryId, pageNo,pageSize);
	}

	@Override
	public List<ILiveLotteryParktake> getListByLotteryIdAndPhone(Long lotteryId, String phone) {
		return iLiveLotteryParktakeDao.getListByLotteryIdAndPhone(lotteryId,phone);
	}

	@Override
	public Long saveUser(ILiveLotteryParktake iLiveLotteryParktake) {
		Long nextId = fieldIdMng.getNextId("ilive_lottery_parktake", "id", 1);
		iLiveLotteryParktake.setId(nextId);
		iLiveLotteryParktakeDao.saveUser(iLiveLotteryParktake);
		return nextId;
	}

	@Override
	public List<ILiveLotteryParktake> getList(Integer isPrize, String search, Long lotteryId) {
		return iLiveLotteryParktakeDao.getList(isPrize, search, lotteryId);
	}

}
