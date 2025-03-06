package com.bvRadio.iLive.iLive.entity;

import com.bvRadio.iLive.iLive.entity.base.BaseBBSPrize;

@SuppressWarnings("serial")
public class BBSPrize extends BaseBBSPrize implements java.io.Serializable {

	public BBSPrize() {
		super();
	}

	public BBSPrize(Integer prizeId, String awardName, String lotteryName, Integer lotteryNumber, String winningProbability, Integer balance,
			String prizeImg, BBSLotteryActivity lotteryActivity) {
		super(prizeId, awardName, lotteryName, lotteryNumber, winningProbability, balance, prizeImg, lotteryActivity);
	}

	public BBSPrize(Integer prizeId) {
		super(prizeId);
	}

}
