package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.BBSDiyformDao;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.util.StringUtil;

@Repository
public class BBSDiyformDaoImpl extends HibernateBaseDao<BBSDiyform, Integer> implements BBSDiyformDao {
	
	public Pagination getPage(int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from BBSDiyform bean");
		f.append(" where 1=1");
		f.append(" order by bean.diyformId desc");
		return find(f, pageNo, pageSize);
	}

	@Override
	public Pagination getPageByParam(String voteName, Integer pageNo, Integer pageSize) {
		Finder f = Finder.create("select bean from BBSDiyform bean");
		f.append(" where (bean.delFlag is null or bean.delFlag !=1) and "
				+ "(bean.formType is not null and bean.formType =1)");
		if(StringUtils.isNotBlank(voteName)){
			f.append(" and bean.diyformName like :voteName");
			f.setParam("voteName", "%"+voteName+"%");
		}
		f.append(" order by bean.diyformId desc");
		return find(f, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
	}

	
	@SuppressWarnings("unchecked")
	public List<BBSDiyform> getList() {
		Finder f = Finder.create("select bean from BBSDiyform bean");
		f.append(" where 1=1");
		f.append(" order by bean.diyformId desc");
		return find(f);
	}

	public BBSDiyform findById(Integer id) {
		BBSDiyform entity = get(id);
		return entity;
	}

	public BBSDiyform save(BBSDiyform bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(BBSDiyform bean) {
		getSession().update(bean);
	}

	public BBSDiyform deleteById(Integer id) {
		BBSDiyform entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<BBSDiyform> getEntityClass() {
		return BBSDiyform.class;
	}


}