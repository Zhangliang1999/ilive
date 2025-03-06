package com.bvRadio.iLive.iLive.web.mina;

import static com.bvRadio.iLive.iLive.Constants.LIVE_MSG_TYPE_INTERACT;
import static com.bvRadio.iLive.iLive.Constants.LIVE_MSG_TYPE_LIVE;
import static com.bvRadio.iLive.iLive.Constants.SOCKET_SESSION_USER_KEY;
import static com.bvRadio.iLive.iLive.Constants.SOCKET_SESSION_LIVE_ID_KEY;

import java.util.Hashtable;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;

public class MinaServerHandler extends IoHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(MinaServerHandler.class);
	private static final int LAST_MSG_NUM = 20;

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
		// 无效的消息
		if (".".equals(message.toString())) {
			return;
		}
		log.info("messageReceived. sessionId = {} ,message = {}", session.getId(), message);
		try {
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
			Cache cache = CacheManager.getCacheInfo(CacheManager.mobile_token_ + token);
			if (cache == null || cache.isExpired()) {
				log.info("token错误，关闭session。 sessionId = {} ,message = {}", session.getId(), message);
				session.close(false);
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
				session.close(false);
			} else {
				iLiveUser.setSession(session);
				session.setAttribute(SOCKET_SESSION_LIVE_ID_KEY, liveId);
				session.setAttribute(SOCKET_SESSION_USER_KEY, iLiveUser);

				List<ILiveMessage> userMsgList = iLiveUser.getMsgList();
				// 从互动消息缓存添加最新20条到用户缓存
				Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
				List<ILiveMessage> interactCheckedMsgList = chatInteractiveMap.get(liveId);
				if (null == interactCheckedMsgList) {
					interactCheckedMsgList = messageMng.getList(liveId, LIVE_MSG_TYPE_INTERACT, null,
							null, null, null, null, null, false, true, null);
					chatInteractiveMap.put(liveId, interactCheckedMsgList);
				}
				List<ILiveMessage> tempMsgList = null;
				try {
					tempMsgList = interactCheckedMsgList.subList(
							interactCheckedMsgList.size() - LAST_MSG_NUM, interactCheckedMsgList.size());
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
				// 图文直播消息缓存添加最新20条到用户缓存
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
						userMsgList.add(iLiveMessage);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				// 问答消息缓存添加最新20条到用户缓存
				Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
				List<ILiveMessage> quizList = quizLiveMap.get(liveId);
				// 如果缓存中没有问答消息，从数据中读取
				if (null == quizList) {
					quizList = messageMng.getList(liveId, Constants.LIVE_MSG_TYPE_QUIZ, null, null, null, null,
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
						userMsgList.add(iLiveMessage);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				
			}
		} catch (JSONException e) {
//			log.info("不是JSON结构。 sessionId = {} ,message = {}", session.getId(), message, e);
		} catch (Exception e) {
//			log.error("messageReceived error. sessionId = {} ,message = {}", session.getId(), message,
//					e);
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
			UserBean user = (UserBean) session.getAttribute(SOCKET_SESSION_USER_KEY);
			if (null != liveId && null != user) {
				Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				Hashtable<String, UserBean> userList = userListMap.get(liveId);
				userList.remove(user);
//				Integer userId = user.getUserId();
//				log.info("sessionClosed. sessionId = {}, liveId = {}, userId = {}", session.getId(),
//						liveId, userId);
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

}
