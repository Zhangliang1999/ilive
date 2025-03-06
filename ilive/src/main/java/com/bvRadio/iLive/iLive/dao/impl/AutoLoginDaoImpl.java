package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.AutoLoginDao;
import com.bvRadio.iLive.iLive.entity.AutoLogin;

@Repository
public class AutoLoginDaoImpl extends HibernateBaseDao<AutoLogin, String> implements AutoLoginDao{

	@Override
	public void save(AutoLogin autoLogin) {
		this.getSession().save(autoLogin);
	}

	@Override
	public void update(AutoLogin autoLogin) {
		this.getSession().update(autoLogin);
	}

	@Override
	public AutoLogin getById(String id) {
		return this.get(id);
	}

	@Override
	protected Class<AutoLogin> getEntityClass() {
		return AutoLogin.class;
	}

	@Override
	public AutoLogin getByAppId(String appId) {
		Finder finder = Finder.create("from AutoLogin where appId = :appId");
		finder.setParam("appId", appId);
		@SuppressWarnings("unchecked")
		List<AutoLogin> find = find(finder);
		if (find!=null&&find.size()>0) {
			return find.get(0);
		}
		return null;
	}

}
