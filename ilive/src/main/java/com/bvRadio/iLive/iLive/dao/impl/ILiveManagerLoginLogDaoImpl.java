package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveManagerLoginLogDao;
import com.bvRadio.iLive.iLive.entity.ILiveManagerLoginLog;

/**
 * 
 * @author administrator
 *
 */
public class ILiveManagerLoginLogDaoImpl extends HibernateBaseDao<ILiveManagerLoginLog, Long>
		implements ILiveManagerLoginLogDao {

	@Override
	protected Class<ILiveManagerLoginLog> getEntityClass() {
		return ILiveManagerLoginLog.class;
	}

	@Override
	public void addLoginLog(ILiveManagerLoginLog loginLog) {
		this.getSession().save(loginLog);
	}

}
