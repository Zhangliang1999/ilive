package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILivePrizeRoom;

public interface ILivePrizeRoomMng {

	void save(ILivePrizeRoom iLivePrizeRoom);
	
	void update(ILivePrizeRoom iLivePrizeRoom);
	
	ILivePrizeRoom selectStartPrize(Integer roomId);
	
	ILivePrizeRoom selectPrize(Integer roomId,Long prizeId);
	
	boolean isStartPrize(Integer roomId);
	
	void clearEnd(Integer roomId,Integer enterpriseId);
}
