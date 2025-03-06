package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.entity.TotalStatisticResult;

public interface TotalStatisticResultDao {
	public TotalStatisticResult sumByParams(Date startTime, Date endTime);

	public List<TotalStatisticResult> listByParams(Date startTime, Date endTime);

	public TotalStatisticResult getLastBeanByEndTime(Date endTime);

	public TotalStatisticResult getBeanById(Long id);

	public TotalStatisticResult save(TotalStatisticResult bean);

	public TotalStatisticResult updateByUpdater(Updater<TotalStatisticResult> updater);
}