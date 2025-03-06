package com.jwzt.billing.exception;

import java.util.List;

/**
 * @author ysf
 * 
 *         结算不是一个企业异常
 */
public class NotSameEnterpriseException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<Long> errorFlowIdList;

	public NotSameEnterpriseException(String message, List<Long> errorFlowIdList) {
		super(message);
		this.errorFlowIdList = errorFlowIdList;
	}

	public List<Long> getErrorFlowIdList() {
		return errorFlowIdList;
	}

}
