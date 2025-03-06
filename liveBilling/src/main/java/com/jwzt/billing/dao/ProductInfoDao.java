package com.jwzt.billing.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.ProductInfo;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface ProductInfoDao {
	Pagination pageByParams(String productName, Integer[] productTypes, Date startTime, Date endTime, int pageNo,
			int pageSize);

	List<ProductInfo> listByParams(String productName, Integer[] productTypes, Date startTime, Date endTime);

	ProductInfo getBeanById(Integer id);

	ProductInfo save(ProductInfo bean);

	ProductInfo updateByUpdater(Updater<ProductInfo> updater);
}
