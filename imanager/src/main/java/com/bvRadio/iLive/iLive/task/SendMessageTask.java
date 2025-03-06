package com.bvRadio.iLive.iLive.task;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
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
	}

	public void run() {
		// 获取所有的连接用户
		Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache
				.getUserListMap();
		Iterator<Integer> listIterator = userListMap.keySet().iterator();
		while (listIterator.hasNext()) {
			try {
				// 直播间ID
				Integer liveId = listIterator.next();
				// System.out.println("消息线程中的liveID=="+liveId);
				// 拿到一场直播的在线用户
				Hashtable<String, UserBean> userMap = userListMap.get(liveId);
				// System.out.println("消息线程中的userMap.size()=="+userMap.size());
				// 初始化一个容器
				if (null == userMap) {
					userMap = new Hashtable<String, UserBean>();
					userListMap.put(liveId, userMap);
				}
				Iterator<String> mapIterator = userMap.keySet().iterator();
				while (mapIterator.hasNext()) {
					String key = mapIterator.next();
					UserBean user = userMap.get(key);
					
					// System.out.println("ioSession=="+ioSession);
					List<ILiveMessage> userMsgList = user.getMsgList();
					// System.out.println("userMsgList=="+userMsgList.size());
					if (null == userMsgList) {
						userMsgList = new ArrayList<ILiveMessage>();
						user.setMsgList(userMsgList);
					}
					try {
						if (user.getSessionType() == 1) {
							//WEB用户
							WebSocketSession webSocketSession = user.getWebSocketSession();
							if(webSocketSession!=null){
								for (ILiveMessage message : userMsgList) {
									String msgContent = message.getMsgContent();
									if(msgContent!=null){
										msgContent = ExpressionUtil.replaceKeyToImg(msgContent);
										message.setMsgContent(msgContent);
									}
									JSONObject msgJson = message.putMessageInJson(null);
									TextMessage textMessage = new TextMessage(msgJson.toString());
									webSocketSession.sendMessage(textMessage);
								}
								userMsgList.clear();
							}
						} else{
							//APP用户
							IoSession ioSession = user.getSession();
							if(ioSession!=null){
								for (ILiveMessage message : userMsgList) {
									JSONObject msgJson = message
											.putMessageInJson(null);
									ioSession.write(msgJson.toString());
								}
								userMsgList.clear();
							}
						}
					} catch (Exception e) {
						log.error("用户缓存的消息发送到客户端出错", e);
					}
				}
			} catch (Exception e) {
				log.error("用户缓存的消息发送到客户端出错", e);
			}
		}

	}
}