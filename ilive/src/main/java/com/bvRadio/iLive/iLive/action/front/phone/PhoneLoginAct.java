package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEstoppelMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
public class PhoneLoginAct {
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng; 
	@Autowired
	private ILiveMessageMng msgMng;

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
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/intoRoom.jspx")
	public void entLiveLogin(String userId, Integer liveId, Integer sessionType, Integer userType, Integer webType,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			webType = webType == null ? 1 : webType;
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
				liveUser.setLevel(iLiveManager.getLevel()==null?ILiveManager.USER_LEVEL_ADMIN:iLiveManager.getLevel());
				liveUser.setSessionType(sessionType);
				liveUser.setNickname(iLiveManager.getNailName());
				liveUser.setFunctionCode(request.getSession().getId());
				UserBean userBean = ILiveUtils.getUser(request);
				if (userBean == null) {
					ILiveUtils.setUser(request, liveUser);
				}
				
			} catch (Exception e) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户获取失败");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			if("open".equals(ConfigUtils.get("redis_service"))) {
				
				
				JedisUtils.setAdd("userIdList"+liveId,userId+ "_" + request.getSession().getId());
					
				
			}
			if(sessionType==1&&userType==1&&webType==2){
				System.out.println("自服务管理员进入");
			}else{
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
			}
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new ConcurrentHashMap<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			// System.out.println("生存客户端登录token 的liveid=" + liveId);
			String key = userId + "_" + request.getSession().getId();
			if(userMap.containsKey(key)) {
				userMap.remove(key);
				userMap.put(key, liveUser);
			}else {
				userMap.put(key, liveUser);
			}
			
			System.out.println("内存放入用户：" + key+"时间:"+new Date());
			// 生存客户端登录token
			UUID uuid = UUID.randomUUID();
			String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
			JSONObject json = new JSONObject();
			json.put("liveId", liveId);
			json.put("roomId", liveId);
			json.put("userId", key);
			json.put("liveEventId", live.getLiveEvent().getLiveEventId());
			json.put("webType", webType);
			CacheManager.putCacheInfo(CacheManager.mobile_token_ + token, json, 5 * 60 * 1000);
			// 直播内容
			JSONObject liveJson = live.putLiveInJson(null);
			ILiveServerAccessMethod accessMethodBySever = serverAccessMng
					.getAccessMethodBySeverGroupId(live.getServerGroupId());
			String appGuideAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/appguide.html?roomId="
					+ live.getRoomId();
			Integer openGuideSwitch = live.getLiveEvent().getOpenGuideSwitch();
			resultJson.put("appGuideAddr", appGuideAddr);
			resultJson.put("openGuideSwitch", openGuideSwitch);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("data", liveJson);
			resultJson.put("token", token);
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_USER);
			iLiveMessage.setWelcomeLanguage("欢迎【"+liveUser.getNickname()+"】进入直播间");
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(liveId);
			if(concurrentHashMap!=null){
				Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
				while (userIterator.hasNext()) {
					/*if("open".equals(ConfigUtils.get("redis_service"))) {
						JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
						String id = userIterator.next();
						List<String> msgIdList =new ArrayList<String>();
						msgIdList.add(iLiveMessage.getMsgId()+"");
						JedisUtils.setList(liveId+":"+id, msgIdList, 0);
					}else {*/
						String id = userIterator.next();
						UserBean user = concurrentHashMap.get(id);
						List<ILiveMessage> msgList = user.getMsgList();
						if (msgList == null) {
							msgList = new ArrayList<ILiveMessage>();
						}
						msgList.add(iLiveMessage);
					//}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 游客 直播间
	 * 
	 * @param liveId
	 *            直播间ID
	 * @param sessionType
	 *            登录类型 0 IoSession（APP） 1 WebSocketSession(WEB登录)
	 * @param userType
	 *            用户类型 0 个人 1企业
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/tourists/intoRoom.jspx")
	public void entLiveLogin(Integer liveId, Integer sessionType, Integer userType, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {

			ILiveLiveRoom live = iLiveLiveRoomMng.findById(liveId);
			if (live == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			String userId = 0 + "_" + request.getSession().getId();
			UserBean liveUser = new UserBean();
			liveUser.setUserType(userType);
			liveUser.setSessionType(sessionType);
			liveUser.setUserId(userId);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new ConcurrentHashMap<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			// System.out.println("生存客户端登录token 的liveid=" + liveId);
			userMap.put(userId, liveUser);
			// 生存客户端登录token
			UUID uuid = UUID.randomUUID();
			String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
			JSONObject json = new JSONObject();
			json.put("liveId", liveId);
			json.put("roomId", liveId);
			json.put("userId", userId);
			CacheManager.putCacheInfo(CacheManager.mobile_token_ + token, json, 5 * 60 * 1000);
			// 直播内容
			JSONObject liveJson = live.putLiveInJson(null);
			try {
				// 记录
				// liveMng.addViewNumById(liveId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("data", liveJson);
			resultJson.put("token", token);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	@Autowired
	private ILiveManagerMng iLiveManagerMng;// 用户

	@Autowired
	private ILiveEstoppelMng iLiveEstoppelMng;

	@Autowired
	private ILiveServerAccessMethodMng serverAccessMng;
}
