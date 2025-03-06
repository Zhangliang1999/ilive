package com.bvRadio.iLive.iLive.entity.base;

@SuppressWarnings("serial")
public abstract class BaseILiveUploadServer implements java.io.Serializable {

	private Integer id;
	private String ftpIp;
	private Integer ftpPort;
	private String ftpUsername;
	private String ftpPassword;
	private String ftpPath;
	private String ftpEncoding;
	private String httpUrl;
	private String downloadUrl;
	private String httpMount;
	private boolean isDefault;

	public BaseILiveUploadServer() {
	}

	public BaseILiveUploadServer(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFtpIp() {
		return this.ftpIp;
	}

	public void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}

	public Integer getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUsername() {
		return this.ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return this.ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getFtpPath() {
		return this.ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public String getFtpEncoding() {
		return ftpEncoding;
	}

	public void setFtpEncoding(String ftpEncoding) {
		this.ftpEncoding = ftpEncoding;
	}

	public String getHttpUrl() {
		return this.httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getHttpMount() {
		return this.httpMount;
	}

	public void setHttpMount(String httpMount) {
		this.httpMount = httpMount;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

}