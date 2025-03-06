package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * @author administrator 直播间鉴权的关系表
 */
public abstract class BaseILiveViewAuthBill {

	// 主键ID
	private Long authId;

	// 用户ID
	private String userId;

	// 直播间ID
	private Integer liveRoomId;

	// 直播场次Id
	private Long liveEventId;
	//回看文件Id
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

	/**
	 * 版本号
	 * 
	 * @return
	 */
	private Long versionNum;

	public Long getAuthId() {
		return authId;
	}

	public void setAuthId(Long authId) {
		this.authId = authId;
	}

	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(Integer liveRoomId) {
		this.liveRoomId = liveRoomId;
	}

	public Long getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(Long versionNum) {
		this.versionNum = versionNum;
	}

}
