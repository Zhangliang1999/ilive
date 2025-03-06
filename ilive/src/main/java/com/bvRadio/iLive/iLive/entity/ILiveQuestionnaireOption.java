package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 问卷选项
 * @author Wei
 *
 */
public class ILiveQuestionnaireOption implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 2227331960030646919L;

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 投票活动id
	 */
	private Long questionnaireId;
	
	/**
	 * 投票问题id
	 */
	private Long questionnaireProblemId;
	
	/**
	 * 选项内容
	 */
	private String content;
	
	/**
	 * 选项图片
	 */
	private String contentImg;
	
	private Integer num;
	
	private String percen; 
	
	private Timestamp createTime;
	private Timestamp updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getPercen() {
		return percen;
	}

	public void setPercen(String percen) {
		this.percen = percen;
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

	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentImg() {
		return contentImg;
	}

	public void setContentImg(String contentImg) {
		this.contentImg = contentImg;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
}
