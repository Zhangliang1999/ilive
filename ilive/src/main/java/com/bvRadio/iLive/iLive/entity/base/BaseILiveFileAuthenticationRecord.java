package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

public class BaseILiveFileAuthenticationRecord {
	
	/**
	 * 记录ID
	 */
	private Long recordId;

	// 用户ID
	private Long userId;
	
	/**
	 * 文件Id
	 */
	private Long fileId;


	// 直播类型 [密码、白名单、收费 ]
	private Integer authType;

	// 鉴权通过时间
	private Timestamp authPassTime;

	/**
	 * 生效状态
	 * 
	 * @return
	 */
	private Integer deleteStatus;

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}


	public Integer getAuthType() {
		return authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
	}

	public Timestamp getAuthPassTime() {
		return authPassTime;
	}

	public void setAuthPassTime(Timestamp authPassTime) {
		this.authPassTime = authPassTime;
	}

	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
