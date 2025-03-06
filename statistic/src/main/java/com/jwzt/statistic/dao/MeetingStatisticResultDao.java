package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.entity.MeetingStatisticResult;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;
import com.jwzt.statistic.entity.vo.MeetingHoursResult;
import com.jwzt.statistic.entity.vo.MeetingStasticShowResult;

public interface MeetingStatisticResultDao {
	public MeetingStasticShowResult sumByParams();

	public List<MeetingStatisticResult> listByParams(Date startTime, Date endTime);

	public MeetingStatisticResult getLastBeanByEndTime(Date endTime);

	public MeetingStatisticResult getBeanById(Long id);

	public MeetingStatisticResult save(MeetingStatisticResult bean);

	public MeetingStatisticResult updateByUpdater(Updater<MeetingStatisticResult> updater);

	public Long getBeanByMeetingTime(Long minNum, Long maxNum);

	public List<MeetingEnterpriseResult> top10();

	public Long getBeanByRecordOrRedirect(Integer record, Integer redirect);

	public MeetingEnterpriseResult top10ByIndex(Integer index);

	public Long getBeanByEnterpriseId(Long enterpriseId);

	public Long getBeanByFigure(Integer figure);
}