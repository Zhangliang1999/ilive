package com.bvRadio.iLive.core.entity;

import java.util.Date;

import com.bvRadio.iLive.core.entity.base.BaseUnifiedUser;


public class UnifiedUser extends BaseUnifiedUser {
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public UnifiedUser() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public UnifiedUser(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public UnifiedUser(java.lang.Integer id, java.lang.String username, java.lang.String password,
			java.util.Date registerTime, java.lang.String registerIp, java.lang.Integer loginCount,
			java.lang.Integer errorCount, java.lang.Boolean activation, java.lang.String salt) {

		super(id, username, password, registerTime, registerIp, loginCount, errorCount, activation, salt);
	}

	/* [CONSTRUCTOR MARKER END] */

	public void init() {
		if (getLoginCount() == null) {
			setLoginCount(0);
		}
		if (getErrorCount() == null) {
			setErrorCount(0);
		}
		if (getRegisterIp() == null) {
			setRegisterIp("");
		}
		if (getRegisterTime() == null) {
			setRegisterTime(new Date());
		}
		if (getLastLoginIp() == null) {
			setLastLoginIp("");
		}
		if (getLastLoginTime() == null) {
			setLastLoginTime(new Date());
		}
		if (getActivation() == null) {
			setActivation(true);
		}
	}

}