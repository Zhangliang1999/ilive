package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.entity.EnterpriseInfo;

public interface EnterpriseInfoMng {

	public Pagination pageByParams(Integer enterpriseId, String enterpriseName, Date startOfCreateTime,
			Date endOfCreateTime, Date startOfStatisticTime, Date endOfStatisticTime, boolean initStatisticData,
			int pageNo, int pageSize,Integer certStatus, Integer entype, String bindPhone,Integer stamp);

	public List<EnterpriseInfo> listByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime,
			boolean initStatisticData);

	public EnterpriseInfo getBeanById(Integer id);

	public EnterpriseInfo save(EnterpriseInfo bean);

	public EnterpriseInfo update(EnterpriseInfo bean);

	public EnterpriseInfo saveOrUpdateFromDataMap(final Map<?, ?> dataMap);
}