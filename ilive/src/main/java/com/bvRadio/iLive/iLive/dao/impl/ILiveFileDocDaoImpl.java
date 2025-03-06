package com.bvRadio.iLive.iLive.dao.impl;

//import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;
import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveFileDocDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileDoc;

public class ILiveFileDocDaoImpl extends HibernateBaseDao<ILiveFileDoc, Long> implements ILiveFileDocDao {

	@Override
	public void save(ILiveFileDoc iLiveFileDoc) {
		this.getSession().save(iLiveFileDoc);
	}

	@Override
	public void update(ILiveFileDoc iLiveFileDoc) {
		this.getSession().update(iLiveFileDoc);
	}

	

	@Override
	protected Class<ILiveFileDoc> getEntityClass() {
		return ILiveFileDoc.class;
	}

	@Override
	public ILiveFileDoc getById(Long fileId) {
		
		String hql = "from ILiveFileDoc where fileId = :fileId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("fileId", fileId);
		List<ILiveFileDoc> list = query.list();
		return list.get(0);
	}

	@Override
	public List<ILiveFileDoc> getListById(Long fileId) {
		
		Finder finder = Finder.create("select bean from ILiveFileDoc bean  where bean.fileId = :fileId ");
		finder.setParam("fileId", fileId);
		
		
		return find(finder);
	}

	@Override
	public void delete(Long fileDocId) {
		String hql = "delete from ILiveFileDoc where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", fileDocId);
		query.executeUpdate();
	}

	@Override
	public List<ILiveFileDoc> getListByDocId(Long docId) {
		Finder finder = Finder.create("select bean from ILiveFileDoc bean  where bean.document.id = :docId ");
		finder.setParam("docId", docId);
		
		
		return find(finder);
	}

	@Override
	public ILiveFileDoc getByFileIdDocId(Long fileId, Long docId) {
		String hql = "from ILiveFileDoc bean where bean.fileId = :fileId and bean.document.id = :docId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("fileId", fileId);
		query.setParameter("docId", docId);
		List<ILiveFileDoc> list = query.list();
		if(list==null||list.size()==0) {
			return null;
		}else {
			return list.get(0);
		}
		
	}

}
