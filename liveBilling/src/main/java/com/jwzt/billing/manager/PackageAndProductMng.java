package com.jwzt.billing.manager;

import java.util.List;

import com.jwzt.billing.entity.PackageAndProduct;

/**
 * @author ysf
 */
public interface PackageAndProductMng {
	List<PackageAndProduct> listByPackage(Integer packageId);

	PackageAndProduct save(Integer packageId, Integer productId);
}
