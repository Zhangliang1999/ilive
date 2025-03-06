package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveHomepageLinkDao;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageLink;
import com.thoughtworks.xstream.core.util.ThreadSafeSimpleDateFormat;

@Repository
public class ILiveHomepageLinkDaoImpl extends HibernateBaseDao<ILiveHomepageLink, Long> implements ILiveHomepageLinkDao{

	@Override
	protected Class<ILiveHomepageLink> getEntityClass() {
		return ILiveHomepageLink.class;
	}

	@Override
	public void save(ILiveHomepageLink link) {
		this.getSession().save(link);
	}

	@Override
	public ILiveHomepageLink getById(Long id) {
		return this.get(id);
	}

	@Override
	public void update(ILiveHomepageLink link) {
		this.getSession().update(link);
	}

	@Override
	public Pagination getPage(Integer enterpriseId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveHomepageLink where enterpriseId = :enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		return find(finder, pageNo, pageSize);
	}

	@Override
	public List<ILiveHomepageLink> getListByEnterpriseId(Integer enterpriseId) {
		String hql = "from ILiveHomepageLink where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		@SuppressWarnings("unchecked")
		List<ILiveHomepageLink> list = query.list();
		return list;
	}

}
