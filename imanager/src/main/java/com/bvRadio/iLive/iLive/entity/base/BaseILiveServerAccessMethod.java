package com.bvRadio.iLive.iLive.entity.base;

/**
 * @author administrator 服务器访问方式
 */
public class BaseILiveServerAccessMethod {

	/**
	 * 访问方式ID
	 */
	private int method_id;

	/**
	 * 服务器id
	 */
	private Integer iliveServerId;
	/**
	 * 访问方式名称
	 */
	private String method_name;
	/**
	 * ftp地址
	 */
	private String ftp_address;
	/**
	 * ftp地址 -内网数据传输-直播采集
	 */
	private String ftp_address_inner;
	/**
	 * ftp用户名
	 */
	private String ftp_user;
	/**
	 * ftp 密码
	 */
	private String ftp_pwd;
	/**
	 * http地址
	 */
	private String http_address;
	/**
	 * 是否绑定应用服务器url
	 */
	private int default_url;
	/**
	 * 是否绑定ip段
	 */
	private int default_ip;
	/**
	 * 默认访问方式
	 */
	private int default_method;
	/**
	 * 绑定应用服务器URL
	 */
	private String url_address;
	/**
	 * IP段
	 */
	private String ip_ids;
	/**
	 * 扩展字段
	 */
	private String args_1;
	
	/**
	 * umsport
	 */
	private int umsport;
	
	/**
	 * livemsport
	 */
	private int livemsport;


	/**
	 * ftp编码
	 */
	private String ftpEncode;

	/**
	 * ftp端口
	 */
	private Integer ftpPort;

	/**
	 * web/HTML站点
	 */
	private String webHttpUrl;
	
	/**
	 * web/HTML站点
	 */
	private String h5HttpUrl;

	/**
	 * 扩展字段
	 */
	private String args_2;


	public int getMethod_id() {
		return method_id;
	}

	public void setMethod_id(int method_id) {
		this.method_id = method_id;
	}

	public String getMethod_name() {
		return method_name;
	}

	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}


	public String getFtp_address() {
		return ftp_address;
	}

	public void setFtp_address(String ftp_address) {
		this.ftp_address = ftp_address;
	}

	public String getFtp_address_inner() {
		return ftp_address_inner;
	}

	public void setFtp_address_inner(String ftp_address_inner) {
		this.ftp_address_inner = ftp_address_inner;
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

	public String getHttp_address() {
		return http_address;
	}

	public void setHttp_address(String http_address) {
		this.http_address = http_address;
	}

	public int getDefault_url() {
		return default_url;
	}

	public void setDefault_url(int default_url) {
		this.default_url = default_url;
	}

	public int getDefault_ip() {
		return default_ip;
	}

	public void setDefault_ip(int default_ip) {
		this.default_ip = default_ip;
	}

	public int getDefault_method() {
		return default_method;
	}

	public void setDefault_method(int default_method) {
		this.default_method = default_method;
	}

	public String getUrl_address() {
		return url_address;
	}

	public void setUrl_address(String url_address) {
		this.url_address = url_address;
	}

	public String getIp_ids() {
		return ip_ids;
	}

	public void setIp_ids(String ip_ids) {
		this.ip_ids = ip_ids;
	}

	public String getArgs_1() {
		return args_1;
	}

	public void setArgs_1(String args_1) {
		this.args_1 = args_1;
	}

	public int getUmsport() {
		return umsport;
	}

	public void setUmsport(int umsport) {
		this.umsport = umsport;
	}

	public String getArgs_2() {
		return args_2;
	}

	public void setArgs_2(String args_2) {
		this.args_2 = args_2;
	}

	public Integer getIliveServerId() {
		return iliveServerId;
	}

	public void setIliveServerId(Integer iliveServerId) {
		this.iliveServerId = iliveServerId;
	}


	public String getFtpEncode() {
		return ftpEncode;
	}

	public void setFtpEncode(String ftpEncode) {
		this.ftpEncode = ftpEncode;
	}

	public Integer getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getWebHttpUrl() {
		return webHttpUrl;
	}

	public void setWebHttpUrl(String webHttpUrl) {
		this.webHttpUrl = webHttpUrl;
	}

	public String getH5HttpUrl() {
		return h5HttpUrl;
	}

	public void setH5HttpUrl(String h5HttpUrl) {
		this.h5HttpUrl = h5HttpUrl;
	}

	public int getLivemsport() {
		return livemsport;
	}

	public void setLivemsport(int livemsport) {
		this.livemsport = livemsport;
	}
	
	
	


}
