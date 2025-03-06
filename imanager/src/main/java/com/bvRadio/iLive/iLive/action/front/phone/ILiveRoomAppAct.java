package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppJungleUtil;
import com.bvRadio.iLive.iLive.action.front.vo.AppLoginValid;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.action.front.vo.TempLoginInfo;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.BBSDiyformDataMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewAuthBillMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/app/room")
public class ILiveRoomAppAct {

	private static final String HTTP_PROTOACAL = "http://";

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;

	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private ILivePageDecorateMng pageDecorateMng;

	@Autowired
	private ILiveViewAuthBillMng iLiveViewAuthBillMng;

	@Autowired
	private BBSDiyformDataMng bbsDiyfromDataMng;

	@Autowired
	private ILiveViewWhiteBillMng iLiveViewWhiteBillMng;

	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;

	@Autowired
	private ILiveEnterpriseFansMng iLiveEnterpriseFansMng;

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@RequestMapping(value = "checkRoomLogin.jspx")
	public void checkRoomLogin(Integer roomId, Long userId, String loginToken, String token, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		AppJungleUtil jungleUtil = AppJungleUtil.INSTANCE;
		if (request.getSession().getAttribute("tempLoginInfo") == null) {
			request.getSession().setAttribute("tempLoginInfo", new TempLoginInfo());
		}
		boolean needLogin = jungleUtil.needLogin(iliveRoom);
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
		return userBean;
	}

