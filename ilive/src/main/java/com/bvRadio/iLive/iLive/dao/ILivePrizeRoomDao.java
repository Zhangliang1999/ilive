package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILivePrizeRoom;

public interface ILivePrizeRoomDao {

	void save(ILivePrizeRoom iLivePrizeRoom);
	
	void update(ILivePrizeRoom iLivePrizeRoom);
	
	ILivePrizeRoom selectStartPrize(Integer roomId);
	
	ILivePrizeRoom selectPrize(Integer roomId,Long prizeId);
	
	void clearEnd(Integer roomId,List<Long> list);
}
