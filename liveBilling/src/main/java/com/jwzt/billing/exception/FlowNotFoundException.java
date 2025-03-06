package com.jwzt.billing.exception;

import java.util.List;

/**
 * @author ysf
 * 
 *         流水不存在异常
 */
public class FlowNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<Long> errorFlowIdList;

	public FlowNotFoundException(String message, List<Long> errorFlowIdList) {
		super(message);
		this.errorFlowIdList = errorFlowIdList;
	}

	public List<Long> getErrorFlowIdList() {
		return errorFlowIdList;
	}

}
