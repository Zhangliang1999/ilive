package com.bvRadio.iLive.iLive.dao.impl;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.PhotoGalleryDao;
import com.bvRadio.iLive.iLive.entity.PhotoGallery;
@Repository
public class PhotoGalleryDaoImpl extends HibernateBaseDao<PhotoGallery, Long> implements PhotoGalleryDao {

	@Override
	public Pagination getPage(Integer enterpriseId,Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from PhotoGallery");
		
		finder.append(" order by createTime DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void update(PhotoGallery photoGallery) {
		this.getSession().update(photoGallery);
	}

	@Override
	public void save(PhotoGallery photoGallery) {
		this.getSession().save(photoGallery);
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from PhotoGallery where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	protected Class<PhotoGallery> getEntityClass() {
		return PhotoGallery.class;
	}

}
