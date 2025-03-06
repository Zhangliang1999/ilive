package com.jwzt.billing.entity;

import java.sql.Timestamp;

import com.jwzt.billing.entity.base.BasePackageAndProduct;

/**
 * @author ysf
 */

@SuppressWarnings("serial")
public class PackageAndProduct extends BasePackageAndProduct {
	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public PackageAndProduct() {
		super();
	}

}
