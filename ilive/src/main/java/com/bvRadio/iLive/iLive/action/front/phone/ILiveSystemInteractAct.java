package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.action.front.vo.AppUserInfo;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.constants.ILivePlayCtrType;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveRandomRecordTask;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.manager.BBSDiyformMng;
import com.bvRadio.iLive.iLive.manager.BBSDiymodelMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveRandomRecordTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomStaticsMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveVideoHistoryMng;
import com.bvRadio.iLive.iLive.task.MediaTask;
import com.bvRadio.iLive.iLive.util.ILiveUMSMessageUtil;
import com.bvRadio.iLive.iLive.util.IPUtils;
import com.bvRadio.iLive.iLive.util.RoomNoticeUtil;
import com.bvRadio.iLive.iLive.util.StringUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
import com.bvRadio.iLive.iLive.web.PostMan;

import net.sf.json.JSONArray;

/**
 * 系统交互接口
 * 
 * @author administrator
 *
 */
@Controller
@RequestMapping(value = "remote")
public class ILiveSystemInteractAct {

	protected static final String HTTP_PROTOCAL = "http://";

	Logger logger = LoggerFactory.getLogger(ILiveSystemInteractAct.class);

	@Autowired
	private ILiveManagerMng iLiveManagerMng;


	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;
	
	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;

	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveEventMng iLiveEventMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;


	@Autowired
	private ILiveRandomRecordTaskMng iLiveRandomRecordTaskMng;

	@Autowired
	private ILiveVideoHistoryMng iLiveVideoHisotryMng;


