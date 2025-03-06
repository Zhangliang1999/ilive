package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveSubAccountDao;
import com.bvRadio.iLive.iLive.dao.ILiveSubAccountManagerDao;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubAccountManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;

@Repository
public class ILiveSubAccountManagerDaoImpl extends HibernateBaseDao<ILiveSubAccountManager, Integer> implements ILiveSubAccountManagerDao {

	@Override
	public Pagination selectSubAccountManagerPage(Integer pageNo, Integer pageSize,
			Integer enterpriseId) throws Exception {
		Finder finder = Finder.create("from ILiveSubAccountManager bean where bean.enterpriseId=:enterpriseId  order by bean.id Desc");
		finder.setParam("enterpriseId", enterpriseId);
		return find(finder, pageNo, pageSize);
	}

	@Override
	protected Class<ILiveSubAccountManager> getEntityClass() {
		return ILiveSubAccountManager.class;
	}

	@Override
	public void addILiveSubAccountMng(ILiveSubAccountManager manager) throws Exception {
		getSession().save(manager);
	}

	@Override
	public List<ILiveSubAccountManager> getILiveManagerPage(String userId) {
		String hql = "from ILiveSubAccountManager bean where bean.enterPrise.user="+userId;
		Query query=getSession().createQuery(hql);
		List<ILiveSubAccountManager> list= query.list();
		return  list;
	}

	@Override
	public void updateILiveSubAccountMng(ILiveSubAccountManager manager) throws Exception {
		getSession().update(manager);
		
	}
	@Override
	public Long selectMaxId() {
		String hql = "from ILiveSubAccountManager  order by id desc";
		Finder finder = Finder.create(hql);
		List<ILiveSubAccountManager> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			Long id=find.get(0).getId();
			return find.get(0).getId()+1;
		}
		return null;
	}

	@Override
	public ILiveSubAccountManager getILiveSubAccountManager(String user) {
		if (user == null || user.equals("")) {
			return null;
		}
		String hql = "from ILiveSubAccountManager where user=:mobile  ";
		Finder finder = Finder.create(hql);
		finder.setParam("mobile", user);
		List<ILiveSubAccountManager> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			return find.get(0);
		}
		return null;
	}
}
