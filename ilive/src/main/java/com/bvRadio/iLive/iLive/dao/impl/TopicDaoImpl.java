package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.TopicDao;
import com.bvRadio.iLive.iLive.entity.Topic;

@Repository
public class TopicDaoImpl extends HibernateBaseDao<Topic, Long> implements TopicDao {

	@Override
	public Pagination getPage(Long userId,String name, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from Topic where userId = :userId");
		finder.setParam("userId", userId);
		if(name!=null && !name.equals("")) {
			finder.append(" and name like name");
			finder.setParam("name", "%"+name+"%");
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo==null?1:pageNo, pageSize);
	}

	@Override
	public void save(Topic topic) {
		this.getSession().save(topic);
	}

	@Override
	public void update(Topic topic) {
		this.getSession().update(topic);
	}

	@Override
	public List<Topic> getByEnterpriseId(Integer enterpriseId) {
		String hql = "from Topic where enterpriseId = :enterpriseId order by id DESC";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		@SuppressWarnings("unchecked")
		List<Topic> list = query.list();
		return list;
	}

	@Override
	public Topic getById(Long id) {
		return this.get(id);
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from Topic where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	protected Class<Topic> getEntityClass() {
		return Topic.class;
	}

}
