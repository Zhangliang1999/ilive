package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppJungleUtil;
import com.bvRadio.iLive.iLive.action.front.vo.AppLoginValid;
import com.bvRadio.iLive.iLive.action.front.vo.TempLoginInfo;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;
import com.bvRadio.iLive.iLive.entity.MountInfo;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewAuthBillMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/app/room/vod")
public class ILiveMediaFilePhoneAct {

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	private ILiveLiveRoomMng liveRoomMng;

	@Autowired
	private ILiveEventMng iLiveEventMng;

	@Autowired
	private ILiveViewAuthBillMng iLiveViewAuthBillMng;

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private ILiveViewWhiteBillMng iLiveViewWhiteBillMng;

	@Autowired
	private ILiveServerAccessMethodMng serverAccessMethodMng;

	@Autowired
	private ILiveMediaFileCommentsMng commentsMng;

	@Autowired
	private ILiveMediaFileMng iLivemediaFileMng;

	@RequestMapping(value = "checklogin.jspx")
	public void judgeMediaFile(Long fileId, Long userId, String loginToken, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
		if (mediaFile == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "文件不存在");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		ILiveEvent iliveEvent = iLiveEventMng.selectILiveEventByID(mediaFile.getLiveEventId());
		AppJungleUtil jungleUtil = AppJungleUtil.INSTANCE;
		if (request.getSession().getAttribute("tempLoginInfo") == null) {
			request.getSession().setAttribute("tempLoginInfo", new TempLoginInfo());
		}
		boolean needLogin = jungleUtil.needLoginByEvent(iliveEvent);
		if (needLogin) {
			boolean jungeUserSession = jungleUtil.jungeUserSession(request);
			if (!jungeUserSession) {
				if (loginToken == null || userId == null) {
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "连接成功");
					AppLoginValid validRegex = new AppLoginValid();
					validRegex.setRoomNeedLogin(1);
					resultJson.put("data", validRegex);
					Gson gson = new Gson();
					String json = gson.toJson(validRegex);
					resultJson.put("data", json);
				} else {
					ILiveManager manager = iLiveManagerMng.checkLogin(loginToken, userId);
					if (manager != null) {
						// 帮助自动登录
						this.autoLogin(request, manager);
						manager.setEnterPrise(null);
						manager.setUserPwd(null);
						manager.setSalt(null);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "连接成功");
						AppLoginValid validRegex = new AppLoginValid();
						validRegex.setRoomNeedLogin(2);
						validRegex.setUserInfo(manager);
						resultJson.put("data", validRegex);
						Gson gson = new Gson();
						String json = gson.toJson(validRegex);
						resultJson.put("data", json);
					} else {
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "连接成功");
						AppLoginValid validRegex = new AppLoginValid();
						validRegex.setRoomNeedLogin(1);
						resultJson.put("data", validRegex);
						Gson gson = new Gson();
						String json = gson.toJson(validRegex);
						resultJson.put("data", json);
					}
				}
			} else {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "连接成功");
				UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
				String userIdSession = userBean.getUserId();
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userIdSession));
				if (iLiveManager != null) {
					iLiveManager.setEnterPrise(null);
					iLiveManager.setUserPwd(null);
					iLiveManager.setSalt(null);
				}
				AppLoginValid validRegex = new AppLoginValid();
				validRegex.setRoomNeedLogin(2);
				validRegex.setUserInfo(iLiveManager);
				resultJson.put("data", validRegex);
				Gson gson = new Gson();
				String json = gson.toJson(validRegex);
				resultJson.put("data", json);
			}
		} else {
			if (loginToken == null || userId == null) {
				UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
				if (userBean != null) {
					ILiveManager iLiveManager = null;
					try {
						iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userBean.getUserId()));
						if (iLiveManager != null) {
							iLiveManager.setEnterPrise(null);
							iLiveManager.setUserPwd(null);
							iLiveManager.setSalt(null);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "连接成功");
					AppLoginValid validRegex = new AppLoginValid();
					validRegex.setRoomNeedLogin(2);
					validRegex.setUserInfo(iLiveManager);
					Gson gson = new Gson();
					String json = gson.toJson(validRegex);
					resultJson.put("data", json);
				} else {
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "连接成功");
					AppLoginValid validRegex = new AppLoginValid();
					validRegex.setRoomNeedLogin(2);
					Gson gson = new Gson();
					String json = gson.toJson(validRegex);
					resultJson.put("data", json);
				}
			} else {
				ILiveManager manager = iLiveManagerMng.checkLogin(loginToken, userId);
				if (manager == null) {
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "连接成功");
					AppLoginValid validRegex = new AppLoginValid();
					validRegex.setRoomNeedLogin(2);
					Gson gson = new Gson();
					String json = gson.toJson(validRegex);
					resultJson.put("data", json);
				} else {
					manager.setUserPwd(null);
					manager.setEnterPrise(null);
					manager.setSalt(null);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "连接成功");
					AppLoginValid validRegex = new AppLoginValid();
					validRegex.setRoomNeedLogin(2);
					validRegex.setUserInfo(manager);
					Gson gson = new Gson();
					String json = gson.toJson(validRegex);
					resultJson.put("data", json);
				}
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 引导鉴权
	 * 
	 * @param fileId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "guide.jspx")
	public void guide(Long fileId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (fileId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "文件ID不能为空");
			resultJson.put("data", "");
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
		ILiveEvent iliveEvent = iLiveEventMng.selectILiveEventByID(mediaFile.getLiveEventId());
		try {
			if (iliveEvent == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播场次不存在");
				resultJson.put("data", "");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else {
				AppJungleUtil jungleUtil = AppJungleUtil.INSTANCE;
				boolean needLogin = jungleUtil.needLoginByEvent(iliveEvent);
				if (needLogin) {
					boolean jungeUserSession = jungleUtil.jungeUserSession(request);
					// 如果登录成功
					if (jungeUserSession) {
						UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
						// 去除掉房间密码
						ILiveViewAuthBill checkILiveViewAuthBill = iLiveViewAuthBillMng
								.checkILiveViewAuthBill(userBean.getUserId(), iliveEvent.getLiveEventId());
						// 引导客户端是否需要报名或者输入房间密码
						if (checkILiveViewAuthBill == null) {
							// 第一步 判断房间是否开启报名
							Integer openSignupSwitch = iliveEvent.getOpenSignupSwitch();
							openSignupSwitch = openSignupSwitch == null ? 0 : openSignupSwitch;
							if (iliveEvent.getViewAuthorized() == 2) {
								if (request.getSession().getAttribute("tempLoginInfo") != null) {
									TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
											.getAttribute("tempLoginInfo");
									if (tempLoginInfo != null) {
										if (tempLoginInfo.getFileId() != null
												&& !tempLoginInfo.getFileId().equals(fileId)) {
											if (tempLoginInfo.getPasswdCheckResult() != null
													&& tempLoginInfo.getPasswdCheckResult()) {
												iliveEvent.setViewAuthorized(1);
											}
										}
									}
								}
							}
						} else {
							// 对于历史已经鉴定的了 观看方式直接设置为公开观看
							iliveEvent.setViewAuthorized(1);
						}
						ILiveLiveRoom iliveRoom = liveRoomMng.getIliveRoom(iliveEvent.getRoomId());
						iliveRoom.setLiveEvent(iliveEvent);
						JSONObject jsonObject = iliveRoom.toViewBean(iliveRoom, null);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "返回信息成功");
						resultJson.put("data", jsonObject);
					} else {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "用户没有登录");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
				} else {
					if (iliveEvent.getViewAuthorized() == 2) {
						if (request.getSession().getAttribute("tempLoginInfo") != null) {
							TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
									.getAttribute("tempLoginInfo");
							if (tempLoginInfo != null) {
								if (tempLoginInfo.getPasswdCheckResult() != null
										&& tempLoginInfo.getPasswdCheckResult()) {
									iliveEvent.setViewAuthorized(1);
								}
							}
						}
					}
					ILiveLiveRoom iliveRoom = liveRoomMng.getIliveRoom(iliveEvent.getRoomId());
					iliveRoom.setLiveEvent(iliveEvent);
					JSONObject jsonObject = iliveRoom.toViewBean(iliveRoom, null);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "返回信息成功");
					resultJson.put("data", jsonObject);
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 最后一次鉴权
	 * 
	 * @param liveEventId
	 * @param request
	 * @return
	 */
	public boolean jungeAuthFinish(Long liveEventId, HttpServletRequest request) {
		ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
		if (iLiveEvent != null) {
			AppJungleUtil jungleUtil = AppJungleUtil.INSTANCE;
			boolean needLogin = jungleUtil.needLoginByEvent(iLiveEvent);
			if (needLogin) {
				boolean jungeUserSession = jungleUtil.jungeUserSession(request);
				if (jungeUserSession) {
					UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
					// 判断鉴权历史
					ILiveViewAuthBill checkILiveViewAuthBill = iLiveViewAuthBillMng
							.checkILiveViewAuthBill(userBean.getUserId(), iLiveEvent.getLiveEventId());
					int viewAuthor = iLiveEvent.getViewAuthorized();
					if (checkILiveViewAuthBill == null) {
						if (viewAuthor == 4) {
							// 判断是否在白名单里面
							boolean checkwhiteListExist = iLiveViewWhiteBillMng
									.checkUserInWhiteList(userBean.getUserId(), iLiveEvent.getLiveEventId());
							if (checkwhiteListExist) {
								// 设置房间为公开观看
								// 加入鉴权表
								ILiveViewAuthBill buildViewAuth = this.buildViewAuth(4, userBean.getUserId(),
										iLiveEvent);
								iLiveViewAuthBillMng.addILiveViewAuthBill(buildViewAuth);
								return true;
							}
						}
						// 登录观看的类型鉴权
						if (viewAuthor == 5) {
							// 加入鉴权表
							ILiveViewAuthBill buildViewAuth = this.buildViewAuth(5, userBean.getUserId(), iLiveEvent);
							iLiveViewAuthBillMng.addILiveViewAuthBill(buildViewAuth);
							return true;
						}
						// 密码观看的鉴权
						if (viewAuthor == 2) {
							Boolean passwdCheckResult = userBean.getPasswdCheckResult();
							if (passwdCheckResult == null) {
								passwdCheckResult = false;
							}
							if (passwdCheckResult) {
								ILiveViewAuthBill buildViewAuth = this.buildViewAuth(3, userBean.getUserId(),
										iLiveEvent);
								iLiveViewAuthBillMng.addILiveViewAuthBill(buildViewAuth);
								return true;
							}
						}
					} else {
						return true;
					}
				} else {
					return true;
				}
			} else {
				return true;
			}

		}
		return false;
	}

	/**
	 * 获得文件真实信息
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param httpServletResponse
	 * @param httpServletRequest
	 */
	@RequestMapping(value = "fileinfo.jspx")
	public void getFileInfo(Long fileId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
		if (mediaFile == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "资源文件不存在");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		ILiveEvent iliveEvent = iLiveEventMng.selectILiveEventByID(mediaFile.getLiveEventId());
		boolean jungeAuthFinish = this.jungeAuthFinish(iliveEvent.getLiveEventId(), request);
		if (jungeAuthFinish) {
			Integer serverMountId = mediaFile.getServerMountId();
			ILiveServerAccessMethod accessMethodByMountId = serverAccessMethodMng
					.getAccessMethodByMountId(serverMountId);
			if (accessMethodByMountId != null) {
				MountInfo mountInfo = accessMethodByMountId.getMountInfo();
				String allPath = "http://" + accessMethodByMountId.getHttp_address() + ":"
						+ accessMethodByMountId.getUmsport() + mountInfo.getBase_path() + mediaFile.getFilePath();
				mediaFile.setFilePath(allPath);
			}
			JSONObject jsonObject = mediaFile.toMediaFileJson();
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "点播文件获取成功");
			resultJson.put("data", jsonObject);
		} else {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "你没有观看该直播间的权限");
			resultJson.put("data", new JSONObject());
		}

		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 发表评论post提交
	 * 
	 * @param userId
	 * @param comments
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "pushcomments.jspx", method = RequestMethod.POST)
	public void receiveCommentsPost(Long userId, String comments, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
		if (userId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户没有登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} else if (userBean == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户没有登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} else {

		}

	}

	/**
	 * 发表评论
	 * 
	 * @param userId
	 * @param fileId
	 * @param comments
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "pushcomments.jspx", method = RequestMethod.GET)
	public void receiveCommentsGet(Long userId, Long fileId, String comments, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
		if (userId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户没有登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		if (userBean.getUserId() == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户没有登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		if (comments != null) {
			try {
				comments = URLDecoder.decode(comments, "UTF-8");
				ILiveMediaFileComments commentsBean = new ILiveMediaFileComments();
				commentsBean.setCheckState(0);
				commentsBean.setAppreciateCount(0L);
				commentsBean.setCreateTime(new Timestamp(System.currentTimeMillis()));
				commentsBean.setDelFlag(0);
				ILiveMediaFile selectILiveMediaFile = iLivemediaFileMng.selectILiveMediaFileByFileId(fileId);
				commentsBean.setiLiveMediaFile(selectILiveMediaFile);
				commentsBean.setUserId(Long.parseLong(userBean.getUserId()));
				commentsBean.setObjectionCount(0L);
				commentsBean.setComments(ExpressionUtil.replaceTitleToKey(comments));
				commentsBean.setCommentsUser(userBean.getNickname());
				Long commentId = commentsMng.addFileComments(commentsBean);
				ILiveMediaFileComments comment = commentsMng.getFileCommentById(commentId);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "评论发表成功");
				resultJson.put("data", comment.toSimpleJsonObj());
				ResponseUtils.renderJson(request, response, resultJson.toString());
			} catch (Exception e) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "评论发表失败");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				e.printStackTrace();
			}
		} else {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "你没有发表评论");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
		return;
	}

	/**
	 * 推送评论列表向用户
	 * 
	 * @param fileId
	 */
	@RequestMapping(value = "getcomments.jspx")
	public void pushCommentsList2User(Long fileId, Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			Pagination fileComments = commentsMng.getFileComments(fileId, pageNo == null ? 1 : pageNo,
					pageSize == null ? 10 : pageSize);
			List<ILiveMediaFileComments> list = (List<ILiveMediaFileComments>) fileComments.getList();
			List<JSONObject> jsonobjs = new ArrayList<>();
			if (list != null && !list.isEmpty()) {
				for (ILiveMediaFileComments comments : list) {
					JSONObject jobj = comments.toSimpleJsonObj();
					jsonobjs.add(jobj);
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取评论成功");
			resultJson.put("data", jsonobjs);
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取评论失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());

	}

	@RequestMapping(value = "/checkFilePassword.jspx")
	public void checkRoomPassword(Long fileId, String passWord, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (passWord == null || "".equals(passWord)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "密码没有输入");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
		if (mediaFile == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "资源文件不存在");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		ILiveEvent iliveEvent = iLiveEventMng.selectILiveEventByID(mediaFile.getLiveEventId());
		if (iliveEvent != null) {
			String viewPassword = iliveEvent.getViewPassword();
			if (viewPassword.equals(passWord)) {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "密码输入成功");
				TempLoginInfo loginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
				if (loginInfo != null) {
					loginInfo.setPasswdCheckResult(true);
					loginInfo.setFileId(fileId);
				}
				request.getSession().setAttribute("tempLoginInfo", loginInfo);
				resultJson.put("data", "");
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "密码输入错误");
				resultJson.put("data", "");
			}
		} else {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "密码输入错误");
			resultJson.put("data", "");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 自动登录
	 * 
	 * @param request
	 * @param manager
	 */
	private void autoLogin(HttpServletRequest request, ILiveManager manager) {
		UserBean userBean = new UserBean();
		userBean = this.convertManager2UserBean(manager, userBean);
		request.getSession().setAttribute("appUser", userBean);
		manager.setLoginToken(UUID.randomUUID().toString().replace("-", ""));
		iLiveManagerMng.updateLiveManager(manager);
	}

	/**
	 * 转换PO为VO
	 * 
	 * @param manager
	 * @param userBean
	 * @return
	 */
	private UserBean convertManager2UserBean(ILiveManager manager, UserBean userBean) {
		userBean.setUserId(String.valueOf(manager.getUserId()));
		userBean.setNickname(manager.getNailName());
		userBean.setUserThumbImg(manager.getUserImg());
		userBean.setUserId(String.valueOf(manager.getUserId()));
		return userBean;
	}

	/**
	 * 构建鉴权
	 * 
	 * @param authType
	 * @param userId
	 * @param iliveRoom
	 * @return
	 */
	private ILiveViewAuthBill buildViewAuth(int authType, String userId, ILiveEvent liveEvent) {
		ILiveViewAuthBill authbill = new ILiveViewAuthBill();
		authbill.setAuthPassTime(new Timestamp(System.currentTimeMillis()));
		authbill.setAuthType(authType);
		authbill.setDeleteStatus(0);
		authbill.setLiveEventId(liveEvent.getLiveEventId());
		authbill.setLiveRoomId(liveEvent.getRoomId());
		authbill.setUserId(userId);
		return authbill;
	}
}
