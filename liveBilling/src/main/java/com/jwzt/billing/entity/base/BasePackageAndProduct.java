package com.jwzt.billing.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BasePackageAndProduct implements java.io.Serializable {

	/**
	 * ID
	 */
	private String id;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 套餐id
	 */
	private Integer packageId;
	/**
	 * 产品id
	 */
	private Integer productId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

}
