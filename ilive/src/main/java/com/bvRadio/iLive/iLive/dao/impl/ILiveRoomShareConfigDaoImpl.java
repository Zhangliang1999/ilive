package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveRoomShareConfigDao;
import com.bvRadio.iLive.iLive.entity.ILiveRoomShareConfig;

public class ILiveRoomShareConfigDaoImpl extends HibernateBaseDao<ILiveRoomShareConfig, Long>
		implements ILiveRoomShareConfigDao {

	@Override
	protected Class<ILiveRoomShareConfig> getEntityClass() {
		return ILiveRoomShareConfig.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ILiveRoomShareConfig> getShareConfig(Integer roomId) {
		return this.find("from ILiveRoomShareConfig where roomId=?", roomId);
	}

	@Override
	public void addIliveRoomShareConfig(ILiveRoomShareConfig shareConfig) {
		shareConfig.setOpenStatus(0);
		this.getSession().save(shareConfig);
	}

	/**
	 * 
	 */
	@Override
	public void updateConfigDao(ILiveRoomShareConfig configShare) {
		this.getSession().update(configShare);
	}

	/**
	 * 
	 */
	@Override
	public ILiveRoomShareConfig getConfigShare(Long circleId) {
		return this.get(circleId);
	}

}
