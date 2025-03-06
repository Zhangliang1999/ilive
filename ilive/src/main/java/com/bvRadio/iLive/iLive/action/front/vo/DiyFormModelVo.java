package com.bvRadio.iLive.iLive.action.front.vo;

import java.util.List;

public class DiyFormModelVo {

	private Integer questionType;

	private List<String> optionName;

	private Integer isNeed;
	
	private Integer needMsgValid;

	private String questionName;

	private Integer questionOrder;
	
	private Integer modelId;
	

	public Integer getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}

	public List<String> getOptionName() {
		return optionName;
	}

	public void setOptionName(List<String> optionName) {
		this.optionName = optionName;
	}

	public Integer getIsNeed() {
		return isNeed;
	}

	public void setIsNeed(Integer isNeed) {
		this.isNeed = isNeed;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public Integer getQuestionOrder() {
		return questionOrder;
	}

	public void setQuestionOrder(Integer questionOrder) {
		this.questionOrder = questionOrder;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public Integer getNeedMsgValid() {
		return needMsgValid;
	}

	public void setNeedMsgValid(Integer needMsgValid) {
		this.needMsgValid = needMsgValid;
	}
	
	
	

}
