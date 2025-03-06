package com.bvRadio.iLive.core.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.core.dao.AuthenticationDao;
import com.bvRadio.iLive.core.entity.Authentication;

@Repository
public class AuthenticationDaoImpl extends com.bvRadio.iLive.common.hibernate3.HibernateBaseDao<Authentication, String> implements AuthenticationDao {
	public int deleteExpire(Date d) {
		String hql = "delete Authentication bean where bean.updateTime <= :d";
		return getSession().createQuery(hql).setTimestamp("d", d).executeUpdate();
	}

	public Authentication getByUserId(Long userId) {
		String hql = "from Authentication bean where bean.uid=?";
		return (Authentication) findUnique(hql, userId);
	}

	@SuppressWarnings("unchecked")
	public List<Authentication> getListByUserId(Integer userId) {
		String hql = "from Authentication bean where bean.uid=?";
		return find(hql, userId);
	}

	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public Authentication findById(String id) {

		Authentication entity = null;
		entity = get(id);
		return entity;
	}

	public Authentication save(Authentication bean) {
		getSession().save(bean);
		return bean;
	}

	public Authentication deleteById(String id) {
		Authentication entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Authentication> getEntityClass() {
		return Authentication.class;
	}
}