	/**
	 * 简单搜索用户信息
	 */
	@RequestMapping(value = "/userinfo.jspx")
	public void searchSimpleUserInfo(@RequestParam(value = "userIds[]") Long[] userIds, HttpServletRequest request,
			HttpServletResponse response) {
		String realIpAddr = IPUtils.getRealIpAddr(request);
		logger.info("searchSimpleUserInfo realIpAddr{}", realIpAddr);
		JSONObject resultJson = new JSONObject();
		if (!checkValidWhiteList(realIpAddr)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "该IP没有权限访问");
			resultJson.put("data", new JSONArray());
			ResponseUtils.renderJson(response, resultJson.toString());
			return;
		}
		if (userIds == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "参数不合法");
			resultJson.put("data", new JSONArray());
		} else {
			List<AppUserInfo> userList = iLiveManagerMng.batchQueryUserId(userIds);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取用户成功");
			resultJson.put("data", userList);
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 重新计算套餐使用情况
	 */
	@RequestMapping(value = "/recheckUsedPruduct.jspx")
	public void reCheckUsedPruduct(Integer enterpriseId, HttpServletRequest request,
			HttpServletResponse response) {
		String realIpAddr = IPUtils.getRealIpAddr(request);
		logger.info("searchSimpleUserInfo realIpAddr{}", realIpAddr);
		JSONObject resultJson = new JSONObject();
//		if (!checkValidWhiteList(realIpAddr)) {
//			resultJson.put("code", ERROR_STATUS);
//			resultJson.put("message", "该IP没有权限访问");
//			resultJson.put("data", new JSONArray());
//			ResponseUtils.renderJson(response, resultJson.toString());
//			return;
//		}
		if (enterpriseId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "参数不合法");
			resultJson.put("data", new JSONArray());
		} else {
			//调用线程重新计算套餐使用情况
			MediaTask task=new MediaTask();
			task.runnableMedia(enterpriseId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取用户成功");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 重新计算所有用户套餐使用情况
	 */
	@RequestMapping(value = "/recheckPruduct.jspx")
	public void reCheckPruduct(HttpServletRequest request,
			HttpServletResponse response) {
		String realIpAddr = IPUtils.getRealIpAddr(request);
		logger.info("searchSimpleUserInfo realIpAddr{}", realIpAddr);
		JSONObject resultJson = new JSONObject();
//		if (!checkValidWhiteList(realIpAddr)) {
//			resultJson.put("code", ERROR_STATUS);
//			resultJson.put("message", "该IP没有权限访问");
//			resultJson.put("data", new JSONArray());
//			ResponseUtils.renderJson(response, resultJson.toString());
//			return;
//		}
//		if (enterpriseId == null) {
//			resultJson.put("code", ERROR_STATUS);
//			resultJson.put("message", "参数不合法");
//			resultJson.put("data", new JSONArray());
//		} else {
			//调用线程重新计算套餐使用情况
			MediaTask task=new MediaTask();
			task.run();
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "重新计算所有用户套餐使用情况成功");
//		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 重新更新套餐缓存情况
	 */
	@RequestMapping(value = "/recheckUsedCache.jspx")
	public void recheckUsedCache(Integer enterpriseId,Integer certStatus, HttpServletRequest request,
			HttpServletResponse response) {
		String realIpAddr = IPUtils.getRealIpAddr(request);
		logger.info("searchSimpleUserInfo realIpAddr{}", realIpAddr);
		JSONObject resultJson = new JSONObject();
		if (!checkValidWhiteList(realIpAddr)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "该IP没有权限访问");
			resultJson.put("data", new JSONArray());
			ResponseUtils.renderJson(response, resultJson.toString());
			return;
		}
		if (enterpriseId == null||certStatus==null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "参数不合法");
			resultJson.put("data", new JSONArray());
		} else {
			//接口重新更新缓存
			try {
				EnterpriseUtil.selectEnterpriseCache(enterpriseId, certStatus);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取用户成功");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "获取用户成功");
			}
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 增量获取企业信息
	 */
	@RequestMapping(value = "/increment/enterprise.jspx")
	public void searchIncrementEnterpriseInfo(Integer startId, Integer size, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		String realIpAddr = IPUtils.getRealIpAddr(request);
		System.out.println("startId===="+startId);
        System.out.println("realIpAddr:"+realIpAddr+"***");
        System.out.println("startId====="+startId+"****size===="+size+"*******");
		if (!checkValidWhiteList(realIpAddr)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "该IP没有权限访问");
			resultJson.put("data", new JSONArray());
			ResponseUtils.renderJson(response, resultJson.toString());
			return;
		}
		if(size == null) {
			size=100;
		}
		if (startId == null ) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "参数不合法");
			resultJson.put("data", new JSONArray());
		} else {
			List<ILiveAppEnterprise> enterpriseList = iLiveEnterpriseMng.getBatchEnterpriseForStatics(startId, size);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取企业成功");
			resultJson.put("data", enterpriseList);
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 单一获取企业信息
	 */
	@RequestMapping(value = "/single/enterprise.jspx")
	public void searchEnterpriseInfo(@RequestParam(value = "ids[]") Integer[] ids, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		String realIpAddr = IPUtils.getRealIpAddr(request);
		if (!checkValidWhiteList(realIpAddr)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "该IP没有权限访问");
			resultJson.put("data", new JSONArray());
			ResponseUtils.renderJson(response, resultJson.toString());
			return;
		}
		if (ids == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "参数不合法");
			resultJson.put("data", new JSONArray());
		} else {
			try {
				System.out.println("ids========"+ids);
				List<ILiveAppEnterprise> enterpriseList = iLiveEnterpriseMng.geSingleEnterpriseForStatics(ids);
				System.out.println("获取到的企业名称信息为：+++++++"+enterpriseList.get(0).getEnterpriseName());
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取企业信息成功");
				if (enterpriseList != null) {
					resultJson.put("data", enterpriseList);
				} else {
					resultJson.put("data", new JSONArray());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 增量获取回看信息
	 */
	@RequestMapping(value = "/increment/vod.jspx")
	public void searchIncrementVod(Long startId, Integer size, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		String realIpAddr = IPUtils.getRealIpAddr(request);
		if (!checkValidWhiteList(realIpAddr)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "该IP没有权限访问");
			resultJson.put("data", new JSONArray());
			ResponseUtils.renderJson(response, resultJson.toString());
			return;
		}
		logger.info("searchEnterpriseInfo realIpAddr{}", realIpAddr);
		if (startId == null || size == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "参数不合法");
			resultJson.put("data", new JSONArray());
		} else {
			List<AppMediaFile> vodList = new ArrayList<>();
			try {
				vodList = iLiveMediaFileMng.getBatchVodListForStatics(startId, size);
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取回看信息成功");
			resultJson.put("data", vodList);
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 单一获取回看信息
	 */
	@RequestMapping(value = "/single/vod.jspx")
	public void searchSingleVod(@RequestParam(value = "ids[]") Long[] ids, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		String realIpAddr = IPUtils.getRealIpAddr(request);
		System.out.println("realIpAddr:"+realIpAddr);
		if (!checkValidWhiteList(realIpAddr)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "该IP没有权限访问");
			resultJson.put("data", new JSONArray());
			ResponseUtils.renderJson(response, resultJson.toString());
			return;
		}
		if (ids == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "参数不合法");
			resultJson.put("data", new JSONArray());
		} else {
			List<AppMediaFile> appMediaFileForStatics = new ArrayList<>();
			try {
				appMediaFileForStatics = iLiveMediaFileMng.getAppMediaFileForStatics(ids);
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultJson.put("data", appMediaFileForStatics);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取回看信息成功");

		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	public boolean checkValidWhiteList(String ipAddr) {
		String system_whitelist = ConfigUtils.get("system_whitelist");
		System.out.println("system_whitelist:"+system_whitelist+"*********");
		if (!com.jwzt.comm.StringUtils.isEmpty(system_whitelist)) {
			String[] split = system_whitelist.split(",");
			for (String ip : split) {
				if (ip.equals(ipAddr)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	@RequestMapping(value = "valid/stoplive.jspx")
	public void liveStop(HttpServletRequest request, HttpServletResponse response,final Integer roomId) {
		JSONObject resultJson = new JSONObject();
		final ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		if (liveStatus.equals(ILivePlayStatusConstant.PLAY_OVER)
				|| liveStatus.equals(ILivePlayStatusConstant.UN_START)) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合结束标准");
		} else {
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = iLiveServerAccessMethodMng
						.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (iliveRoom.getLiveEvent().getPlayType().equals(ILivePlayCtrType.POOL_LIVE)) {
					ILiveUMSMessageUtil.INSTANCE.closePullStream(accessMethodBySeverGroupId, iliveRoom);
				} else {
					ILiveUMSMessageUtil.INSTANCE.stopPlay(accessMethodBySeverGroupId, iliveRoom);
				}
			} catch (Exception e1) {
				resultJson.put("status", ERROR_STATUS);
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "流服务器交互失败");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				e1.printStackTrace();
				return;
			}
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			liveEvent.setLiveEndTime(timestamp);
			liveEvent.setRealEndTime(timestamp);
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			RoomNoticeUtil.nptice(iliveRoom);
			iLiveRoomMng.stopTask(liveEvent.getLiveEventId());
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			// 正常向流媒体服务器推流操作
			String realIpAddr = IPUtils.getRealIpAddr(request);
			ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(iliveRoom.getEnterpriseId());
			iliveRoom.getLiveEvent().setEnterprise(iLiveEnterPrise);
			ILiveUserViewStatics.INSTANCE.stopLive(iliveRoom, realIpAddr);
			final Integer serverGroupId = iliveRoom.getServerGroupId();
			final Long userIdLong = iliveRoom.getManagerId();
			final ILiveRandomRecordTask task = iLiveRandomRecordTaskMng.getTaskByQuery(liveEvent.getLiveEventId(),
					userIdLong, ILivePlayStatusConstant.PLAY_ING);
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
									liveMediaFile.setEnterpriseId((long) iliveRoom.getEnterpriseId());
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
									iLiveVideoHisotryMng.saveVideoHistory(roomId, saveIliveMediaFile, userIdLong);
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
										liveMediaFile.setEnterpriseId((long) iliveRoom.getEnterpriseId());
										liveMediaFile.setUserId(userIdLong);
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
										iLiveVideoHisotryMng.saveVideoHistory(roomId, saveIliveMediaFile, userIdLong);
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
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

}
