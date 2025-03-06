package com.bvRadio.iLive.iLive.action.front.vo;

/**
 * 问答类
 * @author administrator
 */
public class AppQuestionAnswerVo {
	
	// 问题内容
	private String questionContent;
	
	// 回答内容
	private String answerContent;
	
	// 回答时间
	private String answerTime;
	
	/**
	 * 是否回复 0未回复 1 已回复
	 */
	private Integer replyType;
	
	/**
	 * 回答人
	 */
	private String answerPerson;

	public String getQuestionContent() {
		return questionContent;
	}

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}

	public String getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}

	public String getAnswerPerson() {
		return answerPerson;
	}

	public void setAnswerPerson(String answerPerson) {
		this.answerPerson = answerPerson;
	}

	public Integer getReplyType() {
		return replyType;
	}

	public void setReplyType(Integer replyType) {
		this.replyType = replyType;
	}
	
	
	
	
}
