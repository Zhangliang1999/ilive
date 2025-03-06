package com.bvRadio.iLive.iLive.web.pc;

import static com.bvRadio.iLive.iLive.Constants.LIVE_MSG_TYPE_INTERACT;
import static com.bvRadio.iLive.iLive.Constants.SOCKET_SESSION_LIVE_ID_KEY;
import static com.bvRadio.iLive.iLive.Constants.SOCKET_SESSION_USER_KEY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.entity.ILiveEstoppel;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEstoppelMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
import com.bvRadio.iLive.iLive.web.vo.NotifyStaticsLoginVo;

@Component
public class SystemWebSocketHandler implements WebSocketHandler {
	private static final Logger log = LoggerFactory.getLogger(SystemWebSocketHandler.class);
	private static final int LAST_MSG_NUM = 50;
	private static final ArrayList<WebSocketSession> users;

	static {
		users = new ArrayList<WebSocketSession>();
	}

	public SystemWebSocketHandler() {

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Integer liveId = (Integer) session.getAttributes().get(SOCKET_SESSION_LIVE_ID_KEY);
		ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
		ConcurrentHashMap<String, UserBean> userMap = userListMap.get(liveId);
		if (userMap != null) {
			String websocket_user_sessionId = (String) session.getAttributes().get("websocket_user_sessionId");
			// 记录到总人数中
			NotifyStaticsLoginVo loginVo = new NotifyStaticsLoginVo();
			loginVo.setUserId(websocket_user_sessionId);
			loginVo.setLiveId(liveId);
			Object liveEventId = session.getAttributes().get("liveEventId");
			String hostString = (String) session.getAttributes().get("realIpAddr");
			loginVo.setIpAddr(hostString);
			loginVo.setLiveEventId(liveEventId == null ? 0 : (long) liveEventId);
			loginVo.setSessionType(1);
			loginVo.setWebType((Integer) session.getAttributes().get("webType"));
			ILiveUserViewStatics.INSTANCE.logoutEvent(loginVo);
			try {
				System.out.println("清除websocket1"+session.getAttributes().get("uuid"));
				System.out.println("清除websocket2"+userMap.get(websocket_user_sessionId).getUuid());
				if (websocket_user_sessionId != null&&session.getAttributes().get("uuid").equals(userMap.get(websocket_user_sessionId).getUuid())) {
					UserBean remove = userMap.remove(websocket_user_sessionId);
					UserBean userBean = userMap.get(websocket_user_sessionId);
					boolean flag2=true;
					while (flag2) {
						String requestionIdString=UUID.randomUUID().toString();
						if(JedisUtils.tryGetDistributedLock(websocket_user_sessionId+"lock", requestionIdString, 1)) {
							JedisUtils.del(liveId+":"+websocket_user_sessionId);
							flag2=false;
							JedisUtils.releaseDistributedLock(websocket_user_sessionId+"lock", requestionIdString);
						}else {
							try {
								Thread.sleep(100);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}
					JedisUtils.removeSet("userIdList"+liveId,websocket_user_sessionId);
					System.out.println("websocket 清除用户：" + websocket_user_sessionId+"时间:"+new Date());
				}
			} catch (Exception e) {
				e.printStackTrace();
				UserBean remove = userMap.remove(websocket_user_sessionId);
				UserBean userBean = userMap.get(websocket_user_sessionId);
				boolean flag2=true;
				while (flag2) {
					String requestionIdString=UUID.randomUUID().toString();
					if(JedisUtils.tryGetDistributedLock(websocket_user_sessionId+"lock", requestionIdString, 1)) {
						JedisUtils.del(liveId+":"+websocket_user_sessionId);
						flag2=false;
						JedisUtils.releaseDistributedLock(websocket_user_sessionId+"lock", requestionIdString);
					}else {
						try {
							Thread.sleep(100);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
					}
				}
				JedisUtils.removeSet("userIdList"+liveId,websocket_user_sessionId);
				System.out.println("websocket 清除用户22：" + websocket_user_sessionId+"时间:"+new Date());
			}
			

		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String message = (String) session.getAttributes().get("username");
		// 无效的消息
		if (".".equals(message.toString())) {
			return;
		}
		log.info("messageReceived. sessionId = {} ,message = {}", session.getId(), message);
		try {
			ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
			ILiveMessageMng messageMng = (ILiveMessageMng) applicationContext.getBean("iLiveMessageMng");
			String token = message;
			Cache cache = CacheManager.getCacheInfo(CacheManager.mobile_token_ + token);
			if (cache == null || cache.isExpired()) {
				log.info("token错误，关闭session。 sessionId = {} ,message = {}", session.getId(), message);
				session.close();
			}
			JSONObject json = (JSONObject) cache.getValue();
			cache.setExpired(true);
			String userId = json.getString("userId");
			log.info("注册用户缓存标示。 userId_sessionId = {}", userId);
			Integer liveId = json.getInt("liveId");
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new ConcurrentHashMap<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			UserBean iLiveUser = userMap.get(userId);
			String uuid=UUID.randomUUID().toString();
			iLiveUser.setUuid(uuid);
			userMap.put(userId, iLiveUser);
			if (null == iLiveUser) {
				log.info("用户没有注册到缓存，关闭session。 sessionId = {} ,message = {}", session.getId(), message);
				session.close();
			} else {
				// 记录到总人数中
				NotifyStaticsLoginVo loginVo = new NotifyStaticsLoginVo();
				loginVo.setUserId(userId);
				loginVo.setLiveId(liveId);
				String hostString = (String) session.getAttributes().get("realIpAddr");
				loginVo.setIpAddr(hostString);
				int webType = 1;
				if (!json.isNull("webType")) {
					webType = json.getInt("webType");
				}
				long liveEventId = json.getLong("liveEventId");
				loginVo.setLiveEventId(liveEventId);
				session.getAttributes().put("liveEventId", liveEventId);
				loginVo.setWebType(webType);
				session.getAttributes().put("webType", webType);
				loginVo.setSessionType(1);
				ILiveUserViewStatics.INSTANCE.loginEvent(loginVo);
				iLiveUser.setWebSocketSession(session);
				session.getAttributes().put(SOCKET_SESSION_LIVE_ID_KEY, liveId);
				session.getAttributes().put(SOCKET_SESSION_USER_KEY, iLiveUser);
				session.getAttributes().put("websocket_user_sessionId", userId);
				session.getAttributes().put("uuid", uuid);
				List<ILiveMessage> userMsgList = iLiveUser.getMsgList();
				// 禁言
				Hashtable<Integer, Hashtable<Long, ILiveEstoppel>> userEstopMap = ApplicationCache.getUserEstopMap();
				Hashtable<Long, ILiveEstoppel> estopMap = userEstopMap.get(liveId);
				if (estopMap == null) {
					estopMap = new Hashtable<Long, ILiveEstoppel>();
					ILiveEstoppelMng iLiveEstoppelMng = (ILiveEstoppelMng) applicationContext
							.getBean("iLiveEstoppelMng");
					List<ILiveEstoppel> estoppels = iLiveEstoppelMng.selectILiveEstoppels(liveId);
					for (ILiveEstoppel iLiveEstoppel : estoppels) {
						estopMap.put(iLiveEstoppel.getUserId(), iLiveEstoppel);
					}
					userEstopMap.put(liveId, estopMap);
				}
				
				if("open".equals(ConfigUtils.get("redis_service"))) {
					
					if (iLiveUser.getUserType() == 0) {
						List<String> msgIdList =new ArrayList<String>();
						boolean flag=JedisUtils.exists("msgIdListCheck"+liveId);
						if(!flag) {
								
							List<ILiveMessage> tempMsgList = null;
							List<ILiveMessage> interactCheckedMsgList = messageMng.getList(liveId, LIVE_MSG_TYPE_INTERACT, null, null, null,
									null, null, null, false, true, null, false);
						try {
							tempMsgList = interactCheckedMsgList.subList(interactCheckedMsgList.size() - LAST_MSG_NUM / 2,
									interactCheckedMsgList.size());
						} catch (Exception e) {
							tempMsgList = interactCheckedMsgList.subList(0, interactCheckedMsgList.size());
						}
						// 从互动消息缓存 添加最新25条到redis缓存
						for (int i = 0; i < tempMsgList.size(); i++) {
							try {
								ILiveMessage iLiveMessage = tempMsgList.get(i);
								ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
								// 10 禁言 11 解禁
								if (iLiveEstoppel != null) {
									iLiveMessage.setOpType(11);
								} else {
									iLiveMessage.setOpType(10);
								}
								msgIdList.add(iLiveMessage.getMsgId()+"");
								if (!JedisUtils.exists(("msg:"+iLiveMessage.getMsgId()).getBytes())) {
									JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						List<ILiveMessage> quizList = messageMng.getList(liveId, Constants.LIVE_MSG_TYPE_QUIZ, null, null, null, null,
								null, null, false, true, null, false);
					
				        List<ILiveMessage> tempMsgList1 = null;
					try {
						tempMsgList1 = quizList.subList(quizList.size() - LAST_MSG_NUM, quizList.size());
					} catch (Exception e) {
						tempMsgList1 = quizList.subList(0, quizList.size());
					}
					for (int i = 0; i < tempMsgList1.size(); i++) {
						try {
							ILiveMessage iLiveMessage = tempMsgList1.get(i);
							ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
							// 10 禁言 11 解禁
							if (iLiveEstoppel != null) {
								iLiveMessage.setOpType(11);
							} else {
								iLiveMessage.setOpType(10);
							}
							msgIdList.add(iLiveMessage.getMsgId()+"");
							if (!JedisUtils.exists(("msg:"+iLiveMessage.getMsgId()).getBytes())) {
								JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
							
							
							JedisUtils.setList("msgIdListCheck"+liveId, msgIdList, 0);
							
							boolean flag2=true;
							while (flag2) {
								String requestionIdString=UUID.randomUUID().toString();
								if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
									JedisUtils.setList(liveId+":"+userId, msgIdList, 30);
									flag2=false;
									JedisUtils.releaseDistributedLock(userId+"lock", requestionIdString);
								}else {
									try {
										Thread.sleep(100);
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
							}
							
						}else {
							//System.out.println("前端用户接收消息000000000000000000000000000000000000");
							boolean flag2=true;
							while (flag2) {
								String requestionIdString=UUID.randomUUID().toString();
								if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
									List<String> msgIdListCheck1=null;
									List<String> msgIdListCheck=JedisUtils.getList("msgIdListCheck"+liveId);
									//System.out.println("msgIdListCheck000000000000000000000000000000000000"+msgIdListCheck.size());
									try {
										 //取25条
										msgIdListCheck1=msgIdListCheck.subList(msgIdListCheck.size()- LAST_MSG_NUM / 2, msgIdListCheck.size());
									} catch (Exception e) {
										msgIdListCheck1=msgIdListCheck.subList(0, msgIdListCheck.size());
									}
									
									JedisUtils.setList(liveId+":"+userId, msgIdListCheck1, 30);
									flag2=false;
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
						
					}else {
						
						

						List<String> msgIdList =new ArrayList<String>();
						List<String> newMsgIdList = new ArrayList<String>();
						boolean flag=JedisUtils.exists("msgIdListCheckNo"+liveId);
						if(!flag) {
							List<String> msgIdListCheckNo=new ArrayList<String>();
							List<ILiveMessage> chatInteractiveListNO =messageMng.getList(liveId, LIVE_MSG_TYPE_INTERACT, null, null, null,
										null, null, null, false, false, null, false);
								
							List<ILiveMessage> tempMsgList = null;
							try {
								tempMsgList = chatInteractiveListNO.subList(chatInteractiveListNO.size() - LAST_MSG_NUM / 2,
										chatInteractiveListNO.size());
							} catch (Exception e) {
								tempMsgList = chatInteractiveListNO.subList(0, chatInteractiveListNO.size());
							}
							// 从互动消息 未审核缓存 添加最新25条到用户缓存
							for (int i = 0; i < tempMsgList.size(); i++) {
								try {
									ILiveMessage iLiveMessage = tempMsgList.get(i);
									ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
									// 10 禁言 11 解禁
									if (iLiveEstoppel != null) {
										iLiveMessage.setOpType(11);
									} else {
										iLiveMessage.setOpType(10);
									}
									msgIdList.add(iLiveMessage.getMsgId()+"");
									msgIdListCheckNo.add(iLiveMessage.getMsgId()+"");
									if (!JedisUtils.exists(("msg:"+iLiveMessage.getMsgId()).getBytes())) {
										
										JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
									}
									
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							
		//---------------------------------------------------------------------------------------------
							
							
							
							JedisUtils.setList("msgIdListCheckNo"+liveId, msgIdListCheckNo, 0);
							boolean flag2=true;
							while (flag2) {
								String requestionIdString=UUID.randomUUID().toString();
								if(JedisUtils.tryGetDistributedLock(liveId+"unchecklock", requestionIdString, 1)) {
									
									JedisUtils.setList("msgIdListCheckNo"+liveId, msgIdListCheckNo, 0);
									flag2=false;
									JedisUtils.releaseDistributedLock(liveId+"unchecklock", requestionIdString);
								}else {
									try {
										Thread.sleep(100);
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
							}
							
						}else {
							List<String> msgIdListCheckNo=JedisUtils.getList("msgIdListCheckNo"+liveId);
							
							newMsgIdList.addAll(msgIdListCheckNo);
						}
						
						
						boolean flag1=JedisUtils.exists("msgIdListCheck"+liveId);
						if(!flag1) {
							List<String> msgIdListCheck=new ArrayList<String>();
							List<ILiveMessage> interactCheckedMsgList = messageMng.getList(liveId, LIVE_MSG_TYPE_INTERACT, null, null, null,
									null, null, null, false, true, null, false);
						
							List<ILiveMessage> tempMsgList = null;
						try {
							tempMsgList = interactCheckedMsgList.subList(interactCheckedMsgList.size() - LAST_MSG_NUM / 2,
									interactCheckedMsgList.size());
						} catch (Exception e) {
							tempMsgList = interactCheckedMsgList.subList(0, interactCheckedMsgList.size());
						}
						// 从互动消息缓存 添加最新25条到redis缓存
						for (int i = 0; i < tempMsgList.size(); i++) {
							try {
								ILiveMessage iLiveMessage = tempMsgList.get(i);
								ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
								// 10 禁言 11 解禁
								if (iLiveEstoppel != null) {
									iLiveMessage.setOpType(11);
								} else {
									iLiveMessage.setOpType(10);
								}
								msgIdList.add(iLiveMessage.getMsgId()+"");
								msgIdListCheck.add(iLiveMessage.getMsgId()+"");
								if (!JedisUtils.exists(("msg:"+iLiveMessage.getMsgId()).getBytes())) {
									JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						List<ILiveMessage> quizList = messageMng.getList(liveId, Constants.LIVE_MSG_TYPE_QUIZ, null, null, null, null,
								null, null, false, true, null, false);
					
				        List<ILiveMessage> tempMsgList1 = null;
					try {
						tempMsgList1 = quizList.subList(quizList.size() - LAST_MSG_NUM, quizList.size());
					} catch (Exception e) {
						tempMsgList1 = quizList.subList(0, quizList.size());
					}
					for (int i = 0; i < tempMsgList1.size(); i++) {
						try {
							ILiveMessage iLiveMessage = tempMsgList1.get(i);
							ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
							// 10 禁言 11 解禁
							if (iLiveEstoppel != null) {
								iLiveMessage.setOpType(11);
							} else {
								iLiveMessage.setOpType(10);
							}
							msgIdList.add(iLiveMessage.getMsgId()+"");
							msgIdListCheck.add(iLiveMessage.getMsgId()+"");
							if (!JedisUtils.exists(("msg:"+iLiveMessage.getMsgId()).getBytes())) {
								JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					JedisUtils.setList("msgIdListCheck"+liveId, msgIdListCheck, 0);
					
					}else {
							List<String> msgIdListCheck=JedisUtils.getList("msgIdListCheck"+liveId);
							List<String> newMsgIdListCheck = null;
							try {
								newMsgIdListCheck = msgIdListCheck.subList(msgIdListCheck.size() - LAST_MSG_NUM / 2,
										msgIdListCheck.size());
							} catch (Exception e) {
								newMsgIdListCheck = msgIdListCheck.subList(0, msgIdListCheck.size());
							}
							newMsgIdList.addAll(newMsgIdListCheck);
							
						}
						if(newMsgIdList.size()==0||newMsgIdList==null) {
							
							boolean flag2=true;
							while (flag2) {
								String requestionIdString=UUID.randomUUID().toString();
								if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
									System.out.println("msgIdList。size====="+msgIdList.size());
									JedisUtils.setList(liveId+":"+userId, msgIdList, 30);
									System.out.println("放入消息成功====="+JedisUtils.exists(liveId+":"+iLiveUser.getUserId()));
									flag2=false;
									JedisUtils.releaseDistributedLock(userId+"lock", requestionIdString);
								}else {
									try {
										Thread.sleep(100);
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
							}
							
						}else {
							
							boolean flag2=true;
							while (flag2) {
								String requestionIdString=UUID.randomUUID().toString();
								if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
									System.out.println("newMsgIdList。size====="+newMsgIdList.size());
									JedisUtils.setList(liveId+":"+userId, newMsgIdList, 30);
									System.out.println("放入消息成功====="+JedisUtils.exists(liveId+":"+iLiveUser.getUserId()));
									flag2=false;
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
					
					if (iLiveUser.getUserType() == 0) {
						// 个人用户
						// 从互动消息缓存 添加最新50条到用户缓存
						Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache
								.getChatInteractiveMap();
						List<ILiveMessage> interactCheckedMsgList = chatInteractiveMap.get(liveId);
						if (null == interactCheckedMsgList) {
							//
							interactCheckedMsgList = messageMng.getList(liveId, LIVE_MSG_TYPE_INTERACT, null, null, null,
									null, null, null, false, true, null, false);
							chatInteractiveMap.put(liveId, interactCheckedMsgList);
						}
						List<ILiveMessage> tempMsgList = null;
						try {
							tempMsgList = interactCheckedMsgList.subList(interactCheckedMsgList.size() - LAST_MSG_NUM / 2,
									interactCheckedMsgList.size());
						} catch (Exception e) {
							tempMsgList = interactCheckedMsgList.subList(0, interactCheckedMsgList.size());
						}
						/*for (int i = 0; i < tempMsgList.size(); i++) {
							try {
								ILiveMessage iLiveMessage = tempMsgList.get(i);
								userMsgList.add(iLiveMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}*/
						// 从图文直播消息缓存添加最新50条到用户缓存
						// Hashtable<Integer, List<ILiveMessage>> frameLiveMap =
						// ApplicationCache.getFrameLiveMap();
						// List<ILiveMessage> liveMsgList =
						// frameLiveMap.get(liveId);
						// // 如果缓存中没有直播消息，从数据中读取
						// if (null == liveMsgList) {
						// liveMsgList = messageMng.getList(liveId,
						// LIVE_MSG_TYPE_LIVE, null, null, null, null,
						// null, null, false, true, null,false);
						// frameLiveMap.put(liveId, liveMsgList);
						// }
						// List<ILiveMessage> tempMsgList2 = null;
						// try {
						// tempMsgList2 = liveMsgList.subList(liveMsgList.size() -
						// LAST_MSG_NUM,
						// liveMsgList.size());
						// } catch (Exception e) {
						// tempMsgList2 = liveMsgList.subList(0,
						// liveMsgList.size());
						// }

						// 从问答消息缓存添加最新50条到用户缓存
						Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
						List<ILiveMessage> quizList = quizLiveMap.get(liveId);
						// 如果缓存中没有问答，从数据中读取
						if (null == quizList) {
							quizList = messageMng.getList(liveId, Constants.LIVE_MSG_TYPE_QUIZ, null, null, null, null,
									null, null, false, true, null, false);
							quizLiveMap.put(liveId, quizList);
						}
						List<ILiveMessage> tempMsgList3 = null;
						try {
							tempMsgList3 = quizList.subList(quizList.size() - LAST_MSG_NUM, quizList.size());
						} catch (Exception e) {
							tempMsgList3 = quizList.subList(0, quizList.size());
						}
						List<ILiveMessage> tempMsgListAll = new ArrayList<ILiveMessage>();

						// 互动
						for (int i = 0; i < tempMsgList.size(); i++) {
							try {
								ILiveMessage iLiveMessage = tempMsgList.get(i);
								tempMsgListAll.add(iLiveMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						// 图文
						// for (int i = 0; i < tempMsgList2.size(); i++) {
						// try {
						// ILiveMessage iLiveMessage = tempMsgList2.get(i);
						// tempMsgListAll.add(iLiveMessage);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						// }
						// 问答
						for (int i = 0; i < tempMsgList3.size(); i++) {
							try {
								ILiveMessage iLiveMessage = tempMsgList3.get(i);
								tempMsgListAll.add(iLiveMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						// 排序
						for (int i = 0; i < tempMsgListAll.size(); i++) {
							for (int j = 1; j < tempMsgListAll.size() - i; j++) {
								if ((tempMsgListAll.get(j - 1).getMsgId())
										.compareTo(tempMsgListAll.get(j).getMsgId()) > 0) {
									ILiveMessage iLiveMessage = tempMsgListAll.get(j - 1);
									tempMsgListAll.set((j - 1), tempMsgListAll.get(j));
									tempMsgListAll.set(j, iLiveMessage);
								}
							}
						}
						for (ILiveMessage iLiveMessage : tempMsgListAll) {
							ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
							// 10 禁言 11 解禁
							if (iLiveEstoppel != null) {
								iLiveMessage.setOpType(11);
							} else {
								iLiveMessage.setOpType(10);
							}
							userMsgList.add(iLiveMessage);
						}
					} else {
						
						
						
						
						
						// 从互动消息缓存 添加最新25条到用户缓存
						Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache
								.getChatInteractiveMap();
						List<ILiveMessage> interactCheckedMsgList = chatInteractiveMap.get(liveId);
						if (null == interactCheckedMsgList) {
							//
							interactCheckedMsgList = messageMng.getList(liveId, LIVE_MSG_TYPE_INTERACT, null, null, null,
									null, null, null, false, true, null, false);
							chatInteractiveMap.put(liveId, interactCheckedMsgList);
						}
						List<ILiveMessage> tempMsgList = null;
						try {
							tempMsgList = interactCheckedMsgList.subList(interactCheckedMsgList.size() - LAST_MSG_NUM / 2,
									interactCheckedMsgList.size());
						} catch (Exception e) {
							tempMsgList = interactCheckedMsgList.subList(0, interactCheckedMsgList.size());
						}
						// 从互动消息缓存 添加最新25条到用户缓存
						for (int i = 0; i < tempMsgList.size(); i++) {
							try {
								ILiveMessage iLiveMessage = tempMsgList.get(i);
								ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
								// 10 禁言 11 解禁
								if (iLiveEstoppel != null) {
									iLiveMessage.setOpType(11);
								} else {
									iLiveMessage.setOpType(10);
								}
								userMsgList.add(iLiveMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						// 从互动消息 未审核缓存 添加最新25条到用户缓存
						Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache
								.getChatInteractiveMapNO();
						List<ILiveMessage> chatInteractiveListNO = chatInteractiveMapNO.get(liveId);
						if (null == chatInteractiveListNO) {
							//
							chatInteractiveListNO = messageMng.getList(liveId, LIVE_MSG_TYPE_INTERACT, null, null, null,
									null, null, null, false, false, null, false);
							chatInteractiveMapNO.put(liveId, chatInteractiveListNO);
						}
						tempMsgList = null;
						try {
							tempMsgList = chatInteractiveListNO.subList(chatInteractiveListNO.size() - LAST_MSG_NUM / 2,
									chatInteractiveListNO.size());
						} catch (Exception e) {
							tempMsgList = chatInteractiveListNO.subList(0, chatInteractiveListNO.size());
						}
						// 从互动消息 未审核缓存 添加最新25条到用户缓存
						for (int i = 0; i < tempMsgList.size(); i++) {
							try {
								ILiveMessage iLiveMessage = tempMsgList.get(i);
								ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
								// 10 禁言 11 解禁
								if (iLiveEstoppel != null) {
									iLiveMessage.setOpType(11);
								} else {
									iLiveMessage.setOpType(10);
								}
								userMsgList.add(iLiveMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						

						// 从问答消息缓存添加最新50条到用户缓存
						Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
						List<ILiveMessage> quizList = quizLiveMap.get(liveId);
						// 如果缓存中没有问答，从数据中读取
						if (null == quizList) {
							quizList = messageMng.getList(liveId, Constants.LIVE_MSG_TYPE_QUIZ, null, null, null, null,
									null, null, false, true, null, false);
							quizLiveMap.put(liveId, quizList);
						}
						tempMsgList = null;
						try {
							tempMsgList = quizList.subList(quizList.size() - LAST_MSG_NUM, quizList.size());
						} catch (Exception e) {
							tempMsgList = quizList.subList(0, quizList.size());
						}
						for (int i = 0; i < tempMsgList.size(); i++) {
							try {
								ILiveMessage iLiveMessage = tempMsgList.get(i);
								ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
								// 10 禁言 11 解禁
								if (iLiveEstoppel != null) {
									iLiveMessage.setOpType(11);
								} else {
									iLiveMessage.setOpType(10);
								}
								userMsgList.add(iLiveMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
					
					
				}
				
				
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
		}
		// System.out.println("Server:cennected OK!");
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage message) throws Exception {
		TextMessage tm = new TextMessage(message.getPayload() + "");
		session.sendMessage(tm);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable throwable) {
		if (session.isOpen()) {
			try {
				session.close();
			} catch (IOException e) {
				// System.out.println("session close Exception!"+e.toString());
			}
		}
		try {
			String websocket_user_sessionId = (String) session.getAttributes().get("websocket_user_sessionId");
			Integer liveId = (Integer) session.getAttributes().get(SOCKET_SESSION_LIVE_ID_KEY);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(liveId);
			userMap.remove(websocket_user_sessionId);
		} catch (Exception e) {
			// System.out.println("session remove Exception!"+e.toString());
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给所有在线用户发送消息
	 * 
	 * @param message
	 */
	public void sendMessageToUsers(TextMessage message) {
		for (WebSocketSession user : users) {
			if (user.isOpen()) {
				try {
					user.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 给某个用户发送消息
	 *
	 * @param userName
	 * @param message
	 */
	public void sendMessageToUser(String username, TextMessage message) {
		// System.out.println("获得连接账户" + username);
		for (WebSocketSession user : users) {
			if (user.getAttributes().get("username").equals(username)) {
				try {
					if (user.isOpen()) {
						user.sendMessage(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	/**
	 * 给所有非己在线用户发送消息
	 * 
	 * @param message
	 */
	public void sendMessageToUsers(TextMessage message, String username) {
		for (WebSocketSession user : users) {
			if (user.isOpen() && !user.getAttributes().get("username").equals(username)) {
				try {
					user.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
