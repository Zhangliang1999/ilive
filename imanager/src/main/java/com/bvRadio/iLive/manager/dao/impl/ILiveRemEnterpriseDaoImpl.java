package com.bvRadio.iLive.manager.dao.impl;

import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.manager.dao.ILiveRemEnterpriseDao;
import com.bvRadio.iLive.manager.entity.ILiveRemEnterprise;

public class ILiveRemEnterpriseDaoImpl extends HibernateBaseDao<ILiveRemEnterprise, Long> implements ILiveRemEnterpriseDao {

	@Override
	public List<ILiveRemEnterprise> getPage(String name) {
		Finder finder = null;
		finder = Finder
				.create("select bean from ILiveRemEnterprise bean where 1= 1");
		if (name != null&&!"".equals(name)) {
			finder.append(" and bean.enterprise.enterpriseName like :name or bean.enterprise.userPhone like:name");
			finder.setParam("name", "%" + name + "%");
		}
		
		finder.append(" order by orderNum desc ");
		return find(finder);
	}

	@Override
	public void update(ILiveRemEnterprise iLiveRemEnterprise) {
		this.getSession().update(iLiveRemEnterprise);
	}

	@Override
	public void del(Long renEnterpriseId) {
		String hql = "delete from ILiveRemEnterprise where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", renEnterpriseId);
		query.executeUpdate();
	}

	@Override
	public void saveEnterprise(ILiveRemEnterprise iLiveRemEnterprise) {
		this.getSession().save(iLiveRemEnterprise);
	}
    @Override
    public ILiveRemEnterprise get(Long id) {
    	String hql = "from ILiveRemEnterprise where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		List<ILiveRemEnterprise> list = query.list();
		return list.get(0);
    }

	@Override
	protected Class<ILiveRemEnterprise> getEntityClass() {
		return ILiveRemEnterprise.class;
	}

	@Override
	public boolean getByEnterpriseId(Integer id) {
		String hql = "from ILiveRemEnterprise bean where bean.enterprise.enterpriseId = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		List<ILiveRemEnterprise> list = query.list();
		if(list==null||list.size()==0) {
			return true;
		}else {
			return false;
		}
	}
	@Override
	public ILiveRemEnterprise getIsEnterpriseRem(Integer id) {
		String hql = "from ILiveRemEnterprise bean where bean.enterprise.enterpriseId = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		List<ILiveRemEnterprise> list = query.list();
		if(list==null||list.size()==0) {
			return null;
		}else {
			return list.get(0);
			
		}
	}

	@Override
	public List<ILiveRemEnterprise> getprivacyPage(String name) {
		Finder finder = null;
		finder = Finder
				.create("select bean from ILiveRemEnterprise bean where 1= 1 and (bean.enterprise.privacy is null or bean.enterprise.privacy=0)");
		if (name != null&&!"".equals(name)) {
			finder.append(" and bean.enterprise.enterpriseName like :name or bean.enterprise.userPhone like:name");
			finder.setParam("name", "%" + name + "%");
		}
		
		finder.append(" order by orderNum desc ");
		return find(finder);
	}

}
