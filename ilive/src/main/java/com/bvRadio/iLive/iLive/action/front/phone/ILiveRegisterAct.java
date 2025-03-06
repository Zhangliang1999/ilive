package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.bvRadio.iLive.common.web.RequestUtils;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveRoomRegister;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomRegisterService;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.iLive.web.PostMan;
import com.bvRadio.iLive.iLive.web.vo.UserBaseInfo;
import com.thoughtworks.xstream.mapper.Mapper.Null;

@Controller
@RequestMapping(value = "/register")
public class ILiveRegisterAct {

	@Autowired
	private ILiveRoomRegisterService registerService; // 签到、点赞

	@Autowired
	private ILiveLiveRoomMng roomMng; // 直播间

	@Autowired
	private ILiveEventMng iLiveEventMng; // 直播场次管理

	@Autowired
	private ILiveMediaFileMng mediaFileMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	// 直播场次签到
	@RequestMapping(value = "register.jspx")
	public void register(String userId, Long liveEventId, Integer roomId, Integer state, HttpServletRequest request,
			HttpServletResponse response) {
		UserBean userBean = ILiveUtils.getAppUser(request);
		// if (userId == null) {
		// userId = userBean.getUserId();
		// }
		JSONObject resultJson = new JSONObject();
		if (userBean == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户不存在");
			resultJson.put("data", new JSONObject());
		} else {
			if (state == 0) {
				try {
					// 直播场次是否存在
					ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
					if (iLiveEvent == null) {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "直播场次不存在");
						resultJson.put("data", new JSONObject());
					} else {
						// 查询是否签到
						if (!registerService.queryILiveIsRegister(userId, liveEventId)) {
							ILiveRoomRegister register = new ILiveRoomRegister();
							register.setUserId(userId);
							register.setLiveEventId(liveEventId);
							register.setState(0);
							ILiveRoomRegister saveILiveRoomRegiste = registerService.saveILiveRoomRegiste(register);
							resultJson.put("code", SUCCESS_STATUS);
							resultJson.put("message", "签到成功");
							resultJson.put("data", new JSONObject(saveILiveRoomRegiste));
						} else {
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "已签到");
							resultJson.put("data", new JSONObject());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "签到失败");
					resultJson.put("data", new JSONObject());
				}
			} else if (state == 1) {
				try {
					// 直播间是否存在
					ILiveLiveRoom room = roomMng.findById(roomId);
					if (room == null) {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "直播间不存在");
						resultJson.put("data", new JSONObject());
					} else {
						// 查询是否已点赞
						if (!registerService.queryIliveRoomRegister(userId, roomId)) {
							ILiveRoomRegister register = new ILiveRoomRegister();
							register.setUserId(userId);
							register.setRoomId(roomId);
							register.setState(1);
							ILiveRoomRegister saveILiveRoomRegiste = registerService.saveILiveRoomRegiste(register);

							resultJson.put("code", SUCCESS_STATUS);
							resultJson.put("message", "点赞成功");
							resultJson.put("data", new JSONObject(saveILiveRoomRegiste));
						} else {
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "已点赞");
							resultJson.put("data", new JSONObject());
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "点赞失败");
					resultJson.put("data", new JSONObject());
				}
			}

		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	// 直播场次签到
	@RequestMapping(value = "registerEvent.jspx")
	public void saveRegister(String userId, Long liveEventId, HttpServletRequest request,
			HttpServletResponse response, Integer type) {
		UserBean userBean = ILiveUtils.getAppUser(request);
		// if (userId == null) {
		// userId = userBean.getUserId();
		// }
		JSONObject resultJson = new JSONObject();
		if (userBean == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户不存在");
			resultJson.put("data", new JSONObject());
		} else {
			try {
				// 直播场次是否存在
				ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
				if (iLiveEvent == null) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播场次不存在");
					resultJson.put("data", new JSONObject());
				} else {
//					Integer enterpriseId = iLiveEvent.getEnterprise().getEnterpriseId();
//					ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
//					Integer certStatus = iLiveEnterprise.getCertStatus();
//					boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_Sign,certStatus);
//					if(b){
//						resultJson.put("code", ERROR_STATUS);
//						resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_Sign_Content);
//						ResponseUtils.renderJson(request, response, resultJson.toString());
//						return;
//					}
					// 查询是否签到
					if (!registerService.queryILiveIsRegister(userId, liveEventId)) {
						ILiveRoomRegister register = new ILiveRoomRegister();
						register.setUserId(userId);
						register.setLiveEventId(liveEventId);
						register.setState(0);
						ILiveRoomRegister saveILiveRoomRegiste = registerService.saveILiveRoomRegiste(register);
						// 签到通知
						try {
							String terminalType = null;
							if (null != type) {
								if (type == 1) {
									terminalType = "android";
								} else if (type == 2) {
									terminalType = "ios";
								} else if (type == 3) {
									terminalType = "pc";
								} else if (type == 4) {
									terminalType = "h5";
								}
							}
							String ipAddr = RequestUtils.getIpAddr(request);
							ILiveUserViewStatics.INSTANCE.sign(userId, liveEventId, ipAddr, terminalType);
						} catch (Exception e) {
							e.printStackTrace();
						}
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "签到成功");
						resultJson.put("data", new JSONObject(saveILiveRoomRegiste));
					} else {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "已签到");
						resultJson.put("data", new JSONObject());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "签到失败");
				resultJson.put("data", new JSONObject());
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	private final static String ROOM_REGISTER_STATUS_ = "room_register_status_";
	// 直播间点赞
	@RequestMapping(value = "registerRoom.jspx")
	public void saveRegisterRoom(String userId, Integer roomId, HttpServletRequest request,
			HttpServletResponse response, Integer type) {
		
		JSONObject resultJson = new JSONObject();
		try {
			// 直播间是否存在
			ILiveLiveRoom room = roomMng.findById(roomId);
			if (room == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				resultJson.put("data", new JSONObject());
			} else {
				
				Long eventId = room.getLiveEvent().getLiveEventId();
				
//				ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
//				Integer certStatus = iLiveEnterprise.getCertStatus();
//				boolean b = EnterpriseUtil.selectIfEn(room.getEnterpriseId(),
//						EnterpriseCache.ENTERPRISE_FUNCTION_DotPraise, certStatus);
//				if (b) {
//					resultJson.put("code", ERROR_STATUS);
//					resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_DotPraise_Content);
//					ResponseUtils.renderJson(request, response, resultJson.toString());
//					return;
//				}
				// 支持游客点赞
				Object registerStatus = request.getSession().getAttribute(ROOM_REGISTER_STATUS_ + roomId);
				UserBean userBean = ILiveUtils.getAppUser(request);
				if (userId == null && null != userBean) {
					userId = userBean.getUserId();
				}
				// 查询是否已点赞
				if ((null == userId && null == registerStatus)
						|| (null != userId && !registerService.queryIliveRoomRegister(userId, roomId))) {
					if (null != userId) {
						ILiveRoomRegister register = new ILiveRoomRegister();
						register.setUserId(userId);
						register.setRoomId(roomId);
						register.setLiveEventId(eventId);
						register.setState(1);
						ILiveRoomRegister saveILiveRoomRegiste = registerService.saveILiveRoomRegiste(register);
						resultJson.put("data", new JSONObject(saveILiveRoomRegiste));
					} else {
						request.getSession().setAttribute(ROOM_REGISTER_STATUS_ + roomId, 1);
					}
					// 点赞通知
					try {
						String terminalType = null;
						if (null != type) {
							if (type == 1) {
								terminalType = "android";
							} else if (type == 2) {
								terminalType = "ios";
							} else if (type == 3) {
								terminalType = "pc";
							} else if (type == 4) {
								terminalType = "h5";
							}
						}
						String ipAddr = RequestUtils.getIpAddr(request);
						ILiveUserViewStatics.INSTANCE.likeRoom(userId, roomId, eventId, null, ipAddr, terminalType);
						// notifyRegisterRoom(userId,roomId,type);
					} catch (Exception e) {
						e.printStackTrace();
					}

					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "点赞成功");

				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "已点赞");
					resultJson.put("data", new JSONObject());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "点赞失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	private final static String MEDIA_REGISTER_STATUS_ = "media_register_status_";

	// 回看点赞
	@RequestMapping(value = "registerMedia.jspx")
	public void saveRegisterMedia(String userId, Long fileId, HttpServletRequest request, HttpServletResponse response,
			Integer type) {
		JSONObject resultJson = new JSONObject();
		try {
			// 回看是否存在
			ILiveMediaFile media = mediaFileMng.selectILiveMediaFileByFileId(fileId);
			if (media == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "视频不存在");
				resultJson.put("data", new JSONObject());
			} else {
				// 支持游客点赞
				Object registerStatus = request.getSession().getAttribute(MEDIA_REGISTER_STATUS_ + fileId);
				UserBean userBean = ILiveUtils.getAppUser(request);
				if (userId == null && null != userBean) {
					userId = userBean.getUserId();
				}
				// 查询是否已点赞
				if ((null == userId && null == registerStatus)
						|| (null != userId && !registerService.queryMediaRegister(userId, fileId))) {
					if (null != userId) {
						ILiveRoomRegister register = new ILiveRoomRegister();
						register.setUserId(userId);
						register.setFileId(fileId);
						register.setState(1);
						ILiveRoomRegister saveILiveRoomRegiste = registerService.saveILiveRoomRegiste(register);
						resultJson.put("data", new JSONObject(saveILiveRoomRegiste));
					} else {
						request.getSession().setAttribute(MEDIA_REGISTER_STATUS_ + fileId, 1);
					}
					// 点赞通知
					try {
						String terminalType = null;
						if (null != type) {
							if (type == 1) {
								terminalType = "android";
							} else if (type == 2) {
								terminalType = "ios";
							} else if (type == 3) {
								terminalType = "pc";
							} else if (type == 4) {
								terminalType = "h5";
							}
						}
						String ipAddr = RequestUtils.getIpAddr(request);
						ILiveUserViewStatics.INSTANCE.likeRoom(userId, null, null, fileId, ipAddr, terminalType);
						// notifyRegisterRoom(userId,roomId,type);
					} catch (Exception e) {
						e.printStackTrace();
					}
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "点赞成功");
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "已点赞");
					resultJson.put("data", new JSONObject());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "点赞失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	// 查询直播间是否点赞
	@RequestMapping(value = "queryRoomIsRegister.jspx")
	public void queryRoomIsRegister(String userId, Integer roomId, HttpServletRequest request,
			HttpServletResponse response) {
		
		JSONObject resultJson = new JSONObject();
		try {
			// 直播间是否存在
			ILiveLiveRoom room = roomMng.findById(roomId);
			if (room == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间不存在");
				resultJson.put("data", new JSONObject());
			} else {
				JSONObject object = new JSONObject();
				UserBean userBean = ILiveUtils.getAppUser(request);
				if (userId == null && null != userBean) {
					userId = userBean.getUserId();
				}
				// 查询是否已点赞
				Object registerStatus = request.getSession().getAttribute(ROOM_REGISTER_STATUS_ + roomId);
				boolean isRoomReg = registerService.queryIliveRoomRegister(userId, roomId);
				if (isRoomReg || null != registerStatus) {
					object.put("isReg", 1);
				} else {
					object.put("isReg", 0);
				}
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "查询成功");
				resultJson.put("data", object);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "查询失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	// 查询回看是否点赞
	@RequestMapping(value = "queryMediaIsRegister.jspx")
	public void queryMediaIsRegister(String userId, Long fileId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			// 回看视频是否存在
			ILiveMediaFile media = mediaFileMng.selectILiveMediaFileByFileId(fileId);
			if (media == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "视频不存在");
				resultJson.put("data", new JSONObject());
			} else {
				JSONObject object = new JSONObject();
				// 查询是否已点赞
				UserBean userBean = ILiveUtils.getAppUser(request);
				if (userId == null && null != userBean) {
					userId = userBean.getUserId();
				}
				Object registerStatus = request.getSession().getAttribute(MEDIA_REGISTER_STATUS_ + fileId);
				boolean isRoomReg = registerService.queryMediaRegister(userId, fileId);
				if (isRoomReg || null != registerStatus) {
					object.put("isReg", 1);
				} else {
					object.put("isReg", 0);
				}
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "查询成功");
				resultJson.put("data", object);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "查询失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	// 查询直播间点赞数量
		@RequestMapping(value = "RoomRegisterCount.jspx")
		public void queryRoomIsRegister(Long eventId, Integer roomId, HttpServletRequest request,
				HttpServletResponse response) {
			
			JSONObject resultJson = new JSONObject();
			try {
				// 直播间是否存在
				ILiveLiveRoom room = roomMng.findById(roomId);
				if (room == null) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播间不存在");
					resultJson.put("data", new JSONObject());
				} else {
					JSONObject object = new JSONObject();
					
					// 查询点赞数量
					Long count=registerService.ILiveIsRegisterCount(roomId.longValue());
					if(count==null){
						count=0L;
					}
						object.put("count", count);
					
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "查询成功");
					resultJson.put("data", object);
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "查询失败");
				resultJson.put("data", new JSONObject());
			}
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
	    // 查询回看点赞数量
		@RequestMapping(value = "MediaRegisterCount.jspx")
		public void queryMediaIsRegister( Long fileId, HttpServletRequest request,
				HttpServletResponse response) {
			JSONObject resultJson = new JSONObject();
			try {
				// 回看视频是否存在
				ILiveMediaFile media = mediaFileMng.selectILiveMediaFileByFileId(fileId);
				if (media == null) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "视频不存在");
					resultJson.put("data", new JSONObject());
				} else {
					JSONObject object = new JSONObject();
					// 查询点赞数量
					Long count=registerService.MediaRegisterCount(fileId);
					if(count==null){
						count=0L;
					}
					object.put("count", count);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "查询成功");
					resultJson.put("data", object);
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "查询失败");
				resultJson.put("data", new JSONObject());
			}
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
	// 查询直播场次是否签到
	@RequestMapping(value = "queryEventIsRegister.jspx")
	public void queryIsRegister(String userId, Long liveEventId, HttpServletRequest request,
			HttpServletResponse response) {
		UserBean userBean = ILiveUtils.getAppUser(request);
		// if (userId == null) {
		// userId = userBean.getUserId();
		// }
		JSONObject resultJson = new JSONObject();
		if (userBean == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户不存在");
			resultJson.put("data", new JSONObject());
		} else {
			try {
				userId = userBean.getUserId();
				// 直播场次是否存在
				ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
				if (iLiveEvent == null) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播场次不存在");
					resultJson.put("data", new JSONObject());
				} else {
					JSONObject object = new JSONObject();
					// 查询是否已签到
					boolean isEventReg = registerService.queryILiveIsRegister(userId, liveEventId);
					if (isEventReg) {
						object.put("isReg", 1);
					} else {
						object.put("isReg", 0);
					}
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "查询成功");
					resultJson.put("data", object);
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "查询失败");
				resultJson.put("data", new JSONObject());
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	// 查询直播场次是否签到、点赞
	@RequestMapping(value = "queryIsRegister.jspx")
	public void queryIsRegister1(String userId, Long liveEventId, HttpServletRequest request,
			HttpServletResponse response) {
		
		JSONObject resultJson = new JSONObject();
		try {
//			if(JedisUtils.exists("queryIsRegister:"+liveEventId)) {
//	        	
//				ResponseUtils.renderJson(request, response, JedisUtils.get("queryIsRegister:"+liveEventId));
//				return;
//			}else {
				// 直播场次是否存在
				ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
				if (iLiveEvent == null) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播场次不存在");
					resultJson.put("data", new JSONObject());
				} else {
					JSONObject object = new JSONObject();
					int eventReg;
					int roomReg;
					if (userId==null) {
						eventReg = 0;
						Object registerStatus = request.getSession().getAttribute(ROOM_REGISTER_STATUS_ + iLiveEvent.getRoomId());
						roomReg = registerStatus == null?0:1;
						object.put("isQiandaoReg", eventReg);
						object.put("isDianzanReg", roomReg);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "查询成功");
						resultJson.put("data", object);
						//JedisUtils.set("queryIsRegister:"+liveEventId, resultJson.toString(), 60);
					}else {
						// 查询是否已签到
						Integer roomId = iLiveEvent.getRoomId();
						boolean isEventReg = registerService.queryILiveIsRegister(userId, liveEventId);
						// 查询是否已点赞
						boolean isRoomReg = registerService.queryIliveRoomRegister(userId, roomId);
						if (isEventReg) {
							eventReg = 1;
						} else {
							eventReg = 0;
						}
						if (isRoomReg) {
							roomReg = 1;
						} else {
							roomReg = 0;
						}
						object.put("isQiandaoReg", eventReg);
						object.put("isDianzanReg", roomReg);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "查询成功");
						resultJson.put("data", object);
						//JedisUtils.set("queryIsRegister:"+liveEventId, resultJson.toString(), 60);
					}
					
				}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "查询失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	// 点赞通知
	static void notifyRegisterRoom(final String userId, final int roomId, final Integer type) throws Exception {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
		executor.execute(new Runnable() {

			@Override
			public void run() {

				String url = ConfigUtils.get("StaticsNofityUrl");
				url = url + "/service/user/like";
				UserBaseInfo ubi = new UserBaseInfo();
				ubi.setUserId(userId);
				ubi.setRoomId(roomId);
				ubi.setType(type);
				JSONArray jArr = new JSONArray();
				jArr.put(ubi.buildJson());
				String downloadUrl = PostMan.postJson(url, "POST", "UTF-8", jArr.toString());
			}
		});
	}

}
