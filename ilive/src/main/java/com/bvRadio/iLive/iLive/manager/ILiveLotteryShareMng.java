package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveLotteryShare;

public interface ILiveLotteryShareMng {

	void save(ILiveLotteryShare iLiveLotteryShare);
	
	ILiveLotteryShare getRecordByUserIdAndLotteryId(Long userId,Long lottery);
	
	int share(Integer roomId,Long userId);
}
