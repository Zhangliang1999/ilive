package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.AccountDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

@Repository
public class AccountDaoImpl extends HibernateBaseDao<ILiveEnterprise,Long> implements AccountDao {

	@Override
	public void saveaccount(ILiveEnterprise iLiveEnterprise) {
		StringBuilder sb = new StringBuilder("update ILiveEnterprise set isDel = 1");
		if(iLiveEnterprise.getEnterpriseName()!=null) {
			sb.append(",enterpriseName = '"+iLiveEnterprise.getEnterpriseName()+"'");
		}
		if(iLiveEnterprise.getEnterpriseType()!=null) {
			sb.append(",enterpriseType = '"+iLiveEnterprise.getEnterpriseType()+"'");
		}
		if(iLiveEnterprise.getEnterpriseRegName()!=null) {
			sb.append(",enterpriseRegName = '"+iLiveEnterprise.getEnterpriseRegName()+"'");
		}
		if(iLiveEnterprise.getEnterpriseRegNo()!=null) {
			sb.append(",enterpriseRegNo = '"+iLiveEnterprise.getEnterpriseRegNo()+"'");
		}
		if(iLiveEnterprise.getEnterprisePhone()!=null) {
			sb.append(",enterprisePhone = '"+iLiveEnterprise.getEnterprisePhone()+"'");
		}
		if(iLiveEnterprise.getEnterpriseEmail()!=null) {
			sb.append(",enterpriseEmail = '"+iLiveEnterprise.getEnterpriseEmail()+"'");
		}
		sb.append(" where enterpriseId = "+iLiveEnterprise.getEnterpriseId());
		Query query = this.getSession().createQuery(sb.toString());
		query.executeUpdate();
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
