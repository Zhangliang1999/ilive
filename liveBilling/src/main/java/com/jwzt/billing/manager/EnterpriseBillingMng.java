package com.jwzt.billing.manager;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jwzt.billing.entity.EnterpriseBilling;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface EnterpriseBillingMng {
	Pagination pageByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime, int pageNo,
			int pageSize, boolean initBean);

	List<EnterpriseBilling> listByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime,
			boolean initBean);

	EnterpriseBilling sumTotal();

	EnterpriseBilling getBeanById(Integer id, boolean initBean);

	EnterpriseBilling save(EnterpriseBilling bean);

	EnterpriseBilling update(Integer enterpriseId,String enterpriseName, Double settleAmount, Double totalAmount, Double platformAmount,Timestamp applyTime,Timestamp certTime, String userPhone);

	EnterpriseBilling updateRevenueAccount(Integer enterpriseId, Boolean openRevenueAccount,
			Long revenueAccountWorkOrderId);

	EnterpriseBilling updateRedPackageAccount(Integer enterpriseId, Boolean openRedPackageAccount,
			Long redPackageAccountWorkOrderId);

	EnterpriseBilling saveOrUpdateFromDataMap(final Map<?, ?> dataMap);

	
}
