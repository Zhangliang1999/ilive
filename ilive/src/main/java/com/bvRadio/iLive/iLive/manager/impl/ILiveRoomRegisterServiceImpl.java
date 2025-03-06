package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveRoomRegisterDao;
import com.bvRadio.iLive.iLive.entity.ILiveRoomRegister;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomRegisterService;

@Service
@Transactional
public class ILiveRoomRegisterServiceImpl implements ILiveRoomRegisterService{

	@Autowired
	private ILiveRoomRegisterDao registerDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;
	
	@Override
	@Transactional
	public ILiveRoomRegister saveILiveRoomRegiste(ILiveRoomRegister iLiveRoomRegister) {
		long id = fieldIdMng.getNextId("ilive_room_register", "id", 1);
		iLiveRoomRegister.setId(id);
		Timestamp createTime = new Timestamp(new Date().getTime());
		iLiveRoomRegister.setCreateTime(createTime);
		registerDao.saveILiveRoomRegiste(iLiveRoomRegister);
		return iLiveRoomRegister;
	}

	@Override
	public ILiveRoomRegister queryILiveRoomRegisterByUserIdAndRoomId(String userId, Integer roomId) {
		return null;
	}

	@Override
	public boolean queryILiveIsRegister(String userId, Long liveEventId) {
		return registerDao.queryILiveIsRegister(userId, liveEventId);
	}

	@Override
	public boolean queryIliveRoomRegister(String userId, Integer roomId) {
		return registerDao.queryIliveRoomRegister(userId, roomId);
	}

	@Override
	public boolean queryMediaRegister(String userId, Long fileId) {
		return registerDao.queryMediaRegister(userId,fileId);
	}

	@Override
	public Pagination querySignByRoomId(Integer roomId,Long liveEventId,String name,Integer state,Integer pageNo,Integer pageSize) {
		return registerDao.querySignByRoomId(roomId,liveEventId, name, state, pageNo, pageSize);
	}

	@Override
	public Long MediaRegisterCount(Long fileId) {
		// TODO Auto-generated method stub
		return registerDao.MediaRegisterCount(fileId);
	}

	@Override
	public Long ILiveIsRegisterCount(Long liveEventId) {
		// TODO Auto-generated method stub
		return registerDao.ILiveIsRegisterCount(liveEventId);
	}

}
