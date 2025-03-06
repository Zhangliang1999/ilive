package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.entity.MinuteStatisticResult;

public interface MinuteStatisticResultDao {

	public MinuteStatisticResult getBeanByParams(Long liveEventId, Date startTime, Date endTime);

	public MinuteStatisticResult getBeanWithMaxMinuteUserNum(Long liveEventId, Date startTime, Date endTime);

	public List<MinuteStatisticResult> listByLiveEventId(Long liveEventId);

	public MinuteStatisticResult save(MinuteStatisticResult bean);

	public MinuteStatisticResult updateByUpdater(Updater<MinuteStatisticResult> updater);
	
	public MinuteStatisticResult getBeanWithMaxMinuteUserNumByEnterpriseId(Long enterpriseId,Date startTime,Date endTime);

}