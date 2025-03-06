package com.bvRadio.iLive.iLive.action.front.vo;

public class AppMediaFile {

	/**
	 * 文件ID 唯一标识
	 */
	private Long fileId;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 文件头像
	 */
	private String fileImg;

	/**
	 * 文件大小
	 */
	private String fileSize;

	/**
	 * 文件时长
	 */
	private String fileDuration;

	/**
	 * 生成时间
	 */
	private String createTime;

	/**
	 * 评论数
	 */
	private String commentsNum;

	/**
	 * 点播数
	 * 
	 * @return
	 */
	private String playNum;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileImg() {
		return fileImg;
	}

	public void setFileImg(String fileImg) {
		this.fileImg = fileImg;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileDuration() {
		return fileDuration;
	}

	public void setFileDuration(String fileDuration) {
		this.fileDuration = fileDuration;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCommentsNum() {
		return commentsNum;
	}

	public void setCommentsNum(String commentsNum) {
		this.commentsNum = commentsNum;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getPlayNum() {
		return playNum;
	}

	public void setPlayNum(String playNum) {
		this.playNum = playNum;
	}

}
