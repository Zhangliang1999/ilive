package com.bvRadio.iLive.iLive.action.front.finan;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

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

//import org.apache.naming.java.javaURLContextFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.RequestUtils;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.dao.ILiveRoomStaticsDao;
import com.bvRadio.iLive.iLive.entity.ILiveComments;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveMessagePraise;
import com.bvRadio.iLive.iLive.entity.ILiveRollingAdvertising;
import com.bvRadio.iLive.iLive.entity.ILiveRoomStatics;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.CommentsMessageVo;
import com.bvRadio.iLive.iLive.entity.vo.ILiveEventVo;
import com.bvRadio.iLive.iLive.manager.ILiveCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEstoppelMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessagePraiseMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveRollingAdvertisingMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomRegisterService;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.google.gson.Gson;

/**
 * 发送数据
 * 
 * @author YanXL
 */
@Controller
public class LiveRoomSendMessageController {
	@Autowired
	private ILiveMessageMng iLiveMessageMng;// 消息管理

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间

	@Autowired
	private ILiveManagerMng iLiveManagerMng;// 用户

	@Autowired
	private ILiveCommentsMng iLiveCommentsMng;// 评论

	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;

	@Autowired
	private ILivePageDecorateMng pageDecorateMng;
	@Autowired
	private ILiveMessagePraiseMng iLiveMessagePraiseMng;// 点赞

	@Autowired
	private ILiveEventMng iLiveEventMng;

	@Autowired
	private SentitivewordFilterMng sentitivewordFilterMng;

	private static final Logger log = LoggerFactory.getLogger(LiveRoomSendMessageController.class);
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private ILiveEstoppelMng iLiveEstoppelMng;
	@Autowired
	private ILiveRoomStaticsDao roomStaticsMng;

	private static final String HTTP_PROTOACAL = "http://";

