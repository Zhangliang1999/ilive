package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveRoomStatics;

public interface ILiveRoomStaticsDao {
	
	
	public ILiveRoomStatics getRoomStaticsByEventId(Long eventId);

	public void saveRoomStaticDao(Long liveEventId, Long num);

	public void updateRoomStatic(Long liveEventId, Long showNum);

	
	

}
