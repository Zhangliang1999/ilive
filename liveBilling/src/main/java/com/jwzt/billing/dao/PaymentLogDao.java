package com.jwzt.billing.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.PaymentLog;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface PaymentLogDao {
	Pagination pageByParams(String queryContent, Integer enterpriseId, Integer paymentType, Integer status, Integer channelType,
			Date startTime, Date endTime, int pageNo, int pageSize);

	List<PaymentLog> listByParams(Integer enterpriseId, Integer paymentType, Integer status, Integer channelType,
			Date startTime, Date endTime);

	PaymentLog getBeanById(Long id);

	PaymentLog save(PaymentLog bean);

	PaymentLog update(PaymentLog bean);

	PaymentLog updateByUpdater(Updater<PaymentLog> updater);

	List<PaymentLog> listByParamsByAuto(Integer paymentAuto);
}
