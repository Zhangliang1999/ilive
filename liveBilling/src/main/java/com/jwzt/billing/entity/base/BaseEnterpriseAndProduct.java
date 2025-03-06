package com.jwzt.billing.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseEnterpriseAndProduct implements java.io.Serializable {

	/**
	 * ID
	 */
	private String id;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 企业id
	 */
	private Integer enterpriseId;
	/**
	 * 套餐id
	 */
	private Integer packageId;
	/**
	 * 产品id
	 */
	private Integer productId;
	/**
	 * 有效开始时间
	 */
	private Timestamp vaildStartTime;
	/**
	 * 有效结束时间
	 */
	private Timestamp vaildEndTime;
	/**
	 * 已消耗值
	 */
	private Long usedValue;

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

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
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

	public Timestamp getVaildStartTime() {
		return vaildStartTime;
	}

	public void setVaildStartTime(Timestamp vaildStartTime) {
		this.vaildStartTime = vaildStartTime;
	}

	public Timestamp getVaildEndTime() {
		return vaildEndTime;
	}

	public void setVaildEndTime(Timestamp vaildEndTime) {
		this.vaildEndTime = vaildEndTime;
	}

	public Long getUsedValue() {
		return usedValue;
	}

	public void setUsedValue(Long usedValue) {
		this.usedValue = usedValue;
	}
}
