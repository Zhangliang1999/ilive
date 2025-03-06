package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.UserRoomRelationDao;
import com.bvRadio.iLive.iLive.entity.UserRoomRelation;
@Repository
public class UserRoomRelationDaoImpl extends HibernateBaseDao<UserRoomRelation, Integer> implements UserRoomRelationDao {
	@Override
	protected Class<UserRoomRelation> getEntityClass() {
		return UserRoomRelation.class;
	}

	@Override
	public UserRoomRelation findById(Integer id) {
		UserRoomRelation entity = get(id);
		return entity;
	}

	@Override
	public UserRoomRelation save(UserRoomRelation bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public UserRoomRelation update(UserRoomRelation bean) {
		getSession().update(bean);
		return bean;
	}

	@Override
	public UserRoomRelation deleteById(Integer id) {
		UserRoomRelation entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRoomRelation> findByUid(Integer uid) {
		Finder f = Finder.create("select bean from UserRoomRelation bean");
		f.append(" where 1=1");
		if (null!=uid) {
			f.append(" and bean.uid =:uid");
			f.setParam("uid", uid);
		}
		
		return find(f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRoomRelation> findBeanAll() {
		Finder f = Finder.create("select bean from UserRoomRelation bean");
		return find(f);
	}

}
