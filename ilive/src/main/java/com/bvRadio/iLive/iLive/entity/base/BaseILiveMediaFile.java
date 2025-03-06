package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * 
 * @author administrator 直播媒资文件
 */
public abstract class BaseILiveMediaFile {

	/**
	 * 媒资文件ID
	 */
	private Long fileId;

	/**
	 * 媒资文件名称
	 */
	private String mediaFileName;
	/**
	 * 媒资描述
	 */
	private String mediaFileDesc;

	// 媒资上传时间
	private Timestamp mediaCreateTime;

	// 录制开始时间
	private Timestamp createStartTime;

	// 录制结束时间
	private Timestamp createEndTime;

	// 媒资生产方式 [0 原片、 1 剪辑、 2合并 、3上传  4直播垫片]
	private Integer createType;

	// 文件时长
	private Integer duration;

	// 文件码率
	private Double fileRate;

	// 文件分辨率
	private String widthHeight;

	// 文件大小(KB为单位)
	private Long fileSize;

	// 视频观看次数
	private Long viewNum;

	// 文件路径
	private String filePath;

	// 文件类型 1视频 2文档 3图片
	private Integer fileType;

	// 是否允许下载
	private Boolean allowDownload;

	// doc是否处理完成为图片的状态
	private Integer docDealState;

	// 处理后的地址
	private String docDealFinishAddr;

	// 处理后图片的个数
	private String docDealFinishPicCount;

	/**
	 * 直播场次ID
	 * @return
	 */
	private Long liveEventId;

	/**
	 * 服务器组ID
	 * 
	 * @return
	 */
	private Integer serverMountId;

	/**
	 * 删除标记:1表示yes已经删除，0或者空表示no未删除
	 */
	private Integer delFlag;

	/**
	 * 上线标记:1表示yes上线，0或者空表示下线
	 */
	private Integer onlineFlag;
	/**
	 * 封面图
	 */
	private String fileCover;
	/**
	 * 当前登录用户名
	 */
	private String userName;
	/**
	 * 当前登录用户id
	 */
	private Long userId;

	/**
	 * 直播间Id
	 */
	private Integer liveRoomId;
	/**
	 * 企业Id
	 */
	private Long enterpriseId;

	/**
	 * 文件扩展名
	 */
	private String fileExtension;
	/**
	 * 文件存储状态  0 存储   1 暂存 
	 */
	private Integer isMediaType;
	/**
	 * 暂存时间
	 */
	private Timestamp temporaryTime;
	
	/**
	 * 是否是VR文件
	 */
	private Boolean vrFile;
	
	/**
	 * 是否开启文档直播
	 */
	private Integer isFileDoc;

	/**
	 * 是否同步到天翼云盘（0或null  未同步）
	 */
	private Integer isSync;

	/**
	 * 天翼云盘文件id
	 */
	private String cloudDiskFileId;
	/**
	 * 上传人id
	 */
	private String createId;

	/**
	 * 修改人id
	 */
	private String updateId;

	/**
	 * 修改时间
	 */
	private Timestamp mediaUpdateTime;

	/**
	 * 文件全路径
	 */
	private String fileUrl;

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public Timestamp getMediaUpdateTime() {
		return mediaUpdateTime;
	}

	public void setMediaUpdateTime(Timestamp mediaUpdateTime) {
		this.mediaUpdateTime = mediaUpdateTime;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getCloudDiskFileId() {
		return cloudDiskFileId;
	}

	public void setCloudDiskFileId(String cloudDiskFileId) {
		this.cloudDiskFileId = cloudDiskFileId;
	}

	public Integer getIsSync() {
		return isSync;
	}

	public void setIsSync(Integer isSync) {
		this.isSync = isSync;
	}
	
	
	public Integer getIsFileDoc() {
		return isFileDoc;
	}

	public void setIsFileDoc(Integer isFileDoc) {
		this.isFileDoc = isFileDoc;
	}

	public Integer getIsMediaType() {
		return isMediaType;
	}

	public void setIsMediaType(Integer isMediaType) {
		this.isMediaType = isMediaType;
	}

	public Timestamp getTemporaryTime() {
		return temporaryTime;
	}

	public void setTemporaryTime(Timestamp temporaryTime) {
		this.temporaryTime = temporaryTime;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getMediaFileName() {
		return mediaFileName;
	}

	public void setMediaFileName(String mediaFileName) {
		this.mediaFileName = mediaFileName;
	}

	public Timestamp getMediaCreateTime() {
		return mediaCreateTime;
	}

	public void setMediaCreateTime(Timestamp mediaCreateTime) {
		this.mediaCreateTime = mediaCreateTime;
	}

	public Timestamp getCreateStartTime() {
		return createStartTime;
	}

	public void setCreateStartTime(Timestamp createStartTime) {
		this.createStartTime = createStartTime;
	}

	public Timestamp getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(Timestamp createEndTime) {
		this.createEndTime = createEndTime;
	}

	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getWidthHeight() {
		return widthHeight;
	}

	public void setWidthHeight(String widthHeight) {
		this.widthHeight = widthHeight;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getViewNum() {
		return viewNum;
	}

	public void setViewNum(Long viewNum) {
		this.viewNum = viewNum;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Boolean getAllowDownload() {
		return allowDownload;
	}

	public void setAllowDownload(Boolean allowDownload) {
		this.allowDownload = allowDownload;
	}

	public String getDocDealFinishAddr() {
		return docDealFinishAddr;
	}

	public void setDocDealFinishAddr(String docDealFinishAddr) {
		this.docDealFinishAddr = docDealFinishAddr;
	}

	public String getDocDealFinishPicCount() {
		return docDealFinishPicCount;
	}

	public void setDocDealFinishPicCount(String docDealFinishPicCount) {
		this.docDealFinishPicCount = docDealFinishPicCount;
	}

	public Double getFileRate() {
		return fileRate;
	}

	public void setFileRate(Double fileRate) {
		this.fileRate = fileRate;
	}

	public Integer getDocDealState() {
		return docDealState;
	}

	public void setDocDealState(Integer docDealState) {
		this.docDealState = docDealState;
	}

	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Integer getOnlineFlag() {
		return onlineFlag;
	}

	public void setOnlineFlag(Integer onlineFlag) {
		this.onlineFlag = onlineFlag;
	}

	public String getFileCover() {
		return fileCover;
	}

	public void setFileCover(String fileCover) {
		this.fileCover = fileCover;
	}

	public String getMediaFileDesc() {
		return mediaFileDesc;
	}

	public void setMediaFileDesc(String mediaFileDesc) {
		this.mediaFileDesc = mediaFileDesc;
	}

	public Integer getServerMountId() {
		return serverMountId;
	}

	public void setServerMountId(Integer serverMountId) {
		this.serverMountId = serverMountId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(Integer liveRoomId) {
		this.liveRoomId = liveRoomId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public Boolean getVrFile() {
		return vrFile;
	}

	public void setVrFile(Boolean vrFile) {
		this.vrFile = vrFile;
	}


}
