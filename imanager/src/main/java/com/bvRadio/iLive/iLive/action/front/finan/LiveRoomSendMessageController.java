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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.entity.ILiveComments;
import com.bvRadio.iLive.iLive.entity.ILiveEstoppel;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveMessagePraise;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.CommentsMessageVo;
import com.bvRadio.iLive.iLive.manager.ILiveCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessagePraiseMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.google.gson.Gson;

/**
 * 发送数据
 * 
 * @author YanXL
 *
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
	private ILiveMessagePraiseMng iLiveMessagePraiseMng;//点赞

	private static final Logger log = LoggerFactory.getLogger(LiveRoomSendMessageController.class);
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String HTTP_PROTOACAL = "http://";

	/**
	 * 获取直播间信息
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping("/selectRoom.jspx")
	public void sendMessage(Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
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
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			Gson gson = new Gson();
			resultJson.put("iLiveLiveRoom", gson.toJson(iLiveLiveRoom));
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 获取页面table页选择
	 * @param roomId 直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping("/selectPageDecorate.jspx")
	public void sendPageDecorateMessage(Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
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
	 * @param response
	 */
	@RequestMapping("/sendMessage.jspx")
	public void sendMessage(Long liveEventId, Integer roomId, Long userId, String content, Integer liveMsgType,
			HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null || userId == null || content == null || liveMsgType == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "参数错误");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			content = URLDecoder.decode(content, "utf-8");
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
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> map = userListMap.get(roomId);
			if (map == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间信息不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			UserBean userBean = map.get(String.valueOf(userId));
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
			// 消息源正文
			content = ExpressionUtil.replaceTitleToKey(content);
			iLiveMessage.setMsgOrginContent(content);
			// 消息正文
			iLiveMessage.setMsgContent(content);
			// 消息类型
			iLiveMessage.setMsgType(Constants.MSG_TYPE_TXT);
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
			iLiveMessage.setUpdate(false);
			iLiveMessage = iLiveMessageMng.save(iLiveMessage);
			// =================================================未审核===================================================
			if (liveMsgType == 2) {
				// 互动
				Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache
						.getChatInteractiveMapNO();
				List<ILiveMessage> list = chatInteractiveMapNO.get(roomId);
				if (list == null) {
					list = new ArrayList<ILiveMessage>();
				}
				list.add(iLiveMessage);
				chatInteractiveMapNO.put(roomId, list);
			} else if (liveMsgType == 1) {
				// 图文
				Hashtable<Integer, List<ILiveMessage>> frameLiveMap = ApplicationCache.getFrameLiveMap();
				List<ILiveMessage> list = frameLiveMap.get(roomId);
				if (list == null) {
					list = new ArrayList<ILiveMessage>();
				}
				list.add(iLiveMessage);
				frameLiveMap.put(roomId, list);
			} else if (liveMsgType == 3) {
				// 问答
				Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
				List<ILiveMessage> list = quizLiveMap.get(roomId);
				if (list == null) {
					list = new ArrayList<ILiveMessage>();
				}
				list.add(iLiveMessage);
				quizLiveMap.put(roomId, list);
			}

			// 用户缓存
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
			String msgContent = iLiveMessage.getMsgContent();
			msgContent = ExpressionUtil.replaceKeyToImg(msgContent);
			iLiveMessage.setMsgContent(msgContent);
			resultJson.put("iLiveMessage", iLiveMessage.putMessageInJson(null));
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "消息发送失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 评论消息
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
	public void saveILiveComments(Long userId, Long msgId, String comments, HttpServletResponse response,
			HttpServletRequest request) {
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
			comments = URLDecoder.decode(comments, "utf-8");
			ILiveComments liveComments = new ILiveComments();
			comments = ExpressionUtil.replaceTitleToKey(comments);
			liveComments.setComments(comments);
			liveComments.setCommentsName(iLiveManager.getNailName());
			liveComments.setCreateTime(Timestamp.valueOf(format.format(new Date())));
			liveComments.setIsChecked(true);// 审核通过
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
	 * 获取评论内容
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param response
	 * @param request
	 */
	@RequestMapping("/comments/selectList.jspx")
	public void selectILiveComments(Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
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
				List<ILiveComments> commentsList = iLiveCommentsMng.selectILiveCommentsByMsgId(iLiveMessage.getMsgId(),
						true, false);
				if (commentsList.size() > 0) {
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
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	//点赞===============================================================================================================	
		@RequestMapping(value="/praise/savePraise.jspx")
		public void saveILiveMessagePraise(Long msgId,Long userId, HttpServletResponse response,HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			try {
				if(msgId==null||userId==null){
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "数据错误");
					ResponseUtils.renderJson(request,response, resultJson.toString());
					return;
				}
				ILiveMessagePraise iLiveMessagePraise = iLiveMessagePraiseMng.selectILiveMessagePraise(msgId, userId);
				if(iLiveMessagePraise!=null){
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "成功点赞");
					resultJson.put("praiseNumber", 0);
				}else{
					iLiveMessagePraise = new ILiveMessagePraise();
					iLiveMessagePraise.setMsgId(msgId);
					iLiveMessagePraise.setUserId(userId);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					iLiveMessagePraise.setCreateTime(Timestamp.valueOf(format.format(new Date())));
					iLiveMessagePraiseMng.addILiveMessagePraise(iLiveMessagePraise);
					ILiveMessage iLiveMessage = iLiveMessageMng.findById(msgId);
					Long number = iLiveMessage.getPraiseNumber();
					if(number==null){
						number=0l;
					}
					Long praiseNumber =number +1;
					iLiveMessage.setPraiseNumber(praiseNumber);
					iLiveMessageMng.update(iLiveMessage);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "成功点赞");
					resultJson.put("praiseNumber", praiseNumber);
				}
			}catch (Exception e) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "失败");
				System.out.println(e.toString());
			}
			ResponseUtils.renderJson(request,response, resultJson.toString());
		}
}
