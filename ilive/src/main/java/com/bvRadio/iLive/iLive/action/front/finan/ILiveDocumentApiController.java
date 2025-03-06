package com.bvRadio.iLive.iLive.action.front.finan;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

/**
 * 文档直播   接口
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="api/document")
public class ILiveDocumentApiController {
	@Autowired
	private ILiveEventMng iLiveEventMng;
	/**
	 * 文档直播翻页状态控制
	 * @param eventId 场次ID
	 * @param documentManual 手动翻页是否开启：0 关闭  1 开启
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="update/manual.jspx")
	public void updateDocumentManual(Long eventId,Integer documentManual,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			if(eventId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请确定场次信息");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(eventId);
			iLiveEvent.setDocumentManual(documentManual);
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			Integer roomId = iLiveEvent.getRoomId();
			if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
				JedisUtils.delObject("roomInfo:"+roomId);
			}
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_DOCUMENT);
			iLiveMessage.setDocumentDownload(iLiveEvent.getDocumentDownload());
			iLiveMessage.setDocumentId(-1l);
			iLiveMessage.setDocumentManual(documentManual);
			iLiveMessage.setDocumentPageNO(iLiveEvent.getDocumentPageNO());
			if("open".equals(ConfigUtils.get("redis_service"))) {
				iLiveMessage.setMsgId(Long.parseLong("-"+roomId+""+roomId));
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
			
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 文档直播是否允许下载
	 * @param eventId 场次ID
	 * @param documentDownload 文档是否允许用户下载：0 关闭  1 开启
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="update/download.jspx")
	public void updateDocumentDownload(Long eventId,Integer documentDownload,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			if(eventId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请确定场次信息");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(eventId);
			iLiveEvent.setDocumentDownload(documentDownload);
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			Integer roomId = iLiveEvent.getRoomId();
			if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
				JedisUtils.delObject("roomInfo:"+roomId);
			}
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_DOCUMENT);
			iLiveMessage.setDocumentDownload(documentDownload);
			iLiveMessage.setDocumentId(-1l);
			iLiveMessage.setDocumentManual(iLiveEvent.getDocumentManual());
			iLiveMessage.setDocumentPageNO(iLiveEvent.getDocumentPageNO());
			if("open".equals(ConfigUtils.get("redis_service"))) {
				iLiveMessage.setMsgId(Long.parseLong("-"+roomId+""+roomId));
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
			
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 场次绑定文档发生改变
	 * @param eventId 场次ID
	 * @param documentId 文档ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="update/documentId.jspx")
	public void openDocumentManager(Long eventId,Long documentId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(eventId);
			Integer roomId = iLiveEvent.getRoomId();
			if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
				JedisUtils.delObject("roomInfo:"+roomId);
			}
			if(eventId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请确定场次信息");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			
			iLiveEvent.setDocumentId(documentId);
			iLiveEvent.setDocumentPageNO(1);
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
			if (null == userMap) {
				userMap = new ConcurrentHashMap<String, UserBean>();
				userListMap.put(roomId, userMap);
			}
			Iterator<String> userIterator = userMap.keySet().iterator();
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_DOCUMENT);
			iLiveMessage.setDocumentDownload(iLiveEvent.getDocumentDownload());
			iLiveMessage.setDocumentId(documentId);
			iLiveMessage.setDocumentManual(iLiveEvent.getDocumentManual());
			iLiveMessage.setDocumentPageNO(1);
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
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 页码改变
	 * @param eventId 场次ID
	 * @param documentPageNO 当前页码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="update/documentPageNO.jspx")
	public void updateDocumentPageNO(Long eventId,Integer documentPageNO,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			if(eventId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请确定场次信息");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(eventId);
			iLiveEvent.setDocumentPageNO(documentPageNO);
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			//手动翻页是否开启：0 关闭  1 开启
			Integer documentManual = iLiveEvent.getDocumentManual();
			if(documentManual==0){
				Integer roomId = iLiveEvent.getRoomId();
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
				if (null == userMap) {
					userMap = new ConcurrentHashMap<String, UserBean>();
					userListMap.put(roomId, userMap);
				}
				Iterator<String> userIterator = userMap.keySet().iterator();
				ILiveMessage iLiveMessage = new ILiveMessage();
				iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_DOCUMENT);
				iLiveMessage.setDocumentDownload(iLiveEvent.getDocumentDownload());
				iLiveMessage.setDocumentId(-1l);
				iLiveMessage.setDocumentManual(documentManual);
				iLiveMessage.setDocumentPageNO(documentPageNO);
				if("open".equals(ConfigUtils.get("redis_service"))) {
					iLiveMessage.setMsgId(Long.parseLong("-"+roomId+""+roomId));
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
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	
	
}
