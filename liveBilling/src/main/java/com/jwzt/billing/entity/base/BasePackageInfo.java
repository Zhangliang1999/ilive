package com.jwzt.billing.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BasePackageInfo implements java.io.Serializable {

	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 套餐名称
	 */
	private String packageName;
	/**
	 * 套餐类型
	 */
	private Integer packageType;
	/**
	 * 原价
	 */
	private Double orginalPrice;
	/**
	 * 成本价
	 */
	private Double costPrice;
	/**
	 * 有效时长的单位 年月日
	 */
	private Integer vaildDurationUnit;
	/**
	 * 有效时长的值
	 */
	private Integer vaildDurationValue;
	/**
	 * 封面图
	 */
	private String introduceImagePath;
	/**
	 * 介绍内容
	 */
	private String introduceContent;
	/**
	 * 介绍地址
	 */
	private String introduceUrl;
	/**
	 * 投放渠道类型
	 */
	private String channelTypes;
	/**
	 * 平台销售价格
	 */
	private Double officialPlatformPrice;
	/**
	 * 渠道扣率基准价
	 */
	private Double channelAgentPrice;
	/**
	 * 产品库销售价格
	 */
	private Double groupProductLibraryPrice;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 排序标识
	 */
	private Integer orderNum;

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

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Integer getPackageType() {
		return packageType;
	}

	public void setPackageType(Integer packageType) {
		this.packageType = packageType;
	}

	public Double getOrginalPrice() {
		return orginalPrice;
	}

	public void setOrginalPrice(Double orginalPrice) {
		this.orginalPrice = orginalPrice;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Integer getVaildDurationUnit() {
		return vaildDurationUnit;
	}

	public void setVaildDurationUnit(Integer vaildDurationUnit) {
		this.vaildDurationUnit = vaildDurationUnit;
	}

	public Integer getVaildDurationValue() {
		return vaildDurationValue;
	}

	public void setVaildDurationValue(Integer vaildDurationValue) {
		this.vaildDurationValue = vaildDurationValue;
	}

	public String getIntroduceImagePath() {
		return introduceImagePath;
	}

	public void setIntroduceImagePath(String introduceImagePath) {
		this.introduceImagePath = introduceImagePath;
	}

	public String getIntroduceContent() {
		return introduceContent;
	}

	public void setIntroduceContent(String introduceContent) {
		this.introduceContent = introduceContent;
	}

	public String getIntroduceUrl() {
		return introduceUrl;
	}

	public void setIntroduceUrl(String introduceUrl) {
		this.introduceUrl = introduceUrl;
	}

	public String getChannelTypes() {
		return channelTypes;
	}

	public void setChannelTypes(String channelTypes) {
		this.channelTypes = channelTypes;
	}

	public Double getOfficialPlatformPrice() {
		return officialPlatformPrice;
	}

	public void setOfficialPlatformPrice(Double officialPlatformPrice) {
		this.officialPlatformPrice = officialPlatformPrice;
	}

	public Double getChannelAgentPrice() {
		return channelAgentPrice;
	}

	public void setChannelAgentPrice(Double channelAgentPrice) {
		this.channelAgentPrice = channelAgentPrice;
	}

	public Double getGroupProductLibraryPrice() {
		return groupProductLibraryPrice;
	}

	public void setGroupProductLibraryPrice(Double groupProductLibraryPrice) {
		this.groupProductLibraryPrice = groupProductLibraryPrice;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public String toString() {
		return "id=" + id + ", createTime=" + createTime + ", packageName=" + packageName + ", packageType="
				+ packageType + ", orginalPrice=" + orginalPrice + ", costPrice=" + costPrice + ", vaildDurationUnit="
				+ vaildDurationUnit + ", vaildDurationValue=" + vaildDurationValue + ", introduceImagePath="
				+ introduceImagePath + ", introduceContent=" + introduceContent + ", introduceUrl=" + introduceUrl
				+ ", channelTypes=" + channelTypes + ", officialPlatformPrice=" + officialPlatformPrice
				+ ", channelAgentPrice=" + channelAgentPrice + ", groupProductLibraryPrice=" + groupProductLibraryPrice
				+ ", status=" + status+ ", orderNum=" + orderNum;
	}

}
