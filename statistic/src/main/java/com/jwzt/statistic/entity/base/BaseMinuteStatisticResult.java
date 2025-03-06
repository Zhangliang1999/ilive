package com.jwzt.statistic.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseMinuteStatisticResult implements java.io.Serializable {

	private String id;
	private Long liveEventId;
	private Timestamp startTime;
	private Timestamp endTime;
	private Long userNum;
	private Long viewNum;
	private Long viewTotalNum;
	private Long userTotalNum;

	public BaseMinuteStatisticResult() {
		super();
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

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Long getUserNum() {
		return userNum;
	}

	public void setUserNum(Long userNum) {
		this.userNum = userNum;
	}

	public Long getViewNum() {
		return viewNum;
	}

	public void setViewNum(Long viewNum) {
		this.viewNum = viewNum;
	}

	public Long getViewTotalNum() {
		return viewTotalNum;
	}

	public void setViewTotalNum(Long viewTotalNum) {
		this.viewTotalNum = viewTotalNum;
	}

	public Long getUserTotalNum() {
		return userTotalNum;
	}

	public void setUserTotalNum(Long userTotalNum) {
		this.userTotalNum = userTotalNum;
	}
	
	
}
