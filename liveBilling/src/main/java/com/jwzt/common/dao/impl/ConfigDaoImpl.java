package com.jwzt.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

/*import com.jwzt.core.dao.ConfigDao;
import com.jwzt.core.entity.Config;*/
import com.jwzt.common.dao.ConfigDao;
import com.jwzt.common.entity.Config;
import com.jwzt.common.hibernate3.BaseHibernateDao;

@Repository
public class ConfigDaoImpl extends BaseHibernateDao<Config, String> implements ConfigDao {
	@Override
	@SuppressWarnings("unchecked")
	public List<Config> getList() {
		String hql = "from Config";
		return find(hql);
	}

	@Override
	public Config findById(String id) {
		Config entity = get(id);
		return entity;
	}

	@Override
	public Config save(Config bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public Config deleteById(String id) {
		Config entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Config> getEntityClass() {
		return Config.class;
	}
}