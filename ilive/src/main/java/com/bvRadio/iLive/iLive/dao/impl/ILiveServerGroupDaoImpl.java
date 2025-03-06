package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveServerGroupDao;
import com.bvRadio.iLive.iLive.entity.ILiveServerGroup;

public class ILiveServerGroupDaoImpl extends HibernateBaseDao<ILiveServerGroup, Integer>
		implements ILiveServerGroupDao {

	@Override
	public ILiveServerGroup getILiveServerGroupById(Integer groupId) {
		return this.get(groupId);
	}

	@Override
	protected Class<ILiveServerGroup> getEntityClass() {
		return ILiveServerGroup.class;
	}

}
