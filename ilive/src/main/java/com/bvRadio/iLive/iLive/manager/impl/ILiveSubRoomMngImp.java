package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveSubLevelDao;
import com.bvRadio.iLive.iLive.dao.ILiveSubRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.IliveSubRoom;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubRoomMng;

@Transactional
@Service
public class ILiveSubRoomMngImp implements ILiveSubRoomMng {
   @Autowired ILiveSubRoomDao iLiveSubRoomDao;
	@Override
	public List<IliveSubRoom> selectILiveSubById(Long userId) {
		// TODO Auto-generated method stub
		return iLiveSubRoomDao.selectILiveSubById(userId);
	}

	@Override
	public void save(IliveSubRoom iLiveSubRoom) {
		// TODO Auto-generated method stub
		iLiveSubRoomDao.save(iLiveSubRoom);
	}

	@Override
	public Integer selectMaxId() {
		// TODO Auto-generated method stub
		return iLiveSubRoomDao.selectMaxId();
	}

	@Override
	public void delete(Long userId,String roomId) {
		// TODO Auto-generated method stub
		iLiveSubRoomDao.delete(userId,roomId);
	}

	@Override
	public void update(IliveSubRoom iLiveSubRoom) {
		// TODO Auto-generated method stub
		iLiveSubRoomDao.update(iLiveSubRoom);
	}

	@Override
	public IliveSubRoom getSubRoom(Long userId) {
		// TODO Auto-generated method stub
		return iLiveSubRoomDao.getSubRoom(userId);
	}

	@Override
	public void delete(String roomId) {
		// TODO Auto-generated method stub
		iLiveSubRoomDao.delete(roomId);
	}

	@Override
	public List<IliveSubRoom> getSubRoomByIds(String userIds) {
		// TODO Auto-generated method stub
		return iLiveSubRoomDao.getSubRoomByIds(userIds);
	}
	

}
