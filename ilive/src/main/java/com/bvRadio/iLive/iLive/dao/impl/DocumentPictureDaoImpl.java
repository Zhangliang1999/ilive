package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.DocumentPictureDao;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;

@Repository
public class DocumentPictureDaoImpl extends HibernateBaseDao<DocumentPicture, Long> implements DocumentPictureDao {

	@Override
	public void save(DocumentPicture documentPicture) {
		this.getSession().save(documentPicture);
	}

	@Override
	public void delete(Long id) {
		DocumentPicture documentPicture = this.get(id);
		this.getSession().delete(documentPicture);
	}

	@Override
	protected Class<DocumentPicture> getEntityClass() {
		return DocumentPicture.class;
	}

	@Override
	public List<DocumentPicture> getListByDocId(Long docId,Integer pageSize,Integer pageNo) {
		String hql = "from DocumentPicture where docId = :docId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("docId", docId);
		query.setMaxResults(pageSize);
		query.setFirstResult((pageNo-1)*pageSize);
		return null;
	}

	@Override
	public Pagination getPage(Long docId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from DocumentPicture where docId=:docId");
		finder.setParam("docId", docId);
		return find(finder, pageNo, pageSize);
	}

	@Override
	public List<DocumentPicture> getListByDocId(Long docId) {
		String hql = "from DocumentPicture where docId=:docId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("docId", docId);
		@SuppressWarnings("unchecked")
		List<DocumentPicture> list = query.list();
		return list;
	}

}
