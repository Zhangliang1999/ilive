package com.jwzt.common.exception;

/**
 * @author ysf
 */
@SuppressWarnings("serial")
public class WrongRenderJSONException extends Exception {

	public WrongRenderJSONException() {
		super();
	}

	public WrongRenderJSONException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrongRenderJSONException(String message) {
		super(message);
	}

	public WrongRenderJSONException(Throwable cause) {
		super(cause);
	}

}
