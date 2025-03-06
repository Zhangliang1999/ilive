package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 问卷活动
 * @author Wei
 *
 */
public class ILiveQuestionnaireActivityStatistic implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -175138892081295702L;
	/**
	 * 问卷活动id
	 */
	private Long id;
	
	/**
	 * 问卷活动名称
	 */
	private Long questionnaireId;
	
	/**
	 * 问卷答题开始时间
	 */
	private Timestamp startTime;
	
	/**
	 * 问卷答题结束时间
	 */
	private Timestamp endTime;
	
	/**
	 * 用户id
	 */
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Long questionnaireId) {
		this.questionnaireId = questionnaireId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
   
}
