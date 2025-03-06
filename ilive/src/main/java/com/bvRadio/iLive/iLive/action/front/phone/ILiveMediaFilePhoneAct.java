package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.NOT_BIND_MOBILE;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.RequestUtils;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppJungleUtil;
import com.bvRadio.iLive.iLive.action.front.vo.AppLoginValid;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.action.front.vo.TempLoginInfo;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseTerminalUser;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthentication;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthenticationRecord;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;
import com.bvRadio.iLive.iLive.entity.ILiveViewRecord;
import com.bvRadio.iLive.iLive.entity.MountInfo;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseTerminalUserMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationRecordMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileViewStaticsMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileWhiteBillMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewAuthBillMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewRecordMng;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.IPUtils;
import com.bvRadio.iLive.iLive.util.SafeTyChainPasswdUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.google.gson.Gson;


/**
 * 回看观看端
 * @author administrator
 *
 */
@Controller
@RequestMapping(value = "/app/room/vod")
public class ILiveMediaFilePhoneAct {

	Logger logger = LoggerFactory.getLogger(ILiveMediaFilePhoneAct.class);
	private static final String HTTP_PROTOCAL = "http://";
	
	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private ILiveServerAccessMethodMng serverAccessMethodMng;

	@Autowired
	private ILiveMediaFileCommentsMng commentsMng;

	@Autowired
	private ILiveMediaFileMng iLivemediaFileMng;

	@Autowired
	private ILiveViewRecordMng iLiveViewRecordMng;
	@Autowired
	private ILiveViewAuthBillMng iLiveViewAuthBillMng;
	@Autowired
	private ILiveEnterpriseTerminalUserMng terminalUserMng;

	@Autowired
	private ILiveFileViewStaticsMng iLiveFileViewStaticsMng;

	@Autowired
	private ILiveFileAuthenticationMng iLiveFileAuthenticationMng;

	@Autowired
	private ILiveFileAuthenticationRecordMng iLiveFileAuthenticationRecordMng;

