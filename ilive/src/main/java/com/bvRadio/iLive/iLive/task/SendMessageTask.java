package com.bvRadio.iLive.iLive.task;

//import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.mina.MinaServerHandler;
/**
 * 消息发送定时器
 * @author YanXL
 *
 */
public class SendMessageTask extends TimerTask {

	private static final Logger log = LoggerFactory
			.getLogger(SendMessageTask.class);

	public SendMessageTask() {
		super();
		log.info("SendMessageTask constructor info");
		log.debug("SendMessageTask constructor debug");
	}

	public void run() {
		//System.out.println("发消息线程开始时间========================================="+new Date());
		// 获取所有的连接用户
		ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
				.getUserListMap();
		Iterator<Integer> listIterator = userListMap.keySet().iterator();
		while (listIterator.hasNext()) {
			try {
				// 直播间ID
				Integer liveId = listIterator.next();
				// // System.out.println("消息线程中的liveID=="+liveId);
				// 拿到一场直播的在线用户
				ConcurrentHashMap<String, UserBean> userMap = userListMap.get(liveId);
				// // System.out.println("消息线程中的userMap.size()=="+userMap.size());
				// 初始化一个容器
				if (null == userMap) {
					userMap = new ConcurrentHashMap<String, UserBean>();
					userListMap.put(liveId, userMap);
				}
				Iterator<String> mapIterator = userMap.keySet().iterator();
				while (mapIterator.hasNext()) {
					String key = mapIterator.next();
					UserBean user = userMap.get(key);
					//System.out.println("userId========================================="+user.getUserId());
					if("open".equals(ConfigUtils.get("redis_service"))) {
						try {
							List<String> msgIdList=null;
							String requestionIdString=UUID.randomUUID().toString();
							if("0".equals(user.getUserId().split("_")[0])) {
								if(JedisUtils.tryGetDistributedLock(user.getUserId()+"lock",
										  requestionIdString, 1)) {
											  
												//System.out.println("user开始获取消息集合========================================="+user.getUserId());
											    //System.out.println("user是否获取到消息集合========================================="+JedisUtils.exists(liveId+":"+user.getUserId()));
												msgIdList = JedisUtils.getList(liveId+":"+user.getUserId());
												
												//System.out.println("user删除消息集合========================================="+user.getUserId());
												JedisUtils.del(liveId+":"+user.getUserId());
												//System.out.println("userredis锁执行完毕========================================="+user.getUserId());
												JedisUtils.releaseDistributedLock(user.getUserId()+"lock",
										  requestionIdString);
										  
										  }
							}else {
								if(JedisUtils.tryGetDistributedLock(user.getUserId()+"_"+user.getFunctionCode()+"lock",
										  requestionIdString, 1)) {
											  if(user.getSessionType()==1) {
												  
														msgIdList = JedisUtils.getList(liveId+":"+user.getUserId()+"_"+user.getFunctionCode());
														
														JedisUtils.del(liveId+":"+user.getUserId()+"_"+user.getFunctionCode());
													
											  }else {
												  Hashtable<String, Integer> handler=ApplicationCache.getHandler();
												  if(handler!=null&&handler.get("APP")!=null&&handler.get("APP")==1) {
													  if(user.getFunctionCode().equals("APP")) {
															msgIdList = JedisUtils.getList(liveId+":"+user.getUserId());
													//System.out.println("用户取出缓存消息集合大小++++++++++++++++++++++++++++++"+msgIdList.size());		
															JedisUtils.del(liveId+":"+user.getUserId());
														}else {
															msgIdList = JedisUtils.getList(liveId+":"+user.getUserId()+"_"+user.getFunctionCode());
															
															JedisUtils.del(liveId+":"+user.getUserId()+"_"+user.getFunctionCode());
														}
												  }
											  }
												
												JedisUtils.releaseDistributedLock(user.getUserId()+"_"+user.getFunctionCode()+"lock",
										  requestionIdString);
										  
										  }
							}
							
							Integer sessionType = user.getSessionType();
							if(sessionType!=null){
								if (sessionType == 1) {
									//System.out.println("user开始发送消息========================================="+user.getUserId());
									//WEB用户
									WebSocketSession webSocketSession = user.getWebSocketSession();
									if(webSocketSession!=null){
										if(msgIdList!=null) {
											//System.out.println("user存在消息集合========================================="+user.getUserId());
											for (String id : msgIdList) {
												ILiveMessage message=SerializeUtil.getObject(id);
												if(message!=null) {
													JSONObject msgJson = message.putMessageInJson(null);
													TextMessage textMessage = new TextMessage(msgJson.toString());
													try {
														webSocketSession.sendMessage(textMessage);
													} catch (Exception e) {
														if("0".equals(user.getUserId().split("_")[0])) {
															JedisUtils.del(liveId+":"+user.getUserId());
														}else {
															JedisUtils.del(liveId+":"+user.getUserId()+"_"+user.getFunctionCode());
														}
														user.setWebSocketSession(null);
														System.out.println("WebSocket 连接异常，清除连接数据");
														
													}
												}
												
											}
											
										}else {
											//System.out.println("usermsgIdList为null========================================="+user.getUserId());
										}
									}else {
										//System.out.println("userwebsocktsession为null========================================="+user.getUserId());
									}
									
								} else{
									//APP用户
									IoSession ioSession = user.getSession();
									//System.out.println("ioSession是否存在=================id"+ioSession.getId());
									if(ioSession!=null){
										if(msgIdList!=null) {
										for (String id : msgIdList) {
											ILiveMessage message=SerializeUtil.getObject(id);
											JSONObject msgJson = message.putMessageInJson(null);
											try {
												ioSession.write(msgJson.toString());
											} catch (Exception e) {
												if("0".equals(user.getUserId().split("_")[0])) {
													JedisUtils.del(liveId+":"+user.getUserId());
													}else {
														JedisUtils.del(liveId+":"+user.getUserId()+"_"+user.getFunctionCode());
													}
												user.setSession(null);
												System.out.println("Socket 连接异常，清除连接数据");
											}
										}
										//JedisUtils.del(liveId+":"+user.getUserId());
									}
								}
							}
							}else{
								// System.out.println("链接用户没有链接类型");
							}
						} catch (Exception e) {
							log.error("用户缓存的消息发送到客户端出错", e);
						}
					}else {
						List<ILiveMessage> userMsgList = user.getMsgList();
						if (null == userMsgList) {
							userMsgList = new ArrayList<ILiveMessage>();
							user.setMsgList(userMsgList);
						}
						try {
							Integer sessionType = user.getSessionType();
							if(sessionType!=null){
								if (sessionType == 1) {
									//WEB用户
									WebSocketSession webSocketSession = user.getWebSocketSession();
									if(webSocketSession!=null){
										for (ILiveMessage message : userMsgList) {
											JSONObject msgJson = message.putMessageInJson(null);
											TextMessage textMessage = new TextMessage(msgJson.toString());
											try {
												webSocketSession.sendMessage(textMessage);
											} catch (Exception e) {
												user.setWebSocketSession(null);
												System.out.println("WebSocket 连接异常，清除连接数据");
												userMsgList.clear();
											}
										}
										userMsgList.clear();
									}
								} else{
									//APP用户
									IoSession ioSession = user.getSession();
									if(ioSession!=null){
										for (ILiveMessage message : userMsgList) {
											JSONObject msgJson = message.putMessageInJson(null);
											try {
												ioSession.write(msgJson.toString());
											} catch (Exception e) {
												user.setSession(null);
												System.out.println("Socket 连接异常，清除连接数据");
												userMsgList.clear();
											}
										}
										userMsgList.clear();
									}
								}
							}else{
								// System.out.println("链接用户没有链接类型");
							}
						} catch (Exception e) {
							log.error("用户缓存的消息发送到客户端出错", e);
						}
						
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("用户缓存的消息发送到客户端出错", e);
			}
		}
		//System.out.println("发消息线程结束时间========================================="+new Date());
	}
}