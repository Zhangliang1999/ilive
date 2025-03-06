package com.bvRadio.iLive.manager.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.manager.dao.FankongDao;
import com.bvRadio.iLive.manager.entity.CSAndYellow;

@Repository
public class FankongDaoImpl extends HibernateBaseDao<CSAndYellow, String> implements FankongDao {

	@Override
	public List<CSAndYellow> getList(Integer roomId) {
		String hql = "from CSAndYellow where roomId = :roomId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<CSAndYellow> list = query.list();
		return list;
	}

	@Override
	public CSAndYellow getById(String id) {
		return this.get(id);
	}

	@Override
	public Pagination getPage(Integer roomId, Timestamp checkTime, Integer monitorLevel, Integer pageNo,
			Integer pageSize) {
		Finder finder = Finder.create("from CSAndYellow where roomId = :roomId");
		finder.setParam("roomId", roomId);
		if (monitorLevel!=null) {
			finder.append(" and monitorLevel = :monitorLevel");
			finder.setParam("monitorLevel", monitorLevel);
		}
		return find(finder, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
	}

	@Override
	public void update(CSAndYellow csAndYellow) {
		this.getSession().update(csAndYellow);
	}

	@Override
	protected Class<CSAndYellow> getEntityClass() {
		return CSAndYellow.class;
	}

	@Override
	public Pagination getPage(Integer roomId, Timestamp startTime, Timestamp endTime, Integer type1, Integer type2,
			Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from CSAndYellow where roomId = :roomId");
		finder.setParam("roomId", roomId);
		if (type1!=null&&type1!=0) {
			finder.append(" and monitorLevel = :monitorLevel");
			finder.setParam("monitorLevel", type1);
		}
		if(type2!=null&&type2!=0) {
			finder.append(" and replyMsg like :replyMsg");
			if (type2==1) {
				//涉恐
				finder.setParam("replyMsg", "%error%");
			}else if (type2==2) {
				//涉黄
				finder.setParam("replyMsg", "%porn%");
			}else {
				finder.setParam("replyMsg", "%");
			}
		}
		if (startTime!=null) {
			finder.append(" and checkTime > :checkTime1");
			finder.setParam("checkTime1", startTime);
		}
		if (startTime!=null) {
			finder.append(" and checkTime < :checkTime2");
			finder.setParam("checkTime2", endTime);
		}
		finder.append(" order by checkTime DESC");
		return find(finder, pageNo, pageSize);
	}

}
