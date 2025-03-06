package com.jwzt.billing.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseWorkOrder implements java.io.Serializable {
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 工单类型
	 */
	private Integer workOrderType;
	/**
	 * 企业ID
	 */
	private Integer enterpriseId;
	/**
	 * 提交用户id
	 */
	private Long submitUserId;
	/**
	 * 提交用户
	 */
	private String submitUserName;
	/**
	 * 联系人
	 */
	private String contactName;
	/**
	 * 联系电话
	 */
	private String contactPhoneNumber;
	/**
	 * 状态
	 */
	private Integer status;

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

	public Integer getWorkOrderType() {
		return workOrderType;
	}

	public void setWorkOrderType(Integer workOrderType) {
		this.workOrderType = workOrderType;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getSubmitUserId() {
		return submitUserId;
	}

	public void setSubmitUserId(Long submitUserId) {
		this.submitUserId = submitUserId;
	}

	public String getSubmitUserName() {
		return submitUserName;
	}

	public void setSubmitUserName(String submitUserName) {
		this.submitUserName = submitUserName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}

	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
