package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveGuestsDao;
import com.bvRadio.iLive.iLive.entity.ILiveGuests;
import com.bvRadio.iLive.iLive.manager.ILiveGuestsMng;
@Service
@Transactional
public class ILiveGuestsMngImpl implements ILiveGuestsMng {
	
	@Autowired
	private ILiveGuestsDao iLiveGuestsDao;//嘉宾
	
	@Override
	public void addILiveGuest(ILiveGuests iLiveGuests) {
		try {
			iLiveGuestsDao.insertILiveGuests(iLiveGuests);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ILiveGuests selectILiveGuestsById(Integer roomId) {
		ILiveGuests iLiveGuests = new ILiveGuests();
		try {
			iLiveGuests = iLiveGuestsDao.selectILiveGuestsByID(roomId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iLiveGuests;
	}

	@Override
	public void updateILiveGuests(ILiveGuests iLiveGuests) {
		try {
			iLiveGuestsDao.updateILiveGuests(iLiveGuests);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
