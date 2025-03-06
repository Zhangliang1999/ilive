package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.iLive.dao.ILiveUploadServerDao;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;

@Repository
public class ILiveUploadServerDaoImpl extends HibernateBaseDao<ILiveUploadServer, Integer> implements ILiveUploadServerDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from ILiveUploadServer bean");
		f.append(" where 1=1");
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	public List<ILiveUploadServer> getList() {
		Finder f = Finder.create("select bean from ILiveUploadServer bean");
		f.append(" where 1=1");
		f.append(" order by bean.id desc");
		return find(f);
	}

	@SuppressWarnings("unchecked")
	public ILiveUploadServer findDefaultSever() {
		ILiveUploadServer entity = new ILiveUploadServer();
		List<ILiveUploadServer> list = find("select bean from ILiveUploadServer bean order by bean.id asc");
		if(list.size()>0){
			for (ILiveUploadServer iLiveUploadServer : list) {
				if(iLiveUploadServer.getIsDefault()){
					entity=iLiveUploadServer;
					return iLiveUploadServer;
				}
			}
			if(entity.getId()==null){
				entity=list.get(0);
			}
		}
		return entity;
	}

	public ILiveUploadServer findById(Integer id) {
		ILiveUploadServer entity = get(id);
		return entity;
	}

	public ILiveUploadServer save(ILiveUploadServer bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(ILiveUploadServer bean) {
		getSession().update(bean);
	}

	public ILiveUploadServer deleteById(Integer id) {
		ILiveUploadServer entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ILiveUploadServer> getEntityClass() {
		return ILiveUploadServer.class;
	}

	@Override
	public ILiveUploadServer getDefaultServer() {
		Query query = getSession().createQuery("from ILiveUploadServer where isDefault=:isDefault").setParameter("isDefault", true);
		return (ILiveUploadServer) query.uniqueResult();
	}

}