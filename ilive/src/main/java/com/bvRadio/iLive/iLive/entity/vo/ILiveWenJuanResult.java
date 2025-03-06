package com.bvRadio.iLive.iLive.entity.vo;

public class ILiveWenJuanResult {
   /**
    * 问卷活动id
    */
   private Long questionnaireId;
   /**
    * 问卷活动名称
    */
   private String questionnaireName;
   /**
    * 问卷题号
    */
   private String questionnaireProblemIndex;
   /**
    * 题目名称
    */
   private String questionnaireProblem;
   /**
    * 题号对应的答案
    */
   private String questionnaireProblemAnswer;
   /**
    * 选项图片地址
    * @return
    */
   private String imgUrl;
   /**
    * 用户ID
    * @return
    */
   private Long userId;
   /**
    * 用户手机号
    * @return
    */
   private String mobile;
   /**
    * 用户身份证号
    * @return
    */
   private String idcard;
   /**
    * 用户名
    * @return
    */
   private String userName;
public Long getQuestionnaireId() {
	return questionnaireId;
}
public void setQuestionnaireId(Long questionnaireId) {
	this.questionnaireId = questionnaireId;
}
public String getQuestionnaireName() {
	return questionnaireName;
}
public void setQuestionnaireName(String questionnaireName) {
	this.questionnaireName = questionnaireName;
}
public String getQuestionnaireProblemIndex() {
	return questionnaireProblemIndex;
}
public void setQuestionnaireProblemIndex(String questionnaireProblemIndex) {
	this.questionnaireProblemIndex = questionnaireProblemIndex;
}

public String getQuestionnaireProblem() {
	return questionnaireProblem;
}
public void setQuestionnaireProblem(String questionnaireProblem) {
	this.questionnaireProblem = questionnaireProblem;
}
public String getQuestionnaireProblemAnswer() {
	return questionnaireProblemAnswer;
}
public void setQuestionnaireProblemAnswer(String questionnaireProblemAnswer) {
	this.questionnaireProblemAnswer = questionnaireProblemAnswer;
}
public String getImgUrl() {
	return imgUrl;
}
public void setImgUrl(String imgUrl) {
	this.imgUrl = imgUrl;
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
public String getIdcard() {
	return idcard;
}
public void setIdcard(String idcard) {
	this.idcard = idcard;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
   
}
