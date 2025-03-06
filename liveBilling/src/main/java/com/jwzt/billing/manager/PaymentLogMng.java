package com.jwzt.billing.manager;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.PaymentLog;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface PaymentLogMng {
	Pagination pageByParams(String queryContent, Integer enterpriseId, Integer paymentType, Integer status, Integer channelType,
			Date startTime, Date endTime, int pageNo, int pageSize, boolean initBean);

	List<PaymentLog> listByParams(Integer enterpriseId, Integer paymentType, Integer status, Integer channelType,
			Date startTime, Date endTime, boolean initBean);

	PaymentLog getBeanById(Long id, boolean initBean);

	PaymentLog save(PaymentLog bean);
	PaymentLog update(PaymentLog bean);
	PaymentLog update(Long id, Integer agentId, Integer sellUserId, Boolean paid, Integer paymentWay,
			String paymentFlowId, Timestamp paymentTime, Double paymentPrice, Double agentDeductionRate,String paymentDesc,Integer paymentAuto);

	PaymentLog processById(Long id);

	PaymentLog completeById(Long id,Integer status, Timestamp startTime);

	PaymentLog cancelById(Long id, String cancelReason);

	PaymentLog yytcompleteById(PaymentLog bean, Integer certStatus);
	List<PaymentLog> listByParamsByAuto(Integer paymentAuto);

	PaymentLog completeByAuto(PaymentLog bean, Integer certStatus, Timestamp startTime);
}
