package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.ILiveEventVo;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.web.ApplicationCache;

/**
 * 直播间操作通知管理
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="roomType")
public class ILiveRoomTypeController {
	@Autowired
	private ILiveEventMng iLiveEventMng;

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;
	/**
	 * 是否启动自动审核控制
	 * 
	 * @param response
	 * @param liveEventId
	 *            场次ID
	 * @param autoCheckSecond
	 */
	@RequestMapping(value = "/update/auto.do", method = RequestMethod.POST)
	public void updateILiveEventByAutoCheckSecond(HttpServletResponse response, Long liveEventId,
			Integer autoCheckSecond,Integer roomId) {
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null){
				resultJson.put("status", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
			iLiveEvent.setAutoCheckSecond(autoCheckSecond);
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(roomId);
			if(userMap!=null){
				Iterator<String> userIterator = userMap.keySet().iterator();
				while (userIterator.hasNext()) {
					String userId = userIterator.next();
					UserBean user = userMap.get(userId);
					List<ILiveMessage> userMsgList = user.getMsgList();
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
				}
			}
			Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
			roomListMap.put(roomId, autoCheckSecond);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("status", ERROR_STATUS);
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
	@RequestMapping(value = "/update/estoppeltype.do", method = RequestMethod.POST)
	public void updateILiveEventByEstoppelType(HttpServletResponse response, Long liveEventId,
			Integer estoppelType,Integer roomId) {
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null){
				resultJson.put("status", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
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
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(roomId);
			if(userMap!=null){
				Iterator<String> userIterator = userMap.keySet().iterator();
				while (userIterator.hasNext()) {
					String userId = userIterator.next();
					UserBean user = userMap.get(userId);
					List<ILiveMessage> userMsgList = user.getMsgList();
					iLiveMessage.setRoomType(1);
					userMsgList.add(iLiveMessage);
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
				}
			}
			iLiveEvent.setEstoppelType(estoppelType);
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
}
