package com.bvRadio.iLive.manager.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.manager.dao.ReplyDao;
import com.bvRadio.iLive.manager.entity.Reply;

@Repository
public class ReplyDaoImpl extends HibernateBaseDao<Reply, Long> implements ReplyDao{

	@Override
	public void save(Reply reply) {
		this.getSession().save(reply);
	}

	@Override
	public void deleteById(Long id) {
		Reply reply = this.get(id);
		reply.setIsDel(1);
		update(reply);
	}

	@Override
	public void update(Reply reply) {
		this.getSession().update(reply);
	}

	@Override
	public Reply getById(Long id) {
		return this.get(id);
	}

	@Override
	public List<Reply> getListByReportId(Long reportId) {
		String hql = "from Reply where reportId = :reportId and isDel = 0";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("reportId", reportId);
		@SuppressWarnings("unchecked")
		List<Reply> list = query.list();
		return list;
	}

	@Override
	protected Class<Reply> getEntityClass() {
		return Reply.class;
	}

}
