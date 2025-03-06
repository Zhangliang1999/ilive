package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.entity.EnterpriseInfo;

public interface EnterpriseInfoDao {
	public Pagination pageByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime,
			int pageNo, int pageSize,Integer certStatus, Integer entype, String bindPhone,Integer stamp);

	public List<EnterpriseInfo> listByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime);

	public EnterpriseInfo getBeanById(Integer id);

	public EnterpriseInfo save(EnterpriseInfo bean);

	public EnterpriseInfo updateByUpdater(Updater<EnterpriseInfo> updater);
}