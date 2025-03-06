package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveAPIGateWayRouterDao;
import com.bvRadio.iLive.iLive.entity.ILiveAPIGateWayRouter;

public class ILiveAPIGateWayRouterDaoImpl extends HibernateBaseDao<ILiveAPIGateWayRouter, Long>
		implements ILiveAPIGateWayRouterDao {

	@Override
	protected Class<ILiveAPIGateWayRouter> getEntityClass() {
		return ILiveAPIGateWayRouter.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveAPIGateWayRouter> getRouterList() {
		Finder create = Finder.create("from ILiveAPIGateWayRouter order by routerId asc");
		List<ILiveAPIGateWayRouter> find = this.find(create);
		return find;
	}

	@Override
	public ILiveAPIGateWayRouter getRouterById(Long routerId) {
		return this.get(routerId);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public ILiveAPIGateWayRouter get() {
		Finder create = Finder.create("from ILiveAPIGateWayRouter order by routerId asc");
		List<ILiveAPIGateWayRouter> list = this.find(create);
		if (list!=null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

}
