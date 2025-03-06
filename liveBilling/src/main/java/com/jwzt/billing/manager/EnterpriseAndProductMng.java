package com.jwzt.billing.manager;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.EnterpriseAndProduct;
import com.jwzt.billing.entity.vo.EnterpriseProductInfo;

/**
 * @author ysf
 */
public interface EnterpriseAndProductMng {
	EnterpriseProductInfo getEnterpriseProductInfo(Integer enterpriseId);

	List<EnterpriseAndProduct> listByEnterprise(Integer enterpriseId, Date vaildCheckTime);

	EnterpriseAndProduct save(EnterpriseAndProduct bean);

	EnterpriseAndProduct update(EnterpriseAndProduct bean);

	void insertOrUpgradePackage(Integer enterpriseId, Integer packageId, boolean continueInsert,
			boolean ignoreStatus,Timestamp startTime);

}
