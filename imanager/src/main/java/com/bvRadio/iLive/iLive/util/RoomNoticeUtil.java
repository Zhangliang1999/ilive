package com.bvRadio.iLive.iLive.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.ILiveEventVo;
import com.bvRadio.iLive.iLive.web.ApplicationCache;

public class RoomNoticeUtil {
	/**
	 * 直播间操作通知
	 * @param iLiveLiveRoom 进行修改之后对象
	 */
	public static void nptice(ILiveLiveRoom iLiveLiveRoom){
		Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
		Hashtable<String, UserBean> userMap = userListMap.get(iLiveLiveRoom.getRoomId());
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		if(userMap!=null){
			Iterator<String> userIterator = userMap.keySet().iterator();
			while (userIterator.hasNext()) {
				String userId = userIterator.next();
				UserBean user = userMap.get(userId);
				List<ILiveMessage> userMsgList = user.getMsgList();
				ILiveMessage message = new ILiveMessage();
				message.setRoomType(0);
				ILiveEventVo iLiveEventVo = new ILiveEventVo();
				iLiveEventVo.setCheckedTime(liveEvent.getAutoCheckSecond());
				iLiveEventVo.setEstoppleType(liveEvent.getEstoppelType());
				iLiveEventVo.setLiveStatus(liveEvent.getLiveStatus());
				iLiveEventVo.setPlayType(liveEvent.getPlayType());
				iLiveEventVo.setHlsUrl(iLiveLiveRoom.getHlsAddr());
				iLiveEventVo.setPlayBgAddr(liveEvent.getPlayBgAddr());
				message.setiLiveEventVo(iLiveEventVo);
				message.setLiveRoomId(iLiveLiveRoom.getRoomId());
				userMsgList.add(message);
			}
		}
	}
}
