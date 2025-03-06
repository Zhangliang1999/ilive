package com.jwzt.billing.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseSettlementLog implements java.io.Serializable {
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 企业ID
	 */
	private Integer enterpriseId;
	/**
	 * 企业名称
	 */
	private String enterpriseName;
	/**
	 * 结算状态 1：已结算，2：作废
	 */
	private Integer status;
	/**
	 * 结算金额
	 */
	private Double settleAmount;
	/**
	 * 实际金额
	 */
	private Double actualAmount;
	/**
	 * 平台金额
	 */
	private Double platformAmount;
	/**
	 * 发票状态 0：未开，1：已开
	 */
	private Integer invoiceStatus;
	/**
	 * 转账账户
	 */
	private String transferAccount;
	/**
	 * 转账ID
	 */
	private String transferId;

	public BaseSettlementLog() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getSettleAmount() {
		return settleAmount;
	}

	public void setSettleAmount(Double settleAmount) {
		this.settleAmount = settleAmount;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}

	public Double getPlatformAmount() {
		return platformAmount;
	}

	public void setPlatformAmount(Double platformAmount) {
		this.platformAmount = platformAmount;
	}

	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getTransferAccount() {
		return transferAccount;
	}

	public void setTransferAccount(String transferAccount) {
		this.transferAccount = transferAccount;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

}
