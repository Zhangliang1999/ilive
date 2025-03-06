package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * 企业白名单
 * 
 * @author administrator
 */
public abstract class BaseEnterpriseWhiteBill {

	/**
	 * 白名单ID
	 */
	private Integer whitebillId;

	/**
	 * 企业ID
	 */
	private Integer enterpriseId;

	/**
	 * 企业手机号
	 */
	private String phoneNum;

	/**
	 * 用户姓名
	 */
	private String userName;

	/**
	 * 导入人
	 * 
	 * @return
	 */
	private String exportPerson;

	/**
	 * 导入时间
	 * 
	 * @return
	 */
	private Timestamp exportTime;

	/**
	 * 是否删除 0为未删除 1为删除
	 */
	private int isDel;

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getExportPerson() {
		return exportPerson;
	}

	public void setExportPerson(String exportPerson) {
		this.exportPerson = exportPerson;
	}

	public Timestamp getExportTime() {
		return exportTime;
	}

	public void setExportTime(Timestamp exportTime) {
		this.exportTime = exportTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public Integer getWhitebillId() {
		return whitebillId;
	}

	public void setWhitebillId(Integer whitebillId) {
		this.whitebillId = whitebillId;
	}

}
