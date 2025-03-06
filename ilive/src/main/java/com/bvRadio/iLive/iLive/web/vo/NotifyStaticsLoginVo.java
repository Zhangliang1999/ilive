package com.bvRadio.iLive.iLive.web.vo;

/**
 * 通知统计系统登入登出时的bean
 * 
 * @author administrator
 */
public class NotifyStaticsLoginVo {

	private String userId;

	private Integer liveId;

	private Integer webType;

	private String ipAddr;

	private Integer sessionType;
	
	private Long liveEventId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getLiveId() {
		return liveId;
	}

	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}

	public Integer getWebType() {
		return webType;
	}

	public void setWebType(Integer webType) {
		this.webType = webType;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Integer getSessionType() {
		return sessionType;
	}

	public void setSessionType(Integer sessionType) {
		this.sessionType = sessionType;
	}
	
	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

	@Override
	public String toString() {
		return "NotifyStaticsLoginVo [userId=" + userId + ", liveId=" + liveId + ", webType=" + webType + ", ipAddr="
				+ ipAddr + ", sessionType=" + sessionType + ", liveEventId=" + liveEventId + "]";
	}

	
}
