package com.jwzt.billing.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.WorkOrder;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface WorkOrderDao {
	Pagination pageByParams(Integer enterpriseId, Integer workOrderType, Integer status, Date startTime, Date endTime,
			int pageNo, int pageSize);

	List<WorkOrder> listByParams(Integer enterpriseId, Integer workOrderType, Integer status, Date startTime,
			Date endTime);

	WorkOrder getBeanById(Long id);

	WorkOrder save(WorkOrder bean);

	WorkOrder update(WorkOrder bean);

	WorkOrder updateByUpdater(Updater<WorkOrder> updater);
}
