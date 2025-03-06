package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveSignRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveSignRoom;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSignRoomMng;

@Transactional
public class ILiveSignRoomMngImpl implements ILiveSignRoomMng {

	@Autowired
	private ILiveSignRoomDao iLiveSignRoomDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public void save(ILiveSignRoom iLiveSignRoom) {
		Long nextId = fieldIdMng.getNextId("ilive_prize_room", "id", 1);
		iLiveSignRoom.setId(nextId);
		iLiveSignRoomDao.save(iLiveSignRoom);
	}

	@Override
	public void update(ILiveSignRoom iLiveSignRoom) {
		iLiveSignRoomDao.update(iLiveSignRoom);
	}

	@Override
	public ILiveSignRoom selectStartSign(Integer roomId) {
		return iLiveSignRoomDao.selectStartSign(roomId);
	}

	@Override
	public ILiveSignRoom selectSign(Integer roomId, Long prizeId) {
		return iLiveSignRoomDao.selectSign(roomId, prizeId);
	}

	@Override
	public boolean isStartSign(Integer roomId) {
		ILiveSignRoom prizeRoom = selectStartSign(roomId);
		return prizeRoom!=null;
	}

	@Override
	public List<ILiveSignRoom> selectAllSign(Integer roomId) {
		
		return iLiveSignRoomDao.selectAllSign(roomId);
	}

}
