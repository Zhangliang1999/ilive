package com.bvRadio.iLive.iLive.action.front.finan;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.config.SystemXMLTomcatUrl;
import com.bvRadio.iLive.iLive.entity.ILiveComments;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEstoppel;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveGuests;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveMessagePraise;
import com.bvRadio.iLive.iLive.entity.ILiveRollingAdvertising;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.CommentsMessageVo;
import com.bvRadio.iLive.iLive.manager.ILiveCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEstoppelMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveGuestsMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessagePraiseMng;
import com.bvRadio.iLive.iLive.manager.ILiveRollingAdvertisingMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.google.gson.Gson;

/**
 * 消息处理
 * 
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value = "api/message")
public class ILiveMessageApiController {
	@Autowired
	private ILiveMessageMng iLiveMessageMng;// 消息管理

	@Autowired
	private ILiveEstoppelMng iLiveEstoppelMng;// 禁言

	@Autowired
	private ILiveGuestsMng iLiveGuestsMng;// 嘉宾

	@Autowired
	private ILiveManagerMng iLiveManagerMng;// 用户

	@Autowired
	private ILiveCommentsMng iLiveCommentsMng;// 评论

	@Autowired
	private ILiveMessagePraiseMng iLiveMessagePraiseMng;// 点赞

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间

	@Autowired
	private ILiveEventMng iLiveEventMng;// 场次

	@Autowired
	private SentitivewordFilterMng sentitivewordFilterMng;// 敏感词检测
	
	@Autowired
	private ILiveRollingAdvertisingMng iLiveRollingAdvertisingMng;
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	/**
	 * 删除消息
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param msgId
	 *            消息ID
	 * @param response
	 */
	@RequestMapping(value = "/delete.jspx")
	public void deleteILiveMessage(Integer roomId, Long msgId, HttpServletResponse response,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间信息不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			if (msgId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "消息不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			iLiveMessage.setRoomType(1);
			iLiveMessage.setDeleted(true);
			iLiveMessage.setEmptyAll(true);
			iLiveMessageMng.update(iLiveMessage);
			if (iLiveMessage.getLiveMsgType().equals(Constants.LIVE_MSG_TYPE_LIVE)) {

			} else {
				if("open".equals(ConfigUtils.get("redis_service"))) {
					if (iLiveMessage.getLiveMsgType().equals(Constants.LIVE_MSG_TYPE_INTERACT)) {
						boolean checked = iLiveMessage.getChecked();
						if(checked){
							try {
								JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
							} catch (Exception e) {
								e.printStackTrace();
							}
							List<String> msgIdListCheck=JedisUtils.getList("msgIdListCheck"+roomId);
							msgIdListCheck.remove(msgId+"");
							JedisUtils.del("msgIdListCheck"+roomId);
							JedisUtils.setList("msgIdListCheck"+roomId, msgIdListCheck, 0);
						}else{
							JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
							
							boolean flag=true;
							while (flag) {
								String requestionIdString=UUID.randomUUID().toString();
								if(JedisUtils.tryGetDistributedLock(roomId+"unchecklock", requestionIdString, 1)) {
									List<String> msgIdListCheckNo=JedisUtils.getList("msgIdListCheckNo"+roomId);
									
									msgIdListCheckNo.remove(msgId+"");
									
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
					} else if (iLiveMessage.getLiveMsgType().equals(Constants.LIVE_MSG_TYPE_QUIZ)) {
						// 问答消息
						JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
						
						List<String> msgIdListCheck=JedisUtils.getList("msgIdListCheck"+roomId);
						msgIdListCheck.remove(msgId+"");
						JedisUtils.del("msgIdListCheck"+roomId);
						JedisUtils.setList("msgIdListCheck"+roomId, msgIdListCheck, 0);
					}
					Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
					if(userIdList!=null&&userIdList.size()!=0) {
						for (String userId:userIdList) {
							
							List<String> msgIdList =new ArrayList<String>();
							msgIdList.add(iLiveMessage.getMsgId()+"");
							JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
							iLiveMessage.setRoomType(1);
							JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
							
							
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
					// 聊天互动消息
					Hashtable<Integer, List<ILiveMessage>> map = null;
					if (iLiveMessage.getLiveMsgType().equals(Constants.LIVE_MSG_TYPE_INTERACT)) {
						boolean checked = iLiveMessage.getChecked();
						if(checked){
							map = ApplicationCache.getChatInteractiveMap();
						}else{
							map = ApplicationCache.getChatInteractiveMapNO();
						}
					} else if (iLiveMessage.getLiveMsgType().equals(Constants.LIVE_MSG_TYPE_QUIZ)) {
						// 问答消息
						map = ApplicationCache.getQuizLiveMap();
					}
					List<ILiveMessage> list = map.get(roomId);
					if (list != null) {
						List<ILiveMessage> messages = new ArrayList<ILiveMessage>();
						for (ILiveMessage message : list) {
							if (message.getMsgId().equals(msgId)) {
								messages.add(message);
							}
						}
						for (ILiveMessage message : messages) {
							list.remove(message);
						}
						map.put(roomId, list);
						ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
								.getUserListMap();
						ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
						if (null == userMap) {
							userMap = new ConcurrentHashMap<String, UserBean>();
							userListMap.put(roomId, userMap);
						}
						Iterator<String> userIterator = userMap.keySet().iterator();
						while (userIterator.hasNext()) {
							String key = userIterator.next();
							UserBean user = userMap.get(key);
							List<ILiveMessage> userMsgList = user.getMsgList();
							if (userMsgList == null) {
								userMsgList = new ArrayList<ILiveMessage>();
							}
							userMsgList.add(iLiveMessage);
							user.setMsgList(userMsgList);
							userMap.put(key, user);
						}
					}
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 发送 消息
	 * 
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
	 * @param msgType
	 *            消息体类型 1 文字 2 图片
	 * @param response
	 */
	@RequestMapping(value = "/save.jspx")
	public void saveILiveMessage(Integer msgType, Long userId, Integer roomId, String msgContent, Integer liveMsgType,
			String fontColor, Long liveEventId,Integer isLiveSend, Integer selectType, HttpServletResponse response,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null || userId == null || msgContent == null || liveMsgType == null || liveEventId == null
					|| msgType == null) {
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

			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
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
			msgContent = URLDecoder.decode(msgContent, "utf-8");
			msgContent = ExpressionUtil.replaceTitleToKey(msgContent);
			// 消息源正文
			iLiveMessage.setMsgOrginContent(msgContent);
			// 消息正文
			iLiveMessage.setMsgContent(msgContent);
			// 消息类型
			iLiveMessage.setMsgType(msgType);
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
			iLiveMessage.setSenderType(1);
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
			iLiveMessage.setIsLiveSend(isLiveSend);
			iLiveMessage = iLiveMessageMng.save(iLiveMessage);
			if (!JedisUtils.exists(("msg:"+iLiveMessage.getMsgId()).getBytes())) {
				JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
			}
			if (liveMsgType.equals(Constants.LIVE_MSG_TYPE_LIVE)) {
				// 图文直播消息
			} else {
				Hashtable<Integer, List<ILiveMessage>> iLiveMessageMap = null;
				if (liveMsgType.equals(Constants.LIVE_MSG_TYPE_INTERACT)) {
					iLiveMessageMap = ApplicationCache.getChatInteractiveMap();
				} else if (liveMsgType.equals(Constants.LIVE_MSG_TYPE_QUIZ)) {
					// 问答消息
					iLiveMessageMap = ApplicationCache.getQuizLiveMap();
				}
				if("open".equals(ConfigUtils.get("redis_service"))) {
					JedisUtils.listAdd("msgIdListCheck"+roomId, iLiveMessage.getMsgId()+"");
				}else {
					if (iLiveMessageMap != null) {
						List<ILiveMessage> list = iLiveMessageMap.get(roomId);
						if (list == null) {
							list = new ArrayList<ILiveMessage>();
						}
						list.add(iLiveMessage);
						iLiveMessageMap.put(roomId, list);
					}
				}
				
				List<ILiveMessage> list = new ArrayList<ILiveMessage>();
				list.add(iLiveMessage);
				Iterator<String> userIterator = map.keySet().iterator();
				System.out.println("发送消息结束----------------------------------------------------------------------------"+new Date());
				if("open".equals(ConfigUtils.get("redis_service"))) {
					Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
					if(userIdList!=null&&userIdList.size()!=0) {
						for (String userIds : userIdList) {
							boolean flag=true;
							while (flag) {
								String requestionIdString=UUID.randomUUID().toString();
								if(JedisUtils.tryGetDistributedLock(userIds+"lock", requestionIdString, 1)) {
									JedisUtils.listAdd(roomId+":"+userIds, iLiveMessage.getMsgId()+"");
									flag=false;
									JedisUtils.releaseDistributedLock(userIds+"lock", requestionIdString);
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
				}
				System.out.println("发送消息结束----------------------------------------------------------------------------"+new Date());
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 根据消息ID 禁言
	 * 
	 * @param response
	 * @param msgId
	 *            消息ID
	 * @param estoppelType
	 *            类型 0 禁言 1 解禁
	 */
	@RequestMapping(value = "/estoppel.jspx")
	public void estoppelMessage(HttpServletRequest request, HttpServletResponse response, Long msgId,
			Integer estoppelType) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			iLiveMessage.setRoomType(1);
			Integer liveRoomId = iLiveMessage.getLiveRoomId();
			Long senderId = iLiveMessage.getSenderId();
			ILiveEstoppel iLiveEstoppel = new ILiveEstoppel();
			iLiveEstoppel.setRoomId(liveRoomId);
			iLiveEstoppel.setUserId(senderId);
			Hashtable<Integer, Hashtable<Long, ILiveEstoppel>> userEstopMap = ApplicationCache.getUserEstopMap();
			Hashtable<Long, ILiveEstoppel> estopMap = userEstopMap.get(liveRoomId);
			if (null == estopMap) {
				estopMap = new Hashtable<Long, ILiveEstoppel>();
			}
			if (estoppelType == 0) {
				iLiveEstoppelMng.insertILiveEstoppel(iLiveEstoppel);
				estopMap.put(senderId, iLiveEstoppel);
			} else {
				// System.out.println("");
				iLiveEstoppelMng.deleteILiveEstoppel(iLiveEstoppel);
				estopMap.remove(senderId);
				// System.out.println("删除禁言缓存：用户ID："+estopMap.get(senderId));
			}
			if("open".equals(ConfigUtils.get("redis_service"))) {
				List<String> msgIdNoCheckList =JedisUtils.getList("msgIdListCheckNo"+liveRoomId);
				if(msgIdNoCheckList!=null&&msgIdNoCheckList.size()!=0) {
					for (String id : msgIdNoCheckList) {
						ILiveMessage message=SerializeUtil.getObject(id);
						message.setRoomType(1);
						if (estoppelType == 0) {
							message.setOpType(11);
						} else {
							message.setOpType(10);
						}
						JedisUtils.delObject("msg:"+message.getMsgId());
						JedisUtils.setByte(("msg:"+message.getMsgId()).getBytes(), SerializeUtil.serialize(message), 0);
					}
				}
				
				List<String> msgIdCheckList =JedisUtils.getList("msgIdListCheck"+liveRoomId);
				if(msgIdCheckList!=null&&msgIdCheckList.size()!=0) {
					for (String id : msgIdCheckList) {
						ILiveMessage message=SerializeUtil.getObject(id);
						message.setRoomType(1);
						if (estoppelType == 0) {
							message.setOpType(11);
						} else {
							message.setOpType(10);
						}
						JedisUtils.delObject("msg:"+message.getMsgId());
						JedisUtils.setByte(("msg:"+message.getMsgId()).getBytes(), SerializeUtil.serialize(message), 0);
					}
				}
				
			}else {
				// 审核通过聊天互动消息
				Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
				List<ILiveMessage> list = chatInteractiveMap.get(liveRoomId);
				for (ILiveMessage message : list) {
					if (message.getSenderId().equals(senderId)) {
						if (estoppelType == 0) {
							message.setOpType(11);
						} else {
							message.setOpType(10);
						}
					}
				}
				// 未审核消息
				Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache.getChatInteractiveMapNO();
				List<ILiveMessage> listMapNo = chatInteractiveMapNO.get(liveRoomId);
				for (ILiveMessage message : listMapNo) {
					if (message.getSenderId().equals(senderId)) {
						if (estoppelType == 0) {
							message.setOpType(11);
						} else {
							message.setOpType(10);
						}
					}
				}
				
			}
			iLiveMessage.setUpdate(true);
			if (estoppelType == 0) {
				iLiveMessage.setOpType(11);
			} else {
				iLiveMessage.setOpType(10);
			}
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userList = userListMap.get(liveRoomId);
			if (userList == null) {
				userList = new ConcurrentHashMap<String, UserBean>();
			}
			Iterator<String> userIterator = userList.keySet().iterator();
			Set<String> userIdList =JedisUtils.getSet("userIdList"+liveRoomId);
			
				if("open".equals(ConfigUtils.get("redis_service"))) {
					JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
					JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
					if(userIdList!=null&&userIdList.size()!=0) {
						for (String userId : userIdList) {
							boolean flag=true;
							while (flag) {
								String requestionIdString=UUID.randomUUID().toString();
								if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
									JedisUtils.listAdd(liveRoomId+":"+userId, iLiveMessage.getMsgId()+"");
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
				}
			
			while (userIterator.hasNext()) {
				String key = userIterator.next();
				UserBean user = userList.get(key);
				// System.out.println("禁言用户或者解禁用户："+senderId);
				if (user.getUserType() == 1 || user.getUserId().equals(String.valueOf(senderId))) {
					// 给所有企业用户和管理员发送消息 及 被禁言用户发消息
					// System.out.println("给禁言用户或解禁用户发送请求");
						List<ILiveMessage> userMsgList = user.getMsgList();
						if (userMsgList == null) {
							userMsgList = new ArrayList<ILiveMessage>();
						}
						userMsgList.add(iLiveMessage);
						user.setMsgList(userMsgList);
						userList.put(key, user);
					
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 清空
	 * 
	 * @param response
	 * @param roomId
	 */
	@RequestMapping(value = "/deleteAll.jspx")
	public void deleteMessageAll(HttpServletRequest request, HttpServletResponse response, Integer roomId) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间信息不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			if("open".equals(ConfigUtils.get("redis_service"))) {
				List<String> msgIdCheckList =JedisUtils.getList("msgIdListCheck"+roomId);
				ILiveMessage iLiveMessage = null;
				if (msgIdCheckList.size() > 0) {
					iLiveMessage = SerializeUtil.getObject(msgIdCheckList.get(msgIdCheckList.size() - 1));
				} else {
					iLiveMessage = new ILiveMessage();
				}
				iLiveMessage.setDeleteAll(true);
				iLiveMessage.setRoomType(1);
				
					JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
					iLiveMessage.setRoomType(1);
					JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
					Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
					if(userIdList!=null&&userIdList.size()!=0) {
						for (String userId : userIdList) {
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
					
				
				List<ILiveMessage> InteractiveMap =new ArrayList<ILiveMessage>();
				for (String id : msgIdCheckList) {
					InteractiveMap.add(SerializeUtil.getObject(id));
				}
				iLiveMessageMng.deleteInteractiveMapAll(InteractiveMap);
				JedisUtils.del("msgIdListCheck"+roomId);
			}else {
				Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
				List<ILiveMessage> InteractiveMap = chatInteractiveMap.get(roomId);
				if (InteractiveMap == null) {
					InteractiveMap = new ArrayList<ILiveMessage>();
				}
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
				if (userMap == null) {
					userMap = new ConcurrentHashMap<String, UserBean>();
				}
				Iterator<String> userIterator = userMap.keySet().iterator();
				ILiveMessage iLiveMessage = null;
				if (InteractiveMap.size() > 0) {
					iLiveMessage = InteractiveMap.get(InteractiveMap.size() - 1);
				} else {
					iLiveMessage = new ILiveMessage();
				}
				iLiveMessage.setDeleteAll(true);
				iLiveMessage.setRoomType(1);
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					UserBean user = userMap.get(key);
					List<ILiveMessage> userMsgList = user.getMsgList();
					if (userMsgList == null) {
						userMsgList = new ArrayList<ILiveMessage>();
					}
					userMsgList.add(iLiveMessage);
					user.setMsgList(userMsgList);
					userMap.put(key, user);
				}
				iLiveMessageMng.deleteInteractiveMapAll(InteractiveMap);
				chatInteractiveMap.remove(roomId);
			}
			
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 回答
	 * 
	 * @param response
	 * @param roomId
	 * @param replyContent
	 * @param msgId
	 */
	@RequestMapping(value = "/addReplyContent.jspx")
	public void addILiveMessageReplyContent(HttpServletRequest request, HttpServletResponse response, Integer roomId,
			String replyContent, Long msgId, Long userId) {
		JSONObject resultJson = new JSONObject();
		try {
			if (userId == null || roomId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveManager liveManager = iLiveManagerMng.selectILiveManagerById(userId);
			if (liveManager == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveMessage iliveMessage = iLiveMessageMng.findById(msgId);
			if (replyContent != null && !replyContent.equals("")) {
				try {
					replyContent = URLDecoder.decode(replyContent, "utf-8");
					replyContent = replyContent.replaceAll("[^\\u0000-\\uFFFF]", "*");
					System.out.println("content:  " + replyContent);
				} catch (UnsupportedEncodingException e) {
					replyContent = "";
					e.printStackTrace();
				}
			}
			// 路径处理
			Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
			List<ILiveMessage> list = quizLiveMap.get(roomId);
			if (list != null) {
				for (ILiveMessage message : list) {
					if (message.getMsgId().equals(msgId)) {
						message.setReplyContent(replyContent);
						message.setReplyType(1);
						message.setReplyName(liveManager.getNailName());
					}
				}
			} else {
				list = new ArrayList<ILiveMessage>();
			}
			iliveMessage.setReplyContent(replyContent);
			iliveMessage.setReplyType(1);
			iliveMessage.setUpdate(true);
			iliveMessage.setRoomType(1);
			iliveMessage.setReplyName(liveManager.getNailName());
			// 禁言
			Hashtable<Integer, Hashtable<Long, ILiveEstoppel>> userEstopMap = ApplicationCache.getUserEstopMap();
			Hashtable<Long, ILiveEstoppel> estopMap = userEstopMap.get(roomId);
			if (estopMap == null) {
				estopMap = new Hashtable<Long, ILiveEstoppel>();
				List<ILiveEstoppel> estoppels = iLiveEstoppelMng.selectILiveEstoppels(roomId);
				for (ILiveEstoppel iLiveEstoppel : estoppels) {
					estopMap.put(iLiveEstoppel.getUserId(), iLiveEstoppel);
				}
				userEstopMap.put(roomId, estopMap);
			}
			ILiveEstoppel iLiveEstoppel = estopMap.get(userId);
			// 10 禁言 11 解禁
			if (iLiveEstoppel != null) {
				iliveMessage.setOpType(11);
			} else {
				iliveMessage.setOpType(10);
			}

			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
			if (userMap == null) {
				userMap = new ConcurrentHashMap<String, UserBean>();
			}
			Iterator<String> userIterator = userMap.keySet().iterator();
			if("open".equals(ConfigUtils.get("redis_service"))) {
				
				JedisUtils.delObject("msg:"+iliveMessage.getMsgId());
				JedisUtils.setByte(("msg:"+iliveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iliveMessage), 0);
				Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
				if(userIdList!=null&&userIdList.size()!=0) {
					for (String userIds : userIdList) {
						boolean flag=true;
						while (flag) {
							String requestionIdString=UUID.randomUUID().toString();
							if(JedisUtils.tryGetDistributedLock(userIds+"lock", requestionIdString, 1)) {
								JedisUtils.listAdd(roomId+":"+userIds, iliveMessage.getMsgId()+"");
								flag=false;
								JedisUtils.releaseDistributedLock(userIds+"lock", requestionIdString);
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
				while (userIterator.hasNext()) {
					
					String key = userIterator.next();
					UserBean user = userMap.get(key);
					List<ILiveMessage> userMsgList = user.getMsgList();
					if (userMsgList == null) {
						userMsgList = new ArrayList<ILiveMessage>();
					}
					userMsgList.add(iliveMessage);
					user.setMsgList(userMsgList);
					userMap.put(key, user);
				
				
			}
			}
			
			iLiveMessageMng.update(iliveMessage);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 检索回答
	 * 
	 * @param response
	 * @param roomId
	 * @param replyType
	 */
	@RequestMapping(value = "/retrieve.jspx", method = RequestMethod.GET)
	public void retrieveILiveMessageReplyContent(HttpServletRequest request, HttpServletResponse response,
			Integer roomId, Integer replyType) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
			List<ILiveMessage> list = quizLiveMap.get(roomId);
			if (list == null) {
				list = new ArrayList<ILiveMessage>();
			}
			List<Long> msgIds = new ArrayList<Long>();
			List<Long> msgIds0 = new ArrayList<Long>();
			List<Long> msgIds1 = new ArrayList<Long>();
			for (ILiveMessage iLiveMessage : list) {
				msgIds.add(iLiveMessage.getMsgId());
				if (iLiveMessage.getReplyType() == 1) {
					msgIds1.add(iLiveMessage.getMsgId());
				}
				if (iLiveMessage.getReplyType() == 0) {
					msgIds0.add(iLiveMessage.getMsgId());
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("msgIds", msgIds);
			resultJson.put("msgIds0", msgIds0);
			resultJson.put("msgIds1", msgIds1);
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 是否置顶处理
	 * 
	 * @param response
	 * @param roomId
	 *            直播间ID
	 * @param msgId
	 *            消息ID
	 * @param top
	 *            置顶状态
	 */
	@RequestMapping(value = "/top.jspx")
	public void topILiveMessage(HttpServletRequest request, HttpServletResponse response, Integer roomId, Long msgId,
			Integer top) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null || msgId == null || top == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			iLiveMessage.setRoomType(1);
			iLiveMessage.setTop(top);
			if (iLiveMessage.getLiveMsgType().equals(Constants.LIVE_MSG_TYPE_LIVE)) {
				resultJson.put("type", 1);
			} else {
				resultJson.put("type", 0);
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
				if (userMap == null) {
					userMap = new ConcurrentHashMap<String, UserBean>();
				}

				if (iLiveMessage.getLiveMsgType().equals(Constants.LIVE_MSG_TYPE_INTERACT)) {
					
					if("open".equals(ConfigUtils.get("redis_service"))) {
						List<String> msgIdList=JedisUtils.getList("msgIdListCheck"+roomId);
						if(msgIdList!=null&&msgIdList.size()!=0) {
							for (String id : msgIdList) {
								ILiveMessage message=SerializeUtil.getObject(id);
								if (message.getMsgId().equals(msgId)) {
									message.setTop(top);
									JedisUtils.delObject("msg:"+message.getMsgId());
									JedisUtils.setByte(("msg:"+message.getMsgId()).getBytes(), SerializeUtil.serialize(message), 0);
								}
							}
						}
						
					}else {
						Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache
								.getChatInteractiveMap();
						List<ILiveMessage> list = chatInteractiveMap.get(roomId);
						for (ILiveMessage message : list) {
							if (message.getMsgId().equals(msgId)) {
								message.setTop(top);
							}
						}
					}
					
					
				}
				iLiveMessage.setUpdate(true);
				Hashtable<Integer, Hashtable<Long, ILiveEstoppel>> userEstopMap = ApplicationCache.getUserEstopMap();
				Hashtable<Long, ILiveEstoppel> estopMap = userEstopMap.get(roomId);
				if (estopMap == null) {
					estopMap = new Hashtable<Long, ILiveEstoppel>();
					List<ILiveEstoppel> estoppels = iLiveEstoppelMng.selectILiveEstoppels(roomId);
					for (ILiveEstoppel iLiveEstoppel : estoppels) {
						estopMap.put(iLiveEstoppel.getUserId(), iLiveEstoppel);
					}
					userEstopMap.put(roomId, estopMap);
				}
				ILiveEstoppel iLiveEstoppel = estopMap.get(iLiveMessage.getSenderId());
				// 10 禁言 11 解禁
				if (iLiveEstoppel != null) {
					iLiveMessage.setOpType(11);
				} else {
					iLiveMessage.setOpType(10);
				}
				Iterator<String> userIterator = userMap.keySet().iterator();
				if("open".equals(ConfigUtils.get("redis_service"))) {
					JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
					JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
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
					while (userIterator.hasNext()) {
						String key = userIterator.next();
						UserBean user = userMap.get(key);
						
							List<ILiveMessage> userMsgList = user.getMsgList();
							if (userMsgList == null) {
								userMsgList = new ArrayList<ILiveMessage>();
							}
							userMsgList.add(iLiveMessage);
							user.setMsgList(userMsgList);
							userMap.put(key, user);
						
						
					}
				}
				
			}
			iLiveMessageMng.update(iLiveMessage);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 删除评论
	 * 
	 * @param commentsId
	 *            评论ID
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/comments/delete.jspx")
	public void deleteILiveComments(Long commentsId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			iLiveCommentsMng.deleteILiveComments(commentsId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "删除评论成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "删除评论失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 通过审核
	 * 
	 * @param msgId
	 *            消息ID
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/updateChecked.jspx")
	public void updateChecked(Long msgId, Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveMessage message = iLiveMessageMng.updateCheckById(msgId, true);
			
			//删除审核缓存
			if("open".equals(ConfigUtils.get("redis_service"))) {
				
				JedisUtils.delObject("msg:"+message.getMsgId());
				message.setRoomType(1);
				message.setUpdate(true);
				JedisUtils.setByte(("msg:"+message.getMsgId()).getBytes(), SerializeUtil.serialize(message), 0);
				
				boolean flag=true;
				while (flag) {
					String requestionIdString=UUID.randomUUID().toString();
					if(JedisUtils.tryGetDistributedLock(roomId+"unchecklock", requestionIdString, 1)) {
						List<String> msgIdListCheckNo=JedisUtils.getList("msgIdListCheckNo"+roomId);
						
							msgIdListCheckNo.remove(msgId+"");
						
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
				
				
				JedisUtils.listAdd("msgIdListCheck"+roomId, msgId+"");
				
				Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
				if(userIdList!=null&&userIdList.size()!=0) {
					for(String userId:userIdList) {
						boolean flag2=true;
						while (flag2) {
							String requestionIdString=UUID.randomUUID().toString();
							if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
								JedisUtils.listAdd(roomId+":"+userId, msgId+"");
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
				Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache.getChatInteractiveMapNO();
				List<ILiveMessage> chatInteractiveListNo = chatInteractiveMapNO.get(roomId);
				ILiveMessage iLiveMessageNo = null;
				for (ILiveMessage iLiveMessage : chatInteractiveListNo) {
					if(iLiveMessage.getMsgId().equals(msgId)){
						iLiveMessageNo = iLiveMessage;
					}
				}
				if(iLiveMessageNo!=null) {
					chatInteractiveListNo.remove(iLiveMessageNo);
					
					message.setOpType(iLiveMessageNo.getOpType());
					Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
					List<ILiveMessage> list = chatInteractiveMap.get(roomId);
					iLiveMessageNo.setChecked(true);
					iLiveMessageNo.setUpdate(false);
					list.add(iLiveMessageNo);
					ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
							.getUserListMap();
					ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
					if(userMap!=null){
						Iterator<String> userIterator = userMap.keySet().iterator();
						while (userIterator.hasNext()) {
							String key = userIterator.next();
							UserBean user = userMap.get(key);
							List<ILiveMessage> userMsgList = user.getMsgList();
							if (userMsgList == null) {
								userMsgList = new ArrayList<ILiveMessage>();
							}
							message.setUpdate(true);
							message.setRoomType(1);
							userMsgList.add(message);
							user.setMsgList(userMsgList);
							userMap.put(key, user);
						}
					}
				}
				
			}
			
			
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "审核成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "审核失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	// 话题
	// ==========================================================================================================================
	/**
	 * 获取直播间 话题
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/selectMessageLIVE.jspx")
	public void selectMessageLIVE(Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			List<JSONObject> lists = new ArrayList<JSONObject>();
			List<ILiveMessage> list = iLiveMessageMng.getList(roomId, Constants.LIVE_MSG_TYPE_LIVE, null, null, null,
					null, null, null, false, true, null, false);
			if (list == null) {
				list = new ArrayList<ILiveMessage>();
			}
			for (ILiveMessage iLiveMessage : list) {
				lists.add(iLiveMessage.putMessageInJson(null));
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			Gson gson = new Gson();
			resultJson.put("messageList", gson.toJson(lists));
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
			Integer commentsAllow = liveEvent.getCommentsAllow();
			if (commentsAllow == null) {
				commentsAllow = 0;
			}
			Integer commentsAudit = liveEvent.getCommentsAudit();
			if (commentsAudit == null) {
				commentsAudit = 0;
			}
			resultJson.put("commentsAllow", commentsAllow);
			resultJson.put("commentsAudit", commentsAudit);
			ILiveGuests iLiveGuests = iLiveGuestsMng.selectILiveGuestsById(roomId);
			if (iLiveGuests == null) {
				iLiveGuests = new ILiveGuests();
				iLiveGuests.setGuestsLabel("直播方");
				iLiveGuests.setGuestsName("主持人");
				iLiveGuests.setRoomId(roomId);
				iLiveGuestsMng.addILiveGuest(iLiveGuests);
			}
			resultJson.put("iLiveGuests", iLiveGuests.putMessageInJson(null));
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 获取直播间所有话题评论
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/comments/selectComments.jspx")
	public void selectILiveComments(Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			List<ILiveMessage> list = iLiveMessageMng.getList(roomId, Constants.LIVE_MSG_TYPE_LIVE, null, null, null,
					null, null, null, false, true, null, false);
			if (list == null) {
				list = new ArrayList<ILiveMessage>();
			}
			List<CommentsMessageVo> voList = new ArrayList<CommentsMessageVo>();
			Map<Long, List<ILiveMessagePraise>> map = new HashMap<Long, List<ILiveMessagePraise>>();
			List<ILiveComments> iLiveCommentsListYES = new ArrayList<ILiveComments>();
			List<ILiveComments> iLiveCommentsListNO = new ArrayList<ILiveComments>();
			for (ILiveMessage iLiveMessage : list) {
				List<ILiveComments> commentsList = iLiveCommentsMng
						.selectILiveCommentsListByMsgId(iLiveMessage.getMsgId(), false);
				if (commentsList == null) {
					commentsList = new ArrayList<ILiveComments>();
				}
				for (ILiveComments iLiveComments : commentsList) {
					String comments = iLiveComments.getComments();
					iLiveComments.setComments(ExpressionUtil.replaceKeyToImg(comments));
					boolean b = iLiveEstoppelMng.getILiveEstoppelYesOrNo(roomId, iLiveComments.getUserId());
					if (b) {
						iLiveComments.setEstoppelType(1);
					} else {
						iLiveComments.setEstoppelType(0);
					}
					if (iLiveComments.getIsChecked()) {
						iLiveCommentsListYES.add(iLiveComments);
					} else {
						iLiveCommentsListNO.add(iLiveComments);
					}
				}
				CommentsMessageVo vo = new CommentsMessageVo();
				vo.setMsgId(iLiveMessage.getMsgId());
				vo.setList(commentsList);
				voList.add(vo);
				List<ILiveMessagePraise> praises = iLiveMessagePraiseMng
						.selectILiveMessagePraisByMsgId(iLiveMessage.getMsgId());
				map.put(iLiveMessage.getMsgId(), praises);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			Gson gson = new Gson();
			resultJson.put("iLiveCommentsMap", gson.toJson(voList));
			// 获取点赞数
			resultJson.put("praiseMap", gson.toJson(map));

			// 排序
			for (int i = 0; i < iLiveCommentsListYES.size(); i++) {
				for (int j = 1; j < iLiveCommentsListYES.size() - i; j++) {
					if ((iLiveCommentsListYES.get(j - 1).getCreateTime())
							.compareTo(iLiveCommentsListYES.get(j).getCreateTime()) < 0) {
						ILiveComments iLiveComments = iLiveCommentsListYES.get(j - 1);
						iLiveCommentsListYES.set((j - 1), iLiveCommentsListYES.get(j));
						iLiveCommentsListYES.set(j, iLiveComments);
					}
				}
			}
			resultJson.put("iLiveCommentsListYES", gson.toJson(iLiveCommentsListYES));

			// 排序
			for (int i = 0; i < iLiveCommentsListNO.size(); i++) {
				for (int j = 1; j < iLiveCommentsListNO.size() - i; j++) {
					if ((iLiveCommentsListNO.get(j - 1).getCreateTime())
							.compareTo(iLiveCommentsListNO.get(j).getCreateTime()) < 0) {
						ILiveComments iLiveComments = iLiveCommentsListNO.get(j - 1);
						iLiveCommentsListNO.set((j - 1), iLiveCommentsListNO.get(j));
						iLiveCommentsListNO.set(j, iLiveComments);
					}
				}
			}
			resultJson.put("iLiveCommentsListNO", gson.toJson(iLiveCommentsListNO));

		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 获取话题所有评论
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/comments/selectCommentsAllByMsgId.jspx")
	public void selectILiveCommentsByMsgId(Long msgId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (msgId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "选择话题失败");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			Integer roomId = iLiveMessage.getLiveRoomId();
			List<ILiveComments> commentsList = iLiveCommentsMng.selectILiveCommentsListByMsgId(msgId, false);
			for (ILiveComments iLiveComments : commentsList) {
				String comments = iLiveComments.getComments();
				iLiveComments.setComments(ExpressionUtil.replaceKeyToImg(comments));
				boolean b = iLiveEstoppelMng.getILiveEstoppelYesOrNo(roomId, iLiveComments.getUserId());
				if (b) {
					iLiveComments.setEstoppelType(1);
				} else {
					iLiveComments.setEstoppelType(0);
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			Gson gson = new Gson();
			resultJson.put("commentsList", gson.toJson(commentsList));
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 修改话题 是否允许评论
	 * 
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/topic/comments/allow.jspx")
	public void updateCommentsAllow(Long evenId, Integer commentsAllow, HttpServletResponse response,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			iLiveEventMng.updateILiveEventByCommentsAllow(evenId, commentsAllow);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "修改成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "修改失败");
			// System.out.println(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 修改话题 评论是否需要审核
	 * 
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/topic/comments/audit.jspx")
	public void updateCommentsAudit(Long evenId, Integer commentsAudit, HttpServletResponse response,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			iLiveEventMng.updateILiveEventByCommentsAudit(evenId, commentsAudit);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "修改成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "修改失败");
			// System.out.println(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 修改话题发送人信息
	 * 
	 * @param roomId
	 *            ID
	 * @param guestsName
	 *            昵称
	 * @param guestsLabel
	 *            标签
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/topic/guests/update.jspx")
	public void updateILiveGuests(Integer roomId, String guestsName, String guestsLabel, HttpServletResponse response,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			guestsName = URLDecoder.decode(guestsName, "utf-8");
			guestsLabel = URLDecoder.decode(guestsLabel, "utf-8");
			ILiveGuests iLiveGuests = new ILiveGuests();
			iLiveGuests.setRoomId(roomId);
			iLiveGuests.setGuestsLabel(guestsLabel);
			iLiveGuests.setGuestsName(guestsName);
			iLiveGuestsMng.updateILiveGuests(iLiveGuests);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "修改成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "修改失败");
			// System.out.println(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 发送 话题 消息
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param msgContent
	 *            发送内容
	 * @param iLiveEvent
	 *            场次ID
	 * @param msgType
	 *            消息体类型 1 文字 2 图片 3 视频 4 图片和文字 5 视频和文字
	 * @param images
	 *            图集路径
	 * @param videos
	 *            视频路径
	 * @param response
	 */
	@RequestMapping(value = "/topic/save.jspx")
	public void saveTopicILiveMessage(Integer msgType, Integer roomId, String msgContent, Long liveEventId,
			String images, String videos, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null || liveEventId == null || msgType == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveGuests iLiveGuests = iLiveGuestsMng.selectILiveGuestsById(roomId);
			ILiveMessage iLiveMessage = new ILiveMessage();
			// 直播场次ID
			ILiveEvent iLiveEvent = new ILiveEvent();
			iLiveEvent.setLiveEventId(liveEventId);
			iLiveMessage.setLive(iLiveEvent);
			// 直播间ID
			iLiveMessage.setLiveRoomId(roomId);
			// 消息IP iLiveIpAddress;
			// 发送人名称
			iLiveMessage.setSenderName(iLiveGuests.getGuestsName());
			// 用户ID
			iLiveMessage.setSenderId(0l);
			// 发送人头像
			iLiveMessage.setSenderImage(iLiveGuests.getGuestsLabel());
			// 发送人级别
			iLiveMessage.setSenderLevel(0);
			msgContent = URLDecoder.decode(msgContent, "utf-8");
			msgContent = ExpressionUtil.replaceTitleToKey(msgContent);
			// 消息源正文
			iLiveMessage.setMsgOrginContent(msgContent);
			// 消息正文
			iLiveMessage.setMsgContent(msgContent);
			// 消息类型
			iLiveMessage.setMsgType(msgType);
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
			iLiveMessage.setLiveMsgType(1);
			// 字体颜色
			iLiveMessage.setFontColor("");
			iLiveMessage.setIMEI(null);
			iLiveMessage.setWidth(null);
			iLiveMessage.setHeight(null);
			iLiveMessage.setServiceType(1);
			iLiveMessage.setSenderType(1);
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
			iLiveMessage.setImages(images);
			iLiveMessage.setVideos(videos);
			iLiveMessage = iLiveMessageMng.save(iLiveMessage);
			if (!JedisUtils.exists(("msg:"+iLiveMessage.getMsgId()).getBytes())) {
				JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
			}
			if("open".equals(ConfigUtils.get("redis_service"))) {
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
				// 通知有新增话题
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
				if(userMap!=null){
					Iterator<String> userIterator = userMap.keySet().iterator();
					while (userIterator.hasNext()) {
						String key = userIterator.next();
						UserBean user = userMap.get(key);
						
							List<ILiveMessage> userMsgList = user.getMsgList();
							if (userMsgList == null) {
								userMsgList = new ArrayList<ILiveMessage>();
							}
							iLiveMessage.setRoomType(2);
							userMsgList.add(iLiveMessage);
							user.setMsgList(userMsgList);
							userMap.put(key, user);
						
						
					}
				}
			}
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 修改 话题 消息
	 * 
	 * @param msgId
	 *            话题ID
	 * @param msgType
	 *            话题类型
	 * @param msgContent
	 *            发送内容
	 * @param images
	 *            图集路径
	 * @param videos
	 *            视频路径
	 * @param response
	 */
	@RequestMapping(value = "/topic/update.jspx")
	public void updateTopicILiveMessage(Long msgId, Integer msgType, String msgContent, String images, String videos,
			HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (msgId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			if (msgContent != null) {
				msgContent = URLDecoder.decode(msgContent, "utf-8");
				msgContent = ExpressionUtil.replaceTitleToKey(msgContent);
			}
			iLiveMessage.setMsgContent(msgContent);
			iLiveMessage.setMsgOrginContent(msgContent);
			iLiveMessage.setImages(images);
			iLiveMessage.setMsgType(msgType);
			iLiveMessage.setVideos(videos);
			iLiveMessageMng.update(iLiveMessage);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("iLiveMessage", iLiveMessage);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 清空话题
	 * 
	 * @param roomId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/topic/clear.jspx")
	public void clearAllTopic(Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			List<ILiveMessage> list = iLiveMessageMng.getList(roomId, Constants.LIVE_MSG_TYPE_LIVE, null, null, null,
					null, null, null, false, true, null, false);
			if (list == null) {
				list = new ArrayList<ILiveMessage>();
			}
			iLiveMessageMng.deleteInteractiveMapAll(list);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "清空成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "清空失败");
			// System.out.println(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 获取话题信息
	 * 
	 * @param msgId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/topic/selectById.jspx")
	public void selectTopicById(Long msgId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			String msgContent = iLiveMessage.getMsgContent();
			resultJson.put("iLiveMessage", iLiveMessage.putMessageInJson(null));
			if (msgContent == null) {
				msgContent = "";
			}
			msgContent = ExpressionUtil.replaceKeyToTitle(msgContent);
			resultJson.put("content", msgContent);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			// System.out.println(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 删除 话题信息
	 * 
	 * @param msgId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/topic/deleteById.jspx")
	public void deleteTopicById(Long msgId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			iLiveMessage.setDeleted(true);
			iLiveMessage.setEmptyAll(true);
			iLiveMessageMng.update(iLiveMessage);
			JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			// System.out.println(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 审核通过评论
	 * 
	 * @param commentsId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/topic/comments/updateAuditById.jspx")
	public void updateCommentsAuditTopicById(Long commentsId, HttpServletResponse response,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveComments iLiveComments = iLiveCommentsMng.getILiveCommentsById(commentsId);
			iLiveComments.setIsChecked(true);
			iLiveCommentsMng.updateIliveCommentsById(iLiveComments);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			// System.out.println(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 置顶话题
	 * 
	 * @param msgId
	 * @param top
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/topic/updateTop.jspx")
	public void updateTopicByIdOrTop(Long msgId, Integer top, HttpServletResponse response,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			iLiveMessage.setTop(top);
			iLiveMessageMng.update(iLiveMessage);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			// System.out.println(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 评论禁言
	 * 
	 * @param commentsId
	 *            评论ID
	 * @param estoppelType
	 *            禁言标示 1 禁言 0 否
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/topic/updateEstoppel.jspx")
	public void updateCommentsEstoppel(Long commentsId, Integer estoppelType, HttpServletResponse response,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveComments iLiveComments = iLiveCommentsMng.getILiveCommentsById(commentsId);
			Long userId = iLiveComments.getUserId();
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(iLiveComments.getMsgId());
			Integer roomId = iLiveMessage.getLiveRoomId();
			Hashtable<Integer, Hashtable<Long, ILiveEstoppel>> userEstopMap = ApplicationCache.getUserEstopMap();
			if (estoppelType == 1) {
				ILiveEstoppel iLiveEstoppel = new ILiveEstoppel();
				iLiveEstoppel.setRoomId(roomId);
				iLiveEstoppel.setUserId(userId);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				iLiveEstoppel.setCreateTime(Timestamp.valueOf(format.format(new Date())));
				iLiveEstoppelMng.insertILiveEstoppel(iLiveEstoppel);
				Hashtable<Long, ILiveEstoppel> userEstopList = userEstopMap.get(roomId);
				userEstopList.put(userId, iLiveEstoppel);
				userEstopMap.put(roomId, userEstopList);
			} else {
				ILiveEstoppel iLiveEstoppel = iLiveEstoppelMng.getILiveEstoppel(roomId, userId);
				iLiveEstoppelMng.deleteILiveEstoppel(iLiveEstoppel);
				Hashtable<Long, ILiveEstoppel> userEstopList = userEstopMap.get(roomId);
				userEstopList.remove(userId);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			// 消息通知禁言用户
			// 审核通过聊天互动消息
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
			List<ILiveMessage> list = chatInteractiveMap.get(roomId);
			for (ILiveMessage message : list) {
				if (message.getSenderId().equals(userId)) {
					if (estoppelType == 1) {
						message.setOpType(11);
					} else {
						message.setOpType(10);
					}
				}
			}
			// 未审核消息
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache.getChatInteractiveMapNO();
			List<ILiveMessage> listMapNo = chatInteractiveMapNO.get(roomId);
			for (ILiveMessage message : listMapNo) {
				if (message.getSenderId().equals(userId)) {
					if (estoppelType == 1) {
						message.setOpType(11);
						iLiveMessage.setOpType(11);
					} else {
						message.setOpType(10);
						iLiveMessage.setOpType(10);
					}
				}
			}
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userList = userListMap.get(roomId);
			if (userList == null) {
				userList = new ConcurrentHashMap<String, UserBean>();
			}
			Iterator<String> userIterator = userList.keySet().iterator();
			ILiveMessage iLiveMessage2 = new ILiveMessage();
			// 直播场次ID
			ILiveEvent iLiveEvent = new ILiveEvent();
			iLiveEvent.setLiveEventId(0l);
			iLiveMessage2.setLive(iLiveEvent);
			// 直播间ID
			iLiveMessage2.setLiveRoomId(roomId);
			// 消息IP iLiveIpAddress;
			// 发送人名称
			iLiveMessage2.setSenderName("");
			// 用户ID
			iLiveMessage2.setSenderId(userId);
			// 发送人头像
			iLiveMessage2.setSenderImage("");
			// 发送人级别
			iLiveMessage2.setSenderLevel(0);
			// 消息源正文
			iLiveMessage2.setMsgOrginContent("");
			// 消息正文
			iLiveMessage2.setMsgContent("");
			// 消息类型
			iLiveMessage2.setMsgType(1);
			// 创建时间
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			iLiveMessage2.setCreateTime(Timestamp.valueOf(format.format(new Date())));
			// 状态
			iLiveMessage2.setStatus(0);
			// 删除标识
			iLiveMessage2.setDeleted(false);
			// 审核状态
			iLiveMessage2.setChecked(true);
			// 消息类型
			iLiveMessage2.setLiveMsgType(2);
			// 字体颜色
			iLiveMessage2.setFontColor("");
			iLiveMessage2.setIMEI(null);
			iLiveMessage2.setWidth(null);
			iLiveMessage2.setHeight(null);
			iLiveMessage2.setServiceType(1);
			iLiveMessage2.setSenderType(0);
			// 消息业务类型
			// 1为互动聊天室消息（原聊天业务不变）
			// 2为送礼物
			// 3 为连麦申请
			// 4 连麦同意
			// 5 观众进入房间
			// 6 观众离开房间
			// 7直播结束
			// 8 连麦结束
			iLiveMessage2.setExtra(null); // 为连麦等复杂消息类型封装信息
			// 是否置顶 0 否 1 置顶
			iLiveMessage2.setTop(0);
			// 处理类型 10 禁言 11 解禁 12 关闭互动 13开启互动
			if (estoppelType == 1) {
				iLiveMessage2.setOpType(11);
			} else {
				iLiveMessage2.setOpType(10);
			}
			iLiveMessage2.setRoomType(1);
			iLiveMessage2.setEmptyAll(false);
			iLiveMessage2.setUpdate(true);
			while (userIterator.hasNext()) {
				String key = userIterator.next();
				UserBean user = userList.get(key);
				// System.out.println("禁言用户或者解禁用户："+userId);
				if (user.getUserType() == 1 || user.getUserId().equals(String.valueOf(userId))) {
					// 给所有企业用户和管理员发送消息 及 被禁言用户发消息
					// System.out.println("给禁言用户或解禁用户发送请求");
					List<ILiveMessage> userMsgList = user.getMsgList();
					if (userMsgList == null) {
						userMsgList = new ArrayList<ILiveMessage>();
					}
					userMsgList.add(iLiveMessage2);
					user.setMsgList(userMsgList);
					userList.put(key, user);
				}
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			// System.out.println(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 解禁
	 * 
	 * @param response
	 * @param userId
	 *            用户ID
	 * @param roomId
	 *            直播间ID
	 */
	@RequestMapping(value = "/delete/estoppel.jspx")
	public void deleteEstoppelMessage(HttpServletRequest request, HttpServletResponse response, Long userId,
			Integer roomId) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveEstoppel iLiveEstoppel = new ILiveEstoppel();
			iLiveEstoppel.setRoomId(roomId);
			iLiveEstoppel.setUserId(userId);
			Hashtable<Integer, Hashtable<Long, ILiveEstoppel>> userEstopMap = ApplicationCache.getUserEstopMap();
			Hashtable<Long, ILiveEstoppel> estopMap = userEstopMap.get(roomId);
			if (null == estopMap) {
				estopMap = new Hashtable<Long, ILiveEstoppel>();
			}
			iLiveEstoppelMng.deleteILiveEstoppel(iLiveEstoppel);
			estopMap.remove(userId);
			ILiveMessage iLiveMessage = new ILiveMessage();
			int number = 1;
			
			if("open".equals(ConfigUtils.get("redis_service"))) {
				List<String> msgIdNoCheckList =JedisUtils.getList("msgIdListCheckNo"+roomId);
				if(msgIdNoCheckList!=null&&msgIdNoCheckList.size()!=0) {
					for (String id : msgIdNoCheckList) {
						ILiveMessage message=SerializeUtil.getObject(id);
						message.setRoomType(1);
						message.setOpType(10);
						
						JedisUtils.delObject("msg:"+message.getMsgId());
						JedisUtils.setByte(("msg:"+message.getMsgId()).getBytes(), SerializeUtil.serialize(message), 0);
					}
				}
				
				List<String> msgIdCheckList =JedisUtils.getList("msgIdListCheck"+roomId);
				if(msgIdCheckList!=null&&msgIdCheckList.size()!=0) {
					for (String id : msgIdCheckList) {
						ILiveMessage message=SerializeUtil.getObject(id);
						message.setRoomType(1);
						message.setOpType(10);
						
						JedisUtils.delObject("msg:"+message.getMsgId());
						JedisUtils.setByte(("msg:"+message.getMsgId()).getBytes(), SerializeUtil.serialize(message), 0);
					}
				}
				iLiveMessage.setUpdate(true);
				iLiveMessage.setOpType(10);
				Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
				if(userIdList!=null&&userIdList.size()!=0) {
					for (String userIds : userIdList) {
						boolean flag=true;
						while (flag) {
							String requestionIdString=UUID.randomUUID().toString();
							if(JedisUtils.tryGetDistributedLock(userIds+"lock", requestionIdString, 1)) {
								JedisUtils.listAdd(roomId+":"+userIds, iLiveMessage.getMsgId()+"");
								flag=false;
								JedisUtils.releaseDistributedLock(userIds+"lock", requestionIdString);
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
				// 审核通过聊天互动消息
				Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
				List<ILiveMessage> list = chatInteractiveMap.get(roomId);
				if(list==null){
					list = new  ArrayList<ILiveMessage>();
				}
				for (ILiveMessage message : list) {
					if (message.getSenderId().equals(userId)) {
						message.setOpType(10);
						if (number == 1) {
							BeanUtilsExt.copyProperties(iLiveMessage, message);
							number += 1;
						}
					}
				}
				// 未审核消息
				Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache.getChatInteractiveMapNO();
				List<ILiveMessage> listMapNo = chatInteractiveMapNO.get(roomId);
				if(listMapNo==null){
					listMapNo = new  ArrayList<ILiveMessage>();
				}
				for (ILiveMessage message : listMapNo) {
					if (message.getSenderId().equals(userId)) {
						message.setOpType(10);
						if (number == 1) {
							BeanUtilsExt.copyProperties(iLiveMessage, message);
							number += 1;
						}
					}
				}
				iLiveMessage.setUpdate(true);
				iLiveMessage.setOpType(10);
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				ConcurrentHashMap<String, UserBean> userList = userListMap.get(roomId);
				if (userList == null) {
					userList = new ConcurrentHashMap<String, UserBean>();
				}
				Iterator<String> userIterator = userList.keySet().iterator();
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					UserBean user = userList.get(key);
					// System.out.println("禁言用户或者解禁用户："+senderId);
					if (user.getUserType() == 1 || user.getUserId().equals(String.valueOf(userId))) {
						// 给所有企业用户和管理员发送消息 及 被禁言用户发消息
						// System.out.println("给禁言用户或解禁用户发送请求");
						List<ILiveMessage> userMsgList = user.getMsgList();
						if (userMsgList == null) {
							userMsgList = new ArrayList<ILiveMessage>();
						}
						userMsgList.add(iLiveMessage);
						user.setMsgList(userMsgList);
						userList.put(key, user);
					}
				}
			}
			
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	@RequestMapping(value = "/deleteReplyContent.jspx")
	public void deleteReplyContentByMsgId(Integer roomId, Long msgId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			iLiveMessage.setDeleted(true);
			iLiveMessage.setEmptyAll(true);
			iLiveMessageMng.update(iLiveMessage);
			if("open".equals(ConfigUtils.get("redis_service"))) {
				
				JedisUtils.delList("msgIdListCheck"+roomId, msgId+"", 10);
			}else {
				Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
				List<ILiveMessage> list = quizLiveMap.get(roomId);
				for (ILiveMessage iLiveMessage2 : list) {
					if (iLiveMessage2.getMsgId().equals(msgId)) {
						list.remove(iLiveMessage2);
						break;
					}
				}
			}
			if("open".equals(ConfigUtils.get("redis_service"))) {
				
				JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
				JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
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
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
				if(concurrentHashMap!=null){
					Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
					iLiveMessage.setUpdate(true);
					iLiveMessage.setRoomType(1);
					while (userIterator.hasNext()) {
						
							String key = userIterator.next();
							UserBean user = concurrentHashMap.get(key);
							List<ILiveMessage> msgList = user.getMsgList();
							if (msgList == null) {
								msgList = new ArrayList<ILiveMessage>();
							}
							msgList.add(iLiveMessage);
						
					}
				}
			}
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 修改滚动广告
	 * @param roomId 直播间ID
	 * @param content 广告内容
	 * @param startType 是否启动
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/updateAdvertising.jspx",method=RequestMethod.GET)
	public void updateILiveRollingAdvertising(Integer roomId,String content,Integer startType, HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom liveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			if(liveLiveRoom==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间获取失败");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}else{
				Integer enterpriseId = liveLiveRoom.getEnterpriseId();
				ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
				Integer certStatus = iLiveEnterprise.getCertStatus();
				boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_Advertising,certStatus);
				
			}
			ILiveRollingAdvertising iLiveRollingAdvertising = new ILiveRollingAdvertising();
			iLiveRollingAdvertising.setRoomId(roomId);
			content = URLDecoder.decode(content, "utf-8");
			iLiveRollingAdvertising.setContent(content);
			iLiveRollingAdvertising.setStartType(startType);
			iLiveRollingAdvertisingMng.updateILiveRollingAdvertising(iLiveRollingAdvertising);
			if(JedisUtils.exists(("iLiveRolling:"+roomId).getBytes())) {
				JedisUtils.del("iLiveRolling:"+roomId);
				JedisUtils.setByte(("iLiveRolling:"+roomId).getBytes(), SerializeUtil.serialize(iLiveRollingAdvertising), 300);
				
			}else {
				JedisUtils.setByte(("iLiveRolling:"+roomId).getBytes(), SerializeUtil.serialize(iLiveRollingAdvertising), 300);
			}
               if("open".equals(ConfigUtils.get("redis_service"))) {
            	   ILiveMessage iLiveMessage = new ILiveMessage();
					iLiveMessage.setRoomType(4);
					iLiveMessage.setMsgId(Long.parseLong((-roomId)+""));
				JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
				JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
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
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
				if(concurrentHashMap!=null){
					Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
					ILiveMessage iLiveMessage = new ILiveMessage();
					iLiveMessage.setRoomType(4);
					while (userIterator.hasNext()) {
						String key = userIterator.next();
						UserBean user = concurrentHashMap.get(key);
						List<ILiveMessage> msgList = user.getMsgList();
						if (msgList == null) {
							msgList = new ArrayList<ILiveMessage>();
						}
						msgList.add(iLiveMessage);
					}
				}
			}
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
}
