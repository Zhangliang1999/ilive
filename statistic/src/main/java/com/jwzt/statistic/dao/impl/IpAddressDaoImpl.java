package com.jwzt.statistic.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.IpAddressDao;
import com.jwzt.statistic.entity.IpAddress;

@Repository
public class IpAddressDaoImpl extends BaseHibernateDao<IpAddress, Long> implements IpAddressDao {
	@SuppressWarnings("unchecked")
	@Override
	public List<IpAddress> listNull() {
		Finder finder = Finder.create("select bean from IpAddress bean where 1=1");
		finder.append(" and country is null");
		return find(finder);
	}

	@Override
	public IpAddress getBeanByIpCode(Long ipCode) {
		IpAddress entity = null;
		if (null != ipCode) {
			entity = super.get(ipCode);
		}
		return entity;
	}

	@Override
	public IpAddress save(IpAddress bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public void update(IpAddress bean) {
		getSession().update(bean);
	}

	@Override
	protected Class<IpAddress> getEntityClass() {
		return IpAddress.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IpAddress> isNotSuccess() {
		Finder finder = Finder.create("select bean from IpAddress bean where 1=1");
		finder.append(" and isSuccess = 1");
		return find(finder);
	}

}