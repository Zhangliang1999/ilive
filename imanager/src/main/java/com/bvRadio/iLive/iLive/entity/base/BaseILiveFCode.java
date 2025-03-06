package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * F码管理列表
 * @author administrator
 */
public class BaseILiveFCode {

	/**
	 * 主键ID
	 */
	private Long codeId;

	/**
	 * F码名称
	 */
	private String codeName;

	/*
	 * F码代码
	 */
	private String code;
	
	
	/**
	 * 直播间ID
	 */
	private Integer liveRoomId;
	
	/**
	 * 用户  针对付费用户
	 */
	private Long userId;
	
	/**
	 * F码总数
	 */
	private Integer codeNum;
	
	/**
	 * 已使用的数量
	 */
	private Integer useNum;
	
	/**
	 * 生效开始时间
	 * @return
	 */
	private Timestamp startTime;
	
	/**
	 * 删除标记  0为未删除 1为删除
	 */
	private Integer isDelete;
	
	
	/**
	 * 生效结束时间
	 * @return
	 */
	private Timestamp endTime;
	
	
	/**
	 * 使用时间
	 * @return
	 */
	private Timestamp consumerTime;

	public Long getCodeId() {
		return codeId;
	}

	public void setCodeId(Long codeId) {
		this.codeId = codeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Timestamp getConsumerTime() {
		return consumerTime;
	}

	public void setConsumerTime(Timestamp consumerTime) {
		this.consumerTime = consumerTime;
	}

	public Integer getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(Integer liveRoomId) {
		this.liveRoomId = liveRoomId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	public Integer getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(Integer codeNum) {
		this.codeNum = codeNum;
	}

	public Integer getUseNum() {
		return useNum;
	}

	public void setUseNum(Integer useNum) {
		this.useNum = useNum;
	}
	
}
