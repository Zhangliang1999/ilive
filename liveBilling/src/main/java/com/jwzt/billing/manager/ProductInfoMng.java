package com.jwzt.billing.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.ProductInfo;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface ProductInfoMng {
	Pagination pageByParams(String productName, Integer[] productTypes, Date startTime, Date endTime, int pageNo,
			int pageSize, boolean initBean);

	List<ProductInfo> listByParams(String productName, Integer[] productTypes, Date startTime, Date endTime,
			boolean initBean);

	ProductInfo getBeanById(Integer id, boolean initBean);

	ProductInfo save(ProductInfo bean);

	ProductInfo update(ProductInfo bean);
}
