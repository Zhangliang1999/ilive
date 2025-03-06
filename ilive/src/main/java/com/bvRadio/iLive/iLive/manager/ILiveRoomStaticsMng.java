package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveRoomStatics;

public interface ILiveRoomStaticsMng {
	
	
	public void addRoomStatic(Long eventId,Long num);
	
	public ILiveRoomStatics getRoomStatic(Long eventId);

	public void updateRoomStatic(Long liveEventId, Long showNum);

	public void initRoom(Long liveEventId, Long nowNum);

}
