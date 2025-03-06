package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 问卷问题
 * @author Wei
 *
 */
public class ILiveQuestionnaireProblem implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3778884200560390042L;

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 问卷活动问题id
	 */
	private Long questionnaireId;
	
	/**
	 * 问卷问题
	 */
	private String problemName;
	
	/**
	 * 问卷样式    1文字    2图片加文字  3 问答
	 */
	private Integer style;
	
	/**
	 * 最大投票个数
	 */
	private Integer maxQuestionnaireNum;
	
	private List<ILiveQuestionnaireOption> listOption;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;
    /**
     * 问答题的答案
     * @return
     */
	private List<ILiveQuestionnairePeople> listPeople;
    /**
     * 问卷每个问题对应的标号
     * @return
     */
	private Long problemIndex;
	public List<ILiveQuestionnaireOption> getListOption() {
		return listOption;
	}

	public void setListOption(List<ILiveQuestionnaireOption> listOption) {
		this.listOption = listOption;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public List<ILiveQuestionnairePeople> getListPeople() {
		return listPeople;
	}

	public void setListPeople(List<ILiveQuestionnairePeople> listPeople) {
		this.listPeople = listPeople;
	}

	public Long getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Long questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	

	public Integer getMaxQuestionnaireNum() {
		return maxQuestionnaireNum;
	}

	public void setMaxQuestionnaireNum(Integer maxQuestionnaireNum) {
		this.maxQuestionnaireNum = maxQuestionnaireNum;
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

	public Long getProblemIndex() {
		return problemIndex;
	}

	public void setProblemIndex(Long problemIndex) {
		this.problemIndex = problemIndex;
	}

	
}
