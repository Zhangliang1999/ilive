package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * F码管理列表
 * 
 * @author administrator
 */
public class BaseILiveFCode {
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 生效开始时间
	 */
	private Timestamp createTime;
	/**
	 * F码名称
	 */
	private String codeName;
	/**
	 * F码代码
	 */
	private String code;
	/**
	 * 直播间ID
	 */
	private Integer liveRoomId;
	/**
	 * 文件ID
	 */
	private Long fileId;
	/**
	 * 生效开始时间
	 */
	private Timestamp startTime;
	/**
	 * 生效结束时间
	 * 
	 */
	private Timestamp endTime;
	/**
	 * 状态 1：未使用 2已使用 3作废
	 */
	private Integer status;
	/**
	 * 绑定用户
	 */
	private Long bindUserId;
	/**
	 * 绑定时间
	 */
	private Timestamp bindTime;
	/**
	 * 绑定账号
	 */
	private String bindUserName;
	/**
	 * 绑定昵称
	 */
	private String bingNailName;
	 //失效时间
	private Timestamp outTime;
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

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(Integer liveRoomId) {
		this.liveRoomId = liveRoomId;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public Long getBindUserId() {
		return bindUserId;
	}

	public void setBindUserId(Long bindUserId) {
		this.bindUserId = bindUserId;
	}

	public Timestamp getBindTime() {
		return bindTime;
	}

	public void setBindTime(Timestamp bindTime) {
		this.bindTime = bindTime;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getBindUserName() {
		return bindUserName;
	}

	public void setBindUserName(String bindUserName) {
		this.bindUserName = bindUserName;
	}

	public String getBingNailName() {
		return bingNailName;
	}

	public void setBingNailName(String bingNailName) {
		this.bingNailName = bingNailName;
	}

	public Timestamp getOutTime() {
		return outTime;
	}

	public void setOutTime(Timestamp outTime) {
		this.outTime = outTime;
	}

}
