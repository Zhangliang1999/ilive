package com.jwzt.billing.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseProductInfo implements java.io.Serializable {

	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 产品名称
	 */
	private String productName;
	/**
	 * 产品类型 1：基础产品，2：存储/MB，3：短信/条，4：时长/分，5：并发/个
	 */
	private Integer productType;
	/**
	 * 产品价格
	 */
	private Double productPrice;
	/**
	 * 功能codes， “,”分隔
	 */
	private String functionCodes;
	/**
	 * 增量值
	 */
	private Long incrementValue;
	/**
	 * 产品说明
	 */
	private String productDesc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public String getFunctionCodes() {
		return functionCodes;
	}

	public void setFunctionCodes(String functionCodes) {
		this.functionCodes = functionCodes;
	}

	public Long getIncrementValue() {
		return incrementValue;
	}

	public void setIncrementValue(Long incrementValue) {
		this.incrementValue = incrementValue;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

}
