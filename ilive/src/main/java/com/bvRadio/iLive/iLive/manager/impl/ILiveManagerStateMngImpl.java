package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveManagerStateDao;
import com.bvRadio.iLive.iLive.entity.ILiveManagerState;
import com.bvRadio.iLive.iLive.manager.ILiveManagerStateMng;

public class ILiveManagerStateMngImpl implements ILiveManagerStateMng {

	@Autowired
	private ILiveManagerStateDao iLiveManagerStateDao;

	@Override
	public ILiveManagerState getIliveManagerState(Long managerId) {
		return iLiveManagerStateDao.getIliveManagerState(managerId);
	}

	@Override
	@Transactional
	public void saveIliveManagerState(ILiveManagerState managerState) {
		iLiveManagerStateDao.saveIliveManagerState(managerState);
	}

	@Override
	@Transactional
	public void updateIliveManagerState(ILiveManagerState managerState) {
		iLiveManagerStateDao.updateIliveManagerState(managerState);
	}

}
