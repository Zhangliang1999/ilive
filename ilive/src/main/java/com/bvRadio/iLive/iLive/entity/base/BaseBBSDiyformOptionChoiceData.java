package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.BBSDiyformOption;
import com.bvRadio.iLive.iLive.entity.BBSIpAddress;
import com.bvRadio.iLive.iLive.entity.ILiveIpAddress;
import com.bvRadio.iLive.iLive.entity.ILiveUserEntity;

public abstract class BaseBBSDiyformOptionChoiceData {

	/**
	 * ID
	 */
	private Long choiceId;
	
	/**
	 * 投票人
	 */
	private ILiveUserEntity user;
	
	/**
	 * 对应选项
	 */
	private BBSDiyformOption bbsDiyformOption;
	
	/**
	 * 投票地址
	 */
	private ILiveIpAddress iLiveIpAddress;
	
	private Timestamp createTime;

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public Long getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(Long choiceId) {
		this.choiceId = choiceId;
	}

	public ILiveUserEntity getUser() {
		return user;
	}

	public void setUser(ILiveUserEntity user) {
		this.user = user;
	}

	public BBSDiyformOption getBbsDiyformOption() {
		return bbsDiyformOption;
	}

	public void setBbsDiyformOption(BBSDiyformOption bbsDiyformOption) {
		this.bbsDiyformOption = bbsDiyformOption;
	}

	public ILiveIpAddress getiLiveIpAddress() {
		return iLiveIpAddress;
	}

	public void setiLiveIpAddress(ILiveIpAddress iLiveIpAddress) {
		this.iLiveIpAddress = iLiveIpAddress;
	}

}
