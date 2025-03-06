package com.jwzt.statistic.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseUserViewLog implements java.io.Serializable {

	private String id;
	private Long liveEventId;
	private Integer enterpriseId;
	private String userId;
	private Integer userType;
	private Integer requestType;
	private Timestamp lastStatisticTime;
	private Long viewTotalTime;
	private Integer status;
	private Long ipCode;

	public BaseUserViewLog() {
		super();
	}

	public BaseUserViewLog(Long liveEventId, String userId, Integer userType, Timestamp lastStatisticTime,
			Long viewTotalTime) {
		super();
		this.liveEventId = liveEventId;
		this.userId = userId;
		this.userType = userType;
		this.lastStatisticTime = lastStatisticTime;
		this.viewTotalTime = viewTotalTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public Long getViewTotalTime() {
		return viewTotalTime;
	}

	public void setViewTotalTime(Long viewTotalTime) {
		this.viewTotalTime = viewTotalTime;
	}

	public Timestamp getLastStatisticTime() {
		return lastStatisticTime;
	}

	public void setLastStatisticTime(Timestamp lastStatisticTime) {
		this.lastStatisticTime = lastStatisticTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getIpCode() {
		return ipCode;
	}

	public void setIpCode(Long ipCode) {
		this.ipCode = ipCode;
	}

}
