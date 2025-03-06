package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.DocumentManagerDao;
import com.bvRadio.iLive.iLive.entity.DocumentManager;

@Repository
public class DocumentManagerDaoImpl extends HibernateBaseDao<DocumentManager, Long> implements DocumentManagerDao {

	@Override
	public Pagination getPage(String name,Integer enterpriseId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from DocumentManager where enterpriseId = :enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		if (name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void save(DocumentManager documentManager) {
		this.getSession().save(documentManager);
	}

	@Override
	public void update(DocumentManager documentManagero) {
		this.getSession().update(documentManagero);
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from DocumentManager where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public DocumentManager getById(Long id) {
		return this.get(id);
	}

	@Override
	protected Class<DocumentManager> getEntityClass() {
		return DocumentManager.class;
	}

	@Override
	public Pagination getCollaborativePage(String name, Integer pageNo,
			Integer pageSize, Long userId) {
		Finder finder = Finder.create("from DocumentManager");
		finder.append(" where 1=1 ");
		finder.append(" and userId =:userId ");
		finder.setParam("userId", userId);
		if (name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public Pagination getDocByEnterpriseId(Integer enterpriseId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from DocumentManager");
		finder.append(" where enterpriseId =:enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public List<DocumentManager> getListByEnterpriseId(Integer enterpriseId) {
		String hql = "from DocumentManager where enterpriseId =:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		@SuppressWarnings("unchecked")
		List<DocumentManager> list = query.list();
		return list;
	}

}
