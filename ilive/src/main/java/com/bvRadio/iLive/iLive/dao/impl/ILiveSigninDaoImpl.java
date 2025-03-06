package com.bvRadio.iLive.iLive.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveSigninDao;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;
import com.bvRadio.iLive.iLive.entity.ILiveSignin;

@Repository
public class ILiveSigninDaoImpl extends HibernateBaseDao<ILiveSignin, Long> implements ILiveSigninDao {

	@Override
	public Pagination getPage(String userId,String name,Integer roomName,Integer status,Integer pageNo,Integer pageSize) {
		Finder finder = Finder.create("from ILiveSignin where 1 = 1 ");
		
		if (name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		if (userId!=null&&!userId.equals("")) {
			finder.append(" and userId in (select userId from ILiveManager where enterPrise.enterpriseId=:userId)");
			finder.setParam("userId",Integer.parseInt(userId) );
		}
		if (roomName!=null) {
			finder.append(" and room.roomId =:roomName");
			finder.setParam("roomName", roomName);
		}
		if (status!=0) {
			finder.append(" and state =:status");
			finder.setParam("status", status);
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}
	
	@Override
	public Pagination getPageByEnterpriseId(String userId,String name,Integer enterpriseId,Integer status,Integer pageNo,Integer pageSize) {
		Finder finder = Finder.create("from ILiveSignin where 1 = 1 and enterpriseId = :enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		if (name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		if (userId!=null&&!userId.equals("")) {
			finder.append(" and userId in (select userId from ILiveManager where enterPrise.enterpriseId=:userId)");
			finder.setParam("userId",Integer.parseInt(userId) );
		}
		if (status!=0) {
			finder.append(" and state =:status");
			finder.setParam("status", status);
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}
	
	@Override
	public Pagination getPageByUserId(String name,String userId,Integer status,Integer pageNo,Integer pageSize) {
		Finder finder = Finder.create("from ILiveSignin where 1 = 1 and userId = :userId");
		finder.setParam("userId", Long.parseLong(userId));
		if (name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		
		if (status!=0) {
			finder.append(" and state =:status");
			finder.setParam("status", status);
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void save(ILiveSignin ILiveSignin) {
		this.getSession().save(ILiveSignin);
	}

	@Override
	public void update(ILiveSignin ILiveSignino) {
		this.getSession().update(ILiveSignino);
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from ILiveSignin where signId = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public ILiveSignin getById(Long signId) {
		String hql = "from ILiveSignin where signId = :signId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("signId", signId);
		List<ILiveSignin> list = query.list();
		return list.get(0);
	}

	@Override
	protected Class<ILiveSignin> getEntityClass() {
		return ILiveSignin.class;
	}

	@Override
	public Pagination getCollaborativePage(String name, Integer pageNo,
			Integer pageSize, Long userId) {
		Finder finder = Finder.create("from ILiveSignin");
		finder.append(" where 1=1 ");
		finder.append(" and userId =:userId ");
		finder.setParam("userId", userId);
		if (name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public Pagination getDocByEnterpriseId(Integer enterpriseId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveSignin");
		finder.append(" where enterpriseId =:enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public List<ILiveSignin> getListByEnterpriseId(Integer enterpriseId) {
		Finder finder = Finder.create("from ILiveSignin");
		finder.append(" where room.roomId =:enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		finder.append(" and endTime >=:startTime");
		finder.setParam("startTime", new Date());
		finder.append(" order by id DESC");
		return find(finder);
	}

	@Override
	public ILiveSignin getIsStart(Integer roomId) {
		Date time=new Date();
		String hql = "from ILiveSignin where room.roomId = :roomId and isAllow = 1 and endTime >:time";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		query.setParameter("time", time);
		List<ILiveSignin> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}
	
	@Override
	public ILiveSignin getIsStart2(Integer enterpriseId) {
		Date time=new Date();
		String hql = "from ILiveSignin where enterpriseId = :enterpriseId and isAllow = 1 and endTime >:time";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.setParameter("time", time);
		List<ILiveSignin> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public List<ILiveSignin> getByEnterpriseId(Integer enterpriseId) {
		Finder finder = Finder.create("from ILiveSignin");
		finder.append(" where enterpriseId =:enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		finder.append(" and endTime >=:startTime");
		finder.setParam("startTime", new Date());
		finder.append(" order by id DESC");
		return find(finder);
	}

}
