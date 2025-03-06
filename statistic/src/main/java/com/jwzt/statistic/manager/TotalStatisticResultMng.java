package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.statistic.entity.TotalStatisticResult;

public interface TotalStatisticResultMng {

	public TotalStatisticResult getLastBeanByEndTime(Date endTime);

	public List<TotalStatisticResult> listByParams(Date startTime, Date endTime);

	public TotalStatisticResult sumByParams(Date startTime, Date endTime);

	public TotalStatisticResult getBeanById(Long id);

	public TotalStatisticResult save(TotalStatisticResult bean);

	public void update(TotalStatisticResult bean);
}