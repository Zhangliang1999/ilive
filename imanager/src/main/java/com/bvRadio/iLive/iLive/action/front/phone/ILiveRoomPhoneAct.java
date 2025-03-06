package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;
import static com.bvRadio.iLive.iLive.Constants.UPLOAD_FILE_TYPE_IMAGE;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppCertEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.AppSimpleCertInfo;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.action.front.vo.RoomCreateVo;
import com.bvRadio.iLive.iLive.action.front.vo.RoomEditVo;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.ILiveEventVo;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.RoomNoticeUtil;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.util.StringUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.PostMan;
import com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng;

@Controller
@RequestMapping(value = "livephone/room")
public class ILiveRoomPhoneAct {

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

	/**
	 * 获取直播间列表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/roomlist.jspx")
	public void getRoomList(String roomName, Integer roomStatus, Long userId, Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			Pagination pager = roomMng.getPager(roomName, roomStatus, userId, pageNo, pageSize);
			List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) pager.getList();
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			for (ILiveLiveRoom room : list) {
				JSONObject putNewLiveInJson = room.putNewLiveInJson(room);
				jsonList.add(putNewLiveInJson);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取直播间列表成功");
			resultJson.put("data", jsonList);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取直播间列表失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
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
	@RequestMapping(value = "/create.jspx")
	public void roomCreate(RoomCreateVo roomCreateVo, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			boolean saveNewBean = roomMng.saveNewBean(roomCreateVo);
			if (saveNewBean) {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "生成直播间成功");
				resultJson.put("data", new JSONObject());
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "生成直播间失败");
				resultJson.put("data", new JSONObject());
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "生成直播间失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
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
	@RequestMapping(value = "/edit.jspx")
	public void roomEdit(RoomEditVo roomEditVo, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			boolean editNewBean = roomMng.editNewBean(roomEditVo);
			if (editNewBean) {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "修改直播间成功");
				resultJson.put("data", new JSONObject());
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "修改直播间失败");
				resultJson.put("data", new JSONObject());
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "修改直播间失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
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
		try {
			ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
			if (iliveRoom == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播房间不存在");
				resultJson.put("data", new JSONObject());
			} else if (iliveRoom.getOpenStatus() == 0) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间已经关闭");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else {
				if (!iliveRoom.getManagerId().equals(userId)) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "非本人创建的房间禁止进入");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
				List<PageDecorate> pageRecordList = pageDecorateMng.getList(iliveRoom.getLiveEvent().getLiveEventId());
				iliveRoom.getLiveEvent().setPageRecordList(pageRecordList);
				Integer serverGroupId = iliveRoom.getServerGroupId();
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
						+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
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
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 直播控制
	 */
	@RequestMapping(value = "/liveControl.jspx")
	public void liveControl(Integer playType, Integer roomId,Integer liveStatus, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (liveStatus != null) {
			ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
			if (liveStatus == 1) {
				this.startPlay(iliveRoom, resultJson, request, response);
			} else if (liveStatus == 2) {
				this.pausePlay(iliveRoom, resultJson, request, response);
			} else if (liveStatus == 3) {
				this.stopPlay(iliveRoom, resultJson, request, response);
			} else if (liveStatus == 4) {
				this.continuePlay(iliveRoom, resultJson, request, response);
			} else {
				// TODO
			}
		}
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
			ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
			if (iliveRoom != null) {
				iliveRoom.setOpenStatus(openStatus);
				roomMng.update(iliveRoom);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "操作成功");
				resultJson.put("data", "");
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "操作失败");
				resultJson.put("data", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作失败");
			resultJson.put("data", "");
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
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(roomId);
			if (userMap != null) {
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
					iLiveEventVo.setEstoppleType(interactionStatus);
					iLiveEventVo.setLiveStatus(iLiveEvent.getLiveStatus());
					iLiveEventVo.setPlayType(iLiveEvent.getPlayType());
					message.setiLiveEventVo(iLiveEventVo);
					message.setLiveRoomId(roomId);
					userMsgList.add(message);
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
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 开始直播
	 */
	public void startPlay(ILiveLiveRoom iliveRoom, JSONObject resultJson, HttpServletRequest request,
			HttpServletResponse response) {
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		System.out.println("startPlay之前liveStatus====================>"+liveStatus);
		// 根据直播状态
		if (liveStatus.equals(ILivePlayStatusConstant.UN_START)) {
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
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
			resultJson.put("data", new JSONObject());
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
	public void stopPlay(final ILiveLiveRoom iliveRoom, JSONObject resultJson, HttpServletRequest request,
			HttpServletResponse response) {
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		System.out.println("stopPlay之前liveStatus====================>"+liveStatus);
		final int roomId = iliveRoom.getRoomId();
		final int serverGroupId = iliveRoom.getServerGroupId();
		if (liveStatus.equals(ILivePlayStatusConstant.PLAY_OVER)
				|| liveStatus.equals(ILivePlayStatusConstant.UN_START)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合结束标准");
			resultJson.put("data", "");
		} else {
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
			liveEvent.setLiveEndTime(new Timestamp(System.currentTimeMillis()));
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			RoomNoticeUtil.nptice(iliveRoom);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			resultJson.put("data", "");
			final Integer enterpriseId = iliveRoom.getEnterpriseId();
			if (ILivePlayStatusConstant.PAUSE_ING != liveEvent.getLiveStatus()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
								.getAccessMethodBySeverGroupId(serverGroupId);
						String postUrl = HTTP_PROTOCAL + serverAccessMethod.getHttp_address() + ":"
								+ serverAccessMethod.getLivemsport() + "/livems/servlet/LiveServlet";
						String mountName = "live" + roomId;
						int vodGroupId = 102;
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
							liveMediaFile.setDuration(length);
							liveMediaFile.setFileType(1);
							liveMediaFile.setOnlineFlag(1);
							liveMediaFile.setMediaInfoDealState(0);
							// 通过用户拿到企业
							liveMediaFile.setEnterpriseId((long) enterpriseId);
							liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
							liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
						}
					}
				}).start();
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 继续直播
	 */
	public void continuePlay(final ILiveLiveRoom iliveRoom, JSONObject resultJson, HttpServletRequest request,
			HttpServletResponse response) {
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		System.out.println("continuePlay之前liveStatus====================>"+liveStatus);
		// 直播结束的继续直播
		if (liveStatus.equals(ILivePlayStatusConstant.PLAY_OVER)) {
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			ILiveEvent iLiveEventNew = new ILiveEvent();
			try {
				BeanUtils.copyProperties(iLiveEventNew, liveEvent);
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
			Long saveIliveMng = iLiveEventMng.saveIliveMng(iLiveEventNew);
			iLiveEventNew.setLiveEventId(saveIliveMng);
			iliveRoom.setLiveEvent(iLiveEventNew);
			roomMng.update(iliveRoom);
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
			resultJson.put("data", new JSONObject());
		}
		// 暂停结束后的继续直播
		else if (liveStatus.equals(ILivePlayStatusConstant.PAUSE_ING)) {
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
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
			resultJson.put("data", new JSONObject());
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
	public void pausePlay(final ILiveLiveRoom iliveRoom, JSONObject resultJson, HttpServletRequest request,
			HttpServletResponse response) {
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		System.out.println("pausePlay之前liveStatus====================>"+liveStatus);
		final int roomId = iliveRoom.getRoomId();
		if (!liveStatus.equals(ILivePlayStatusConstant.PLAY_ING)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合暂停标准");
			resultJson.put("data", "");
		} else {
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PAUSE_ING);
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			RoomNoticeUtil.nptice(iliveRoom);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			resultJson.put("data", "");
			final Integer enterpriseId = iliveRoom.getEnterpriseId();
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Integer serverGroupId = iliveRoom.getServerGroupId();
					ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
					String postUrl = HTTP_PROTOCAL + serverAccessMethod.getHttp_address() + ":"
							+ serverAccessMethod.getLivemsport() + "/livems/servlet/LiveServlet";
					String mountName = "live" + roomId;
					int length = (int) (System.currentTimeMillis()
							- iliveRoom.getLiveEvent().getRecordStartTime().getTime()) / 1000;
					int vodGroupId = 102;
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
						// 通过用户拿到企业
						liveMediaFile.setEnterpriseId((long) enterpriseId);
						liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
						liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
						liveMediaFile.setMediaInfoDealState(0);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
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
			if (certStatus == 1) {
				// 准备试用
				try {
					roomMng.initRoom(iLiveManager);
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
			try {
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
			resultJson.put("message", "用户账号不能为空");
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
	public void getRoomListForOperator(String keyWord, Integer searchType, Long userId, Integer pageNo,
			Integer pageSize, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			Pagination pager = roomMng.getPagerForOperator(userId, pageNo, pageSize);
			List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) pager.getList();
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			if (list != null) {
				for (ILiveLiveRoom room : list) {
					Integer serverGroupId = room.getServerGroupId();
					ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
					String rtmpAddr = "rtmp://" + serverAccessMethod.getOrgLiveHttpUrl() + ":"
							+ serverAccessMethod.getUmsport() + "/live/live" + room.getRoomId() + "_tzwj_5000k";
					room.setRtmpAddr(rtmpAddr);
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
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
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
}
