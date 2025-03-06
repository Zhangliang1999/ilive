package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveRoomSuggestDao;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveRoomSuggest;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomSuggestMng;

public class ILiveRoomSuggestMngImpl implements ILiveRoomSuggestMng {

	@Autowired
	private ILiveRoomSuggestDao iLiveRoomSuggestDao;

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;

	@Override
	@Transactional
	public void addILiveRoomSuggest(ILiveLiveRoom iliveRoom, String content, Long userId) {
		if (userId == null) {
			userId = 0L;
		}
		if (iliveRoom == null) {
			ILiveRoomSuggest roomSuggest = new ILiveRoomSuggest();
			System.out.println("roomSuggest："+roomSuggest);
			Long nextId = iLiveFieldIdManagerMng.getNextId("ilive_room_suggest", "suggest_id", 1);
			System.out.println("nextId："+nextId);
			roomSuggest.setSuggestId(nextId);
			roomSuggest.setRoomId(0);
			roomSuggest.setLiveEventId(0L);
			roomSuggest.setUserId(userId);
			roomSuggest.setContent(content);
			iLiveRoomSuggestDao.addRoomSuggest(roomSuggest);
		} else {
			ILiveRoomSuggest roomSuggest = new ILiveRoomSuggest();
			Long nextId = iLiveFieldIdManagerMng.getNextId("ilive_room_suggest", "suggest_id", 1);
			roomSuggest.setSuggestId(nextId);
			roomSuggest.setRoomId(iliveRoom.getRoomId());
			roomSuggest.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
			roomSuggest.setUserId(userId);
			roomSuggest.setContent(content);
			iLiveRoomSuggestDao.addRoomSuggest(roomSuggest);
		}

	}

}
