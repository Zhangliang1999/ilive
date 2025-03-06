package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILIvePageDecorateDao;
import com.bvRadio.iLive.iLive.entity.PageDecorate;

@Repository
public class ILIvePageDecorateDaoImpl extends HibernateBaseDao<PageDecorate, Long> implements ILIvePageDecorateDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageDecorate> getList(Long liveEventId) {
		String hql = "from PageDecorate where liveEventId = ? order by menuOrder";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, liveEventId);
		List<PageDecorate> list = query.list();
		return list;
	}

	@Override
	protected Class<PageDecorate> getEntityClass() {
		return PageDecorate.class;
	}

	@Override
	public void removePageDecorate(Long eventId, Integer type) {
		String hql = "delete from PageDecorate where liveEventId = ? and menuType = ?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, eventId);
		query.setParameter(1, type);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageDecorate> getListByIdAndType(Long liveEventId, Integer menuType) {
		String hql = "from PageDecorate where liveEventId = ? and menuType = ?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, liveEventId);
		query.setParameter(1, menuType);
		List<PageDecorate> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageDecorate> getListByIdAndTypeAndOrder(Long liveEventId, Integer menuOrder) {
		String hql = "from PageDecorate where liveEventId = ? and menuOrder > ?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, liveEventId);
		query.setParameter(1, menuOrder);
		List<PageDecorate> list = query.list();
		return list;
	}

	@Override
	public void updatePageDecorate(Long liveEventId,Integer menuType, Integer menuOrder) {
		String hql = "update PageDecorate set menuOrder = ? where liveEventId = ? and menuType = ?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, menuOrder);
		query.setParameter(1, liveEventId);
		query.setParameter(2, menuType);
		query.executeUpdate();
		this.getSession().clear();
	}

	@Override
	public void addPageDecorate(PageDecorate page) {
		this.getSession().save(page);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageDecorate> getAddList(Long liveEventId, Integer menuOrder) {
		String hql = "from PageDecorate where liveEventId = ? and menuOrder >= ?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, liveEventId);
		query.setParameter(1, menuOrder);
		List<PageDecorate> list = query.list();
		return list;
	}

	@Override
	public void updatePageDecorate(PageDecorate page) {
		StringBuilder hql = new StringBuilder("update PageDecorate set menuName = ?,hrefValue = ? ");
		if(page.getRichContent()!=null) {
			hql.append(",richContent = '"+page.getRichContent()+"'");
		}
		hql.append(" where id = ?");
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter(0, page.getMenuName());
		query.setParameter(1, page.getHrefValue());
		query.setParameter(2, page.getId());
		query.executeUpdate();
	}

	@Override
	public void updateOrderById(Long id, Integer menuOrder) {
		System.out.println("dao:  "+id  +"  "+menuOrder);
		String hql = "update PageDecorate set menuOrder = :menuOrder where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("menuOrder", menuOrder);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public void removeById(Long id) {
		String hql = "delete from PageDecorate where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public void updatePageContent(PageDecorate page) {
		this.getSession().update(page);
	}

}
