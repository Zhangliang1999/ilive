package com.jwzt.statistic.manager.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.statistic.dao.IiveEventCurrentMaxDao;
import com.jwzt.statistic.entity.IiveEventCurrentMax;
import com.jwzt.statistic.manager.IiveEventCurrentMaxMng;

@Transactional
public class IiveEventCurrentMaxMngImpl implements IiveEventCurrentMaxMng {

	@Autowired
	private IiveEventCurrentMaxDao liveEventCurrentMaxDao;
	
	@Override
	public void save(IiveEventCurrentMax iiveEventCurrentMax) {
		liveEventCurrentMaxDao.save(iiveEventCurrentMax);
	}

	@Override
	public IiveEventCurrentMax getByEventId(Long eventId) {
		return liveEventCurrentMaxDao.getByEventId(eventId);
	}

	@Override
	public void update(IiveEventCurrentMax iiveEventCurrentMax) {
		liveEventCurrentMaxDao.update(iiveEventCurrentMax);
	}

}
