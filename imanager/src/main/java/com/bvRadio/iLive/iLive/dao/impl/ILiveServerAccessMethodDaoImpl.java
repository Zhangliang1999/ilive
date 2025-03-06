package com.bvRadio.iLive.iLive.dao.impl;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveServerAccessMethodDao;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;

public class ILiveServerAccessMethodDaoImpl extends HibernateBaseDao<ILiveServerAccessMethod, Integer>
		implements ILiveServerAccessMethodDao {

	@Override
	public ILiveServerAccessMethod getMethodByServerId(Integer serverId) {
		Query query = this.getSession().createQuery("from ILiveServerAccessMethod where iliveServerId=:server_id")
				.setParameter("server_id", serverId);
		return (ILiveServerAccessMethod) query.uniqueResult();
	}

	@Override
	protected Class<ILiveServerAccessMethod> getEntityClass() {
		return ILiveServerAccessMethod.class;
	}

}
