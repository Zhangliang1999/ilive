package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;
import static com.bvRadio.iLive.iLive.Constants.UPLOAD_FILE_TYPE_IMAGE;
import static com.bvRadio.iLive.iLive.Constants.UN_LOGIN;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.xml.sax.InputSource;

import com.alibaba.druid.support.json.JSONUtils;
import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppCertEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.AppSimpleCertInfo;
import com.bvRadio.iLive.iLive.action.front.vo.AppUserStateVo;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.action.front.vo.LiveEventsVo;
import com.bvRadio.iLive.iLive.action.front.vo.RoomCreateVo;
import com.bvRadio.iLive.iLive.action.front.vo.RoomEditVo;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveManagerState;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMeetRequest;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveRandomRecordTask;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.IliveSubRoom;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.ILiveEventVo;
import com.bvRadio.iLive.iLive.manager.BBSDiyformMng;
import com.bvRadio.iLive.iLive.manager.BBSDiymodelMng;
import com.bvRadio.iLive.iLive.manager.ILiveAppointmentMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveEstoppelMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerStateMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveMeetRequestMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveRandomRecordTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomStaticsMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.manager.ILiveVideoHistoryMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;
import com.bvRadio.iLive.iLive.task.AliYunLiveTask;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;
import com.bvRadio.iLive.iLive.util.ConvertThread;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.HttpRequest;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.ILiveUMSMessageUtil;
import com.bvRadio.iLive.iLive.util.IPUtils;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.QrCodeUtils;
import com.bvRadio.iLive.iLive.util.RoomNoticeUtil;
import com.bvRadio.iLive.iLive.util.SafeTyChainPasswdUtil;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.util.StringUtil;
import com.bvRadio.iLive.iLive.util.TwoDimensionCode;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.iLive.web.PostMan;
import com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "livephone/room")
public class ILiveRoomPhoneAct {

	private static final String HTTP_PROTOACAL = "http://";

	Logger logger = LoggerFactory.getLogger(ILiveRoomPhoneAct.class);

	@Autowired
	private ILiveLiveRoomMng roomMng;

	@Autowired
	private ILivePageDecorateMng pageDecorateMng;

	@Autowired
	private ILiveEventMng iLiveEventMng;

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private ILiveEnterpriseMng ILiveEnterpriseMng;
	
	@Autowired
	private BBSDiyformMng bbsDiyformMng;

	@Autowired
	private BBSDiymodelMng bbsDiymodelMng;

	@Autowired
	private ILiveAppointmentMng iLiveAppointmentMng;

	@Autowired
	private ILiveEstoppelMng iLiveEstoppelMng;

	@Autowired
	private ILiveEnterpriseFansMng iLiveEnterpriseFansMng;

	@Autowired
	private ILiveVideoHistoryMng iLiveVideoHisotryMng;

	@Autowired
	private ILiveRandomRecordTaskMng iLiveRandomRecordTaskMng;

	@Autowired
	private ILiveManagerStateMng iLiveManagerStateMng;

	@Autowired
	private ILiveRoomStaticsMng iLiveRoomStaticsMng;
	@Autowired
	private ILiveSubRoomMng iLiveSubRoomMng;
	@Autowired
	private SentitivewordFilterMng sentitivewordFilterMng;
	@Autowired
	private ILiveViewWhiteBillMng viewWhiteMng;
	@Autowired
	private ILiveMeetRequestMng meetRequestMng;
	private final static String RTMP_PROTOACAL = "rtmp://";
	/**
	 * 获取直播间列表 #ADD v1.0
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/roomlist.jspx")
	public void getRoomList(String roomName, Integer roomStatus, Long userId, Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			Pagination pager = roomMng.getPagerForManager(roomName, roomStatus, userId, pageNo, pageSize);
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
			int intValue = iLiveManager.getCertStatus().intValue();
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			if (intValue == 4) {
				if (pager != null && pager.getList() != null) {
					List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) pager.getList();
					if (!list.isEmpty()) {
						for (ILiveLiveRoom room : list) {
							JSONObject putNewLiveInJson = room.putPlayNewLiveInJson(room);
							jsonList.add(putNewLiveInJson);
						}
					}
				}
			} else if (intValue == 3 || intValue == 2 || intValue == 5) {
				if (pager != null && pager.getList() != null) {
					List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) pager.getList();
					if (!list.isEmpty()) {
						for (ILiveLiveRoom room : list) {
							JSONObject putNewLiveInJson = room.putPlayNewLiveInJson(room);
							jsonList.add(putNewLiveInJson);
							break;
						}
					}
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取直播间列表成功");
			resultJson.put("data", jsonList);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取直播间列表失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	@RequestMapping(value = "/into/manager.jspx")
	public void gotoManager(Long userId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			UserBean appUser = ILiveUtils.getAppUser(request);
			ILiveManagerState managerState = iLiveManagerStateMng
					.getIliveManagerState(Long.parseLong(appUser.getUserId()));
			if (managerState == null) {
				managerState = new ILiveManagerState();
				managerState.setCertStatus(appUser.getCertStatus());
				managerState.setManagerId(Long.parseLong(appUser.getUserId()));
				iLiveManagerStateMng.saveIliveManagerState(managerState);
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(appUser.getUserId()));
				ILiveEnterprise enterPrise = iLiveManager.getEnterPrise();
				IliveAppRetInfo userInfo = this.buildAppRet(iLiveManager, enterPrise);
				AppUserStateVo userStateVo = new AppUserStateVo();
				appUser.setCertStatus(managerState.getCertStatus());
				ILiveUtils.setAppUser(request, appUser);
				userStateVo.setUpdateState(1);
				userStateVo.setUserInfo(userInfo);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "用户状态初始化");
				resultJson.put("data", userStateVo);
			} else {
				if (managerState.getCertStatus().equals(appUser.getCertStatus())) {
					// 用户状态未发生改变
					AppUserStateVo userStateVo = new AppUserStateVo();
					userStateVo.setUpdateState(0);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "用户状态未发生改变");
					resultJson.put("data", userStateVo);
				} else {
					// 用户状态发生了改变
					ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(appUser.getUserId()));
					ILiveEnterprise enterPrise = iLiveManager.getEnterPrise();
					IliveAppRetInfo userInfo = this.buildAppRet(iLiveManager, enterPrise);
					AppUserStateVo userStateVo = new AppUserStateVo();
					userStateVo.setUpdateState(1);
					userStateVo.setUserInfo(userInfo);
					appUser.setCertStatus(managerState.getCertStatus());
					ILiveUtils.setAppUser(request, appUser);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "用户状态发生改变");
					resultJson.put("data", userStateVo);
				}
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "内部发生异常");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 直接进入观看端页面
	 */
	@RequestMapping(value = "/goto/approom.jspx")
	public void gotoMyAppRoom(Long userId, Integer roomId, Integer sessionType, Integer webType,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom live = roomMng.findById(roomId);
		if (live == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播不存在");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		
//		if (!live.getManagerId().equals(userId)) {
//			resultJson.put("code", ERROR_STATUS);
//			resultJson.put("message", "该直播间不属于请求用户");
//			resultJson.put("data", new JSONObject());
//			ResponseUtils.renderJson(request, response, resultJson.toString());
//			return;
//		}
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
//		Integer level=iLiveManager.getLevel();
		boolean ret=this.checkIfIn(roomId, userId);
		if (ret) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播房间不属于该操作用户");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		this.entLiveLoginByUser(String.valueOf(userId), live, sessionType, 1, request, response, webType);
	}

	public void entLiveLoginByUser(String userId, ILiveLiveRoom live, Integer sessionType, Integer userType,
			HttpServletRequest request, HttpServletResponse response, Integer webType) {
		JSONObject resultJson = new JSONObject();
		Integer liveId = live.getRoomId();
		try {
			UserBean liveUser = new UserBean();
			try {
				ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(Long.parseLong(userId));
				liveUser.setUserType(userType);
				liveUser.setUserId(String.valueOf(iLiveManager.getUserId()));
				liveUser.setUsername(iLiveManager.getUserName());
				liveUser.setUserThumbImg(iLiveManager.getUserImg());
				liveUser.setLevel(iLiveManager.getLevel());
				liveUser.setSessionType(sessionType);
				liveUser.setNickname(iLiveManager.getNailName());
				Integer enterpriseId = iLiveManager.getEnterPrise().getEnterpriseId();
				liveUser.setEnterpriseId(enterpriseId);
				liveUser.setFunctionCode(request.getSession().getId());
			} catch (Exception e) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户获取失败");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new ConcurrentHashMap<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			
			String key = userId + "_" + request.getSession().getId();
			userMap.put(key, liveUser);
			if("open".equals(ConfigUtils.get("redis_service"))) {
				
					JedisUtils.setAdd("userIdList"+liveId,key);
					
				
			}
			// 生存客户端登录token
			UUID uuid = UUID.randomUUID();
			String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
			// System.out.println("登录在线用户生存客户端登录token=" + token);
			Long liveEventId = live.getLiveEvent().getLiveEventId();
			iLiveEventMng.putLiveEventUserCache(liveEventId);
			JSONObject json = new JSONObject();
			json.put("liveId", liveId);
			json.put("roomId", liveId);
			json.put("userId", key);
			json.put("webType", webType);
			json.put("liveEventId", liveEventId);
			CacheManager.putCacheInfo(CacheManager.mobile_token_ + token, json, 5 * 60 * 1000);
			JedisUtils.del(CacheManager.mobile_token_ + token);
			JedisUtils.set(CacheManager.mobile_token_ + token, json.toString(), 300);
			/**
			 * 获取直播间推流地址
			 */
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = iLiveServerAccessMethodMng
						.getAccessMethodBySeverGroupId(live.getServerGroupId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			String pushStreamAddr = HTTP_PROTOACAL + accessMethodBySeverGroupId.getHttp_address() + "/live/live"
					+ live.getRoomId() + "/5000k/tzwj_video.m3u8";
			String rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
					+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + live.getRoomId() + "_tzwj_5000k";
			Map<String, String> generatorEncoderPwd = SafeTyChainPasswdUtil.INSTANCE.generatorEncoderPwd();
			pushStreamAddr = String.format("%s?ut=%s&us=%s&sign=%s", pushStreamAddr,
					generatorEncoderPwd.get("timestamp"), generatorEncoderPwd.get("sequence"),
					generatorEncoderPwd.get("encodePwd"));
			rtmpAddr = String.format("%s?ut=%s&us=%s&sign=%s", rtmpAddr, generatorEncoderPwd.get("timestamp"),
					generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
			ILiveEvent liveEvent = new ILiveEvent();
			BeanUtilsExt.copyProperties(liveEvent, live.getLiveEvent());
			List<PageDecorate> list = pageDecorateMng.getList(liveEvent.getLiveEventId());
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
					int menuType = page.getMenuType().intValue();
					if (menuType == 3) {
						continue;
					}
					if (page.getMenuType().intValue() == 1) {
						// http://zb.tv189.com/phone/theme.html?roomId=352&userId=125
						String guideAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/theme.html?roomId="
								+ liveId + "&userId=" + (userId == null ? 0 : userId);
						String richContent = guideAddr;
						page.setRichContent(richContent);
					}
					newPageDecorate.add(page);
				}
			} else if (!pageType2 && pageType3) {
				for (PageDecorate page : list) {
					int menuType = page.getMenuType().intValue();
					if (menuType == 3) {
						page.setMenuName("直播聊天");
						page.setMenuType(2);
					}

					if (page.getMenuType().intValue() == 1) {
						// http://zb.tv189.com/phone/theme.html?roomId=352&userId=125
						String guideAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/theme.html?roomId="
								+ liveId + "&userId=" + (userId == null ? 0 : userId);
						String richContent = guideAddr;
						page.setRichContent(richContent);
					}
					newPageDecorate.add(page);
				}
			} else {
				for (PageDecorate page : list) {
					// 话题的 给上访问地址
					if (page.getMenuType().intValue() == 1) {
						// http://zb.tv189.com/phone/theme.html?roomId=352&userId=125
						String guideAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/theme.html?roomId="
								+ liveId + "&userId=" + (userId == null ? 0 : userId);
						String richContent = guideAddr;
						page.setRichContent(richContent);
					}
					newPageDecorate.add(page);
				}
			}
			liveEvent.setPageRecordList(newPageDecorate);
			// 判断预约状态
			boolean appointment = iLiveAppointmentMng.isAppointment(userId, live.getLiveEvent().getLiveEventId());
			if (appointment) {
				resultJson.put("appointment", 1);
			} else {
				resultJson.put("appointment", 0);
			}
			// 开启全局禁言
			if (liveEvent.getEstoppelType() == 0) {
				resultJson.put("estoppelType", 0);
				resultJson.put("forbidTalk", 1);
			} else {
				// 判断用户禁言状态
				UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
				if (userBean != null) {
					// 登录后的用户判断
					boolean iLiveEstoppelYesOrNo = iLiveEstoppelMng.getILiveEstoppelYesOrNo(live.getRoomId(),
							Long.parseLong(userId));
					if (iLiveEstoppelYesOrNo) {
						resultJson.put("forbidTalk", 1);
					} else {
						resultJson.put("forbidTalk", 0);
					}
				} else {
					// 不登录就不禁言 等待发言登录
					resultJson.put("forbidTalk", 0);
					resultJson.put("isAppointment", 0);
				}
				resultJson.put("estoppelType", 1);
			}
			Integer enterpriseId = live.getEnterpriseId();
			ILiveEnterprise iLiveEnterPrise = ILiveEnterpriseMng.getILiveEnterpriseById(enterpriseId);
			boolean exist = iLiveEnterpriseFansMng.isExist(enterpriseId, Long.parseLong(userId));
			Integer concernStatus = 0;
			if (exist) {
				concernStatus = 1;
			}
			iLiveEnterPrise.setConcernStatus(concernStatus);
			int fansNum = iLiveEnterpriseFansMng.getFansNum(enterpriseId);
			iLiveEnterPrise.setConcernTotal(fansNum);
			JSONObject sendjson = new JSONObject();
			sendjson.put("enterpriseId", enterpriseId);
			String data= HttpUtils.sendStr(ConfigUtils.get("imanager_url")+"/managercontrol/isEnterpriseRem.do", "POST", sendjson.toString());
			net.sf.json.JSONObject json_result = net.sf.json.JSONObject.fromObject(data);
			Integer status=Integer.parseInt(json_result.get("data").toString());
			iLiveEnterPrise.setIsEnterpriseRem(status);
			JSONObject liveJson = live.toViewBean(live, liveEvent, iLiveEnterPrise);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("data", liveJson);
			resultJson.put("hlsAddr", pushStreamAddr);
			resultJson.put("token", token);
			resultJson.put("rtmpAddr", rtmpAddr);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "进入直播间失败");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			logger.error(e.toString());
			return;
		}
	}

