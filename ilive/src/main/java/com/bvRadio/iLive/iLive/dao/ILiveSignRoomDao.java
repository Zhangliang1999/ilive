package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveSignRoom;

public interface ILiveSignRoomDao {

	void save(ILiveSignRoom iLiveSignRoom);
	
	void update(ILiveSignRoom iLiveSignRoom);
	
	ILiveSignRoom selectStartSign(Integer roomId);
	
	ILiveSignRoom selectSign(Integer roomId,Long prizeId);
	List<ILiveSignRoom> selectAllSign(Integer roomId);
}
