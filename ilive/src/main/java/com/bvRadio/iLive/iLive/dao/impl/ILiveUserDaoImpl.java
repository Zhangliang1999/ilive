package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveUserDao;
import com.bvRadio.iLive.iLive.entity.ILiveUserEntity;


@Repository
public class ILiveUserDaoImpl extends HibernateBaseDao<ILiveUserEntity, Integer> implements ILiveUserDao {

	@Override
	public ILiveUserEntity findById(Integer uid) {
		return super.get(uid);
	}

	@Override
	public void deleteById(Integer id) {
		ILiveUserEntity findById = this.findById(id);
		if(findById!=null) {
			getSession().delete(findById);
		}
	}

	@Override
	public void save(ILiveUserEntity user) {
		// TODO Auto-generated method stub
		getSession().save(user);
	}

	@Override
	protected Class<ILiveUserEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return ILiveUserEntity.class;
	}

	@Override
	public void updateUser(ILiveUserEntity liveUser) {
		// TODO Auto-generated method stub
		getSession().update(liveUser);
	}

}
