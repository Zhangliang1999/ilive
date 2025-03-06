package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveManagerStateDao;
import com.bvRadio.iLive.iLive.entity.ILiveManagerState;

public class ILiveManagerStateDaoImpl extends HibernateBaseDao<ILiveManagerState, Long>
		implements ILiveManagerStateDao {

	@Override
	public ILiveManagerState getIliveManagerState(Long managerId) {
		return this.get(managerId);
	}

	@Override
	public void saveIliveManagerState(ILiveManagerState managerState) {
		this.getSession().save(managerState);
	}

	@Override
	public void updateIliveManagerState(ILiveManagerState managerState) {
		this.getSession().update(managerState);
	}

	@Override
	protected Class<ILiveManagerState> getEntityClass() {
		return ILiveManagerState.class;
	}

}
