package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.statistic.entity.EnterpriseStatisticResult;

public interface EnterpriseStatisticResultMng {

	public EnterpriseStatisticResult getLastBeanByEndTime(Integer enterpriseId, Date endTime);

	public List<EnterpriseStatisticResult> listByParams(Integer enterpriseId, Date startTime, Date endTime);

	public EnterpriseStatisticResult sumByParams(Integer enterpriseId, Date startTime, Date endTime);

	public EnterpriseStatisticResult getBeanById(Long id);

	public EnterpriseStatisticResult save(EnterpriseStatisticResult bean);

	public void update(EnterpriseStatisticResult bean);
	
	public EnterpriseStatisticResult sumByLiveMostEnterprise();

	public int enterpriseByMost();
}