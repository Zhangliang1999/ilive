package com.jwzt.DB.soms.live.task;

import java.sql.Timestamp;

/**
 * 截图重试任务
 * 
 * @author administrator
 */
public class ILiveMakeScreenRetryTask {

	// 文件ID
	private Long fileId;

	// 任务节点
	private String taskNode;

	// 任务创建时间
	private Timestamp taskCreateTime;

	// 处理结果
	private Integer dealResult;

	// 重试次数
	private Integer retryTime;

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Timestamp getTaskCreateTime() {
		return taskCreateTime;
	}

	public void setTaskCreateTime(Timestamp taskCreateTime) {
		this.taskCreateTime = taskCreateTime;
	}

	public Integer getDealResult() {
		return dealResult;
	}

	public void setDealResult(Integer dealResult) {
		this.dealResult = dealResult;
	}

	public Integer getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(Integer retryTime) {
		this.retryTime = retryTime;
	}

	public String getTaskNode() {
		return taskNode;
	}

	public void setTaskNode(String taskNode) {
		this.taskNode = taskNode;
	}

}
