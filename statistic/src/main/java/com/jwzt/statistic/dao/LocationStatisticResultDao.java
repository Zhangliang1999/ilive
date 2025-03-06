package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.entity.LocationStatisticResult;

public interface LocationStatisticResultDao {

	public List<LocationStatisticResult> listByFlag(Long flagId, Integer flagType, Date startTime, Date endTime);

	public List<LocationStatisticResult> listStatisticResutlForLiveEventView(Long liveEventId);

	public List<LocationStatisticResult> listStatisticResutlForLiveEventViewGroupByCity(Long liveEventId);

	public List<LocationStatisticResult> listStatisticResutlForEnterpriseBeginLive(Long enterpriseId, Date startTime,
			Date endTime);
	public List<LocationStatisticResult> listStatisticResutlForEnterpriseLiveView(Long enterpriseId, Date startTime,
			Date endTime);

	public List<LocationStatisticResult> listStatisticResutlForVideo(Long videoId, Date startTime, Date endTime);

	public List<LocationStatisticResult> listStatisticResutlForTotalView(Date startTime, Date endTime);

	public List<LocationStatisticResult> listStatisticResutlForTotalBeginLive(Date startTime, Date endTime);

	public LocationStatisticResult getBeanByProvinceName(String provinceName, Long flagId, Integer flagType,
			Date startTime, Date endTime);

	public LocationStatisticResult save(LocationStatisticResult bean);

	public LocationStatisticResult updateByUpdater(Updater<LocationStatisticResult> updater);

}