package com.bvRadio.iLive.iLive.entity.base;

import java.util.Set;

import com.bvRadio.iLive.iLive.entity.BBSDiyformOptionChoiceData;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;

/**
 * 自定义表单选项
 * 
 * @author administrator
 */
public abstract class BaseBBSDiyformOption {

	/**
	 * 选项ID
	 */
	private Long optionId;

	/**
	 * 选项名称
	 */
	private String optionName;

	/**
	 * 选项顺序
	 */
	private Integer optionOrder;

	/**
	 * 问题ID
	 */
	private BBSDiymodel bbsDiymodel;
	
	private Set<BBSDiyformOptionChoiceData> bbsDiyformOptionChoiceDatas;

	public Long getOptionId() {
		return optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public Integer getOptionOrder() {
		return optionOrder;
	}

	public void setOptionOrder(Integer optionOrder) {
		this.optionOrder = optionOrder;
	}

	public BBSDiymodel getBbsDiymodel() {
		return bbsDiymodel;
	}

	public void setBbsDiymodel(BBSDiymodel bbsDiymodel) {
		this.bbsDiymodel = bbsDiymodel;
	}

	public Set<BBSDiyformOptionChoiceData> getBbsDiyformOptionChoiceDatas() {
		return bbsDiyformOptionChoiceDatas;
	}

	public void setBbsDiyformOptionChoiceDatas(Set<BBSDiyformOptionChoiceData> bbsDiyformOptionChoiceDatas) {
		this.bbsDiyformOptionChoiceDatas = bbsDiyformOptionChoiceDatas;
	}
	
}
