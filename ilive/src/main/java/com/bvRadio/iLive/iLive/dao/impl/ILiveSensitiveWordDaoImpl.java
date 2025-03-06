package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.iLive.dao.ILiveSensitiveWordDao;
import com.bvRadio.iLive.iLive.entity.ILiveSensitiveWord;

import freemarker.ext.rhino.RhinoScriptableModel;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;

@Repository
public class ILiveSensitiveWordDaoImpl extends HibernateBaseDao<ILiveSensitiveWord, Integer> implements ILiveSensitiveWordDao {
	public Pagination getPage(String sensitiveName, int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from ILiveSensitiveWord bean");
		f.append(" where 1=1");
		if (!StringUtils.isBlank(sensitiveName)) {
			f.append(" and bean.sensitiveName like :sensitiveName");
			f.setParam("sensitiveName", "%" + sensitiveName + "%");
		}
		f.append(" order by bean.createTime desc,bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	public List<ILiveSensitiveWord> getList(Boolean disabled) {
		Finder f = Finder.create("select bean from ILiveSensitiveWord bean");
		f.append(" where 1=1");
		if (null != disabled) {
			f.append(" and bean.disabled = :disabled");
			f.setParam("disabled", disabled);
		}
		f.append(" order by bean.id desc");
		return find(f);
	}

	public ILiveSensitiveWord findById(Integer id) {
		ILiveSensitiveWord entity = get(id);
		return entity;
	}

	public ILiveSensitiveWord save(ILiveSensitiveWord bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(ILiveSensitiveWord bean) {
		getSession().update(bean);
	}

	public ILiveSensitiveWord deleteById(Integer id) {
		ILiveSensitiveWord entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	public ILiveSensitiveWord disableById(Integer id, Boolean disabled) {
		ILiveSensitiveWord entity = findById(id);
		if (entity != null) {
			entity.setDisabled(disabled);
		}
		return entity;
	}

	@Override
	protected Class<ILiveSensitiveWord> getEntityClass() {
		return ILiveSensitiveWord.class;
	}

	@Override
	public Pagination getPageByType(Integer type, int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from ILiveSensitiveWord bean");
		f.append(" where 1=1");
		if (type!=0) {
			f.append(" and bean.type like :type");
			f.setParam("type", type);
		}
		f.append(" order by bean.createTime desc,bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@Override
	public Pagination getPageByTypeAndName(Integer type, String sensitiveName, int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from ILiveSensitiveWord bean");
		f.append(" where 1=1");
		if (!StringUtils.isBlank(sensitiveName)) {
			f.append(" and bean.sensitiveName like :sensitiveName");
			f.setParam("sensitiveName", "%" + sensitiveName + "%");
		}
		if (type!=0) {
			f.append(" and bean.type like :type");
			f.setParam("type", type);
		}
		f.append(" order by bean.createTime desc,bean.id desc");
		return find(f, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveSensitiveWord> getAllSentitiveword() {
		String hql = "from ILiveSensitiveWord";
		Query query = this.getSession().createQuery(hql);
		List<ILiveSensitiveWord> list = query.list();
		return list;
	}
}