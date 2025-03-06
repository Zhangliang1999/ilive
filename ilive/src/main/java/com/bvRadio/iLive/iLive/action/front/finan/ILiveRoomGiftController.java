package com.bvRadio.iLive.iLive.action.front.finan;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveGift;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveUserGift;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveGiftMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveUserGiftMng;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

/**
 * 直播间礼物接口
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="gift")
public class ILiveRoomGiftController {
	
	@Autowired
	private ILiveUserGiftMng iLiveUserGiftMng;//收取礼物
	@Autowired
	private ILiveGiftMng iLiveGiftMng;//礼物
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;//直播间
	@Autowired
	private ILiveManagerMng iLiveManagerMng;//用户
	private static final Logger log = LoggerFactory.getLogger(ILiveRoomGiftController.class);
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 获取直播间礼物列表
	 * @param roomId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="/giftList.jspx",method=RequestMethod.GET)
	public void selectGiftList(Integer roomId,String terminalType, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if(JedisUtils.exists("giftList:"+roomId)) {
	        	
				ResponseUtils.renderJson(request, response, JedisUtils.get("giftList:"+roomId));
				return;
			}else {
				if(roomId==null){	
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播间标示错误");
					return;
				}
				ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
				Integer isSystemGift = iLiveLiveRoom.getIsSystemGift();
				isSystemGift = isSystemGift==null?0:isSystemGift;
				if(isSystemGift==1){
					List<ILiveGift> iLiveGifts = new ArrayList<ILiveGift>();
					Integer isUserGift = iLiveLiveRoom.getIsUserGift();
					isUserGift = isUserGift==null?0:isUserGift;
					if(isUserGift==1){
						iLiveGifts = iLiveGiftMng.selectIliveGiftByGiftType(roomId,ILiveGift.LIVE_USER_GIFT);
					}else{
						iLiveGifts = iLiveGiftMng.selectIliveGiftByUserId(0l,0);
					}
					JSONArray jsonArray = new JSONArray();
					for (ILiveGift iLiveGift : iLiveGifts) {
						JSONObject jo = new JSONObject(iLiveGift);
						jsonArray.put(jo);
					}
					resultJson.put("data", jsonArray);
					resultJson.put("isSystemGift", isSystemGift);
				}else{
					resultJson.put("isSystemGift", isSystemGift);
				}
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取信息成功");
				JedisUtils.set("giftList:"+roomId, resultJson.toString(), 120);
			}
			
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取礼物列表失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 发送礼物
	 * @param terminalType
	 * @param roomId 直播间ID
	 * @param userId 用户ID 
	 * @param giftId 礼物ID
	 * @param giftNumber 礼物数量
	 * @param response 
	 * @param request
	 */
	@RequestMapping(value="/save/userGift.jspx",method=RequestMethod.GET)
	public void saveILiveUserGift(String terminalType,Integer roomId,Long userId,Long giftId,Integer giftNumber,
			HttpServletResponse response, HttpServletRequest request){
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null||userId==null||giftId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "标示错误");
				return;
			}
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			ILiveGift iLiveGift = iLiveGiftMng.selectIliveGiftByGiftId(giftId);
			ILiveUserGift iLiveUserGift = new ILiveUserGift();
			ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
			iLiveUserGift.setEvenId(liveEvent.getLiveEventId());
			iLiveUserGift.setGiftId(giftId);
			iLiveUserGift.setGiftImage(iLiveGift.getGiftImage());
			iLiveUserGift.setGiftName(iLiveGift.getGiftName());
			iLiveUserGift.setGiftNumber(giftNumber);
			iLiveUserGift.setGiftPrice(iLiveGift.getGiftPrice());
			iLiveUserGift.setGiveTime(Timestamp.valueOf(format.format(new Date())));
			iLiveUserGift.setRoomId(roomId);
			iLiveUserGift.setRoomTitle(liveEvent.getLiveTitle());
			ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
			iLiveUserGift.setUserId(userId);
			iLiveUserGift.setUserName(iLiveManager.getNailName());
			iLiveUserGiftMng.addILiveUserGift(iLiveUserGift);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
			if(concurrentHashMap!=null){
				Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
				ILiveMessage iLiveMessage = new ILiveMessage();
				iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_GIFT);
				iLiveUserGift.setGiftOperation(ILiveUserGift.ILIVE_GIFT_ADD);
				iLiveMessage.setiLiveUserGift(iLiveUserGift);
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
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "送礼物失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	/**
	 * 新增用户礼物
	 * @param roomId 直播间ID
	 * @param giftName 礼物名称
	 * @param giftImage 礼物地址
	 * @param giftPrice 礼物单价
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/admin/saveGift.jspx")
	public void saveILiveGift(String terminalType,Long userId,Integer roomId,String giftName,String giftImage,String giftPrice,HttpServletRequest request, HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			Double.parseDouble(giftPrice);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "输入礼物价格不规范");
			ResponseUtils.renderJson(request,response, resultJson.toString());
			return;
		}
		try {
			UserBean userBean = ILiveUtils.getUser(request);
			if (userBean == null) {
				ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
				if (iLiveManager == null) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户信息不存在");
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				} else {
					userBean = new UserBean();
					userBean.setUsername(iLiveManager.getNailName());
					userBean.setUserId(String.valueOf(userId));
					userBean.setUserThumbImg(iLiveManager.getUserImg());
					userBean.setLevel(iLiveManager.getLevel());
					userBean.setNickname(iLiveManager.getNailName());
				}
			}
			ILiveGift iLiveGift = new ILiveGift();
			iLiveGift.setGiftImage(giftImage);
			giftName = URLDecoder.decode(giftName, "utf-8");
			iLiveGift.setGiftName(giftName);
			iLiveGift.setGiftPrice(giftPrice);
			iLiveGift.setGiftType(ILiveGift.LIVE_USER_GIFT);
			iLiveGift.setRoomId(roomId);
			iLiveGift.setUserId(Long.parseLong(userBean.getUserId()));
			iLiveGiftMng.addILiveGift(iLiveGift);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
			if(concurrentHashMap!=null){
				Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
				ILiveMessage iLiveMessage = new ILiveMessage();
				iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_GIFT);
				ILiveUserGift iLiveUserGift = new ILiveUserGift();
				iLiveUserGift.setGiftOperation(ILiveUserGift.ILIVE_GIFT_Operation);
				iLiveMessage.setiLiveUserGift(iLiveUserGift);
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
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 删除数据
	 * @param roomId 直播间
	 * @param giftId 礼物ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/admin/deleteGift.jspx")
	public void deleteILiveGift(String terminalType,Integer roomId,Long giftId,HttpServletRequest request, HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			iLiveGiftMng.deleteILiveGiftById(giftId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
			if(concurrentHashMap!=null){
				Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
				ILiveMessage iLiveMessage = new ILiveMessage();
				iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_GIFT);
				ILiveUserGift iLiveUserGift = new ILiveUserGift();
				iLiveUserGift.setGiftOperation(ILiveUserGift.ILIVE_GIFT_Operation);
				iLiveMessage.setiLiveUserGift(iLiveUserGift);
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
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 启动或者关闭系统礼物
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/admin/update/isSystemGift.jspx")
	public void updateRoomSystemGift(String terminalType,Integer roomId,HttpServletRequest request, HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			Integer isSystemGift = iliveRoom.getIsSystemGift();
			isSystemGift = isSystemGift==null?0:isSystemGift;
			if(isSystemGift==1){
				iliveRoom.setIsSystemGift(0);
				isSystemGift=0;
			}else{
				iliveRoom.setIsSystemGift(1);
				isSystemGift=1;
			}
			iLiveLiveRoomMng.update(iliveRoom);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("isSystemGift", isSystemGift);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
			if(concurrentHashMap!=null){
				Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
				ILiveMessage iLiveMessage = new ILiveMessage();
				iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_GIFT);
				ILiveUserGift iLiveUserGift = new ILiveUserGift();
				iLiveUserGift.setGiftOperation(ILiveUserGift.ILIVE_GIFT_Operation);
				iLiveMessage.setiLiveUserGift(iLiveUserGift);
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
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 *  启动或者关闭用户礼物
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/admin/update/isUserGift.jspx")
	public void updateRoomUserGift(String terminalType,Integer roomId,HttpServletRequest request, HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			Integer isUserGift = iliveRoom.getIsUserGift();
			isUserGift = isUserGift==null?0:isUserGift;
			if(isUserGift==1){
				iliveRoom.setIsUserGift(0);
				isUserGift=0;
			}else{
				iliveRoom.setIsUserGift(1);
				isUserGift=1;
			}
			iLiveLiveRoomMng.update(iliveRoom);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("isUserGift", isUserGift);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
			if(concurrentHashMap!=null){
				Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
				ILiveMessage iLiveMessage = new ILiveMessage();
				iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_GIFT);
				ILiveUserGift iLiveUserGift = new ILiveUserGift();
				iLiveUserGift.setGiftOperation(ILiveUserGift.ILIVE_GIFT_Operation);
				iLiveMessage.setiLiveUserGift(iLiveUserGift);
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
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
}
