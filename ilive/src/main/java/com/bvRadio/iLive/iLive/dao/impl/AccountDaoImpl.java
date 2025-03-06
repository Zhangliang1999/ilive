package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.AccountDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

@Repository
public class AccountDaoImpl extends HibernateBaseDao<ILiveEnterprise,Integer> implements AccountDao {

	@Override
	public void saveaccount(ILiveEnterprise iLiveEnterprise) {
		ILiveEnterprise enterprise = this.get(iLiveEnterprise.getEnterpriseId());
		enterprise.setEnterpriseName(iLiveEnterprise.getEnterpriseName());
		enterprise.setEnterpriseImg(iLiveEnterprise.getEnterpriseImg());
		enterprise.setEnterpriseType(iLiveEnterprise.getEnterpriseType());
		enterprise.setEnterprisePhone(iLiveEnterprise.getEnterprisePhone());
		enterprise.setRoad(iLiveEnterprise.getRoad());
		enterprise.setEnterpriseEmail(iLiveEnterprise.getEnterpriseEmail());
		enterprise.setEnterpriseDesc(iLiveEnterprise.getEnterpriseDesc().trim());
		enterprise.setProvince(iLiveEnterprise.getProvince());
		enterprise.setCity(iLiveEnterprise.getCity());
		enterprise.setArea(iLiveEnterprise.getArea());
		this.getSession().update(enterprise);
	}

	@Override
	protected Class<ILiveEnterprise> getEntityClass() {
		return ILiveEnterprise.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveEnterprise getEnterpriseById(Integer enterpriseId) {
		String hql = "from ILiveEnterprise where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveEnterprise> list = query.list();
		return list.get(0);
	}

}
