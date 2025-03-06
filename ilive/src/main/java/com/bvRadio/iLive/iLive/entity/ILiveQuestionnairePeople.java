package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class ILiveQuestionnairePeople implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 2433380508659767358L;

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 问答活动id
	 */
	private Long questionnaireId;
	
	/**
	 * 问答问题id
	 */
	private Long questionnaireProblemId;
	
	/**
	 * 问答选项id
	 */
	private Long questionnaireOptionId;
	/**
	 * 问答题答案
	 */
	private String answer;
	/**
	 * userId
	 */
	private Long userId;
	
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 身份证号
	 */
    private String IdCard;
    /**
     * 用户名
     */
    private String userName;
    
	private Timestamp createTime;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	

	public Long getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Long questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public Long getQuestionnaireProblemId() {
		return questionnaireProblemId;
	}

	public void setQuestionnaireProblemId(Long questionnaireProblemId) {
		this.questionnaireProblemId = questionnaireProblemId;
	}

	public Long getQuestionnaireOptionId() {
		return questionnaireOptionId;
	}

	public void setQuestionnaireOptionId(Long questionnaireOptionId) {
		this.questionnaireOptionId = questionnaireOptionId;
	}

	

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdCard() {
		return IdCard;
	}

	public void setIdCard(String idCard) {
		IdCard = idCard;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
