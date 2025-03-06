package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseConvertDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseConvert;


@Repository
public class ILiveEnterpriseConvertDaoImpl extends HibernateBaseDao<ILiveEnterpriseConvert, Long> implements ILiveEnterpriseConvertDao {

	@SuppressWarnings("unchecked")
	@Override
	public ILiveEnterpriseConvert getConvertTemplet(Long enterpriseId) {
		// TODO Auto-generated method stub
		return this.get(enterpriseId);
	}

	@Override
	protected Class<ILiveEnterpriseConvert> getEntityClass() {
		// TODO Auto-generated method stub
		return ILiveEnterpriseConvert.class;
	}


}
