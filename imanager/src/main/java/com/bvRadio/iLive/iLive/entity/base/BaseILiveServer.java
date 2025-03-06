package com.bvRadio.iLive.iLive.entity.base;

import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveServerGroup;

/**
 * @author administrator 服务器
 */
public abstract class BaseILiveServer {

	/**
	 * 服务器ID
	 */
	private Integer server_id;

	/**
	 * 服务器组ID
	 */
	private ILiveServerGroup server_group;

	/**
	 * 服务器名称
	 */
	private String server_name;

	/**
	 * 服务器地址
	 */
	private String address;

	/**
	 * ftp地址
	 */
	private String ftp_address;

	/**
	 * 服务器平台 0:windows 1:unix 2:linux
	 */
	private Integer platform;

	/**
	 * 服务器类型 1:Media 2:Real
	 */
	private Integer type;

	/**
	 * 服务器模式 0:附从模式 1:主模式(1,因为公用一个磁盘，那么内容只需有主模式上传一次)
	 */
	private Integer mode;

	/**
	 * 日志路径
	 */
	private String logPath;

	/**
	 * 处理器数目
	 */
	private Integer number_of_cpu;

	/**
	 * 内存大小(M)
	 */
	private Integer memory_size;

	/**
	 * 服务器的带宽(Kbps)
	 */
	private Integer band_width;

	/**
	 * FTP用户名
	 */
	private String ftp_user;

	/**
	 * FTP用户密码
	 */
	private String ftp_pwd;

	/**
	 * 服务器的最大连接数
	 */
	private Integer loadbalance;

	/**
	 * CPU使用情况
	 */
	private Float monitor_cpu;

	/**
	 * 内存使用情况
	 */
	private Float monitor_mem;

	/**
	 * 带宽情况(Kbps)
	 */
	private Integer monitor_band_width;

	/**
	 * 连接数
	 */
	private Integer monitor_connections;

	/**
	 * 磁盘使用情况
	 */
	private String disksinfo;

	/**
	 * 服务器管理端口
	 */
	private Integer manage_port;

	/**
	 * 
	 * @return
	 */
	ILiveServerAccessMethod serverAccessMethod;

	public Integer getServer_id() {
		return server_id;
	}

	public void setServer_id(Integer server_id) {
		this.server_id = server_id;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFtp_address() {
		return ftp_address;
	}

	public void setFtp_address(String ftp_address) {
		this.ftp_address = ftp_address;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public Integer getNumber_of_cpu() {
		return number_of_cpu;
	}

	public void setNumber_of_cpu(Integer number_of_cpu) {
		this.number_of_cpu = number_of_cpu;
	}

	public Integer getMemory_size() {
		return memory_size;
	}

	public void setMemory_size(Integer memory_size) {
		this.memory_size = memory_size;
	}

	public Integer getBand_width() {
		return band_width;
	}

	public void setBand_width(Integer band_width) {
		this.band_width = band_width;
	}

	public String getFtp_user() {
		return ftp_user;
	}

	public void setFtp_user(String ftp_user) {
		this.ftp_user = ftp_user;
	}

	public String getFtp_pwd() {
		return ftp_pwd;
	}

	public void setFtp_pwd(String ftp_pwd) {
		this.ftp_pwd = ftp_pwd;
	}

	public Integer getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(Integer loadbalance) {
		this.loadbalance = loadbalance;
	}

	public Float getMonitor_cpu() {
		return monitor_cpu;
	}

	public void setMonitor_cpu(Float monitor_cpu) {
		this.monitor_cpu = monitor_cpu;
	}

	public Float getMonitor_mem() {
		return monitor_mem;
	}

	public void setMonitor_mem(Float monitor_mem) {
		this.monitor_mem = monitor_mem;
	}

	public Integer getMonitor_band_width() {
		return monitor_band_width;
	}

	public void setMonitor_band_width(Integer monitor_band_width) {
		this.monitor_band_width = monitor_band_width;
	}

	public Integer getMonitor_connections() {
		return monitor_connections;
	}

	public void setMonitor_connections(Integer monitor_connections) {
		this.monitor_connections = monitor_connections;
	}

	public String getDisksinfo() {
		return disksinfo;
	}

	public void setDisksinfo(String disksinfo) {
		this.disksinfo = disksinfo;
	}

	public Integer getManage_port() {
		return manage_port;
	}

	public void setManage_port(Integer manage_port) {
		this.manage_port = manage_port;
	}

	public ILiveServerGroup getServer_group() {
		return server_group;
	}

	public void setServer_group(ILiveServerGroup server_group) {
		this.server_group = server_group;
	}

	public ILiveServerAccessMethod getServerAccessMethod() {
		return serverAccessMethod;
	}

	public void setServerAccessMethod(ILiveServerAccessMethod serverAccessMethod) {
		this.serverAccessMethod = serverAccessMethod;
	}

}
