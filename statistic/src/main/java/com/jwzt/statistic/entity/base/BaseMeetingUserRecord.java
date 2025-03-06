package com.jwzt.statistic.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseMeetingUserRecord implements java.io.Serializable {
 
	private String id;
	private Long userId;
	private Timestamp enterTime;
	private Timestamp outTime;
	private Long meetingId;
	private Integer role;
	private Integer loginType;
	private String nailName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Timestamp getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(Timestamp enterTime) {
		this.enterTime = enterTime;
	}
	public Timestamp getOutTime() {
		return outTime;
	}
	public void setOutTime(Timestamp outTime) {
		this.outTime = outTime;
	}
	public Long getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public Integer getLoginType() {
		return loginType;
	}
	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
	public String getNailName() {
		return nailName;
	}
	public void setNailName(String nailName) {
		this.nailName = nailName;
	}

	
	
}
