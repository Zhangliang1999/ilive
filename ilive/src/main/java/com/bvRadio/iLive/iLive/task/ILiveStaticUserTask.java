package com.bvRadio.iLive.iLive.task;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;

import com.bvRadio.iLive.iLive.entity.ILiveRoomStatics;
import com.bvRadio.iLive.iLive.manager.ILiveRoomStaticsMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;

public class ILiveStaticUserTask extends TimerTask {

	@Override
	public void run() {
		ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
		ILiveRoomStaticsMng iLiveRoomStaticsMng = (ILiveRoomStaticsMng) applicationContext
				.getBean("iLiveRoomStaticsMng");
		ConcurrentHashMap<Long, Long> liveEventUserCache = ApplicationCache.LiveEventUserCache;
		for (Map.Entry<Long, Long> entry : liveEventUserCache.entrySet()) {
			Long liveEventId = entry.getKey();
			Long num = entry.getValue();
			num = num == null ? 0 : num;
			ILiveRoomStatics roomStaticsByEventId = iLiveRoomStaticsMng.getRoomStatic(liveEventId);
			if (roomStaticsByEventId == null) {
				iLiveRoomStaticsMng.addRoomStatic(liveEventId, num);
			} else {
				Long showNum = roomStaticsByEventId.getShowNum();
				showNum = showNum == null ? 0 : showNum;
				if (showNum != num) {
					if (showNum > num) {
						// iLiveRoomStaticsMng.updateRoomStatic(liveEventId, showNum);
						ApplicationCache.LiveEventUserCache.put(liveEventId, showNum);
					} else {
						iLiveRoomStaticsMng.updateRoomStatic(liveEventId, num);
					}
				}
			}
		}
	}

}
