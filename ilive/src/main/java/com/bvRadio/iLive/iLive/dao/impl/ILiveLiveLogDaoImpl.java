package com.bvRadio.iLive.iLive.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRoomDao;
import com.bvRadio.iLive.iLive.dao.ILiveLogDao;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveLog;

@Repository
public class ILiveLiveLogDaoImpl extends HibernateBaseDao<ILiveLog, Integer> implements ILiveLogDao {
	
	public ILiveLog findById(Integer id) {
		ILiveLog entity = get(id);
		return entity;
	}

	public ILiveLog save(ILiveLog bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(ILiveLog bean) {
		getSession().update(bean);
	}

	public ILiveLog deleteById(Integer id) {
		ILiveLog entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ILiveLog> getEntityClass() {
		return ILiveLog.class;
	}
	

	

}