package com.jwzt.billing.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.EnterpriseBilling;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface EnterpriseBillingDao {
	Pagination pageByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime, int pageNo,
			int pageSize);

	List<EnterpriseBilling> listByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime);

	EnterpriseBilling sumTotal();

	EnterpriseBilling getBeanById(Integer id);

	EnterpriseBilling save(EnterpriseBilling bean);

	EnterpriseBilling updateByUpdater(Updater<EnterpriseBilling> updater);

}
