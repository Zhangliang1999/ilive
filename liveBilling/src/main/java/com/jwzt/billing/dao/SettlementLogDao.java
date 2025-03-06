package com.jwzt.billing.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.SettlementLog;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface SettlementLogDao {
	Pagination pageByParams(Integer enterpriseId, String enterpriseName, Integer[] status, Integer invoiceStatus,
			Date startTime, Date endTime, int pageNo, int pageSize);

	List<SettlementLog> listByParams(Integer enterpriseId, String enterpriseName, Integer[] status,
			Integer invoiceStatus, Date startTime, Date endTime);

	SettlementLog getBeanById(Long id);

	SettlementLog save(SettlementLog bean);

	SettlementLog updateByUpdater(Updater<SettlementLog> updater);
}
