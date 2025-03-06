package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 子账号审核
 * @author Administrator zhangliang
 *
 */

public class ILiveSubAccountManager implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8946654035012790141L;
	private Long id;
	private Integer enterpriseId;
	/**
	 * 申请人账户
	 */
	private String user;
	/**
	 * 申请人姓名
	 */
	private String name;
	/**
	 * 联系方式
	 */
	private String contactPhone;
	/**
	 * 申请日期
	 */
	private Timestamp creatTime;
	/**
	 * 申请理由
	 */
	private String reason;
	/**
	 * 审核过程     1新建申请；2  处理完成
	 */
	private Integer process;
	/**
	 * 审核结果 0 不通过 1 通过 2 未审核
	 * @return
	 */
	private Integer result;
	/**
	 * 拒绝理由
	 * @return
	 */
	private String refuse;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public Timestamp getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Timestamp creatTime) {
		this.creatTime = creatTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Integer getProcess() {
		return process;
	}
	public void setProcess(Integer process) {
		this.process = process;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getRefuse() {
		return refuse;
	}
	public void setRefuse(String refuse) {
		this.refuse = refuse;
	}
	
	
	
}
