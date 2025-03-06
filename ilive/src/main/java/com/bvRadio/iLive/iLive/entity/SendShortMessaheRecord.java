package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 发短信设置
 * @author Wei
 *
 */
public class SendShortMessaheRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4565708369606512545L;

	private Long id;
	
	private Long recordId;
	
	private Long sendUserId;
	
	private String sendUserName;
	
	private String receiveUserName;
	
	private String receiveUserPhone;
	
	private Integer enterpriseId;
	
	private Timestamp createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(Long sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendUserName() {
		return this.sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	public String getReceiveUserPhone() {
		return receiveUserPhone;
	}

	public void setReceiveUserPhone(String receiveUserPhone) {
		this.receiveUserPhone = receiveUserPhone;
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
	
	
}
