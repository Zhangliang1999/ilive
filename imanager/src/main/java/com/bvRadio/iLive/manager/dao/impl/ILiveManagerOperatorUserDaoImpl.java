package com.bvRadio.iLive.manager.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.manager.dao.ILiveManagerOperatorUserDao;
import com.bvRadio.iLive.manager.entity.IliveOperationUser;
@Repository
public class ILiveManagerOperatorUserDaoImpl extends HibernateBaseDao<IliveOperationUser, Long>
		implements ILiveManagerOperatorUserDao {

	@Override
	public IliveOperationUser getUserByUserName(String principal) {
		return this.findUniqueByProperty("userName", principal);
	}

	@Override
	protected Class<IliveOperationUser> getEntityClass() {
		return IliveOperationUser.class;
	}

	@Override
	public IliveOperationUser getUserById(Long userId) {
		return this.get(userId);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<IliveOperationUser> listByParams(){
		Finder finder = Finder.create("select bean from IliveOperationUser bean where 1=1");
		return find(finder);
	}

}
