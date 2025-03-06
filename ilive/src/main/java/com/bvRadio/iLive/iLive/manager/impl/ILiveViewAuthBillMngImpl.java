package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveViewAuthBillDao;
import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewAuthBillMng;


@Transactional
public class ILiveViewAuthBillMngImpl implements ILiveViewAuthBillMng {

	@Autowired
	private ILiveViewAuthBillDao iLiveViewAuthBillDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdManager;

	@Override
	public void addILiveViewAuthBill(ILiveViewAuthBill iLiveViewAuthBill) {
		Long nextId = fieldIdManager.getNextId("ilive_room_authbill", "auth_id", 1);
		iLiveViewAuthBill.setAuthId(nextId);
		iLiveViewAuthBillDao.addILiveViewAuthBill(iLiveViewAuthBill);
	}

	@Override
	public void updateILiveViewAuthBill(ILiveViewAuthBill iLiveViewAuthBill) {
		iLiveViewAuthBillDao.updateILiveViewAuthBill(iLiveViewAuthBill);
	}

	@Override
	@Transactional(readOnly=true)
	public ILiveViewAuthBill checkILiveViewAuthBill(String userId, Long liveEventId) {
		return iLiveViewAuthBillDao.checkILiveViewAuthBill(userId, liveEventId);
	}

	@Override
	@Transactional
	public boolean deleteLiveViewAuthBillByLiveEventId(Long iLiveEventId) {
		return iLiveViewAuthBillDao.deleteLiveViewAuthBillByLiveEventId(iLiveEventId);
	}

	@Override
	@Transactional
	public boolean deleteLiveViewAuthBillByLiveRoomId(Integer roomId,String userId) {
		// TODO Auto-generated method stub
		return iLiveViewAuthBillDao.deleteLiveViewAuthBillByLiveRoomId(roomId,userId);
	}

	@Override
	@Transactional(readOnly=true)
	public ILiveViewAuthBill checkILiveViewAuthBill(String userId, Integer roomId) {
		// TODO Auto-generated method stub
		return iLiveViewAuthBillDao.checkILiveViewAuthBill(userId, roomId);
	}

	@Override
	@Transactional(readOnly=true)
	public ILiveViewAuthBill checkILiveViewAuthBillByFileId(String userId, Long fileId) {
		// TODO Auto-generated method stub
		return iLiveViewAuthBillDao.checkILiveViewAuthBillByFileId(userId,fileId);
	}

	@Override
	public boolean deleteLiveViewAuthBillByfileId(String userId, Long fileId) {
		// TODO Auto-generated method stub
		return iLiveViewAuthBillDao.deleteLiveViewAuthBillByfileId(userId,fileId);
	}
	

}
