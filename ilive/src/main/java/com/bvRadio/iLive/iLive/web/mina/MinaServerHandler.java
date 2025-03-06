package com.bvRadio.iLive.iLive.web.mina;

import static com.bvRadio.iLive.iLive.Constants.LIVE_MSG_TYPE_INTERACT;
import static com.bvRadio.iLive.iLive.Constants.SOCKET_SESSION_LIVE_ID_KEY;
import static com.bvRadio.iLive.iLive.Constants.SOCKET_SESSION_USER_KEY;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

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

public class MinaServerHandler extends IoHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(MinaServerHandler.class);
	private static final int LAST_MSG_NUM = 25;

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
		// 无效的消息
		if (".".equals(message.toString())) {
			return;
		}
		log.info("messageReceived. sessionId = {} ,message = {}", session.getId(), message);
		try {
			System.out.println("开始进入header+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			Hashtable<String, Integer> handler=ApplicationCache.getHandler();
			handler.put("APP", 1);
			ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
			ILiveMessageMng messageMng = (ILiveMessageMng) applicationContext.getBean("iLiveMessageMng");
			JSONObject msgJson = new JSONObject((String) message);
			try {
				Object msgId = msgJson.get("msgId");
				if (null != msgId) {
					// 暂不处理
					return;
				}
			} catch (Exception e) {
			}
			String token = msgJson.getString("token");
			JSONObject json =null;
			if(JedisUtils.exists(CacheManager.mobile_token_ + token)) {
				String tokenInfo=JedisUtils.get(CacheManager.mobile_token_ + token);
				json=new JSONObject(tokenInfo);
			}else {
				Cache cache = CacheManager.getCacheInfo(CacheManager.mobile_token_ + token);
				if (cache == null || cache.isExpired()) {
					log.info("token错误，关闭session。 sessionId = {} ,message = {}", session.getId(), message);
					session.close(false);
				}
				json = (JSONObject) cache.getValue();
				cache.setExpired(true);
			}
			
			
			
			String key = json.getString("userId");
			Integer liveId = json.getInt("liveId");
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new ConcurrentHashMap<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			UserBean iLiveUser = userMap.get(key);
			if(null == iLiveUser&&JedisUtils.exists(CacheManager.mobile_token_ + token)) {
				iLiveUser=new UserBean();
				iLiveUser.setUserId(key);
				iLiveUser.setSessionType(0);
				iLiveUser.setFunctionCode("APP");
				userMap.put(key, iLiveUser);
			}
			if (null == iLiveUser) {
				log.info("用户没有注册到缓存，关闭session。 sessionId = {} ,message = {}", session.getId(), message);
				session.close(false);
			} else {
				// 记录到总人数中
				NotifyStaticsLoginVo loginVo = new NotifyStaticsLoginVo();
				loginVo.setUserId(key);
				loginVo.setLiveId(liveId);
				String clientIP = (String) session.getAttribute("KEY_SESSION_CLIENT_IP");
				loginVo.setIpAddr(clientIP);
				loginVo.setSessionType(0);
				loginVo.setWebType(json.getInt("webType"));
				loginVo.setLiveEventId(json.getLong("liveEventId"));
				ILiveUserViewStatics.INSTANCE.loginEvent(loginVo);
				iLiveUser.setSession(session);
				session.setAttribute(SOCKET_SESSION_LIVE_ID_KEY, liveId);
				session.setAttribute(SOCKET_SESSION_USER_KEY, iLiveUser);
				session.setAttribute("userKey", key);
				session.setAttribute("liveEventId", json.getLong("liveEventId"));
				session.setAttribute("webType", json.getInt("webType"));
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

				if ("open".equals(ConfigUtils.get("redis_service"))) {
					System.out.println("APP用户开始存入消息+++++++++++++++++++++++++++++++++++");
					List<String> msgIdList = new ArrayList<String>();
					boolean flag = JedisUtils.exists("msgIdListCheck" + liveId);
					if (!flag) {

						List<ILiveMessage> tempMsgList = null;
						List<ILiveMessage> interactCheckedMsgList = messageMng.getList(liveId, LIVE_MSG_TYPE_INTERACT,
								null, null, null, null, null, null, false, true, null, false);
						try {
							tempMsgList = interactCheckedMsgList.subList(
									interactCheckedMsgList.size() - LAST_MSG_NUM / 2, interactCheckedMsgList.size());
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
								msgIdList.add(iLiveMessage.getMsgId() + "");
								if (!JedisUtils.exists(("msg:" + iLiveMessage.getMsgId()).getBytes())) {
									JedisUtils.setByte(("msg:" + iLiveMessage.getMsgId()).getBytes(),
											SerializeUtil.serialize(iLiveMessage), 0);
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						List<ILiveMessage> quizList = messageMng.getList(liveId, Constants.LIVE_MSG_TYPE_QUIZ, null,
								null, null, null, null, null, false, true, null, false);

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
								msgIdList.add(iLiveMessage.getMsgId() + "");
								if (!JedisUtils.exists(("msg:" + iLiveMessage.getMsgId()).getBytes())) {
									JedisUtils.setByte(("msg:" + iLiveMessage.getMsgId()).getBytes(),
											SerializeUtil.serialize(iLiveMessage), 0);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						Integer appType = 0;
						try {
							appType = json.getInt("appType");
						} catch (Exception e) {
							log.info("Socket appType  类型 ！", e);
							appType = 0;
						}
						System.out.println("appType  数据值  " + appType);
						if (appType == 1) {
							log.info("Socket 推流 连接  方式不发送消息！");
							System.out.println("Socket 推流 连接  方式不发送消息+++++++++++++++++++++++++++++++++++");
						} else {
							JedisUtils.setList("msgIdListCheck" + liveId, msgIdList, 0);
							System.out.println("APP用户开始存入消息22222222222222222222222+++++++++++++++++++++++++++++++++++");
							boolean flag2=true;
							while (flag2) {
								String requestionIdString=UUID.randomUUID().toString();
								if(JedisUtils.tryGetDistributedLock(key+"lock", requestionIdString, 1)) {
									JedisUtils.setList(liveId + ":" +key , msgIdList, 30);
									flag2=false;
									JedisUtils.releaseDistributedLock(key+"lock", requestionIdString);
								}else {
									try {
										Thread.sleep(100);
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
							}
						}

					} else {
						Integer appType = 0;
						try {
							appType = json.getInt("appType");
						} catch (Exception e) {
							log.info("Socket appType  类型 ！", e);
							appType = 0;
						}
						System.out.println("appType  数据值  " + appType);
						if (appType == 1) {
							log.info("Socket 推流 连接  方式不发送消息！");
						} else {
							
							boolean flag2=true;
							while (flag2) {
								String requestionIdString=UUID.randomUUID().toString();
								if(JedisUtils.tryGetDistributedLock(key+"lock", requestionIdString, 1)) {
									List<String> msgIdListCheck = JedisUtils.getList("msgIdListCheck" + liveId);
									JedisUtils.setList(liveId + ":" + key, msgIdListCheck, 30);
									flag2=false;
									JedisUtils.releaseDistributedLock(key+"lock", requestionIdString);
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
				} else {
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
					Integer appType = 0;
					try {
						appType = json.getInt("appType");
					} catch (Exception e) {
						log.info("Socket appType  类型 ！", e);
						appType = 0;
					}
					System.out.println("appType  数据值  " + appType);
					if (appType == 1) {
						log.info("Socket 推流 连接  方式不发送消息！");
					} else {
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
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			session.close(false);
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
		log.info("messageSent. sessionId = {} ,message = {}", session.getId(), message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		Object liveId = session.getAttribute(SOCKET_SESSION_LIVE_ID_KEY);
		try {
			String key = (String) session.getAttribute("userKey");
			if (null != liveId && null != key) {
				NotifyStaticsLoginVo loginVo = new NotifyStaticsLoginVo();
				loginVo.setUserId(key);
				loginVo.setLiveId((Integer) liveId);
				String clientIP = (String) session.getAttribute("KEY_SESSION_CLIENT_IP");
				loginVo.setIpAddr(clientIP);
				loginVo.setSessionType(0);
				Object liveEventId = session.getAttribute("liveEventId");
				Object webTypeAttr = session.getAttribute("webType");
				if (webTypeAttr == null) {
					webTypeAttr = 0;
				}
				loginVo.setWebType((int) webTypeAttr);
				loginVo.setLiveEventId(liveEventId == null ? 0 : (Long) liveEventId);
				ILiveUserViewStatics.INSTANCE.logoutEvent(loginVo);
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				ConcurrentHashMap<String, UserBean> userList = userListMap.get(liveId);
				userList.remove(key);
				System.out.println("socket 清除用户：" + key);
			} else {
				log.info("sessionClosed. sessionId = {}, liveId = {}", session.getId(), liveId);
			}
		} catch (Exception e) {
			log.info("sessionClosed. sessionId = {}, liveId = {}", session.getId(), liveId);
		}
		Hashtable<Long, IoSession> sessionMap = ApplicationCache.getSessionMap();
		sessionMap.remove(session.getId());
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		log.info("sessionCreated. sessionId = {}", session.getId());
		String clientIP = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
		session.setAttribute("KEY_SESSION_CLIENT_IP", clientIP);
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 3 * 1000);
		Hashtable<Long, IoSession> sessionMap = ApplicationCache.getSessionMap();
		sessionMap.put(session.getId(), session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);
		log.info("sessionIdle. sessionId = {} ,status= {}", session.getId(), status);
		if (status == IdleStatus.BOTH_IDLE) {
			// session.write("heartBeat");
		}
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		log.info("sessionOpened. sessionId = {}", session.getId());
		// session.write("Hello");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		log.info("MinaServer 遇到异常,异常cause为:" + cause.getMessage());
		if (cause instanceof IOException) {
			log.info("处理请求出现IO异常 " + session, cause);
			// session.close();
		} else if (cause instanceof ProtocolDecoderException) {
			log.warn("出现编码与解码异常:" + session, cause);
			// session.close();
		} else {
			log.error("出现其它异常：" + session, cause);
			// session.close();
		}
		super.exceptionCaught(session, cause);
	}

}
