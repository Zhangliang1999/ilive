package com.bvRadio.iLive.iLive.entity.base;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.bvRadio.iLive.iLive.entity.BBSPrize;

@SuppressWarnings("serial")
public abstract class BaseBBSLotteryActivity implements Serializable {

	private Integer lotteryId;
	private Integer lotteryType;
	private Integer everyoneMaxLotteryCount;
	private Integer everyoneEverydayMaxLotteryCount;
	private String notifierproPlus;
	private Date lotteryStartTime;
	private Date lotteryEndTime;
	private Integer closeComment;
	private Integer creditspolicy;
	private Integer enableTerminalRestrictions;
	private Integer enableRepeat;

	private Set<BBSPrize> prizes;

	public BaseBBSLotteryActivity() {

	}

	public BaseBBSLotteryActivity(Integer lotteryId) {
		this.lotteryId = lotteryId;
	}

	

	public Integer getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(Integer lotteryId) {
		this.lotteryId = lotteryId;
	}

	public Integer getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(Integer lotteryType) {
		this.lotteryType = lotteryType;
	}

	public Integer getEveryoneMaxLotteryCount() {
		return everyoneMaxLotteryCount;
	}

	public void setEveryoneMaxLotteryCount(Integer everyoneMaxLotteryCount) {
		this.everyoneMaxLotteryCount = everyoneMaxLotteryCount;
	}

	public Integer getEveryoneEverydayMaxLotteryCount() {
		return everyoneEverydayMaxLotteryCount;
	}

	public void setEveryoneEverydayMaxLotteryCount(Integer everyoneEverydayMaxLotteryCount) {
		this.everyoneEverydayMaxLotteryCount = everyoneEverydayMaxLotteryCount;
	}

	public String getNotifierproPlus() {
		return notifierproPlus;
	}

	public void setNotifierproPlus(String notifierproPlus) {
		this.notifierproPlus = notifierproPlus;
	}

	public Date getLotteryStartTime() {
		return lotteryStartTime;
	}

	public void setLotteryStartTime(Date lotteryStartTime) {
		this.lotteryStartTime = lotteryStartTime;
	}

	public Date getLotteryEndTime() {
		return lotteryEndTime;
	}

	public void setLotteryEndTime(Date lotteryEndTime) {
		this.lotteryEndTime = lotteryEndTime;
	}

	public Integer getCloseComment() {
		return closeComment;
	}

	public void setCloseComment(Integer closeComment) {
		this.closeComment = closeComment;
	}

	public Integer getCreditspolicy() {
		return creditspolicy;
	}

	public void setCreditspolicy(Integer creditspolicy) {
		this.creditspolicy = creditspolicy;
	}

	public Integer getEnableTerminalRestrictions() {
		return enableTerminalRestrictions;
	}

	public void setEnableTerminalRestrictions(Integer enableTerminalRestrictions) {
		this.enableTerminalRestrictions = enableTerminalRestrictions;
	}


	public Set<BBSPrize> getPrizes() {
		return prizes;
	}

	public void setPrizes(Set<BBSPrize> prizes) {
		this.prizes = prizes;
	}

	public Integer getEnableRepeat() {
		return enableRepeat;
	}

	public void setEnableRepeat(Integer enableRepeat) {
		this.enableRepeat = enableRepeat;
	}
	
}
