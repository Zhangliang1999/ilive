package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.constants.ILivePlayCtrType;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveRandomRecordTask;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.ILivePlayTypeVo;
import com.bvRadio.iLive.iLive.manager.BBSDiyformMng;
import com.bvRadio.iLive.iLive.manager.BBSDiymodelMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveRandomRecordTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomStaticsMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveVideoHistoryMng;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;
import com.bvRadio.iLive.iLive.util.ILiveUMSMessageUtil;
import com.bvRadio.iLive.iLive.util.IPUtils;
import com.bvRadio.iLive.iLive.util.RoomNoticeUtil;
import com.bvRadio.iLive.iLive.util.StringUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
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
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

	@Autowired
	private ILivePageDecorateMng pageDecorateMng;

	@Autowired
	private BBSDiyformMng bbsDiyformMng;

	@Autowired
	private BBSDiymodelMng bbsDiymodelMng;

	@Autowired
	private ILiveVideoHistoryMng iLiveVideoHisotryMng;

	@Autowired
	private ILiveRandomRecordTaskMng iLiveRandomRecordTaskMng;

	@Autowired
	private ILiveRoomStaticsMng iLiveRoomStaticsMng;

	private static final String HTTP_PROTOCAL = "http://";

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

	@RequestMapping(value = "randomrecord/start.do")
	public void recordFileStart(Integer roomId, HttpServletRequest request, HttpServletResponse response) {
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getUser(request);
		// 直播结束
		if (liveStatus.intValue() == ILivePlayStatusConstant.UN_START) {
			resultJson.put("code", 0);
			resultJson.put("message", "启动收录失败,直播尚未开始");
		} else if (liveStatus.intValue() == ILivePlayStatusConstant.PLAY_OVER) {
			resultJson.put("code", 0);
			resultJson.put("message", "启动收录失败,直播已经结束");
		} else if (liveStatus.intValue() == ILivePlayStatusConstant.PAUSE_ING) {
			resultJson.put("code", 0);
			resultJson.put("message", "启动收录失败,直播暂停中");
		} else {
			
			ILiveRandomRecordTask task = iLiveRandomRecordTaskMng.getTaskByQuery(
					iliveRoom.getLiveEvent().getLiveEventId(), Long.parseLong(user.getUserId()),
					ILivePlayStatusConstant.PLAY_ING);
			// 没有建立过任务
			if (task == null) {
				// 创建个任务
				task = new ILiveRandomRecordTask();
				task.setStartTime(System.currentTimeMillis());
				task.setPhoneNum(user.getNickname());
				task.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
				task.setUserId(Long.parseLong(user.getUserId()));
				task.setRoomId(roomId);
				task.setPhoneNum(user.getNickname());
				task.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
				iLiveRandomRecordTaskMng.saveTask(task);
				resultJson.put("code", 1);
				resultJson.put("message", "启动收录成功");
				resultJson.put("time", "0");
			} else {
				resultJson.put("code", 0);
				resultJson.put("message", "启动收录失败,已有任务正在收录中");
			}
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	@RequestMapping(value = "randomrecord/end.do")
	public void recordFileEnd(final Integer roomId, final HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		final ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		final ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		if (liveEvent.getLiveStatus().intValue() == ILivePlayStatusConstant.UN_START) {
			resultJson.put("code", 0);
			resultJson.put("message", "结束收录失败,直播未开始");
			ResponseUtils.renderJson(response, resultJson.toString());
			return;
		}
		final UserBean userBean = ILiveUtils.getUser(request);
		final ILiveRandomRecordTask task = iLiveRandomRecordTaskMng.getTaskByQuery(liveEvent.getLiveEventId(),
				Long.parseLong(userBean.getUserId()), ILivePlayStatusConstant.PLAY_ING);
		if (task != null) {
			task.setEndTime(System.currentTimeMillis());
			task.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
			iLiveRandomRecordTaskMng.updateRecordTask(task);
			resultJson.put("code", 1);
			resultJson.put("message", "结束收录成功");
			new Thread(new Runnable() {
				@Override
				public void run() {
					ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
					String postUrl = HTTP_PROTOCAL + serverAccessMethod.getOrgLiveHttpUrl() + ":"
							+ serverAccessMethod.getLivemsport() + "/livems/servlet/LiveServlet";
					String mountName = "live" + roomId;
					int vodGroupId = Integer.parseInt(ConfigUtils.get("defaultVodServerGroupId"));
					int length = (int) (System.currentTimeMillis() - task.getStartTime()) / 1000;
					if (liveEvent.getLiveStatus().intValue() == ILivePlayStatusConstant.PLAY_OVER) {
						length = (int) (liveEvent.getLiveEndTime().getTime() - task.getStartTime()) / 1000;
					}
					String common = "";
					try {
						common = "?function=RecordLive&mountName=" + mountName + "&startTime="
								+ URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
										.format(new Timestamp(task.getStartTime())), "UTF-8")
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
							liveMediaFile.setUserId(Long.parseLong(userBean.getUserId()));
							liveMediaFile.setMediaInfoStatus(0);
							liveMediaFile.setLiveEventId(liveEvent.getLiveEventId());
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
				}
			}).start();
		} else {
			resultJson.put("code", 0);
			resultJson.put("message", "结束收录失败,未找到收录任务");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	@RequestMapping(value = "openSignup.do")
	public void openSignup(Integer roomId, Integer signUp, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		if (signUp != null && signUp.intValue() == 1) {
			Integer formId = iliveRoom.getLiveEvent().getFormId();
			if (formId != null) {
				BBSDiyform diyform = bbsDiyformMng.getDiyfromById(formId);
				if (diyform != null) {
					Set<BBSDiymodel> bbsDiymodels = diyform.getBbsDiymodels();
					if (bbsDiymodels == null || bbsDiymodels.isEmpty()) {
						resultJson.put("status", "0");
						resultJson.put("message", "开启失败,不存在表单内容");
						ResponseUtils.renderJson(response, resultJson.toString());
						return;
					}
				} else {
					resultJson.put("status", "0");
					resultJson.put("message", "开启失败,不存在表单内容");
					ResponseUtils.renderJson(response, resultJson.toString());
					return;
				}
			} else {
				resultJson.put("status", "0");
				resultJson.put("message", "开启失败,不存在表单内容");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}
		}
		try {
			liveEvent.setOpenSignupSwitch(signUp);
			iLiveEventMng.updateILiveEvent(liveEvent);
			resultJson.put("status", "1");
			resultJson.put("message", "操作成功");
		} catch (Exception e) {
			// iLiveEventMng.updateILiveEvent(liveEvent);
			resultJson.put("status", "0");
			resultJson.put("message", "操作失败");
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
	public void startEventPlay(Integer playType, Integer roomId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId=user.getEnterpriseId();
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		//首先判断是否还有直播时长--访问计费系统
		try {
			
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
							ILiveServerAccessMethod accessMethodBySeverGroupId = null;
							try {
								accessMethodBySeverGroupId = iLiveServerAccessMethodMng
										.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
								ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId, iliveRoom);
								// 正常向流媒体服务器推流操作
								String realIpAddr = IPUtils.getRealIpAddr(request);
								ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng
										.getILiveEnterPrise(iliveRoom.getEnterpriseId());
								iliveRoom.getLiveEvent().setEnterprise(iLiveEnterPrise);
								ILiveUserViewStatics.INSTANCE.startLive(iliveRoom, realIpAddr, "pc");
								ILiveEvent liveEvent = iliveRoom.getLiveEvent();
								liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
								liveEvent.setRecordStartTime(new Timestamp(System.currentTimeMillis()));
								liveEvent.setRealStartTime(new Timestamp(System.currentTimeMillis()));
								iLiveEventMng.updateILiveEvent(liveEvent);
								iliveRoom.setLiveEvent(liveEvent);
								/**
								 * 获取直播间推流地址
								 */
								String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address()
										+ "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
								iliveRoom.setHlsAddr(pushStreamAddr);
								RoomNoticeUtil.nptice(iliveRoom);
								try {
									ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
											.getUserListMap();
									Long nowNum = 0L;
									if (userListMap == null) {
									} else {
										nowNum = (long) userListMap.size();
									}
									iLiveRoomStaticsMng.initRoom(liveEvent.getLiveEventId(), nowNum);
									ApplicationCache.getOnlineNumber().remove(iliveRoom.getRoomId());
									iLiveRoomMng.startTask(iliveRoom.getLiveEvent().getLiveEventId(), iliveRoom.getRoomId());
								} catch (Exception e) {
									e.printStackTrace();
								}
								resultJson.put("status", SUCCESS_STATUS);
								resultJson.put("message", "操作成功");
							} catch (Exception e) {
								resultJson.put("status", ERROR_STATUS);
								resultJson.put("message", "操作失败");
								e.printStackTrace();
							}

						}
					} else {
						try {
							ILiveServerAccessMethod accessMethodBySeverGroupId = null;
							try {
								accessMethodBySeverGroupId = iLiveServerAccessMethodMng
										.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
								ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId, iliveRoom);
								ILiveEvent liveEvent = iliveRoom.getLiveEvent();
								liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
								liveEvent.setRealStartTime(new Timestamp(System.currentTimeMillis()));
								iLiveEventMng.updateILiveEvent(liveEvent);
								iliveRoom.setLiveEvent(liveEvent);
								String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + ":"
										+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId
										+ "/5000k/tzwj_video.m3u8";
								iliveRoom.setHlsAddr(pushStreamAddr);
								RoomNoticeUtil.nptice(iliveRoom);
								resultJson.put("status", SUCCESS_STATUS);
								resultJson.put("message", "操作成功");
							} catch (Exception e) {
								resultJson.put("status", ERROR_STATUS);
								resultJson.put("message", "操作失败");
								e.printStackTrace();
							}
						} catch (Exception e) {
							resultJson.put("status", ERROR_STATUS);
							resultJson.put("message", "操作失败");
							e.printStackTrace();
						}
					}
				} else {
					resultJson.put("status", ERROR_STATUS);
					resultJson.put("message", "直播状态不符合开始标准");
				}
			
		} catch (Exception e) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "操作失败");
			e.printStackTrace();
		}
		
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
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
			ILiveServerAccessMethod accessMethodBySeverGroupId = iLiveServerAccessMethodMng
					.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			try {
				ILiveUMSMessageUtil.INSTANCE.stopPlay(accessMethodBySeverGroupId, iliveRoom);
			} catch (Exception e2) {
				resultJson.put("status", ERROR_STATUS);
				resultJson.put("message", "操作成功");
				ResponseUtils.renderJson(response, resultJson.toString());
				e2.printStackTrace();
			}
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
			liveEvent.setLiveEndTime(new Timestamp(System.currentTimeMillis()));
			liveEvent.setRealEndTime(new Timestamp(System.currentTimeMillis()));
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			RoomNoticeUtil.nptice(iliveRoom);
			iLiveRoomMng.stopTask(liveEvent.getLiveEventId());
			//更新企业直播时长
			final Integer serverGroupId = iliveRoom.getServerGroupId();
			final UserBean userBean = ILiveUtils.getUser(request);
			Integer enterpriseId=userBean.getEnterpriseId();
			ILiveEnterprise iLiveEnterPrise1 = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			Integer certStatus = null;
			if(iLiveEnterPrise1!=null){
				certStatus = iLiveEnterPrise1.getCertStatus();
			}
			Long useValue=(liveEvent.getRealEndTime().getTime()-liveEvent.getRealStartTime().getTime())/1000;
			boolean ret=EnterpriseUtil.openEnterprise(enterpriseId,EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration,useValue.toString(),certStatus);
			
			// 正常向流媒体服务器推流操作
			String realIpAddr = IPUtils.getRealIpAddr(request);
			ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(iliveRoom.getEnterpriseId());
			iliveRoom.getLiveEvent().setEnterprise(iLiveEnterPrise);
			ILiveUserViewStatics.INSTANCE.stopLive(iliveRoom, realIpAddr);
			String userId = userBean.getUserId();
			final Long userIdLong = Long.parseLong(userId);
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
							String trimDownloadUrl = downloadUrl.trim();
							String relativePath = StringUtil.getTagValue(trimDownloadUrl, "ret");
							if (!relativePath.trim().equals("")) {
								try {
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
								} catch (Exception e) {
									e.printStackTrace();
								}
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
										liveMediaFile.setMediaInfoDealState(0);
										liveMediaFile.setAllowComment(1);
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
							liveMediaFile.setAllowComment(1);
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Long saveIliveMediaFile = iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
							iLiveVideoHisotryMng.saveVideoHistory(roomId, saveIliveMediaFile,
									Long.parseLong(userBean.getUserId()));
						}
					}
				}
			}).start();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 上传垫片
	 */
	@RequestMapping(value = "uploadslim")
	public String uploadSlimFile(Integer roomId) {
		return "liveroom/slimupload";
	}

	/**
	 * 继续直播
	 */
	@RequestMapping(value = "live/continue.do")
	public void continueEventPlay(Integer playType, Integer roomId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		Integer serverGroupId = iliveRoom.getServerGroupId();
		/**
		 * 获取直播间推流地址
		 */
		ILiveServerAccessMethod accessMethodBySeverGroupId = null;
		try {
			accessMethodBySeverGroupId = iLiveServerAccessMethodMng.getAccessMethodBySeverGroupId(serverGroupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 直播结束的继续直播
		if (liveStatus.equals(ILivePlayStatusConstant.PLAY_OVER)) {
			try {
				ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId, iliveRoom);
			} catch (Exception e1) {
				resultJson.put("status", ERROR_STATUS);
				resultJson.put("message", "流服务器交互失败");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				e1.printStackTrace();
				return;
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
			iLiveEventNew.setRealStartTime(new Timestamp(System.currentTimeMillis()));
			List<PageDecorate> pageRecordList = pageDecorateMng.getList(iliveRoom.getLiveEvent().getLiveEventId());
			iLiveEventNew.setPageRecordList(pageRecordList);
			Timestamp startTmp = new Timestamp(System.currentTimeMillis());
			iLiveEventNew.setLiveStartTime(startTmp);
			iLiveEventNew.setLiveEndTime(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
			iLiveEventNew.setRecordStartTime(startTmp);
			iLiveEventNew.setRealStartTime(startTmp);
			Long saveIliveMng = iLiveEventMng.saveIliveMng(iLiveEventNew);
			// 1 处理菜单
			List<PageDecorate> list = pageDecorateMng.getList(oldLiveEventId);
			if (list != null) {
				for (PageDecorate pd : list) {
					PageDecorate pdNew = new PageDecorate();
					try {
						BeanUtilsExt.copyProperties(pdNew, pd);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					pdNew.setLiveEventId(saveIliveMng);
					pageDecorateMng.addPageDecorateInit(pdNew);
				}
			}

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
				}
			}
			// 3处理白名单
			iLiveEventNew.setLiveEventId(saveIliveMng);
			iLiveEventNew.setFormId(formId);
			iliveRoom.setLiveEvent(iLiveEventNew);
			iLiveRoomMng.update(iliveRoom);
			// 正常向流媒体服务器推流操作
			String realIpAddr = IPUtils.getRealIpAddr(request);
			ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(iliveRoom.getEnterpriseId());
			iliveRoom.getLiveEvent().setEnterprise(iLiveEnterPrise);
			ILiveUserViewStatics.INSTANCE.startLive(iliveRoom, realIpAddr, "pc");
			String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + "/live/live" + roomId
					+ "/5000k/tzwj_video.m3u8";
			iliveRoom.setHlsAddr(pushStreamAddr);
			RoomNoticeUtil.nptice(iliveRoom);
			try {
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				Long nowNum = 0L;
				if (userListMap == null) {
				} else {
					nowNum = (long) userListMap.size();
				}
				iLiveRoomStaticsMng.initRoom(iLiveEventNew.getLiveEventId(), nowNum);
				ApplicationCache.getOnlineNumber().remove(iliveRoom.getRoomId());
				iLiveRoomMng.startTask(iliveRoom.getLiveEvent().getLiveEventId(), iliveRoom.getRoomId());
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	private BBSDiymodel convertModel2NewModel(BBSDiymodel model, BBSDiyform bbsDiyform) {
		BBSDiymodel newModel = new BBSDiymodel();
		newModel.setOptValue(model.getOptValue());
		newModel.setDiymodelType(model.getDiymodelType());
		newModel.setDiymodelTitle(model.getDiymodelTitle());
		newModel.setDiyOrder(model.getDiyOrder());
		newModel.setBbsDiyform(bbsDiyform);
		newModel.setNeedAnswer(model.getNeedAnswer());
		newModel.setDiymodelKey(model.getDiymodelKey());
		newModel.setNeedMsgValid(model.getNeedMsgValid() == null ? 0 : model.getNeedMsgValid());
		newModel.setOpenAnswer(model.getOpenAnswer() == null ? 0 : model.getOpenAnswer());
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
