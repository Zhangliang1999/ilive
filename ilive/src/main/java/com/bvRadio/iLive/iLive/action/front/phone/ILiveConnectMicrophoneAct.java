package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.constants.SocketConstants;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.task.AliYunLiveTask;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

@Controller
@RequestMapping(value = "/app/room/microphone")
public class ILiveConnectMicrophoneAct {

	private static final Logger log = LoggerFactory.getLogger(ILiveConnectMicrophoneAct.class);

	@RequestMapping(value = "/getAppUserList.jspx")
	public void getAppUserList(Integer roomId, Long userId, String userName, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			JSONArray dataListJson = new JSONArray();
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
			if (null == userMap) {
				userMap = new ConcurrentHashMap<String, UserBean>();
				userListMap.put(roomId, userMap);
			}
			Iterator<Entry<String, UserBean>> iterator = userMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, UserBean> entry = (Map.Entry<String, UserBean>) iterator.next();
				UserBean userInMap = entry.getValue();
				if (null != userInMap) {
					String userIdInMap = userInMap.getUserId();
					if (null != userIdInMap && !userIdInMap.equals(String.valueOf(userId))) {
						Integer sessionType = userInMap.getSessionType();
						if (SocketConstants.SESSION_TYPE_APP.equals(sessionType)) {
							if (StringUtils.isNotBlank(userName)) {
								String usernameInMap = userInMap.getUsername();
								if (null != usernameInMap && usernameInMap.indexOf(userName) != -1) {
									IliveAppRetInfo retInfo = buildAppRet(userInMap);
									JSONObject userJson = new JSONObject(retInfo);
									dataListJson.put(userJson);
								}
							} else {
								IliveAppRetInfo retInfo = buildAppRet(userInMap);
								JSONObject userJson = new JSONObject(retInfo);
								dataListJson.put(userJson);
							}
						}
					}
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取连麦用户列表成功");
			resultJson.put("data", dataListJson);
		} catch (Exception e) {
			log.warn("ILiveConnectMicrophoneAct.getAppUserList", e);
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取连麦用户列表失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
		return;
	}

	@RequestMapping(value = "/connectToUser.jspx")
	public void connectToUser(Integer roomId, Long userId, Long connectUserId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			if (null == roomId && null == userId && null == connectUserId) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveLiveRoom liveRoom = roomMng.findById(roomId);
			if (null == liveRoom) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
			String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
			// 推流地址示例
			// rtmp://video-center.alivecdn.com/[appName]/[streamName]?vhost=直播域名
			String userPushAddr = "rtmp://video-center.alivecdn.com/" + aliyunLiveAppname + "/" + roomId
					+ "_user?vhost=" + aliyunLiveDomain;
			// 播放地址示例
			// rtmp://直播域名/[AppName]/[StreamName]
			String hostPlayAddr = "rtmp://" + aliyunLiveDomain + "/" + aliyunLiveAppname + "/" + roomId + "_host";
			UserBean connectUser = null;
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
			Iterator<Entry<String, UserBean>> iterator = userMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, UserBean> entry = (Map.Entry<String, UserBean>) iterator.next();
				UserBean userInMap = entry.getValue();
				if (null != userInMap) {
					String userIdInMap = userInMap.getUserId();
					if (null != userIdInMap) {
						Integer sessionType = userInMap.getSessionType();
						if (SocketConstants.SESSION_TYPE_APP.equals(sessionType)) {
							if (userIdInMap.equals(String.valueOf(connectUserId))) {
								connectUser = userInMap;
							}
						}
					}
				}
			}
			if (null == connectUser) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "连接用户没有找到");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			List<ILiveMessage> msgList = connectUser.getMsgList();
			if (msgList == null) {
				msgList = new ArrayList<ILiveMessage>();
				connectUser.setMsgList(msgList);
			}
			JSONObject contentJson = new JSONObject();
			contentJson.put("userPushAddr", userPushAddr);
			contentJson.put("hostPlayAddr", hostPlayAddr);
			String msgContent = contentJson.toString();
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_MICROPHONE);
			iLiveMessage.setConnectType(ILiveMessage.CONNECT_TYPE_ACCESS);
			iLiveMessage.setMsgContent(msgContent);
			iLiveMessage.setCreateTime(new Timestamp(System.currentTimeMillis()));
			iLiveMessage.setSenderId(userId);
			msgList.add(iLiveMessage);
			JSONObject dataJson = new JSONObject();
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "连接用户请求发送成功");
			resultJson.put("data", dataJson);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} catch (Exception e) {
			log.warn("ILiveConnectMicrophoneAct.connectToUser", e);
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "连接用户请求发送失败");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
	}

	@RequestMapping(value = "/acceptConnect.jspx")
	public void acceptConnect(Integer roomId, Long userId, Long connectUserId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			if (null == roomId && null == userId && null == connectUserId) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveLiveRoom liveRoom = roomMng.findById(roomId);
			if (null == liveRoom) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
			String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
			// 播放地址示例
			// rtmp://直播域名/[AppName]/[StreamName]
			String userPlayAddr = "rtmp://" + aliyunLiveDomain + "/" + aliyunLiveAppname + "/" + roomId + "_user";
			UserBean connectUser = null;
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
			Iterator<Entry<String, UserBean>> iterator = userMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, UserBean> entry = (Map.Entry<String, UserBean>) iterator.next();
				UserBean userInMap = entry.getValue();
				if (null != userInMap) {
					String userIdInMap = userInMap.getUserId();
					if (null != userIdInMap) {
						Integer sessionType = userInMap.getSessionType();
						if (SocketConstants.SESSION_TYPE_APP.equals(sessionType)) {
							if (userIdInMap.equals(String.valueOf(connectUserId))) {
								connectUser = userInMap;
							}
						}
					}
				}
			}
			if (null == connectUser) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "连接用户没有找到");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			List<ILiveMessage> msgList = connectUser.getMsgList();
			if (msgList == null) {
				msgList = new ArrayList<ILiveMessage>();
				connectUser.setMsgList(msgList);
			}
			JSONObject contentJson = new JSONObject();
			contentJson.put("userPlayAddr", userPlayAddr);
			String msgContent = contentJson.toString();
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_MICROPHONE);
			iLiveMessage.setConnectType(ILiveMessage.CONNECT_TYPE_ACCEPT);
			iLiveMessage.setMsgContent(msgContent);
			iLiveMessage.setSenderId(userId);
			iLiveMessage.setCreateTime(new Timestamp(System.currentTimeMillis()));
			msgList.add(iLiveMessage);
			AliYunLiveTask aliYunLiveTask = new AliYunLiveTask(roomId, AliYunLiveTask.MODEL_ADD);
			new Thread(aliYunLiveTask).start();
			JSONObject dataJson = new JSONObject();
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "接受连麦请求成功");
			resultJson.put("data", dataJson);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} catch (Exception e) {
			log.warn("ILiveConnectMicrophoneAct.connectToUser", e);
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "接受连麦请求失败");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
	}

	@RequestMapping(value = "/refuseConnect.jspx")
	public void refuseConnect(Integer roomId, Long userId, Long connectUserId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			if (null == roomId && null == userId && null == connectUserId) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveLiveRoom liveRoom = roomMng.findById(roomId);
			if (null == liveRoom) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			UserBean connectUser = null;
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
			Iterator<Entry<String, UserBean>> iterator = userMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, UserBean> entry = (Map.Entry<String, UserBean>) iterator.next();
				UserBean userInMap = entry.getValue();
				if (null != userInMap) {
					String userIdInMap = userInMap.getUserId();
					if (null != userIdInMap) {
						Integer sessionType = userInMap.getSessionType();
						if (SocketConstants.SESSION_TYPE_APP.equals(sessionType)) {
							if (userIdInMap.equals(String.valueOf(connectUserId))) {
								connectUser = userInMap;
							}
						}
					}
				}
			}
			if (null == connectUser) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "连接用户没有找到");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			List<ILiveMessage> msgList = connectUser.getMsgList();
			if (msgList == null) {
				msgList = new ArrayList<ILiveMessage>();
				connectUser.setMsgList(msgList);
			}
			JSONObject contentJson = new JSONObject();
			String msgContent = contentJson.toString();
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_MICROPHONE);
			iLiveMessage.setConnectType(ILiveMessage.CONNECT_TYPE_REFUSE);
			iLiveMessage.setSenderId(userId);
			iLiveMessage.setMsgContent(msgContent);
			iLiveMessage.setCreateTime(new Timestamp(System.currentTimeMillis()));
			msgList.add(iLiveMessage);
			AliYunLiveTask aliYunLiveTask = new AliYunLiveTask(roomId, AliYunLiveTask.MODEL_REMOVE);
			new Thread(aliYunLiveTask).start();
			JSONObject dataJson = new JSONObject();
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "拒绝连麦请求成功");
			resultJson.put("data", dataJson);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} catch (Exception e) {
			log.warn("ILiveConnectMicrophoneAct.connectToUser", e);
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "拒绝连麦请求失败");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
	}

	private IliveAppRetInfo buildAppRet(UserBean userBean) {
		IliveAppRetInfo retInfo = new IliveAppRetInfo();
		retInfo.setUserId(Long.parseLong(userBean.getUserId()));
		retInfo.setNailName(userBean.getNickname());
		retInfo.setLoginToken(userBean.getLoginToken());
		retInfo.setUserName(StringPatternUtil.convertEmpty(userBean.getUsername()));
		retInfo.setCertStatus(userBean.getCertStatus() == null ? 0 : userBean.getCertStatus());
		retInfo.setPhoto(userBean.getUserThumbImg() == null ? "" : userBean.getUserThumbImg());
		return retInfo;
	}

	@Autowired
	private ILiveLiveRoomMng roomMng;

}
