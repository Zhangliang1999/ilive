package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.BBSDiymodelDao;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;

@Repository
public class BBSDiymodelDaoImpl extends HibernateBaseDao<BBSDiymodel, Integer> implements BBSDiymodelDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from BBSDiymodel bean");
		f.append(" where 1=1");
		f.append(" order by bean.diymodelId desc");
		return find(f, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	public List<BBSDiymodel> getList() {
		Finder f = Finder.create("select bean from BBSDiymodel bean");
		f.append(" where 1=1");
		f.append(" order by bean.diyOrder asc");
		return find(f);
	}

	@SuppressWarnings("unchecked")
	public List<BBSDiymodel> getListByDiyformId(Integer diyformId) {
		Finder f = Finder.create("select bean from BBSDiymodel bean");
		f.append(" where 1=1");
		if (null != diyformId) {
			f.append(" and bean.bbsDiyform.diyformId = :diyformId");
			f.setParam("diyformId", diyformId);
		}
		f.append(" order by bean.diyOrder asc");
		return find(f);
	}

	public BBSDiymodel findById(Integer id) {
		BBSDiymodel entity = get(id);
		return entity;
	}

	public BBSDiymodel save(BBSDiymodel bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(BBSDiymodel bean) {
		getSession().update(bean);
	}

	public BBSDiymodel deleteById(Integer id) {
		BBSDiymodel entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	public List<BBSDiymodel> deleteByDiyformId(Integer diyformId) {
		List<BBSDiymodel> BBSDiymodelList = getListByDiyformId(diyformId);
		for (int i = 0; i < BBSDiymodelList.size(); i++) {
			BBSDiymodel entity = BBSDiymodelList.get(i);
			if (entity != null) {
				getSession().delete(entity);
			}
		}
		return BBSDiymodelList;
	}

	@Override
	protected Class<BBSDiymodel> getEntityClass() {
		return BBSDiymodel.class;
	}

}