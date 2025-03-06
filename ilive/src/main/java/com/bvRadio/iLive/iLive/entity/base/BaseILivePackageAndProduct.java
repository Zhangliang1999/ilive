package com.bvRadio.iLive.iLive.entity.base;

public abstract class BaseILivePackageAndProduct {
/**
 * 主键id
 */
	private Long id;
	/**
	 * 领航产品Id
	 */
	private String LHProductId;
	/**
	 * 购买领航套餐类型
	 */
	private Integer LHpackageId;
	/**
	 * 平台对应的套餐id
	 */
	private Integer plapackageId;
	/**
	 * 领航购买叠加包类型
	 */
	private Integer ProductId;
	/**
	 * 平台对应的叠加包Id
	 */
	private Integer plaProductId;
	/**
	 * 价格
	 * @return
	 */
	 private Double price;
	 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLHProductId() {
		return LHProductId;
	}
	public void setLHProductId(String lHProductId) {
		LHProductId = lHProductId;
	}
	public Integer getLHpackageId() {
		return LHpackageId;
	}
	public void setLHpackageId(Integer lHpackageId) {
		LHpackageId = lHpackageId;
	}
	public Integer getPlapackageId() {
		return plapackageId;
	}
	public void setPlapackageId(Integer plapackageId) {
		this.plapackageId = plapackageId;
	}
	public Integer getProductId() {
		return ProductId;
	}
	public void setProductId(Integer productId) {
		ProductId = productId;
	}
	public Integer getPlaProductId() {
		return plaProductId;
	}
	public void setPlaProductId(Integer plaProductId) {
		this.plaProductId = plaProductId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
}
