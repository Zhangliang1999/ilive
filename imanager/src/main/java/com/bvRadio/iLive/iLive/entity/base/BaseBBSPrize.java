package com.bvRadio.iLive.iLive.entity.base;

import com.bvRadio.iLive.iLive.entity.BBSLotteryActivity;

@SuppressWarnings("serial")
public class BaseBBSPrize implements java.io.Serializable {

	private Integer prizeId;
	private String awardName;
	private String lotteryName;
	private Integer lotteryNumber;
	private String winningProbability;
	private Integer balance;
	private String prizeImg;
	private BBSLotteryActivity lotteryActivity;
	
	
	public BaseBBSPrize() {

	}

	public BaseBBSPrize(Integer prizeId) {
		this.prizeId = prizeId;
	}

	public BaseBBSPrize(Integer prizeId, String awardName, String lotteryName, Integer lotteryNumber, String winningProbability, Integer balance,
			String prizeImg, BBSLotteryActivity lotteryActivity) {
		super();
		this.prizeId = prizeId;
		this.awardName = awardName;
		this.lotteryName = lotteryName;
		this.lotteryNumber = lotteryNumber;
		this.winningProbability = winningProbability;
		this.balance = balance;
		this.prizeImg = prizeImg;
		this.lotteryActivity = lotteryActivity;
	}

	public Integer getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(Integer prizeId) {
		this.prizeId = prizeId;
	}

	public String getAwardName() {
		return awardName;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public Integer getLotteryNumber() {
		return lotteryNumber;
	}

	public void setLotteryNumber(Integer lotteryNumber) {
		this.lotteryNumber = lotteryNumber;
	}

	public String getWinningProbability() {
		return winningProbability;
	}

	public void setWinningProbability(String winningProbability) {
		this.winningProbability = winningProbability;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public String getPrizeImg() {
		return prizeImg;
	}

	public void setPrizeImg(String prizeImg) {
		this.prizeImg = prizeImg;
	}

	public BBSLotteryActivity getLotteryActivity() {
		return lotteryActivity;
	}

	public void setLotteryActivity(BBSLotteryActivity lotteryActivity) {
		this.lotteryActivity = lotteryActivity;
	}

}
