package com.bvRadio.iLive.iLive.action.front.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TempLoginInfo {

	// 直播间ID
	private Map<Long, String> map = new HashMap<>();

	/**
	 * 回看文件
	 */
	private ConcurrentHashMap<Long, String> fileMap = new ConcurrentHashMap<>();

	// 密码校验结果
	private Boolean passwdCheckResult = false;

	/**
	 * 关联的文件ID
	 */
	private Long fileId;

	// 引导页标识
	private Boolean getGuideAddr;

	/**
	 * @return
	 */
	private Boolean wxLogin;

	public Boolean getPasswdCheckResult() {
		return passwdCheckResult;
	}

	public void setPasswdCheckResult(Boolean passwdCheckResult) {
		this.passwdCheckResult = passwdCheckResult;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Map<Long, String> getMap() {
		return map;
	}

	public void setMap(Map<Long, String> map) {
		this.map = map;
	}

	public Boolean getGetGuideAddr() {
		return getGuideAddr;
	}

	public void setGetGuideAddr(Boolean getGuideAddr) {
		this.getGuideAddr = getGuideAddr;
	}

	public Boolean getWxLogin() {
		return wxLogin;
	}

	public void setWxLogin(Boolean wxLogin) {
		this.wxLogin = wxLogin;
	}

	public ConcurrentHashMap<Long, String> getFileMap() {
		return fileMap;
	}

	public void setFileMap(ConcurrentHashMap<Long, String> fileMap) {
		this.fileMap = fileMap;
	}
	
	
	

}
