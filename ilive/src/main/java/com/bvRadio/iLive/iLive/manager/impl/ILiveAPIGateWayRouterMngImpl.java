package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveAPIGateWayRouterDao;
import com.bvRadio.iLive.iLive.entity.ILiveAPIGateWayRouter;
import com.bvRadio.iLive.iLive.manager.ILiveAPIGateWayRouterMng;

@Transactional
public class ILiveAPIGateWayRouterMngImpl implements ILiveAPIGateWayRouterMng {

	@Autowired
	private ILiveAPIGateWayRouterDao iLiveAPIGateWayRouterDao;

	@Override
	public List<ILiveAPIGateWayRouter> getRouterList() {
		return iLiveAPIGateWayRouterDao.getRouterList();
	}

	@Override
	public ILiveAPIGateWayRouter getRouterById(Long routerId) {
		return iLiveAPIGateWayRouterDao.getRouterById(routerId);
	}

	@Override
	public ILiveAPIGateWayRouter get() {
		return iLiveAPIGateWayRouterDao.get();
	}

}
