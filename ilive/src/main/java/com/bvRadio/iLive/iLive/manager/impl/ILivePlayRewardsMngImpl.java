package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILivePlayRewardsDao;
import com.bvRadio.iLive.iLive.entity.ILivePlayRewards;
import com.bvRadio.iLive.iLive.manager.ILivePlayRewardsMng;
@Service
public class ILivePlayRewardsMngImpl implements ILivePlayRewardsMng {
	@Autowired
	private ILivePlayRewardsDao iLivePlayRewardsMng;
	@Override
	@Transactional(readOnly=true)
	public ILivePlayRewards selectILivePlayRewardsById(Integer roomId) throws Exception {
		ILivePlayRewards iLivePlayRewards = iLivePlayRewardsMng.selectILivePlayRewardsById(roomId);
		return iLivePlayRewards;
	}

	@Override
	@Transactional
	public void addILivePlayRewards(ILivePlayRewards iLivePlayRewards) throws Exception {
		iLivePlayRewardsMng.addILivePlayRewards(iLivePlayRewards);
	}

	@Override
	@Transactional
	public void updateILivePlayRewards(ILivePlayRewards iLivePlayRewards)
			throws Exception {
		iLivePlayRewardsMng.updateILivePlayRewards(iLivePlayRewards);
	}

}
