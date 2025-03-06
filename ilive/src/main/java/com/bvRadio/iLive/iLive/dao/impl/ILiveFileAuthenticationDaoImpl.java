package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveFileAuthenticationDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthentication;

public class ILiveFileAuthenticationDaoImpl extends HibernateBaseDao<ILiveFileAuthentication, Long>
		implements ILiveFileAuthenticationDao {

	@Override
	protected Class<ILiveFileAuthentication> getEntityClass() {
		return ILiveFileAuthentication.class;
	}

	@Override
	public ILiveFileAuthentication getFileAuthenticationByFileId(Long fileId) {
		return this.get(fileId);
	}

	@Override
	public void addFileAuth(ILiveFileAuthentication fileAuthentication) {
		this.getSession().save(fileAuthentication);
	}

	@Override
	public void updateFileAuth(ILiveFileAuthentication fileAuthentication) {
		this.getSession().update(fileAuthentication);
	}

}
