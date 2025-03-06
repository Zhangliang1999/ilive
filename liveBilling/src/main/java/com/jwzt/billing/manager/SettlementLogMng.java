package com.jwzt.billing.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.SettlementLog;
import com.jwzt.billing.exception.ErrorPlatformRateException;
import com.jwzt.billing.exception.FlowNotFoundException;
import com.jwzt.billing.exception.FlowSettlementStatusErrorException;
import com.jwzt.billing.exception.SettlementLogNotFoundException;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface SettlementLogMng {
	Pagination pageByParams(Integer enterpriseId, String enterpriseName, Integer[] status, Integer invoiceStatus,
			Date startTime, Date endTime, int pageNo, int pageSize);

	List<SettlementLog> listByParams(Integer enterpriseId, String enterpriseName, Integer[] status,
			Integer invoiceStatus, Date startTime, Date endTime);

	SettlementLog getBeanById(Long id);

	SettlementLog save(Integer enterpriseId, String enterpriseName, Long[] revenueFlowIds, Double platformRate)
			throws ErrorPlatformRateException, FlowNotFoundException, FlowSettlementStatusErrorException, Exception;

	SettlementLog cancelById(Long id) throws SettlementLogNotFoundException, Exception;
	
	SettlementLog updateInvoiceStatus(Long id,Integer invoiceStatus);
}
