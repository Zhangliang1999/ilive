package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveUserPlayRewardsDao;
import com.bvRadio.iLive.iLive.entity.ILiveUserPlayRewards;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveUserPlayRewardsMng;
@Service
public class ILiveUserPlayRewardsMngImpl implements ILiveUserPlayRewardsMng {
	
	@Autowired
	private ILiveUserPlayRewardsDao iLiveUserPlayRewardsDao;
	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;
	
	@Override
	@Transactional(readOnly=true)
	public Pagination selectILiveUserPlayRewardsByPage(String userContent,
			String roomContent, Integer roomId, Integer pageNo, Integer pageSize)
			throws Exception {
		Pagination pagination = iLiveUserPlayRewardsDao.selectILiveUserPlayRewardsByPage(userContent,roomContent,roomId,pageNo, pageSize);
		return pagination;
	}

	@Override
	@Transactional
	public void addILiveUserPlayRewards(
			ILiveUserPlayRewards iLiveUserPlayRewards) throws Exception {
		Long nextId = iLiveFieldIdManagerMng.getNextId("ilive_play_rewards_user", "id", 1);
		iLiveUserPlayRewards.setId(nextId);
		iLiveUserPlayRewardsDao.insertILiveUserPlayRewards(iLiveUserPlayRewards);
	}

	@Override
	public List<ILiveUserPlayRewards> selectILiveUserPlayRewardsByExcel(
			String userContent, String roomContent, Integer roomId) throws Exception {
		List<ILiveUserPlayRewards> list = iLiveUserPlayRewardsDao.selectILiveUserPlayRewardsByExcel(userContent,roomContent,roomId);
		return list;
	}

}
