package com.bvRadio.iLive.iLive.action.front.vo;

public class TempLoginInfo {

	// 密码校验结果
	private Boolean passwdCheckResult = false;
	
	/**
	 * 关联的文件ID
	 */
	private Long fileId;

	public Boolean getPasswdCheckResult() {
		return passwdCheckResult;
	}

	public void setPasswdCheckResult(Boolean passwdCheckResult) {
		this.passwdCheckResult = passwdCheckResult;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	
	
	

}
