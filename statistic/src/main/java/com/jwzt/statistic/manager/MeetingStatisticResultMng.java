package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.statistic.entity.MeetingStatisticResult;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;
import com.jwzt.statistic.entity.vo.MeetingHoursResult;
import com.jwzt.statistic.entity.vo.MeetingStasticShowResult;

public interface MeetingStatisticResultMng {

	public MeetingStatisticResult getLastBeanByEndTime(Date endTime);

	public List<MeetingStatisticResult> listByParams(Date startTime, Date endTime);

	public List<MeetingEnterpriseResult> top10();
	public MeetingEnterpriseResult top10ByIndex(Integer index);
	public MeetingStasticShowResult sumByParams();

	public MeetingStatisticResult getBeanById(Long id);
	public Long getBeanByMeetingTime(Long minNum,Long maxNum);
	public MeetingStatisticResult save(MeetingStatisticResult bean);

	public void update(MeetingStatisticResult bean);
	
	public Long getBeanByRecordOrRedirect(Integer record,Integer redirect);
	public Long getBeanByEnterpriseId(Long enterpriseId);

	public Long getBeanByFigure(Integer figure);
	
	
}