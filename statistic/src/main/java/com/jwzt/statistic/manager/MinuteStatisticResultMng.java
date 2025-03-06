package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.statistic.entity.MinuteStatisticResult;

public interface MinuteStatisticResultMng {
	public MinuteStatisticResult getBeanByParams(Long liveEventId, Date startTime, Date endTime);

	public MinuteStatisticResult getBeanWithMaxMinuteUserNum(Long liveEventId, Date startTime, Date endTime);

	public List<MinuteStatisticResult> listByLiveEventId(Long liveEventId);

	public MinuteStatisticResult save(MinuteStatisticResult bean);

	public void update(MinuteStatisticResult bean);
	
	public MinuteStatisticResult getBeanWithMaxMinuteUserNumByEnterpriseId(Long enterpriseId, Date startTime, Date endTime);
}