	/**
	 * 获取当前直播间人数
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping("/online/number.jspx")
	public void onlineNumber(Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间未知");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			Hashtable<Integer, ILiveEvent> onlineNumberMap = ApplicationCache.getOnlineNumber();
			
			ILiveEvent iLiveEvent = null;
			
				ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
				if (iLiveLiveRoom != null) {
					iLiveEvent = iLiveLiveRoom.getLiveEvent();
					onlineNumberMap.remove(roomId);
					onlineNumberMap.put(roomId, iLiveEvent);
				}
			
			
			Integer baseNum = 0;
			Integer multiple = 1;
			
			if(iLiveEvent.getOpenDataBeautifySwitch()==1){
				baseNum = iLiveEvent.getBaseNum();
				if (baseNum == null  ) {
					baseNum = 0;
				}
				multiple = iLiveEvent.getMultiple();
				if (multiple == null || multiple.intValue() == 0 ) {
					multiple = 1;
				}
			}
			ILiveRoomStatics roomStaticsByEventId = roomStaticsMng.getRoomStaticsByEventId(iLiveEvent.getLiveEventId());
			System.out.println(iLiveEvent.getLiveEventId()+"数据库取出数据人数-------------------"+roomStaticsByEventId.getShowNum());
			Long numId=0L;
			if(JedisUtils.exists("onlineNumber:"+iLiveEvent.getLiveEventId())) {
				
				try {
					numId=Long.parseLong(JedisUtils.get("onlineNumber:"+iLiveEvent.getLiveEventId()));
					if(numId>roomStaticsByEventId.getShowNum()) {
						roomStaticsMng.updateRoomStatic(iLiveEvent.getLiveEventId(), numId);
					}else {
						JedisUtils.del("onlineNumber:"+iLiveEvent.getLiveEventId());
						JedisUtils.set("onlineNumber:"+iLiveEvent.getLiveEventId(), numId+"", 300);
					}
				} catch (Exception e) {
					System.out.println(iLiveEvent.getLiveEventId()+"redis出错从数据库取出数据人数-------------------"+roomStaticsByEventId.getShowNum());
					numId=roomStaticsByEventId.getShowNum();
				}
				
			}else {
				System.out.println(iLiveEvent.getLiveEventId()+"redis不存在从数据库取出数据人数-------------------"+roomStaticsByEventId.getShowNum());
				Long showNum = 0L;
				if (roomStaticsByEventId == null) {
				} else {
					showNum = roomStaticsByEventId.getShowNum();
				}
				numId = showNum;
				JedisUtils.set("onlineNumber:"+iLiveEvent.getLiveEventId(), numId+"", 300);
			}
			System.out.println(iLiveEvent.getLiveEventId()+"-------------------"+numId);
			if (numId == null) {
				numId = 0L;
			}
			long snum = (baseNum + (numId * multiple));
			System.out.println(roomId + ":snum---------->" + snum);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取人数成功");
			resultJson.put("data", "{\"number\":\"" + snum + "\"}");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取在线人数失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 获取直播间信息
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping("/selectRoom.jspx")
	public void selectRoom(Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom iLiveLiveRoom=null;
			
			if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
				iLiveLiveRoom= SerializeUtil.getObjectRoom(roomId);
			}else {
				iLiveLiveRoom=iLiveLiveRoomMng.getIliveRoom(roomId);
				JedisUtils.setByte(("roomInfo:"+roomId).getBytes(), SerializeUtil.serialize(iLiveLiveRoom), 300);
			}
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = accessMethodMng
						.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
				String pushStreamAddr = HTTP_PROTOACAL + accessMethodBySeverGroupId.getHttp_address() + ":"
						+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
				iLiveLiveRoom.setHlsAddr(pushStreamAddr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (iLiveLiveRoom!=null) {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取信息成功");
				Gson gson = new Gson();
				resultJson.put("iLiveLiveRoom", gson.toJson(iLiveLiveRoom));
			}else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "该直播间不存在");
				resultJson.put("iLiveLiveRoom", new JSONObject());
			}
			
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 获取页面table页选择
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping("/selectPageDecorate.jspx")
	public void sendPageDecorateMessage(Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		
		try {
		if(JedisUtils.exists("selectPageDecorate:"+roomId)) {
			
			ResponseUtils.renderJson(request, response, JedisUtils.get("selectPageDecorate:"+roomId));
			return;
		}else {
			
				ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
				List<PageDecorate> list = pageDecorateMng.getList(iLiveLiveRoom.getLiveEvent().getLiveEventId());
				List<PageDecorate> newPageDecorate = new ArrayList<>();
				boolean pageType2 = false;
				boolean pageType3 = false;
				for (PageDecorate page : list) {
					Integer menuType = page.getMenuType();
					if (menuType == 3) {
						pageType3 = true;
					}
					if (menuType == 2) {
						pageType2 = true;
					}
				}
				if (pageType2 && pageType3) {
					for (PageDecorate page : list) {
						Integer menuType = page.getMenuType();
						if (menuType == 3) {
							continue;
						}
						newPageDecorate.add(page);
					}
				} else if (!pageType2 && pageType3) {
					for (PageDecorate page : list) {
						Integer menuType = page.getMenuType();
						if (menuType == 3) {
							page.setMenuName("直播聊天");
							page.setMenuType(2);
						}
						newPageDecorate.add(page);
					}
				} else {
					for (PageDecorate page : list) {
						newPageDecorate.add(page);
					}
				}
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取信息成功");
				Gson gson = new Gson();
				resultJson.put("pageDecorates", gson.toJson(newPageDecorate));
				JedisUtils.set("selectPageDecorate:"+roomId, resultJson.toString(), 300);
				
		}
		
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 发送消息
	 * 
	 * @param terminalType
	 * 
	 * @param liveEventId
	 *            场次ID
	 * @param roomId
	 *            直播间id
	 * @param userId
	 *            用户ID
	 * @param content
	 *            正文内容
	 * @param liveMsgType
	 *            消息类型
	 * @param msgType
	 *            消息体类型 1 文字 2 图片 3 视频
	 * @param response
	 */
	@RequestMapping("/sendMessage.jspx")
	public void sendMessage(String terminalType, Integer msgType, Long liveEventId, Integer roomId, Long userId,
			String content,String nickname, Integer liveMsgType, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null || userId == null || content == null || liveMsgType == null || msgType == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveLiveRoom liveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			if(liveMsgType==2){
				//聊天
				if(liveLiveRoom==null){
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播间获取失败");
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}else{
//					ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(liveLiveRoom.getEnterpriseId());
//					Integer certStatus = iLiveEnterprise.getCertStatus();
//					boolean b = EnterpriseUtil.selectIfEn(liveLiveRoom.getEnterpriseId(), EnterpriseCache.ENTERPRISE_FUNCTION_ChatBombScreen,certStatus);
//					if(b){
//						resultJson.put("code", ERROR_STATUS);
//						resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_ChatBombScreen_Content);
//						ResponseUtils.renderJson(request, response, resultJson.toString());
//						return;
//					}
				}
			}else if(liveMsgType==3){
				//问答
				if(liveLiveRoom==null){
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播间获取失败");
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}else{
//					ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(liveLiveRoom.getEnterpriseId());
//					Integer certStatus = iLiveEnterprise.getCertStatus();
//					boolean b = EnterpriseUtil.selectIfEn(liveLiveRoom.getEnterpriseId(), EnterpriseCache.ENTERPRISE_FUNCTION_Question,certStatus);
//					if(b){
//						resultJson.put("code", ERROR_STATUS);
//						resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_Question_Content);
//						ResponseUtils.renderJson(request, response, resultJson.toString());
//						return;
//					}
				}
			}else if (liveMsgType == 1) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "消息类型错误、你发送的是话题");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveEvent liveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
			if (null == liveEvent || liveEvent.getEstoppelType() == 0) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "你已被禁言");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			boolean b = iLiveEstoppelMng.getILiveEstoppelYesOrNo(roomId, userId);
			if (b) {
				// System.out.println("你被禁言：userId：" + userId + " 直播间：roomId：" +
				// roomId);
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "你已被禁言");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			content = URLDecoder.decode(content, "utf-8");
			content = content.replaceAll("[^\\u0000-\\uFFFF]", "[表情]");
			if(nickname!=null&&!"".equals(nickname)) {
				nickname =URLDecoder.decode(nickname, "utf-8");
			}
			
			System.out.println("content:  " + content);
			System.out.println("nickname:  " + nickname);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> map = userListMap.get(roomId);
			if (map == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间信息不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			UserBean appUser = ILiveUtils.getAppUser(request);
			if (appUser == null) {
				ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
				if (iLiveManager == null) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户信息不存在");
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				} else {
					appUser = new UserBean();
					appUser.setUsername(iLiveManager.getNailName());
					appUser.setUserId(String.valueOf(userId));
					appUser.setUserThumbImg(iLiveManager.getUserImg());
					appUser.setLevel(iLiveManager.getLevel());
					appUser.setNickname(iLiveManager.getNailName());
				}
			}

			ILiveMessage iLiveMessage = new ILiveMessage();
			// 直播场次ID
			ILiveEvent iLiveEvent = new ILiveEvent();
			iLiveEvent.setLiveEventId(liveEventId);
			iLiveMessage.setLive(iLiveEvent);
			// 直播间ID
			iLiveMessage.setLiveRoomId(roomId);
			// 消息IP iLiveIpAddress;
			if(nickname==null||"".equals(nickname)) {
				// 发送人名称
				iLiveMessage.setSenderName(appUser.getNickname());
			}else {
				// 发送人名称
				iLiveMessage.setSenderName(nickname);
			}
			
			// 用户ID
			iLiveMessage.setSenderId(Long.valueOf(appUser.getUserId()));
			// 发送人头像
			iLiveMessage.setSenderImage(appUser.getUserThumbImg());
			// 发送人级别
			iLiveMessage.setSenderLevel(appUser.getLevel());
			// 消息源正文
			content = ExpressionUtil.replaceTitleToKey(content);
			iLiveMessage.setMsgOrginContent(content);
			// 消息正文
			iLiveMessage.setMsgContent(content);
			// 消息类型
			iLiveMessage.setMsgType(msgType);
			// 创建时间
			iLiveMessage.setCreateTime(Timestamp.valueOf(format.format(new Date())));
			// 状态
			iLiveMessage.setStatus(0);
			// 删除标识
			iLiveMessage.setDeleted(false);
			// 审核状态
			if (liveMsgType == 2) {
				iLiveMessage.setChecked(false);
			} else {
				iLiveMessage.setChecked(true);
			}
			// 消息类型
			iLiveMessage.setLiveMsgType(liveMsgType);
			if (liveMsgType == 3) {
				iLiveMessage.setReplyType(0);
			}
			// 字体颜色
			iLiveMessage.setIMEI(null);
			iLiveMessage.setWidth(null);
			iLiveMessage.setHeight(null);
			iLiveMessage.setServiceType(1);
			iLiveMessage.setSenderType(0);
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
			iLiveMessage.setUpdate(false);
			iLiveMessage.setEmptyAll(false);
			iLiveMessage = iLiveMessageMng.save(iLiveMessage);
			// =================================================未审核===================================================
			JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
			if (liveMsgType == 2) {
				// 互动
				if("open".equals(ConfigUtils.get("redis_service"))) {
					
					JedisUtils.listAdd("msgIdListCheckNo"+roomId, iLiveMessage.getMsgId()+"");
					
				}else {
					Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache
							.getChatInteractiveMapNO();
					List<ILiveMessage> list = chatInteractiveMapNO.get(roomId);
					if (list == null) {
						list = new ArrayList<ILiveMessage>();
					}
					list.add(iLiveMessage);
					chatInteractiveMapNO.put(roomId, list);
				}
				
				
				
				// 评论通知
				try {
					String ipAddr = RequestUtils.getIpAddr(request);
					ILiveUserViewStatics.INSTANCE.commentRoom(String.valueOf(userId), roomId, liveEventId, null,
							ipAddr, terminalType);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (liveMsgType == 3) {
				// 问答
				if("open".equals(ConfigUtils.get("redis_service"))) {
					
					
					JedisUtils.listAdd("msgIdListCheck"+roomId, iLiveMessage.getMsgId()+"");
					
				}else {
					Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
					List<ILiveMessage> list = quizLiveMap.get(roomId);
					if (list == null) {
						list = new ArrayList<ILiveMessage>();
					}
					list.add(iLiveMessage);
					quizLiveMap.put(roomId, list);
				}
				
				
			}
			if("open".equals(ConfigUtils.get("redis_service"))) {
				Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
				if(userIdList!=null&&userIdList.size()!=0) {
					for(String userIds:userIdList) {
						if(!userIds.split("_")[0].equals("0")&&iLiveManagerMng.getILiveManager(Long.parseLong(userIds.split("_")[0]))!=null) {
							
							if(userIds.split("_")[0].equals(userId+"")||iLiveManagerMng.getILiveManager(Long.parseLong(userIds.split("_")[0])).getEnterPrise().getEnterpriseId().equals(liveLiveRoom.getEnterpriseId())) {
								//System.out.println("room企业id++++++++++++++++++++"+liveLiveRoom.getEnterpriseId()+"用户企业id+++++++++++++++++++++++++++++++++++"+iLiveManagerMng.getILiveManager(Long.parseLong(userIds.split("_")[0])).getEnterPrise().getEnterpriseId());
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
					}
				}
				
			}else{
				// 用户缓存
				Iterator<String> userIterator = map.keySet().iterator();
				while (userIterator.hasNext()) {
					
					String key = userIterator.next();
					UserBean user = map.get(key);
					if(user.getUserId().equals(userId+"")||user.getUserType()==1) {
						
						
							List<ILiveMessage> userMsgList = user.getMsgList();
							if (userMsgList == null) {
								userMsgList = new ArrayList<ILiveMessage>();
							}
							userMsgList.add(iLiveMessage);
							user.setMsgList(userMsgList);
						
					}
				}
			}
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			// System.out.println("对象复制");
			resultJson.put("iLiveMessage", iLiveMessage.putMessageInJson(null));
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "消息发送失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	// 操作============================================================================
	/**
	 * 是否启动自动审核控制
	 * 
	 * @param response
	 * @param roomId
	 *            直播间ID
	 * @param autoCheckSecond
	 */
	@RequestMapping(value = "/update/auto.jspx")
	public void updateILiveEventByAutoCheckSecond(HttpServletResponse response, Integer autoCheckSecond, Integer roomId,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null) {
				resultJson.put("status", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveLiveRoom room = iLiveLiveRoomMng.findById(roomId);
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(room.getLiveEvent().getLiveEventId());
			iLiveEvent.setAutoCheckSecond(autoCheckSecond);
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
			if (userMap != null) {
				Iterator<String> userIterator = userMap.keySet().iterator();
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					UserBean user = userMap.get(key);
					List<ILiveMessage> userMsgList = user.getMsgList();
					if (userMsgList == null) {
						userMsgList = new ArrayList<ILiveMessage>();
					}
					ILiveMessage message = new ILiveMessage();
					message.setRoomType(0);
					ILiveEventVo iLiveEventVo = new ILiveEventVo();
					iLiveEventVo.setCheckedTime(autoCheckSecond);
					iLiveEventVo.setEstoppleType(iLiveEvent.getEstoppelType());
					iLiveEventVo.setLiveStatus(iLiveEvent.getLiveStatus());
					iLiveEventVo.setPlayType(iLiveEvent.getPlayType());
					message.setiLiveEventVo(iLiveEventVo);
					message.setLiveRoomId(roomId);
					userMsgList.add(message);
					user.setMsgList(userMsgList);
					userMap.put(key, user);
				}
			}
			Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
			roomListMap.put(roomId, autoCheckSecond);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 全局禁言
	 * 
	 * @param response
	 * @param estoppelType
	 *            0开启 1关闭
	 * @param roomId
	 *            直播间ID
	 */
	@RequestMapping(value = "/update/estoppeltype.jspx", method = RequestMethod.POST)
	public void updateILiveEventByEstoppelType(HttpServletResponse response, Integer estoppelType, Integer roomId,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			JedisUtils.delObject("roomInfo:"+roomId);
		}
		try {
			if (roomId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveLiveRoom room = iLiveLiveRoomMng.findById(roomId);
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(room.getLiveEvent().getLiveEventId());
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
			List<ILiveMessage> list = chatInteractiveMap.get(roomId);
			if (list == null) {
				list = new ArrayList<ILiveMessage>();
			}
			ILiveMessage iLiveMessage = null;
			if (list.size() > 0) {
				iLiveMessage = list.get(list.size() - 1);
			} else {
				iLiveMessage = new ILiveMessage();
			}
			if (estoppelType == 0) {
				// 开启
				iLiveMessage.setOpType(11);
			} else {
				// 关闭
				iLiveMessage.setOpType(10);
			}
			if("open".equals(ConfigUtils.get("redis_service"))) {
				Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
				if(userIdList!=null&&userIdList.size()!=0) {
					for(String userId:userIdList) {
						ILiveMessage message = new ILiveMessage();
						message.setRoomType(0);
						ILiveEventVo iLiveEventVo = new ILiveEventVo();
						iLiveEventVo.setCheckedTime(iLiveEvent.getAutoCheckSecond());
						iLiveEventVo.setEstoppleType(estoppelType);
						iLiveEventVo.setLiveStatus(iLiveEvent.getLiveStatus());
						iLiveEventVo.setPlayType(iLiveEvent.getPlayType());
						message.setiLiveEventVo(iLiveEventVo);
						message.setLiveRoomId(roomId);
						message.setMsgId(Long.parseLong((-roomId)+""));
						JedisUtils.delObject("msg:"+message.getMsgId());
						JedisUtils.setByte(("msg:"+message.getMsgId()).getBytes(), SerializeUtil.serialize(message), 0);
						
						boolean flag=true;
						while (flag) {
							String requestionIdString=UUID.randomUUID().toString();
							if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
								JedisUtils.listAdd(roomId+":"+userId, message.getMsgId()+"");
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
				iLiveEvent.setEstoppelType(estoppelType);
				iLiveEventMng.updateILiveEvent(iLiveEvent);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "成功");
			}else {
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
				if (userMap != null) {
					Iterator<String> userIterator = userMap.keySet().iterator();
					while (userIterator.hasNext()) {
						String key = userIterator.next();
						UserBean user = userMap.get(key);
						List<ILiveMessage> userMsgList = user.getMsgList();
						if (userMsgList == null) {
							userMsgList = new ArrayList<ILiveMessage>();
						}
						ILiveMessage message = new ILiveMessage();
						message.setRoomType(0);
						ILiveEventVo iLiveEventVo = new ILiveEventVo();
						iLiveEventVo.setCheckedTime(iLiveEvent.getAutoCheckSecond());
						iLiveEventVo.setEstoppleType(estoppelType);
						iLiveEventVo.setLiveStatus(iLiveEvent.getLiveStatus());
						iLiveEventVo.setPlayType(iLiveEvent.getPlayType());
						message.setiLiveEventVo(iLiveEventVo);
						message.setLiveRoomId(roomId);
						userMsgList.add(message);
						user.setMsgList(userMsgList);
						userMap.put(key, user);
					}
				}
				iLiveEvent.setEstoppelType(estoppelType);
				iLiveEventMng.updateILiveEvent(iLiveEvent);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "成功");
			}
			
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	// =============================================话题=====
	/**
	 * 获取直播间 话题
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/message/selectMessageLIVE.jspx")
	public void selectMessageLIVE(Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
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
			// 排序
			for (int i = 0; i < list.size(); i++) {
				for (int j = 1; j < list.size() - i; j++) {
					if ((list.get(j - 1).getMsgId()).compareTo(list.get(j).getMsgId()) < 0) {
						ILiveMessage iLiveMessage = list.get(j - 1);
						list.set((j - 1), list.get(j));
						list.set(j, iLiveMessage);
					}
				}
			}
			for (ILiveMessage iLiveMessage : list) {
				JSONObject putMessageInJson = iLiveMessage.putMessageInJson(null);
				jsonObjects.add(putMessageInJson);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取成功");
			resultJson.put("data", jsonObjects);
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
			ILiveLiveRoom room =iLiveLiveRoomMng.getIliveRoom(list.get(0).getLiveRoomId());
			ILiveEnterprise enterprise=iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
			resultJson.put("senderImg", enterprise.getEnterpriseImg());
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 评论消息
	 * 
	 * @param terminalType
	 * 
	 * @param userId
	 *            评论用户
	 * @param msgId
	 *            话题ID
	 * @param comments
	 *            评论内容
	 * @param response
	 * @param request
	 */
	@RequestMapping("/saveComments.jspx")
	public void saveILiveComments(String terminalType, Long userId, Long msgId, String comments,
			HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (userId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户ID传递失败");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
			if (iLiveManager == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
			if (iLiveMessage == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "评论话题不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			comments = URLDecoder.decode(comments, "utf-8");
			comments = comments.replaceAll("[^\\u0000-\\uFFFF]", "[表情]");
			ILiveComments liveComments = new ILiveComments();
			comments = ExpressionUtil.replaceTitleToKey(comments);
			liveComments.setComments(comments);
			liveComments.setUserId(userId);
			liveComments.setCommentsName(iLiveManager.getNailName());
			liveComments.setCreateTime(Timestamp.valueOf(format.format(new Date())));
			ILiveLiveRoom room = iLiveLiveRoomMng.findById(iLiveMessage.getLiveRoomId());
			Long liveEventId = iLiveMessage.getLive().getLiveEventId();
			ILiveEvent live = null;
			if (room == null) {
				live = iLiveEventMng.selectILiveEventByID(liveEventId);
			} else {
				live = room.getLiveEvent();
			}

			Integer commentsAudit = live.getCommentsAudit();
			commentsAudit = commentsAudit == null ? 1 : commentsAudit;
			if (commentsAudit == 1) {
				liveComments.setIsChecked(false);
			} else {
				liveComments.setIsChecked(true);
			}
			resultJson.put("commentsAudit", commentsAudit);
			liveComments.setIsDelete(false);
			liveComments.setMsgId(msgId);
			iLiveCommentsMng.saveILiveComments(liveComments);
			Gson gson = new Gson();
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			liveComments.setComments(ExpressionUtil.replaceKeyToImg(comments));
			resultJson.put("comments", gson.toJson(liveComments));
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 获取话题评论内容
	 * 
	 * @param userId
	 *            用户ID
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping("/comments/selectList.jspx")
	public void selectILiveComments(Long userId, Integer roomId, HttpServletResponse response,
			HttpServletRequest request) {
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
			
				for (ILiveMessage iLiveMessage : list) {
					List<ILiveComments> commentsList = iLiveCommentsMng.selectILiveCommentsByMsgId(iLiveMessage.getMsgId(),
							true, false);
					if (userId != null) {
						List<ILiveComments> iLiveComments = iLiveCommentsMng
								.selectILiveCommentsByMsgId(iLiveMessage.getMsgId(), false, false);
						for (ILiveComments iLiveComments2 : iLiveComments) {
							if (iLiveComments2.getUserId().equals(userId)) {
								ILiveManager manager=iLiveManagerMng.selectILiveManagerById(userId);
								iLiveComments2.setUserPhotoUrl(manager.getUserImg());
								commentsList.add(iLiveComments2);
							}
						}
					}
					for (ILiveComments iLiveComments : commentsList) {
						String comments = iLiveComments.getComments();
						comments = sentitivewordFilterMng.replaceSensitiveWord(comments);
						iLiveComments.setComments(ExpressionUtil.replaceKeyToImg(comments));
					}
					if (commentsList.size() > 0) {
						CommentsMessageVo vo = new CommentsMessageVo();
						vo.setMsgId(iLiveMessage.getMsgId());
						vo.setList(commentsList);
						voList.add(vo);
					}
					List<ILiveMessagePraise> praises = iLiveMessagePraiseMng
							.selectILiveMessagePraisByMsgId(iLiveMessage.getMsgId());
					map.put(iLiveMessage.getMsgId(), praises);
				}
			
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			Gson gson = new Gson();
			resultJson.put("iLiveCommentsMap", gson.toJson(voList));
			ConcurrentHashMap<String, UserBean> userMap = ApplicationCache.getUserListMap().get(roomId);
			if (userMap == null) {
				userMap = new ConcurrentHashMap<String, UserBean>();
			}
			resultJson.put("number", userMap.size());
			// 获取点赞数
			resultJson.put("praiseMap", gson.toJson(map));
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	// 点赞===============================================================================================================
	@RequestMapping(value = "/praise/savePraise.jspx")
	public void saveILiveMessagePraise(Long msgId, Long userId,HttpServletResponse response,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (msgId == null || userId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "数据错误");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveManager user =iLiveManagerMng.getILiveManager(userId);
			ILiveMessagePraise iLiveMessagePraise = iLiveMessagePraiseMng.selectILiveMessagePraise(msgId, userId);
			if (iLiveMessagePraise != null) {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "成功点赞");
				resultJson.put("praiseNumber", 0);
			} else {
				iLiveMessagePraise = new ILiveMessagePraise();
				iLiveMessagePraise.setMsgId(msgId);
				iLiveMessagePraise.setUserId(userId);
				if(user.getNailName()!=null&&!"".equals(user.getNailName())) {
					iLiveMessagePraise.setUserName(user.getNailName());
				}else {
					iLiveMessagePraise.setUserName(user.getUserName());
				}
				iLiveMessagePraise.setCreateTime(Timestamp.valueOf(format.format(new Date())));
				iLiveMessagePraiseMng.addILiveMessagePraise(iLiveMessagePraise);
				ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
				Long number = iLiveMessage.getPraiseNumber();
				if (number == null) {
					number = 0l;
				}
				Long praiseNumber = number + 1;
				iLiveMessage.setPraiseNumber(praiseNumber);
				iLiveMessageMng.update(iLiveMessage);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "成功点赞");
				resultJson.put("praiseNumber", praiseNumber);
				resultJson.put("praiseName", user.getUserName());
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			// System.out.println(e.toString());
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
	public void selectILiveCommentsByMsgId(Long userId, Long msgId, HttpServletResponse response,
			HttpServletRequest request) {
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
			List<ILiveComments> commentsList = iLiveCommentsMng.selectILiveCommentsByMsgId(msgId, true, false);
			if (userId != null && userId != 0) {
				List<ILiveComments> iLiveComments = iLiveCommentsMng.selectILiveCommentsByMsgId(iLiveMessage.getMsgId(),
						false, false);
				for (ILiveComments iLiveComments2 : iLiveComments) {
					if (iLiveComments2.getUserId().equals(userId)) {
						commentsList.add(iLiveComments2);
					}
				}
			}
			for (ILiveComments iLiveComments : commentsList) {
				String comments = iLiveComments.getComments();
				comments = sentitivewordFilterMng.replaceSensitiveWord(comments);
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
	
	@Autowired
	private ILiveRollingAdvertisingMng iLiveRollingAdvertisingMng;
	/**
	 * 获取滚动消息
	 * @param roomId 直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/selectAdvertising.jspx" , method=RequestMethod.GET)
	public void selectILiveRollingAdvertising(Integer roomId, HttpServletResponse response, HttpServletRequest request){
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "信息错误");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveRollingAdvertising iLiveRollingAdvertising =null;
			if(JedisUtils.exists(("iLiveRolling:"+roomId).getBytes())) {
				iLiveRollingAdvertising = SerializeUtil.getObjectRollingAdvertising(roomId);
				
			}else {
				
				iLiveRollingAdvertising = iLiveRollingAdvertisingMng.selectILiveRollingAdvertising(roomId);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			resultJson.put("data", iLiveRollingAdvertising.putMessageInJson(null));
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	@Autowired
	private ILiveEnterpriseFansMng iLiveEnterpriseFansMng;
	@Autowired
	private ILiveRoomRegisterService registerService; // 签到、点赞
	/**
	 * 获取文件企业信息
	 * @param userId 用户ID
	 * @param fileId 文件ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/file/enterprise.jspx",method=RequestMethod.GET)
	public void selectILiveAppEnterpriseFileId(String terminalType , Long userId,Long fileId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			if (fileId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "文件获取失败");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
			int enterpriseId = new Long(iLiveMediaFile.getEnterpriseId()).intValue();
			ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			if(userId==0){
				iLiveEnterPrise.setConcernStatus(0);
				resultJson.put("isReg", 0);
			}else{
				boolean exist = iLiveEnterpriseFansMng.isExist(enterpriseId,userId);
				Integer concernStatus = 0;
				if (exist) {
					concernStatus = 1;
				}
				iLiveEnterPrise.setConcernStatus(concernStatus);
				// 查询是否已点赞
				boolean isRoomReg = registerService.queryMediaRegister(String.valueOf(userId), fileId);
				if (isRoomReg) {
					resultJson.put("isReg", 1);
				} else {
					resultJson.put("isReg", 0);
				}
			}
			int fansNum = iLiveEnterpriseFansMng.getFansNum(enterpriseId);
			iLiveEnterPrise.setConcernTotal(fansNum);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("data", iLiveEnterPrise.toSimpleJsonObject());
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取企业信息失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	
	/**
	 * web获取直播间互动
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/message/getMessageOfInteractForWeb.jspx")
	public void getMessageOfInteractForWeb(Integer roomId, Long startId, Integer size, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
			if (roomId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			if (null == iLiveLiveRoom) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			if (null == size) {
				size = 40;
			}
			List<ILiveMessage> list = iLiveMessageMng.getListForWeb(roomId, Constants.LIVE_MSG_TYPE_INTERACT, null,
					false, true, startId, size);
			if (list == null) {
				list = new ArrayList<ILiveMessage>();
			}
			for (ILiveMessage iLiveMessage : list) {
				JSONObject putMessageInJson = iLiveMessage.putMessageInJson(null);
				jsonObjects.add(putMessageInJson);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取成功");
			resultJson.put("data", jsonObjects);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
}
