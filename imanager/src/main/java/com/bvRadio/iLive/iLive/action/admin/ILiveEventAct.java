package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.constants.ILivePlayCtrType;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.ILivePlayTypeVo;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.util.RoomNoticeUtil;
import com.bvRadio.iLive.iLive.util.StringUtil;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.iLive.web.PostMan;

/**
 * 直播场次控制类
 * 
 * @author administrator
 */
@Controller
@RequestMapping("/liveevent")
public class ILiveEventAct {

	@Autowired
	private ILiveEventMng iLiveEventMng;

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

	@Autowired
	private ILivePageDecorateMng pageDecorateMng;

	@RequestMapping("document.do")
	public String mediaLib() {
		return "medialib/vedio";
	}

	@RequestMapping(value = "/update.do")
	public String updateLiveEventPlayType(ILiveEvent liveEvent) {
		ILiveEvent dbEvent = iLiveEventMng.selectILiveEventByID(liveEvent.getLiveEventId());
		iLiveEventMng.updateILiveEvent(dbEvent);
		return null;
	}

	@RequestMapping(value = "openSignup.do")
	public void openSignup(Integer roomId, Integer signUp, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		liveEvent.setOpenSignupSwitch(signUp);
		try {
			iLiveEventMng.updateILiveEvent(liveEvent);
			resultJson.put("status", "1");
		} catch (Exception e) {
			iLiveEventMng.updateILiveEvent(liveEvent);
			resultJson.put("status", "0");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	@RequestMapping(value = "/thirdstream.do")
	public String thirdStream(Integer roomId, ModelMap modelMap) {
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		modelMap.addAttribute("iLiveLiveRoom", iliveRoom);
		modelMap.addAttribute("leftActive", "1_2");
		modelMap.addAttribute("topActive", "1");
		return "live/config/thirdstream";
	}

	@RequestMapping(value = "/cyclefile.do")
	public String cyclefile(Integer roomId, ModelMap modelMap) {
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		modelMap.addAttribute("iLiveLiveRoom", iliveRoom);
		modelMap.addAttribute("leftActive", "1_3");
		modelMap.addAttribute("topActive", "1");
		return "live/config/cycle_file";
	}

	@RequestMapping(value = "/toolkit.do")
	public String toolkit(Integer roomId, ModelMap modelMap) {
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		modelMap.addAttribute("iLiveLiveRoom", iliveRoom);
		modelMap.addAttribute("leftActive", "1_4");
		modelMap.addAttribute("topActive", "1");
		return "live/tool/toolkit";
	}

	@RequestMapping(value = "playconfig.do")
	public String playconfig(Integer roomId, ModelMap modelMap) {
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		modelMap.addAttribute("iLiveLiveRoom", iliveRoom);
		modelMap.addAttribute("liveEvent", liveEvent);
		modelMap.addAttribute("roomId", roomId);
		modelMap.addAttribute("leftActive", "1_5");
		modelMap.addAttribute("topActive", "1");
		return "live/playconfig";
	}

	/**
	 * 修改切换直播类型的地址
	 * 
	 * @param playTypeVo
	 * @param response
	 */
	@RequestMapping(value = "/updatePlayTypeAddr.do")
	public void updatePlayType(ILivePlayTypeVo playTypeVo, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(playTypeVo.getLiveEventId());
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			// TODO: handle exception
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 修改切换直播类型的地址
	 * 
	 * @param playTypeVo
	 * @param response
	 */
	@RequestMapping(value = "/switchPlayType.do")
	public void updatePlayType(Long iLiveEventId, Integer playType, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(iLiveEventId);
			if (ILivePlayCtrType.PUSH_LIVE.equals(playType)) {
			} else if (ILivePlayCtrType.POOL_LIVE.equals(playType)) {
			}
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 开始直播
	 * 
	 * @param playType
	 * @param roomId
	 */
	@RequestMapping("live/start.do")
	public void startEventPlay(Integer playType, Integer roomId, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		// 根据直播状态
		if (liveStatus.equals(ILivePlayStatusConstant.UN_START)) {
			if (playType != null) {
				// 拉流直播
				if (playType.equals(ILivePlayCtrType.POOL_LIVE)) {
					iliveRoom.getLiveEvent().setPlayType(ILivePlayCtrType.POOL_LIVE);
				}
				// 列表直播
				else if (playType.equals(ILivePlayCtrType.LIST_LIVE)) {
					iliveRoom.getLiveEvent().setPlayType(ILivePlayCtrType.LIST_LIVE);
				} else {
					// 正常向流媒体服务器推流操作
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
							+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId
							+ "/5000k/tzwj_video.m3u8";
					iliveRoom.setHlsAddr(pushStreamAddr);
					RoomNoticeUtil.nptice(iliveRoom);
					resultJson.put("status", SUCCESS_STATUS);
					resultJson.put("message", "操作成功");
				}
			} else {
				ILiveEvent liveEvent = iliveRoom.getLiveEvent();
				liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
				iLiveEventMng.updateILiveEvent(liveEvent);
				iliveRoom.setLiveEvent(liveEvent);
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + ":"
						+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
				iliveRoom.setHlsAddr(pushStreamAddr);
				RoomNoticeUtil.nptice(iliveRoom);
				resultJson.put("status", SUCCESS_STATUS);
				resultJson.put("message", "操作成功");
			}
		} else {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合结束标准");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	private static final String HTTP_PROTOCAL = "http://";

	/**
	 * 结束直播
	 */
	@RequestMapping(value = "live/stop.do")
	public void stopEventPlay(Integer playType, final Integer roomId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		final ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		if (liveStatus.equals(ILivePlayStatusConstant.PLAY_OVER)
				|| liveStatus.equals(ILivePlayStatusConstant.UN_START)) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合结束标准");
		} else {
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
			liveEvent.setLiveEndTime(new Timestamp(System.currentTimeMillis()));
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			RoomNoticeUtil.nptice(iliveRoom);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			final Integer serverGroupId = iliveRoom.getServerGroupId();
			final UserBean userBean = ILiveUtils.getUser(request);
			String userId = userBean.getUserId();
			final Long userIdLong = Long.parseLong(userId);
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
								liveMediaFile.setServerMountId(vodAccessMethod.getMountInfo().getServer_mount_id());
								liveMediaFile.setCreateType(0);
								liveMediaFile.setDuration(length);
								liveMediaFile.setFileType(1);
								liveMediaFile.setOnlineFlag(1);
								// 通过用户拿到企业
								liveMediaFile.setEnterpriseId((long) userBean.getEnterpriseId());
								liveMediaFile.setUserId(userIdLong);
								liveMediaFile.setMediaInfoStatus(0);
								liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
								liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
								liveMediaFile.setDelFlag(0);
								liveMediaFile.setMediaInfoDealState(0);
								try {
									Thread.sleep(15000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
							}

						}
					}
				}).start();
			}
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 暂停直播
	 */
	@RequestMapping(value = "live/pause.do")
	public void pauseEventPlay(Integer playType, final Integer roomId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		final ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		if (!liveStatus.equals(ILivePlayStatusConstant.PLAY_ING)) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合暂停标准");
		} else {
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PAUSE_ING);
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			RoomNoticeUtil.nptice(iliveRoom);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			final UserBean userBean = ILiveUtils.getUser(request);
			String userId = userBean.getUserId();
			final Long userIdLong = Long.parseLong(userId);
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
						if (!relativePath.trim().equals("")) {
							ILiveMediaFile liveMediaFile = new ILiveMediaFile();
							liveMediaFile.setMediaFileName(iliveRoom.getLiveEvent().getLiveTitle() + "-"
									+ new SimpleDateFormat("yyyyMMddHHmmss")
											.format(iliveRoom.getLiveEvent().getRecordStartTime()));
							liveMediaFile.setFilePath(relativePath);
							liveMediaFile.setCreateStartTime(iliveRoom.getLiveEvent().getRecordStartTime());
							liveMediaFile.setMediaCreateTime(new Timestamp(System.currentTimeMillis()));
							liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
							liveMediaFile.setServerMountId(serverAccessMethod.getMountInfo().getServer_mount_id());
							liveMediaFile.setCreateType(0);
							liveMediaFile.setDuration(length);
							liveMediaFile.setFileType(1);
							liveMediaFile.setOnlineFlag(1);
							liveMediaFile.setMediaInfoDealState(0);
							// 通过用户拿到企业
							liveMediaFile.setEnterpriseId((long) userBean.getEnterpriseId());
							liveMediaFile.setMediaInfoStatus(0);
							liveMediaFile.setUserId(userIdLong);
							liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
							liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
							liveMediaFile.setDelFlag(0);
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
						}
					}
				}
			}).start();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 继续直播
	 */
	@RequestMapping(value = "live/continue.do")
	public void continueEventPlay(Integer playType, Integer roomId, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		Integer serverGroupId = iliveRoom.getServerGroupId();
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
			List<PageDecorate> pageRecordList = pageDecorateMng.getList(iliveRoom.getLiveEvent().getLiveEventId());
			iLiveEventNew.setPageRecordList(pageRecordList);
			Timestamp startTmp = new Timestamp(System.currentTimeMillis());
			iLiveEventNew.setLiveStartTime(startTmp);
			iLiveEventNew.setLiveEndTime(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
			iLiveEventNew.setRecordStartTime(startTmp);
			Long saveIliveMng = iLiveEventMng.saveIliveMng(iLiveEventNew);
			iLiveEventNew.setLiveEventId(saveIliveMng);
			iliveRoom.setLiveEvent(iLiveEventNew);
			iLiveRoomMng.update(iliveRoom);
			/**
			 * 获取直播间推流地址
			 */
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = iLiveServerAccessMethodMng.getAccessMethodBySeverGroupId(serverGroupId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + ":"
					+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
			iliveRoom.setHlsAddr(pushStreamAddr);
			RoomNoticeUtil.nptice(iliveRoom);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
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
				accessMethodBySeverGroupId = iLiveServerAccessMethodMng.getAccessMethodBySeverGroupId(serverGroupId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + ":"
					+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
			iliveRoom.setHlsAddr(pushStreamAddr);
			RoomNoticeUtil.nptice(iliveRoom);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
		} else {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合继续直播标准");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

}
