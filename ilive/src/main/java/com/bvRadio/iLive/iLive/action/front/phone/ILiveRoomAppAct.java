package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.NOFIND_STATUS;
import static com.bvRadio.iLive.iLive.Constants.NOT_BIND_MOBILE;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;
import static org.hamcrest.CoreMatchers.nullValue;
//import static org.hamcrest.CoreMatchers.nullValue;
import static com.bvRadio.iLive.iLive.Constants.FCODE_BIND;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.RequestUtils;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.action.front.vo.AppILiveRoom;
import com.bvRadio.iLive.iLive.action.front.vo.AppJungleUtil;
import com.bvRadio.iLive.iLive.action.front.vo.AppLoginValid;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.TempLoginInfo;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.cache.MessageCache;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseTerminalUser;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;
import com.bvRadio.iLive.iLive.entity.ILiveViewRecord;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.BBSDiyformDataMng;
import com.bvRadio.iLive.iLive.manager.ILiveAppointmentMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseTerminalUserMng;
import com.bvRadio.iLive.iLive.manager.ILiveEstoppelMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveFCodeMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileRelatedMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveVideoHistoryMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewAuthBillMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewRecordMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;
import com.bvRadio.iLive.iLive.util.BeanUtilsSpring;
import com.bvRadio.iLive.iLive.util.Dom4jUtil;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.IPUtil;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.SafeTyChainPasswdUtil;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.util.TerminalUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.google.gson.Gson;
import com.jwzt.comm.StringUtils;

/**
 * 构建观看端的接口
 * @author administrator
 *
 */
@Controller
@RequestMapping(value = "/app/room")
public class ILiveRoomAppAct {

	Logger logger = LoggerFactory.getLogger(ILiveRoomAppAct.class);

