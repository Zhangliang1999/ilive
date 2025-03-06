package com.jwzt.statistic.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseRequestLog implements java.io.Serializable {

	private String id;
	private Integer roomId;
	private Long liveEventId;
	private String userId;
	private Integer userType;
	private Integer requestType;
	private Integer logType;
	private Timestamp createTime;
	private Long ipCode;
	private Integer enterpriseId;
	private Long videoId;

	private String prop1;
	private String prop2;
	private String prop3;
	private String prop4;
	private String prop5;

	public BaseRequestLog() {
		super();
	}

	public BaseRequestLog(Integer roomId, Long liveEventId, String userId, Integer userType, Integer requestType,
			Integer logType, Long ipCode) {
		super();
		this.roomId = roomId;
		this.liveEventId = liveEventId;
		this.userId = userId;
		this.userType = userType;
		this.requestType = requestType;
		this.logType = logType;
		this.ipCode = ipCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getLogType() {
		return logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getIpCode() {
		return ipCode;
	}

	public void setIpCode(Long ipCode) {
		this.ipCode = ipCode;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public String getProp1() {
		return prop1;
	}

	public void setProp1(String prop1) {
		this.prop1 = prop1;
	}

	public String getProp2() {
		return prop2;
	}

	public void setProp2(String prop2) {
		this.prop2 = prop2;
	}

	public String getProp3() {
		return prop3;
	}

	public void setProp3(String prop3) {
		this.prop3 = prop3;
	}

	public String getProp4() {
		return prop4;
	}

	public void setProp4(String prop4) {
		this.prop4 = prop4;
	}

	public String getProp5() {
		return prop5;
	}

	public void setProp5(String prop5) {
		this.prop5 = prop5;
	}

}
