package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveSignRoom;

public interface ILiveSignRoomMng {

	void save(ILiveSignRoom iLiveSignRoom);
	
	void update(ILiveSignRoom iLiveSignRoom);
	
	ILiveSignRoom selectStartSign(Integer roomId);
	
	ILiveSignRoom selectSign(Integer roomId,Long prizeId);
	
	boolean isStartSign(Integer roomId);

	List<ILiveSignRoom> selectAllSign(Integer roomId);
}
