package com.jwzt.statistic.dao.impl;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.statistic.dao.IiveEventCurrentMaxDao;
import com.jwzt.statistic.entity.IiveEventCurrentMax;

public class IiveEventCurrentMaxDaoImpl extends BaseHibernateDao<IiveEventCurrentMax, Long> implements IiveEventCurrentMaxDao {

	@Override
	public void save(IiveEventCurrentMax iiveEventCurrentMax) {
		this.getSession().save(iiveEventCurrentMax);
	}

	@Override
	public IiveEventCurrentMax getByEventId(Long eventId) {
		return this.get(eventId);
	}

	@Override
	protected Class<IiveEventCurrentMax> getEntityClass() {
		return IiveEventCurrentMax.class;
	}

	@Override
	public void update(IiveEventCurrentMax iiveEventCurrentMax) {
		this.getSession().update(iiveEventCurrentMax);
	}

}
