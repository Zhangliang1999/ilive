package com.bvRadio.iLive.iLive.entity.base;

/**
 * 点播分享
 * 
 * @author administrator
 */
public class BaseILiveMediaFileShareConfig {

	private Long shareId;

	private Integer fileId;

	private String mediaFileName;

	// 简介
	private String mediaFileDesc;

	// 分享图片
	private String shareImg;

	// 1朋友圈 2朋友
	private Integer shareType;

	//分享地址
	private String shareUrl;

	// 启用状态
	private Integer openStatus;

	// 是否手动修改过
	private Integer ifEdit;
	public Long getShareId() {
		return shareId;
	}

	public void setShareId(Long shareId) {
		this.shareId = shareId;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public String getMediaFileName() {
		return mediaFileName;
	}

	public void setMediaFileName(String mediaFileName) {
		this.mediaFileName = mediaFileName;
	}

	public String getMediaFileDesc() {
		return mediaFileDesc;
	}

	public void setMediaFileDesc(String mediaFileDesc) {
		this.mediaFileDesc = mediaFileDesc;
	}

	public String getShareImg() {
		return shareImg;
	}

	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
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

	public Integer getIfEdit() {
		return ifEdit;
	}

	public void setIfEdit(Integer ifEdit) {
		this.ifEdit = ifEdit;
	}

	

}
