package com.bvRadio.iLive.iLive.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILivePrizeDao;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;

@Repository
public class ILivePrizeDaoImpl extends HibernateBaseDao<ILiveLottery, Long> implements ILivePrizeDao{

	@Override
	protected Class<ILiveLottery> getEntityClass() {
		return ILiveLottery.class;
	}

	@Override
	public Pagination getPage(String prizeName,Integer roomId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveLottery where roomId = :roomId");
		finder.setParam("roomId", roomId);
		if (prizeName!=null&&!"".equals(prizeName)) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+prizeName+"%");
		}
		finder.append(" order by id Desc");
		return find(finder, pageNo, pageSize);
	}
	@Override
	public Pagination getPageByEnterpriseId(String prizeName, Integer enterpriseId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveLottery where enterpriseId = :enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		if (prizeName!=null&&!"".equals(prizeName)) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+prizeName+"%");
		}
		finder.append(" order by id Desc");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void save(ILiveLottery lottery) {
		this.getSession().save(lottery);
	}

	@Override
	public ILiveLottery getPrize(Long id) {
		return this.get(id);
	}

	@Override
	public void setLetEnd() {
		String hql = "update ILiveLottery set isEnd = 1 where endTime>:now";
		Query query = this.getSession().createQuery(hql);
		Timestamp now = new Timestamp(new Date().getTime());
		query.setParameter("now", now);
		query.executeUpdate();
		
	}

	@Override
	public void update(ILiveLottery lottery) {
		this.getSession().update(lottery);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLottery> getListByRoomId(Integer roomId) {
		String hql = "from ILiveLottery where roomId = :roomId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<ILiveLottery> list = query.list();
		return list;
	}

	@Override
	public List<ILiveLottery> getListUserH5ByRoomId(Integer roomId) {
		StringBuilder hql = new StringBuilder("from ILiveLottery where roomId = :roomId");
		hql.append(" and isEnd = 0");
		hql.append(" order by isSwitch DESC,id DESC");
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("roomId", roomId);
		List<ILiveLottery> list = query.list();
		return list;
	}

	@Override
	public void letClose(Integer roomId) {
		String hql = "update ILiveLottery set isSwitch = 0 where isSwitch = 1 and roomId = :roomId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveLottery isStartPrize(Integer roomId) {
		String hql = "from ILiveLottery where roomId = :roomId and isSwitch = 1";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<ILiveLottery> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public ILiveLottery isEnterpriseStartPrize(Integer enterpriseId) {
		String hql = "from ILiveLottery where enterpriseId = :enterpriseId and isSwitch = 1";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveLottery> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public List<ILiveLottery> getListUserH5ByEnterpriseId(Integer enterpriseId) {
		Date time=new Date();
		StringBuilder hql = new StringBuilder("from ILiveLottery where enterpriseId = :enterpriseId and endTime >:time ");
		hql.append(" and isEnd = 0");
		hql.append(" order by id DESC");
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("enterpriseId", enterpriseId);
		query.setParameter("time", time);
		List<ILiveLottery> list = query.list();
		return list;
	}

	@Override
	public ILiveLottery isStartPrize2(Integer enterpriseId) {
		String hql = "from ILiveLottery where enterpriseId = :enterpriseId and isSwitch = 1";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveLottery> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public List<ILiveLottery> getListUserH5ByUserId(Long userId) {
		Date time=new Date();
		StringBuilder hql = new StringBuilder("from ILiveLottery where userId = :userId and endTime >:time ");
		hql.append(" and isEnd = 0");
		hql.append(" order by id DESC");
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("userId", userId);
		query.setParameter("time", time);
		@SuppressWarnings("unchecked")
		List<ILiveLottery> list = query.list();
		return list;
	}

	@Override
	public Pagination getpageByUserId(Long userId, String prizeName, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveLottery where userId = :userId");
		finder.setParam("userId", userId);
		if (prizeName!=null&&!prizeName.equals("")) {
			finder.append(" name like :name");
			finder.setParam("name", prizeName);
		}
		finder.append(" and isEnd = 0 order by id DESC");
		return find(finder, pageNo, pageSize);
	}

}
