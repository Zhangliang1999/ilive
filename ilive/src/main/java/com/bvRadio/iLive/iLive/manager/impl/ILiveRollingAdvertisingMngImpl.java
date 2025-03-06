package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveRollingAdvertisingDao;
import com.bvRadio.iLive.iLive.entity.ILiveRollingAdvertising;
import com.bvRadio.iLive.iLive.manager.ILiveRollingAdvertisingMng;
@Service
public class ILiveRollingAdvertisingMngImpl implements
		ILiveRollingAdvertisingMng {
	@Autowired
	private ILiveRollingAdvertisingDao iLiveRollingAdvertisingDao;
	
	@Override
	@Transactional
	public ILiveRollingAdvertising selectILiveRollingAdvertising(Integer roomId) {
		ILiveRollingAdvertising iLiveRollingAdvertising = new ILiveRollingAdvertising();
		try {
			iLiveRollingAdvertising = iLiveRollingAdvertisingDao.selectILiveRollingAdvertising(roomId);
			if(iLiveRollingAdvertising==null){
				iLiveRollingAdvertising = new ILiveRollingAdvertising();
				iLiveRollingAdvertising.setRoomId(roomId);
				iLiveRollingAdvertising.setStartType(0);
				iLiveRollingAdvertisingDao.insertILiveRollingAdvertising(iLiveRollingAdvertising);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iLiveRollingAdvertising;
	}

	@Override
	@Transactional
	public ILiveRollingAdvertising updateILiveRollingAdvertising(
			ILiveRollingAdvertising iLiveRollingAdvertising) {
		try {
			iLiveRollingAdvertisingDao.updateILiveRollingAdvertising(iLiveRollingAdvertising);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
