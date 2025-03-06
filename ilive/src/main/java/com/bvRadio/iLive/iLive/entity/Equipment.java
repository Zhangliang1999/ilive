package com.bvRadio.iLive.iLive.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 设备
 * @author Wei
 *
 */
public class Equipment {

	/**
	 * 设备id
	 */
	private Long id;
	
	/**
	 * 设备名称
	 */
	private String name;
	
	/**
	 * 设备图片
	 */
	private String img;
	
	/**
	 * 设备描述
	 */
	private String descript;
	
	/**
	 * 设备价格
	 */
	private BigDecimal price;
	
	/**
	 * 租或卖    1租   2卖
	 */
	private Integer rentOrSell;
	
	/**
	 * 租的价格
	 */
	private BigDecimal rentPrice;
	
	/**
	 * 租的单位
	 */
	private Integer rentUnit;
	
	/**
	 * 租的库存
	 */
	private Integer rentInventory;
	
	/**
	 * 卖的库存
	 */
	private Integer sellInventory;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	/**
	 * 更新时间
	 */
	private Timestamp updateTime;
	
	/**
	 * 用户id
	 */
	private Long userId;
	
	/**
	 * 是否上架   0上架  1下架
	 */
	private Integer isShangjia;
	
	/**
	 * 库存
	 */
	private Integer number;
	
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getIsShangjia() {
		return isShangjia;
	}

	public void setIsShangjia(Integer isShangjia) {
		this.isShangjia = isShangjia;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getRentOrSell() {
		return rentOrSell;
	}

	public void setRentOrSell(Integer rentOrSell) {
		this.rentOrSell = rentOrSell;
	}

	public BigDecimal getRentPrice() {
		return rentPrice;
	}

	public void setRentPrice(BigDecimal rentPrice) {
		this.rentPrice = rentPrice;
	}

	public Integer getRentUnit() {
		return rentUnit;
	}

	public void setRentUnit(Integer rentUnit) {
		this.rentUnit = rentUnit;
	}

	public Integer getRentInventory() {
		return rentInventory;
	}

	public void setRentInventory(Integer rentInventory) {
		this.rentInventory = rentInventory;
	}

	public Integer getSellInventory() {
		return sellInventory;
	}

	public void setSellInventory(Integer sellInventory) {
		this.sellInventory = sellInventory;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}
