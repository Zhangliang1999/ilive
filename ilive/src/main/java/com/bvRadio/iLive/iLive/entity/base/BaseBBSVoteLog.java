package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.BBSIpAddress;
import com.bvRadio.iLive.iLive.entity.ILiveUserEntity;

@SuppressWarnings("serial")
public abstract class BaseBBSVoteLog implements java.io.Serializable {

	/**
	 * 主键
	 */
	private Integer id;

	/**
	 * 标识ID
	 */
	private String uuid;

	/**
	 * 投票时间
	 */
	private Timestamp voteTime;

	/**
	 * 
	 */
	private Timestamp creationTime;

	/**
	 * 投票人
	 */
	private ILiveUserEntity user;


	/**
	 * 投票地址
	 */
	private BBSIpAddress bbsIpAddress;

	public BaseBBSVoteLog() {

	}

	public BaseBBSVoteLog(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BBSIpAddress getBbsIpAddress() {
		return bbsIpAddress;
	}

	public void setBbsIpAddress(BBSIpAddress bbsIpAddress) {
		this.bbsIpAddress = bbsIpAddress;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Timestamp getVoteTime() {
		return voteTime;
	}

	public void setVoteTime(Timestamp voteTime) {
		this.voteTime = voteTime;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}


	public ILiveUserEntity getUser() {
		return user;
	}

	public void setUser(ILiveUserEntity user) {
		this.user = user;
	}
}
