package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveServerDao;
import com.bvRadio.iLive.iLive.entity.ILiveServer;

public class ILiveServerDaoImpl extends HibernateBaseDao<ILiveServer, Integer>  implements ILiveServerDao {

	@Override
	public ILiveServer getILiveServer(Integer serverId) {
		
		return this.get(serverId);
	}

	@Override
	protected Class<ILiveServer> getEntityClass() {
		return ILiveServer.class;
	}

}
