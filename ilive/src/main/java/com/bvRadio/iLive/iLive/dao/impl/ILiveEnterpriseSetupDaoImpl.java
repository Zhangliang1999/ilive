package com.bvRadio.iLive.iLive.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseSetupDao;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseSetup;

@Repository
public class ILiveEnterpriseSetupDaoImpl extends HibernateBaseDao<ILiveEnterpriseSetup, Long> implements ILiveEnterpriseSetupDao {

	

	@Override
	public void save(ILiveEnterpriseSetup ILiveEnterpriseSetup) {
		this.getSession().save(ILiveEnterpriseSetup);
	}

	@Override
	public void update(ILiveEnterpriseSetup ILiveEnterpriseSetupo) {
		this.getSession().update(ILiveEnterpriseSetupo);
	}

	@Override
	public ILiveEnterpriseSetup getById(Long id) {
		String hql = "from ILiveEnterpriseSetup where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		List<ILiveEnterpriseSetup> list = query.list();
		return list.get(0);
	}

	@Override
	protected Class<ILiveEnterpriseSetup> getEntityClass() {
		return ILiveEnterpriseSetup.class;
	}

	
	

	@Override
	public ILiveEnterpriseSetup getByEnterpriseId(Integer enterpriseId) {
		String hql = "from ILiveEnterpriseSetup where enterpriseId =:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveEnterpriseSetup> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}
	
	
}
