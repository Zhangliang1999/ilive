package com.bvRadio.iLive.core.entity;

import java.sql.Timestamp;
import java.util.Date;

import com.bvRadio.iLive.core.entity.base.BaseAuthentication;

public class Authentication extends BaseAuthentication {
	private static final long serialVersionUID = 1L;

	public void init() {
		Date now = new Timestamp(System.currentTimeMillis());
		setLoginTime(now);
		setUpdateTime(now);
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Authentication() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Authentication(java.lang.String id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Authentication(java.lang.String id, java.lang.Integer uid, java.lang.String username, java.util.Date loginTime, java.lang.String loginIp,
			java.util.Date updateTime, java.lang.String appId, java.lang.Integer appType) {

		super(id, uid, username, loginTime, loginIp, updateTime, appId, appType);
	}

	/* [CONSTRUCTOR MARKER END] */

}