	private static final String HTTP_PROTOACAL = "http://";

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void buildMessage() {
		System.out.println("开始构建message.xmlToMessageCache");
		if (MessageCache.messageCache.isEmpty()) {
			try {
				ClassPathResource classPathResource = new ClassPathResource("message.xml");
				Document document = Dom4jUtil.getDocument(classPathResource.getFile().getAbsolutePath());
				Element rootElement = document.getRootElement();
				Element element = rootElement.element("message");
				List<Element> elements = element.elements();
				for (Element ele : elements) {
					String name = ele.getName();
					String textTrim = ele.getTextTrim();
					MessageCache.messageCache.put(name, textTrim);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

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

	@Autowired
	private ILiveViewRecordMng iLiveViewRecordMng;

	@Autowired
	private ILiveAppointmentMng iLiveAppointmentMng;

	@Autowired
	private ILiveEstoppelMng iLiveEstoppelMng;

	@Autowired
	private ILiveEnterpriseTerminalUserMng iLiveEnterpriseTerminalUserMng;

	@Autowired
	private ILiveEventMng iLiveEventMng;

	@Autowired
	private ILiveVideoHistoryMng iLiveVideoHistoryMng;
	
	@Autowired
	private ILiveMediaFileRelatedMng mediaFileRelatedMng;
	
	@Autowired
	private ILiveFCodeMng fCodeMng;
	@Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
	
	@RequestMapping(value = "checkRoomLogin.jspx")
	public void checkRoomLogin(Integer roomId, Long userId, String loginToken, String token, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom iliveRoom=null;
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			iliveRoom= SerializeUtil.getObjectRoom(roomId);
			if(iliveRoom==null) {
				iliveRoom=iLiveRoomMng.getIliveRoom(roomId);
				JedisUtils.setByte(("roomInfo:"+roomId).getBytes(), SerializeUtil.serialize(iliveRoom), 300);
			}
		}else {
			iliveRoom=iLiveRoomMng.getIliveRoom(roomId);
			JedisUtils.setByte(("roomInfo:"+roomId).getBytes(), SerializeUtil.serialize(iliveRoom), 300);
		}
		if(iliveRoom==null) {
			iliveRoom=iLiveRoomMng.getIliveRoom(roomId);
			JedisUtils.setByte(("roomInfo:"+roomId).getBytes(), SerializeUtil.serialize(iliveRoom), 300);
			
		}
		if (iliveRoom == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播间未获取到");
			AppLoginValid validRegex = new AppLoginValid();
			validRegex.setRoomNeedLogin(1);
			Gson gson = new Gson();
			String json = gson.toJson(validRegex);
			resultJson.put("data", json);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		if (iliveRoom.getOpenStatus() == 0 || iliveRoom.getDeleteStatus().intValue() == 1) {
			resultJson.put("code", NOFIND_STATUS);
			resultJson.put("message", "直播间已经关闭");
			String roomTitle=iliveRoom.getLiveEvent().getLiveTitle();
			AppLoginValid validRegex = new AppLoginValid();
			validRegex.setRoomNeedLogin(1);
			validRegex.setRoomTitle(roomTitle);
			Gson gson = new Gson();
			String json = gson.toJson(validRegex);
			resultJson.put("data", json);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}

		AppJungleUtil jungleUtil = AppJungleUtil.INSTANCE;
		if (request.getSession().getAttribute("tempLoginInfo") == null) {
			request.getSession().setAttribute("tempLoginInfo", new TempLoginInfo());
		}
		TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
		try {
			boolean needLogin = jungleUtil.needLogin(iliveRoom);
			ILiveServerAccessMethod accessMethodBySever = accessMethodMng
					.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			String guideAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/guide.html?roomId=" + roomId;
			String appGuideAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/appguide.html?roomId=" + roomId;
			if (needLogin) {
				boolean jungeUserSession = jungleUtil.jungeUserSession(request);
				if (!jungeUserSession) {
					if (loginToken == null || userId == null) {
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "连接成功");
						AppLoginValid validRegex = new AppLoginValid();
						validRegex.setRoomNeedLogin(1);
						validRegex.setGuideAddr(guideAddr);
						validRegex.setAppGuideAddr(appGuideAddr);
						Integer openGuideSwitch = iliveRoom.getLiveEvent().getOpenGuideSwitch();
						validRegex.setOpenGuideSwitch(openGuideSwitch == null ? 0 : openGuideSwitch);
						boolean repeatLogin = tempLoginInfo.getGetGuideAddr() == null ? false
								: tempLoginInfo.getGetGuideAddr();
						// 不需要引导
						if (repeatLogin) {
							// 等于0时 需要跳到引导页
							validRegex.setRepeateGuide(1);
							tempLoginInfo.setGetGuideAddr(false);
							request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
						} else {
							// 需要引导
							validRegex.setRepeateGuide(0);
						}
						String roomTitle=iliveRoom.getLiveEvent().getLiveTitle();
						validRegex.setRoomTitle(roomTitle);
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
							validRegex.setGuideAddr(guideAddr);
							validRegex.setAppGuideAddr(appGuideAddr);
							Integer openGuideSwitch = iliveRoom.getLiveEvent().getOpenGuideSwitch();
							validRegex.setOpenGuideSwitch(openGuideSwitch == null ? 0 : openGuideSwitch);
							boolean repeatLogin = tempLoginInfo.getGetGuideAddr() == null ? false
									: tempLoginInfo.getGetGuideAddr();
							if (repeatLogin) {
								validRegex.setRepeateGuide(1);
								tempLoginInfo.setGetGuideAddr(false);
								request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
							} else {
								validRegex.setRepeateGuide(0);
							}
							String roomTitle=iliveRoom.getLiveEvent().getLiveTitle();
							validRegex.setRoomTitle(roomTitle);
							Gson gson = new Gson();
							String json = gson.toJson(validRegex);
							resultJson.put("data", json);
						} else {
							resultJson.put("code", SUCCESS_STATUS);
							resultJson.put("message", "连接成功");
							AppLoginValid validRegex = new AppLoginValid();
							validRegex.setRoomNeedLogin(1);
							Integer openGuideSwitch = iliveRoom.getLiveEvent().getOpenGuideSwitch();
							validRegex.setOpenGuideSwitch(openGuideSwitch == null ? 0 : openGuideSwitch);
							validRegex.setGuideAddr(guideAddr);
							validRegex.setAppGuideAddr(appGuideAddr);
							boolean repeatLogin = tempLoginInfo.getGetGuideAddr() == null ? false
									: tempLoginInfo.getGetGuideAddr();
							if (repeatLogin) {
								validRegex.setRepeateGuide(1);
								tempLoginInfo.setGetGuideAddr(false);
								request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
							} else {
								validRegex.setRepeateGuide(0);
							}
							String roomTitle=iliveRoom.getLiveEvent().getLiveTitle();
							validRegex.setRoomTitle(roomTitle);
							Gson gson = new Gson();
							String json = gson.toJson(validRegex);
							resultJson.put("data", json);
						}
					}
				} else {
					
					//已经登陆用户先判断一下房间鉴权是不是白名单/付费观看/公开且需要登录/密码且需要登录，如果是白名单/付费观看/公开且需要登录/密码且需要登录需要判断用户是否有绑定了手机，没有绑定那个手机的情况需要让用户绑定手机
					UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
					Integer viewAuthorized = iliveRoom.getLiveEvent().getViewAuthorized();
					if (ILiveEvent.VIEW_AUTHORIZED_WHITE_LIST.equals(viewAuthorized)
							|| ILiveEvent.VIEW_AUTHORIZED_PAY.equals(viewAuthorized)
							||(ILiveEvent.VIEW_AUTHORIZED_FREE.equals(viewAuthorized)&&needLogin==true)
							||(ILiveEvent.VIEW_AUTHORIZED_PASSWORD.equals(viewAuthorized)&&needLogin==true)
							||ILiveEvent.VIEW_AUTHORIZED_FCODE.equals(viewAuthorized)
							||ILiveEvent.VIEW_AUTHORIZED_THIRD.equals(viewAuthorized)) {
						ILiveManager liveManager = iLiveManagerMng
								.getILiveManager(Long.parseLong(userBean.getUserId()));
						if (liveManager != null
								&& (liveManager.getMobile() == null || "".equals(liveManager.getMobile()))) {
							resultJson.put("code", NOT_BIND_MOBILE);
						} else {
							resultJson.put("code", SUCCESS_STATUS);
						}
					} else {
						resultJson.put("code", SUCCESS_STATUS);
					}
					
					//resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "连接成功");
					
					String userIdSession = userBean.getUserId();
					ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userIdSession));
					if (iLiveManager != null) {
						iLiveManager.setEnterPrise(null);
						iLiveManager.setUserPwd(null);
						iLiveManager.setSalt(null);
					}
					AppLoginValid validRegex = new AppLoginValid();
					validRegex.setGuideAddr(guideAddr);
					validRegex.setAppGuideAddr(appGuideAddr);
					validRegex.setRoomNeedLogin(2);
					Integer openGuideSwitch = iliveRoom.getLiveEvent().getOpenGuideSwitch();
					validRegex.setOpenGuideSwitch(openGuideSwitch == null ? 0 : openGuideSwitch);
					validRegex.setUserInfo(iLiveManager);
					boolean repeatLogin = tempLoginInfo.getGetGuideAddr() == null ? false
							: tempLoginInfo.getGetGuideAddr();
					if (repeatLogin) {
						validRegex.setRepeateGuide(1);
						tempLoginInfo.setGetGuideAddr(false);
						request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
					} else {
						validRegex.setRepeateGuide(0);
					}
					String roomTitle=iliveRoom.getLiveEvent().getLiveTitle();
					validRegex.setRoomTitle(roomTitle);
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
						validRegex.setGuideAddr(guideAddr);
						validRegex.setAppGuideAddr(appGuideAddr);
						validRegex.setUserInfo(iLiveManager);
						Integer openGuideSwitch = iliveRoom.getLiveEvent().getOpenGuideSwitch();
						validRegex.setOpenGuideSwitch(openGuideSwitch == null ? 0 : openGuideSwitch);
						boolean repeatLogin = tempLoginInfo.getGetGuideAddr() == null ? false
								: tempLoginInfo.getGetGuideAddr();
						if (repeatLogin) {
							validRegex.setRepeateGuide(1);
							tempLoginInfo.setGetGuideAddr(false);
							request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
						} else {
							validRegex.setRepeateGuide(0);
						}
						String roomTitle=iliveRoom.getLiveEvent().getLiveTitle();
						validRegex.setRoomTitle(roomTitle);
						Gson gson = new Gson();
						String json = gson.toJson(validRegex);
						resultJson.put("data", json);
					} else {
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "连接成功");
						AppLoginValid validRegex = new AppLoginValid();
						validRegex.setRoomNeedLogin(2);
						Integer openGuideSwitch = iliveRoom.getLiveEvent().getOpenGuideSwitch();
						validRegex.setOpenGuideSwitch(openGuideSwitch == null ? 0 : openGuideSwitch);
						validRegex.setGuideAddr(guideAddr);
						validRegex.setAppGuideAddr(appGuideAddr);
						boolean repeatLogin = tempLoginInfo.getGetGuideAddr() == null ? false
								: tempLoginInfo.getGetGuideAddr();
						if (repeatLogin) {
							validRegex.setRepeateGuide(1);
							tempLoginInfo.setGetGuideAddr(false);
							request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
						} else {
							validRegex.setRepeateGuide(0);
						}
						String roomTitle=iliveRoom.getLiveEvent().getLiveTitle();
						validRegex.setRoomTitle(roomTitle);
						Gson gson = new Gson();
						String json = gson.toJson(validRegex);
						resultJson.put("data", json);
					}
				} else {
					// 先判断是否是已经登录过的
					boolean jungeUserSession = jungleUtil.jungeUserSession(request);
					if (jungeUserSession) {
						UserBean userBean = ILiveUtils.getAppUser(request);
						ILiveManager manager = iLiveManagerMng.getILiveManager(Long.parseLong(userBean.getUserId()));
						manager.setUserPwd(null);
						manager.setEnterPrise(null);
						manager.setSalt(null);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "连接成功");
						AppLoginValid validRegex = new AppLoginValid();
						validRegex.setRoomNeedLogin(2);
						Integer openGuideSwitch = iliveRoom.getLiveEvent().getOpenGuideSwitch();
						validRegex.setOpenGuideSwitch(openGuideSwitch == null ? 0 : openGuideSwitch);
						validRegex.setGuideAddr(guideAddr);
						validRegex.setAppGuideAddr(appGuideAddr);
						validRegex.setUserInfo(manager);
						boolean repeatLogin = tempLoginInfo.getGetGuideAddr() == null ? false
								: tempLoginInfo.getGetGuideAddr();
						if (repeatLogin) {
							validRegex.setRepeateGuide(1);
							tempLoginInfo.setGetGuideAddr(false);
							request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
						} else {
							validRegex.setRepeateGuide(0);
						}
						String roomTitle=iliveRoom.getLiveEvent().getLiveTitle();
						validRegex.setRoomTitle(roomTitle);
						Gson gson = new Gson();
						String json = gson.toJson(validRegex);
						resultJson.put("data", json);
					} else {
						ILiveManager manager = iLiveManagerMng.checkLogin(loginToken, userId);
						if (manager == null) {
							if (userId != null) {
								ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
								this.autoLogin(request, iLiveManager);
								resultJson.put("code", SUCCESS_STATUS);
								resultJson.put("message", "连接成功");
								AppLoginValid validRegex = new AppLoginValid();
								validRegex.setRoomNeedLogin(2);
								validRegex.setGuideAddr(guideAddr);
								validRegex.setAppGuideAddr(appGuideAddr);
								Integer openGuideSwitch = iliveRoom.getLiveEvent().getOpenGuideSwitch();
								validRegex.setOpenGuideSwitch(openGuideSwitch == null ? 0 : openGuideSwitch);
								validRegex.setUserInfo(manager);
								boolean repeatLogin = tempLoginInfo.getGetGuideAddr() == null ? false
										: tempLoginInfo.getGetGuideAddr();
								if (repeatLogin) {
									validRegex.setRepeateGuide(1);
									tempLoginInfo.setGetGuideAddr(false);
									request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
								} else {
									validRegex.setRepeateGuide(0);
								}
								String roomTitle=iliveRoom.getLiveEvent().getLiveTitle();
								validRegex.setRoomTitle(roomTitle);
								Gson gson = new Gson();
								String json = gson.toJson(validRegex);
								resultJson.put("data", json);
							}
						} else {
							this.autoLogin(request, manager);
							manager.setUserPwd(null);
							manager.setEnterPrise(null);
							manager.setSalt(null);
							// 帮助重新自动登录一次
							resultJson.put("code", SUCCESS_STATUS);
							resultJson.put("message", "连接成功");
							AppLoginValid validRegex = new AppLoginValid();
							validRegex.setRoomNeedLogin(2);
							validRegex.setGuideAddr(guideAddr);
							validRegex.setAppGuideAddr(appGuideAddr);
							Integer openGuideSwitch = iliveRoom.getLiveEvent().getOpenGuideSwitch();
							validRegex.setOpenGuideSwitch(openGuideSwitch == null ? 0 : openGuideSwitch);
							validRegex.setUserInfo(manager);
							boolean repeatLogin = tempLoginInfo.getGetGuideAddr() == null ? false
									: tempLoginInfo.getGetGuideAddr();
							if (repeatLogin) {
								validRegex.setRepeateGuide(1);
								tempLoginInfo.setGetGuideAddr(false);
								request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
							} else {
								validRegex.setRepeateGuide(0);
							}
							String roomTitle=iliveRoom.getLiveEvent().getLiveTitle();
							validRegex.setRoomTitle(roomTitle);
							Gson gson = new Gson();
							String json = gson.toJson(validRegex);
							resultJson.put("data", json);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "内部异常");
			resultJson.put("data", new JSONObject());
			logger.error(e.toString());
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
		userBean.setUsername(manager.getMobile());
		userBean.setNickname(manager.getNailName());
		userBean.setUserThumbImg(manager.getUserImg());
		userBean.setCertStatus(manager.getCertStatus());
		userBean.setEnterpriseId(manager.getEnterPrise().getEnterpriseId());
		return userBean;
	}

	/**
	 * 获取房间信息
	 * 
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "getRoomInfo.jspx")
	public void getRoomInfo(Integer roomId, String loginToken, Long userId,String terminalType, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (roomId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播房间ID不能为空");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
//		UserBean user;
//		if("ios".equals(terminalType)||"android".equals(terminalType)){
//			user = (UserBean) request.getSession().getAttribute("appUser");
//		}else{
//			user = (UserBean) request.getSession().getAttribute("_user_key");
//		}
//		
//		String userid=user.getUserId();
		ILiveLiveRoom iliveRoom=null;
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			iliveRoom= SerializeUtil.getObjectRoom(roomId);
			if(iliveRoom==null) {
				iliveRoom=iLiveRoomMng.getIliveRoom(roomId);
				JedisUtils.setByte(("roomInfo:"+roomId).getBytes(), SerializeUtil.serialize(iliveRoom), 300);
			}
		}else {
			iliveRoom=iLiveRoomMng.getIliveRoom(roomId);
			JedisUtils.setByte(("roomInfo:"+roomId).getBytes(), SerializeUtil.serialize(iliveRoom), 300);
		}
		ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap2=null;
		 try {
			 userListMap2= ApplicationCache.getUserListMap();
		} catch (Exception e) {
			userListMap2=new ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>>();
		}
		 Integer enterpriseId1=null;
		if(iliveRoom==null) {
			iliveRoom=iLiveRoomMng.getIliveRoom(roomId);
			JedisUtils.setByte(("roomInfo:"+roomId).getBytes(), SerializeUtil.serialize(iliveRoom), 300);
			enterpriseId1 = iliveRoom.getEnterpriseId();
		}else {
			enterpriseId1 = iliveRoom.getEnterpriseId();
		}
		
//		boolean isblack=iLiveEnterpriseFansMng.isblack(Long.parseLong(userid),enterpriseId1);
//		if(isblack){
//			resultJson.put("code", ERROR_STATUS);
//			resultJson.put("message", "企业黑名单用户，无法进行观看");
//			ResponseUtils.renderJson(request, response, resultJson.toString());
//			return;
//		}
		List<ILiveLiveRoom> rooms = iLiveRoomMng.findRoomListPassByEnterprise(enterpriseId1);
		long number = 0;
		for (ILiveLiveRoom iLiveLiveRoom : rooms) {
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap2.get(iLiveLiveRoom.getRoomId());
			if(concurrentHashMap==null){
				concurrentHashMap = new ConcurrentHashMap<String, UserBean>();
			}
			number = number +concurrentHashMap.size();
		}
		 ILiveEnterprise iLiveEnterPrise1=null;
		if(JedisUtils.exists(("enterpriseInfo:"+enterpriseId1).getBytes())) {
			iLiveEnterPrise1= SerializeUtil.getObjectEnterprise(enterpriseId1);
		}else {
			iLiveEnterPrise1=iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId1);
			JedisUtils.setByte(("enterpriseInfo:"+enterpriseId1).getBytes(), SerializeUtil.serialize(iLiveEnterPrise1), 300);
		}
		boolean b = EnterpriseUtil.selectIfContent(EnterpriseCache.ENTERPRISE_Concurrent, number, enterpriseId1, iLiveEnterPrise1.getCertStatus());
		if(b){
			resultJson.put("code", Constants.ERROR_full);
			resultJson.put("message", "直播间观看人数已满，无法进行观看");
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			if (iliveRoom == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播房间不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else {
				ILiveEvent oldliveEvent = iliveRoom.getLiveEvent();
				ILiveEvent liveEvent = new ILiveEvent();
				BeanUtilsSpring.copyProperties(liveEvent, oldliveEvent);
				AppJungleUtil jungleUtil = AppJungleUtil.INSTANCE;
				boolean needLogin = jungleUtil.needLogin(iliveRoom);
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = accessMethodMng
							.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.toString());
				}
				if (needLogin) {
					boolean jungeUserSession = jungleUtil.jungeUserSession(request);
					// 如果登录成功
					if (jungeUserSession) {
						UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
						String userid=userBean.getUserId();
						boolean isblack=iLiveEnterpriseFansMng.isblack(Long.parseLong(userid),enterpriseId1);
						if(isblack){
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "企业黑名单用户，无法进行观看");
							ResponseUtils.renderJson(request, response, resultJson.toString());
							return;
						}else{
						ILiveViewAuthBill checkILiveViewAuthBill = iLiveViewAuthBillMng.checkILiveViewAuthBill(
								userBean.getUserId(), iliveRoom.getLiveEvent().getLiveEventId());
						// 引导客户端是否需要报名或者输入房间密码
						if (checkILiveViewAuthBill == null) {
							// 第一步 判断房间是否开启报名
							Integer openSignupSwitch = liveEvent.getOpenSignupSwitch();
							openSignupSwitch = openSignupSwitch == null ? 0 : openSignupSwitch;
							if (openSignupSwitch == 1) {
								// 判断是否报名通过
								boolean signUp = false;
								//先校验session后校验数据库
								java.util.HashSet<String> submitForm = (java.util.HashSet)request.getSession().getAttribute("submitForm");
								if(submitForm != null){
									signUp = submitForm.contains(String.valueOf(liveEvent.getFormId()));
								}
								if(!signUp){
									signUp = bbsDiyfromDataMng.checkUserSignUp(userBean.getUserId(),
											liveEvent.getFormId());
								}
								if (signUp) {
									// 报名过了 设置房间为不需要报名
									liveEvent.setOpenSignupSwitch(0);
								} else {
									liveEvent.setOpenSignupSwitch(1);
									// 设置报名地址
									try {
										String h5HttpUrl = accessMethodBySeverGroupId.getH5HttpUrl();
										liveEvent.setDiyfromAddr(
												h5HttpUrl + "/phone/form.html?roomId=" + iliveRoom.getRoomId());
										liveEvent.setAppDiyformAddr(
												h5HttpUrl + "/phone/appform.html?roomId=" + iliveRoom.getRoomId());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
                             //如果是F码观看 查看健全表中是否有记录
							if(liveEvent.getViewAuthorized()==6){
								ILiveViewAuthBill checkILiveViewAuthBill1 = iLiveViewAuthBillMng.checkILiveViewAuthBill(
										userBean.getUserId(), iliveRoom.getRoomId());
								if(checkILiveViewAuthBill1!=null){
									liveEvent.setViewAuthorized(1);
								}
							}
							 //如果是第三方授權观看 詢問是否有權限
							if(liveEvent.getViewAuthorized()==7){
								//根據userId 查詢第三方的userId
								System.out.println("用户id："+userBean.getUserId());
								
								ILiveManager manager=null;
								if(JedisUtils.exists(("managerInfo:"+userBean.getUserId()).getBytes())) {
									manager= SerializeUtil.getObjectManager(userBean.getUserId());
								}else {
									manager=iLiveManagerMng.selectILiveManagerById(Long.parseLong(userBean.getUserId()));
									JedisUtils.setByte(("managerInfo:"+userBean.getUserId()).getBytes(), SerializeUtil.serialize(manager), 300);
								}
								Map<String, String> map = new HashMap<String, String>();
								map.put("userId", manager.getZhxyuserId());
								map.put("roomId", iliveRoom.getRoomId().toString());
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
												liveEvent.setViewAuthorized(1);
											}else{
												String redirectUrl=data.getString("redirectUrl");
												liveEvent.setOutLinkUrl(redirectUrl);
											}
										}
									}
								}
							}
							if (liveEvent.getViewAuthorized() == 2||liveEvent.getViewAuthorized() == 1) {
								if (request.getSession().getAttribute("tempLoginInfo") != null) {
									TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
											.getAttribute("tempLoginInfo");
									if (tempLoginInfo != null) {
										if (tempLoginInfo.getMap()
												.get(iliveRoom.getLiveEvent().getLiveEventId()) != null
												&& tempLoginInfo.getMap().get(iliveRoom.getLiveEvent().getLiveEventId())
														.equals(oldliveEvent.getViewPassword())) {
											liveEvent.setViewAuthorized(1);
										}
									}

								}
							}

							// 对于登录观看的鉴权如果登录成功的直接设置为公开观看
							if (liveEvent.getViewAuthorized() == 0) {
								liveEvent.setViewAuthorized(1);
							}
							// 对于登录观看的鉴权如果登录成功的直接设置为公开观看
							if (liveEvent.getViewAuthorized() == 5) {
								liveEvent.setViewAuthorized(1);
							}
							
						} else {
							// 对于历史已经鉴定的了 观看方式直接设置为公开观看
							liveEvent.setViewAuthorized(1);
							liveEvent.setOpenSignupSwitch(0);
						}
						List<PageDecorate> list = pageDecorateMng.getList(liveEvent.getLiveEventId());
						List<PageDecorate> newPageDecorate = new ArrayList<>();
						boolean pageType2 = false;
						boolean pageType3 = false;
						for (PageDecorate page : list) {
							int menuType = page.getMenuType().intValue();
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
								if (page.getMenuType().intValue() == 1) {
									// http://zb.tv189.com/phone/theme.html?roomId=352&userId=125
									String guideAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone"
											+ "/theme.html?roomId=" + roomId + "&userId="
											+ (userId == null ? 0 : userId);
									String richContent = guideAddr;
									page.setRichContent(richContent);
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
								if (page.getMenuType().intValue() == 1) {
									// http://zb.tv189.com/phone/theme.html?roomId=352&userId=125
									String guideAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone"
											+ "/theme.html?roomId=" + roomId + "&userId="
											+ (userId == null ? 0 : userId);
									String richContent = guideAddr;
									page.setRichContent(richContent);
								}
								newPageDecorate.add(page);
							}
						} else {
							for (PageDecorate page : list) {
								if (page.getMenuType().intValue() == 1) {
									// http://zb.tv189.com/phone/theme.html?roomId=352&userId=125
									String guideAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone"
											+ "/theme.html?roomId=" + roomId + "&userId="
											+ (userId == null ? 0 : userId);
									String richContent = guideAddr;
									page.setRichContent(richContent);
								}
								newPageDecorate.add(page);
							}
						}
						liveEvent.setPageRecordList(newPageDecorate);
						Integer enterpriseId = iliveRoom.getEnterpriseId();
						 ILiveEnterprise iLiveEnterPrise=null;
						if(JedisUtils.exists(("enterpriseInfo:"+enterpriseId).getBytes())) {
							iLiveEnterPrise= SerializeUtil.getObjectEnterprise(enterpriseId);
						}else {
							iLiveEnterPrise=iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
							JedisUtils.setByte(("enterpriseInfo:"+enterpriseId).getBytes(), SerializeUtil.serialize(iLiveEnterPrise), 300);
						}
						boolean exist = iLiveEnterpriseFansMng.isExist(enterpriseId,
								Long.parseLong(userBean.getUserId()));
						Integer concernStatus = 0;
						if (exist) {
							concernStatus = 1;
						}
						iLiveEnterPrise.setConcernStatus(concernStatus);
						int fansNum = 0;
						if(JedisUtils.exists("enterPriseFansNum:"+enterpriseId)) {
							fansNum = Integer.parseInt(JedisUtils.get("enterPriseFansNum:"+enterpriseId));
						}else {
							fansNum = iLiveEnterpriseFansMng.getFansNum(enterpriseId);
							JedisUtils.set("enterPriseFansNum:"+enterpriseId, fansNum+"", 18000);
						}
						iLiveEnterPrise.setConcernTotal(fansNum);
						JSONObject jsonObject = iliveRoom.toViewBean(iliveRoom, liveEvent, iLiveEnterPrise);
						// 添加输入密码页面地址
						String appPasswordAddr;
						try {
							ILiveServerAccessMethod accessMethodBySever = accessMethodMng
									.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
							appPasswordAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/apppassword.html?roomId="
									+ roomId;
						} catch (Exception e) {
							appPasswordAddr = "";
						}
						//添加输入观看码页面地址
						String appFcodeAddr;
						try {
							ILiveServerAccessMethod accessMethodBySever = accessMethodMng
									.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
							appFcodeAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/appFCode.html?roomId="
									+ roomId;
						} catch (Exception e) {
							appFcodeAddr = "";
						}
						//已经登陆用户先判断一下房间鉴权是不是白名单/付费观看，如果是白名单/付费观看需要判断用户是否有绑定了手机，没有绑定那个手机的情况需要让用户绑定手机
						Integer viewAuthorized = iliveRoom.getLiveEvent().getViewAuthorized();
						if (ILiveEvent.VIEW_AUTHORIZED_WHITE_LIST.equals(viewAuthorized)
								|| ILiveEvent.VIEW_AUTHORIZED_PAY.equals(viewAuthorized)
								||ILiveEvent.VIEW_AUTHORIZED_FCODE.equals(viewAuthorized)
								||ILiveEvent.VIEW_AUTHORIZED_THIRD.equals(viewAuthorized)) {
							ILiveManager liveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userBean.getUserId()));
							if(liveManager!= null && (liveManager.getMobile()==null || "".equals(liveManager.getMobile()))){
								resultJson.put("code", NOT_BIND_MOBILE);
							}else{
								resultJson.put("code", SUCCESS_STATUS);
							}
						}else{
							resultJson.put("code", SUCCESS_STATUS);
						}
						
						jsonObject.put("appPasswordAddr", appPasswordAddr);
						jsonObject.put("appFcodeAddr", appFcodeAddr);
						//resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "返回信息成功");
						resultJson.put("data", jsonObject);
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
					} else {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "用户没有登录");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
				} else {
					boolean jungeUserSession = jungleUtil.jungeUserSession(request);
					
					// 第一步 判断房间是否开启报名
					Integer openSignupSwitch = liveEvent.getOpenSignupSwitch();
					openSignupSwitch = openSignupSwitch == null ? 0 : openSignupSwitch;
					if (openSignupSwitch == 1) {
						// 判断是否报名通过
						boolean signUp = false;
						UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
						//先校验session后校验数据库
						java.util.HashSet<String> submitForm = (java.util.HashSet)request.getSession().getAttribute("submitForm");
						if(submitForm != null){
							signUp = submitForm.contains(String.valueOf(liveEvent.getFormId()));
						}
						if(!signUp && userBean!=null){
							signUp = bbsDiyfromDataMng.checkUserSignUp(userBean.getUserId(),
									liveEvent.getFormId());
						}
						if (signUp) {
							// 报名过了 设置房间为不需要报名
							liveEvent.setOpenSignupSwitch(0);
						} else {
							liveEvent.setOpenSignupSwitch(1);
							// 设置报名地址
							try {
								String h5HttpUrl = accessMethodBySeverGroupId.getH5HttpUrl();
								liveEvent.setDiyfromAddr(
										h5HttpUrl + "/phone/form.html?roomId=" + iliveRoom.getRoomId());
								liveEvent.setAppDiyformAddr(
										h5HttpUrl + "/phone/appform.html?roomId=" + iliveRoom.getRoomId());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
					if (jungeUserSession) {
						if (liveEvent.getViewAuthorized() == 2) {
							UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
							ILiveViewAuthBill checkILiveViewAuthBill = iLiveViewAuthBillMng.checkILiveViewAuthBill(
									userBean.getUserId(), iliveRoom.getLiveEvent().getLiveEventId());
							if (checkILiveViewAuthBill == null) {
								if (request.getSession().getAttribute("tempLoginInfo") != null) {
									TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
											.getAttribute("tempLoginInfo");
									if (tempLoginInfo != null) {
										if (tempLoginInfo.getMap()
												.get(iliveRoom.getLiveEvent().getLiveEventId()) != null
												&& tempLoginInfo.getMap().get(iliveRoom.getLiveEvent().getLiveEventId())
														.equals(oldliveEvent.getViewPassword())) {
											liveEvent.setViewAuthorized(1);
										}
									}
								}
							} else {
								liveEvent.setViewAuthorized(1);
							}
						}
					} else {
						if (liveEvent.getViewAuthorized() == 2) {
							if (request.getSession().getAttribute("tempLoginInfo") != null) {
								TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
										.getAttribute("tempLoginInfo");
								if (tempLoginInfo != null) {
									if (tempLoginInfo.getMap().get(iliveRoom.getLiveEvent().getLiveEventId()) != null
											&& tempLoginInfo.getMap().get(iliveRoom.getLiveEvent().getLiveEventId())
													.equals(oldliveEvent.getViewPassword())) {
										liveEvent.setViewAuthorized(1);
									}
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
						//如果是直播简介为了将图片居中显示需要增加一段样式表
						if (menuType == 4) {
							
							if(page.getRichContent()==null || page.getRichContent().equals("")){
								if(liveEvent.getLiveDesc()!=null) {
								page.setRichContent("<div id='livedescdiv'>"+liveEvent.getLiveDesc()+"</div><style>#livedescdiv img{max-width:98%;}</style>");
									
								}else {
								page.setRichContent("<div id='livedescdiv'></div><style>#livedescdiv img{max-width:98%;}</style>");
								}
							}else{
								page.setRichContent("<div id='livedescdiv'>"+page.getRichContent()+"</div><style>#livedescdiv img{max-width:98%;}</style>");
							}
							
						}
					}

					if (pageType2 && pageType3) {
						for (PageDecorate page : list) {
							Integer menuType = page.getMenuType();
							if (menuType == 3) {
								continue;
							}
							if (page.getMenuType().intValue() == 1) {
								// http://zb.tv189.com/phone/theme.html?roomId=352&userId=125
								String guideAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone"
										+ "/theme.html?roomId=" + roomId + "&userId=" + (userId == null ? 0 : userId);
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
								String guideAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone"
										+ "/theme.html?roomId=" + roomId + "&userId=" + (userId == null ? 0 : userId);
								String richContent = guideAddr;
								page.setRichContent(richContent);
							}
							newPageDecorate.add(page);
						}
					} else {
						for (PageDecorate page : list) {
							if (page.getMenuType().intValue() == 1) {
								// http://zb.tv189.com/phone/theme.html?roomId=352&userId=125
								String guideAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone"
										+ "/theme.html?roomId=" + roomId + "&userId=" + (userId == null ? 0 : userId);
								String richContent = guideAddr;
								page.setRichContent(richContent);
							}
							newPageDecorate.add(page);
						}
					}
					liveEvent.setPageRecordList(newPageDecorate);
					UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
					if (userBean != null) {
						boolean exist = iLiveEnterpriseFansMng.isExist(enterpriseId,
								Long.parseLong(userBean.getUserId()));
						Integer concernStatus = 0;
						if (exist) {
							concernStatus = 1;
						}
						iLiveEnterPrise.setConcernStatus(concernStatus);
					}
					int fansNum = iLiveEnterpriseFansMng.getFansNum(enterpriseId);
					iLiveEnterPrise.setConcernTotal(fansNum);
					if(iLiveEnterPrise.getEnterpriseName()==null){
						iLiveEnterPrise.setEnterpriseName("");
					}
					JSONObject jsonObject = iliveRoom.toViewBean(iliveRoom, liveEvent, iLiveEnterPrise);
					// 添加输入密码页面地址
					String appPasswordAddr;
					try {
						ILiveServerAccessMethod accessMethodBySever = accessMethodMng
								.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
						appPasswordAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/apppassword.html?roomId="
								+ roomId;
					} catch (Exception e) {
						appPasswordAddr = "";
					}
					//添加输入观看码页面地址
					String appFcodeAddr;
					try {
						ILiveServerAccessMethod accessMethodBySever = accessMethodMng
								.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
						appFcodeAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/appFCode.html?roomId="
								+ roomId;
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
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "内部异常");
			resultJson.put("data", new JSONObject());
			logger.error(e.toString());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
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
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			int viewAuthor = liveEvent.getViewAuthorized();
			if (needLogin) {
				boolean jungeUserSession = jungleUtil.jungeUserSession(request);
				if (jungeUserSession) {
					UserBean userBean = ILiveUtils.getAppUser(request);
					// 判断鉴权历史
					ILiveViewAuthBill checkILiveViewAuthBill = iLiveViewAuthBillMng
							.checkILiveViewAuthBill(userBean.getUserId(), liveEvent.getLiveEventId());
					if (checkILiveViewAuthBill == null) {
						if (viewAuthor == 4) {
							// 判断是否在白名单里面
						ILiveManager manager=	iLiveManagerMng.getILiveManager(Long.parseLong(userBean.getUserId()));
							boolean checkwhiteListExist = iLiveViewWhiteBillMng
									.checkUserInWhiteList(manager.getMobile(), liveEvent.getLiveEventId());
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
						if (viewAuthor == 7) {
							// 加入鉴权表
							//根據userId 查詢第三方的userId
							System.out.println("用户id："+userBean.getUserId());
							ILiveManager manager=iLiveManagerMng.selectILiveManagerById(Long.parseLong(userBean.getUserId()));
							Map<String, String> map = new HashMap<String, String>();
							map.put("userId", manager.getZhxyuserId());
							map.put("roomId", iliveRoom.getRoomId().toString());
							String checkUrl=iLiveEnterpriseMng.getILiveEnterPriseByAppId(manager.getZhxyappId()).getCheckIfCanUrl();
							System.out.println("checkUrl========="+checkUrl);
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
											System.out.println("access========================"+access);
											if(access==1){
												ILiveViewAuthBill buildViewAuth = this.buildViewAuth(7, userBean.getUserId(), iliveRoom);
												iLiveViewAuthBillMng.addILiveViewAuthBill(buildViewAuth);
												return true;
											}
										}
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						}
						// 登录观看的类型鉴权
						if (viewAuthor == 6) {
							// 加入鉴权表
							ILiveViewAuthBill checkILiveViewAuthBill1 = iLiveViewAuthBillMng.checkILiveViewAuthBill(
									userBean.getUserId(), iliveRoom.getRoomId());
							if(checkILiveViewAuthBill1!=null){
								ILiveViewAuthBill buildViewAuth = this.buildViewAuth(5, userBean.getUserId(), iliveRoom);
								iLiveViewAuthBillMng.addILiveViewAuthBill(buildViewAuth);
								return true;
							}
						}
						// 密码观看的鉴权
						if (viewAuthor == 2) {
							TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
									.getAttribute("tempLoginInfo");
							String loginResult = tempLoginInfo.getMap().get(liveEvent.getLiveEventId());
							if (loginResult != null && loginResult.equals(iliveRoom.getLiveEvent().getViewPassword())) {
								ILiveViewAuthBill buildViewAuth = this.buildViewAuth(2, userBean.getUserId(),
										iliveRoom);
								iLiveViewAuthBillMng.addILiveViewAuthBill(buildViewAuth);
								return true;
							} else {
								
								return false;
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
					if (viewAuthor == 2) {
						UserBean appUser = ILiveUtils.getAppUser(request);
						// 判断鉴权历史
						ILiveViewAuthBill checkILiveViewAuthBill = iLiveViewAuthBillMng
								.checkILiveViewAuthBill(appUser.getUserId(), liveEvent.getLiveEventId());
						if (checkILiveViewAuthBill == null) {
							TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
									.getAttribute("tempLoginInfo");
							String loginResult = tempLoginInfo.getMap().get(liveEvent.getLiveEventId());
							if (loginResult != null && loginResult.equals(iliveRoom.getLiveEvent().getViewPassword())) {
								ILiveViewAuthBill buildViewAuth = this.buildViewAuth(2, appUser.getUserId(), iliveRoom);
								iLiveViewAuthBillMng.addILiveViewAuthBill(buildViewAuth);
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
	public void checkRoomPassword(Integer roomId, Integer orginal, String roomPassword, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		if (roomPassword == null || "".equals(roomPassword.trim())) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "密码未输入");
			resultJson.put("data", new JSONObject());
		}
		if (orginal != null) {
			TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
			if (tempLoginInfo == null) {
				TempLoginInfo tempLoginInfo2 = new TempLoginInfo();
				tempLoginInfo2.setGetGuideAddr(true);
				tempLoginInfo2.setPasswdCheckResult(true);
				request.getSession().setAttribute("tempLoginInfo", tempLoginInfo2);
			} else {
				tempLoginInfo = new TempLoginInfo();
				tempLoginInfo.setGetGuideAddr(true);
				tempLoginInfo.setPasswdCheckResult(true);
				request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
			}
		}
		if (iliveRoom != null) {
			String viewPassword = iliveRoom.getLiveEvent().getViewPassword();
			if (viewPassword.equals(roomPassword)) {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "密码输入成功");
				TempLoginInfo loginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
				if (loginInfo != null) {
					loginInfo.getMap().put(iliveRoom.getLiveEvent().getLiveEventId(), roomPassword);
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
			HttpServletResponse response, String terminalType) {
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
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		Integer certStatus = iLiveEnterprise.getCertStatus();
		boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_FocusOnTheEnterprise,certStatus);
		if(b){
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_FocusOnTheEnterprise_Content);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			if (type == 1) {
				try {
					ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
					if (iLiveManager == null) {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "用户不能为空");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
					iLiveEnterpriseFansMng.addEnterpriseConcern(enterpriseId, userId);
					if(JedisUtils.exists("enterPriseFansNum:"+enterpriseId)) {
						int fansNum = Integer.parseInt(JedisUtils.get("enterPriseFansNum:"+enterpriseId));
						JedisUtils.del("enterPriseFansNum:"+enterpriseId);
						JedisUtils.set("enterPriseFansNum:"+enterpriseId, (fansNum+1)+"", 18000);
					}
					ILiveEnterpriseTerminalUser terminalUser = new ILiveEnterpriseTerminalUser();
					terminalUser.setEnterpriseId(enterpriseId);
					// 0为观看 1为关注
					int fansType = 1;
					terminalUser.setFansType(fansType);
					terminalUser.setIsBlacklist(0);
					terminalUser.setIsDel(0);
					terminalUser.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
					// 0为app登录
					int loginType = 0;
					terminalUser.setLoginType(loginType);
					terminalUser.setNailName(iLiveManager.getNailName());
					terminalUser.setUserId(userId);
					terminalUser.setUserImg(iLiveManager.getUserImg());
					terminalUser.setMobile(iLiveManager.getMobile());
					iLiveEnterpriseTerminalUserMng.saveTerminaluser(terminalUser);
					resultJson.put("code", SUCCESS_STATUS);
					
					try {
						String ipAddr = RequestUtils.getIpAddr(request);
						ILiveUserViewStatics.INSTANCE.concernEnterprise(String.valueOf(userId), enterpriseId, ipAddr,
								terminalType);
					} catch (Exception e) {
					}
					if (TerminalUtil.checkApp(terminalType)) {
						String concern_app = MessageCache.messageCache.get("concern_app");
						if (!StringUtils.isEmpty(concern_app)) {
							resultJson.put("message", concern_app);
						} else {
							resultJson.put("message", "关注成功");
						}
					} else if (TerminalUtil.checkH5(terminalType)) {
						String concern_h5 = MessageCache.messageCache.get("concern_h5");
						if (!StringUtils.isEmpty(concern_h5)) {
							resultJson.put("message", concern_h5);
						} else {
							resultJson.put("message", "关注成功");
						}
					} else if (TerminalUtil.checkPc(terminalType)) {
						String concern_pc = MessageCache.messageCache.get("concern_pc");
						if (!StringUtils.isEmpty(concern_pc)) {
							resultJson.put("message", concern_pc);
						} else {
							resultJson.put("message", "关注成功");
						}
					} else {
						resultJson.put("message", "关注成功");
					}
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
					try {
						String ipAddr = RequestUtils.getIpAddr(request);
						ILiveUserViewStatics.INSTANCE.unconcernEnterprise(String.valueOf(userId), enterpriseId, ipAddr,
								terminalType);
					} catch (Exception e) {
					}
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
			logger.error(e.toString());
		}
	}

	/**
	 * 个人关注的企业列表
	 * 
	 * @param userId
	 */
	@RequestMapping(value = "/concern/list.jspx")
	public void getMyConcernList(Long userId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		try {
			List<Integer> myEnterprise = iLiveEnterpriseFansMng.getMyEnterprise(userId);
			if (myEnterprise != null && !myEnterprise.isEmpty()) {
				for (Integer enterpriseId : myEnterprise) {
					ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
					JSONObject simpleJsonObject = iLiveEnterPrise.toSimpleJsonObject();
					String defaultEnterpriseServerId = ConfigUtils.get("defaultEnterpriseServerId");
					ILiveServerAccessMethod serverGroup = accessMethodMng
							.getAccessMethodBySeverGroupId(Integer.parseInt(defaultEnterpriseServerId));
					String homePageUrl = serverGroup.getH5HttpUrl() + "/home/index.html?enterpriseId=" + enterpriseId;
					simpleJsonObject.put("homePageUrl", homePageUrl);
					jsonArr.put(simpleJsonObject);
				}
				resultJson.put("data", jsonArr);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取成功");
			} else {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取成功");
				resultJson.put("data", jsonArr);
			}
		} catch (Exception e) {
			resultJson.put("data", new JSONObject());
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取失败");
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 最后一次进入直播间
	 * 
	 * @param roomId
	 * @param sessionType
	 * @param request
	 * @param response
	 * @param webType
	 *            1是web端 2是socket端
	 */
	@RequestMapping(value = "/roomenter.jspx")
	public void enterLiveRoomIng(Integer roomId, Integer sessionType, HttpServletRequest request,
			HttpServletResponse response, Integer webType) {
		boolean jungeAuthFinish = this.jungeAuthFinish(roomId, request);
		webType = webType == null ? 0 : webType;
		if (jungeAuthFinish) {
			UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap2 = ApplicationCache.getUserListMap();
			 //ILiveLiveRoom iliveRoom=iLiveRoomMng.getIliveRoom(roomId);
			
			ILiveLiveRoom iliveRoom=null;
			  if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) { 
				  iliveRoom=SerializeUtil.getObjectRoom(roomId); }
			  else {
				  iliveRoom=iLiveRoomMng.getIliveRoom(roomId);
				  JedisUtils.setByte(("roomInfo:"+roomId).getBytes(),SerializeUtil.serialize(iliveRoom), 300);
				  
			  }
			 
			 
			Integer enterpriseId = iliveRoom.getEnterpriseId();
			List<ILiveLiveRoom> rooms = iLiveRoomMng.findRoomListPassByEnterprise(enterpriseId);
			long number = 0;
			for (ILiveLiveRoom iLiveLiveRoom : rooms) {
				ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap2.get(iLiveLiveRoom.getRoomId());
				if(concurrentHashMap==null){
					concurrentHashMap = new ConcurrentHashMap<String, UserBean>();
				}
				number = number +concurrentHashMap.size();
			}
			//ILiveEnterprise iLiveEnterPrise=iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			ILiveEnterprise iLiveEnterPrise=null;
			
			 if(JedisUtils.exists(("enterpriseInfo:"+enterpriseId).getBytes())) {
			 iLiveEnterPrise= SerializeUtil.getObjectEnterprise(enterpriseId); }else {
			  iLiveEnterPrise=iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			  JedisUtils.setByte(("enterpriseInfo:"+enterpriseId).getBytes(),
			  SerializeUtil.serialize(iLiveEnterPrise), 300); }
			 
			boolean b = EnterpriseUtil.selectIfContent(EnterpriseCache.ENTERPRISE_Concurrent, number, enterpriseId, iLiveEnterPrise.getCertStatus());
			if(b){
				JSONObject resultJson = new JSONObject();
				resultJson.put("code", Constants.ERROR_full);
				resultJson.put("message", "直播间观看人数已满，无法进行观看");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			// 从观看端进来的人都是以个人身份
			if (userBean != null) {
				String userId=userBean.getUserId()+"_"+request.getSession().getId();
				if("open".equals(ConfigUtils.get("redis_service"))) {
					
						JedisUtils.setAdd("userIdList"+roomId,userId);
				}
				sessionType = sessionType == null ? 0 : sessionType;
				this.entLiveLoginByUser(userBean.getUserId(), roomId, sessionType, 0, request, response, webType);
				//通知有人进入直播间
				ILiveMessage iLiveMessage = new ILiveMessage();
				iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_USER);
				iLiveMessage.setWelcomeLanguage("欢迎【"+userBean.getNickname()+"】进入直播间");
				iLiveMessage.setMsgId(Long.parseLong((-roomId+"")));
				JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
				JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
				if("open".equals(ConfigUtils.get("redis_service"))) {
					Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
					if(userIdList!=null&&userIdList.size()!=0) {
						for (String userIds : userIdList) {
							boolean flag=true;
							while (flag) {
								String requestionIdString=UUID.randomUUID().toString();
								if(JedisUtils.tryGetDistributedLock(userIds+"lock", requestionIdString, 1)) {
									JedisUtils.del(roomId+":"+userIds);
									List<String> msgIdList =new ArrayList<String>();
									msgIdList.add(iLiveMessage.getMsgId()+"");
									JedisUtils.setList(roomId+":"+userIds, msgIdList, 0);
									flag=false;
									JedisUtils.releaseDistributedLock(userIds+"lock", requestionIdString);
								}else {
									try {
										Thread.sleep(100);
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
							}
						}
					}
				}else {
					ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
					ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
					if(concurrentHashMap!=null){
						Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
						while (userIterator.hasNext()) {
							
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
				}
				
			} else {
				this.entLiveLogin(roomId, sessionType, 0, request, response, webType);
			}
		} else {
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", Constants.ERROR_authentication);
			resultJson.put("message", "鉴权失败,无法进入直播间");
			JSONObject jsonObject = new JSONObject();
			String appErrorAddr;
			try {
				ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
				ILiveServerAccessMethod accessMethodBySever = accessMethodMng
						.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
				appErrorAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/nopermission.html";
			} catch (Exception e) {
				appErrorAddr = "http://zbt.tv189.net/phone/nopermission.html";
			}
			jsonObject.put("appErrorAddr", appErrorAddr);
			resultJson.put("data", jsonObject);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
	}
	
	/**
	 * 使用F码
	 * 
	 * @param roomId
	 *            房间id
	 * @param fcode
	 *            F码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/userFCode.jspx")
	public void userFCode(Integer roomId,Long fileId, String code, HttpServletRequest request, HttpServletResponse response) {
		ILiveFCode fCode = fCodeMng.getBeanByCode(fileId==null?roomId:0, fileId, code);
		JSONObject resultJson = new JSONObject();
		UserBean appUser = ILiveUtils.getAppUser(request);
		System.out.println("獲取到的用戶信息為==================================："+appUser);
		if (null == appUser) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户未登录");
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		String userId = appUser.getUserId();
		//查询该用户该直播间时候已有绑定F码，是则不允许再绑
		ILiveFCode checkfCode =fCodeMng.checkFcode(Long.parseLong(userId),roomId,fileId);
		if(checkfCode!=null){
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "该用户已绑定观看码，无需再次绑定！");
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		if (null != fCode && null != userId) {
			Integer status = fCode.getStatus();
			if (!ILiveFCode.STATUS_NEW.equals(status)) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "F码已失效");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			Long bindId=fCode.getBindUserId();
			if(bindId!=null&&!userId.equals(bindId)){
				resultJson.put("code", FCODE_BIND);
				resultJson.put("message", "该观看码已被其他用户绑定！");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			Long id = fCode.getId();
			fCodeMng.useByCodeId(id, Long.parseLong(userId),fileId);
			ILiveManager manager= iLiveManagerMng.selectILiveManagerById(Long.parseLong(userId));
			try {
				fCode.setBindUserId(Long.parseLong(userId));
				fCode.setBindUserName(manager.getUserName()==null?manager.getMobile():manager.getUserName());
				fCode.setBingNailName(manager.getNailName());
				fCodeMng.update(fCode);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				ILiveLiveRoom liveRoom = iLiveRoomMng.findById(roomId);
				ILiveEvent liveEvent;
				if(liveRoom==null){
					liveEvent=new ILiveEvent();
					liveEvent.setLiveEventId(0L);
				}else{
				liveEvent = liveRoom.getLiveEvent();
				}
				Long liveEventId = liveEvent.getLiveEventId();
				ILiveViewAuthBill authbill = new ILiveViewAuthBill();
				authbill.setAuthPassTime(new Timestamp(System.currentTimeMillis()));
				authbill.setAuthType(ILiveViewAuthBill.AUTH_TYPE_FCODE);
				authbill.setDeleteStatus(0);
				authbill.setLiveEventId(liveEventId);
				authbill.setLiveRoomId(roomId);
				authbill.setFileId(fileId==null?0L:fileId);
				authbill.setUserId(String.valueOf(userId));
				iLiveViewAuthBillMng.addILiveViewAuthBill(authbill);
			
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "F码验证成功");
			ResponseUtils.renderJson(request, response, resultJson.toString());
		} else {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "F码验证失败");
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
	}

	public void entLiveLoginByUser(String userId, Integer liveId, Integer sessionType, Integer userType,
			HttpServletRequest request, HttpServletResponse response, Integer webType) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom live = iLiveRoomMng.findById(liveId);
			if (live == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
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
			System.out.println("登录用户生存客户端登录token 的liveid=" + liveId);
			System.out.println("登录用户生存客户端登录token 的userid=" + userId);
			String key = userId + "_" + request.getSession().getId();
			userMap.put(key, liveUser);
			// 生存客户端登录token
			UUID uuid = UUID.randomUUID();
			String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
			// System.out.println("登录在线用户生存客户端登录token=" + token);
			JSONObject json = new JSONObject();
			json.put("liveId", liveId);
			json.put("roomId", liveId);
			json.put("userId", key);
			json.put("webType", webType);
			json.put("liveEventId", live.getLiveEvent().getLiveEventId());
			CacheManager.putCacheInfo(CacheManager.mobile_token_ + token, json, 5 * 60 * 1000);
			JedisUtils.del(CacheManager.mobile_token_ + token);
			JedisUtils.set(CacheManager.mobile_token_ + token, json.toString(), 300);
			/**
			 * 获取直播间推流地址
			 */
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = accessMethodMng.getAccessMethodBySeverGroupId(live.getServerGroupId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, String> generatorEncoderPwd = SafeTyChainPasswdUtil.INSTANCE.generatorEncoderPwd();
			String pushStreamAddr = HTTP_PROTOACAL + accessMethodBySeverGroupId.getHttp_address() + "/live/live"
					+ live.getRoomId() + "/5000k/tzwj_video.m3u8";
			pushStreamAddr = String.format("%s?ut=%s&us=%s&sign=%s", pushStreamAddr,
					generatorEncoderPwd.get("timestamp"), generatorEncoderPwd.get("sequence"),
					generatorEncoderPwd.get("encodePwd"));
			String rtmpAddr  = "rtmp://" + accessMethodBySeverGroupId.getCdnLiveHttpUrl() + ":"
						+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + live.getRoomId() + "_tzwj_5000k";
			
			
			rtmpAddr = String.format("%s?ut=%s&us=%s&sign=%s", rtmpAddr, generatorEncoderPwd.get("timestamp"),
					generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
			ILiveEvent liveEvent = new ILiveEvent();
			BeanUtilsSpring.copyProperties(liveEvent, live.getLiveEvent());
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
				UserBean userBean = ILiveUtils.getAppUser(request);
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
			iLiveEventMng.putLiveEventUserCache(liveEvent.getLiveEventId());
			JSONObject liveJson = live.putLiveInJson(null, liveEvent);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("data", liveJson);
			resultJson.put("hlsAddr", pushStreamAddr);
			resultJson.put("token", token);
			resultJson.put("rtmpAddr", rtmpAddr);
			
			ILiveViewRecord viewRecord = new ILiveViewRecord();
			viewRecord.setViewOrigin(0);
			viewRecord.setManagerId(Long.parseLong(userId));
			viewRecord.setOuterId(live.getLiveEvent().getLiveEventId());
			viewRecord.setViewType(1);
			iLiveViewRecordMng.addILiveViewRecord(viewRecord);
			// 粉丝数据
			ILiveEnterpriseTerminalUser terminalUser = new ILiveEnterpriseTerminalUser();
			terminalUser.setEnterpriseId(live.getEnterpriseId());
			// 0为观看 1关注
			int fansType = 0;
			terminalUser.setFansType(fansType);
			terminalUser.setIsBlacklist(0);
			terminalUser.setIsDel(0);
			terminalUser.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
			// 0为app登录
			int loginType = 0;
			terminalUser.setLoginType(loginType);
			terminalUser.setNailName(liveUser.getNickname());
			terminalUser.setUserId(Long.parseLong(liveUser.getUserId()));
			terminalUser.setUserImg(liveUser.getUserThumbImg());
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
			terminalUser.setMobile(iLiveManager.getMobile());
			iLiveEnterpriseTerminalUserMng.saveTerminaluser(terminalUser);
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
	 * 进入直播间
	 * 
	 * @param liveId
	 * @param sessionType
	 * @param userType
	 * @param request
	 * @param response
	 * @param webType
	 */
	public void entLiveLogin(Integer liveId, Integer sessionType, Integer userType, HttpServletRequest request,
			HttpServletResponse response, Integer webType) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom live=null;
			
			  
			  if(JedisUtils.exists(("roomInfo:"+liveId).getBytes())) { live=
			  SerializeUtil.getObjectRoom(liveId); }else {
			  live=iLiveRoomMng.getIliveRoom(liveId);
			 JedisUtils.setByte(("roomInfo:"+liveId).getBytes(),
			  SerializeUtil.serialize(live), 300); }
			 
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
			userMap.put(userId, liveUser);
			if("open".equals(ConfigUtils.get("redis_service"))) {
				
					JedisUtils.setAdd("userIdList"+liveId,userId);
					
				
			}
			// 生存客户端登录token
			UUID uuid = UUID.randomUUID();
			String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
			JSONObject json = new JSONObject();
			ILiveEvent liveEvent = live.getLiveEvent();
			Long liveEventId = liveEvent.getLiveEventId();
			json.put("liveId", liveId);
			json.put("roomId", liveId);
			json.put("userId", userId);
			json.put("webType", webType);
			json.put("liveEventId", liveEventId);
			resultJson.put("estoppelType", liveEvent.getEstoppelType());
			resultJson.put("forbidTalk", 0);
			CacheManager.putCacheInfo(CacheManager.mobile_token_ + token, json, 5 * 60 * 1000);
			JedisUtils.del(CacheManager.mobile_token_ + token);
			JedisUtils.set(CacheManager.mobile_token_ + token, json.toString(), 300);
			/**
			 * 获取直播间推流地址
			 */
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = accessMethodMng.getAccessMethodBySeverGroupId(live.getServerGroupId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, String> generatorEncoderPwd = SafeTyChainPasswdUtil.INSTANCE.generatorEncoderPwd();
			String pushStreamAddr = HTTP_PROTOACAL + accessMethodBySeverGroupId.getHttp_address() + "/live/live"
					+ live.getRoomId() + "/5000k/tzwj_video.m3u8";
			pushStreamAddr = String.format("%s?ut=%s&us=%s&sign=%s", pushStreamAddr,
					generatorEncoderPwd.get("timestamp"), generatorEncoderPwd.get("sequence"),
					generatorEncoderPwd.get("encodePwd"));
			String rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getCdnLiveHttpUrl() + ":"
					+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + live.getRoomId() + "_tzwj_5000k";
			rtmpAddr = String.format("%s?ut=%s&us=%s&sign=%s", rtmpAddr, generatorEncoderPwd.get("timestamp"),
					generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
			
			
			//增加判断用户IP，特定用户IP地址的情况直接使用HLS播放
			String hlsLiveIp = ConfigUtils.get("hls_live_ip");
			if(hlsLiveIp != null){
				String ips[] = hlsLiveIp.split(",");
				String userIp = IPUtil.getIP(request);
				
				String[] userIps = userIp.split(",");
				for(String uip: userIps){
					for(String ip : ips){
						if(ip.equals(uip)){
							rtmpAddr = pushStreamAddr;
						}
					}
				}
			}
			
			
			JSONObject liveJson = live.putLiveInJson(null);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("data", liveJson);
			resultJson.put("token", token);
			resultJson.put("hlsAddr", pushStreamAddr);
			resultJson.put("rtmpAddr", rtmpAddr);
			resultJson.put("appointment", 0);
			iLiveEventMng.putLiveEventUserCache(liveEventId);
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
	 * 获取回看列表
	 * 
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
		if(pageNo==null){
			pageNo=1;
		}
        if(JedisUtils.exists("recordlist:"+roomId+"_"+pageNo)) {
        	
			ResponseUtils.renderJson(httpServletRequest, httpServletResponse, JedisUtils.get("recordlist:"+roomId+"_"+pageNo));
			return;
		}else {
			Map<String, Object> sqlParam = new HashMap<>();
			if (roomId == null) {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "信息获取成功");
				resultJson.put("data", new JSONArray());
				ResponseUtils.renderJson(httpServletRequest, httpServletResponse, resultJson.toString());
				return;
			}
			try {
				sqlParam.put("roomId", roomId);
				List<AppMediaFile> mediaFilePageByRoom = iLiveVideoHistoryMng.getMediaFilePageByRoom(sqlParam,
						pageNo == null ? 1 : pageNo, pageSize == null ? 15 : pageSize, roomId);
				JSONArray jArr = new JSONArray();
				if (mediaFilePageByRoom != null && !mediaFilePageByRoom.isEmpty()) {
					JSONObject jo = null;
					for (AppMediaFile mediaFile : mediaFilePageByRoom) {
						jo = new JSONObject(mediaFile);
						jArr.put(jo);
					}
				}
				//获取是否显示回看和会看列表标题名称
				ILiveLiveRoom iLiveLiveRoom = iLiveRoomMng.findById(roomId);
				ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
				String hkmc = "相关视频";
				Integer relatedVideo=iLiveLiveRoom.getRelatedVideo();
				List<PageDecorate> list = pageDecorateMng.getList(liveEvent.getLiveEventId());
				if(list!=null && !list.isEmpty()) {
					
					Iterator<PageDecorate> iterator = list.iterator();
					while (iterator.hasNext()) {
						PageDecorate page = iterator.next();
						if(page.getMenuType()==4) {
							//relatedVideo=page.getRelatedVideo();
							hkmc=page.getMenuName1();
						}
						
					}
				}
				resultJson.put("hkmc", hkmc==null?"相关视频":hkmc);
				resultJson.put("relatedVideo", relatedVideo==null?1:relatedVideo);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "信息获取成功");
				resultJson.put("data", jArr);
				JedisUtils.set("recordlist:"+roomId, resultJson.toString(), 300);
			} catch (Exception e) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "信息获取失败");
				resultJson.put("data", new JSONObject());
				e.printStackTrace();
				logger.error(e.toString());
			}
			ResponseUtils.renderJson(httpServletRequest, httpServletResponse, resultJson.toString());
		}
		
	}
	/**
	 * 获取回看相关视频列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param roomId
	 * @param httpServletResponse
	 * @param httpServletRequest
	 */
	@RequestMapping("/getrelatedlist.jspx")
	public void getMediaRelatedList(Integer pageNo, Integer pageSize, Long fileId,
			HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
		JSONObject resultJson = new JSONObject();
		if (fileId == null) {
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "信息获取成功");
			resultJson.put("data", new JSONArray());
			ResponseUtils.renderJson(httpServletRequest, httpServletResponse, resultJson.toString());
			return;
		}
		try {
			if (null == pageNo) {
				pageNo = 1;
			}
			if (null == pageSize) {
				pageSize = 200;
			}
			List<AppMediaFile> mediaFilePageByRoom = mediaFileRelatedMng.listForApp(fileId, pageNo, pageSize);
			JSONArray jArr = new JSONArray();
			if (mediaFilePageByRoom != null && !mediaFilePageByRoom.isEmpty()) {
				JSONObject jo = null;
				for (AppMediaFile mediaFile : mediaFilePageByRoom) {
					jo = new JSONObject(mediaFile);
					jArr.put(jo);
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "信息获取成功");
			resultJson.put("data", jArr);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "信息获取失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(httpServletRequest, httpServletResponse, resultJson.toString());
	}
	
	
	/**
	 * 获取直播间列表
	 */
	@RequestMapping(value = "/search.jspx")
	public void getSearchContent(String keyWord, Integer searchType, Integer pageNo, Long userId, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			if( keyWord != null) keyWord = keyWord.trim();
			if(searchType==null||"".equals(searchType)) {
				searchType=0;
			}
			if (searchType == 0) {
				// 全部
				List<AppILiveRoom> roomList = iLiveRoomMng.getTop4ForView(keyWord);
				List<AppMediaFile> fileList = iLiveMediaFileMng.getTop4ForView(keyWord);
				if (userId == null) {
					userId = 0L;
				}
				List<ILiveAppEnterprise> enterpriseList = iLiveEnterpriseMng.getTop4ForView(keyWord, userId);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取搜索列表成功");
				JSONObject jsonMap = new JSONObject();
				jsonMap.put("appIliveRoom", roomList);
				jsonMap.put("appMediaFile", fileList);
				jsonMap.put("appEnterprise", enterpriseList);
				resultJson.put("data", jsonMap);
			} else if (searchType == 1) {
				// 直播
				List<AppILiveRoom> roomList = iLiveRoomMng.getPagerForView(keyWord, pageNo, pageSize, searchType);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取搜索列表成功");
				resultJson.put("data", roomList);
			} else if (searchType == 2) {
				// 回看
				List<AppMediaFile> fileList = iLiveMediaFileMng.getPagerForView(keyWord, pageNo, pageSize, searchType);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取搜索列表成功");
				resultJson.put("data", fileList);
			} else if (searchType == 3) {
				// 企业
				List<ILiveAppEnterprise> enterpriseList = iLiveEnterpriseMng.getPagerForView(keyWord, pageNo, pageSize,
						searchType, userId);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取搜索列表成功");
				resultJson.put("data", enterpriseList);
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "查询类型不正确");
				resultJson.put("data", new JSONObject());
			}
		} catch (Exception e) {
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "查询失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 获取直播间列表
	 */
	@RequestMapping(value = "/userContentSearch.jspx")
	public void getSearchUserContent(String keyWord, Integer searchType, Integer pageNo, Long userId, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			if( keyWord != null) keyWord = keyWord.trim();
			if (searchType == 0) {
				// 全部
//				List<AppILiveRoom> roomList = iLiveRoomMng.getTop4ForView(keyWord);
//				List<AppMediaFile> fileList = iLiveMediaFileMng.getTop4ForView(keyWord);
				if (userId == null) {
					userId = 0L;
				}
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
				Integer level=iLiveManager.getLevel();
				if(level==null){
					level=0;
				}
				Integer enterpriseId=iLiveManager.getEnterpriseId().intValue();
				boolean per=true;
				List<JSONObject> roomList1 = iLiveRoomMng.getPagerForView1(keyWord, pageNo, pageSize, searchType,per,enterpriseId,userId,level);
			    //查询子账户是否有点播视频查看全部权限
				boolean per1=true;
				List<AppMediaFile> fileList1 = iLiveMediaFileMng.getPagerForView(keyWord, pageNo, pageSize, searchType,per1,enterpriseId,userId,level);
				
				List<ILiveAppEnterprise> enterpriseList = iLiveEnterpriseMng.getPagerForView1(keyWord, pageNo, pageSize,
						searchType, userId);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取搜索列表成功");
				JSONObject jsonMap = new JSONObject();
				jsonMap.put("appIliveRoom", roomList1);
				jsonMap.put("appMediaFile", fileList1);
				jsonMap.put("appEnterprise", enterpriseList);
				resultJson.put("data", jsonMap);
			} else if (searchType == 1) {
				// 直播
				if (userId == null) {
					userId = 0L;
				}
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
				Integer level=iLiveManager.getLevel();
				Integer enterpriseId=iLiveManager.getEnterpriseId().intValue();
				List<AppILiveRoom> roomList = iLiveRoomMng.getPagerForView(keyWord, pageNo, pageSize, searchType);
				boolean per=true;
				List<JSONObject> roomList1 = iLiveRoomMng.getPagerForView1(keyWord, pageNo, pageSize, searchType,per,enterpriseId,userId,level);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取搜索列表成功");
				resultJson.put("data", roomList1);
			} else if (searchType == 2) {
				// 回看
				if (userId == null) {
					userId = 0L;
				}
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
				Integer level=iLiveManager.getLevel();
				Integer enterpriseId=iLiveManager.getEnterpriseId().intValue();
				//List<AppMediaFile> fileList = iLiveMediaFileMng.getPagerForView(keyWord, pageNo, pageSize, searchType);
				//查询子账户是否有点播视频查看全部权限
				boolean per=true;
				List<AppMediaFile> fileList1 = iLiveMediaFileMng.getPagerForView(keyWord, pageNo, pageSize, searchType,per,enterpriseId,userId,level);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取搜索列表成功");
				resultJson.put("data", fileList1);
			} else if (searchType == 3) {
				// 企业
				List<ILiveAppEnterprise> enterpriseList = iLiveEnterpriseMng.getPagerForView1(keyWord, pageNo, pageSize,
						searchType, userId);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取搜索列表成功");
				resultJson.put("data", enterpriseList);
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "查询类型不正确");
				resultJson.put("data", new JSONObject());
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "查询失败");
			resultJson.put("data", "");
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 推荐直播
	 */
	@RequestMapping(value = "/live/recommend.jspx")
	public void recommendLive(HttpServletRequest request, Long userId, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			String realPath = request.getSession().getServletContext().getRealPath("/");
			File file = new File(realPath, "WEB-INF/tempdata/recommend.json");
			if (file.exists()) {
				String readFileToString = this.buildJsonStr(file);
				// System.out.println("readFileToString-->" + readFileToString);
				if (readFileToString != null && !readFileToString.trim().equals("")) {
					JSONArray jsonArr = new JSONArray(readFileToString);
					for (int i = 0; i < jsonArr.length(); i++) {
						JSONObject jsonObject = jsonArr.getJSONObject(i);
						JSONObject appEnterprise = jsonObject.getJSONObject("appEnterprise");
						int enterpriseId = appEnterprise.getInt("enterpriseId");
						boolean exist = iLiveEnterpriseFansMng.isExist(enterpriseId, userId);
						int fansNum = iLiveEnterpriseFansMng.getFansNum(enterpriseId);
						appEnterprise.put("concernTotal", fansNum);
						if (exist) {
							appEnterprise.put("concernStatus", 1);
						} else {
							appEnterprise.put("concernStatus", 0);
						}
					}
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "获取推荐列表成功");
					resultJson.put("data", jsonArr);
				} else {
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "获取推荐列表成功");
					resultJson.put("data", new JSONArray());
				}
			} else {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取推荐列表成功");
				resultJson.put("data", new JSONArray());
			}
		} catch (Exception e) {
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取推荐列表成功");
			resultJson.put("data", new JSONArray());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 构建JSON
	 * 
	 * @param file
	 * @return
	 */
	private String buildJsonStr(File file) {
		BufferedReader reader = null;
		String json = "";
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String str = null;
			while ((str = reader.readLine()) != null) {
				json += str;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return json;
	}

	/**
	 * 构建推荐
	 * 
	 * @param validRegex
	 * @param liveRoomIds
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/live/generatorRecommend.jspx")
	public void generatorRecommendLive(String validRegex, String liveRoomIds, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (validRegex != null && validRegex.equals("jwztadmin2018")) {
			if (liveRoomIds != null) {
				String[] splitArr = liveRoomIds.split(",");
				String realPath = request.getSession().getServletContext().getRealPath("/");
				File file = new File(realPath, "WEB-INF/tempdata/recommend.json");
				iLiveRoomMng.createRecommends(splitArr, file);
			}
		}
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "创建成功");
		resultJson.put("data", new JSONObject());
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

}
