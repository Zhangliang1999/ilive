package com.jwzt.billing.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BasePaymentLog implements java.io.Serializable {

	/**
	 * ID
	 */
	private Long id;
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
	 * 有效时间
	 */
	private Timestamp vaildTime;
	/**
	 * 订购类型
	 */
	private Integer paymentType;
	/**
	 * 渠道类型
	 */
	private Integer channelType;
	/**
	 * 原订单填写
	 */
	private String originalPayment;
	/**
	 * 支付标记
	 */
	private Boolean paid;
	/**
	 * 支付方式
	 */
	private Integer paymentWay;
	/**
	 * 流水ID
	 */
	private String paymentFlowId;
	/**
	 * 支付金额
	 */
	private Double paymentPrice;
	/**
	 * 代理商id
	 */
	private Integer agentId;
	/**
	 * 代理扣率
	 */
	private Double agentDeductionRate;
	/**
	 * 支付时间
	 */
	private Timestamp paymentTime;
	/**
	 * 备注
	 */
	private String paymentDesc;

	/**
	 * 是否已退款
	 */
	private Boolean refunded;
	/**
	 * 退款金额
	 */
	private Double refundAmount;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 联系人
	 */
	private String contactName;
	/**
	 * 联系电话
	 */
	private String contactNumber;
	/**
	 * 销售用户id
	 */
	private Long sellUserId;
	/**
	 * 取消原因
	 */
	private String cancelReason;
	/**
	 * 营业厅CustID
	 */
	private String CustID;
	/**
	 * 套餐生效日期
	 * @return
	 */
	private Timestamp vaildStartTime;
	/**
	 * 套餐结束日期
	 * @return
	 */
	private Timestamp vaildEndTime;
	/**
	 * 是否续订
	 * @return
	 */
	private Integer paymentAuto;
	/**
	 * 折扣率
	 */
	private Double DiscountRate;
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

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Timestamp getVaildTime() {
		return vaildTime;
	}

	public void setVaildTime(Timestamp vaildTime) {
		this.vaildTime = vaildTime;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getChannelType() {
		return channelType;
	}

	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}

	public String getOriginalPayment() {
		return originalPayment;
	}

	public void setOriginalPayment(String originalPayment) {
		this.originalPayment = originalPayment;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public Integer getPaymentWay() {
		return paymentWay;
	}

	public void setPaymentWay(Integer paymentWay) {
		this.paymentWay = paymentWay;
	}

	public String getPaymentFlowId() {
		return paymentFlowId;
	}

	public void setPaymentFlowId(String paymentFlowId) {
		this.paymentFlowId = paymentFlowId;
	}

	public Double getPaymentPrice() {
		return paymentPrice;
	}

	public void setPaymentPrice(Double paymentPrice) {
		this.paymentPrice = paymentPrice;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public Double getAgentDeductionRate() {
		return agentDeductionRate;
	}

	public void setAgentDeductionRate(Double agentDeductionRate) {
		this.agentDeductionRate = agentDeductionRate;
	}

	public Timestamp getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Timestamp paymentTime) {
		this.paymentTime = paymentTime;
	}

	public String getPaymentDesc() {
		return paymentDesc;
	}

	public void setPaymentDesc(String paymentDesc) {
		this.paymentDesc = paymentDesc;
	}

	public Boolean getRefunded() {
		return refunded;
	}

	public void setRefunded(Boolean refunded) {
		this.refunded = refunded;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Long getSellUserId() {
		return sellUserId;
	}

	public void setSellUserId(Long sellUserId) {
		this.sellUserId = sellUserId;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getCustID() {
		return CustID;
	}

	public void setCustID(String custID) {
		CustID = custID;
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

	public Integer getPaymentAuto() {
		return paymentAuto;
	}

	public void setPaymentAuto(Integer paymentAuto) {
		this.paymentAuto = paymentAuto;
	}

	public Double getDiscountRate() {
		return DiscountRate;
	}

	public void setDiscountRate(Double discountRate) {
		DiscountRate = discountRate;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", createTime=" + createTime + ", enterpriseId=" + enterpriseId + ", packageId="
				+ packageId + ", vaildTime=" + vaildTime + ", paymentType=" + paymentType + ", channelType="
				+ channelType + ", originalPayment=" + originalPayment + ", paid=" + paid + ", paymentWay=" + paymentWay
				+ ", paymentFlowId=" + paymentFlowId + ", paymentPrice=" + paymentPrice + ", agentId=" + agentId
				+ ", agentDeductionRate=" + agentDeductionRate + ", paymentTime=" + paymentTime + ", paymentDesc="
				+ paymentDesc + ", refunded=" + refunded + ", refundAmount=" + refundAmount + ", status=" + status
				+ ", contactName=" + contactName + ", contactNumber=" + contactNumber + ", sellUserId=" + sellUserId
				+ ", cancelReason=" + cancelReason;
	}

}
