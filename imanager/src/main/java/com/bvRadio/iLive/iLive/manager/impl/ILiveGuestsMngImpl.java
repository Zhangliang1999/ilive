package com.bvRadio.iLive.iLive.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.dao.ILiveGuestsDao;
import com.bvRadio.iLive.iLive.entity.ILiveGuests;
import com.bvRadio.iLive.iLive.manager.ILiveGuestsMng;
@Service
@Transactional
public class ILiveGuestsMngImpl implements ILiveGuestsMng {
	
	@Autowired
	private ILiveGuestsDao iLiveGuestsDao;//嘉宾
	
	@Autowired
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao;//主键
	
	@Override
	public ILiveGuests addILiveGuest(ILiveGuests iLiveGuests) {
		try {
			Long nextId = iLiveFieldIdManagerDao.getNextId("ilive_guests", "guests_id", 1);
			iLiveGuests.setGuestsId(nextId);
			iLiveGuests = iLiveGuestsDao.insertILiveGuests(iLiveGuests);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iLiveGuests;
	}

	@Override
	public List<ILiveGuests> selectILiveGuestsList(Integer roomId,boolean isDelete) {
		List<ILiveGuests> list = new ArrayList<ILiveGuests>();
		try {
			list = iLiveGuestsDao.selectILiveGuestsList(roomId,isDelete);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public ILiveGuests selectILiveGuestsByID(Long guestsId) {
		ILiveGuests iLiveGuests = new ILiveGuests();
		try {
			iLiveGuests = iLiveGuestsDao.selectILiveGuestsByID(guestsId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iLiveGuests;
	}

	@Override
	public void deleteILiveGuests(Long guestsId) {
		try {
			iLiveGuestsDao.deleteILiveGuests(guestsId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
