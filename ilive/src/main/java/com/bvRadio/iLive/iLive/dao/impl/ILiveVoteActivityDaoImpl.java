package com.bvRadio.iLive.iLive.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveVoteActivityDao;
import com.bvRadio.iLive.iLive.entity.ILiveVoteActivity;

public class ILiveVoteActivityDaoImpl extends HibernateBaseDao<ILiveVoteActivity, Long> implements ILiveVoteActivityDao {

	@Override
	public Pagination getPage(Integer roomId,String name, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create(" from ILiveVoteActivity where roomId = :roomId");
		finder.setParam("roomId", roomId);
		if(name!=null && !"".equals(name)) {
			finder.append(" and voteName like :voteName");
			finder.setParam("voteName", "%"+name+"%");
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}
	
	@Override
	public Pagination getPageByEnterpriseId(Integer enterpriseId,String name, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create(" from ILiveVoteActivity where enterpriseId = :enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		if(name!=null && !"".equals(name)) {
			finder.append(" and voteName like :voteName");
			finder.setParam("voteName", "%"+name+"%");
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	protected Class<ILiveVoteActivity> getEntityClass() {
		return ILiveVoteActivity.class;
	}

	@Override
	public ILiveVoteActivity getById(Long voteId) {
		return this.get(voteId);
	}

	@Override
	public void save(ILiveVoteActivity voteActivity) {
		this.getSession().save(voteActivity);
	}

	@Override
	public void update(ILiveVoteActivity voteActivity) {
		this.getSession().update(voteActivity);
	}

	@Override
	public ILiveVoteActivity getActivityByRoomId(Integer roomId) {
		//String hql = "from ILiveVoteActivity where roomId = :roomId and isSwitch = 1";
		String hql = "from ILiveVoteActivity where roomId = :roomId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		ILiveVoteActivity activity = (ILiveVoteActivity) query.uniqueResult();
		return activity;
	}
	@Override
	public ILiveVoteActivity getActivityByEnterpriseId(Integer enterpriseId) {
		//String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId and isSwitch = 1";
		String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		ILiveVoteActivity activity = (ILiveVoteActivity) query.uniqueResult();
		return activity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveVoteActivity> getH5VoteList(Integer roomId) {
		//String hql = "from ILiveVoteActivity where roomId = :roomId and isEnd = 0 order by isSwitch Desc,id Desc";
		String hql = "from ILiveVoteActivity where roomId = :roomId and isEnd = 0 order by id Desc";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<ILiveVoteActivity> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveVoteActivity getH5Vote(Integer roomId) {
		//String hql = "from ILiveVoteActivity where roomId = :roomId and isEnd = 0 and isSwitch = 1 ";
		String hql = "from ILiveVoteActivity where roomId = :roomId and isEnd = 0 ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<ILiveVoteActivity> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public void checkEndIsClose() {
		//String hql = "update ILiveVoteActivity set isSwitch = 0 where isEnd = 1";
//		String hql = "update ILiveVoteActivity set isSwitch = 0 where isEnd = 1";
//		Query query = this.getSession().createQuery(hql);
//		query.executeUpdate();
	}

	@Override
	public ILiveVoteActivity getActivityByenterpriseId(Integer enterpriseId) {
		//String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId and isSwitch = 1";
		String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId and isSwitch = 1";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveVoteActivity> list =  query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public List<ILiveVoteActivity> getH5VoteListByEnterpriseId(Integer enterpriseId) {
		Date time=new Date();
		String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId and isEnd = 0 and endTime >:time order by id Desc";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.setParameter("time", time);
		List<ILiveVoteActivity> list = query.list();
		return list;
	}

	@Override
	public List<ILiveVoteActivity> getH5VoteList2(Integer enterpriseId) {
//		String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId and isEnd = 0 order by isSwitch Desc,id Desc";
//		Query query = this.getSession().createQuery(hql);
//		query.setParameter("enterpriseId", enterpriseId);
//		List<ILiveVoteActivity> list = query.list();
		return null;
	}

	@Override
	public ILiveVoteActivity getH5Vote2(Integer enterpriseId) {
		/*String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId and isEnd = 0 and isSwitch = 1 ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveVoteActivity> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}*/
		return null;
	}

	@Override
	public List<ILiveVoteActivity> getH5VoteListByUserId(Long userId) {
		Date time=new Date();
		String hql = "from ILiveVoteActivity where userId = :userId and isEnd = 0 and endTime >:time order by id Desc";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("time", time);
		List<ILiveVoteActivity> list = query.list();
		return list;
	}

	@Override
	public Pagination getpageByUserId(Long userId,String votename,Integer pageNo,Integer pageSize) {
		Finder finder = Finder.create("from ILiveVoteActivity where userId = :userId and isEnd = 0 ");
		finder.setParam("userId", userId);
		if (votename!=null&&!votename.equals("")) {
			finder.append("and voteName like :voteName");
			finder.setParam("voteName", "%"+votename+"%");
		}
		finder.append(" order by id Desc");
		return find(finder, pageNo, pageSize);
	}

}
