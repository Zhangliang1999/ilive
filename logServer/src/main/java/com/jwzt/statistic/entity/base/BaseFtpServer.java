package com.jwzt.statistic.entity.base;

import java.io.Serializable;

/**
 * FTP 服务器信息
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public class BaseFtpServer implements Serializable{
	/**
	 * 主键
	 */
	private Integer serverId;
	/**
	 * 服务器名称
	 */
	private String serverName;
	/**
	 * 服务器IP
	 */
	private String ftpIp;
	/**
	 * 登录名称
	 */
	private String loginName;
	/**
	 * 登录密码
	 */
	private String loginPassword;
	/**
	 * ftp端口
	 */
	private Integer ftpPost;
	/**
	 * 路径
	 */
	private String ftpPath;
	public Integer getServerId() {
		return serverId;
	}
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getFtpIp() {
		return ftpIp;
	}
	public void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public Integer getFtpPost() {
		return ftpPost;
	}
	public void setFtpPost(Integer ftpPost) {
		this.ftpPost = ftpPost;
	}
	public String getFtpPath() {
		return ftpPath;
	}
	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}
	public BaseFtpServer() {
		super();
	}
	public BaseFtpServer(Integer serverId, String serverName, String ftpIp,
			String loginName, String loginPassword, Integer ftpPost,
			String ftpPath) {
		super();
		this.serverId = serverId;
		this.serverName = serverName;
		this.ftpIp = ftpIp;
		this.loginName = loginName;
		this.loginPassword = loginPassword;
		this.ftpPost = ftpPost;
		this.ftpPath = ftpPath;
	}
	
}
