package com.jwzt.billing.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.WorkOrder;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface WorkOrderMng {
	Pagination pageByParams(Integer enterpriseId, Integer paymentType, Integer status, Date startTime, Date endTime,
			int pageNo, int pageSize, boolean initBean);

	List<WorkOrder> listByParams(Integer enterpriseId, Integer paymentType, Integer status, Date startTime,
			Date endTime, boolean initBean);

	WorkOrder getBeanById(Long id, boolean initBean);

	WorkOrder save(WorkOrder bean);

	WorkOrder update(WorkOrder bean);

	WorkOrder processById(Long id);

	WorkOrder completeById(Long id);

	WorkOrder cancelById(Long id, String cancelReason);
}
