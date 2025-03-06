package com.jwzt.billing.entity;

/**
* @author ysf
*/
import java.sql.Timestamp;
import java.util.List;

import com.jwzt.billing.entity.base.BaseFunctionInfo;

@SuppressWarnings("serial")
public class FunctionInfo extends BaseFunctionInfo {

	private List<FunctionInfo> childList;

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public FunctionInfo() {
		super();
	}

	public List<FunctionInfo> getChildList() {
		return childList;
	}

	public void setChildList(List<FunctionInfo> childList) {
		this.childList = childList;
	}

}
