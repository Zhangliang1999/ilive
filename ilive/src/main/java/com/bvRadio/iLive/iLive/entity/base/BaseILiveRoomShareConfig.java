package com.bvRadio.iLive.iLive.entity.base;

/**
 * 直播间分享
 * 
 * @author administrator
 */
public class BaseILiveRoomShareConfig {

	private Long shareId;

	private Integer roomId;

	private String liveTitle;

	// 简介
	private String liveDesc;

	// 分享图片
	private String liveImg;

	// 1朋友圈 2朋友
	private Integer shareType;

	//分享地址
	private String shareUrl;

	// 启用状态
	private Integer openStatus;

	public Long getShareId() {
		return shareId;
	}

	public void setShareId(Long shareId) {
		this.shareId = shareId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}

	public String getLiveDesc() {
		return liveDesc;
	}

	public void setLiveDesc(String liveDesc) {
		this.liveDesc = liveDesc;
	}

	public String getLiveImg() {
		return liveImg;
	}

	public void setLiveImg(String liveImg) {
		this.liveImg = liveImg;
	}

	public Integer getShareType() {
		return shareType;
	}

	public void setShareType(Integer shareType) {
		this.shareType = shareType;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public Integer getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(Integer openStatus) {
		this.openStatus = openStatus;
	}

}
