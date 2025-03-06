package com.jwzt.statistic.entity.base;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author zl
 */

@SuppressWarnings("serial")
public class BaseMeetingStatisticResult implements java.io.Serializable {

	private String id;
	private Long meetingId;
	private Long enterpriseId;
	private String enterpriseName;
	private Timestamp meetingStartTime;
	private Timestamp meetingEndTime;
	private Long PCNum;
	private Long APPNum;
	private Integer meetingRecord;
	private Integer MeetingRedirect;
	private Long MeetingFigure;
	private Long MeetingTime;//按秒记录
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public Timestamp getMeetingStartTime() {
		return meetingStartTime;
	}
	public void setMeetingStartTime(Timestamp meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}
	public Timestamp getMeetingEndTime() {
		return meetingEndTime;
	}
	public void setMeetingEndTime(Timestamp meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}
	public Long getPCNum() {
		return PCNum;
	}
	public void setPCNum(Long pCNum) {
		PCNum = pCNum;
	}
	public Long getAPPNum() {
		return APPNum;
	}
	public void setAPPNum(Long aPPNum) {
		APPNum = aPPNum;
	}
	public Integer getMeetingRecord() {
		return meetingRecord;
	}
	public void setMeetingRecord(Integer meetingRecord) {
		this.meetingRecord = meetingRecord;
	}
	public Integer getMeetingRedirect() {
		return MeetingRedirect;
	}
	public void setMeetingRedirect(Integer meetingRedirect) {
		MeetingRedirect = meetingRedirect;
	}
	public Long getMeetingFigure() {
		return MeetingFigure;
	}
	public void setMeetingFigure(Long meetingFigure) {
		MeetingFigure = meetingFigure;
	}
	public Long getMeetingTime() {
		return MeetingTime;
	}
	public void setMeetingTime(Long meetingTime) {
		MeetingTime = meetingTime;
	}
	
}