	/**
	 * 删除直播间
	 * 
	 * @param userId
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/destoryroom.jspx")
	public void deleteLiveRoom(Long userId, Integer roomId, HttpServletRequest request, HttpServletResponse response) {
		UserBean userBean = ILiveUtils.getAppUser(request);
		JSONObject resultJson = new JSONObject();
		if (userBean == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户未登录");
			resultJson.put("data", new JSONObject());
		} else {
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
			Integer certStatus = iLiveManager.getCertStatus();
			if (certStatus == null || certStatus.intValue() != 4) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户认证未通过,不具备删除直播间的能力");
				resultJson.put("data", new JSONObject());
			} else {
				long userIdSS = Long.parseLong(userBean.getUserId());
				if (userIdSS == userId) {
					if (iLiveManager != null) {
						ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
						Integer level=iLiveManager.getLevel();
						if(level==null){
							level=0;
						}
						boolean ret=false;
						if(level==3){
							List<IliveSubRoom> list=iLiveSubRoomMng.selectILiveSubById(userId);
							for(IliveSubRoom subRoom:list){
								if(!roomId.equals(subRoom.getLiveRoomId())){
									ret=true;
								}else{
									ret=false;
								}
							}
						}
						if (iliveRoom != null &&(( level!=3&&iliveRoom.getManagerId().equals(userId))||(level==3&&!ret))) {
							/**
							 * 直播状态
							 */
							Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
							if (liveStatus != ILivePlayStatusConstant.PLAY_ING
									&& liveStatus != ILivePlayStatusConstant.PAUSE_ING) {
								iliveRoom.setDeleteStatus(1);
								roomMng.update(iliveRoom);
								// 通知有删除直播间
								ILiveMessage iLiveMessage = new ILiveMessage();
								iLiveMessage.setRoomType(3);
								ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
										.getUserListMap();
								ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
								if (userMap != null) {
									Iterator<String> userIterator = userMap.keySet().iterator();
									while (userIterator.hasNext()) {
										String key = userIterator.next();
										UserBean userIt = userMap.get(key);
										List<ILiveMessage> userMsgList = userIt.getMsgList();
										if (userMsgList == null) {
											userMsgList = new ArrayList<ILiveMessage>();
										}
										userMsgList.add(iLiveMessage);
										userBean.setMsgList(userMsgList);
										userMap.put(key, userIt);
									}
								}
								Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache
										.getChatInteractiveMap();
								chatInteractiveMap.remove(roomId);
								Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache
										.getChatInteractiveMapNO();
								chatInteractiveMapNO.remove(roomId);
								Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
								quizLiveMap.remove(roomId);
								Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
								roomListMap.remove(roomId);
								resultJson.put("code", SUCCESS_STATUS);
								resultJson.put("message", "删除成功");
								resultJson.put("data", new JSONObject());
							} else {
								resultJson.put("code", ERROR_STATUS);
								resultJson.put("message", "直播间正在直播,不能删除");
								resultJson.put("data", new JSONObject());
							}
						} else {
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "房间不属于该用户");
							resultJson.put("data", new JSONObject());
						}
					}
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户状态不合法");
					resultJson.put("data", new JSONObject());
				}
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 开始直播
	 */
	@RequestMapping(value = "openlive.jspx")
	public void openLive(Long userId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
			if (iLiveManager != null) {
				// 认证通过
				if (iLiveManager.getCertStatus() == 4) {
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "获取房间信息成功");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("roomId", 0);
					resultJson.put("data", jsonObject);
				} else {
					ILiveLiveRoom liveRoom = roomMng.getIliveRoomByUserId(userId);
					if (liveRoom != null) {
						Integer roomId = liveRoom.getRoomId();
						JSONObject jsonObject = new JSONObject();
						resultJson.put("code", SUCCESS_STATUS);
						jsonObject.put("roomId", roomId);
						resultJson.put("message", "获取房间信息成功");
						resultJson.put("data", jsonObject);
					} else {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "无法获取到该用户的试用房间");
						resultJson.put("data", new JSONObject());
					}
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户不存在");
				resultJson.put("data", new JSONObject());
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "开启直播时发生异常");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	/**
	 * 创建直播间
	 * 
	 * @param roomCreateVo
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/create.jspx")
	public void roomCreate(RoomCreateVo roomCreateVo, Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		UserBean userBean = ILiveUtils.getAppUser(request);
		JSONObject resultJson = new JSONObject();
		if(userBean==null) {
			resultJson.put("code", UN_LOGIN);
			resultJson.put("message", "用户未登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		
		//手机客户端不再传输用户ID了，所以此处从
		userId=Long.parseLong(userBean.getUserId());
		roomCreateVo.setUserId(userId);
		ILiveManager manager=iLiveManagerMng.selectILiveManagerById(userId);
		Integer level=manager.getLevel();
		String subTop="";
		List<ILiveSubLevel> iLiveSubLevel = iLiveManagerMng.selectILiveSubById(userId);
		if(iLiveSubLevel!=null && iLiveSubLevel.size()>0) {
			 subTop=iLiveSubLevel.get(0).getSubTop();
			if(subTop==null) {
				subTop="0";
			}
		}
		if(roomCreateVo.getLiveDesc()==null) {
			roomCreateVo.setLiveDesc("");
		}
		if(roomCreateVo.getNeedLogin()==null){
			roomCreateVo.setNeedLogin(0);
		}
		if(level!=null&&level==3&&subTop.indexOf("1")<0) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户无权限创建直播间");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}else {
		try {

			if (userBean == null) {
				resultJson.put("code", UN_LOGIN);
				resultJson.put("message", "用户未登录");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			//查询如果是子账户,查看有没有权限
			String userId2 = userBean.getUserId();
			long parseLong = Long.parseLong(userId2);
			 if (parseLong != userId.intValue()) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户状态不合法");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else {
				
				Set<String> sensitiveWord = sentitivewordFilterMng.getSensitiveWord(roomCreateVo.getLiveTitle());
				Set<String> sensitiveWord2 = new HashSet<>();
				if (roomCreateVo.getLiveDesc()!=null&&!roomCreateVo.getLiveDesc().equals("")) {
					sensitiveWord2 = sentitivewordFilterMng.getSensitiveWord(roomCreateVo.getLiveDesc());
				}
				if (sensitiveWord.size()!=0||sensitiveWord2.size()!=0) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "包含敏感词");
					if (sensitiveWord.size()!=0) {
						resultJson.put("data", JSONArray.fromObject(sensitiveWord));
					}else if (sensitiveWord2.size()!=0) {
						resultJson.put("data", JSONArray.fromObject(sensitiveWord2));
					}
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
				
				
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
				Long enterpriseId = iLiveManager.getEnterpriseId();
				ILiveEnterprise iLiveEnterprise = ILiveEnterpriseMng.getILiveEnterpriseById(enterpriseId.intValue());
				Integer certStatus = iLiveEnterprise.getCertStatus();
//				boolean b = EnterpriseUtil.selectIfEn(enterpriseId.intValue(), EnterpriseCache.ENTERPRISE_FUNCTION_createLive,certStatus);
//				if(b){
//					resultJson.put("code", ERROR_STATUS);
//					resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_createLive_Content);
//					ResponseUtils.renderJson(request, response, resultJson.toString());
//					return;
//				}
				Integer liveType1 = roomCreateVo.getLiveType()==null?1:roomCreateVo.getLiveType();
				if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType1)) {
					boolean b2 = EnterpriseUtil.selectIfEn(enterpriseId.intValue(), EnterpriseCache.ENTERPRISE_FUNCTION_LianMai,certStatus);
					if(b2){
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_LianMai_Content);
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
				}
				if (iLiveManager.getCertStatus().intValue() == 4
						&& iLiveEnterprise.getCertStatus().intValue() == 4) {
					roomCreateVo.setRoomType(0);
					Integer saveNewBean = roomMng.saveNewBean(roomCreateVo);
					if (saveNewBean > 0) {
						ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(saveNewBean);
						Integer serverGroupId = iliveRoom.getServerGroupId();
						ILiveServerAccessMethod accessMethodBySeverGroupId = null;
						try {
							accessMethodBySeverGroupId = iLiveServerAccessMethodMng
									.getAccessMethodBySeverGroupId(serverGroupId);
						} catch (Exception e) {
							e.printStackTrace();
						}
						String rtmpAddr;
						Integer liveType = iliveRoom.getLiveType();
						if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType)) {
							String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
							String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
							rtmpAddr = "rtmp://video-center.alivecdn.com/" + aliyunLiveAppname + "/" + saveNewBean
									+ "_host?vhost=" + aliyunLiveDomain;
						} else {
							rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
									+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + saveNewBean + "_tzwj_5000k";
						}
						iliveRoom.setRtmpAddr(rtmpAddr);
						JSONObject putNewLiveInJson = iliveRoom.putPlayNewLiveInJson(iliveRoom);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "生成直播间成功");
						resultJson.put("data", putNewLiveInJson);
					} else {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "生成直播间失败");
						resultJson.put("data", new JSONObject());
					}
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户认证未通过,不能创建直播间");
					resultJson.put("data", new JSONObject());
				}
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "生成直播间失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	/**
	 * 创建直播间
	 * 
	 * @param roomCreateVo
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/createMeet.jspx")
	public void meetCreate(RoomCreateVo roomCreateVo, Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		UserBean userBean = ILiveUtils.getAppUser(request);
		JSONObject resultJson = new JSONObject();
		if(userBean==null) {
			resultJson.put("code", UN_LOGIN);
			resultJson.put("message", "用户未登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		
		//手机客户端不再传输用户ID了，所以此处从
		userId=Long.parseLong(userBean.getUserId());
		roomCreateVo.setUserId(userId);
		ILiveManager manager=iLiveManagerMng.selectILiveManagerById(userId);
		Integer level=manager.getLevel();
		String subTop="";
		List<ILiveSubLevel> iLiveSubLevel = iLiveManagerMng.selectILiveSubById(userId);
		if(iLiveSubLevel!=null && iLiveSubLevel.size()>0) {
			 subTop=iLiveSubLevel.get(0).getSubTop();
			if(subTop==null) {
				subTop="0";
			}
		}
		if(roomCreateVo.getLiveDesc()==null) {
			roomCreateVo.setLiveDesc("");
		}
		if(roomCreateVo.getNeedLogin()==null){
			roomCreateVo.setNeedLogin(0);
		}
		if(level!=null&&level==3&&subTop.indexOf("1")<0) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户无权限创建会议");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}else {
		try {

			if (userBean == null) {
				resultJson.put("code", UN_LOGIN);
				resultJson.put("message", "用户未登录");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			//查询如果是子账户,查看有没有权限
			String userId2 = userBean.getUserId();
			long parseLong = Long.parseLong(userId2);
			 if (parseLong != userId.intValue()) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户状态不合法");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else {
				
				Set<String> sensitiveWord = sentitivewordFilterMng.getSensitiveWord(roomCreateVo.getLiveTitle());
				Set<String> sensitiveWord2 = new HashSet<>();
				if (roomCreateVo.getLiveDesc()!=null&&!roomCreateVo.getLiveDesc().equals("")) {
					sensitiveWord2 = sentitivewordFilterMng.getSensitiveWord(roomCreateVo.getLiveDesc());
				}
				if (sensitiveWord.size()!=0||sensitiveWord2.size()!=0) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "包含敏感词");
					if (sensitiveWord.size()!=0) {
						resultJson.put("data", JSONArray.fromObject(sensitiveWord));
					}else if (sensitiveWord2.size()!=0) {
						resultJson.put("data", JSONArray.fromObject(sensitiveWord2));
					}
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
				
				
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
				Long enterpriseId = iLiveManager.getEnterpriseId();
				ILiveEnterprise iLiveEnterprise = ILiveEnterpriseMng.getILiveEnterpriseById(enterpriseId.intValue());
				Integer certStatus = iLiveEnterprise.getCertStatus();
				boolean b = EnterpriseUtil.selectIfEn(enterpriseId.intValue(), EnterpriseCache.ENTERPRISE_FUNCTION_createLive,certStatus);
				if(b){
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_createLive_Content);
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
				Integer liveType1 = roomCreateVo.getLiveType()==null?1:roomCreateVo.getLiveType();
				if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType1)) {
					boolean b2 = EnterpriseUtil.selectIfEn(enterpriseId.intValue(), EnterpriseCache.ENTERPRISE_FUNCTION_LianMai,certStatus);
					if(b2){
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_LianMai_Content);
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
				}
				if (iLiveManager.getCertStatus().intValue() == 4
						&& iLiveEnterprise.getCertStatus().intValue() == 4) {
					roomCreateVo.setRoomType(1);
					
					Integer saveNewBean = roomMng.saveNewBean(roomCreateVo);
					if (saveNewBean > 0) {
						ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(saveNewBean);
						ILiveMeetRequest host =new ILiveMeetRequest();
						ILiveMeetRequest student =new ILiveMeetRequest();
						host.setRoomId(saveNewBean);
						host.setPassword((int)((Math.random()*9+1)*100000)+"");
						host.setRoomName(iliveRoom.getLiveEvent().getLiveTitle());
						host.setType(1);
						student.setRoomId(saveNewBean);
						student.setPassword((int)((Math.random()*9+1)*100000)+"");
						student.setType(3);
						student.setRoomName(iliveRoom.getLiveEvent().getLiveTitle());
						String hostId=UUID.randomUUID().toString();
						String studentId=UUID.randomUUID().toString();
						host.setId(hostId);
						student.setId(studentId);
						for (int i = 0; i < 2; i++) {
							String ext = "png";
							String tempFileName = System.currentTimeMillis() + "." + ext;
							String realPath = request.getSession().getServletContext().getRealPath("/temp");
							File tempFile = new File(realPath + "/" + tempFileName);
							//TwoDimensionCode.encoderQRCode(ConfigUtils.get("meet_play_url")+"?roomId="+room.getMeetPushRoomId(),  realPath + "/" + tempFileName,"jpg");
							String defaultLogoPath = ConfigUtils.get("default_logo_path");
							String logoPath = FileUtils.getRootPath(defaultLogoPath);
							if(i==0) {
								QrCodeUtils.createQRCodeWithFileLogo(ConfigUtils.get("meet_invitation_url")+"/enterMeet/index.html?roomId="+saveNewBean+"&id="+hostId.substring(0, 6), realPath + "/" + tempFileName, logoPath);
							}else {
								QrCodeUtils.createQRCodeWithFileLogo(ConfigUtils.get("meet_invitation_url")+"/enterMeet/index.html?roomId="+saveNewBean+"&id="+studentId.substring(0, 6), realPath + "/" + tempFileName, logoPath);
							}
							
							String filePath = FileUtils.getTimeFilePath(tempFileName);
							
								boolean result = false;
								ILiveUploadServer uploadServer = iLiveUploadServerMng.getDefaultServer();
								if (uploadServer != null) {
									FileInputStream in = new FileInputStream(tempFile);
									result = uploadServer.upload(filePath, in);
								}
								
								if(tempFile.exists()) {
									tempFile.delete();
								}
								String httpUrl = uploadServer.getHttpUrl() + uploadServer.getFtpPath() + "/" + filePath;
								if(i==0) {
									host.setPic(httpUrl);
								}else {
									student.setPic(httpUrl);
								}
						}
							
                        host.setLoginUrl(ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+saveNewBean);
						
						
						student.setLoginUrl(ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+saveNewBean);
							
	                        
						meetRequestMng.save(host);
						meetRequestMng.save(student);
						Map<String,Object> param = new HashMap<String,Object>();
				    	param.put("tenant_id",ConfigUtils.get("tenant_id"));
				    	param.put("apikey",ConfigUtils.get("apikey")); 
				        String message = JSONUtils.toJSONString(param);
				    	String data=HttpRequest.sendPost(ConfigUtils.get("meet_url_check"), message,null);
					    net.sf.json.JSONObject json_result = net.sf.json.JSONObject.fromObject(data);
					    String meetToken=json_result.get("data").toString();
					    Boolean flag= JedisUtils.exists("meetRoomToken");
					    if(!flag) {
					    	JedisUtils.set("meetRoomToken", meetToken, 0);
					    }
					    Map<String,Object> param2 = new HashMap<String,Object>();
					    param2.put("event_id", saveNewBean+"");
					    param2.put("service_type", "2");
					    param2.put("start_time", iliveRoom.getLiveEvent().getLiveStartTime().getTime()/1000);
					    param2.put("end_time", iliveRoom.getLiveEvent().getLiveEndTime().getTime()/1000);
					    param2.put("inadvance", -1);
					    param2.put("viewer", true);
					    param2.put("record", false);
					    param2.put("tenant_id", ConfigUtils.get("tenant_id"));
					    param2.put("room_type", "1");
					   
				        String message2 = JSONUtils.toJSONString(param2);
					    String data2=HttpRequest.sendPost(ConfigUtils.get("meet_url_create"), message2,meetToken);
					    net.sf.json.JSONObject json_result2 = net.sf.json.JSONObject.fromObject(data2);
					    net.sf.json.JSONObject json_result3 = net.sf.json.JSONObject.fromObject(json_result2.get("data").toString());
					    iliveRoom.setMeetId(json_result3.get("event_id").toString());
					    roomMng.update(iliveRoom);
						Integer serverGroupId = iliveRoom.getServerGroupId();
						ILiveServerAccessMethod accessMethodBySeverGroupId = null;
						try {
							accessMethodBySeverGroupId = iLiveServerAccessMethodMng
									.getAccessMethodBySeverGroupId(serverGroupId);
						} catch (Exception e) {
							e.printStackTrace();
						}
						String rtmpAddr;
						Integer liveType = iliveRoom.getLiveType();
						if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType)) {
							String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
							String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
							rtmpAddr = "rtmp://video-center.alivecdn.com/" + aliyunLiveAppname + "/" + saveNewBean
									+ "_host?vhost=" + aliyunLiveDomain;
						} else {
							rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
									+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + saveNewBean + "_tzwj_5000k";
						}
						iliveRoom.setRtmpAddr(rtmpAddr);
						JSONObject putNewLiveInJson = iliveRoom.putPlayNewLiveInJson(iliveRoom);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "生成会议成功");
						resultJson.put("data", putNewLiveInJson);
					} else {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "生成会议失败");
						resultJson.put("data", new JSONObject());
					}
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户认证未通过,不能创建会议");
					resultJson.put("data", new JSONObject());
				}
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "生成会议失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 修改直播间
	 * 
	 * @param roomCreateVo
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/edit.jspx")
	public void roomEdit(RoomEditVo roomEditVo, Long userId, Integer noticeRestore, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		
		try {
			UserBean userBean = ILiveUtils.getAppUser(request);
			if (userBean == null) {
				resultJson.put("code", UN_LOGIN);
				resultJson.put("message", "用户未登录");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			long userIdLong = Long.parseLong(userBean.getUserId());
			if (userIdLong != userId.intValue()) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户状态不合法");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			Integer roomId = roomEditVo.getRoomId();
			ILiveLiveRoom iliveRoomOrgin = roomMng.getIliveRoom(roomId);
			if (iliveRoomOrgin == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
//			Integer level=iLiveManager.getLevel();
			boolean ret=this.checkIfIn(roomId, userId);
			if (ret) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不属于该用户,不能修改");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
//			if (!iliveRoomOrgin.getManagerId().equals(userId)) {
//				resultJson.put("code", ERROR_STATUS);
//				resultJson.put("message", "直播间不属于该用户,不能修改");
//				resultJson.put("data", new JSONObject());
//				ResponseUtils.renderJson(request, response, resultJson.toString());
//				return;
//			}
			noticeRestore = noticeRestore == null ? 0 : noticeRestore;
			boolean editNewBean = roomMng.editNewBean(roomEditVo, noticeRestore);
			if (editNewBean) {
				Set<String> sensitiveWord = sentitivewordFilterMng.getSensitiveWord(roomEditVo.getLiveTitle());
				Set<String> sensitiveWord2 = sentitivewordFilterMng.getSensitiveWord(roomEditVo.getLiveDesc());
				if (sensitiveWord.size()!=0||sensitiveWord2.size()!=0) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "包含敏感词");
					if (sensitiveWord.size()!=0) {
						resultJson.put("data", JSONArray.fromObject(sensitiveWord));
					}else if (sensitiveWord2.size()!=0) {
						resultJson.put("data", JSONArray.fromObject(sensitiveWord2));
					}
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
				
				
				ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomEditVo.getRoomId());
				Integer serverGroupId = iliveRoom.getServerGroupId();
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String rtmpAddr;
				Integer liveType = iliveRoom.getLiveType();
				if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType)) {
					String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
					String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
					rtmpAddr = "rtmp://video-center.alivecdn.com/" + aliyunLiveAppname + "/" + roomId
							+ "_host?vhost=" + aliyunLiveDomain;
				} else {
					rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
							+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
				}
				iliveRoom.setRtmpAddr(rtmpAddr);
				JSONObject putNewLiveInJson = iliveRoom.putNewLiveInJson(iliveRoom);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "设置直播间成功");
				resultJson.put("data", putNewLiveInJson);
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "设置直播间失败");
				resultJson.put("data", new JSONObject());
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "设置直播间失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 修改直播间
	 * 
	 * @param roomCreateVo
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/editMeet.jspx")
	public void meetEdit(RoomEditVo roomEditVo, Long userId, Integer noticeRestore, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		
		try {
			UserBean userBean = ILiveUtils.getAppUser(request);
			if (userBean == null) {
				resultJson.put("code", UN_LOGIN);
				resultJson.put("message", "用户未登录");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			long userIdLong = Long.parseLong(userBean.getUserId());
			if (userIdLong != userId.intValue()) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户状态不合法");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			Integer roomId = roomEditVo.getRoomId();
			ILiveLiveRoom iliveRoomOrgin = roomMng.getIliveRoom(roomId);
			if (iliveRoomOrgin == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			boolean ret=this.checkIfIn(roomId, userId);
			if (ret) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不属于该用户,不能修改");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}

			noticeRestore = noticeRestore == null ? 0 : noticeRestore;
			boolean editNewBean = roomMng.editNewBean(roomEditVo, noticeRestore);
			if (editNewBean) {
				Set<String> sensitiveWord = sentitivewordFilterMng.getSensitiveWord(roomEditVo.getLiveTitle());
				Set<String> sensitiveWord2 = sentitivewordFilterMng.getSensitiveWord(roomEditVo.getLiveDesc());
				if (sensitiveWord.size()!=0||sensitiveWord2.size()!=0) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "包含敏感词");
					if (sensitiveWord.size()!=0) {
						resultJson.put("data", JSONArray.fromObject(sensitiveWord));
					}else if (sensitiveWord2.size()!=0) {
						resultJson.put("data", JSONArray.fromObject(sensitiveWord2));
					}
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
				
				
				ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomEditVo.getRoomId());
				Integer serverGroupId = iliveRoom.getServerGroupId();
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String rtmpAddr;
				Integer liveType = iliveRoom.getLiveType();
				if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType)) {
					String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
					String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
					rtmpAddr = "rtmp://video-center.alivecdn.com/" + aliyunLiveAppname + "/" + roomId
							+ "_host?vhost=" + aliyunLiveDomain;
				} else {
					rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
							+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
				}
				iliveRoom.setRtmpAddr(rtmpAddr);
				JSONObject putNewLiveInJson = iliveRoom.putNewLiveInJson(iliveRoom);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "设置直播间成功");
				resultJson.put("data", putNewLiveInJson);
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "设置直播间失败");
				resultJson.put("data", new JSONObject());
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "设置直播间失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	/**
	 * 修改直播间连麦状态
	 * 
	 * @param roomCreateVo
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/editLianmai.jspx")
	public void roomEdit(Integer roomId , Integer liveType, Long userId, Integer noticeRestore, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			UserBean userBean = ILiveUtils.getAppUser(request);
			if (userBean == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户为登录");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			long userIdLong = Long.parseLong(userBean.getUserId());
			if (userIdLong != userId.intValue()) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户状态不合法");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveLiveRoom iliveRoomOrgin = roomMng.getIliveRoom(roomId);
			if (iliveRoomOrgin == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
//			Integer level=iLiveManager.getLevel();
			boolean ret=this.checkIfIn(roomId, userId);
			if (ret) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不属于该用户,不能修改");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
//			if (!iliveRoomOrgin.getManagerId().equals(userId)) {
//				resultJson.put("code", ERROR_STATUS);
//				resultJson.put("message", "直播间不属于该用户,不能修改");
//				resultJson.put("data", new JSONObject());
//				ResponseUtils.renderJson(request, response, resultJson.toString());
//				return;
//			}
			noticeRestore = noticeRestore == null ? 0 : noticeRestore;
			boolean editNewBean = roomMng.editLianmaiNewBean(roomId, liveType);
			if (editNewBean) {
				ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
				Integer serverGroupId = iliveRoom.getServerGroupId();
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String rtmpAddr;
				Integer liveTypeDB = iliveRoom.getLiveType();
				if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveTypeDB)) {
					String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
					String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
					rtmpAddr = "rtmp://video-center.alivecdn.com/" + aliyunLiveAppname + "/" + roomId
							+ "_host?vhost=" + aliyunLiveDomain;
				} else {
					rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
							+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
				}
				iliveRoom.setRtmpAddr(rtmpAddr);
				JSONObject putNewLiveInJson = iliveRoom.putNewLiveInJson(iliveRoom);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "设置直播间成功");
				resultJson.put("data", putNewLiveInJson);
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "设置直播间失败");
				resultJson.put("data", new JSONObject());
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "设置直播间失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	

	/**
	 * 获取直播间，企业版
	 * 
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/enterRoom.jspx")
	public void getRoomInfoByRoomId(Integer roomId, Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
//		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
//		Integer level=iLiveManager.getLevel();
		boolean ret=this.checkIfIn(roomId, userId);
//		boolean ret1=false;
		ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
//		if(iliveRoom.getManagerId().equals(userId)){
//			ret1=false;
//		}else if(level==3&&ret){
//			ret1=true;
//		}
		
		try {
			
			if (iliveRoom == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播房间不存在");
				resultJson.put("data", new JSONObject());
			} 
			else if (ret) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播房间不属于该操作用户");
				resultJson.put("data", new JSONObject());
			} 
			else if (iliveRoom.getOpenStatus() == 0) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间已经关闭");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else {
				if (ret) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播房间不属于该操作用户");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
				Integer serverGroupId = iliveRoom.getServerGroupId();
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String rtmpAddr;
				Integer liveType = iliveRoom.getLiveType();
				if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType)) {
					String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
					String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
					rtmpAddr = "rtmp://video-center.alivecdn.com/" + aliyunLiveAppname + "/" + roomId
							+ "_host?vhost=" + aliyunLiveDomain;
				} else {
					if(iliveRoom.getOpenLogoSwitch()!=null&&iliveRoom.getOpenLogoSwitch()==1) {
						rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
								+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "o_tzwj_5000k";
					}else {
						rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
								+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
					}
					
				}
				iliveRoom.setRtmpAddr(rtmpAddr);
				JSONObject putNewLiveInJson = iliveRoom.putNewLiveInJson(iliveRoom);

				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取直播间列表成功");
				resultJson.put("data", putNewLiveInJson);
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "内部异常");
			resultJson.put("data", new JSONObject());
			logger.error(e.toString());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 直播控制
	 */
	@RequestMapping(value = "/liveControl.jspx")
	public void liveControl(Integer playType, Integer roomId, Long userId, Integer liveStatus,
			HttpServletRequest request, HttpServletResponse response, String terminalType) {
		JSONObject resultJson = new JSONObject();
		UserBean userBean = ILiveUtils.getAppUser(request);
		if (userBean == null) {
			resultJson.put("code", UN_LOGIN);
			resultJson.put("message", "用户未登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}else {
			userId=Long.parseLong(userBean.getUserId());
			ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
			Integer days=null;
			try {
				 days=EnterpriseUtil.checkValiteTime(iliveRoom.getEnterpriseId(), userBean.getCertStatus());
				
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			 if(days==null){
				 days=1; 
			 }
			if (liveStatus != null) {
//				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
//				Integer level=iLiveManager.getLevel();
				boolean ret=this.checkIfIn(roomId, userId);
				if (ret) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播房间不属于该操作用户");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
				if (liveStatus == 1) {
					//判断是否还有直播时长
					
					boolean ret1=EnterpriseUtil.getIfCan(iliveRoom.getEnterpriseId(), EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration.toString(), userBean.getCertStatus());
				
					if(days==0) {
						System.out.println("提示套餐过期");
						resultJson.put("status", ERROR_STATUS);
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "您的套餐以过期，请重新购买套餐。");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}else if(ret1){
						resultJson.put("status", ERROR_STATUS);
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "您的可用直播时长不足，请购买对应直播套餐。");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}else{
						this.startPlay(iliveRoom, userId, resultJson, request, response, terminalType);
					}
					
				} else if (liveStatus == 2) {
					this.pausePlay(iliveRoom, userId, resultJson, request, response);
				} else if (liveStatus == 3) {
					this.stopPlay(iliveRoom, userId, resultJson, request, response);
				} else if (liveStatus == 4) {
					if(ILivePlayStatusConstant.PLAY_OVER.equals(iliveRoom.getLiveEvent().getLiveStatus())){
						boolean ret1=EnterpriseUtil.getIfCan(iliveRoom.getEnterpriseId(), EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration.toString(), userBean.getCertStatus());
						if(days==0) {
							System.out.println("提示套餐过期");
							resultJson.put("status", ERROR_STATUS);
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "您的套餐以过期，请重新购买套餐。");
							resultJson.put("data", new JSONObject());
							ResponseUtils.renderJson(request, response, resultJson.toString());
							return;
						}else if(ret1){
							resultJson.put("status", ERROR_STATUS);
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "您的可用直播时长不足，请购买对应直播套餐。");
							resultJson.put("data", new JSONObject());
							ResponseUtils.renderJson(request, response, resultJson.toString());
							return;
						}else{
							this.continuePlay(iliveRoom, userId, resultJson, request, response, terminalType);
						}
					}else{
						this.continuePlay(iliveRoom, userId, resultJson, request, response, terminalType);
					}
					
				} else {

				}
				
			}
		}
		
	}
