package com.jwzt.statistic.manager;

import com.jwzt.statistic.entity.IiveEventCurrentMax;

public interface IiveEventCurrentMaxMng {

	void save(IiveEventCurrentMax iiveEventCurrentMax);
	void update(IiveEventCurrentMax iiveEventCurrentMax);
	
	IiveEventCurrentMax getByEventId(Long eventId);
}
