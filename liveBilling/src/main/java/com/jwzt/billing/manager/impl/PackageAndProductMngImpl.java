package com.jwzt.billing.manager.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.dao.PackageAndProductDao;
import com.jwzt.billing.entity.PackageAndProduct;
import com.jwzt.billing.manager.PackageAndProductMng;

/**
 * @author ysf
 */
@Service
public class PackageAndProductMngImpl implements PackageAndProductMng {

	@Override
	@Transactional(readOnly = true)
	public List<PackageAndProduct> listByPackage(Integer packageId) {
		return dao.listByParams(packageId, null);
	}

	@Override
	@Transactional
	public PackageAndProduct save(Integer packageId, Integer productId) {
		PackageAndProduct bean = null;
		if (null != packageId && null != productId) {
			bean = new PackageAndProduct();
			String id = UUID.randomUUID().toString();
			bean.setId(id);
			bean.setPackageId(packageId);
			bean.setProductId(productId);
			bean.initFieldValue();
			dao.save(bean);
		}
		return bean;
	}

	@Autowired
	private PackageAndProductDao dao;

}
