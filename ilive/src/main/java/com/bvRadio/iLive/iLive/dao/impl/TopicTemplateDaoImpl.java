package com.bvRadio.iLive.iLive.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.TopicTemplateDao;
import com.bvRadio.iLive.iLive.entity.TopicTemplate;

@Repository
public class TopicTemplateDaoImpl extends HibernateBaseDao<TopicTemplate, Long> implements TopicTemplateDao {

	@Override
	public void save(TopicTemplate topicTemplate) {
		this.getSession().save(topicTemplate);
	}

	@Override
	public TopicTemplate getById(Long id) {
		return this.get(id);
	}

	@Override
	public TopicTemplate getByEnterprise(Integer enterpriseId) {
		String hql = "from TopicTemplate where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		TopicTemplate uniqueResult = (TopicTemplate) query.uniqueResult();
		return uniqueResult;
	}

	@Override
	public void update(TopicTemplate topicTemplate) {
		this.getSession().update(topicTemplate);
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from TopicTemplate where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	protected Class<TopicTemplate> getEntityClass() {
		return TopicTemplate.class;
	}

}
