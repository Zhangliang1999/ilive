package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;
import java.util.Set;

import com.bvRadio.iLive.iLive.entity.BBSDiyformData;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

/**
 * 
 * @author zpn
 * 转码任务表
 */
@SuppressWarnings("serial")
public abstract class BaseILiveConvertTask implements java.io.Serializable {
	
	
	/**
	 * 主键
	 */
	private Long taskId;
	
	
	/**
	 * 转码器返回的ID
	 */
	private Integer callBackId;
	
	
	/**
	 * 转码服务ID
	 */
	private Integer convertServerId;
	
	/**
	 * 转码模板ID
	 */
	private Integer templetId;
	
	/**
	 * 文件ID
	 */
	private Long fileId;

	/**
	 * 文件所属表	 0或null：ilive_media_file  1：ilive_meeting_file
	 */
	private Integer fileSourceTable;
	
	/**
	 * 转码状态 0:未开始转码 1:正在转码 2：转码失败 3：转码成功 4：转码成功后处理完毕
	 */
	private Integer state;
	
	/**
	 * 转码前文件地址ftp://username:password@127.0.0.1/vod/aaa.mp4
	 */
	private String sFtpPath;
	
	/**
	 * 转码后文件地址ftp://username:password@127.0.0.1/vod/aaa.mp4
	 */
	private String dFtpPath;

	/**
	 * 源文件相对路径
	 */
	private String sPath;
	
	/**
	 * 源目标文件相对路径
	 */
	private String dPath;

	public Integer getFileSourceTable() {
		return fileSourceTable;
	}

	public void setFileSourceTable(Integer fileSourceTable) {
		this.fileSourceTable = fileSourceTable;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Integer getTempletId() {
		return templetId;
	}

	public void setTempletId(Integer templetId) {
		this.templetId = templetId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getsFtpPath() {
		return sFtpPath;
	}

	public void setsFtpPath(String sFtpPath) {
		this.sFtpPath = sFtpPath;
	}

	public String getdFtpPath() {
		return dFtpPath;
	}

	public void setdFtpPath(String dFtpPath) {
		this.dFtpPath = dFtpPath;
	}

	public Integer getConvertServerId() {
		return convertServerId;
	}

	public void setConvertServerId(Integer convertServerId) {
		this.convertServerId = convertServerId;
	}

	public Integer getCallBackId() {
		return callBackId;
	}

	public void setCallBackId(Integer callBackId) {
		this.callBackId = callBackId;
	}

	public String getsPath() {
		return sPath;
	}

	public void setsPath(String sPath) {
		this.sPath = sPath;
	}

	public String getdPath() {
		return dPath;
	}

	public void setdPath(String dPath) {
		this.dPath = dPath;
	}

	

	
	
	

}