package com.bvRadio.iLive.manager.dao.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.manager.dao.InstationMessageDao;
import com.bvRadio.iLive.manager.entity.InstationMessage;

@Repository
public class InstationMessageDaoImpl extends HibernateBaseDao<InstationMessage, Long> implements InstationMessageDao {

	@Override
	public void save(InstationMessage instationMessage) {
		this.getSession().save(instationMessage);
	}

	@Override
	public Pagination getPage(Long id, Long userId, Timestamp startTime, Timestamp endTime, Integer pageNo,
			Integer pageSize) {
		Finder finder = Finder.create("from InstationMessage where 1=1");
		if (id!=null) {
			finder.append(" and id = :id");
			finder.setParam("id", id);
		}
		if (userId!=null) {
			finder.append(" and userId = :userId");
			finder.setParam("userId", userId);
		}
		
		finder.append(" order by createTime DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void update(InstationMessage instationMessage) {
		this.getSession().update(instationMessage);
	}

	@Override
	public void delete(Long id) {
		InstationMessage instationMessage = this.get(id);
		instationMessage.setIsDel(1);
		update(instationMessage);
	}

	@Override
	protected Class<InstationMessage> getEntityClass() {
		return InstationMessage.class;
	}

	@Override
	public InstationMessage getById(Long id) {
		return this.get(id);
	}

}
