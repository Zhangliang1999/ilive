package com.bvRadio.iLive.iLive.action.front.finan;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.ILiveEventVo;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

/**
 * 直播间操作通知管理
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="api/roomType")
public class ILiveRoomTypeApiController {
	@Autowired
	private ILiveEventMng iLiveEventMng;

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	/**
	 * 是否启动自动审核控制
	 * 
	 * @param response
	 * @param liveEventId
	 *            场次ID
	 * @param autoCheckSecond
	 */
	@RequestMapping(value = "/update/auto.jspx")
	public void updateILiveEventByAutoCheckSecond(HttpServletResponse response, Long liveEventId,
			Integer autoCheckSecond,Integer roomId) {
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
			if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
				JedisUtils.delObject("roomInfo:"+roomId);
			}
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
			ILiveLiveRoom iLiveLiveRoom = iLiveRoomMng.findById(roomId);
			ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
			iLiveEvent.setAutoCheckSecond(autoCheckSecond);
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
			if("open".equals(ConfigUtils.get("redis_service"))) {
				Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
				if(userIdList!=null&&userIdList.size()!=0) {
					for (String userIds : userIdList) {
						ILiveMessage message = new ILiveMessage();
						message.setRoomType(18);
						ILiveEventVo iLiveEventVo = new ILiveEventVo(); 
						/**
						 * 审核时长 s
						 */
						iLiveEventVo.setCheckedTime(autoCheckSecond);
						/**
						 * 全局禁言类型: 0 开启 1 关闭
						 */
						iLiveEventVo.setEstoppleType(iLiveEvent.getEstoppelType());
						/**
						 * 直播状态： 0 直播未开始 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
						 */
						iLiveEventVo.setLiveStatus(iLiveEvent.getLiveStatus());
						/**
						 * 直播控制播放类型 : 1 推流直播 2 拉流直播 3 列表直播 4 PC手机桌面直播 5 手机直播 6 导播台
						 */
						iLiveEventVo.setPlayType(iLiveEvent.getPlayType());
						/**
						 * 流地址
						 */
						String hlsPlayAddr = "http://" + accessMethodBySeverGroupId.getHttp_address() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
						iLiveEventVo.setHlsUrl(hlsPlayAddr);
						/**
						 * 图片
						 */
						iLiveEventVo.setPlayBgAddr(iLiveEvent.getPlayBgAddr());
						message.setiLiveEventVo(iLiveEventVo);
						message.setLiveRoomId(roomId);
						message.setMsgId(Long.parseLong((-roomId)+""));
						JedisUtils.delObject("msg:"+message.getMsgId());
						JedisUtils.setByte(("msg:"+message.getMsgId()).getBytes(), SerializeUtil.serialize(message), 0);
						
						JedisUtils.listAdd(iLiveLiveRoom.getRoomId()+":"+userIds, message.getMsgId()+"");
					}
				}
				
			}else {
				if(userMap!=null){
					Iterator<String> userIterator = userMap.keySet().iterator();
					while (userIterator.hasNext()) {
						String key = userIterator.next();
						UserBean user = userMap.get(key);
						List<ILiveMessage> userMsgList = user.getMsgList();
						if(userMsgList==null){
							userMsgList= new ArrayList<ILiveMessage>();
						}
						ILiveMessage message = new ILiveMessage();
						message.setRoomType(18);
						ILiveEventVo iLiveEventVo = new ILiveEventVo(); 
						/**
						 * 审核时长 s
						 */
						iLiveEventVo.setCheckedTime(autoCheckSecond);
						/**
						 * 全局禁言类型: 0 开启 1 关闭
						 */
						iLiveEventVo.setEstoppleType(iLiveEvent.getEstoppelType());
						/**
						 * 直播状态： 0 直播未开始 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
						 */
						iLiveEventVo.setLiveStatus(iLiveEvent.getLiveStatus());
						/**
						 * 直播控制播放类型 : 1 推流直播 2 拉流直播 3 列表直播 4 PC手机桌面直播 5 手机直播 6 导播台
						 */
						iLiveEventVo.setPlayType(iLiveEvent.getPlayType());
						/**
						 * 流地址
						 */
						String hlsPlayAddr = "http://" + accessMethodBySeverGroupId.getHttp_address() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
						iLiveEventVo.setHlsUrl(hlsPlayAddr);
						/**
						 * 图片
						 */
						iLiveEventVo.setPlayBgAddr(iLiveEvent.getPlayBgAddr());
						message.setiLiveEventVo(iLiveEventVo);
						message.setLiveRoomId(roomId);
						userMsgList.add(message);
						user.setMsgList(userMsgList);
						userMap.put(key, user);
						
					}
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
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
	/**
	 * 全局禁言
	 * @param response
	 * @param liveEventId
	 *            场次ID
	 * @param autoCheckSecond
	 * @param roomId 直播间ID
	 */
	@RequestMapping(value = "/update/estoppeltype.jspx")
	public void updateILiveEventByEstoppelType(HttpServletResponse response, Long liveEventId,
			Integer estoppelType,Integer roomId) {
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
			if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
				JedisUtils.delObject("roomInfo:"+roomId);
			}
			if("open".equals(ConfigUtils.get("redis_service"))) {
				/*
				 * List<String> msgIdCheckList =JedisUtils.getList("msgIdListCheck"+roomId);
				 * ILiveMessage iLiveMessage = null; if (msgIdCheckList.size() > 0) {
				 * iLiveMessage =
				 * SerializeUtil.getObject(msgIdCheckList.get(msgIdCheckList.size() - 1)); }
				 * else { iLiveMessage = new ILiveMessage(); } if(estoppelType==0){ //开启
				 * iLiveMessage.setOpType(11); }else{ //关闭 iLiveMessage.setOpType(10); }
				 * iLiveMessage.setRoomType(0);
				 */
				ILiveLiveRoom iLiveLiveRoom = iLiveRoomMng.findById(roomId);
				ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
				ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
				
				Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
				if(userIdList!=null&&userIdList.size()!=0) {
					for(String userId:userIdList) {

						ILiveMessage message = new ILiveMessage();
						message.setRoomType(0);
						ILiveEventVo iLiveEventVo = new ILiveEventVo(); 
						/**
						 * 审核时长 s
						 */
						iLiveEventVo.setCheckedTime(iLiveEvent.getAutoCheckSecond());
						/**
						 * 全局禁言类型: 0 开启 1 关闭
						 */
						iLiveEventVo.setEstoppleType(estoppelType);
						/**
						 * 直播状态： 0 直播未开始 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
						 */
						iLiveEventVo.setLiveStatus(iLiveEvent.getLiveStatus());
						/**
						 * 直播控制播放类型 : 1 推流直播 2 拉流直播 3 列表直播 4 PC手机桌面直播 5 手机直播 6 导播台
						 */
						iLiveEventVo.setPlayType(iLiveEvent.getPlayType());
						/**
						 * 流地址
						 */
						String hlsPlayAddr = "http://" + accessMethodBySeverGroupId.getHttp_address() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
						iLiveEventVo.setHlsUrl(hlsPlayAddr);
						/**
						 * 图片
						 */
						iLiveEventVo.setPlayBgAddr(iLiveEvent.getPlayBgAddr());
						message.setiLiveEventVo(iLiveEventVo);
						message.setLiveRoomId(roomId);
						message.setMsgId(Long.parseLong((-iLiveLiveRoom.getRoomId())+""));
						
						JedisUtils.delObject("msg:"+message.getMsgId());
						JedisUtils.setByte(("msg:"+message.getMsgId()).getBytes(), SerializeUtil.serialize(message), 0);
						
						boolean flag=true;
						while (flag) {
							String requestionIdString=UUID.randomUUID().toString();
							if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
								JedisUtils.listAdd(iLiveLiveRoom.getRoomId()+":"+userId, message.getMsgId()+"");
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
				ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
				Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
				List<ILiveMessage> list = chatInteractiveMap.get(roomId);
				if(list==null){
					list = new ArrayList<ILiveMessage>();
				}
				ILiveMessage iLiveMessage = null;
				if(list.size()>0){
					iLiveMessage = list.get(list.size()-1);
				}else{
					iLiveMessage = new ILiveMessage();
				}
				if(estoppelType==0){
					//开启
					iLiveMessage.setOpType(11);
				}else{
					//关闭
					iLiveMessage.setOpType(10);
				}
				ILiveLiveRoom iLiveLiveRoom = iLiveRoomMng.findById(roomId);
				ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
				ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
				if(userMap!=null){
					Iterator<String> userIterator = userMap.keySet().iterator();
					while (userIterator.hasNext()) {
						String key = userIterator.next();
						UserBean user = userMap.get(key);
						List<ILiveMessage> userMsgList = user.getMsgList();
						if(userMsgList==null){
							userMsgList= new ArrayList<ILiveMessage>();
						}
						ILiveMessage message = new ILiveMessage();
						message.setRoomType(0);
						ILiveEventVo iLiveEventVo = new ILiveEventVo(); 
						/**
						 * 审核时长 s
						 */
						iLiveEventVo.setCheckedTime(iLiveEvent.getAutoCheckSecond());
						/**
						 * 全局禁言类型: 0 开启 1 关闭
						 */
						iLiveEventVo.setEstoppleType(estoppelType);
						/**
						 * 直播状态： 0 直播未开始 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
						 */
						iLiveEventVo.setLiveStatus(iLiveEvent.getLiveStatus());
						/**
						 * 直播控制播放类型 : 1 推流直播 2 拉流直播 3 列表直播 4 PC手机桌面直播 5 手机直播 6 导播台
						 */
						iLiveEventVo.setPlayType(iLiveEvent.getPlayType());
						/**
						 * 流地址
						 */
						String hlsPlayAddr = "http://" + accessMethodBySeverGroupId.getHttp_address() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
						iLiveEventVo.setHlsUrl(hlsPlayAddr);
						/**
						 * 图片
						 */
						iLiveEventVo.setPlayBgAddr(iLiveEvent.getPlayBgAddr());
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
		ResponseUtils.renderJson(response, resultJson.toString());
	}
}
