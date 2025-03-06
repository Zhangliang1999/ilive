package com.bvRadio.iLive.core.entity.base;

import java.io.Serializable;

import com.bvRadio.iLive.core.entity.Authentication;

@SuppressWarnings("serial")
public abstract class BaseAuthentication implements Serializable {

	public BaseAuthentication() {
		initialize();
	}

	public BaseAuthentication(java.lang.String id) {
		this.setId(id);
		initialize();
	}

	public BaseAuthentication(java.lang.String id, java.lang.Integer uid, java.lang.String username, java.util.Date loginTime,
			java.lang.String loginIp, java.util.Date updateTime, java.lang.String appId, java.lang.Integer appType) {

		this.setId(id);
		this.setUid(uid);
		this.setUsername(username);
		this.setLoginTime(loginTime);
		this.setLoginIp(loginIp);
		this.setUpdateTime(updateTime);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	private java.lang.String id;

	private java.lang.Integer uid;
	private java.lang.String username;
	private java.lang.String email;
	private java.util.Date loginTime;
	private java.lang.String loginIp;
	private java.util.Date updateTime;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	public java.lang.Integer getUid() {
		return uid;
	}

	public void setUid(java.lang.Integer uid) {
		this.uid = uid;
	}

	public java.lang.String getUsername() {
		return username;
	}

	public void setUsername(java.lang.String username) {
		this.username = username;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.util.Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(java.util.Date loginTime) {
		this.loginTime = loginTime;
	}

	public java.lang.String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(java.lang.String loginIp) {
		this.loginIp = loginIp;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof Authentication))
			return false;
		else {
			Authentication authentication = (Authentication) obj;
			if (null == this.getId() || null == authentication.getId())
				return false;
			else
				return (this.getId().equals(authentication.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}