	@Autowired
	private ILiveFileWhiteBillMng iLiveFileWhiteBillMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	/**
	 * 检验登录
	 * @param fileId
	 * @param userId
	 * @param loginToken
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "checklogin.jspx")
	public void judgeMediaFile(Long fileId, Long userId, String loginToken, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
			if (mediaFile == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "文件不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			Integer onlineFlag = mediaFile.getOnlineFlag();
			Integer del=mediaFile.getDelFlag();
			if ((null != onlineFlag && onlineFlag.equals(0))||(del!=null&&del.equals(1))) {
				JSONObject dataJson = new JSONObject();
				dataJson.put("onlineFlag", onlineFlag);
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "文件已下架");
				resultJson.put("data", dataJson);
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			AppJungleUtil jungleUtil = AppJungleUtil.INSTANCE;
			if (request.getSession().getAttribute("tempLoginInfo") == null) {
				request.getSession().setAttribute("tempLoginInfo", new TempLoginInfo());
			}
			ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng.getFileAuthenticationByFileId(fileId);
			if (fileAuth == null) {
				fileAuth = new ILiveFileAuthentication();
				fileAuth.setAuthType(1);
				fileAuth.setFileId(fileId);
				fileAuth.setViewPassword("");
			}
			boolean needLogin = jungleUtil.needLoginByEvent(fileAuth);
			System.out.println("needLogin==========>>>>>>>"+needLogin);
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
							Gson gson = new Gson();
							String json = gson.toJson(validRegex);
							resultJson.put("data", json);
						} else {
							resultJson.put("code", SUCCESS_STATUS);
							resultJson.put("message", "连接成功");
							AppLoginValid validRegex = new AppLoginValid();
							validRegex.setRoomNeedLogin(1);
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
					Gson gson = new Gson();
					String json = gson.toJson(validRegex);
					resultJson.put("data", json);
				}
			} else {
				if (loginToken == null || userId == null) {
					UserBean userBean = ILiveUtils.getAppUser(request);
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
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "进入回看页失败");
			resultJson.put("data", new JSONObject());
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
	public void guide(Long fileId, Integer roomId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (fileId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "文件ID不能为空");
			resultJson.put("data", "");
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
		try {
			if (mediaFile == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "回看文件不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else {
				AppJungleUtil jungleUtil = AppJungleUtil.INSTANCE;
				ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng.getFileAuthenticationByFileId(fileId);
				Integer authType = null;
				Double viewMoney = null;
				String viewPassword = null;
				String welcomeMsg = null;
				Integer openFCodeSwitch = null;
				if (fileAuth != null) {
					authType = fileAuth.getAuthType();
					viewMoney = fileAuth.getViewMoney();
					viewPassword = fileAuth.getViewPassword();
					welcomeMsg = fileAuth.getWelcomeMsg();
					openFCodeSwitch = fileAuth.getOpenFCodeSwitch();
				}
				authType = authType == null ? ILiveFileAuthentication.AUTH_TYPE_FREE : authType;
				boolean needLogin = jungleUtil.needLoginByEvent(fileAuth);
				ILiveLiveRoom iliveRoom = this.buildRawILiveRoom();
				if (roomId != null) {
				} else {
					roomId = 0;
				}
				iliveRoom.setRoomId(roomId);
				iliveRoom.setServerGroupId(Integer.parseInt(ConfigUtils.get("defaultVodServerGroupId")));
				ILiveEvent iliveEvent = iliveRoom.getLiveEvent();
				iliveEvent.setWelcomeMsg(welcomeMsg);
				iliveEvent.setOpenFCodeSwitch(openFCodeSwitch);
				iliveEvent.setViewMoney(viewMoney);
				if (needLogin) {
					boolean jungeUserSession = jungleUtil.jungeUserSession(request);
					// 如果登录成功
					if (jungeUserSession) {
						UserBean userBean = ILiveUtils.getAppUser(request);
						// 去除掉房间密码
						ILiveFileAuthenticationRecord authRecord = iLiveFileAuthenticationRecordMng
								.getILiveFileViewAuthBill(Long.parseLong(userBean.getUserId()), fileId);
						// 引导客户端是否需要报名或者输入房间密码
						if (authRecord == null) {
							// 是否开启密码观看
							if (ILiveFileAuthentication.AUTH_TYPE_PASSWORD.equals(authType)) {
								TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
										.getAttribute("tempLoginInfo");
								if (tempLoginInfo != null) {
									String loginResult = tempLoginInfo.getFileMap().get(fileId);
									if (loginResult != null && !loginResult.equals("")
											&& loginResult.equals(fileAuth.getViewPassword())) {
										iliveEvent.setViewAuthorized(ILiveEvent.VIEW_AUTHORIZED_FREE);
									} else {
										iliveEvent.setViewAuthorized(ILiveEvent.VIEW_AUTHORIZED_PASSWORD);
									}
								} else {
									iliveEvent.setViewAuthorized(ILiveEvent.VIEW_AUTHORIZED_PASSWORD);
								}
							}
							if (ILiveFileAuthentication.AUTH_TYPE_PAY.equals(authType)) {
								iliveEvent.setViewAuthorized(ILiveEvent.VIEW_AUTHORIZED_PAY);
							}
							if (ILiveFileAuthentication.AUTH_TYPE_MOBILE.equals(authType)) {
								iliveEvent.setViewAuthorized(ILiveEvent.VIEW_AUTHORIZED_LOGIN);
							}
							
							if(ILiveFileAuthentication.AUTH_TYPE_FCODE.equals(authType)){
								ILiveViewAuthBill checkILiveViewAuthBill1 = iLiveViewAuthBillMng.checkILiveViewAuthBillByFileId(
										userBean.getUserId(), fileId);
								if(checkILiveViewAuthBill1!=null){
									iliveEvent.setViewAuthorized(ILiveEvent.VIEW_AUTHORIZED_FREE);
								}else{
									iliveEvent.setViewAuthorized(ILiveEvent.VIEW_AUTHORIZED_FCODE);
								}
							}
							 //如果是第三方授權观看 詢問是否有權限
							if(authType==7){
								//根據userId 查詢第三方的userId
								System.out.println("用户id："+userBean.getUserId());
								ILiveManager manager=iLiveManagerMng.selectILiveManagerById(Long.parseLong(userBean.getUserId()));
								Map<String, String> map = new HashMap<String, String>();
								map.put("userId", manager.getZhxyuserId());
								map.put("roomId", iliveRoom.getRoomId().toString());
								map.put("fileId", fileId.toString());
								String checkUrl=iLiveEnterpriseMng.getILiveEnterPriseByAppId(manager.getZhxyappId()).getCheckIfCanUrl();
								if(checkUrl!=null){
									String postJson = HttpUtils.sendPost(checkUrl, map, "UTF-8");
									if(postJson!=null){
										JSONObject jsonObject = new JSONObject(postJson);
										Integer code=jsonObject.getInt("code");
										System.out.println("map=============="+code);
										if(code==1){
											JSONObject data=jsonObject.getJSONObject("data");
											Integer access=data.getInt("access");
											if(access==1){
												iliveEvent.setViewAuthorized(1);
											}else{
												String redirectUrl=data.getString("redirectUrl");
												iliveEvent.setOutLinkUrl(redirectUrl);
											}
										}
									}
								}
							}
							// 对于登录观看的鉴权如果登录成功的直接设置为公开观看
							if (authType == 0) {
								iliveEvent.setViewAuthorized(ILiveEvent.VIEW_AUTHORIZED_FREE);
							}

							// 对于登录观看的鉴权如果登录成功的直接设置为公开观看
							if (iliveEvent.getViewAuthorized() == 5) {
								iliveEvent.setViewAuthorized(ILiveEvent.VIEW_AUTHORIZED_FREE);
							}
							
						} else {
							// 对于历史已经鉴定的了 观看方式直接设置为公开观看
							iliveEvent.setViewAuthorized(1);
						}
						//已经登陆用户先判断一下房间鉴权是不是白名单/付费观看，如果是白名单/付费观看需要判断用户是否有绑定了手机，没有绑定那个手机的情况需要让用户绑定手机
						Integer viewAuthorized = iliveRoom.getLiveEvent().getViewAuthorized();
						if (ILiveEvent.VIEW_AUTHORIZED_WHITE_LIST.equals(viewAuthorized)
								|| ILiveEvent.VIEW_AUTHORIZED_PAY.equals(viewAuthorized)
								||(ILiveEvent.VIEW_AUTHORIZED_FREE.equals(viewAuthorized)&&needLogin==true)
								||(ILiveEvent.VIEW_AUTHORIZED_PASSWORD.equals(viewAuthorized)&&needLogin==true)
								|| ILiveEvent.VIEW_AUTHORIZED_FCODE.equals(viewAuthorized)
								|| ILiveEvent.VIEW_AUTHORIZED_THIRD.equals(viewAuthorized)) {
							ILiveManager liveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userBean.getUserId()));
							if(liveManager!= null && (liveManager.getMobile()==null || "".equals(liveManager.getMobile()))){
								resultJson.put("code", NOT_BIND_MOBILE);
							}else{
								resultJson.put("code", SUCCESS_STATUS);
							}
						}else{
							resultJson.put("code", SUCCESS_STATUS);
						}
						JSONObject jsonObject = iliveRoom.toViewBean(iliveRoom, iliveEvent, null);
						// 添加输入密码页面地址
						String appPasswordAddr;
						try {
							ILiveServerAccessMethod accessMethodBySever = accessMethodMng
									.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
							appPasswordAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/apppassword.html?roomId="
									+ roomId+"&fileId="+fileId;
						} catch (Exception e) {
							e.printStackTrace();
							appPasswordAddr = "";
						}
						//添加输入观看码页面地址
						String appFcodeAddr;
						try {
							ILiveServerAccessMethod accessMethodBySever = accessMethodMng
									.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
							appFcodeAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/appFCode.html?roomId="
									+ roomId+"&fileId="+fileId;
						} catch (Exception e) {
							e.printStackTrace();
							appFcodeAddr = "";
						}
						jsonObject.put("appPasswordAddr", appPasswordAddr);
						jsonObject.put("appFcodeAddr", appFcodeAddr);
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
					boolean jungeUserSession = jungleUtil.jungeUserSession(request);
					if (jungeUserSession) {
						UserBean userBean = ILiveUtils.getAppUser(request);
						// 去除掉房间密码

						if (authType == 2) {
							ILiveFileAuthenticationRecord authRecord = iLiveFileAuthenticationRecordMng
									.getILiveFileViewAuthBill(Long.parseLong(userBean.getUserId()), fileId);
							if (authRecord == null) {
								TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
										.getAttribute("tempLoginInfo");
								if (tempLoginInfo != null) {
									String loginResult = tempLoginInfo.getFileMap().get(fileId);
									if (loginResult != null && !loginResult.equals("")
											&& loginResult.equals(viewPassword)) {
										iliveEvent.setViewAuthorized(1);
									} else {
										iliveEvent.setViewAuthorized(2);
									}
								} else {
									iliveEvent.setViewAuthorized(2);
								}
							} else {
								iliveEvent.setViewAuthorized(1);
							}
						}
					} else {
						if (authType == 2) {
							TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
									.getAttribute("tempLoginInfo");
							if (tempLoginInfo != null) {
								String loginResult = tempLoginInfo.getFileMap().get(fileId);
								if (loginResult != null && !loginResult.equals("")
										&& loginResult.equals(viewPassword)) {
									iliveEvent.setViewAuthorized(1);
								} else {
									iliveEvent.setViewAuthorized(2);
								}
							} else {
								iliveEvent.setViewAuthorized(2);
							}
						}
					}
					JSONObject jsonObject = iliveRoom.toViewBean(iliveRoom, iliveRoom.getLiveEvent(), null);
					// 添加输入密码页面地址
					String appPasswordAddr;
					try {
						ILiveServerAccessMethod accessMethodBySever = accessMethodMng
								.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
						appPasswordAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/apppassword.html?roomId="
								+ roomId+"&fileId="+fileId;
					} catch (Exception e) {
						appPasswordAddr = "";
					}
					//添加输入观看码页面地址
					String appFcodeAddr;
					try {
						ILiveServerAccessMethod accessMethodBySever = accessMethodMng
								.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
						appFcodeAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/appFCode.html?roomId="
								+ roomId+"&fileId="+fileId;
					} catch (Exception e) {
						appFcodeAddr = "";
					}
					jsonObject.put("appPasswordAddr", appPasswordAddr);
					jsonObject.put("appFcodeAddr", appFcodeAddr);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "返回信息成功");
					resultJson.put("data", jsonObject);
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "后台异常");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	public ILiveLiveRoom buildRawILiveRoom() {
		ILiveLiveRoom iliveRoom = new ILiveLiveRoom();
		iliveRoom.setRoomId(0);
		iliveRoom.setOpenStatus(1);
		ILiveEvent iliveEvent = new ILiveEvent();
		iliveEvent.setViewAuthorized(1);
		iliveEvent.setLiveEventId(0L);
		iliveEvent.setLiveTitle("");
		iliveEvent.setLiveDesc("");
		iliveEvent.setLiveStartTime(new Timestamp(System.currentTimeMillis()));
		iliveEvent.setLiveStatus(1);
		iliveEvent.setConverAddr("");
		iliveEvent.setHostName("");
		iliveEvent.setOpenLogoSwitch(0);
		iliveEvent.setOpenSignupSwitch(0);
		iliveEvent.setOpenGuideSwitch(0);
		iliveEvent.setOpenCountdownSwitch(0);
		iliveEvent.setOpenViewNumSwitch(0);
		iliveEvent.setCountdownTitle("");
		iliveEvent.setGuideAddr("");
		iliveEvent.setPlayBgAddr("");
		iliveEvent.setWelcomeMsg("");
		iliveEvent.setDiyfromAddr("");
		iliveEvent.setAppDiyformAddr("");
		iliveEvent.setLogoUrl("");
		iliveEvent.setLogoPosition(0);
		iliveRoom.setLiveEvent(iliveEvent);
		return iliveRoom;
	}

	/**
	 * 最后一次鉴权
	 * 
	 * @param liveEventId
	 * @param request
	 * @return
	 */
	public boolean jungeAuthFinish(ILiveFileAuthentication fileAuth, HttpServletRequest request) {
		if (fileAuth != null) {
			AppJungleUtil jungleUtil = AppJungleUtil.INSTANCE;
			boolean needLogin = jungleUtil.needLoginByEvent(fileAuth);
			int viewAuthor = fileAuth.getAuthType() == null ? 1 : fileAuth.getAuthType().intValue();
			if (needLogin) {
				boolean jungeUserSession = jungleUtil.jungeUserSession(request);
				if (jungeUserSession) {
					UserBean userBean = ILiveUtils.getAppUser(request);
					// 判断鉴权历史
					ILiveFileAuthenticationRecord authRecord = iLiveFileAuthenticationRecordMng
							.getILiveFileViewAuthBill(Long.parseLong(userBean.getUserId()), fileAuth.getFileId());
					if (authRecord == null) {
						if (viewAuthor == 4) {
							// 判断是否在白名单里面
							ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userBean.getUserId()));
							boolean checkwhiteListExist = iLiveFileWhiteBillMng
									.checkUserInWhiteList(iLiveManager.getMobile(), fileAuth.getFileId());
							if (checkwhiteListExist) {
								// 设置房间为公开观看
								// 加入鉴权表
								ILiveFileAuthenticationRecord fileAuthRecord = this.buildAuthRecord(4,
										userBean.getUserId(), fileAuth);
								authRecord = iLiveFileAuthenticationRecordMng.addILiveViewAuthBill(fileAuthRecord);
								return true;
							}
						}
						// 登录观看的类型鉴权
						if (viewAuthor == 5) {
							// 加入鉴权表
							ILiveFileAuthenticationRecord fileAuthRecord = this.buildAuthRecord(5, userBean.getUserId(),
									fileAuth);
							authRecord = iLiveFileAuthenticationRecordMng.addILiveViewAuthBill(fileAuthRecord);
							return true;
						}

						// 密码观看的鉴权
						if (viewAuthor == 2) {
							TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
									.getAttribute("tempLoginInfo");
							String loginResult = tempLoginInfo.getFileMap().get(fileAuth.getFileId());
							if (loginResult != null && !loginResult.equals("")
									&& loginResult.equals(fileAuth.getViewPassword())) {
								ILiveFileAuthenticationRecord fileAuthRecord = this.buildAuthRecord(2,
										userBean.getUserId(), fileAuth);
								authRecord = iLiveFileAuthenticationRecordMng.addILiveViewAuthBill(fileAuthRecord);
								return true;
							} else {
								return false;
							}
						}
                        //F码观看的鉴权
						if(viewAuthor == 6){
							ILiveViewAuthBill checkILiveViewAuthBill1 = iLiveViewAuthBillMng.checkILiveViewAuthBillByFileId(
									userBean.getUserId(), fileAuth.getFileId());
							if(checkILiveViewAuthBill1!=null){
								ILiveFileAuthenticationRecord fileAuthRecord = this.buildAuthRecord(6,
										userBean.getUserId(), fileAuth);
								authRecord = iLiveFileAuthenticationRecordMng.addILiveViewAuthBill(fileAuthRecord);
								return true;
							}else{
								return false;
							}
						}
						 //如果是第三方授權观看 詢問是否有權限
						if(viewAuthor==7){
							//根據userId 查詢第三方的userId
							System.out.println("用户id："+userBean.getUserId());
							ILiveManager manager=iLiveManagerMng.selectILiveManagerById(Long.parseLong(userBean.getUserId()));
							Map<String, String> map = new HashMap<String, String>();
							map.put("userId", manager.getZhxyuserId());
							map.put("fileId", fileAuth.getFileId().toString());
							String checkUrl=iLiveEnterpriseMng.getILiveEnterPriseByAppId(manager.getZhxyappId()).getCheckIfCanUrl();
							if(checkUrl!=null){
								String postJson;
								try {
									postJson = HttpUtils.sendPost(checkUrl, map, "UTF-8");
									if(postJson!=null){
										JSONObject jsonObject = new JSONObject(postJson);
										Integer code=jsonObject.getInt("code");
										System.out.println("map=============="+code);
										if(code==1){
											JSONObject data=jsonObject.getJSONObject("data");
											Integer access=data.getInt("access");
											if(access==1){
												ILiveFileAuthenticationRecord fileAuthRecord = this.buildAuthRecord(7,
														userBean.getUserId(), fileAuth);
												authRecord = iLiveFileAuthenticationRecordMng.addILiveViewAuthBill(fileAuthRecord);
												return true;
											}									}
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						// 登录观看的类型鉴权
						if (viewAuthor == 0 || viewAuthor == 1) {
							return true;
						}

					} else {
						return true;
					}
				} else {
					return false;
				}
			} else {
				boolean jungeUserSession = jungleUtil.jungeUserSession(request);
				if (jungeUserSession) {
					UserBean appUser = ILiveUtils.getAppUser(request);
					// 密码观看的鉴权
					if (viewAuthor == 2) {
						ILiveFileAuthenticationRecord authRecord = iLiveFileAuthenticationRecordMng
								.getILiveFileViewAuthBill(Long.parseLong(appUser.getUserId()), fileAuth.getFileId());
						if (authRecord == null) {
							TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
									.getAttribute("tempLoginInfo");
							String loginResult = tempLoginInfo.getFileMap().get(fileAuth.getFileId());
							if (loginResult != null && !loginResult.equals("")
									&& loginResult.equals(fileAuth.getViewPassword())) {
								ILiveFileAuthenticationRecord fileAuthRecord = this.buildAuthRecord(2,
										appUser.getUserId(), fileAuth);
								fileAuthRecord = iLiveFileAuthenticationRecordMng.addILiveViewAuthBill(fileAuthRecord);
								return true;
							} else {
								return false;
							}
						}
					}
				}
				return true;
			}

		}
		return false;
	}

	private ILiveFileAuthenticationRecord buildAuthRecord(int authType, String userId,
			ILiveFileAuthentication fileAuth) {
		ILiveFileAuthenticationRecord ar = new ILiveFileAuthenticationRecord();
		ar.setAuthPassTime(new Timestamp(System.currentTimeMillis()));
		ar.setAuthType(authType);
		ar.setDeleteStatus(0);
		ar.setFileId(fileAuth.getFileId());
		ar.setUserId(Long.parseLong(userId));
		return ar;
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
	public void getFileInfo(Long fileId, HttpServletRequest request, HttpServletResponse response,
			String terminalType) {
		System.out.println("fileId========"+fileId);
		JSONObject resultJson = new JSONObject();
		ILiveMediaFile mediaFilePo = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
		System.out.println("mediaFilePo======"+mediaFilePo);
		try {
			if (mediaFilePo == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "资源文件不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveMediaFile mediaFile = new ILiveMediaFile();
			Long addNumCircle = iLiveFileViewStaticsMng.addNumCircle(fileId);
			BeanUtilsExt.copyProperties(mediaFile, mediaFilePo);
			ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng.getFileAuthenticationByFileId(fileId);
			if (fileAuth == null) {
				fileAuth = new ILiveFileAuthentication();
				fileAuth.setAuthType(1);
				fileAuth.setFileId(fileId);
				fileAuth.setViewPassword("");
			}
			boolean jungeAuthFinish = this.jungeAuthFinish(fileAuth, request);
			if (jungeAuthFinish) {
				Integer serverMountId = mediaFile.getServerMountId();
				ILiveServerAccessMethod accessMethodByMountId = serverAccessMethodMng
						.getAccessMethodByMountId(serverMountId);
				String shareUrl = "";
				String rtmpAddr = "";
				if (accessMethodByMountId != null) {
					MountInfo mountInfo = accessMethodByMountId.getMountInfo();
					
					String allPath = "http://" + accessMethodByMountId.getHttp_address() + ":"
							+ accessMethodByMountId.getUmsport() + mountInfo.getBase_path() + mediaFile.getFilePath()+"/tzwj_video.m3u8";
					
					try{
						//判断一下是不是windows，windows不支持m3u8文件播放
						String agent = request.getHeader("User-Agent");
						if(agent.toLowerCase().indexOf("windows")>0){
							allPath = "http://" + accessMethodByMountId.getHttp_address() + ":"
									+ accessMethodByMountId.getUmsport() + mountInfo.getBase_path() + mediaFile.getFilePath();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					
					Map<String, String> generatorEncoderPwd = SafeTyChainPasswdUtil.INSTANCE.generatorEncoderPwd();
					// 因为客户端的地址用的是80端口，rtmp地址临时写死1935
					rtmpAddr = "rtmp://" + accessMethodByMountId.getCdnLiveHttpUrl() + ":1935"
							+ mountInfo.getBase_path() + mediaFile.getFilePath();
					rtmpAddr = String.format("%s?ut=%s&us=%s&sign=%s", rtmpAddr, generatorEncoderPwd.get("timestamp"),
							generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
					allPath = String.format("%s?ut=%s&us=%s&sign=%s", allPath, generatorEncoderPwd.get("timestamp"),
							generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
					mediaFile.setFilePath(allPath);
					shareUrl = accessMethodByMountId.getH5HttpUrl() + "/phone" + "/review.html?roomId="
							+ (mediaFile.getLiveRoomId() == null ? 0 : mediaFile.getLiveRoomId()) + "&fileId=" + fileId;
				}
				ILiveViewRecord viewRecord = new ILiveViewRecord();
				UserBean userBean = ILiveUtils.getAppUser(request);
				if (userBean != null) {
					String userId = userBean.getUserId();
					viewRecord.setManagerId(Long.parseLong(userId));
				} else {
					if (request.getParameter("userId") != null && !("").equals(request.getParameter("userId"))) {
						viewRecord.setManagerId(Long.parseLong(request.getParameter("userId")));
					} else {
						viewRecord.setManagerId(0L);
					}
				}
				viewRecord.setOuterId(mediaFile.getFileId());
				viewRecord.setViewType(2);
				if (viewRecord.getManagerId() > 0) {
					iLiveViewRecordMng.addILiveViewRecord(viewRecord);
				}
				mediaFile.setViewNum(addNumCircle);
				mediaFile.setShareUrl(shareUrl);
				mediaFile.setAllowComment(mediaFile.getAllowComment() == null ? 1 : mediaFile.getAllowComment());
				String sessionId = request.getSession().getId();
				String userId = viewRecord.getManagerId() + "_" + sessionId;
				String ipAddr = IPUtils.getRealIpAddr(request);
				ILiveUserViewStatics.INSTANCE.fileViewNotify(userId,
						mediaFile.getLiveRoomId() == null ? 0 : mediaFile.getLiveRoomId(),
						mediaFile.getLiveEventId() == null ? 0 : mediaFile.getLiveEventId(), fileId, ipAddr,
						terminalType);
				JSONObject jsonObject = mediaFile.toMediaFileJson();
				jsonObject.put("rtmpAddr", rtmpAddr);
				jsonObject.put("fileCover", ConfigUtils.get("defaultTysxLogoUrl"));
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "点播文件获取成功");
				resultJson.put("data", jsonObject);
				
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "你没有观看该直播间的权限");
				resultJson.put("data", new JSONObject());
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
	 * 欢迎语
	 */
	@RequestMapping(value = "filewelcomeMsg.jspx")
	public void getWelcomMsg(Long fileId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveFileAuthentication iLiveFileAuthentication = iLiveFileAuthenticationMng
					.getFileAuthenticationByFileId(fileId);
			if (iLiveFileAuthentication == null) {
				iLiveFileAuthentication = new ILiveFileAuthentication();
				iLiveFileAuthentication.setAuthType(1);
			}
			String welcomeMsg = iLiveFileAuthentication.getWelcomeMsg();
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			resultJson.put("data", welcomeMsg);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作异常");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	/**
	 * 获得文件真实信息
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param httpServletResponse
	 * @param httpServletRequest
	 */
	@RequestMapping(value = "appmediafile.jspx")
	public void getAppMediaFile(Long fileId, HttpServletRequest request, HttpServletResponse response,
			String terminalType) {
		JSONObject resultJson = new JSONObject();
		ILiveMediaFile mediaFilePo = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
		try {
			if (mediaFilePo == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "资源文件不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveMediaFile mediaFile = new ILiveMediaFile();
			Long addNumCircle = iLiveFileViewStaticsMng.addNumCircle(fileId);
			BeanUtilsExt.copyProperties(mediaFile, mediaFilePo);
			ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng.getFileAuthenticationByFileId(fileId);
			if (fileAuth == null) {
				fileAuth = new ILiveFileAuthentication();
				fileAuth.setAuthType(1);
				fileAuth.setFileId(fileId);
				fileAuth.setViewPassword("");
			}
			boolean jungeAuthFinish = this.jungeAuthFinish(fileAuth, request);
			if (jungeAuthFinish) {
				Integer serverMountId = mediaFile.getServerMountId();
				ILiveServerAccessMethod accessMethodByMountId = serverAccessMethodMng
						.getAccessMethodByMountId(serverMountId);
				String shareUrl = "";
				if (accessMethodByMountId != null) {
					//MountInfo mountInfo = accessMethodByMountId.getMountInfo();
					//String allPath = "http://" + accessMethodByMountId.getHttp_address() + ":"
					//		+ accessMethodByMountId.getUmsport() + mountInfo.getBase_path() + mediaFile.getFilePath();
					//Map<String, String> generatorEncoderPwd = SafeTyChainPasswdUtil.INSTANCE.generatorEncoderPwd();
					//allPath = String.format("%s?ut=%s&us=%s&sign=%s", allPath, generatorEncoderPwd.get("timestamp"),
					//		generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
					//mediaFile.setFilePath(allPath);
					shareUrl = accessMethodByMountId.getH5HttpUrl() + "/phone" + "/review.html?roomId="
							+ (mediaFile.getLiveRoomId() == null ? 0 : mediaFile.getLiveRoomId()) + "&fileId=" + fileId;
				}
				ILiveViewRecord viewRecord = new ILiveViewRecord();
				UserBean userBean = ILiveUtils.getAppUser(request);
				if (userBean != null) {
					String userId = userBean.getUserId();
					viewRecord.setManagerId(Long.parseLong(userId));
				} else {
					if (request.getParameter("userId") != null) {
						viewRecord.setManagerId(Long.parseLong(request.getParameter("userId")));
					} else {
						viewRecord.setManagerId(0L);
					}
				}
				viewRecord.setOuterId(mediaFile.getFileId());
				viewRecord.setViewType(2);
				if (viewRecord.getManagerId() > 0) {
					iLiveViewRecordMng.addILiveViewRecord(viewRecord);
				}
				if(mediaFile.getOpenDataBeautifySwitch()==1) {
					mediaFile.setViewNum(addNumCircle*mediaFile.getMultiple()+mediaFile.getBaseNum());
				}else {
					mediaFile.setViewNum(addNumCircle);
				}
				
				mediaFile.setShareUrl(shareUrl);
				mediaFile.setAllowComment(mediaFile.getAllowComment() == null ? 1 : mediaFile.getAllowComment());
				String sessionId = request.getSession().getId();
				String userId = viewRecord.getManagerId() + "_" + sessionId;
				String ipAddr = IPUtils.getRealIpAddr(request);
				ILiveUserViewStatics.INSTANCE.fileViewNotify(userId,
						mediaFile.getLiveRoomId() == null ? 0 : mediaFile.getLiveRoomId(),
						mediaFile.getLiveEventId() == null ? 0 : mediaFile.getLiveEventId(), fileId, ipAddr,
						terminalType);
				//JSONObject jsonObject = mediaFile.toMediaFileJson();
				AppMediaFile appMediaFile = iLiveMediaFileMng.convertILiveMediaFile2AppMediaFile(mediaFile);

			
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "点播文件获取成功");
				resultJson.put("data",  new JSONObject(appMediaFile));
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "你没有观看该直播间的权限");
				resultJson.put("data", new JSONObject());
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
	 * 发表评论post提交 	手机APP接口
	 * 
	 * @param userId
	 * @param comments
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "pushcomments.jspx", method = RequestMethod.POST)
	public void receiveCommentsPost(Long userId, Long fileId, String comments, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (userId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户没有登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
		if (iLiveManager != null && comments != null) {
			try {
				ILiveMediaFile selectILiveMediaFile = iLivemediaFileMng.selectILiveMediaFileByFileId(fileId);
				ILiveMediaFileComments commentsBean = new ILiveMediaFileComments();
				if (selectILiveMediaFile.getAutoCheckFlag()==null||selectILiveMediaFile.getAutoCheckFlag()==0) {
					commentsBean.setCheckState(0);
				}else {
					commentsBean.setCheckState(1);
				}
				commentsBean.setAppreciateCount(0L);
				commentsBean.setCreateTime(new Timestamp(System.currentTimeMillis()));
				commentsBean.setDelFlag(0);
				commentsBean.setiLiveMediaFile(selectILiveMediaFile);
				commentsBean.setUserId(userId);
				commentsBean.setObjectionCount(0L);
				comments = comments.replaceAll("[^\\u0000-\\uFFFF]", "\\[表情\\]");
				commentsBean.setComments(ExpressionUtil.replaceTitleToKey(comments));
				commentsBean.setCommentsUser(iLiveManager.getNailName());
				commentsBean.setUserImg(iLiveManager.getUserImg());
				if(selectILiveMediaFile.getUserId().equals(userId)) {
					commentsBean.setSendType(1);
				}else {
					commentsBean.setSendType(0);
				}
				commentsBean.setTopFlagNum(0);
				Long commentId = commentsMng.addFileComments(commentsBean);
				ILiveMediaFileComments comment = commentsMng.getFileCommentById(commentId);
				ILiveEnterpriseTerminalUser terminalUserFind = terminalUserMng.getTerminalUser(userId,
						selectILiveMediaFile.getEnterpriseId().intValue());
				if (terminalUserFind == null) {
					ILiveEnterpriseTerminalUser terminalUser = new ILiveEnterpriseTerminalUser();
					terminalUser.setEnterpriseId(selectILiveMediaFile.getEnterpriseId().intValue());
					terminalUser.setIsBlacklist(0);
					terminalUser.setIsDel(0);
					terminalUser.setUserId(iLiveManager.getUserId());
					terminalUser.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
					terminalUser.setNailName(iLiveManager.getNailName());
					terminalUser.setUserImg(iLiveManager.getUserImg());
					terminalUser.setFansType(0);
					terminalUser.setLoginType(0);
					terminalUser.setMobile(iLiveManager.getMobile());
					terminalUserMng.saveTerminaluser(terminalUser);
				} else {
					if(terminalUserFind.getIsBlacklist()==1) {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "你已经被禁言");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
						
					}
					terminalUserFind.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
					terminalUserFind.setNailName(iLiveManager.getNailName());
					terminalUserFind.setUserImg(iLiveManager.getUserImg());
					terminalUserFind.setFansType(0);
					terminalUserFind.setLoginType(0);
					terminalUserFind.setMobile(iLiveManager.getMobile());
					terminalUserMng.updateTerminalUser(terminalUserFind);
				}
				
				// 评论通知
				try {
					String ipAddr = RequestUtils.getIpAddr(request);
					ILiveUserViewStatics.INSTANCE.commentRoom(String.valueOf(userId), null, null, fileId, ipAddr,
							"android");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
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
	 * 发表评论 H5/PC接口
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
		if (userBean == null || userBean.getUserId() == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户没有登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		ILiveMediaFile selectILiveMediaFile = iLivemediaFileMng.selectILiveMediaFileByFileId(fileId);
		if(selectILiveMediaFile.getAllowComment()==0) {
			resultJson.put("code", -1);
			resultJson.put("message", "评论未开启");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
				
		}
		if (comments != null) {
			try {
				
				ILiveEnterpriseTerminalUser terminalUserFind = terminalUserMng.getTerminalUser(userId,
						selectILiveMediaFile.getEnterpriseId().intValue());
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
				if (terminalUserFind == null) {
					ILiveEnterpriseTerminalUser terminalUser = new ILiveEnterpriseTerminalUser();
					terminalUser.setEnterpriseId(selectILiveMediaFile.getEnterpriseId().intValue());
					terminalUser.setIsBlacklist(0);
					terminalUser.setIsDel(0);
					terminalUser.setUserId(iLiveManager.getUserId());
					terminalUser.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
					terminalUser.setNailName(iLiveManager.getNailName());
					terminalUser.setUserImg(iLiveManager.getUserImg());
					terminalUser.setFansType(0);
					terminalUser.setLoginType(1);
					terminalUser.setMobile(iLiveManager.getMobile());
					terminalUserMng.saveTerminaluser(terminalUser);
				} else {
					
					if(terminalUserFind.getIsBlacklist()==1) {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "你已经被禁言");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
						
					}
					
					terminalUserFind.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
					terminalUserFind.setNailName(iLiveManager.getNailName());
					terminalUserFind.setUserImg(iLiveManager.getUserImg());
					terminalUserFind.setFansType(0);
					terminalUserFind.setLoginType(1);
					terminalUserFind.setMobile(iLiveManager.getMobile());
					terminalUserMng.updateTerminalUser(terminalUserFind);
				}
				logger.info(comments);
				comments = URLDecoder.decode(comments, "UTF-8");
				comments = comments.replaceAll("[^\\u0000-\\uFFFF]", "\\[表情\\]");
				ILiveMediaFileComments commentsBean = new ILiveMediaFileComments();
				Integer autoCheckFlag = selectILiveMediaFile.getAutoCheckFlag();
				if (autoCheckFlag == null || autoCheckFlag==0) {
					commentsBean.setCheckState(0);
				}else {
					commentsBean.setCheckState(1);
				}
				commentsBean.setAppreciateCount(0L);
				commentsBean.setCreateTime(new Timestamp(System.currentTimeMillis()));
				commentsBean.setDelFlag(0);
				commentsBean.setiLiveMediaFile(selectILiveMediaFile);
				commentsBean.setUserId(Long.parseLong(userBean.getUserId()));
				commentsBean.setObjectionCount(0L);
				commentsBean.setTopFlagNum(0);
				commentsBean.setComments(ExpressionUtil.replaceTitleToKey(comments));
				commentsBean.setCommentsUser(userBean.getNickname());
				if(selectILiveMediaFile.getUserId().equals(userId)) {
					commentsBean.setSendType(1);
				}else {
					commentsBean.setSendType(0);
				}
				commentsBean.setUserImg(userBean.getUserThumbImg());
				Long commentId = commentsMng.addFileComments(commentsBean);
				ILiveMediaFileComments comment = commentsMng.getFileCommentById(commentId);
				
				// 评论通知
				try {
					String ipAddr = RequestUtils.getIpAddr(request);
					ILiveUserViewStatics.INSTANCE.commentRoom(String.valueOf(userId), null, null, fileId, ipAddr, "h5");
				} catch (Exception e) {
					e.printStackTrace();
				}
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
	 * 
	 * 获取点播文件标题和欢迎语
	 * @param userId
	 * @param fileId
	 * @param comments
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getVodMsg.jspx", method = RequestMethod.GET)
	public void AllowComment(Long fileId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if(fileId==null||fileId==0L){
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "文件id为空");
			ResponseUtils.renderJson(request, response, resultJson.toString());	
			return;
		}
		try {
			ILiveFileAuthentication fileAuthentication = iLiveFileAuthenticationMng
					.getFileAuthenticationByFileId(fileId);
			ILiveMediaFile selectILiveMediaFile = iLivemediaFileMng.selectILiveMediaFileByFileId(fileId);
//			resultJson.put("welcomeMsg", fileAuthentication.getWelcomeMsg());
//			resultJson.put("title", selectILiveMediaFile.getMediaFileName());
			ILiveLiveRoom iliveRoom = this.buildRawILiveRoom();
			Integer	roomId = 0;
			
			iliveRoom.setRoomId(roomId);
			ILiveEvent iliveEvent = iliveRoom.getLiveEvent();
			iliveEvent.setWelcomeMsg(fileAuthentication.getWelcomeMsg());
			iliveEvent.setLiveTitle(selectILiveMediaFile.getMediaFileName());
			JSONObject jsonObject = iliveRoom.toViewBean(iliveRoom, iliveEvent, null);
			Gson gson = new Gson();
			resultJson.put("iLiveLiveRoom", gson.toJson(iliveRoom));
			resultJson.put("code", SUCCESS_STATUS);
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());		
	}
	/**
	 * 是否开启评论
	 * 
	 * @param userId
	 * @param fileId
	 * @param comments
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "AllowComment.jspx", method = RequestMethod.GET)
	public void getVodMsg(Long fileId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		
		ILiveMediaFile selectILiveMediaFile = iLivemediaFileMng.selectILiveMediaFileByFileId(fileId);
		if(selectILiveMediaFile.getAllowComment()==1) {
			resultJson.put("code", SUCCESS_STATUS);
				
		}else {
			resultJson.put("code", ERROR_STATUS);
			
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());		
	}

	/**
	 * 推送评论列表向用户
	 * 
	 * @param fileId
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getcomments.jspx")
	public void pushCommentsList2User(Long fileId, Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveMediaFile selectILiveMediaFileByFileId = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
			if (selectILiveMediaFileByFileId != null && selectILiveMediaFileByFileId.getDelFlag() != null
					&& selectILiveMediaFileByFileId.getDelFlag().intValue() != 1) {
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
				resultJson.put("totalCount", fileComments.getTotalCount());
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "访问的回看已不存在");
				resultJson.put("data", new JSONObject());
				resultJson.put("totalCount", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取评论失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 校验文件密码
	 * 
	 * @param fileId
	 * @param passWord
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/checkFilePassword.jspx")
	public void checkFilePassword(Long fileId, String passWord, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		System.out.println("checkFilePassword:" + passWord);
		if (passWord == null || "".equals(passWord)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "密码没有输入");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
			if (mediaFile == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "资源文件不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng.getFileAuthenticationByFileId(fileId);
			if (fileAuth != null) {
				String viewPassword = fileAuth.getViewPassword();
				if (viewPassword.equals(passWord)) {
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "密码输入成功");
					TempLoginInfo loginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
					if (loginInfo != null) {
						loginInfo.getFileMap().put(fileId, passWord);
					}
					request.getSession().setAttribute("tempLoginInfo", loginInfo);
					resultJson.put("data", new JSONObject());
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "密码输入错误");
					resultJson.put("data", new JSONObject());
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "密码输入错误");
				resultJson.put("data", new JSONObject());
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作异常");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	
	/**
	 * 获得VR文件播放器XML
	 * 
	 * @param fileId
	 * @param httpServletResponse
	 * @param httpServletRequest
	 */
	@RequestMapping(value = "vrPlayerXml.jspx")
	public String getVrPlayerXml(Long fileId, ModelMap map, HttpServletRequest request, HttpServletResponse response) {
		ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaForWeb(fileId);
		if (iLiveMediaFile != null) {
			Integer serverMountId = iLiveMediaFile.getServerMountId();
			ILiveServerAccessMethod serverAccess = serverAccessMethodMng.getAccessMethodByMountId(serverMountId);
			MountInfo mountInfo = serverAccess.getMountInfo();
			String allPath = HTTP_PROTOCAL + serverAccess.getHttp_address() + ":" + serverAccess.getUmsport()
					+ mountInfo.getBase_path() + iLiveMediaFile.getFilePath();
			iLiveMediaFile.setFilePath(allPath);
		}
		map.addAttribute("iLiveMediaFile", iLiveMediaFile);
		return "mediaFile/vrPlayerXml";
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
		ILiveUtils.setAppUser(request, userBean);
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
		userBean.setUsername(manager.getMobile());
		userBean.setUserThumbImg(manager.getUserImg());
		userBean.setEnterpriseId(manager.getEnterPrise().getEnterpriseId());
		userBean.setLevel(manager.getLevel()==null?0:manager.getLevel());
		userBean.setRoomId(manager.getRoomId());
		return userBean;
	}

}
