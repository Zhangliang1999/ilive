package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.iLive.dao.ILiveIpAddressDao;
import com.bvRadio.iLive.iLive.entity.ILiveIpAddress;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;

@Repository
public class ILiveIpAddressDaoImpl extends HibernateBaseDao<ILiveIpAddress, Integer> implements ILiveIpAddressDao {

	public ILiveIpAddress findByIpCode(Long ipCode) {
		ILiveIpAddress entity = null;
		if (null != ipCode) {
			entity = findUniqueByProperty("ipCode", ipCode);
		}
		return entity;
	}

	public ILiveIpAddress save(ILiveIpAddress bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(ILiveIpAddress bean) {
		getSession().update(bean);
	}

	@Override
	protected Class<ILiveIpAddress> getEntityClass() {
		return ILiveIpAddress.class;
	}

}