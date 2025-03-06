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

}
