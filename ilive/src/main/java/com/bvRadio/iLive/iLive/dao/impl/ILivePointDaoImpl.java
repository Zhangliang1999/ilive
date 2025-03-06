package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILivePointDao;
import com.bvRadio.iLive.iLive.entity.ILivePoint;

@Repository
public class ILivePointDaoImpl extends HibernateBaseDao<ILivePoint, Integer> implements ILivePointDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from ILivePoint bean");
		f.append(" where 1=1");
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	public List<ILivePoint> getList() {
		Finder f = Finder.create("select bean from ILivePoint bean");
		f.append(" where 1=1");
		f.append(" order by bean.id desc");
		return find(f);
	}

	

	public ILivePoint findById(Integer id) {
		ILivePoint entity = get(id);
		return entity;
	}

	public ILivePoint save(ILivePoint bean) {
		getSession().save(bean);
		return bean;
	}

	public ILivePoint update(ILivePoint bean) {
		getSession().update(bean);
		return bean;
	}

	public ILivePoint deleteById(Integer id) {
		ILivePoint entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	

	@Override
	protected Class<ILivePoint> getEntityClass() {
		return ILivePoint.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILivePoint findDefaultPoint() {
		ILivePoint entity = new ILivePoint();
		List<ILivePoint> list = find("select bean from ILivePoint bean order by bean.id asc");
		if(list.size()>0){
			for (ILivePoint iLivePoint : list) {
				if(iLivePoint.getIsDefault()){
					entity=iLivePoint;
					return iLivePoint;
				}
			}
			if(entity.getPointId()==null){
				entity=list.get(0);
			}
		}
		return entity;
	}

	

	

}