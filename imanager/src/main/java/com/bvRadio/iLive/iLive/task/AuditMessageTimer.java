package com.bvRadio.iLive.iLive.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
/**
 * 消息定时器
 * @author Administrator
 *
 */
public class AuditMessageTimer extends TimerTask{
	ExecutorService newFixedThreadPool = null;
	
	public AuditMessageTimer() {
		 newFixedThreadPool = Executors.newFixedThreadPool(10);
	}

	@Override
	public void run() {
		Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache.getChatInteractiveMapNO();
		if(!chatInteractiveMapNO.isEmpty()){
			ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
			Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
			if(roomListMap==null||roomListMap.isEmpty()){
				roomListMap = new Hashtable<Integer, Integer>();
				ILiveLiveRoomMng iLiveLiveRoomMng = (ILiveLiveRoomMng) applicationContext.getBean("iLiveLiveRoomMng");
				List<ILiveLiveRoom> list = iLiveLiveRoomMng.findRoomList();
				for (ILiveLiveRoom iLiveLiveRoom : list) {
					Integer roomId = iLiveLiveRoom.getRoomId();
					ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
					ApplicationCache.getRoomListMap().put(roomId, liveEvent.getAutoCheckSecond());
				}
			}
			Iterator<Integer> roomIds = roomListMap.keySet().iterator();
			while (roomIds.hasNext()) {
				Integer roomId = roomIds.next();
				Integer autoCheckSecond = roomListMap.get(roomId);
				if(autoCheckSecond>0){
					List<ILiveMessage> list = chatInteractiveMapNO.get(roomId);
					if(list==null){
						list = new ArrayList<ILiveMessage>();
					}
					if(!list.isEmpty()) {
						this.checkUserMsg(list,roomId);
						chatInteractiveMapNO.remove(roomId);
					}
				}
			}
		}
	}
	
	private static final Logger log = LoggerFactory
			.getLogger(AuditMessageTimer.class);

	private void checkUserMsg(final List<ILiveMessage> list,final Integer roomId) {
		newFixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
				ILiveMessageMng iLiveMessageMng = (ILiveMessageMng) applicationContext.getBean("iLiveMessageMng");
				Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
				Integer autoCheckSecond = roomListMap.get(roomId);
				while (true) {
					if(list.size()>0){
						List<ILiveMessage> messages = new ArrayList<ILiveMessage>();
						for (ILiveMessage iLiveMessage : list) {
							long createLong = iLiveMessage.getCreateTime().getTime();
							long newLong = new Date().getTime();
							if((newLong-createLong)/1000>autoCheckSecond){
								iLiveMessageMng.updateCheckById(iLiveMessage.getMsgId(), true);
								try {
									messages.add(iLiveMessage);
								} catch (Exception e) {
									log.error("后台审核的互动消息从未审核的互动消息缓存移除出错", e);
								}
								Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
								List<ILiveMessage> chatInteractiveList = chatInteractiveMap.get(roomId);
								if(null==chatInteractiveList){
									chatInteractiveList = new ArrayList<ILiveMessage>();
									chatInteractiveMap.put(roomId,chatInteractiveList);
								}else{
									try {
										iLiveMessage.setChecked(true);
										iLiveMessage.setUpdate(true);
										chatInteractiveList.add(iLiveMessage);
										chatInteractiveMap.put(roomId, chatInteractiveList);
									} catch (Exception e) {
										log.error("后台审核的互动消息放到审核的互动消息缓存出错", e);
									}
								}
								// 后台审核消息放到用户缓存
								Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
								Hashtable<String, UserBean> userMap = userListMap.get(roomId);
								if (null == userMap) {
									userMap = new Hashtable<String, UserBean>();
									userListMap.put(roomId, userMap);
								}
								Iterator<String> userIterator = userMap.keySet().iterator();
								while (userIterator.hasNext()) {
									String key = userIterator.next();
									UserBean user = userMap.get(key);
									List<ILiveMessage> userMsgList = user.getMsgList();
									try {
										userMsgList.add(iLiveMessage);
									} catch (Exception e) {
										log.error("后台审核消息放到用户缓存出错", e);
									}
								}
							}
						}
						for (ILiveMessage iLiveMessage : messages) {
							list.remove(iLiveMessage);
						}
					}else{
						break;
					}
				}
			}
		});
	}

}
