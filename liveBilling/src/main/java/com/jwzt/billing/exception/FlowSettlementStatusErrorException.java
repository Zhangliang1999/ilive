package com.jwzt.billing.exception;

import java.util.List;

/**
 * @author ysf
 * 
 *         流水结算状态异常
 */
public class FlowSettlementStatusErrorException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<Long> errorFlowIdList;

	public FlowSettlementStatusErrorException(String message, List<Long> errorFlowIdList) {
		super(message);
		this.errorFlowIdList = errorFlowIdList;
	}

	public List<Long> getErrorFlowIdList() {
		return errorFlowIdList;
	}

}
