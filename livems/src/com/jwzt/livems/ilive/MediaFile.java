package com.jwzt.livems.ilive;

/**
 * 
 * @author administrator 媒体文件
 */
public class MediaFile {

	/**
	 * 文件ID
	 */
	private Long fileId;

	/**
	 * 文件路径
	 */
	private String filePath;

	/**
	 * 文件大小
	 */
	private Integer fileSize;

	/**
	 * 文件分辨率
	 */
	private String fileWh;

	/**
	 * 文件时长
	 */
	private Integer duration = 0;

	/**
	 * 文件扩展名
	 * 
	 * @return
	 */
	private String fileExt;

	/**
	 * 文件码率
	 * 
	 * @return
	 */
	private Double biterate;
	
	/**
	 * 文件截图
	 * @return
	 */
	private String fileConver;
	
	/**
	 * 重试次数
	 * @return
	 */
	private Integer retryTime = 30;

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileWh() {
		return fileWh;
	}

	public void setFileWh(String fileWh) {
		this.fileWh = fileWh;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public Double getBiterate() {
		return biterate;
	}

	public void setBiterate(Double biterate) {
		this.biterate = biterate;
	}

	public String getFileConver() {
		return fileConver;
	}

	public void setFileConver(String fileConver) {
		this.fileConver = fileConver;
	}
	
	
	public Integer getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(Integer retryTime) {
		this.retryTime = retryTime;
	}

	@Override
	public String toString() {
		return "MediaFile [fileId=" + fileId + ", filePath=" + filePath + ", fileSize=" + fileSize + ", fileWh="
				+ fileWh + ", duration=" + duration + ", fileExt=" + fileExt + ", biterate=" + biterate
				+ ", fileConver=" + fileConver + "]";
	}
	
	
	

}
