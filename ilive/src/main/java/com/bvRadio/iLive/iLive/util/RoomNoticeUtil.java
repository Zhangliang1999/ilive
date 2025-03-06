package com.bvRadio.iLive.iLive.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.ILiveEventVo;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

public class RoomNoticeUtil {
	/**
	 * 直播间操作通知
	 * @param iLiveLiveRoom 进行修改之后对象
	 */
	public static void nptice(ILiveLiveRoom iLiveLiveRoom){
		ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
		ConcurrentHashMap<String, UserBean> userMap = userListMap.get(iLiveLiveRoom.getRoomId());
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		if("open".equals(ConfigUtils.get("redis_service"))) {
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
			message.setMsgId(Long.parseLong((-iLiveLiveRoom.getRoomId())+""));
			
			JedisUtils.delObject("msg:"+message.getMsgId());
			JedisUtils.setByte(("msg:"+message.getMsgId()).getBytes(), SerializeUtil.serialize(message), 0);
			Set<String> userIdList =JedisUtils.getSet("userIdList"+iLiveLiveRoom.getRoomId());
			if(userIdList!=null&&userIdList.size()!=0) {
				for(String userId:userIdList) {
					boolean flag=true;
					while (flag) {
						String requestionIdString=UUID.randomUUID().toString();
						if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
							JedisUtils.del(iLiveLiveRoom.getRoomId()+":"+userId);
							List<String> msgIdList =new ArrayList<String>();
							msgIdList.add(message.getMsgId()+"");
							JedisUtils.setList(iLiveLiveRoom.getRoomId()+":"+userId, msgIdList, 0);
							flag=false;
							JedisUtils.releaseDistributedLock(userId+"lock", requestionIdString);
						}else {
							try {
								Thread.sleep(100);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}
				}
			}
			
		}else {
			if(userMap!=null){
				Iterator<String> userIterator = userMap.keySet().iterator();
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					UserBean user = userMap.get(key);
					
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
}
