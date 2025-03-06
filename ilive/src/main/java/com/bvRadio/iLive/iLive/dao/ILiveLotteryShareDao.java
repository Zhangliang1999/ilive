package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveLotteryShare;

public interface ILiveLotteryShareDao {

	void save(ILiveLotteryShare iLiveLotteryShare);
	
	ILiveLotteryShare getRecordByUserId(Long userId,Long lotteryId);
}
