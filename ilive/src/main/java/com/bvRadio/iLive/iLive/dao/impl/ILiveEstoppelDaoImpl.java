package com.bvRadio.iLive.iLive.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
	public List<ILiveEstoppel> selectILiveEstoppels(Integer roomId) throws Exception {
		Finder finder = Finder.create("select bean from ILiveEstoppel bean where roomId=:roomId");
		finder.setParam("roomId", roomId);
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
		Finder finder = Finder.create("from ILiveEstoppel where roomId=:roomId");
		finder.setParam("roomId", roomId);
		String countHQL = "select count(userId) from ILiveEstoppel where roomId=" + roomId;
		return findByUnion(finder, cpn, pageSize, countHQL);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveEstoppel getILiveEstoppelYesOrNo(Integer roomId, Long userId) {
		Finder finder = Finder.create("select bean from ILiveEstoppel bean");
		finder.append(" where bean.roomId=:roomId");
		finder.setParam("roomId", roomId);
		finder.append(" and bean.userId=:userId");
		finder.setParam("userId", userId);
		List<ILiveEstoppel> find = find(finder);
		if(find==null){
			find = new ArrayList<ILiveEstoppel>();
		}
		if(find.size()>0){
			return find.get(0);
		}
		return null;
	}

	@Override
	public Pagination queryPager(String params, Integer roomId, int cpn, int pageSize) {
		Finder finder = Finder.create("from ILiveEstoppel where roomId=:roomId");
		finder.setParam("roomId", roomId);
		if(!StringUtils.isBlank(params)){
			finder.append(" and userId in(select userId from ILiveManager where userName like:userName  and isDel=0 )");
			finder.setParam("userName", "%"+params+"%" );
		}
		return find(finder, cpn, pageSize);
	}

}
