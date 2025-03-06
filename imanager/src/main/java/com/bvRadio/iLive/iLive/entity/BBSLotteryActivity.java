package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.bvRadio.iLive.iLive.entity.base.BaseBBSLotteryActivity;

@SuppressWarnings("serial")
public class BBSLotteryActivity extends BaseBBSLotteryActivity implements Serializable {

	public BBSLotteryActivity() {
		super();
	}


	public BBSLotteryActivity(Integer lotteryId) {
		super(lotteryId);
	}

}
