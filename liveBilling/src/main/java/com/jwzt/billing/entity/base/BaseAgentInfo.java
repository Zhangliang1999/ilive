package com.jwzt.billing.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseAgentInfo implements java.io.Serializable {

	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 代理商名称
	 */
	private String agentName;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 代理商扣率
	 */
	private Double deductionRate;
	/**
	 * 公司全称
	 */
	private String fullName;
	/**
	 * 组织机构代码
	 */
	private String organizationCode;
	/**
	 * 法定代表人
	 */
	private String legalRepresentative;
	/**
	 * 注册地址
	 */
	private String registeredAddress;
	/**
	 * 银行开户行
	 */
	private String bankName;
	/**
	 * 银行账号
	 */
	private String bankAccount;
	/**
	 * 联系人
	 */
	private String contactName;
	/**
	 * 联系电话
	 */
	private String contactNumber;

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

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getDeductionRate() {
		return deductionRate;
	}

	public void setDeductionRate(Double deductionRate) {
		this.deductionRate = deductionRate;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getLegalRepresentative() {
		return legalRepresentative;
	}

	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
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
}