	/**
	 * 获取房间信息
	 * 
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getRoomInfo.jspx")
	public void getRoomInfo(Integer roomId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (roomId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播房间ID不能为空");
			resultJson.put("data", "");
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		try {
			if (iliveRoom == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播房间不存在");
				resultJson.put("data", "");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else {
				ILiveEvent liveEvent = iliveRoom.getLiveEvent();
				AppJungleUtil jungleUtil = AppJungleUtil.INSTANCE;
				boolean needLogin = jungleUtil.needLogin(iliveRoom);
				if (needLogin) {
					boolean jungeUserSession = jungleUtil.jungeUserSession(request);
					// 如果登录成功
					if (jungeUserSession) {
						UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
						// 去除掉房间密码
						// liveEvent.setViewPassword(null);
						ILiveViewAuthBill checkILiveViewAuthBill = iLiveViewAuthBillMng.checkILiveViewAuthBill(
								userBean.getUserId(), iliveRoom.getLiveEvent().getLiveEventId());
						// 引导客户端是否需要报名或者输入房间密码
						if (checkILiveViewAuthBill == null) {
							// 第一步 判断房间是否开启报名
							Integer openSignupSwitch = liveEvent.getOpenSignupSwitch();
							openSignupSwitch = openSignupSwitch == null ? 0 : openSignupSwitch;
							if (openSignupSwitch == 1) {
								// 判断是否报名通过
								boolean signUp = bbsDiyfromDataMng.checkUserSignUp(userBean.getUserId(),
										liveEvent.getFormId());
								if (signUp) {
									// 报名过了 设置房间为不需要报名
									liveEvent.setOpenSignupSwitch(0);
								} else {
									liveEvent.setOpenSignupSwitch(1);
									// 设置报名地址
									ILiveServerAccessMethod accessMethodBySeverGroupId = null;
									try {
										accessMethodBySeverGroupId = accessMethodMng
												.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
										String h5HttpUrl = accessMethodBySeverGroupId.getH5HttpUrl();
										liveEvent.setDiyfromAddr(
												h5HttpUrl + "/form.html?roomId=" + iliveRoom.getRoomId());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							if (liveEvent.getViewAuthorized() == 2) {
								if (request.getSession().getAttribute("tempLoginInfo") != null) {
									TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
											.getAttribute("tempLoginInfo");
									if (tempLoginInfo != null) {
										if (tempLoginInfo.getPasswdCheckResult() != null
												&& tempLoginInfo.getPasswdCheckResult()) {
											liveEvent.setViewAuthorized(1);
										}
									}

								}
							}
						} else {
							// 对于历史已经鉴定的了 观看方式直接设置为公开观看
							liveEvent.setViewAuthorized(1);
							liveEvent.setOpenSignupSwitch(0);
						}
						Integer enterpriseId = iliveRoom.getEnterpriseId();
						ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
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
								Integer menuType = page.getMenuType();
								if (menuType == 3) {
									continue;
								}
								newPageDecorate.add(page);
							}
						} else if (!pageType2 && pageType3) {
							for (PageDecorate page : list) {
								Integer menuType = page.getMenuType();
								if (menuType == 3) {
									page.setMenuName("直播聊天");
									page.setMenuType(2);
								}
								newPageDecorate.add(page);
							}
						} else {
							for (PageDecorate page : list) {
								newPageDecorate.add(page);
							}
						}
						liveEvent.setPageRecordList(newPageDecorate);
						boolean exist = iLiveEnterpriseFansMng.isExist(enterpriseId,
								Long.parseLong(userBean.getUserId()));
						Integer concernStatus = 0;
						if (exist) {
							concernStatus = 1;
						}
						iLiveEnterPrise.setConcernStatus(concernStatus);
						JSONObject jsonObject = iliveRoom.toViewBean(iliveRoom, iLiveEnterPrise);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "返回信息成功");
						resultJson.put("data", jsonObject);
					} else {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "用户没有登录");
						resultJson.put("data", "");
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
				} else {
					if (liveEvent.getViewAuthorized() == 2) {
						if (request.getSession().getAttribute("tempLoginInfo") != null) {
							TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
									.getAttribute("tempLoginInfo");
							if (tempLoginInfo != null) {
								if (tempLoginInfo.getPasswdCheckResult() != null
										&& tempLoginInfo.getPasswdCheckResult()) {
									liveEvent.setViewAuthorized(1);
								}
							}
						}
					}
					Integer enterpriseId = iliveRoom.getEnterpriseId();
					ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
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
							Integer menuType = page.getMenuType();
							if (menuType == 3) {
								continue;
							}
							newPageDecorate.add(page);
						}
					} else if (!pageType2 && pageType3) {
						for (PageDecorate page : list) {
							Integer menuType = page.getMenuType();
							if (menuType == 3) {
								page.setMenuName("直播聊天");
								page.setMenuType(2);
							}
							newPageDecorate.add(page);
						}
					} else {
						for (PageDecorate page : list) {
							newPageDecorate.add(page);
						}
					}
					liveEvent.setPageRecordList(newPageDecorate);
					JSONObject jsonObject = iliveRoom.toViewBean(iliveRoom, iLiveEnterPrise);
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
	 * 鉴权完成
	 * 
	 * @param roomId
	 * @param request
	 */
	public boolean jungeAuthFinish(Integer roomId, HttpServletRequest request) {
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		if (iliveRoom != null) {
			AppJungleUtil jungleUtil = AppJungleUtil.INSTANCE;
			boolean needLogin = jungleUtil.needLogin(iliveRoom);
			if (needLogin) {
				boolean jungeUserSession = jungleUtil.jungeUserSession(request);
				if (jungeUserSession) {
					UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
					ILiveEvent liveEvent = iliveRoom.getLiveEvent();
					// 判断鉴权历史
					ILiveViewAuthBill checkILiveViewAuthBill = iLiveViewAuthBillMng
							.checkILiveViewAuthBill(userBean.getUserId(), liveEvent.getLiveEventId());
					int viewAuthor = liveEvent.getViewAuthorized();
					if (checkILiveViewAuthBill == null) {
						if (viewAuthor == 4) {
							// 判断是否在白名单里面
							boolean checkwhiteListExist = iLiveViewWhiteBillMng
									.checkUserInWhiteList(userBean.getUserId(), liveEvent.getLiveEventId());
							if (checkwhiteListExist) {
								// 设置房间为公开观看
								// 加入鉴权表
								ILiveViewAuthBill buildViewAuth = this.buildViewAuth(4, userBean.getUserId(),
										iliveRoom);
								iLiveViewAuthBillMng.addILiveViewAuthBill(buildViewAuth);
								return true;
							}
						}
						// 登录观看的类型鉴权
						if (viewAuthor == 5) {
							// 加入鉴权表
							ILiveViewAuthBill buildViewAuth = this.buildViewAuth(5, userBean.getUserId(), iliveRoom);
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
										iliveRoom);
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
	 * 构建鉴权
	 * 
	 * @param authType
	 * @param userId
	 * @param iliveRoom
	 * @return
	 */
	private ILiveViewAuthBill buildViewAuth(int authType, String userId, ILiveLiveRoom iliveRoom) {
		ILiveViewAuthBill authbill = new ILiveViewAuthBill();
		authbill.setAuthPassTime(new Timestamp(System.currentTimeMillis()));
		authbill.setAuthType(authType);
		authbill.setDeleteStatus(0);
		authbill.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
		authbill.setLiveRoomId(iliveRoom.getRoomId());
		authbill.setUserId(userId);
		return authbill;
	}

	/**
	 * 鉴定房间密码
	 * 
	 * @param roomId
	 * @param roomPassword
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/checkRoomPassword.jspx")
	public void checkRoomPassword(Integer roomId, String roomPassword, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		if (iliveRoom != null) {
			String viewPassword = iliveRoom.getLiveEvent().getViewPassword();
			if (viewPassword.equals(roomPassword)) {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "密码输入成功");
				TempLoginInfo loginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
				if (loginInfo != null) {
					loginInfo.setPasswdCheckResult(true);
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
	 * 关注和取关 type 0为取关 1为关注
	 * 
	 * @param type
	 * @param enterpriseId
	 * @param userId
	 * @param request
	 */
	@RequestMapping(value = "enterprise/concern.jspx")
	public void concernState(Integer type, Integer enterpriseId, Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (userId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户ID不能为空");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		if (enterpriseId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "企业ID不能为空");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			if (type == 1) {
				try {
					boolean addEnterpriseConcern = iLiveEnterpriseFansMng.addEnterpriseConcern(enterpriseId, userId);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "关注成功");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
				} catch (Exception e) {
					e.printStackTrace();
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "关注失败");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
				}
			} else {
				try {
					iLiveEnterpriseFansMng.deleteEnterpriseConcern(enterpriseId, userId);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "取消关注成功");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
				} catch (Exception e) {
					e.printStackTrace();
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "取消关注失败");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
				}
			}

		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作失败");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 最后一次进入直播间
	 * 
	 * @param roomId
	 * @param sessionType
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/roomenter.jspx")
	public void enterLiveRoomIng(Integer roomId, Integer sessionType, HttpServletRequest request,
			HttpServletResponse response) {
		boolean jungeAuthFinish = this.jungeAuthFinish(roomId, request);
		if (jungeAuthFinish) {
			UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
			// 从观看端进来的人都是以个人身份
			if (userBean != null) {
				this.entLiveLoginByUser(userBean.getUserId(), roomId, sessionType, 0, request, response);
			} else {
				this.entLiveLogin(roomId, sessionType, 0, request, response);
			}
		} else {
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "鉴权失败");
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
	}

	public void entLiveLoginByUser(String userId, Integer liveId, Integer sessionType, Integer userType,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom live = iLiveRoomMng.findById(liveId);
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
				liveUser.setLevel(iLiveManager.getLevel());
				liveUser.setSessionType(sessionType);
			} catch (Exception e) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户获取失败");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new Hashtable<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			System.out.println("生存客户端登录token 的liveid=" + liveId);
			userMap.put(userId, liveUser);
			// 生存客户端登录token
			UUID uuid = UUID.randomUUID();
			String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
			JSONObject json = new JSONObject();
			json.put("liveId", liveId);
			json.put("userId", userId);
			CacheManager.putCacheInfo(CacheManager.mobile_token_ + token, json, 5 * 60 * 1000);
			/**
			 * 获取直播间推流地址
			 */
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = accessMethodMng.getAccessMethodBySeverGroupId(live.getServerGroupId());
				System.out.println(accessMethodBySeverGroupId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String pushStreamAddr = HTTP_PROTOACAL + accessMethodBySeverGroupId.getHttp_address() + ":"
					+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + live.getRoomId()
					+ "/5000k/tzwj_video.m3u8";
			json.put("hlsAddr", pushStreamAddr);
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
			resultJson.put("hlsAddr", pushStreamAddr);
			resultJson.put("token", token);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void entLiveLogin(Integer liveId, Integer sessionType, Integer userType, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {

			ILiveLiveRoom live = iLiveRoomMng.findById(liveId);
			if (live == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播不存在");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			String userId = request.getSession().getId();
			UserBean liveUser = new UserBean();
			liveUser.setUserType(userType);
			liveUser.setSessionType(sessionType);
			liveUser.setUserId(userId);
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new Hashtable<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			System.out.println("生存客户端登录token 的liveid=" + liveId);
			userMap.put(userId, liveUser);
			// 生存客户端登录token
			UUID uuid = UUID.randomUUID();
			String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
			JSONObject json = new JSONObject();
			json.put("liveId", liveId);
			json.put("userId", userId);
			CacheManager.putCacheInfo(CacheManager.mobile_token_ + token, json, 5 * 60 * 1000);
			/**
			 * 获取直播间推流地址
			 */
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = accessMethodMng.getAccessMethodBySeverGroupId(live.getServerGroupId());
				System.out.println(accessMethodBySeverGroupId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String pushStreamAddr = HTTP_PROTOACAL + accessMethodBySeverGroupId.getHttp_address() + ":"
					+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + live.getRoomId()
					+ "/5000k/tzwj_video.m3u8";
			json.put("hlsAddr", pushStreamAddr);
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
			resultJson.put("hlsAddr", pushStreamAddr);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 获取回看列表
	 * @param pageNo
	 * @param pageSize
	 * @param roomId
	 * @param httpServletResponse
	 * @param httpServletRequest
	 */
	@RequestMapping("/getrecordlist.jspx")
	public void getMediaRecordList(Integer pageNo, Integer pageSize, Integer roomId,
			HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
		JSONObject resultJson = new JSONObject();
		Map<String, Object> sqlParam = new HashMap<>();
		sqlParam.put("roomId", roomId);
		List<AppMediaFile> mediaFilePageByRoom = iLiveMediaFileMng.getMediaFilePageByRoom(sqlParam,
				pageNo == null ? 1 : pageNo, 20);
		JSONArray jArr = new JSONArray();
		if (mediaFilePageByRoom != null && !mediaFilePageByRoom.isEmpty()) {
			JSONObject jo = null;
			for (AppMediaFile mediaFile : mediaFilePageByRoom) {
				jo = new JSONObject(mediaFile);
				jArr.put(jo);
			}
		}
		resultJson.put("code", 1);
		resultJson.put("message", "信息获取成功");
		resultJson.put("data", jArr);
		ResponseUtils.renderJson(httpServletRequest, httpServletResponse, resultJson.toString());
	}

	/**
	 * 获取直播间列表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/roomlist.jspx")
	public void getRoomList(String keyWord, Integer searchType, Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			Pagination pager = iLiveRoomMng.getPagerForView(keyWord, pageNo, pageSize, searchType);
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
			resultJson.put("data", "");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 获取企业列表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/enterpriselist.jspx")
	public void getEnterpriseList(String keyWord, Integer searchType, Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			Pagination pager = iLiveEnterpriseMng.getPagerForView(keyWord, pageNo, pageSize, searchType);
			List<ILiveEnterprise> list = (List<ILiveEnterprise>) pager.getList();
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			if (list != null) {
				for (ILiveEnterprise enterprise : list) {
					JSONObject simpleJsonObject = enterprise.toSimpleJsonObject();
					jsonList.add(simpleJsonObject);
				}
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取企业列表成功");
				resultJson.put("data", jsonList);
			} else {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取企业列表成功");
				resultJson.put("data", jsonList);
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取企业列表失败");
			resultJson.put("data", "");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

}
