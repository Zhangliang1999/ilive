package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.BBSIpAddress;
import com.bvRadio.iLive.iLive.entity.ILiveUserEntity;

public abstract class BaseBBSLotteryLog {

	private Integer id;
	private String uuid;
	private ILiveUserEntity user;
	private String lotteryName;
	private Timestamp creationTime;
	private BBSIpAddress bbsIpAddress;

	public BaseBBSLotteryLog() {

	}

	public BaseBBSLotteryLog(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}


	public BBSIpAddress getBbsIpAddress() {
		return bbsIpAddress;
	}

	public void setBbsIpAddress(BBSIpAddress bbsIpAddress) {
		this.bbsIpAddress = bbsIpAddress;
	}
	
}
