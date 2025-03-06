package com.jwzt.billing.dao;
/**
* @author ysf
*/

import java.util.List;

import com.jwzt.billing.entity.PackageAndProduct;

public interface PackageAndProductDao {
	List<PackageAndProduct> listByParams(Integer packageId, Integer productId);

	PackageAndProduct save(PackageAndProduct bean);

}
