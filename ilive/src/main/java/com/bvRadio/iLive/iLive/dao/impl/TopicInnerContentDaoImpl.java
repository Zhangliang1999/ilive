package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.TopicInnerContentDao;
import com.bvRadio.iLive.iLive.entity.TopicInnerContent;

@Repository
public class TopicInnerContentDaoImpl extends HibernateBaseDao<TopicInnerContent, Long> implements TopicInnerContentDao {

	@Override
	public List<TopicInnerContent> getListByTypeId(Long typeId) {
		String hql = "from TopicInnerContent where innerTypeId = :innerTypeId and isSave = 1 order by orderN";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("innerTypeId", typeId);
		@SuppressWarnings("unchecked")
		List<TopicInnerContent> list = query.list();
		return list;
	}

	@Override
	public void save(TopicInnerContent topicInnerContent) {
		this.getSession().save(topicInnerContent);
	}

	@Override
	public void update(TopicInnerContent topicInnerContent) {
		this.getSession().update(topicInnerContent);
	}

	@Override
	public void deleteById(Long id) {
		String hql = "delete from TopicInnerContent where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public void deleteByTypeId(Long typeId) {
		String hql = "delete from TopicInnerContent where innerTypeId = :innerTypeId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("innerTypeId", typeId);
		query.executeUpdate();
	}

	@Override
	protected Class<TopicInnerContent> getEntityClass() {
		return TopicInnerContent.class;
	}

	@Override
	public TopicInnerContent getById(Long id) {
		return this.get(id);
	}

}
