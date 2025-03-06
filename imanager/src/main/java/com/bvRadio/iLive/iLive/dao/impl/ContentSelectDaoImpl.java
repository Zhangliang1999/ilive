package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ContentSelectDao;
import com.bvRadio.iLive.iLive.entity.ContentSelect;

@Repository
public class ContentSelectDaoImpl extends HibernateBaseDao<ContentSelect, Long> implements ContentSelectDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentSelect> getListByType(Integer contentType) {
		String hql = "from ContentSelect where contentType = :contentType order by indexs";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("contentType", contentType);
		List<ContentSelect> list = query.list();
		return list;
	}

	@Override
	protected Class<ContentSelect> getEntityClass() {
		return ContentSelect.class;
	}

	@Override
	public void saveselect(ContentSelect content) {
		this.getSession().save(content);
	}

	@Override
	public void removeselect(Integer id) {
		String hql = "delete from ContentSelect where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public void saveindex(Integer id, Integer index) {
		String hql = "update ContentSelect set indexs = :indexs where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("indexs", index);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentSelect> getList() {
		String hql = "from ContentSelect";
		Query query = this.getSession().createQuery(hql);
		List<ContentSelect> list = query.list();
		return list;
	}

	@Override
	public Pagination getPager(Integer contentType,Integer type,Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ContentSelect where 1=1");
		if(contentType!=null) {
			finder.append(" and contentType = :contentType");
			finder.setParam("contentType", contentType);
		}
		if(type!=null) {
			finder.append(" and type = :type");
			finder.setParam("type", type);
		}
		finder.append(" order by indexs");
		return find(finder,pageNo==null?1:pageNo,pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentSelect> getListByShows(Integer shows) {
		String hql = "from ContentSelect where shows = :shows order by indexs";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("shows", shows);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContentSelect> getListByShows(Integer shows,Integer pageNo,Integer enterpriseId) {
		String hql = "from ContentSelect where shows = :shows and enterpriseId = :enterpriseId order by indexs";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("shows", shows);
		query.setParameter("enterpriseId", enterpriseId);
		query.setFirstResult((pageNo-1)*10);
		query.setMaxResults(10);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentSelect> getNumByShows(Integer shows, Integer num,Integer enterpriseId) {
		String hql = "from ContentSelect where shows = :shows and enterpriseId = :enterpriseId order by indexs";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("shows", shows);
		query.setParameter("enterpriseId", enterpriseId);
		query.setFirstResult(0);
		query.setMaxResults(num);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentSelect> getListByShowsAndEnid(Integer shows, Integer enterpriseId) {
		String hql = "from ContentSelect where shows = :shows and enterpriseId = :enterpriseId order by indexs";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("shows", shows);
		query.setParameter("enterpriseId", enterpriseId);
		return query.list();
	}

}
