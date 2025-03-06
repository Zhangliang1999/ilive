package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.BBSDiyOptionDao;
import com.bvRadio.iLive.iLive.entity.BBSDiyformOption;

@Repository
public class BBSDiyOptionDaoImpl extends HibernateBaseDao<BBSDiyformOption, Integer> implements BBSDiyOptionDao {

	@Override
	public BBSDiyformOption findById(Integer id) {
		BBSDiyformOption entity = get(id);
		return entity;
	}

	@Override
	public BBSDiyformOption save(BBSDiyformOption bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<BBSDiyformOption> getEntityClass() {
		// TODO Auto-generated method stub
		return BBSDiyformOption.class;
	}
	

}