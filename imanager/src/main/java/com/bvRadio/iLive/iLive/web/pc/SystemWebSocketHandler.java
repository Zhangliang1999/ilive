package com.bvRadio.iLive.iLive.web.pc;

import static com.bvRadio.iLive.iLive.Constants.LIVE_MSG_TYPE_INTERACT;
import static com.bvRadio.iLive.iLive.Constants.LIVE_MSG_TYPE_LIVE;
import static com.bvRadio.iLive.iLive.Constants.SOCKET_SESSION_LIVE_ID_KEY;
import static com.bvRadio.iLive.iLive.Constants.SOCKET_SESSION_USER_KEY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
import com.bvRadio.iLive.iLive.web.ApplicationCache;

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
		Integer liveId =  (Integer) session.getAttributes().get(SOCKET_SESSION_LIVE_ID_KEY);
		UserBean userBean = (UserBean) session.getAttributes().get(SOCKET_SESSION_USER_KEY);
		System.out.println("liveId:"+liveId);
		String userId = userBean.getUserId();
		System.out.println("userID:"+userId);
		Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
		Hashtable<String, UserBean> userMap = userListMap.get(liveId);
		if(userMap!=null){
			userMap.remove(userId);
			System.out.println("清除用户："+userId);
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
			Integer liveId = json.getInt("liveId");
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new Hashtable<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			UserBean iLiveUser = userMap.get(userId);
			if (null == iLiveUser) {
				log.info("用户没有注册到缓存，关闭session。 sessionId = {} ,message = {}", session.getId(), message);
				session.close();
			} else {
				iLiveUser.setWebSocketSession(session);
				session.getAttributes().put(SOCKET_SESSION_LIVE_ID_KEY, liveId);
				session.getAttributes().put(SOCKET_SESSION_USER_KEY, iLiveUser);
				List<ILiveMessage> userMsgList = iLiveUser.getMsgList();
				//禁言
				Hashtable<Integer, Hashtable<Long, ILiveEstoppel>> userEstopMap = ApplicationCache.getUserEstopMap();
				Hashtable<Long, ILiveEstoppel> estopMap = userEstopMap.get(liveId);
				if(estopMap==null){
					estopMap  = new Hashtable<Long, ILiveEstoppel>();
					ILiveEstoppelMng iLiveEstoppelMng = (ILiveEstoppelMng) applicationContext.getBean("iLiveEstoppelMng");
					List<ILiveEstoppel> estoppels = iLiveEstoppelMng.selectILiveEstoppels();
					for (ILiveEstoppel iLiveEstoppel : estoppels) {
						estopMap.put(iLiveEstoppel.getUserId(), iLiveEstoppel);
					}
					userEstopMap.put(liveId, estopMap);
				}
				if(iLiveUser.getUserType()==0){
					//个人用户
					// 从互动消息缓存        添加最新50条到用户缓存
					Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
					List<ILiveMessage> interactCheckedMsgList = chatInteractiveMap.get(liveId);
					if (null == interactCheckedMsgList) {
						//
						interactCheckedMsgList = messageMng.getList(liveId, LIVE_MSG_TYPE_INTERACT, null,
									null, null, null, null, null, false, true, null);
						chatInteractiveMap.put(liveId, interactCheckedMsgList);
					}
					List<ILiveMessage> tempMsgList = null;
					try {
						tempMsgList = interactCheckedMsgList.subList(
								interactCheckedMsgList.size() - LAST_MSG_NUM/2, interactCheckedMsgList.size());
					} catch (Exception e) {
						tempMsgList = interactCheckedMsgList.subList(0, interactCheckedMsgList.size());
					}
					for (int i = 0; i < tempMsgList.size(); i++) {
						try {
							ILiveMessage iLiveMessage = tempMsgList.get(i);
							userMsgList.add(iLiveMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// 从图文直播消息缓存添加最新50条到用户缓存
					Hashtable<Integer, List<ILiveMessage>> frameLiveMap = ApplicationCache.getFrameLiveMap();
					List<ILiveMessage> liveMsgList = frameLiveMap.get(liveId);
					// 如果缓存中没有直播消息，从数据中读取
					if (null == liveMsgList) {
						liveMsgList = messageMng.getList(liveId, LIVE_MSG_TYPE_LIVE, null, null, null, null,
								null, null, false, true, null);
						frameLiveMap.put(liveId, liveMsgList);
					}
					List<ILiveMessage> tempMsgList2 = null;
					try {
						tempMsgList2 = liveMsgList.subList(liveMsgList.size() - LAST_MSG_NUM,
								liveMsgList.size());
					} catch (Exception e) {
						tempMsgList2 = liveMsgList.subList(0, liveMsgList.size());
					}
					
					// 从问答消息缓存添加最新50条到用户缓存
					Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
					List<ILiveMessage> quizList = quizLiveMap.get(liveId);
					// 如果缓存中没有问答，从数据中读取
					if (null == quizList) {
						quizList = messageMng.getList(liveId,Constants.LIVE_MSG_TYPE_QUIZ, null, null, null, null,
								null, null, false, true, null);
						quizLiveMap.put(liveId, quizList);
					}
					List<ILiveMessage> tempMsgList3 = null;
					try {
						tempMsgList3 = quizList.subList(quizList.size() - LAST_MSG_NUM,
								quizList.size());
					} catch (Exception e) {
						tempMsgList3 = quizList.subList(0, quizList.size());
					}
					List<ILiveMessage> tempMsgListAll = new ArrayList<ILiveMessage>();
					
					//互动
					for (int i = 0; i < tempMsgList.size(); i++) {
						try {
							ILiveMessage iLiveMessage = tempMsgList.get(i);
							tempMsgListAll.add(iLiveMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					//图文
					for (int i = 0; i < tempMsgList2.size(); i++) {
						try {
							ILiveMessage iLiveMessage = tempMsgList2.get(i);
							tempMsgListAll.add(iLiveMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					//问答
					for (int i = 0; i < tempMsgList3.size(); i++) {
						try {
							ILiveMessage iLiveMessage = tempMsgList3.get(i);
							tempMsgListAll.add(iLiveMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					//排序
					for (int i = 0; i < tempMsgListAll.size(); i++) {
						for (int j = 1; j < tempMsgListAll.size()-i; j++) {
							if ((tempMsgListAll.get(j - 1).getMsgId()).compareTo(tempMsgListAll.get(j).getMsgId()) > 0) {
								ILiveMessage iLiveMessage = tempMsgListAll.get(j - 1);  
			                    tempMsgListAll.set((j - 1), tempMsgListAll.get(j));  
			                    tempMsgListAll.set(j, iLiveMessage);  
			                }  
						}
					}
					for (ILiveMessage iLiveMessage : tempMsgListAll) {
						ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
						//10 禁言   11 解禁 
						if(iLiveEstoppel!=null){
							iLiveMessage.setOpType(11);
						}else{
							iLiveMessage.setOpType(10);
						}
						userMsgList.add(iLiveMessage);
					}
				}else{
					// 从互动消息缓存        添加最新25条到用户缓存
					Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
					List<ILiveMessage> interactCheckedMsgList = chatInteractiveMap.get(liveId);
					if (null == interactCheckedMsgList) {
						//
						interactCheckedMsgList = messageMng.getList(liveId, LIVE_MSG_TYPE_INTERACT, null,
									null, null, null, null, null, false, true, null);
						chatInteractiveMap.put(liveId, interactCheckedMsgList);
					}
					List<ILiveMessage> tempMsgList = null;
					try {
						tempMsgList = interactCheckedMsgList.subList(
								interactCheckedMsgList.size() - LAST_MSG_NUM/2, interactCheckedMsgList.size());
					} catch (Exception e) {
						tempMsgList = interactCheckedMsgList.subList(0, interactCheckedMsgList.size());
					}
					// 从互动消息缓存        添加最新25条到用户缓存
					for (int i = 0; i < tempMsgList.size(); i++) {
						try {
							ILiveMessage iLiveMessage = tempMsgList.get(i);
							ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
							//10 禁言   11 解禁 
							if(iLiveEstoppel!=null){
								iLiveMessage.setOpType(11);
							}else{
								iLiveMessage.setOpType(10);
							}
							userMsgList.add(iLiveMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// 从互动消息     未审核缓存        添加最新25条到用户缓存
					Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache.getChatInteractiveMapNO();
					List<ILiveMessage> chatInteractiveListNO = chatInteractiveMapNO.get(liveId);
					if (null == chatInteractiveListNO) {
						//
						chatInteractiveListNO = messageMng.getList(liveId, LIVE_MSG_TYPE_INTERACT, null,
									null, null, null, null, null, false, false, null);
						chatInteractiveMapNO.put(liveId, chatInteractiveListNO);
					}
					tempMsgList = null;
					try {
						tempMsgList = chatInteractiveListNO.subList(
								chatInteractiveListNO.size() - LAST_MSG_NUM/2, chatInteractiveListNO.size());
					} catch (Exception e) {
						tempMsgList = chatInteractiveListNO.subList(0, chatInteractiveListNO.size());
					}
					// 从互动消息     未审核缓存        添加最新25条到用户缓存
					for (int i = 0; i < tempMsgList.size(); i++) {
						try {
							ILiveMessage iLiveMessage = tempMsgList.get(i);
							ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
							//10 禁言   11 解禁 
							if(iLiveEstoppel!=null){
								iLiveMessage.setOpType(11);
							}else{
								iLiveMessage.setOpType(10);
							}
							userMsgList.add(iLiveMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					// 从图文直播消息缓存添加最新50条到用户缓存
					Hashtable<Integer, List<ILiveMessage>> frameLiveMap = ApplicationCache.getFrameLiveMap();
					List<ILiveMessage> liveMsgList = frameLiveMap.get(liveId);
					// 如果缓存中没有直播消息，从数据中读取
					if (null == liveMsgList) {
						liveMsgList = messageMng.getList(liveId, LIVE_MSG_TYPE_LIVE, null, null, null, null,
								null, null, false, true, null);
						frameLiveMap.put(liveId, liveMsgList);
					}
					tempMsgList = null;
					try {
						tempMsgList = liveMsgList.subList(liveMsgList.size() - LAST_MSG_NUM,
								liveMsgList.size());
					} catch (Exception e) {
						tempMsgList = liveMsgList.subList(0, liveMsgList.size());
					}
					for (int i = 0; i < tempMsgList.size(); i++) {
						try {
							ILiveMessage iLiveMessage = tempMsgList.get(i);
							ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
							//10 禁言   11 解禁 
							if(iLiveEstoppel!=null){
								iLiveMessage.setOpType(11);
							}else{
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
						quizList = messageMng.getList(liveId,Constants.LIVE_MSG_TYPE_QUIZ, null, null, null, null,
								null, null, false, true, null);
						quizLiveMap.put(liveId, quizList);
					}
					tempMsgList = null;
					try {
						tempMsgList = quizList.subList(quizList.size() - LAST_MSG_NUM,
								quizList.size());
					} catch (Exception e) {
						tempMsgList = quizList.subList(0, quizList.size());
					}
					for (int i = 0; i < tempMsgList.size(); i++) {
						try {
							ILiveMessage iLiveMessage = tempMsgList.get(i);
							ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
							//10 禁言   11 解禁 
							if(iLiveEstoppel!=null){
								iLiveMessage.setOpType(11);
							}else{
								iLiveMessage.setOpType(10);
							}
							userMsgList.add(iLiveMessage);
						} catch (Exception e) {
							e.printStackTrace();
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
		System.out.println("Server:cennected OK!");
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage message) throws Exception {
		TextMessage tm = new TextMessage(message.getPayload() + "");
		session.sendMessage(tm);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		users.remove(session);
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
		System.out.println("获得连接账户"+username);
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
