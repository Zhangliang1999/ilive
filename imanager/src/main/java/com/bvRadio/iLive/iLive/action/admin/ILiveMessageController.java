package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.bvRadio.iLive.iLive.config.SystemXMLTomcatUrl;
import com.bvRadio.iLive.iLive.entity.ILiveComments;
import com.bvRadio.iLive.iLive.entity.ILiveEstoppel;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveGuests;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveMessagePraise;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.CommentsMessageVo;
import com.bvRadio.iLive.iLive.manager.ILiveCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveEstoppelMng;
import com.bvRadio.iLive.iLive.manager.ILiveGuestsMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessagePraiseMng;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.google.gson.Gson;

/**
 * 消息处理  
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="message")
public class ILiveMessageController {
	@Autowired
	private ILiveMessageMng iLiveMessageMng;//消息管理
	
	@Autowired
	private ILiveEstoppelMng iLiveEstoppelMng;//禁言
	
	@Autowired
	private ILiveGuestsMng iLiveGuestsMng;//嘉宾
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;//用户
	
	@Autowired
	private ILiveCommentsMng iLiveCommentsMng;//评论
	
	@Autowired
	private ILiveMessagePraiseMng iLiveMessagePraiseMng;//点赞
	/**
	 * 删除消息
	 * @param roomId 直播间ID
	 * @param msgId 消息ID
	 * @param response
	 */
	@RequestMapping(value="/delete.do")
	public void deleteILiveMessage(Integer roomId,Long msgId,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间信息不存在");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
			if(msgId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "消息不存在");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			iLiveMessage.setRoomType(1);
			iLiveMessage.setDeleted(true);
			iLiveMessageMng.update(iLiveMessage);
			//聊天互动消息
			Hashtable<Integer, List<ILiveMessage>> map = null;
			if(iLiveMessage.getLiveMsgType().equals(Constants.LIVE_MSG_TYPE_INTERACT)){
				map = ApplicationCache.getChatInteractiveMap();
			}else if(iLiveMessage.getLiveMsgType().equals(Constants.LIVE_MSG_TYPE_LIVE)){
				//图文直播消息
				map = ApplicationCache.getFrameLiveMap();
			}else if(iLiveMessage.getLiveMsgType().equals(Constants.LIVE_MSG_TYPE_QUIZ)){
				//问答消息
				map = ApplicationCache.getQuizLiveMap();
			}
			List<ILiveMessage> list = map.get(roomId);
			if(list!=null){
				for (ILiveMessage message : list) {
					if(message.getMsgId().equals(msgId)){
						message.setDeleted(true);
					}
				}
				map.put(roomId, list);
				Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
				Hashtable<String, UserBean> userMap = userListMap.get(roomId);
				if (null == userMap) {
					userMap = new Hashtable<String, UserBean>();
					userListMap.put(roomId, userMap);
				}
				Iterator<String> userIterator = userMap.keySet().iterator();
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					UserBean user = userMap.get(key);
					List<ILiveMessage> userMsgList = user.getMsgList();
					userMsgList.add(iLiveMessage);
				}	
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 发送   消息
	 * @param userId 用户ID
	 * @param roomId 直播间ID
	 * @param msgContent 发送内容
	 * @param liveMsgType 直播消息类型
	 * @param fontColor 字体颜色
	 * @param iLiveEvent 场次ID
	 * @param selectType 消息选择  0 当前登录用户    1 选择虚拟用户   2 嘉宾用户
	 * @param response
	 */

	@RequestMapping(value="/save.do")
	public void saveILiveMessage(Long userId,Integer roomId,String msgContent,Integer liveMsgType,String fontColor,Long liveEventId,Integer selectType, HttpServletResponse response,HttpServletRequest request){
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null||userId==null||msgContent==null||liveMsgType==null||liveEventId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				ResponseUtils.renderJson(request,response, resultJson.toString());
				return;
			}
			Hashtable<Integer, Hashtable<Long, ILiveEstoppel>> userEstopMap = ApplicationCache.getUserEstopMap();
			Hashtable<Long, ILiveEstoppel> estopMap = userEstopMap.get(roomId);
			if(estopMap != null){
				ILiveEstoppel iLiveEstoppel = estopMap.get(userId);
				if(iLiveEstoppel!=null){
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "你已被禁言");
					ResponseUtils.renderJson(request,response, resultJson.toString());
					return;
				}
			}
			
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> map = userListMap.get(roomId);
			if(map==null){
				map = new Hashtable<String, UserBean>();
			}
			UserBean userBean = null;
			if(selectType==0){
				userBean = map.get(String.valueOf(userId));
			}else if(selectType==1){
				//虚拟网友
				try {
					List<UserBean> outUserBeanXml = SystemXMLTomcatUrl.outUserBeanXml();
					for (UserBean userBean2 : outUserBeanXml) {
						if(userBean2.getUserId().equals(userId)){
							userBean = userBean2;
						}
					}
				} catch (Exception e) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户信息不存在");
					ResponseUtils.renderJson(request,response, resultJson.toString());
					return;
				}
			}else if(selectType==2){
				ILiveGuests iLiveGuests = iLiveGuestsMng.selectILiveGuestsByID(userId);
				userBean = new UserBean();
				userBean.setUserId(String.valueOf(userId));
				userBean.setUserThumbImg(iLiveGuests.getGuestsImage());
				userBean.setUsername(iLiveGuests.getGuestsName());
				userBean.setLevel(0);
				userBean.setUserType(0);
				
			}else{
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请选择发送消息用户类型");
				ResponseUtils.renderJson(request,response, resultJson.toString());
				return;
			}
			
			if(userBean==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户信息不存在");
				ResponseUtils.renderJson(request,response, resultJson.toString());
				return;
			}
			ILiveMessage iLiveMessage = new ILiveMessage();
			//直播场次ID
			ILiveEvent iLiveEvent = new ILiveEvent();
			iLiveEvent.setLiveEventId(liveEventId);
			iLiveMessage.setLive(iLiveEvent);
			//直播间ID
			iLiveMessage.setLiveRoomId(roomId);
			//消息IP iLiveIpAddress;
			//发送人名称
			iLiveMessage.setSenderName(userBean.getUsername());
			//用户ID
			iLiveMessage.setSenderId(Long.valueOf(userBean.getUserId()));
			//发送人头像
			iLiveMessage.setSenderImage(userBean.getUserThumbImg());
			//发送人级别
			iLiveMessage.setSenderLevel(userBean.getLevel());
			msgContent = ExpressionUtil.replaceTitleToKey(msgContent);
			//消息源正文
			iLiveMessage.setMsgOrginContent(msgContent);
			//消息正文
			iLiveMessage.setMsgContent(msgContent);
			//消息类型
			iLiveMessage.setMsgType(Constants.MSG_TYPE_TXT);
			// 创建时间
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			iLiveMessage.setCreateTime(Timestamp.valueOf(format.format(new Date())));
			//状态
			iLiveMessage.setStatus(0);
			//删除标识
			iLiveMessage.setDeleted(false);
			//审核状态
			iLiveMessage.setChecked(true);
			//消息类型
			iLiveMessage.setLiveMsgType(liveMsgType);
			//字体颜色
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
			//是否置顶  0 否  1 置顶 
			iLiveMessage.setTop(0);
			//处理类型  10 禁言   11 解禁     12 关闭互动   13开启互动
			iLiveMessage.setOpType(10);
			iLiveMessage.setRoomType(1);
			iLiveMessage = iLiveMessageMng.save(iLiveMessage);
			Hashtable<Integer, List<ILiveMessage>> iLiveMessageMap = null;
			if(liveMsgType.equals(Constants.LIVE_MSG_TYPE_INTERACT)){
				iLiveMessageMap = ApplicationCache.getChatInteractiveMap();
			}else if(liveMsgType.equals(Constants.LIVE_MSG_TYPE_LIVE)){
				//图文直播消息
				iLiveMessageMap = ApplicationCache.getFrameLiveMap();
			}else if(liveMsgType.equals(Constants.LIVE_MSG_TYPE_QUIZ)){
				//问答消息
				iLiveMessageMap = ApplicationCache.getQuizLiveMap();
			}
			if(iLiveMessageMap!=null){
				List<ILiveMessage> list = iLiveMessageMap.get(roomId);
				list.add(iLiveMessage);
				iLiveMessageMap.put(roomId, list);
			}
			List<ILiveMessage> list = new ArrayList<ILiveMessage>();
			list.add(iLiveMessage);
			Iterator<String> userIterator = map.keySet().iterator();
			while (userIterator.hasNext()) {
				String key = userIterator.next();
				UserBean user = map.get(key);
				List<ILiveMessage> userMsgList = user.getMsgList();
				userMsgList.add(iLiveMessage);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 根据消息ID 禁言
	 * @param response
	 * @param msgId 消息ID
	 * @param estoppelType 类型   0  禁言   1  解禁
	 */
	@RequestMapping(value="/estoppel.do")
	public void estoppelMessage(HttpServletResponse response,Long msgId,Integer estoppelType){
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
			if(null==estopMap){
				estopMap = new Hashtable<Long, ILiveEstoppel>();
			}
			if(estoppelType==0){
				iLiveEstoppelMng.insertILiveEstoppel(iLiveEstoppel);
				estopMap.put(senderId, iLiveEstoppel);
			}else{
				iLiveEstoppelMng.deleteILiveEstoppel(iLiveEstoppel);
				estopMap.remove(senderId);
			}
			//审核通过聊天互动消息
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
			List<ILiveMessage> list = chatInteractiveMap.get(liveRoomId);
			for (ILiveMessage message : list) {
				if(message.getSenderId().equals(senderId)){
					if(estoppelType==0){
						message.setOpType(11);
					}else{
						message.setOpType(10);
					}
				}
			}
			//未审核消息
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache.getChatInteractiveMapNO();
			List<ILiveMessage> listMapNo = chatInteractiveMapNO.get(liveRoomId);
			for (ILiveMessage message : listMapNo) {
				if(message.getSenderId().equals(senderId)){
					if(estoppelType==0){
						message.setOpType(11);
					}else{
						message.setOpType(10);
					}
				}
			}
			iLiveMessage.setUpdate(true);
			if(estoppelType==0){
				iLiveMessage.setOpType(11);
			}else{
				iLiveMessage.setOpType(10);
			}
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> userList = userListMap.get(liveRoomId);
			if(userList==null){
				userList = new Hashtable<String, UserBean>();
			}
			Iterator<String> userIterator = userList.keySet().iterator();
			while (userIterator.hasNext()) {
				String key = userIterator.next();
				UserBean user = userList.get(key);
				if(user.getUserType()==1 || user.getUserId().equals(senderId)){
					//给所有企业用户和管理员发送消息     及   被禁言用户发消息
					List<ILiveMessage> userMsgList = user.getMsgList();
					userMsgList.add(iLiveMessage);
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 清空
	 * @param response
	 * @param roomId
	 */
	@RequestMapping(value="/deleteAll.do",method=RequestMethod.POST)
	public void deleteMessageAll(HttpServletResponse response,Integer roomId){
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间信息不存在");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
			List<ILiveMessage> InteractiveMap = chatInteractiveMap.get(roomId);
			if(InteractiveMap==null){
				InteractiveMap=new ArrayList<ILiveMessage>();
			}
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(roomId);
			if(userMap==null){
				userMap = new Hashtable<String, UserBean>();
			}
			Iterator<String> userIterator = userMap.keySet().iterator();
			ILiveMessage iLiveMessage =  null;
			if(InteractiveMap.size()>0){
				iLiveMessage = InteractiveMap.get(InteractiveMap.size()-1);
			}else{
				iLiveMessage = new ILiveMessage();
			}
			iLiveMessage.setDeleteAll(true);
			iLiveMessage.setRoomType(1);
			while (userIterator.hasNext()) {
				String key = userIterator.next();
				UserBean user = userMap.get(key);
				List<ILiveMessage> userMsgList = user.getMsgList();
				userMsgList.add(iLiveMessage);
				userMap.put(key, user);
			}	
			iLiveMessageMng.deleteInteractiveMapAll(InteractiveMap);
			chatInteractiveMap.remove(roomId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
	/**
	 * 回答
	 * @param response
	 * @param roomId
	 * @param replyContent
	 * @param msgId
	 */
	@RequestMapping(value="/addReplyContent.do",method=RequestMethod.POST)
	public void addILiveMessageReplyContent(HttpServletResponse response,Integer roomId,String replyContent,Long msgId,Long userId){
		JSONObject resultJson = new JSONObject();
		try {
			if(userId==null||roomId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
			ILiveManager liveManager = iLiveManagerMng.selectILiveManagerById(userId);
			if(liveManager==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户不存在");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
			ILiveMessage iliveMessage = iLiveMessageMng.findById(msgId);
			//路径处理
			Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
			List<ILiveMessage> list = quizLiveMap.get(roomId);
			if(list!=null){
				for (ILiveMessage message : list) {
					if(message.getMsgId().equals(msgId)){
						message.setReplyContent(replyContent);
						message.setReplyType(1);
						message.setReplyName(liveManager.getUserName());
					}
				}
			}else{
				list = new ArrayList<ILiveMessage>();
			}
			iliveMessage.setReplyContent(replyContent);
			iliveMessage.setReplyType(1);
			iliveMessage.setUpdate(true);
			iliveMessage.setRoomType(1);
			iliveMessage.setReplyName(liveManager.getUserName());
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(roomId);
			if(userMap==null){
				userMap = new Hashtable<String, UserBean>();
			}
			Iterator<String> userIterator = userMap.keySet().iterator();
			while (userIterator.hasNext()) {
				String key = userIterator.next();
				UserBean user = userMap.get(key);
				List<ILiveMessage> userMsgList = user.getMsgList();
				userMsgList.add(iliveMessage);
				userMap.put(key, user);
			}	
			iLiveMessageMng.update(iliveMessage);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
	/**
	 * 检索回答
	 * @param response
	 * @param roomId
	 * @param replyType
	 */
	@RequestMapping(value="/retrieve.do",method=RequestMethod.GET)
	public void retrieveILiveMessageReplyContent(HttpServletResponse response,Integer roomId,Integer replyType){
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
			Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
			List<ILiveMessage> list = quizLiveMap.get(roomId);
			if(list==null){
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
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
	/**
	 * 是否置顶处理
	 * @param response
	 * @param roomId 直播间ID
	 * @param msgId 消息ID
	 * @param top 置顶状态
	 */
	@RequestMapping(value="/top.do",method=RequestMethod.POST)
	public void topILiveMessage(HttpServletResponse response,Integer roomId,Long msgId,Integer top){
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null||msgId==null||top==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(roomId);
			if(userMap==null){
				userMap = new Hashtable<String, UserBean>();
			}
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			iLiveMessage.setRoomType(1);
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
			List<ILiveMessage> list = chatInteractiveMap.get(roomId);
			for (ILiveMessage message : list) {
				if(message.getMsgId().equals(msgId)){
					message.setTop(top);
				}
			}
			iLiveMessage.setTop(top);
			iLiveMessage.setUpdate(true);
			Iterator<String> userIterator = userMap.keySet().iterator();
			while (userIterator.hasNext()) {
				String key = userIterator.next();
				UserBean user = userMap.get(key);
				List<ILiveMessage> userMsgList = user.getMsgList();
				userMsgList.add(iLiveMessage);
				userMap.put(key, user);
			}	
			iLiveMessageMng.update(iLiveMessage);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 获取直播间所有话题评论
	 * @param roomId 直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="/comments/selectComments.do")
	public void selectILiveComments(Integer roomId, HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(request,response, resultJson.toString());
				return;
			}
			Hashtable<Integer, List<ILiveMessage>> frameLiveMap = ApplicationCache.getFrameLiveMap();
			List<ILiveMessage> list = frameLiveMap.get(roomId);
			if(list==null){
				list = new ArrayList<ILiveMessage>();
			}
			List<CommentsMessageVo> voList = new ArrayList<CommentsMessageVo>();
			Map<Long, List<ILiveMessagePraise>> map = new HashMap<Long, List<ILiveMessagePraise>>();
			
			for (ILiveMessage iLiveMessage : list) {
				List<ILiveComments> commentsList = iLiveCommentsMng.selectILiveCommentsListByMsgId(iLiveMessage.getMsgId(),false);
				if(commentsList.size()>0){
					CommentsMessageVo vo = new CommentsMessageVo();
					vo.setMsgId(iLiveMessage.getMsgId());
					vo.setList(commentsList);
					voList.add(vo);
				}
				List<ILiveMessagePraise> praises = iLiveMessagePraiseMng.selectILiveMessagePraisByMsgId(iLiveMessage.getMsgId());
				map.put(iLiveMessage.getMsgId(), praises);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			Gson gson = new Gson();
			resultJson.put("iLiveCommentsMap", gson.toJson(voList));
			//获取点赞数
			resultJson.put("praiseMap", gson.toJson(map));
		}catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 删除评论
	 * @param commentsId 评论ID
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="/comments/delete.do")
	public void deleteILiveComments(Long commentsId, HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			iLiveCommentsMng.deleteILiveComments(commentsId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "删除评论成功");
		}catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "删除评论失败");
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 通过审核
	 * @param msgId 消息ID
	 * @param roomId 直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="/updateChecked.do")
	public void updateChecked(Long msgId,Integer roomId, HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(request,response, resultJson.toString());
				return;
			}
			ILiveMessage message = iLiveMessageMng.updateCheckById(msgId, true);
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
			List<ILiveMessage> list = chatInteractiveMap.get(roomId);
			list.add(message);
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(roomId);
			Iterator<String> userIterator = userMap.keySet().iterator();
			while (userIterator.hasNext()) {
				String key = userIterator.next();
				UserBean user = userMap.get(key);
				List<ILiveMessage> userMsgList = user.getMsgList();
				message.setUpdate(true);
				message.setRoomType(1);
				userMsgList.add(message);
				userMap.put(key, user);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "审核成功");
		}catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "审核失败");
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
}
