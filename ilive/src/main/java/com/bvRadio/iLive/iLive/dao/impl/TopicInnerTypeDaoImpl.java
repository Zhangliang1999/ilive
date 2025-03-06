package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.TopicInnerTypeDao;
import com.bvRadio.iLive.iLive.entity.TopicInnerType;

@Repository
public class TopicInnerTypeDaoImpl extends HibernateBaseDao<TopicInnerType, Long> implements TopicInnerTypeDao {

	@Override
	public void save(TopicInnerType topicInnerType) {
		this.getSession().save(topicInnerType);
	}

	@Override
	public void update(TopicInnerType topicInnerType) {
		this.getSession().update(topicInnerType);
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from TopicInnerType where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public TopicInnerType getById(Long id) {
		return this.get(id);
	}

	@Override
	public List<TopicInnerType> getListByTopicId(Long topicId) {
		String hql = "from TopicInnerType where topicId = :topicId and isSave = 1 order by orderN";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("topicId", topicId);
		@SuppressWarnings("unchecked")
		List<TopicInnerType> list = query.list();
		return list;
	}

	@Override
	protected Class<TopicInnerType> getEntityClass() {
		return TopicInnerType.class;
	}

	@Override
	public void deleteAllByTopicId(Long topicId) {
		String hql = "delete from TopicInnerType where topicId = :topicId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("topicId", topicId);
		query.executeUpdate();
	}

}