public boolean checkIfIn(Integer roomId, Long userId){
	ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
	Integer level=iLiveManager.getLevel();
	if(level==null){
		level=0;
	}
	boolean ret=false;
	if(level==3){
		List<IliveSubRoom> list=iLiveSubRoomMng.selectILiveSubById(userId);
		for(IliveSubRoom subRoom:list){
			if(!roomId.equals(subRoom.getLiveRoomId())){
				ret=true;
			}else{
				ret=false;
			}
		}
	}
	boolean ret1=false;
	ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
	if(iliveRoom.getManagerId().equals(userId)){
		ret1=false;
	}else if(level==3&&ret){
		ret1=true;
	}
	return ret1;
}
	/**
	 * 控制直播开关按钮
	 * 
	 * @param roomId
	 * @param openStatus
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/roomControl.jspx")
	public void roomControl(Integer roomId, Integer openStatus, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			UserBean appUser = ILiveUtils.getAppUser(request);
			String userId = appUser.getUserId();
			ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
			if (iliveRoom != null) {
				Long managerId = iliveRoom.getManagerId();
				long parseLong = Long.parseLong(userId);
				if (managerId.equals(parseLong)) {
					iliveRoom.setOpenStatus(openStatus);
					roomMng.update(iliveRoom);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "操作成功");
					resultJson.put("data", new JSONObject());
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "操作成功");
					resultJson.put("data", new JSONObject());
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "操作失败");
				resultJson.put("data", new JSONObject());
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作失败");
			resultJson.put("data", new JSONObject());
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 控制直播开关按钮
	 * 
	 * @param roomId
	 * @param openStatus
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/interactionControl.jspx")
	public void interactionControl(Integer roomId, Integer interactionStatus, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
			ILiveEvent iLiveEvent = iliveRoom.getLiveEvent();
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
			List<ILiveMessage> list = chatInteractiveMap.get(roomId);
			if (list == null) {
				resultJson.put("status", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
			ILiveMessage iLiveMessage = null;
			if (list.size() > 0) {
				iLiveMessage = list.get(list.size() - 1);
			} else {
				iLiveMessage = new ILiveMessage();
			}
			if (interactionStatus == 1) {
				// 开启
				iLiveMessage.setOpType(11);
			} else {
				// 关闭
				iLiveMessage.setOpType(10);
			}
			ILiveServerAccessMethod accessMethodBySeverGroupId = iLiveServerAccessMethodMng
					.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
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
					iLiveMessage.setRoomType(1);
					userMsgList.add(iLiveMessage);
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
					String hlsPlayAddr = "http://" + accessMethodBySeverGroupId.getHttp_address() + ":"
							+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId
							+ "/5000k/tzwj_video.m3u8";
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
			iLiveEvent.setEstoppelType(interactionStatus);
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			resultJson.put("data", new JSONObject());
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作失败");
			resultJson.put("data", new JSONObject());
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 开始直播
	 */
	public void startPlay(ILiveLiveRoom iliveRoom, Long userId, JSONObject resultJson, HttpServletRequest request,
			HttpServletResponse response, String terminalType) {
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		/**
		 * 获取直播间推流地址
		 */
		ILiveServerAccessMethod accessMethodBySeverGroupId = null;
		try {
			accessMethodBySeverGroupId = iLiveServerAccessMethodMng
					.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 根据直播状态
		if (liveStatus.equals(ILivePlayStatusConstant.UN_START)) {
			Integer liveType = iliveRoom.getLiveType();
			if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType)) {
				Integer roomId = iliveRoom.getRoomId();
				String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
				String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
				String aliyunLiveModelName = ConfigUtils.get("aliyun_live_model_name");
				AliYunLiveTask aliYunLiveTask = new AliYunLiveTask(roomId, AliYunLiveTask.MODEL_START);
				new Thread(aliYunLiveTask).start();
				String pullAddr = "rtmp://" + aliyunLiveDomain + "/" + aliyunLiveAppname + "/" + roomId + "_host"
						+ aliyunLiveModelName;
				try {
					boolean pullStream = ILiveUMSMessageUtil.INSTANCE.pullStream(pullAddr, accessMethodBySeverGroupId,
							iliveRoom);
					logger.info("pullStream:" + pullStream);
				} catch (Exception e) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "流服务器交互失败");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					e.printStackTrace();
					return;
				}
			} else {
				try {
					boolean startPlay = ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId, iliveRoom);
					logger.info("startPlay:" + startPlay);
				} catch (Exception e1) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "流服务器交互失败");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					e1.printStackTrace();
					return;
				}
			}
			
			final String converTaskLogo=ConfigUtils.get("conver_task_logo");
			if(iliveRoom.getOpenLogoSwitch()!=null&&iliveRoom.getOpenLogoSwitch()==1) {
				if(iliveRoom.getConvertTaskId()!=null&&iliveRoom.getConvertTaskId()>0) {
					
				}else {
					try {
						
			          //创建一个SAXBuilder对象
			              SAXBuilder saxBuilder = new SAXBuilder();            
			            //读取prop.xml资源
			              Document Doc = saxBuilder.build(request.getSession().getServletContext().getRealPath("/temp")+"/xml/4.xml");
			            //获取根元素(prop)
			            Element Root = Doc.getRootElement();
			            
			            Element name=  Root.getChild("name");
			            name.setText(iliveRoom.getLiveEvent().getLiveTitle());
			            Element uri=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("uri");
			           
			            uri.setText(iliveRoom.getConvertLogo());
			            Element rtmpuri=  Root.getChild("inputs").getChild("network").getChild("uri");
			            
			            String pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
			    				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + iliveRoom.getRoomId() + "_tzwj_5000k";
			    		
			            String[] addr= pushStreamAddr.split("_");
			            rtmpuri.setText(addr[0]+"o_"+addr[1]+"_"+addr[2]);
			            Element Top=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("top");
			            Top.setText(iliveRoom.getLogoWidth()+"");
			            Element Left=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("left");
			            Left.setText(iliveRoom.getLogoHight()+"");
			            Element rtmpuri1=  Root.getChild("outputgroups").getChild("flashstreaming").getChild("uri");
			            rtmpuri1.setText(pushStreamAddr);
			            //格式化输出xml文件字符串
						Format format = Format.getCompactFormat();
						format.setEncoding("UTF-8");
						//这行保证输出后的xml的格式
						format.setIndent(" ");
						// 创建xml输出流操作类
						XMLOutputter xmlout = new XMLOutputter(format);
						ByteArrayOutputStream byteRsp = new ByteArrayOutputStream();
						xmlout.output(Doc, byteRsp);
						String str = byteRsp.toString("utf-8");
			            String data1=HttpUtils.sendStr(converTaskLogo+"/api/task", "POST", str);
			            Integer taskId=getTaskId(data1);
			            String res  =HttpUtils.sendStr(converTaskLogo+"/api/task/"+taskId+"/start", "PUT", "");
			            if(taskId!=null) {
			            	iliveRoom.setConvertTaskId(taskId);
			            }
			            
					} catch (JDOMException | IOException e) {
						resultJson.put("message", "创建logo转码任务失败");
						e.printStackTrace();
					}
				}
				
			}else {
				if(iliveRoom.getConvertTaskId()!=null&&iliveRoom.getConvertTaskId()>0) {

					try {
						String data1=HttpUtils.sendStr(converTaskLogo+"/api/task/"+iliveRoom.getConvertTaskId()+"/stop", "PUT", "");
						
			            	ConvertThread thread =new ConvertThread(iliveRoom.getRoomId(), roomMng, iliveRoom.getConvertTaskId());
			            	thread.run();
				           
				           
					} catch (Exception e) {
						resultJson.put("message", "删除logo转码任务失败");
						e.printStackTrace();
					}
				}
				
			}
			
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
			liveEvent.setLiveStartTime(new Timestamp(System.currentTimeMillis()));
			liveEvent.setRecordStartTime(new Timestamp(System.currentTimeMillis()));
			liveEvent.setRealStartTime(new Timestamp(System.currentTimeMillis()));
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + "/live/live"
					+ iliveRoom.getRoomId() + "/5000k/tzwj_video.m3u8";
			iliveRoom.setHlsAddr(pushStreamAddr);
			RoomNoticeUtil.nptice(iliveRoom);
			String realIpAddr = IPUtils.getRealIpAddr(request);
			ILiveEnterprise iLiveEnterPrise = ILiveEnterpriseMng.getILiveEnterpriseById(iliveRoom.getEnterpriseId());
			iliveRoom.getLiveEvent().setEnterprise(iLiveEnterPrise);
			try {
				// ConcurrentHashMap<Integer, ConcurrentHashMap<String,
				// UserBean>> userListMap = ApplicationCache
				// .getUserListMap();
				// Long nowNum = 0L;
				// if (userListMap == null) {
				// } else {
				// nowNum = (long) userListMap.get(key);
				// }
				// iLiveRoomStaticsMng.initRoom(liveEvent.getLiveEventId(),
				// nowNum);
				ApplicationCache.getOnlineNumber().remove(iliveRoom.getRoomId());
				ILiveUserViewStatics.INSTANCE.startLive(iliveRoom, realIpAddr, terminalType);
				roomMng.startTask(iliveRoom.getLiveEvent().getLiveEventId(), iliveRoom.getRoomId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("liveStatus", liveEvent.getLiveStatus());
			resultJson.put("data", jsonObject);
		} else {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合结束标准");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 结束直播
	 */
	public void stopPlay(final ILiveLiveRoom iliveRoom, final Long userId, JSONObject resultJson,
			HttpServletRequest request, HttpServletResponse response) {
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		final int roomId = iliveRoom.getRoomId();
		final int serverGroupId = iliveRoom.getServerGroupId();
		final UserBean userBean = ILiveUtils.getAppUser(request);
		if (userBean == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户未登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		if (liveStatus.equals(ILivePlayStatusConstant.PLAY_OVER)
				|| liveStatus.equals(ILivePlayStatusConstant.UN_START)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合结束标准");
			resultJson.put("data", new JSONObject());
		} else {
			/**
			 * 获取直播间推流地址
			 */
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = iLiveServerAccessMethodMng
						.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Integer liveType = iliveRoom.getLiveType();
				if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType)) {
					try {
						ILiveUMSMessageUtil.INSTANCE.closePullStream(accessMethodBySeverGroupId, iliveRoom);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				boolean stopPlay = ILiveUMSMessageUtil.INSTANCE.stopPlay(accessMethodBySeverGroupId, iliveRoom);
				logger.info("stopPlay:" + stopPlay);
			} catch (Exception e1) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "流服务器交互失败");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				e1.printStackTrace();
				return;
			}
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
			liveEvent.setLiveEndTime(new Timestamp(System.currentTimeMillis()));
			liveEvent.setRealEndTime(new Timestamp(System.currentTimeMillis()));
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			RoomNoticeUtil.nptice(iliveRoom);
			roomMng.stopTask(iliveRoom.getLiveEvent().getLiveEventId());
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("liveStatus", liveEvent.getLiveStatus());
			resultJson.put("data", jsonObject);
			
			
			//更新企业直播时长
			Integer enterpriseId1=iliveRoom.getEnterpriseId();
			ILiveEnterprise iLiveEnterPrise1 = ILiveEnterpriseMng.getILiveEnterpriseById(enterpriseId1);
			Integer certStatus = null;
			if(iLiveEnterPrise1!=null){
				certStatus = iLiveEnterPrise1.getCertStatus();
			}
			//判断是否有暂停过，若有，从暂停的开始时间开始计算
			Timestamp lastStartTime=null;
			if(liveEvent.getLastPauseStartTime()!=null){
				lastStartTime=liveEvent.getLastPauseStartTime();
			}else{
				lastStartTime=liveEvent.getRealStartTime();
			}
			Long useValue=(liveEvent.getRealEndTime().getTime()-lastStartTime.getTime())/1000;
			
			EnterpriseUtil.openEnterprise(enterpriseId1,EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration,useValue.toString(),certStatus);
			
			// 正常向流媒体服务器推流操作
			String realIpAddr = IPUtils.getRealIpAddr(request);
			ILiveEnterprise iLiveEnterPrise = ILiveEnterpriseMng.getILiveEnterpriseById(iliveRoom.getEnterpriseId());
			iliveRoom.getLiveEvent().setEnterprise(iLiveEnterPrise);
			ILiveUserViewStatics.INSTANCE.stopLive(iliveRoom, realIpAddr);
			final Integer enterpriseId = iliveRoom.getEnterpriseId();
			final Long userIdLong = iliveRoom.getManagerId();
			final ILiveRandomRecordTask task = iLiveRandomRecordTaskMng.getTaskByQuery(liveEvent.getLiveEventId(),
					Long.parseLong(userBean.getUserId()), ILivePlayStatusConstant.PLAY_ING);
			if (ILivePlayStatusConstant.PAUSE_ING != liveEvent.getLiveStatus()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
								.getAccessMethodBySeverGroupId(serverGroupId);
						String postUrl = HTTP_PROTOCAL + serverAccessMethod.getHttp_address() + ":"
								+ serverAccessMethod.getLivemsport() + "/livems/servlet/LiveServlet";
						String mountName = "live" + roomId;
						int vodGroupId = Integer.parseInt(ConfigUtils.get("defaultVodServerGroupId"));
						int length = (int) (System.currentTimeMillis()
								- iliveRoom.getLiveEvent().getRecordStartTime().getTime()) / 1000;
						String common = "";
						try {
							common = "?function=RecordLive&mountName=" + mountName + "&startTime="
									+ URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
											.format(iliveRoom.getLiveEvent().getRecordStartTime()), "UTF-8")
									+ "&length=" + length + "&destGroupId=" + vodGroupId;
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						}
						postUrl = postUrl + common;
						String downloadUrl = PostMan.downloadUrl(postUrl);
						if (downloadUrl != null) {
							try {
								String trimDownloadUrl = downloadUrl.trim();
								String relativePath = StringUtil.getTagValue(trimDownloadUrl, "ret");
								ILiveMediaFile liveMediaFile = new ILiveMediaFile();
								liveMediaFile.setMediaFileName(iliveRoom.getLiveEvent().getLiveTitle() + "-"
										+ new SimpleDateFormat("yyyyMMddHHmmss")
												.format(iliveRoom.getLiveEvent().getRecordStartTime()));
								liveMediaFile.setFilePath(relativePath);
								liveMediaFile.setCreateStartTime(iliveRoom.getLiveEvent().getRecordStartTime());
								liveMediaFile.setMediaCreateTime(new Timestamp(System.currentTimeMillis()));
								liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
								ILiveServerAccessMethod vodAccessMethod = iLiveServerAccessMethodMng
										.getAccessMethodBySeverGroupId(vodGroupId);
								liveMediaFile.setServerMountId(vodAccessMethod.getMountInfo().getServer_mount_id());
								liveMediaFile.setCreateType(0);
								liveMediaFile.setDelFlag(0);
								liveMediaFile.setDuration(length);
								liveMediaFile.setFileType(1);
								liveMediaFile.setOnlineFlag(1);
								liveMediaFile.setMediaInfoDealState(0);
								liveMediaFile.setMediaInfoStatus(0);
								liveMediaFile.setUserId(userId);
								liveMediaFile.setAllowComment(1);
								// 通过用户拿到企业
								liveMediaFile.setEnterpriseId((long) enterpriseId);
								liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
								liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
								//根据userid获取username
								ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userIdLong);
								liveMediaFile.setUserName(iLiveManager.getUserName());
								try {
									Thread.sleep(10000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								Long saveIliveMediaFile = iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
								iLiveVideoHisotryMng.saveVideoHistory(roomId, saveIliveMediaFile, userId);
							} catch (Exception e) {
								e.printStackTrace();
								logger.error(e.toString());
							}
						}

						if (task != null) {
							try {
								task.setEndTime(System.currentTimeMillis());
								task.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
								iLiveRandomRecordTaskMng.updateRecordTask(task);
								length = (int) (System.currentTimeMillis() - task.getStartTime()) / 1000;
								try {
									common = "?function=RecordLive&mountName=" + mountName + "&startTime="
											+ URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
													.format(new Timestamp(task.getStartTime())), "UTF-8")
											+ "&length=" + length + "&destGroupId=" + vodGroupId;
								} catch (UnsupportedEncodingException e1) {
									e1.printStackTrace();
								}
								postUrl = postUrl + common;
								downloadUrl = PostMan.downloadUrl(postUrl);
								if (downloadUrl != null) {
									String trimDownloadUrl = downloadUrl.trim();
									String relativePath = StringUtil.getTagValue(trimDownloadUrl, "ret");
									if (!relativePath.trim().equals("")) {
										ILiveMediaFile liveMediaFile = new ILiveMediaFile();
										liveMediaFile.setMediaFileName(iliveRoom.getLiveEvent().getLiveTitle() + "-"
												+ new SimpleDateFormat("yyyyMMddHHmmss")
														.format(iliveRoom.getLiveEvent().getRecordStartTime()));
										liveMediaFile.setFilePath(relativePath);
										liveMediaFile.setCreateStartTime(iliveRoom.getLiveEvent().getRecordStartTime());
										liveMediaFile.setMediaCreateTime(new Timestamp(System.currentTimeMillis()));
										liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
										ILiveServerAccessMethod vodAccessMethod = iLiveServerAccessMethodMng
												.getAccessMethodBySeverGroupId(vodGroupId);
										liveMediaFile
												.setServerMountId(vodAccessMethod.getMountInfo().getServer_mount_id());
										liveMediaFile.setCreateType(0);
										liveMediaFile.setDuration(length);
										liveMediaFile.setFileType(1);
										liveMediaFile.setOnlineFlag(1);
										// 通过用户拿到企业
										liveMediaFile.setEnterpriseId((long) userBean.getEnterpriseId());
										liveMediaFile.setUserId(Long.parseLong(userBean.getUserId()));
										liveMediaFile.setMediaInfoStatus(0);
										liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
										liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
										liveMediaFile.setDelFlag(0);
										liveMediaFile.setAllowComment(1);
										liveMediaFile.setMediaInfoDealState(0);
										try {
											Thread.sleep(15000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										Long saveIliveMediaFile = iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
										iLiveVideoHisotryMng.saveVideoHistory(roomId, saveIliveMediaFile,
												Long.parseLong(userBean.getUserId()));
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								logger.error(e.toString());
							}
						}
					}
				}).start();
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
		String converTaskLogo=ConfigUtils.get("conver_task_logo");
		try {
			String data1=HttpUtils.sendStr(converTaskLogo+"/api/task/"+iliveRoom.getConvertTaskId()+"/stop", "PUT", "");
			
            	ConvertThread thread =new ConvertThread(roomId, roomMng, iliveRoom.getConvertTaskId());
            	thread.run();
	           
	           
		} catch (Exception e) {
			resultJson.put("message", "删除logo转码任务失败");
			e.printStackTrace();
		}
	}

	/**
	 * 结束直播 直播数据量统计
	 */
	@RequestMapping(value = "statics/info.jspx")
	public void playStatics(Integer roomId, Long userId, HttpServletRequest request, HttpServletResponse response) {
		ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
		JSONObject resultJson = new JSONObject();
		// 直播间不存在
		if (iliveRoom == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播房间不存在");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		// 直播间不属于结束状态
		else if (!iliveRoom.getLiveEvent().getLiveStatus().equals(ILivePlayStatusConstant.PLAY_OVER)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播不是结束状态");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} else {
			JSONObject jsonObject = new JSONObject();
			Timestamp realStartTime = iliveRoom.getLiveEvent().getRealStartTime();
			if (System.currentTimeMillis() - realStartTime.getTime() > 0) {
				long duration = (System.currentTimeMillis() - realStartTime.getTime()) / 1000;
				// 直播时长
				jsonObject.put("durationTotal", duration);
			} else {
				jsonObject.put("durationTotal", 0);
			}
			// int base = new Random().nextInt(5000) + 100;
			// 最高人数
			long maxViewNum = roomMng.getMaxViewNum(iliveRoom.getLiveEvent().getLiveEventId(), iliveRoom.getRoomId());
			jsonObject.put("maxViewNum", maxViewNum);
			// 观看总数
			Long viewTotal = ApplicationCache.LiveEventUserCache.get(iliveRoom.getLiveEvent().getLiveEventId());
			if (viewTotal == null) {
				viewTotal = 0L;
			}
			if (viewTotal < maxViewNum) {
				viewTotal = maxViewNum + 1;
			}
			jsonObject.put("viewNumTotal", viewTotal);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "直播统计信息获取成功");
			resultJson.put("data", jsonObject);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
	}

	/**
	 * 继续直播
	 */
	public void continuePlay(final ILiveLiveRoom iliveRoom, Long userId, JSONObject resultJson,
			HttpServletRequest request, HttpServletResponse response, String terminalType) {
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		// 直播结束的继续直播
		if (liveStatus.equals(ILivePlayStatusConstant.PLAY_OVER)) {
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = iLiveServerAccessMethodMng
						.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Integer liveType = iliveRoom.getLiveType();
			if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType)) {
				Integer roomId = iliveRoom.getRoomId();
				String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
				String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
				String aliyunLiveModelName = ConfigUtils.get("aliyun_live_model_name");
				AliYunLiveTask aliYunLiveTask = new AliYunLiveTask(roomId, AliYunLiveTask.MODEL_START);
				new Thread(aliYunLiveTask).start();
				String pullAddr = "rtmp://" + aliyunLiveDomain + "/" + aliyunLiveAppname + "/" + roomId + "_host"
						+ aliyunLiveModelName;
				try {
					boolean pullStream = ILiveUMSMessageUtil.INSTANCE.pullStream(pullAddr, accessMethodBySeverGroupId,
							iliveRoom);
					logger.info("pullStream:" + pullStream);
				} catch (Exception e) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "流服务器交互失败");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					e.printStackTrace();
					return;
				}
			} else {
				try {
					boolean continuePlay = ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId,
							iliveRoom);
					logger.info("continuePlay:" + continuePlay);
				} catch (Exception e1) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "流服务器交互失败");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					e1.printStackTrace();
					return;
				}
			}
			
			final String converTaskLogo=ConfigUtils.get("conver_task_logo");
			if(iliveRoom.getOpenLogoSwitch()!=null&&iliveRoom.getOpenLogoSwitch()==1) {
				if(iliveRoom.getConvertTaskId()!=null&&iliveRoom.getConvertTaskId()>0) {
					
				}else {
					try {
						
			          //创建一个SAXBuilder对象
			              SAXBuilder saxBuilder = new SAXBuilder();            
			            //读取prop.xml资源
			              Document Doc = saxBuilder.build(request.getSession().getServletContext().getRealPath("/temp")+"/xml/4.xml");
			            //获取根元素(prop)
			            Element Root = Doc.getRootElement();
			            
			            Element name=  Root.getChild("name");
			            name.setText(iliveRoom.getLiveEvent().getLiveTitle());
			            Element uri=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("uri");
			           
			            uri.setText(iliveRoom.getConvertLogo());
			            Element rtmpuri=  Root.getChild("inputs").getChild("network").getChild("uri");
			            
			            String pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
			    				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + iliveRoom.getRoomId() + "_tzwj_5000k";
			    		
			            String[] addr= pushStreamAddr.split("_");
			            rtmpuri.setText(addr[0]+"o_"+addr[1]+"_"+addr[2]);
			            Element Top=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("top");
			            Top.setText(iliveRoom.getLogoWidth()+"");
			            Element Left=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("left");
			            Left.setText(iliveRoom.getLogoHight()+"");
			            Element rtmpuri1=  Root.getChild("outputgroups").getChild("flashstreaming").getChild("uri");
			            rtmpuri1.setText(pushStreamAddr);
			            //格式化输出xml文件字符串
						Format format = Format.getCompactFormat();
						format.setEncoding("UTF-8");
						//这行保证输出后的xml的格式
						format.setIndent(" ");
						// 创建xml输出流操作类
						XMLOutputter xmlout = new XMLOutputter(format);
						ByteArrayOutputStream byteRsp = new ByteArrayOutputStream();
						xmlout.output(Doc, byteRsp);
						String str = byteRsp.toString("utf-8");
			            String data1=HttpUtils.sendStr(converTaskLogo+"/api/task", "POST", str);
			            Integer taskId=getTaskId(data1);
			            String res  =HttpUtils.sendStr(converTaskLogo+"/api/task/"+taskId+"/start", "PUT", "");
			            if(taskId!=null) {
			            	iliveRoom.setConvertTaskId(taskId);
			            }
			            
					} catch (JDOMException | IOException e) {
						resultJson.put("message", "创建logo转码任务失败");
						e.printStackTrace();
					}
				}
				
			}else {
				if(iliveRoom.getConvertTaskId()!=null&&iliveRoom.getConvertTaskId()>0) {

					try {
						String data1=HttpUtils.sendStr(converTaskLogo+"/api/task/"+iliveRoom.getConvertTaskId()+"/stop", "PUT", "");
						
			            	ConvertThread thread =new ConvertThread(iliveRoom.getRoomId(), roomMng, iliveRoom.getConvertTaskId());
			            	thread.run();
				           
				           
					} catch (Exception e) {
						resultJson.put("message", "删除logo转码任务失败");
						e.printStackTrace();
					}
				}
				
			}
			
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			long oldLiveEventId = liveEvent.getLiveEventId();
			ILiveEvent iLiveEventNew = new ILiveEvent();
			try {
				BeanUtilsExt.copyProperties(iLiveEventNew, liveEvent);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			iLiveEventNew.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
			Timestamp startTmp = new Timestamp(System.currentTimeMillis());
			iLiveEventNew.setLiveStartTime(startTmp);
			iLiveEventNew.setLiveEndTime(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
			iLiveEventNew.setRecordStartTime(startTmp);
			iLiveEventNew.setRealStartTime(startTmp);
			// 2处理表单
			Integer formId = liveEvent.getFormId();
			if (formId > 0) {
				try {
					BBSDiyform diyfrom = bbsDiyformMng.getDiyfromById(formId);
					BBSDiyform newForm = this.convertDiyForm2NewForm(diyfrom);
					newForm = bbsDiyformMng.save(newForm);
					formId = newForm.getDiyformId();
					Set<BBSDiymodel> bbsDiymodels = diyfrom.getBbsDiymodels();
					if (bbsDiymodels != null) {
						Set<BBSDiymodel> newModelSet = new HashSet<>();
						BBSDiymodel newModel = null;
						for (BBSDiymodel model : bbsDiymodels) {
							newModel = this.convertModel2NewModel(model, newForm);
							newModel = bbsDiymodelMng.save(newModel);
							newModelSet.add(newModel);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.toString());
				}
			}
			iLiveEventNew.setFormId(formId);
			Long saveIliveMng = iLiveEventMng.saveIliveMng(iLiveEventNew);
			// 保存直播菜单
			List<PageDecorate> list = pageDecorateMng.getList(oldLiveEventId);
			if (list != null) {
				for (PageDecorate pd : list) {
					PageDecorate pdNew = new PageDecorate();
					try {
						BeanUtilsExt.copyProperties(pdNew, pd);
						pdNew.setLiveEventId(saveIliveMng);
						pageDecorateMng.addPageDecorateInit(pdNew);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			// 3处理白名单
			viewWhiteMng.updateEventId(oldLiveEventId,saveIliveMng);
			iLiveEventNew.setLiveEventId(saveIliveMng);
			iliveRoom.setLiveEvent(iLiveEventNew);
			roomMng.update(iliveRoom);
			String realIpAddr = IPUtils.getRealIpAddr(request);
			ILiveEnterprise iLiveEnterPrise = ILiveEnterpriseMng.getILiveEnterpriseById(iliveRoom.getEnterpriseId());
			iliveRoom.getLiveEvent().setEnterprise(iLiveEnterPrise);
			ILiveUserViewStatics.INSTANCE.startLive(iliveRoom, realIpAddr, terminalType);
			/**
			 * 获取直播间推流地址
			 */
			String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + "/live/live"
					+ iliveRoom.getRoomId() + "/5000k/tzwj_video.m3u8";
			iliveRoom.setHlsAddr(pushStreamAddr);
			RoomNoticeUtil.nptice(iliveRoom);
			try {
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				Long nowNum = 0L;
				if (userListMap == null) {
				} else {
					ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(iliveRoom.getRoomId());
					if (concurrentHashMap != null && !concurrentHashMap.isEmpty()) {
						nowNum = (long) concurrentHashMap.size();
					}
				}
				iLiveRoomStaticsMng.initRoom(iLiveEventNew.getLiveEventId(), nowNum);
				ApplicationCache.LiveEventUserCache.put(iLiveEventNew.getLiveEventId(), nowNum);
				ApplicationCache.getOnlineNumber().remove(iliveRoom.getRoomId());
				roomMng.startTask(iliveRoom.getLiveEvent().getLiveEventId(), iliveRoom.getRoomId());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.toString());
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("liveStatus", liveEvent.getLiveStatus());
			resultJson.put("data", jsonObject);
		}
		// 暂停结束后的继续直播
		else if (liveStatus.equals(ILivePlayStatusConstant.PAUSE_ING)) {
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
			liveEvent.setLastPauseStartTime(new Timestamp(System.currentTimeMillis()));
			liveEvent.setRecordStartTime(new Timestamp(System.currentTimeMillis()));
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			/**
			 * 获取直播间推流地址
			 */
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = iLiveServerAccessMethodMng
						.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + ":"
					+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + iliveRoom.getRoomId()
					+ "/5000k/tzwj_video.m3u8";
			iliveRoom.setHlsAddr(pushStreamAddr);
			RoomNoticeUtil.nptice(iliveRoom);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("liveStatus", liveEvent.getLiveStatus());
			resultJson.put("data", jsonObject);
		} else {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合继续直播标准");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	private static final String HTTP_PROTOCAL = "http://";

	/**
	 * 暂停直播
	 */
	public void pausePlay(final ILiveLiveRoom iliveRoom, final Long userId, JSONObject resultJson,
			HttpServletRequest request, HttpServletResponse response) {
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		final int roomId = iliveRoom.getRoomId();
		if (!liveStatus.equals(ILivePlayStatusConstant.PLAY_ING)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合暂停标准");
			resultJson.put("data", new JSONObject());
		} else {
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PAUSE_ING);
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			RoomNoticeUtil.nptice(iliveRoom);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("liveStatus", liveEvent.getLiveStatus());
			resultJson.put("data", jsonObject);
			//与计费系统交互计算时长
			Timestamp lastStartTime=null;
			if(liveEvent.getLastPauseStartTime()!=null){
				lastStartTime=liveEvent.getLastPauseStartTime();
			}else{
				lastStartTime=liveEvent.getRealStartTime();
			}
			final Long userIdLong = iliveRoom.getManagerId();
			final Integer enterpriseId1 = iliveRoom.getEnterpriseId();
			ILiveEnterprise iLiveEnterPrise1 = ILiveEnterpriseMng.getILiveEnterpriseById(enterpriseId1);
			Integer certStatus = null;
			if(iLiveEnterPrise1!=null){
				certStatus = iLiveEnterPrise1.getCertStatus();
			}
			Long useValue=(liveEvent.getLastPauseStopTime().getTime()-lastStartTime.getTime())/1000;
			
			EnterpriseUtil.openEnterprise(enterpriseId1,EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration,useValue.toString(),certStatus);
			
			final Integer enterpriseId = iliveRoom.getEnterpriseId();
			new Thread(new Runnable() {
				@Override
				public void run() {
					Integer serverGroupId = iliveRoom.getServerGroupId();
					ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
					String postUrl = HTTP_PROTOCAL + serverAccessMethod.getHttp_address() + ":"
							+ serverAccessMethod.getLivemsport() + "/livems/servlet/LiveServlet";
					String mountName = "live" + roomId;
					int length = (int) (System.currentTimeMillis()
							- iliveRoom.getLiveEvent().getRecordStartTime().getTime()) / 1000;
					int vodGroupId = Integer.parseInt(ConfigUtils.get("defaultVodServerGroupId"));
					String common = "";
					try {
						common = "?function=RecordLive&mountName=" + mountName + "&startTime="
								+ URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
										.format(iliveRoom.getLiveEvent().getRecordStartTime()), "UTF-8")
								+ "&length=" + length + "&destGroupId=" + vodGroupId;
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					postUrl = postUrl + common;
					String downloadUrl = PostMan.downloadUrl(postUrl);
					if (downloadUrl != null) {
						String trimDownloadUrl = downloadUrl.trim();
						String relativePath = StringUtil.getTagValue(trimDownloadUrl, "ret");
						ILiveMediaFile liveMediaFile = new ILiveMediaFile();
						liveMediaFile.setMediaFileName(
								iliveRoom.getLiveEvent().getLiveTitle() + "-" + new SimpleDateFormat("yyyyMMddHHmmss")
										.format(iliveRoom.getLiveEvent().getRecordStartTime()));
						liveMediaFile.setFilePath(relativePath);
						liveMediaFile.setCreateStartTime(iliveRoom.getLiveEvent().getRecordStartTime());
						liveMediaFile.setMediaCreateTime(new Timestamp(System.currentTimeMillis()));
						liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
						ILiveServerAccessMethod vodAccessMethod = iLiveServerAccessMethodMng
								.getAccessMethodBySeverGroupId(vodGroupId);
						liveMediaFile.setServerMountId(vodAccessMethod.getMountInfo().getServer_mount_id());
						liveMediaFile.setCreateType(0);
						liveMediaFile.setDuration(length);
						liveMediaFile.setFileType(1);
						liveMediaFile.setOnlineFlag(1);
						liveMediaFile.setUserId(userId);
						liveMediaFile.setDelFlag(0);
						// 通过用户拿到企业
						liveMediaFile.setEnterpriseId((long) enterpriseId);
						liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
						liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
						liveMediaFile.setMediaInfoDealState(0);
						liveMediaFile.setMediaInfoStatus(0);
						liveMediaFile.setAllowComment(1);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Long saveIliveMediaFile = iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
						iLiveVideoHisotryMng.saveVideoHistory(roomId, saveIliveMediaFile, userId);
					}
				}
			}).start();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 试用直播间接口 点击我要试用
	 */
	@RequestMapping(value = "/tryuse.jspx")
	public void tryUseLiveRoom(Long userId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
		if (iLiveManager == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "试用用户不存在");
			resultJson.put("data", new JSONObject());
		} else {
			Integer certStatus = iLiveManager.getCertStatus();
			if (certStatus == 1 || certStatus == 0) {
				// 准备试用
				try {
					Integer enterPriseId= roomMng.initRoom(iLiveManager);
					roomMng.initMeet(iLiveManager, enterPriseId, request);
					ILiveManager iLiveManagerRefresh = iLiveManagerMng.getILiveManager(userId);
					ILiveEnterprise enterPrise = iLiveManagerRefresh.getEnterPrise();
					IliveAppRetInfo retInfo = this.buildAppRet(iLiveManagerRefresh, enterPrise);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "试用成功");
					resultJson.put("data", new JSONObject(retInfo));
				} catch (Exception e) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "试用失败");
					resultJson.put("data", new JSONObject());
					e.printStackTrace();
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户不是属于可以试用的用户");
				resultJson.put("data", new JSONObject());
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 认证提交企业信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/enterprise/create.jspx")
	public void buildEnterpriseSimpleInfo(AppCertEnterprise certEnterprise, Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		ILiveManager iLiveManagerRefresh = iLiveManagerMng.getILiveManager(userId);
		JSONObject resultJson = new JSONObject();
		if (iLiveManagerRefresh == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户不是属于可以试用的用户");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} else {
			try {
				ILiveEnterprise iliveEnterprise = new ILiveEnterprise();
				iliveEnterprise.setEnterpriseName(certEnterprise.getEnterpriseName());
				iliveEnterprise.setEnterpriseType(certEnterprise.getEnterpriseType());
				iliveEnterprise.setEnterpriseInfo(certEnterprise.getEnterpriseInfo());
				iliveEnterprise.setRoad(certEnterprise.getRoad());
				iliveEnterprise.setEnterpriseRegName(certEnterprise.getEnterpriseRegName());
				iliveEnterprise.setEnterpriseRegNo(certEnterprise.getEnterpriseRegNo());
				iliveEnterprise.setEnterpriseLicence(certEnterprise.getEnterpriseLicence());
				iliveEnterprise.setContactPhone(certEnterprise.getContactPhone());
				iliveEnterprise.setContactName(certEnterprise.getContactName());
				iliveEnterprise.setIdCard(certEnterprise.getIdCard());
				iliveEnterprise.setIdCardImg(certEnterprise.getIdCardImg());
				iliveEnterprise.setEnterprisePhone(certEnterprise.getEnterprisePhone());
				iliveEnterprise.setEnterpriseEmail(certEnterprise.getEnterpriseEmail());
				iliveEnterprise.setEnterpriseImg(certEnterprise.getEnterpriseImg());
				iliveEnterprise.setApplyTime(new Timestamp(System.currentTimeMillis()));
				ILiveEnterpriseMng.saveEnterpriseForPhone(iliveEnterprise, iLiveManagerRefresh);
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
				ILiveEnterprise enterPrise = iLiveManagerRefresh.getEnterPrise();
				IliveAppRetInfo retInfo = this.buildAppRet(iLiveManager, enterPrise);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "提交成功");
				resultJson.put("data", new JSONObject(retInfo));
			} catch (Exception e) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "提交失败");
				resultJson.put("data", new JSONObject());
				logger.error(e.toString());
			}
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
	}

	/**
	 * 上传图片
	 * 
	 * @return
	 */
	@RequestMapping(value = "file/upload.jspx")
	public void uploadFile(@RequestParam("file") CommonsMultipartFile file, Integer userId, HttpServletRequest request,
			String fileType, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (fileType == null) {
			json.put("code", 0);
			json.put("message", "fileType不需存在");
			json.put("data", new JSONObject());
			ResponseUtils.renderJson(response, json.toString());
			return;
		}
		try {
			String ext = FileUtils.getFileExt(file.getOriginalFilename());
			String tempFileName = System.currentTimeMillis() + "." + ext;
			String realPath = request.getSession().getServletContext().getRealPath("/temp");
			File tempFile = createTempFile(realPath + "/" + tempFileName, file);
			String filePath = FileUtils.getTimeFilePath(tempFileName);
			if (null != fileType && UPLOAD_FILE_TYPE_IMAGE.equalsIgnoreCase(fileType)) {
				boolean result = false;
				ILiveUploadServer uploadServer = iLiveUploadServerMng.getDefaultServer();
				if (uploadServer != null) {
					FileInputStream in = new FileInputStream(tempFile);
					result = uploadServer.upload(filePath, in);
				}
				String httpPrefix = uploadServer.getHttpUrl();
				String suffix = uploadServer.getFtpPath() + filePath;
				suffix = suffix.replace("//", "/");
				if (tempFile.exists()) {
					tempFile.delete();
				}
				if (result) {
					json.put("code", 1);
					json.put("message", "图片上传成功");
					JSONObject dataObj = new JSONObject();
					dataObj.put("httpUrl", httpPrefix + suffix);
					json.put("data", dataObj);
					ResponseUtils.renderJson(response, json.toString());
					return;
				} else {
					json.put("code", 0);
					json.put("message", "图片转存FTP失败");
					json.put("data", new JSONObject());
					ResponseUtils.renderJson(response, json.toString());
					return;
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
			try {
				json.put("code", 0);
				json.put("message", "图片上传失败");
				json.put("data", new JSONObject());
				ResponseUtils.renderJson(response, json.toString());
				return;
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 简单认证流程
	 */
	@RequestMapping(value = "cert/simple.jspx")
	public void simpleCert(AppSimpleCertInfo simpleCertInfo, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		String phoneNum = simpleCertInfo.getPhoneNum();
		if (phoneNum == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "手机号不能为空");
			resultJson.put("data", new JSONObject());
		} else if (simpleCertInfo.getUserName() == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户账号不能为空");
			resultJson.put("data", new JSONObject());
		} else if (simpleCertInfo.getVpassword() == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "验证码不能为空");
			resultJson.put("data", new JSONObject());
		} else if (simpleCertInfo.getPassword() == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "密码未填写");
			resultJson.put("data", new JSONObject());
		} else if (simpleCertInfo.getConfirmPassword() == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "确认密码未填写");
			resultJson.put("data", new JSONObject());
		} else if (!simpleCertInfo.getPassword().equals(simpleCertInfo.getConfirmPassword())) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "两次密码不一致");
			resultJson.put("data", new JSONObject());
		} else {
			Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "simplecert_" + phoneNum);
			if (cacheInfo == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码不通过");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else if (cacheInfo.isExpired()) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码已过期,请重新发送");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else if (!simpleCertInfo.getVpassword().equals(cacheInfo.getValue())) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码不通过");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveManager managerUser = iLiveManagerMng.getILiveManagerByUserName(simpleCertInfo.getUserName());
			if (managerUser == null) {
				ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
				if (iLiveMangerByMobile != null) {
					iLiveMangerByMobile.setUserName(simpleCertInfo.getUserName());
					iLiveMangerByMobile.setSimpleEnterpriseName(simpleCertInfo.getSimpleEnterpriseName());
					String password = md5Passwd(simpleCertInfo.getPassword(), iLiveMangerByMobile.getSalt());
					iLiveMangerByMobile.setUserPwd(password);
					iLiveMangerByMobile.setCertStatus(1);
					iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
					ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
					IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise);
					resultJson.put("data", new JSONObject(retInfo));
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "信息完善成功");
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户不存在");
					resultJson.put("data", new JSONObject());
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户账号已被注册");
				resultJson.put("data", new JSONObject());
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 获取企业直播间
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/liverooms.jspx")
	public void getRoomListForOperator(String keyWord,Integer roomType, Integer liveStatus, Long userId, Integer pageNo,
			Integer pageSize, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean userBean = ILiveUtils.getAppUser(request);
		if (userBean == null) {
			resultJson.put("code", UN_LOGIN);
			resultJson.put("message", "用户未登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			Pagination pager = roomMng.getPagerForOperator(userId, pageNo, pageSize,liveStatus,roomType);
			List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) pager.getList();
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			if (list != null) {
				for (ILiveLiveRoom room : list) {
					Integer serverGroupId = room.getServerGroupId();
					ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
					String rtmpAddr=null;
					if(room.getOpenLogoSwitch()!=null&&room.getOpenLogoSwitch()==1) {
						rtmpAddr = "rtmp://" + serverAccessMethod.getOrgLiveHttpUrl() + ":"
								+ serverAccessMethod.getUmsport() + "/live/live" + room.getRoomId() + "o_tzwj_5000k";
						room.setRtmpAddr(rtmpAddr);
					}else {
						rtmpAddr = "rtmp://" + serverAccessMethod.getOrgLiveHttpUrl() + ":"
								+ serverAccessMethod.getUmsport() + "/live/live" + room.getRoomId() + "_tzwj_5000k";
						room.setRtmpAddr(rtmpAddr);
					}
					
					String hlsAddr="http://"+serverAccessMethod.getHttp_address()+"/live/live" + room.getRoomId()+"/5000k/tzwj_video.m3u8";
					room.setHlsPlayUrl(hlsAddr);
					String rtmpPlay="rtmp://"+serverAccessMethod.getCdnLiveHttpUrl()+":"
							+serverAccessMethod.getUmsport()+"/live/live" + room.getRoomId() + "_tzwj_5000k";
					room.setRtmpPlayUrl(rtmpPlay);
					JSONObject putNewLiveInJson = room.putNewLiveInJson(room);
					jsonList.add(putNewLiveInJson);
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取直播间列表成功");
			resultJson.put("data", jsonList);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取直播间列表失败");
			resultJson.put("data", "");
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 获取企业直播间
	 */
	@RequestMapping(value = "/liveMeet.jspx")
	public void getMeetInfoById(Integer roomId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean userBean = ILiveUtils.getAppUser(request);
		if (userBean == null) {
			resultJson.put("code", UN_LOGIN);
			resultJson.put("message", "用户未登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			ILiveLiveRoom room = roomMng.getIliveRoom(roomId);
			
					Integer serverGroupId = room.getServerGroupId();
					ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
					String rtmpAddr = "rtmp://" + serverAccessMethod.getOrgLiveHttpUrl() + ":"
							+ serverAccessMethod.getUmsport() + "/live/live" + room.getRoomId() + "_tzwj_5000k";
					room.setRtmpAddr(rtmpAddr);
					String hlsAddr="http://"+serverAccessMethod.getHttp_address()+"/live/live" + room.getRoomId()+"/5000k/tzwj_video.m3u8";
					room.setHlsPlayUrl(hlsAddr);
					String rtmpPlay="rtmp://"+serverAccessMethod.getCdnLiveHttpUrl()+":"
							+serverAccessMethod.getUmsport()+"/live/live" + room.getRoomId() + "_tzwj_5000k";
					room.setRtmpPlayUrl(rtmpPlay);
					JSONObject putNewLiveInJson = room.putNewLiveInJson(room);
					
				
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取会议信息成功");
			resultJson.put("data", putNewLiveInJson);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取会议失败");
			resultJson.put("data", "");
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 直播场次统计
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "events.jspx")
	public void liveEvent(HttpServletRequest request, HttpServletResponse response, Integer roomId, Integer pageNo,
			Integer pageSize) {
		pageNo = pageNo == null ? 1 : pageNo;
		pageSize = pageSize == null ? 15 : pageSize;
		List<LiveEventsVo> eventsVoList = null;
		JSONObject resultJson = new JSONObject();
		try {
			eventsVoList = iLiveEventMng.getLiveEventsByRoomId(roomId, pageNo, pageSize);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取场次列表成功");
			resultJson.put("data", eventsVoList);
			ResponseUtils.renderJson(request, response, resultJson.toString());
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取场次列表失败");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			e.printStackTrace();
		}
	}
	
public static Integer getTaskId(String xml) {
		
		try {
			//创建一个新的字符串
	        StringReader read = new StringReader(xml);
	        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
	        InputSource source = new InputSource(read);
	        //创建一个新的SAXBuilder
	        SAXBuilder sb = new SAXBuilder();
	      //通过输入源构造一个Document
	        Document doc= sb.build(source);
			 //取的根元素
	        Element root = doc.getRootElement();
	        
	        return Integer.parseInt(root.getAttributeValue("id"));
		} catch (JDOMException | IOException e) {
			
			e.printStackTrace();
		}
		return null;
       
	}

	@RequestMapping(value = "enterprise/types.jspx")
	public void getEnterpriseType(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("type", "1");
		map.put("name", "IT");
		list.add(map);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("type", "2");
		map2.put("name", "金融");
		list.add(map2);
		Map<String, Object> map3 = new HashMap<>();
		map3.put("type", "3");
		map3.put("name", "教育");
		list.add(map3);
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		if (list != null) {
			for (Map<String, Object> mapBean : list) {
				JSONObject putNewLiveInJson = new JSONObject(mapBean);
				jsonList.add(putNewLiveInJson);
			}
		}
		JSONObject resultJson = new JSONObject();
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "获取类型成功");
		resultJson.put("data", jsonList);
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	private static final String hashAlgorithmName = "MD5";

	private String md5Passwd(String password, String saltOrg) {
		int hashIterations = 1024;
		Object salt = new Md5Hash(saltOrg);
		Object result = new SimpleHash(hashAlgorithmName, password, salt, hashIterations);
		String retpassword = result.toString();
		return retpassword;
	}

	/**
	 * 构建成appRet 企业对象
	 * 
	 * @param iLiveMangerByMobile
	 * @param enterPrise
	 * @return
	 */
	private IliveAppRetInfo buildAppRet(ILiveManager iLiveMangerByMobile, ILiveEnterprise enterPrise) {
		IliveAppRetInfo retInfo = new IliveAppRetInfo();
		retInfo.setUserId(iLiveMangerByMobile.getUserId());
		retInfo.setMobile(iLiveMangerByMobile.getMobile());
		retInfo.setNailName(iLiveMangerByMobile.getNailName());
		retInfo.setJpushId(StringPatternUtil.convertEmpty(iLiveMangerByMobile.getJpushId()));
		retInfo.setUserName(StringPatternUtil.convertEmpty(iLiveMangerByMobile.getUserName()));
		retInfo.setCertStatus(iLiveMangerByMobile.getCertStatus() == null ? 0 : iLiveMangerByMobile.getCertStatus());
		retInfo.setPhoto(iLiveMangerByMobile.getUserImg() == null ? "" : iLiveMangerByMobile.getUserImg());
		if (enterPrise != null) {
			ILiveAppEnterprise enterprise = new ILiveAppEnterprise();
			enterprise.setCertStatus(enterPrise.getCertStatus());
			enterprise.setEnterpriseId(enterPrise.getEnterpriseId());
			enterprise.setEnterpriseImg(StringPatternUtil.convertEmpty(enterPrise.getEnterpriseImg()));
			enterprise.setEnterpriseName(enterPrise.getEnterpriseName());
			enterprise.setEnterpriseDesc(StringPatternUtil.convertEmpty(enterprise.getEnterpriseDesc()));
			enterprise.setHomePageUrl(StringPatternUtil.convertEmpty(enterprise.getHomePageUrl()));
			retInfo.setEnterprise(enterprise);
		}
		return retInfo;
	}

	/**
	 * 构建临时文件
	 * 
	 * @param tempFilePath
	 * @param file
	 * @return
	 */
	private File createTempFile(String tempFilePath, CommonsMultipartFile file) {
		File tempFile = new File(tempFilePath);
		if (null != tempFile && !tempFile.exists()) {
			tempFile.mkdirs();
		}
		try {
			file.transferTo(tempFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempFile;
	}

	private BBSDiymodel convertModel2NewModel(BBSDiymodel model, BBSDiyform bbsDiyform) {
		BBSDiymodel newModel = new BBSDiymodel();
		newModel.setOptValue(model.getOptValue());
		newModel.setDiymodelType(model.getDiymodelType());
		newModel.setDiymodelTitle(model.getDiymodelTitle());
		newModel.setDiyOrder(model.getDiyOrder());
		newModel.setBbsDiyform(bbsDiyform);
		newModel.setNeedAnswer(model.getNeedAnswer());
		newModel.setDiymodelKey(model.getDiymodelKey());
		return newModel;
	}

	private BBSDiyform convertDiyForm2NewForm(BBSDiyform diyfrom) {
		BBSDiyform newForm = new BBSDiyform();
		newForm.setDiyformName(diyfrom.getDiyformName());
		newForm.setCreateTime(new Timestamp(System.currentTimeMillis()));
		newForm.setDelFlag(0);
		return newForm;
	}
}
