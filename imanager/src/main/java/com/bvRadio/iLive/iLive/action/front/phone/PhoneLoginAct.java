package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.Hashtable;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.web.ApplicationCache;

@Controller
public class PhoneLoginAct {
	
	/**
	 * 用户登录直播间
	 * @param userId 用户ID
	 * @param liveId 直播间ID
	 * @param sessionType 登录类型   0 IoSession（APP） 1 WebSocketSession(WEB登录)
	 * @param userType 用户类型   0 个人 1企业
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/intoRoom.jspx")
	public void entLiveLogin(String userId, Integer liveId,Integer sessionType,Integer userType,HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			
			ILiveLiveRoom live = iLiveLiveRoomMng.findById(liveId);
			if (live == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播不存在");
				ResponseUtils.renderJson(request,response, resultJson.toString());
				return;
			}
			UserBean liveUser = new UserBean();
			try {
				ILiveManager iLiveManager  = iLiveManagerMng.selectILiveManagerById(Long.parseLong(userId));
				liveUser.setUserType(userType);
				liveUser.setUserId(String.valueOf(iLiveManager.getUserId()));
				liveUser.setUsername(iLiveManager.getNailName()); 
				liveUser.setUserThumbImg(iLiveManager.getUserImg());
				liveUser.setLevel(iLiveManager.getLevel());
				liveUser.setSessionType(sessionType);
			} catch (Exception e) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户获取失败");
				ResponseUtils.renderJson(request,response, resultJson.toString());
				return;
			}
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new Hashtable<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			System.out.println("生存客户端登录token 的liveid="+liveId);
			userMap.put(userId, liveUser);
			// 生存客户端登录token
			UUID uuid = UUID.randomUUID();
			String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
			JSONObject json = new JSONObject();
			json.put("liveId", liveId);
			json.put("userId", userId);
			CacheManager.putCacheInfo(CacheManager.mobile_token_ + token, json, 5 * 60 * 1000);
			// 直播内容
			JSONObject liveJson = live.putLiveInJson(null);
			try {
				// 记录
				//liveMng.addViewNumById(liveId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("data", liveJson);
			resultJson.put("token", token);
			ResponseUtils.renderJson(request,response, resultJson.toString());
			return;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 游客 直播间
	 * @param liveId 直播间ID
	 * @param sessionType 登录类型   0 IoSession（APP） 1 WebSocketSession(WEB登录)
	 * @param userType 用户类型   0 个人 1企业
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/tourists/intoRoom.jspx")
	public void entLiveLogin(Integer liveId,Integer sessionType,Integer userType,HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			
			ILiveLiveRoom live = iLiveLiveRoomMng.findById(liveId);
			if (live == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播不存在");
				ResponseUtils.renderJson(request,response, resultJson.toString());
				return;
			}
			String userId = request.getSession().getId();
			UserBean liveUser = new UserBean();
			liveUser.setUserType(userType);
			liveUser.setSessionType(sessionType);
			liveUser.setUserId(userId);
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new Hashtable<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			System.out.println("生存客户端登录token 的liveid="+liveId);
			userMap.put(userId, liveUser);
			// 生存客户端登录token
			UUID uuid = UUID.randomUUID();
			String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
			JSONObject json = new JSONObject();
			json.put("liveId", liveId);
			json.put("userId", userId);
			CacheManager.putCacheInfo(CacheManager.mobile_token_ + token, json, 5 * 60 * 1000);
			// 直播内容
			JSONObject liveJson = live.putLiveInJson(null);
			try {
				// 记录
				//liveMng.addViewNumById(liveId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("data", liveJson);
			resultJson.put("token", token);
			ResponseUtils.renderJson(request,response, resultJson.toString());
			return;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;//直播间
	@Autowired
	private ILiveManagerMng iLiveManagerMng;//用户
}
