package com.bvRadio.iLive.iLive.realm;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordLoginTypeToken extends UsernamePasswordToken {

	/**
	 */
	private static final long serialVersionUID = 1L;
	private String loginType = "0";// 0为用户密码登录，1为手机验证码登录  2 用户名 密码登录
	private String utoken;
	private String uphoneNum;

	public String getUphoneNum() {
		return uphoneNum;
	}

	public void setUphoneNum(String uphoneNum) {
		this.uphoneNum = uphoneNum;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getUtoken() {
		return utoken;
	}

	public void setUtoken(String utoken) {
		this.utoken = utoken;
	}

	public UsernamePasswordLoginTypeToken() {
		super();
	}

	public UsernamePasswordLoginTypeToken(final String username, final String password, final boolean rememberMe,
			final String host, String loginType, String utoken, String uphoneNum) {
		super(username, password, rememberMe, host);
		this.loginType = loginType;
		this.utoken = utoken;
		this.uphoneNum = uphoneNum;
	}
}
