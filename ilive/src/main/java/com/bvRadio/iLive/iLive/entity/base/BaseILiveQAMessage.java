package com.bvRadio.iLive.iLive.entity.base;

/**
 * @author administrator
 * 互动问答
 */
public abstract class BaseILiveQAMessage {
	
	/**
	 * 问答ID
	 */
	private Long msgId;
	
	
	/**
	 * 问题
	 */
	private String question;
	
	/**
	 * 回答
	 */
	private String answer;
	
	
	/**
	 * 回答人名称
	 */
	private String answerPerson;
	
	/**
	 * 问题人名称
	 */
	private String questionPerson;
	
	
	/**
	 * 回答状态
	 */
	private Integer answerStatus;


	public Long getMsgId() {
		return msgId;
	}


	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}


	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public String getAnswer() {
		return answer;
	}


	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public String getAnswerPerson() {
		return answerPerson;
	}


	public void setAnswerPerson(String answerPerson) {
		this.answerPerson = answerPerson;
	}


	public String getQuestionPerson() {
		return questionPerson;
	}


	public void setQuestionPerson(String questionPerson) {
		this.questionPerson = questionPerson;
	}


	public Integer getAnswerStatus() {
		return answerStatus;
	}


	public void setAnswerStatus(Integer answerStatus) {
		this.answerStatus = answerStatus;
	}


	
}
