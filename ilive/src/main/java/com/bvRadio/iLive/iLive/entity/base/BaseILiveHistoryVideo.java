package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * 视频往期回看
 * @author administrator
 */
public class BaseILiveHistoryVideo {
	
	/**
	 * 历史ID
	 */
	private Long historyId;
	
	
	/**
	 * 文件ID
	 */
	private Long fileId;
	
	
	/**
	 * 直播间Id
	 */
	private Integer roomId;
	
	/**
	 * 文件顺序
	 * @return
	 */
	private Double fileOrder;
	
	/**
	 * 删除状态
	 * @return
	 */
	private Integer delFlag;
	
	/**
	 * 操作人
	 * @return
	 */
	private Long userId;
	
	
	/**
	 * 录入时间
	 * @return
	 */
	private Timestamp recordTime;
	
	public Long getHistoryId() {
		return historyId;
	}


	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}


	public Long getFileId() {
		return fileId;
	}


	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}


	public Integer getRoomId() {
		return roomId;
	}


	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}


	public Double getFileOrder() {
		return fileOrder;
	}


	public void setFileOrder(Double fileOrder) {
		this.fileOrder = fileOrder;
	}


	public Integer getDelFlag() {
		return delFlag;
	}


	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Timestamp getRecordTime() {
		return recordTime;
	}


	public void setRecordTime(Timestamp recordTime) {
		this.recordTime = recordTime;
	}
	
	
	
	
}
