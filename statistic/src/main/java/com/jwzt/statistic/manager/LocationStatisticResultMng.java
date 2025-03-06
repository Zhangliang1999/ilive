package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.statistic.entity.LocationStatisticResult;

public interface LocationStatisticResultMng {
	public List<LocationStatisticResult> listStatisticResutlByFlag(Long flagId, Integer flagType, Date startTime,
			Date endTime);

	public List<LocationStatisticResult> listStatisticResutlByFlagGroupByCity(Long flagId, Integer flagType,
			Date startTime, Date endTime);

	public List<LocationStatisticResult> listByFlag(Long flagId, Integer flagType, Date startTime, Date endTime);

	public LocationStatisticResult getBeanByProvinceName(String provinceName, Long flagId, Integer flagType,
			Date startTime, Date endTime);

	public LocationStatisticResult save(LocationStatisticResult bean);

	public void update(LocationStatisticResult bean);
}