package com.jwzt.statistic.dao;

import com.jwzt.statistic.entity.IiveEventCurrentMax;

public interface IiveEventCurrentMaxDao {

	void save(IiveEventCurrentMax iiveEventCurrentMax);
	void update(IiveEventCurrentMax iiveEventCurrentMax);
	
	IiveEventCurrentMax getByEventId(Long eventId);
}
