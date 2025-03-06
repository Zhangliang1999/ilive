package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILivePlayRewardsDao;
import com.bvRadio.iLive.iLive.entity.ILivePlayRewards;
@Repository
public class ILivePlayRewardsDaoImpl extends HibernateBaseDao<ILivePlayRewards, Integer> implements ILivePlayRewardsDao{

	@Override
	public ILivePlayRewards selectILivePlayRewardsById(Integer roomId)
			throws Exception {
		ILivePlayRewards iLivePlayRewards = get(roomId);
		return iLivePlayRewards;
	}

	@Override
	public void addILivePlayRewards(ILivePlayRewards iLivePlayRewards)
			throws Exception {
		getSession().save(iLivePlayRewards);
	}

	@Override
	protected Class<ILivePlayRewards> getEntityClass() {
		return ILivePlayRewards.class;
	}

	@Override
	public void updateILivePlayRewards(ILivePlayRewards iLivePlayRewards)
			throws Exception {
		getSession().update(iLivePlayRewards);
	}

}
