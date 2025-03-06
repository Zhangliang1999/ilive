package com.jwzt.billing.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseEnterpriseBilling implements java.io.Serializable {

	/**
	 * 企业ID
	 */
	private Integer enterpriseId;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 更新时间
	 */
	private Timestamp updateTime;
	/**
	 * 企业名
	 */
	private String enterpriseName;
	/**
	 * 企业图片
	 */
	private String enterpriseImg;
	/**
	 * 结算金额
	 */
	private Double settleAmount;
	/**
	 * 累计金额
	 */
	private Double totalAmount;
	/**
	 * 平台金额
	 */
	private Double platformAmount;
	/**
	 * 注册时间
	 * 
	 */
	private Timestamp applyTime;
	/**
	 * 认证时间
	 * 
	 */
	private Timestamp certTime;
	/**
	 * 认证状态
	 */
	private Integer certStatus;
	/**
	 * 开通收益账户
	 */
	private Boolean openRevenueAccount;
	/**
	 * 开通红包账户
	 */
	private Boolean openRedPackageAccount;
	/**
	 * 开通收益账户工单ID
	 */
	private Long revenueAccountWorkOrderId;
	/**
	 * 开通红包账户工单ID
	 */
	private Long redPackageAccountWorkOrderId;
    /**
     * 注册手机号
     */
	private String userPhone;
	public BaseEnterpriseBilling() {
		super();
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
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

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getEnterpriseImg() {
		return enterpriseImg;
	}

	public void setEnterpriseImg(String enterpriseImg) {
		this.enterpriseImg = enterpriseImg;
	}

	public Double getSettleAmount() {
		return settleAmount;
	}

	public void setSettleAmount(Double settleAmount) {
		this.settleAmount = settleAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getPlatformAmount() {
		return platformAmount;
	}

	public void setPlatformAmount(Double platformAmount) {
		this.platformAmount = platformAmount;
	}

	public Timestamp getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}

	public Timestamp getCertTime() {
		return certTime;
	}

	public void setCertTime(Timestamp certTime) {
		this.certTime = certTime;
	}

	public Integer getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(Integer certStatus) {
		this.certStatus = certStatus;
	}

	public Boolean getOpenRevenueAccount() {
		return openRevenueAccount;
	}

	public void setOpenRevenueAccount(Boolean openRevenueAccount) {
		this.openRevenueAccount = openRevenueAccount;
	}

	public Boolean getOpenRedPackageAccount() {
		return openRedPackageAccount;
	}

	public void setOpenRedPackageAccount(Boolean openRedPackageAccount) {
		this.openRedPackageAccount = openRedPackageAccount;
	}

	public Long getRevenueAccountWorkOrderId() {
		return revenueAccountWorkOrderId;
	}

	public void setRevenueAccountWorkOrderId(Long revenueAccountWorkOrderId) {
		this.revenueAccountWorkOrderId = revenueAccountWorkOrderId;
	}

	public Long getRedPackageAccountWorkOrderId() {
		return redPackageAccountWorkOrderId;
	}

	public void setRedPackageAccountWorkOrderId(Long redPackageAccountWorkOrderId) {
		this.redPackageAccountWorkOrderId = redPackageAccountWorkOrderId;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

}
