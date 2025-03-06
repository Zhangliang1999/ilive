package com.bvRadio.iLive.common.security.rememberme;

import com.bvRadio.iLive.common.security.AuthenticationException;

@SuppressWarnings("serial")
public class RememberMeAuthenticationException extends AuthenticationException {
	public RememberMeAuthenticationException() {
	}

	public RememberMeAuthenticationException(String msg) {
		super(msg);
	}
}
