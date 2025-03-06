package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
/**
 * 邀请卡
 * @author Wei
 *
 */
public class InviteCard {

//	/**
//	 * 主键id
//	 */
//	private Long id;
	
	/**
	 * 是否开启   0未开启   1开启
	 */
	private Integer isOpen;
	
	/**
	 * 直播间id
	 */
	private Integer roomId;
	
	/**
	 * 背景图
	 */
	private String coverUrl;
	
	/**
	 * 直播标题
	 */
	private String liveTitle;
	
	/**
	 * 主办方
	 */
	private String sponser;
	
	/**
	 * 开始时间
	 */
	private Timestamp startTime;

	/**
	 * 直播间地址
	 */
	private String liveUrl;
	
	/**
	 * h5分享地址
	 */
	private String h5Live;
	
	/**
	 * 
	 */
	private Integer isPublish;
	
	
	public Integer getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Integer isPublish) {
		this.isPublish = isPublish;
	}

	public String getH5Live() {
		return h5Live;
	}

	public void setH5Live(String h5Live) {
		this.h5Live = h5Live;
	}

	public String getLiveUrl() {
		return liveUrl;
	}

	public void setLiveUrl(String liveUrl) {
		this.liveUrl = liveUrl;
	}

//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}

	public String getSponser() {
		return sponser;
	}

	public void setSponser(String sponser) {
		this.sponser = sponser;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	
}
