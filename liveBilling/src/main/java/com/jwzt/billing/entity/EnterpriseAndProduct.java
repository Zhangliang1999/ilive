package com.jwzt.billing.entity;
/**
* @author ysf
*/

import java.sql.Timestamp;

import com.jwzt.billing.entity.base.BaseEnterpriseAndProduct;

@SuppressWarnings("serial")
public class EnterpriseAndProduct extends BaseEnterpriseAndProduct {

	private ProductInfo product;

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getVaildStartTime()) {
			setVaildStartTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getUsedValue()) {
			setUsedValue(0L);
		}
	}

	public EnterpriseAndProduct() {
		super();
	}

	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

}
