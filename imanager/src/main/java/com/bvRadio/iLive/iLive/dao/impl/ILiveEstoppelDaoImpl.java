package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEstoppelDao;
import com.bvRadio.iLive.iLive.entity.ILiveEstoppel;

@Repository
public class ILiveEstoppelDaoImpl extends HibernateBaseDao<ILiveEstoppel, Integer> implements ILiveEstoppelDao {

	@Override
	protected Class<ILiveEstoppel> getEntityClass() {
		return ILiveEstoppel.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEstoppel> selectILiveEstoppels() throws Exception {
		Finder finder = Finder.create("select bean from ILiveEstoppel bean");
		return find(finder);
	}

	@Override
	public void insertILiveEstoppel(ILiveEstoppel iLiveEstoppel) throws Exception {
		getSession().save(iLiveEstoppel);
	}

	@Override
	public void deleteILiveEstoppel(ILiveEstoppel iLiveEstoppel) throws Exception {
		getSession().delete(iLiveEstoppel);
	}

	@Override
	public Pagination getPager(Integer roomId, int cpn, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveEstoppel where roomId=:roomId");
		finder.setParam("roomId", roomId);
		String countHQL = "select count(userId) from ILiveEstoppel where roomId=" + roomId;
		return findByUnion(finder, cpn, pageSize, countHQL);
	}

}
