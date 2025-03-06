package com.jwzt.billing.dao;
/**
* @author ysf
*/

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.EnterpriseAndProduct;

public interface EnterpriseAndProductDao {
	List<EnterpriseAndProduct> listByParams(Integer enterpriseId, Integer productId, Date vaildCheckTime);

	EnterpriseAndProduct save(EnterpriseAndProduct bean);

	EnterpriseAndProduct update(EnterpriseAndProduct bean);

}
