package com.bvRadio.iLive.iLive.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

/**
 * 消息定时器
 * 
 * @author Administrator
 *
 */
public class AuditMessageTimer extends TimerTask {
	ExecutorService newFixedThreadPool = null;

	public AuditMessageTimer() {
		newFixedThreadPool = Executors.newFixedThreadPool(10);
	}

	@Override
	public void run() {
		try {
			
			if("open".equals(ConfigUtils.get("redis_service"))){
				ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
				ILiveLiveRoomMng iLiveLiveRoomMng = (ILiveLiveRoomMng) applicationContext.getBean("iLiveLiveRoomMng");
				Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				Iterator<Integer> roomIds = userListMap.keySet().iterator();
				while (roomIds.hasNext()) {
					Integer roomId = roomIds.next();
					Integer autoCheckSecond =null;
					if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
						autoCheckSecond = SerializeUtil.getObjectRoom(roomId).getLiveEvent().getAutoCheckSecond();
					}else {
						ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
						autoCheckSecond = iLiveLiveRoom.getLiveEvent().getAutoCheckSecond();
						roomListMap.remove(roomId);
						roomListMap.put(roomId, autoCheckSecond);
					}
					if (autoCheckSecond == null) {
						ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
						autoCheckSecond = iLiveLiveRoom.getLiveEvent().getAutoCheckSecond();
						roomListMap.remove(roomId);
						roomListMap.put(roomId, autoCheckSecond);
					}
					if (autoCheckSecond > 0) {
						
						
						String requestionIdString=UUID.randomUUID().toString();
						
							
							if(JedisUtils.tryGetDistributedLock(roomId+"messageLock", requestionIdString, 3)) {
								if(JedisUtils.exists(roomId+"AuditMessage")) {
									
								}else {
									JedisUtils.set(roomId+"AuditMessage", "1", 0);
									List<String> msgIdListCheckNo=JedisUtils.getList("msgIdListCheckNo"+roomId);
									if(msgIdListCheckNo!=null&&msgIdListCheckNo.size()!=0) {
										List<ILiveMessage> list = new ArrayList<ILiveMessage>();
										for (String id : msgIdListCheckNo) {
											list.add(SerializeUtil.getObject(id));
										}
										//System.out.println("开始进入审核方法+++++++++++++++++集合大小"+list.size()+"+++++++++++++++");
										this.checkUserMsg(list, roomId,requestionIdString);
									}else {
										JedisUtils.del(roomId+"AuditMessage");
									}
								}
								
								JedisUtils.releaseDistributedLock(roomId+"messageLock", requestionIdString);
							}
						
						
						
					}
				}
				
			}else {
				Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache.getChatInteractiveMapNO();
				if (!chatInteractiveMapNO.isEmpty()) {
					ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
					ILiveLiveRoomMng iLiveLiveRoomMng = (ILiveLiveRoomMng) applicationContext.getBean("iLiveLiveRoomMng");
					Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
					Iterator<Integer> roomIds = chatInteractiveMapNO.keySet().iterator();
					while (roomIds.hasNext()) {
						Integer roomId = roomIds.next();
						Integer autoCheckSecond = roomListMap.get(roomId);
						if (autoCheckSecond == null) {
							ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
							autoCheckSecond = iLiveLiveRoom.getLiveEvent().getAutoCheckSecond();
							roomListMap.put(roomId, autoCheckSecond);
						}
						if (autoCheckSecond > 0) {
							List<ILiveMessage> list = chatInteractiveMapNO.get(roomId);
							if (list == null) {
								list = new ArrayList<ILiveMessage>();
							}
							if (!list.isEmpty()) {
								this.checkUserMsg(list, roomId,null);
								chatInteractiveMapNO.put(roomId, new ArrayList<ILiveMessage>());
							}
						}
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static final Logger log = LoggerFactory.getLogger(AuditMessageTimer.class);

	private void checkUserMsg(final List<ILiveMessage> list, final Integer roomId,final String requestionIdString) {
		newFixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
				ILiveMessageMng iLiveMessageMng = (ILiveMessageMng) applicationContext.getBean("iLiveMessageMng");
				Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
				Integer autoCheckSecond = roomListMap.get(roomId);
					if (list.size() > 0) {
						List<String> messages = new ArrayList<String>();
						for (ILiveMessage iLiveMessage : list) {
							long createLong = iLiveMessage.getCreateTime().getTime();
							long newLong = new Date().getTime();
							try {
								
							if ((newLong - createLong) / 1000 > autoCheckSecond) {
							
								iLiveMessageMng.updateCheckById(iLiveMessage.getMsgId(), true);
								try {
									messages.add(iLiveMessage.getMsgId()+"");
								} catch (Exception e) {
									log.error("后台审核的互动消息从未审核的互动消息缓存移除出错", e);
								}
								if("open".equals(ConfigUtils.get("redis_service"))) {
									iLiveMessage.setRoomType(1);
									iLiveMessage.setUpdate(true);
									iLiveMessage.setChecked(true);
								String setMsg=	JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
									
								Long setMsgLong=	JedisUtils.listAdd("msgIdListCheck"+roomId, iLiveMessage.getMsgId()+"");
								System.out.println("进入审核方法+++++++++++++++++房间id++++++++++++"+roomId);
								Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
								if(userIdList!=null&&userIdList.size()!=0) {
									for(String userId:userIdList) {
										boolean flag=true;
										while (flag) {
											String requestionIdString=UUID.randomUUID().toString();
											if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
												JedisUtils.listAdd(roomId+":"+userId, iLiveMessage.getMsgId()+"");
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
									Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache
											.getChatInteractiveMap();
									List<ILiveMessage> chatInteractiveList = chatInteractiveMap.get(roomId);
									if (null == chatInteractiveList) {
										chatInteractiveList = new ArrayList<ILiveMessage>();
										chatInteractiveMap.put(roomId, chatInteractiveList);
									} else {
										try {
											iLiveMessage.setChecked(true);
											chatInteractiveList.add(iLiveMessage);
											chatInteractiveMap.put(roomId, chatInteractiveList);
										} catch (Exception e) {
											log.error("后台审核的互动消息放到审核的互动消息缓存出错", e);
										}
									}
									// 后台审核消息放到用户缓存
									ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
											.getUserListMap();
									ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
									if (null == userMap) {
										userMap = new ConcurrentHashMap<String, UserBean>();
										userListMap.put(roomId, userMap);
									}
									ILiveMessage message = new ILiveMessage();
									try {
										BeanUtilsExt.copyProperties(message, iLiveMessage);
										message.setUpdate(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
									Iterator<String> userIterator = userMap.keySet().iterator();
									while (userIterator.hasNext()) {
										String key = userIterator.next();
										UserBean user = userMap.get(key);
										List<ILiveMessage> userMsgList = user.getMsgList();
										if (userMsgList == null) {
											userMsgList = new ArrayList<ILiveMessage>();
										}
										try {
											userMsgList.add(message);
										} catch (Exception e) {
											log.error("后台审核消息放到用户缓存出错", e);
										}
										user.setMsgList(userMsgList);
										userMap.put(key, user);
									}
								}
							}
							
							} catch (Exception e) {
								e.printStackTrace();
								JedisUtils.del(roomId+"AuditMessage");
								//System.out.println("审核报错删除标记--------------------------------------------------------------------");
							}
						}
						JedisUtils.del(roomId+"AuditMessage");
						//System.out.println("审核完成删除标记--------------------------------------------------------------------");
						boolean flag=true;
						while (flag) {
							String requestionIdString=UUID.randomUUID().toString();
							if(JedisUtils.tryGetDistributedLock(roomId+"unchecklock", requestionIdString, 1)) {
								List<String> msgIdListCheckNo=JedisUtils.getList("msgIdListCheckNo"+roomId);
								for (String id : messages) {
									msgIdListCheckNo.remove(id);
								}
								JedisUtils.setList("msgIdListCheckNo"+roomId, msgIdListCheckNo, 0);
								flag=false;
								JedisUtils.releaseDistributedLock(roomId+"unchecklock", requestionIdString);
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
		});
	}

}
