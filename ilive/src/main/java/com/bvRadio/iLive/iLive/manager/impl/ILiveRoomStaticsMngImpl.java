package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveRoomStaticsDao;
import com.bvRadio.iLive.iLive.entity.ILiveRoomStatics;
import com.bvRadio.iLive.iLive.manager.ILiveRoomStaticsMng;

public class ILiveRoomStaticsMngImpl implements ILiveRoomStaticsMng {

	@Autowired
	private ILiveRoomStaticsDao iLiveRoomStaticsDao;

	@Override
	@Transactional
	public void addRoomStatic(Long eventId, Long num) {
		iLiveRoomStaticsDao.saveRoomStaticDao(eventId, num);
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveRoomStatics getRoomStatic(Long eventId) {
		return iLiveRoomStaticsDao.getRoomStaticsByEventId(eventId);
	}

	@Override
	@Transactional
	public void updateRoomStatic(Long liveEventId, Long showNum) {
		iLiveRoomStaticsDao.updateRoomStatic(liveEventId, showNum);
	}

	@Override
	@Transactional
	public void initRoom(Long liveEventId, Long nowNum) {
		try {
			ILiveRoomStatics roomStaticsByEventId = iLiveRoomStaticsDao.getRoomStaticsByEventId(liveEventId);
			if (roomStaticsByEventId == null) {
				roomStaticsByEventId = new ILiveRoomStatics();
				iLiveRoomStaticsDao.saveRoomStaticDao(liveEventId, nowNum);
			} else {
				roomStaticsByEventId.setShowNum(nowNum);
				iLiveRoomStaticsDao.updateRoomStatic(liveEventId, nowNum);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
