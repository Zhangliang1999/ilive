package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveFCode;

public class ILiveFCode extends BaseILiveFCode {
	/**
	 * 状态：未使用
	 */
	public final static Integer STATUS_NEW = 1;
	/**
	 * 状态：已使用
	 */
	public final static Integer STATUS_USED = 2;
	/**
	 * 状态：作废
	 */
	public final static Integer STATUS_CANCELED = 3;

	public void initFieldValue() {
		if (null == getStatus()) {
			setStatus(STATUS_NEW);
		}
		if (null == getStartTime()) {
			setStartTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getEndTime()) {
			setEndTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
	}
	
	private ILiveManager bindUser;

	public ILiveManager getBindUser() {
		return bindUser;
	}

	public void setBindUser(ILiveManager bindUser) {
		this.bindUser = bindUser;
	}
	
}
