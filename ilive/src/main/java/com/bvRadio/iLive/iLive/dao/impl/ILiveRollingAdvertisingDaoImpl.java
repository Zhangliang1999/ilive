package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveRollingAdvertisingDao;
import com.bvRadio.iLive.iLive.entity.ILiveRollingAdvertising;
@Repository
public class ILiveRollingAdvertisingDaoImpl extends HibernateBaseDao<ILiveRollingAdvertising, Integer> implements
		ILiveRollingAdvertisingDao {

	@Override
	public ILiveRollingAdvertising selectILiveRollingAdvertising(Integer roomId)
			throws Exception {
		ILiveRollingAdvertising iLiveRollingAdvertising = get(roomId);
		return iLiveRollingAdvertising;
	}

	@Override
	public void insertILiveRollingAdvertising(
			ILiveRollingAdvertising iLiveRollingAdvertising) throws Exception {
		getSession().save(iLiveRollingAdvertising);
	}

	@Override
	protected Class<ILiveRollingAdvertising> getEntityClass() {
		return ILiveRollingAdvertising.class;
	}

	@Override
	public void updateILiveRollingAdvertising(
			ILiveRollingAdvertising iLiveRollingAdvertising) throws Exception {
		getSession().update(iLiveRollingAdvertising);
	}

}
