package com.bvRadio.iLive.core.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.core.dao.UnifiedUserDao;
import com.bvRadio.iLive.core.entity.UnifiedUser;

@Repository
public class UnifiedUserDaoImpl extends HibernateBaseDao<UnifiedUser, Integer> implements UnifiedUserDao {
	public UnifiedUser getByUsername(String username) {
		UnifiedUser bean = null;
		if (!StringUtils.isBlank(username)) {
			bean = findUniqueByProperty("username", username);
		}
		return bean;
	}

	public List<UnifiedUser> getByEmail(String email) {
		return findByProperty("email", email);
	}

	public int countByEmail(String email) {
		String hql = "select count(*) from UnifiedUser bean where bean.email=:email";
		Query query = getSession().createQuery(hql);
		query.setParameter("email", email);
		return ((Number) query.iterate().next()).intValue();
	}

	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public UnifiedUser findById(Integer id) {
		UnifiedUser entity = get(id);
		return entity;
	}

	public UnifiedUser save(UnifiedUser bean) {
		getSession().save(bean);
		return bean;
	}

	public UnifiedUser deleteById(Integer id) {
		UnifiedUser entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	public int countByMobile(String mobile) {
		String hql = "select count(*) from UnifiedUser bean where bean.mobile=:mobile and bean.mobileAuth=:mobileAuth";
		Query query = getSession().createQuery(hql);
		query.setParameter("mobile", mobile);
		query.setParameter("mobileAuth", UnifiedUser.IS_YES_AUTH);
		return ((Number) query.iterate().next()).intValue();
	}

	public UnifiedUser getByMobile(String mobile) {

		Finder f = Finder.create("from UnifiedUser bean where bean.mobile=:mobile and bean.mobileAuth=:mobileAuth");
		f.setParam("mobile", mobile);
		f.setParam("mobileAuth", UnifiedUser.IS_YES_AUTH);
		f.setMaxResults(1);
		return (UnifiedUser) f.createQuery(getSession()).uniqueResult();
	}

	public UnifiedUser getByThirdId(String thirdId) {
		Finder f = Finder.create("from UnifiedUser bean where bean.thirdId=:thirdId ");
		f.setParam("thirdId", thirdId);
		f.setMaxResults(1);
		return (UnifiedUser) f.createQuery(getSession()).uniqueResult();
	}

	@Override
	protected Class<UnifiedUser> getEntityClass() {
		return UnifiedUser.class;
	}

	@Override
	public UnifiedUser getByThirdIdAndType(String openId, int thirdType) {
		Finder f = Finder.create("from UnifiedUser bean where bean.thirdId=:thirdId and bean.thirdType=:thirdType");
		f.setParam("thirdId", openId);
		f.setParam("thirdType", thirdType);
		f.setMaxResults(1);
		return (UnifiedUser) f.createQuery(getSession()).uniqueResult();
	}
}