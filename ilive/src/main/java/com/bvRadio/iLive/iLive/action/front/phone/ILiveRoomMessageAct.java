package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.config.SystemXMLTomcatUrl;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEstoppel;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@RequestMapping(value = "/livephone/room/message")
public class ILiveRoomMessageAct {
	
	Logger logger = LoggerFactory.getLogger(ILiveRoomMessageAct.class);

	@Autowired
	private ILiveMessageMng iLiveMessageMng;// 消息管理



	@Autowired
	private ILiveManagerMng iLiveManagerMng;// 用户



	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	/**
	 * 第一次和socket连接 获取token
	 */
	/**
	 * 用户登录直播间
	 * 
	 * @param userId
	 *            用户ID
	 * @param liveId
	 *            直播间ID
	 * @param sessionType
	 *            登录类型 0 IoSession（APP） 1 WebSocketSession(WEB登录)
	 * @param userType
	 *            用户类型 0 个人 1企业
	 * @param appType App连接方式  0 直播间   1 推流
	 * @param webType 统计
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/intoRoom.jspx")
	public void entLiveLogin(String userId, Integer liveId, Integer sessionType, Integer userType, Integer webType,Integer appType,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			webType = webType == null ? 1 : webType;
			appType = appType==null ? 0 : appType;
			ILiveLiveRoom live = iLiveLiveRoomMng.findById(liveId);
			if (live == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			UserBean liveUser = new UserBean();
			try {
				ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(Long.parseLong(userId));
				liveUser.setUserType(userType);
				liveUser.setUserId(String.valueOf(iLiveManager.getUserId()));
				liveUser.setUsername(iLiveManager.getNailName());
				liveUser.setUserThumbImg(iLiveManager.getUserImg());
				liveUser.setLevel(iLiveManager.getLevel());
				liveUser.setSessionType(sessionType);
			} catch (Exception e) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户获取失败");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(liveId);
			Integer enterpriseId = iliveRoom.getEnterpriseId();
			List<ILiveLiveRoom> rooms = iLiveLiveRoomMng.findRoomListPassByEnterprise(enterpriseId);
			long number = 0;
			for (ILiveLiveRoom iLiveLiveRoom : rooms) {
				ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(iLiveLiveRoom.getRoomId());
				if(concurrentHashMap==null){
					concurrentHashMap = new ConcurrentHashMap<String, UserBean>();
				}
				number = number +concurrentHashMap.size();
			}
			ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			boolean b = EnterpriseUtil.selectIfContent(EnterpriseCache.ENTERPRISE_Concurrent, number, enterpriseId, iLiveEnterPrise.getCertStatus());
			if(b){
				resultJson.put("code", Constants.ERROR_full);
				resultJson.put("message", "直播间观看人数已满，无法进行观看");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new ConcurrentHashMap<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			// System.out.println("生存客户端登录token 的liveid=" + liveId);
			String key = userId + "_" + request.getSession().getId();
			userMap.put(key, liveUser);
			// 生存客户端登录token
			UUID uuid = UUID.randomUUID();
			String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
			JSONObject json = new JSONObject();
			json.put("liveId", liveId);
			json.put("roomId", liveId);
			json.put("userId", key);
			json.put("webType", webType);
			json.put("appType", appType);
			json.put("liveEventId", live.getLiveEvent().getLiveEventId());
			CacheManager.putCacheInfo(CacheManager.mobile_token_ + token, json, 5 * 60 * 1000);
			// 直播内容
			JSONObject liveJson = live.putLiveInJson(null);
			liveJson.put("token", token);
			try {
				// 记录
				// liveMng.addViewNumById(liveId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("data", liveJson);
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_USER);
			iLiveMessage.setWelcomeLanguage("欢迎【"+liveUser.getNickname()+"】进入直播间");
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(liveId);
			if(concurrentHashMap!=null){
				Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
				while (userIterator.hasNext()) {
						String id = userIterator.next();
						UserBean user = concurrentHashMap.get(id);
						List<ILiveMessage> msgList = user.getMsgList();
						if (msgList == null) {
							msgList = new ArrayList<ILiveMessage>();
						}
						msgList.add(iLiveMessage);
					
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 接收消息接口
	 */
	/**
	 * @param userId
	 *            用户ID
	 * @param roomId
	 *            直播间ID
	 * @param msgContent
	 *            发送内容
	 * @param liveMsgType
	 *            直播消息类型
	 * @param fontColor
	 *            字体颜色
	 * @param iLiveEvent
	 *            场次ID
	 * @param selectType
	 *            消息选择 0 当前登录用户 1 选择虚拟用户 2 嘉宾用户
	 * @param response
	 */
	@RequestMapping(value = "/save.do")
	public void saveILiveMessage(Long userId, Integer roomId, String msgContent, Integer liveMsgType, String fontColor,
			Long liveEventId, Integer selectType, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null || userId == null || msgContent == null || liveMsgType == null || liveEventId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			Hashtable<Integer, Hashtable<Long, ILiveEstoppel>> userEstopMap = ApplicationCache.getUserEstopMap();
			Hashtable<Long, ILiveEstoppel> estopMap = userEstopMap.get(roomId);
			if (estopMap != null) {
				ILiveEstoppel iLiveEstoppel = estopMap.get(userId);
				if (iLiveEstoppel != null) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "你已被禁言");
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			}

			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			ConcurrentHashMap<String, UserBean> map = userListMap.get(roomId);
			if (map == null) {
				map = new ConcurrentHashMap<String, UserBean>();
			}
			UserBean userBean = null;
			if (selectType == 0) {
				userBean = ILiveUtils.getUser(request);
			} else if (selectType == 1) {
				// 虚拟网友
				try {
					List<UserBean> outUserBeanXml = SystemXMLTomcatUrl.outUserBeanXml();
					for (UserBean userBean2 : outUserBeanXml) {
						if (userBean2.getUserId().equals(userId)) {
							userBean = userBean2;
						}
					}
				} catch (Exception e) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户信息不存在");
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请选择发送消息用户类型");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}

			if (userBean == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户信息不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveMessage iLiveMessage = new ILiveMessage();
			// 直播场次ID
			ILiveEvent iLiveEvent = new ILiveEvent();
			iLiveEvent.setLiveEventId(liveEventId);
			iLiveMessage.setLive(iLiveEvent);
			// 直播间ID
			iLiveMessage.setLiveRoomId(roomId);
			// 消息IP iLiveIpAddress;
			// 发送人名称
			iLiveMessage.setSenderName(userBean.getUsername());
			// 用户ID
			iLiveMessage.setSenderId(Long.valueOf(userBean.getUserId()));
			// 发送人头像
			iLiveMessage.setSenderImage(userBean.getUserThumbImg());
			// 发送人级别
			iLiveMessage.setSenderLevel(userBean.getLevel());
			msgContent = ExpressionUtil.replaceTitleToKey(msgContent);
			// 消息源正文
			iLiveMessage.setMsgOrginContent(msgContent);
			// 消息正文
			iLiveMessage.setMsgContent(msgContent);
			// 消息类型
			iLiveMessage.setMsgType(Constants.MSG_TYPE_TXT);
			// 创建时间
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			iLiveMessage.setCreateTime(Timestamp.valueOf(format.format(new Date())));
			// 状态
			iLiveMessage.setStatus(0);
			// 删除标识
			iLiveMessage.setDeleted(false);
			// 审核状态
			iLiveMessage.setChecked(true);
			// 消息类型
			iLiveMessage.setLiveMsgType(liveMsgType);
			// 字体颜色
			iLiveMessage.setFontColor(fontColor);
			iLiveMessage.setIMEI(null);
			iLiveMessage.setWidth(null);
			iLiveMessage.setHeight(null);
			iLiveMessage.setServiceType(1);
			iLiveMessage.setSenderType(userBean.getUserType());
			// 消息业务类型
			// 1为互动聊天室消息（原聊天业务不变）
			// 2为送礼物
			// 3 为连麦申请
			// 4 连麦同意
			// 5 观众进入房间
			// 6 观众离开房间
			// 7直播结束
			// 8 连麦结束
			iLiveMessage.setExtra(null); // 为连麦等复杂消息类型封装信息
			// 是否置顶 0 否 1 置顶
			iLiveMessage.setTop(0);
			// 处理类型 10 禁言 11 解禁 12 关闭互动 13开启互动
			iLiveMessage.setOpType(10);
			iLiveMessage.setRoomType(1);
			iLiveMessage.setEmptyAll(false);
			iLiveMessage = iLiveMessageMng.save(iLiveMessage);
			Hashtable<Integer, List<ILiveMessage>> iLiveMessageMap = null;
			if (liveMsgType.equals(Constants.LIVE_MSG_TYPE_INTERACT)) {
				iLiveMessageMap = ApplicationCache.getChatInteractiveMap();
			} else if (liveMsgType.equals(Constants.LIVE_MSG_TYPE_QUIZ)) {
				// 问答消息
				iLiveMessageMap = ApplicationCache.getQuizLiveMap();
			}
			if (iLiveMessageMap != null) {
				List<ILiveMessage> list = iLiveMessageMap.get(roomId);
				list.add(iLiveMessage);
				iLiveMessageMap.put(roomId, list);
			}
			Iterator<String> userIterator = map.keySet().iterator();
			while (userIterator.hasNext()) {
				String key = userIterator.next();
				UserBean user = map.get(key);
				List<ILiveMessage> userMsgList = user.getMsgList();
				if (userMsgList == null) {
					userMsgList = new ArrayList<ILiveMessage>();
				}
				userMsgList.add(iLiveMessage);
				user.setMsgList(userMsgList);
				map.put(key, user);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
}
