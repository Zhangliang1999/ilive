package com.jwzt.billing.entity;
/**
* @author ysf
*/

import java.sql.Timestamp;

import com.jwzt.billing.entity.base.BaseProductInfo;

@SuppressWarnings("serial")
public class ProductInfo extends BaseProductInfo {
	/**
	 * 产品类型 基础产品
	 */
	public final static Integer PRODUCT_TYPE_BASIC = 1;
	/**
	 * 产品类型 存储/G
	 */
	public final static Integer PRODUCT_TYPE_STORAGE = 2;
	/**
	 * 产品类型 短信/条
	 */
	public final static Integer PRODUCT_TYPE_SHORT_MESSAGE = 3;
	/**
	 * 产品类型 时长/小时
	 */
	public final static Integer PRODUCT_TYPE_DURATION = 4;
	/**
	 * 产品类型 并发/个
	 */
	public final static Integer PRODUCT_TYPE_CONCURRENCE = 5;
	/**
	 * 产品类型 子账户个数/个
	 */
	public final static Integer PRODUCT_TYPE_SUBCOUNT = 6;
	public Long getIncrementShowValue() {
		if (ProductInfo.PRODUCT_TYPE_DURATION.equals(getProductType())) {
			// 小时 转 秒
			return getIncrementValue() / 3600;
		} else if (ProductInfo.PRODUCT_TYPE_STORAGE.equals(getProductType())) {
			// G 转 KB
			return getIncrementValue() / 1024 / 1024;
		}
		return getIncrementValue();
	}

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public ProductInfo() {
		super();
	}

}
