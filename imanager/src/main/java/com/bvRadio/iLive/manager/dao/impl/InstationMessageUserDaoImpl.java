package com.bvRadio.iLive.manager.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.manager.dao.InstationMessageUserDao;
import com.bvRadio.iLive.manager.entity.InstationMessageUser;
@Repository
public class InstationMessageUserDaoImpl extends HibernateBaseDao<InstationMessageUser, Long> implements InstationMessageUserDao {

	@Override
	public void save(InstationMessageUser instationMessageUser) {
		this.getSession().save(instationMessageUser);
	}

	@Override
	public void update(InstationMessageUser instationMessageUser) {
		this.getSession().update(instationMessageUser);
	}

	@Override
	public InstationMessageUser getById(Long id) {
		return this.get(id);
	}

	@Override
	public List<InstationMessageUser> getByMsgId(Long msgId) {
		String hql = "from InstationMessageUser where messageId = :messageId order by id DESC";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("messageId", msgId);
		@SuppressWarnings("unchecked")
		List<InstationMessageUser> list = query.list();
		return list;
	}

	@Override
	public Pagination getPage(Long mesId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from InstationMessageUser where messageId = :messageId");
		finder.setParam("messageId",mesId);
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	protected Class<InstationMessageUser> getEntityClass() {
		return InstationMessageUser.class;
	}

	@Override
	public Pagination getPageByUserId(Long userId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from InstationMessageUser where recUserId = :recUserId");
		finder.setParam("recUserId",userId);
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

}
