package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * @author administrator
 * 回看白名单观看列表
 */
public class BaseILiveFileWhiteBill {

	/**
	 * 白名单Id
	 */
	private Long billId;

	/**
	 * 文件ID
	 */
	private Long fileId;

	/**
	 * 手机号
	 */
	private String phoneNum;
	//用户姓名
	private String userName;	
	/**
	 * 导入时间
	 * 
	 */
	private Timestamp exportTime;
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Timestamp getExportTime() {
		return exportTime;
	}

	public void setExportTime(Timestamp exportTime) {
		this.exportTime = exportTime;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

}
