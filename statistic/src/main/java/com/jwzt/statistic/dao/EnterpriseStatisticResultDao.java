package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.entity.EnterpriseStatisticResult;

public interface EnterpriseStatisticResultDao {
	public EnterpriseStatisticResult getLastBeanByEndTime(Integer enterpriseId, Date endTime);

	public List<EnterpriseStatisticResult> listByParams(Integer enterpriseId, Date startTime, Date endTime);

	public EnterpriseStatisticResult sumByParams(Integer enterpriseId, Date startTime, Date endTime);

	public EnterpriseStatisticResult getBeanById(Long id);

	public EnterpriseStatisticResult save(EnterpriseStatisticResult bean);

	public EnterpriseStatisticResult updateByUpdater(Updater<EnterpriseStatisticResult> updater);

	public EnterpriseStatisticResult sumByLiveMostEnterprise();

	public int enterpriseByMost();
}