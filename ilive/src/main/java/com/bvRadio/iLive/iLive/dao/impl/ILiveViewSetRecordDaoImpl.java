package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveViewSetRecordDao;
import com.bvRadio.iLive.iLive.entity.ILiveViewSetRecord;


@Repository
public class ILiveViewSetRecordDaoImpl extends HibernateBaseDao<ILiveViewSetRecord, Long> implements ILiveViewSetRecordDao {

	@Override
	public void saveRecord(ILiveViewSetRecord iLiveViewSetRecord) {
		this.getSession().save(iLiveViewSetRecord);
	}

	@Override
	protected Class<ILiveViewSetRecord> getEntityClass() {
		return ILiveViewSetRecord.class;
	}

}
