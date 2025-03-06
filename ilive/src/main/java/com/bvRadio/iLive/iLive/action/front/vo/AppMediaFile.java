package com.bvRadio.iLive.iLive.action.front.vo;

/**
 * API
 * 媒资文件
 * @author administrator
 */
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
	 * 文件描述
	 */
	private String fileDesc;

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
	private Long viewNum;

	/**
	 * 点赞数
	 */
	private Integer praiseNum;

	/**
	 * 文件上线标识 1上线 0下线
	 */
	private Integer onlineState;

	/**
	 * 文件播放地址
	 */
	private String playAddr;

	/**
	 * 企业
	 * 
	 * @return
	 */
	ILiveAppEnterprise appEnterprise;

	/**
	 * 分辨率
	 */
	private String widthHeight;

	/**
	 * 是否允许评论
	 * 
	 * @return
	 */
	private Integer allowComment;

	/**
	 * 直播间Id
	 * 
	 * @return
	 */
	private Integer roomId;

	/**
	 * 场次Id
	 * 
	 * @return
	 */
	private Long liveEventId;

	/**
	 * 企业Id
	 * 
	 * @return
	 */
	private Integer enterpriseId;

	/**
	 * 用户ID
	 * 
	 * @return
	 */
	private Long userId;

	/**
	 * 分享地址
	 * 
	 * @return
	 */
	private String shareUrl;
	
	
	/**
	 * 鉴权类型
	 * @return
	 */
	private Integer authType;
	
	
	/**
	 * 鉴权密码
	 * @return
	 */
	private String authPassword;
	
	
	/**
	 * 暂存状态
	 * @return
	 */
	private Integer storageState;
	
	/**
	 * 暂存剩余时长
	 * @return
	 */
	private  String storageLeftTime;
	
	/**
	 * 是否是VR视频
	 */
	private Integer vrFile;
	/**
	 * 下载地址
	 * @return
	 */
    private String downloadUrl;
    /**
     * 是否需要登陆
     * @return
     */
    private Integer needLogin;
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

	public ILiveAppEnterprise getAppEnterprise() {
		return appEnterprise;
	}

	public void setAppEnterprise(ILiveAppEnterprise appEnterprise) {
		this.appEnterprise = appEnterprise;
	}

	public Integer getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(Integer onlineState) {
		this.onlineState = onlineState;
	}

	public String getPlayAddr() {
		return playAddr;
	}

	public void setPlayAddr(String playAddr) {
		this.playAddr = playAddr;
	}

	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}

	public String getFileDesc() {
		return fileDesc;
	}

	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	public Integer getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(Integer allowComment) {
		this.allowComment = allowComment;
	}

	public String getWidthHeight() {
		return widthHeight;
	}

	public void setWidthHeight(String widthHeight) {
		this.widthHeight = widthHeight;
	}

	public Long getViewNum() {
		return viewNum;
	}

	public void setViewNum(Long viewNum) {
		this.viewNum = viewNum;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public Integer getAuthType() {
		return authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
	}

	public String getAuthPassword() {
		return authPassword;
	}

	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}

	public Integer getStorageState() {
		return storageState;
	}

	public void setStorageState(Integer storageState) {
		this.storageState = storageState;
	}

	public String getStorageLeftTime() {
		return storageLeftTime;
	}

	public void setStorageLeftTime(String storageLeftTime) {
		this.storageLeftTime = storageLeftTime;
	}

	public Integer getVrFile() {
		return vrFile;
	}

	public void setVrFile(Integer vrFile) {
		this.vrFile = vrFile;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public Integer getNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(Integer needLogin) {
		this.needLogin = needLogin;
	}
	

}
