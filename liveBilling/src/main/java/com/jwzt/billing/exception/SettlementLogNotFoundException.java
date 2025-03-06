package com.jwzt.billing.exception;

/**
 * @author ysf
 * 
 *         结算单不存在异常
 */
public class SettlementLogNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public SettlementLogNotFoundException(String message) {
		super(message);
	}

}
