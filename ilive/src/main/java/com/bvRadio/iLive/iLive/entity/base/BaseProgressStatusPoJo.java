package com.bvRadio.iLive.iLive.entity.base;

public class BaseProgressStatusPoJo {

	private Long taskId;
	
	private String taskUUID;
	
	private String localFileId;
	
	private String statusDesc;
	
	private Integer statusCode;
	
	public Long targetFileId;
	
	public String targetFtpIp;
	
	public String targetFtpPort;
	
	public String targetFtpUsername;
	
	public String targetFtppassword;
	
	public String targetFtpPath;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskUUID() {
		return taskUUID;
	}

	public void setTaskUUID(String taskUUID) {
		this.taskUUID = taskUUID;
	}

	public String getLocalFileId() {
		return localFileId;
	}

	public void setLocalFileId(String localFileId) {
		this.localFileId = localFileId;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Long getTargetFileId() {
		return targetFileId;
	}

	public void setTargetFileId(Long targetFileId) {
		this.targetFileId = targetFileId;
	}

	public String getTargetFtpIp() {
		return targetFtpIp;
	}

	public void setTargetFtpIp(String targetFtpIp) {
		this.targetFtpIp = targetFtpIp;
	}

	public String getTargetFtpPort() {
		return targetFtpPort;
	}

	public void setTargetFtpPort(String targetFtpPort) {
		this.targetFtpPort = targetFtpPort;
	}

	public String getTargetFtpUsername() {
		return targetFtpUsername;
	}

	public void setTargetFtpUsername(String targetFtpUsername) {
		this.targetFtpUsername = targetFtpUsername;
	}

	public String getTargetFtppassword() {
		return targetFtppassword;
	}

	public void setTargetFtppassword(String targetFtppassword) {
		this.targetFtppassword = targetFtppassword;
	}

	public String getTargetFtpPath() {
		return targetFtpPath;
	}

	public void setTargetFtpPath(String targetFtpPath) {
		this.targetFtpPath = targetFtpPath;
	}

	
	
	
}
