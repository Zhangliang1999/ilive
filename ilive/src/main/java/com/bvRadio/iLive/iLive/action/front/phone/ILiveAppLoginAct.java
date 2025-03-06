package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;
import com.bvRadio.iLive.iLive.action.front.phone.ILiveRoomAppAct;

import java.awt.Window;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.iLive.util.*;
import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.action.front.constant.ThirdOrgnization;
import com.bvRadio.iLive.iLive.action.front.vo.AppDiyFormVo;
import com.bvRadio.iLive.iLive.action.front.vo.AppDiyformReceiver;
import com.bvRadio.iLive.iLive.action.front.vo.AppJungleUtil;
import com.bvRadio.iLive.iLive.action.front.vo.AppLoginValid;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.action.front.vo.AppUserInfo;
import com.bvRadio.iLive.iLive.action.front.vo.DiyFormModelVo;
import com.bvRadio.iLive.iLive.action.front.vo.DiyFormVo;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.action.front.vo.TempLoginInfo;
import com.bvRadio.iLive.iLive.action.front.vo.ThirdLoginVo;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.BBSDiyformData;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.BBSDiyformDataMng;
import com.bvRadio.iLive.iLive.manager.BBSDiyformMng;
import com.bvRadio.iLive.iLive.manager.BBSDiymodelMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewAuthBillMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.sms.ILiveSmgServerCenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwzt.comm.StringUtils;
import com.jwzt.jssdk.EmojiFilter;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/appuser")
public class ILiveAppLoginAct {

	Logger logger = LoggerFactory.getLogger(ILiveAppLoginAct.class);
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private ILiveLiveRoomMng liveRoomMng;

	@Autowired
	private BBSDiyformMng bbsDiyformMng;

	@Autowired
	private BBSDiymodelMng bbsDiymodelMng;

	@Autowired
	private BBSDiyformDataMng dataMng;

	@Autowired
	private ILiveEnterpriseFansMng enterpriseFansMng;
	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;
	@Autowired
	private SentitivewordFilterMng sentitivewordFilterMng;
	/**
	 * 登录接口 1是账号密码登录 2是验证码登录
	 * 
	 * @param username
	 * @param password
	 * @param vpassword
	 * @param jpushId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/login.jspx")
	public void login(String phoneNum, Integer orginal, String password, String vpassword, Integer type,
			HttpServletRequest request, HttpServletResponse response, String terminalType) {
		JSONObject resultJson = new JSONObject();
		Cache cacheInfo = null;
		if (orginal != null) {
			TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
			if (tempLoginInfo == null) {
				TempLoginInfo tempLoginInfo2 = new TempLoginInfo();
				tempLoginInfo2.setGetGuideAddr(true);
				request.getSession().setAttribute("tempLoginInfo", tempLoginInfo2);
			} else {
				tempLoginInfo.setGetGuideAddr(true);
				request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
			}
		}
		try {
			if (phoneNum == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "手机号必须输入");
				resultJson.put("data", new JSONObject());
			} else if (type == 1 && password == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请输入密码");
				resultJson.put("data", new JSONObject());
			} else if (type == 2 && vpassword == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请输入验证码");
				resultJson.put("data", new JSONObject());
			} else {
				if (type == 2) {
					cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "login_" + phoneNum);
					System.out.println("cacheInfo======="+cacheInfo);
					String defaultVpassword = ConfigUtils.get("defaultVpassword");
					if (defaultVpassword.equals(vpassword)) {
					} else if (cacheInfo == null) {
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
					} else if (!vpassword.equals(cacheInfo.getValue())) {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "验证码不通过");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
					ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByPhoneNumber(phoneNum);
					if (iLiveMangerByMobile == null) {
						long registeredManagerId = iLiveManagerMng.registeredManager(phoneNum, password, 0, type,
								terminalType);
						iLiveMangerByMobile = iLiveManagerMng.getILiveManager(registeredManagerId);
						ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
						IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise, request);
						String loginToken = this.buildLoginToken();
						retInfo.setLoginToken(loginToken);
						iLiveMangerByMobile.setLoginToken(loginToken);
						iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "登录成功");
						resultJson.put("data", new JSONObject(retInfo));
						retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
						UserBean userBean = new UserBean();
						userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
						userBean.setLoginToken(loginToken);
						userBean.setUsername(iLiveMangerByMobile.getMobile());
						userBean.setNickname(iLiveMangerByMobile.getNailName());
						userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
						userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
						userBean.setLevel(iLiveMangerByMobile.getLevel()==null?0:iLiveMangerByMobile.getLevel());
						ILiveUtils.setAppUser(request, userBean);
						if(CacheManager.cacheExpired(cacheInfo)){
							cacheInfo.setExpired(true);
							CacheManager.clearOnly(CacheManager.mobile_token_ + "login_" + phoneNum);
						}
						System.out.println("cacheInfo=========="+cacheInfo);
						logger.info("登录时返回的sessionId->" + request.getSession().getId());
					} else {
						ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
						IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise , request);
						String loginToken = this.buildLoginToken();
						retInfo.setLoginToken(loginToken);
						iLiveMangerByMobile.setLoginToken(loginToken);
						iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "登录成功");
						resultJson.put("data", new JSONObject(retInfo));
						retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
						UserBean userBean = new UserBean();
						userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
						userBean.setLoginToken(loginToken);
						userBean.setUsername(iLiveMangerByMobile.getMobile());
						userBean.setNickname(iLiveMangerByMobile.getNailName());
						userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
						userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
						ILiveUtils.setAppUser(request, userBean);
						if(CacheManager.cacheExpired(cacheInfo)){
							cacheInfo.setExpired(true);
							CacheManager.clearOnly(CacheManager.mobile_token_ + "login_" + phoneNum);
						}
						System.out.println("cacheInfo=========="+cacheInfo);
						logger.info("登录时返回的sessionId->" + request.getSession().getId());
					}
				} else {
					ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
					if (iLiveMangerByMobile == null) {
						// 开启注册登录
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "登录失败,请确认账户或密码是否正确");
						resultJson.put("data", new JSONObject());
					}
					if (type == 1) {
						String encrtypePwd = md5(password, iLiveMangerByMobile.getSalt());
						if (encrtypePwd.equals(iLiveMangerByMobile.getUserPwd())) {
							ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
							IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise , request);
							String loginToken = this.buildLoginToken();
							retInfo.setLoginToken(loginToken);
							iLiveMangerByMobile.setLoginToken(loginToken);
							iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
							resultJson.put("code", SUCCESS_STATUS);
							resultJson.put("message", "登录成功");
							resultJson.put("data", new JSONObject(retInfo));
							retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
							UserBean userBean = new UserBean();
							userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
							userBean.setLoginToken(loginToken);
							userBean.setNickname(iLiveMangerByMobile.getNailName());
							userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
							userBean.setUsername(iLiveMangerByMobile.getMobile());
							userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
							ILiveUtils.setAppUser(request, userBean);
							if(CacheManager.cacheExpired(cacheInfo)){
								cacheInfo.setExpired(true);
								CacheManager.clearOnly(CacheManager.mobile_token_ + "login_" + phoneNum);
							}
							System.out.println("cacheInfo=========="+cacheInfo);
							logger.info("登录时返回的sessionId->" + request.getSession().getId());
						} else {
							CacheManager.clearOnly(CacheManager.mobile_token_ + "login_" + phoneNum);
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "登录失败,密码不正确");
							resultJson.put("data", new JSONObject());
						}
					}
				}
			}
		} catch (Exception e) {
			cacheInfo.setExpired(true);
			CacheManager.clearOnly(CacheManager.mobile_token_ + "login_" + phoneNum);
			System.out.println("cacheInfo=========="+cacheInfo);
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "登录失败,请确认账户或密码是否正确");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 登录接口 1是账号密码登录 2是验证码登录
	 * 
	 * @param username
	 * @param password
	 * @param vpassword
	 * @param jpushId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/newLogin.jspx")
	public void newLogin(String phoneNum, Integer orginal, String password, String vpassword, Integer type,
			HttpServletRequest request, HttpServletResponse response, String terminalType) {
		JSONObject resultJson = new JSONObject();
		Cache cacheInfo = null;
		if (orginal != null) {
			TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
			if (tempLoginInfo == null) {
				TempLoginInfo tempLoginInfo2 = new TempLoginInfo();
				tempLoginInfo2.setGetGuideAddr(true);
				request.getSession().setAttribute("tempLoginInfo", tempLoginInfo2);
			} else {
				tempLoginInfo.setGetGuideAddr(true);
				request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
			}
		}
		try {
			if (phoneNum == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "手机号必须输入");
				resultJson.put("data", new JSONObject());
			} else if (type == 1 && password == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请输入密码");
				resultJson.put("data", new JSONObject());
			} else if (type == 2 && vpassword == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请输入验证码");
				resultJson.put("data", new JSONObject());
			} else {
				if (type == 2) {
					cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "login_" + phoneNum);
					System.out.println("cacheInfo======="+cacheInfo);
					String defaultVpassword = ConfigUtils.get("defaultVpassword");
					if (defaultVpassword.equals(vpassword)) {
					} else if (cacheInfo == null) {
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
					} else if (!vpassword.equals(cacheInfo.getValue())) {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "验证码不通过");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
					ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByPhoneNumber(phoneNum);
					if (iLiveMangerByMobile == null) {
						long registeredManagerId = iLiveManagerMng.registeredManager(phoneNum, password, 0, type,
								terminalType);
						iLiveMangerByMobile = iLiveManagerMng.getILiveManager(registeredManagerId);
						ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
						IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise, request);
						String loginToken = this.buildLoginToken();
						retInfo.setLoginToken(loginToken);
						iLiveMangerByMobile.setLoginToken(loginToken);
						iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
						//新注册用户自动开通试用功能
					    System.out.println("自动开通试用功能！！！！");
						UserBean userBean = new UserBean();
						Integer enterpriseId = liveRoomMng.initRoom(iLiveMangerByMobile);
						liveRoomMng.initMeet(iLiveMangerByMobile, enterpriseId, request);
						userBean.setEnterpriseId(enterpriseId);
						ILiveManager iLiveManagerRefresh = iLiveManagerMng.getILiveManager(iLiveMangerByMobile.getUserId());
						String code=EnterpriseUtil.getEnterpriseMsg(iLiveMangerByMobile.getEnterpriseId().intValue(),iLiveMangerByMobile.getCertStatus());
						userBean.setFunctionCode(code);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "登录成功");
						resultJson.put("data", new JSONObject(retInfo));
						retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
						userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
						userBean.setLoginToken(loginToken);
						userBean.setUsername(iLiveMangerByMobile.getMobile());
						userBean.setNickname(iLiveMangerByMobile.getNailName());
						userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
						userBean.setCertStatus(iLiveManagerRefresh.getCertStatus());
						userBean.setLevel(iLiveMangerByMobile.getLevel()==null?0:iLiveMangerByMobile.getLevel());
						ILiveUtils.setAppUser(request, userBean);
						if(CacheManager.cacheExpired(cacheInfo)){
							cacheInfo.setExpired(true);
							CacheManager.clearOnly(CacheManager.mobile_token_ + "login_" + phoneNum);
						}
						logger.info("登录时返回的sessionId->" + request.getSession().getId());
					} else {
						ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
						IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise , request);
						String loginToken = this.buildLoginToken();
						retInfo.setLoginToken(loginToken);
						iLiveMangerByMobile.setLoginToken(loginToken);
						iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "登录成功");
						resultJson.put("data", new JSONObject(retInfo));
						retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
						UserBean userBean = new UserBean();
						userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
						userBean.setLoginToken(loginToken);
						userBean.setUsername(iLiveMangerByMobile.getMobile());
						userBean.setNickname(iLiveMangerByMobile.getNailName());
						userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
						userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
						ILiveUtils.setAppUser(request, userBean);
						if(CacheManager.cacheExpired(cacheInfo)){
							cacheInfo.setExpired(true);
							CacheManager.clearOnly(CacheManager.mobile_token_ + "login_" + phoneNum);
						}
						logger.info("登录时返回的sessionId->" + request.getSession().getId());
					}
				} else {
					ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
					if (iLiveMangerByMobile == null) {
						// 开启注册登录
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "登录失败,请确认账户或密码是否正确");
						resultJson.put("data", new JSONObject());
					}
					if (type == 1) {
						String encrtypePwd = md5(password, iLiveMangerByMobile.getSalt());
						if (encrtypePwd.equals(iLiveMangerByMobile.getUserPwd())) {
							ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
							IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise , request);
							String loginToken = this.buildLoginToken();
							retInfo.setLoginToken(loginToken);
							iLiveMangerByMobile.setLoginToken(loginToken);
							iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
							resultJson.put("code", SUCCESS_STATUS);
							resultJson.put("message", "登录成功");
							resultJson.put("data", new JSONObject(retInfo));
							retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
							UserBean userBean = new UserBean();
							userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
							userBean.setLoginToken(loginToken);
							userBean.setNickname(iLiveMangerByMobile.getNailName());
							userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
							userBean.setUsername(iLiveMangerByMobile.getMobile());
							userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
							ILiveUtils.setAppUser(request, userBean);
							if(CacheManager.cacheExpired(cacheInfo)){
								cacheInfo.setExpired(true);
								CacheManager.clearOnly(CacheManager.mobile_token_ + "login_" + phoneNum);
							}
							logger.info("登录时返回的sessionId->" + request.getSession().getId());
						} else {
							cacheInfo.setExpired(true);
							CacheManager.clearOnly(CacheManager.mobile_token_ + "login_" + phoneNum);
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "登录失败,密码不正确");
							resultJson.put("data", new JSONObject());
						}
					}
				}
			}
		} catch (Exception e) {
			CacheManager.clearOnly(CacheManager.mobile_token_ + "login_" + phoneNum);
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "登录失败,请确认账户或密码是否正确");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * app天翼免密登录
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = "/appautoLogin.jspx")
	public void WebLoginbox(HttpServletRequest request,String clientId,String timeStamp,String accessToken,String version,String clientIp, HttpServletResponse response) throws Exception {
		System.out.println("app免密登录");
		JSONObject resultJson = new JSONObject();
		try {	
				Map<String, String> map1 = new HashMap<String, String>();
				UserBean userBean = new UserBean();
				map1.put("clientId", clientId);
				map1.put("timeStamp", timeStamp);
				map1.put("accessToken",accessToken);
				map1.put("version", version);
				map1.put("clientIp",clientIp);
				String infoUrl=ConfigUtils.get("free_info_url");
				String postJson1 = HttpUtils.sendPost(infoUrl, map1, "UTF-8");
				JSONObject jsonObject1 = new JSONObject(postJson1);
				String phoneNum=jsonObject1.getString("mobileName");
				Integer result1=jsonObject1.getInt("result");
				ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
				if(result1==0) {
					if (iLiveMangerByMobile == null) {
						long registeredManagerId = iLiveManagerMng.registeredManager(phoneNum, null, 0, 2,
								null);
						iLiveMangerByMobile = iLiveManagerMng.getILiveManager(registeredManagerId);
						ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
						IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise, request);
						String loginToken = this.buildLoginToken();
						retInfo.setLoginToken(loginToken);
						iLiveMangerByMobile.setLoginToken(loginToken);
						iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
						//新注册用户自动开通试用功能
					    System.out.println("自动开通试用功能！！！！");
						Integer enterpriseId = liveRoomMng.initRoom(iLiveMangerByMobile);
						liveRoomMng.initMeet(iLiveMangerByMobile, enterpriseId, request);
						userBean.setEnterpriseId(enterpriseId);
						ILiveManager iLiveManagerRefresh = iLiveManagerMng.getILiveManager(iLiveMangerByMobile.getUserId());
						String code=EnterpriseUtil.getEnterpriseMsg(iLiveMangerByMobile.getEnterpriseId().intValue(),iLiveMangerByMobile.getCertStatus());
						userBean.setFunctionCode(code);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "登录成功");
						resultJson.put("data", new JSONObject(retInfo));
						retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
						userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
						userBean.setLoginToken(loginToken);
						userBean.setUsername(iLiveMangerByMobile.getMobile());
						userBean.setNickname(iLiveMangerByMobile.getNailName());
						userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
						userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
						userBean.setLevel(iLiveMangerByMobile.getLevel()==null?0:iLiveMangerByMobile.getLevel());
						ILiveUtils.setAppUser(request, userBean);
						logger.info("登录时返回的sessionId->" + request.getSession().getId());
					    
					} else {
						ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
						IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise , request);
						String loginToken = this.buildLoginToken();
						retInfo.setLoginToken(loginToken);
						iLiveMangerByMobile.setLoginToken(loginToken);
						iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "登录成功");
						resultJson.put("data", new JSONObject(retInfo));
						retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
						userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
						userBean.setLoginToken(loginToken);
						userBean.setUsername(iLiveMangerByMobile.getMobile());
						userBean.setNickname(iLiveMangerByMobile.getNailName());
						userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
						userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
						ILiveUtils.setAppUser(request, userBean);
						logger.info("登录时返回的sessionId->" + request.getSession().getId());
					}
					
				}else {
					String  msg1=jsonObject1.getString("msg");
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", msg1);
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			
			
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "免密登录失败");
			Map<String, Object> map = new HashMap<>();
			map.put("state", 0);
			resultJson.put("data", new JSONObject(map));
		}
	
		ResponseUtils.renderJson(request, response, resultJson.toString());
		
		
	}
	/**
	 * 第三方登录
	 */
	@RequestMapping(value = "third/{org}/login.jspx")
	public void thirdSystemLogin(@PathVariable(value = "org") String org, ThirdLoginVo thirdLoginVo,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (org == null || thirdLoginVo == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "第三方登录类型不能为空");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			if (org.equals(ThirdOrgnization.WeiXin.getType())) {
				ILiveManager iLiveMangerByWxOpenId = iLiveManagerMng
						.getILiveMangerByWxUnionId(thirdLoginVo.getUnionid());
				if (iLiveMangerByWxOpenId == null) {
					/**
					 * 注册并返回至绑定手机页
					 */
					ILiveManager manager = iLiveManagerMng.registeredManagerByWxUnionId(thirdLoginVo);
					if (manager != null) {
						UserBean userBean = new UserBean();
						userBean.setUserId(String.valueOf(manager.getUserId()));
						userBean.setUsername(manager.getMobile());
						userBean.setNickname(manager.getNailName());
						userBean.setUserThumbImg(manager.getUserImg());
						userBean.setEnterpriseId(manager.getEnterPrise().getEnterpriseId());
						userBean.setCertStatus(manager.getCertStatus());
						userBean.setLevel(manager.getLevel()==null?0:manager.getLevel());
						ILiveUtils.setAppUser(request, userBean);
						IliveAppRetInfo retInfo = this.buildAppRet(manager, manager.getEnterPrise() , request);
						String mobile = manager.getMobile();
						if (StringUtils.isEmpty(mobile)) {
							retInfo.setBindMobile(0);
						} else {
							retInfo.setBindMobile(1);
						}
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "登录成功");
						resultJson.put("data", new JSONObject(retInfo));
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					} else {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "注册失败");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
				} else {
					String nailName = iLiveMangerByWxOpenId.getNailName();
					if (StringUtils.isEmpty(nailName)) {
						iLiveMangerByWxOpenId.setNailName(EmojiFilter.filterEmoji(thirdLoginVo.getName()));
					}
					String userImg = iLiveMangerByWxOpenId.getUserImg();
					if (StringUtils.isEmpty(userImg)) {
						iLiveMangerByWxOpenId.setUserImg(thirdLoginVo.getUser_img());
					}
					iLiveManagerMng.updateLiveManager(iLiveMangerByWxOpenId);
					UserBean userBean = new UserBean();
					userBean.setUserId(String.valueOf(iLiveMangerByWxOpenId.getUserId()));
					userBean.setUsername(iLiveMangerByWxOpenId.getMobile());
					userBean.setNickname(iLiveMangerByWxOpenId.getNailName());
					userBean.setUserThumbImg(iLiveMangerByWxOpenId.getUserImg());
					userBean.setEnterpriseId(iLiveMangerByWxOpenId.getEnterPrise().getEnterpriseId());
					userBean.setCertStatus(iLiveMangerByWxOpenId.getCertStatus());
					ILiveUtils.setAppUser(request, userBean);
					IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByWxOpenId,
							iLiveMangerByWxOpenId.getEnterPrise() , request);
					String mobile = iLiveMangerByWxOpenId.getMobile();
					if (StringUtils.isEmpty(mobile)) {
						retInfo.setBindMobile(0);
					} else {
						retInfo.setBindMobile(1);
					}
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "登录成功");
					resultJson.put("data", new JSONObject(retInfo));
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "登录失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
	}
	/**
	 * 第三方登录
	 */
	@RequestMapping(value = "third/{org}/newlogin.jspx")
	public void thirdSystemLogin1(@PathVariable(value = "org") String org, ThirdLoginVo thirdLoginVo,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (org == null || thirdLoginVo == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "第三方登录类型不能为空");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			if (org.equals(ThirdOrgnization.WeiXin.getType())) {
				ILiveManager iLiveMangerByWxOpenId = iLiveManagerMng
						.getILiveMangerByWxUnionId(thirdLoginVo.getUnionid());
				if (iLiveMangerByWxOpenId == null) {
					/**
					 * 注册并返回至绑定手机页
					 */
					ILiveManager manager = iLiveManagerMng.registeredManagerByWxUnionId(thirdLoginVo);
					if (manager != null) {
						//新注册用户自动开通试用功能
					    System.out.println("自动开通试用功能！！！！");
					    UserBean userBean = new UserBean();
						Integer enterpriseId = liveRoomMng.initRoom(manager);
						liveRoomMng.initMeet(manager, enterpriseId, request);
						userBean.setEnterpriseId(enterpriseId);
						ILiveManager iLiveManagerRefresh = iLiveManagerMng.getILiveManager(manager.getUserId());
						String code=EnterpriseUtil.getEnterpriseMsg(manager.getEnterpriseId().intValue(),manager.getCertStatus());
						userBean.setFunctionCode(code);
						userBean.setUserId(String.valueOf(manager.getUserId()));
						userBean.setUsername(manager.getMobile());
						userBean.setNickname(manager.getNailName());
						userBean.setUserThumbImg(manager.getUserImg());
						userBean.setEnterpriseId(manager.getEnterPrise().getEnterpriseId());
						userBean.setCertStatus(manager.getCertStatus());
						userBean.setLevel(manager.getLevel()==null?0:manager.getLevel());
						ILiveUtils.setAppUser(request, userBean);
						IliveAppRetInfo retInfo = this.buildAppRet(manager, manager.getEnterPrise() , request);
						String mobile = manager.getMobile();
						if (StringUtils.isEmpty(mobile)) {
							retInfo.setBindMobile(0);
						} else {
							retInfo.setBindMobile(1);
						}
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "登录成功");
						resultJson.put("data", new JSONObject(retInfo));
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					} else {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "注册失败");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
				} else {
					String nailName = iLiveMangerByWxOpenId.getNailName();
					if (StringUtils.isEmpty(nailName)) {
						iLiveMangerByWxOpenId.setNailName(EmojiFilter.filterEmoji(thirdLoginVo.getName()));
					}
					String userImg = iLiveMangerByWxOpenId.getUserImg();
					if (StringUtils.isEmpty(userImg)) {
						iLiveMangerByWxOpenId.setUserImg(thirdLoginVo.getUser_img());
					}
					iLiveManagerMng.updateLiveManager(iLiveMangerByWxOpenId);
					UserBean userBean = new UserBean();
					userBean.setUserId(String.valueOf(iLiveMangerByWxOpenId.getUserId()));
					userBean.setUsername(iLiveMangerByWxOpenId.getMobile());
					userBean.setNickname(iLiveMangerByWxOpenId.getNailName());
					userBean.setUserThumbImg(iLiveMangerByWxOpenId.getUserImg());
					userBean.setEnterpriseId(iLiveMangerByWxOpenId.getEnterPrise().getEnterpriseId());
					userBean.setCertStatus(iLiveMangerByWxOpenId.getCertStatus());
					ILiveUtils.setAppUser(request, userBean);
					IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByWxOpenId,
							iLiveMangerByWxOpenId.getEnterPrise() , request);
					String mobile = iLiveMangerByWxOpenId.getMobile();
					if (StringUtils.isEmpty(mobile)) {
						retInfo.setBindMobile(0);
					} else {
						retInfo.setBindMobile(1);
					}
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "登录成功");
					resultJson.put("data", new JSONObject(retInfo));
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "登录失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
	}
	/**
	 * 
	 */
	@RequestMapping(value = "checkMobile.jspx")
	public void jungeUserMobile(String phoneNum, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (StringUtils.isEmpty(phoneNum)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "手机号未输入");
			resultJson.put("data", new JSONObject());
		}
		try {
			ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
			if (iLiveMangerByMobile != null) {
				String userPwd = iLiveMangerByMobile.getUserPwd();
				if (StringUtils.isEmpty(userPwd)) {
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "该手机号需要设置密码");
					Map<String, Object> map = new HashMap<>();
					map.put("state", 1);
					resultJson.put("data", new JSONObject(map));
				} else {
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "该手机号可以直接登录");
					Map<String, Object> map = new HashMap<>();
					map.put("state", 2);
					resultJson.put("data", new JSONObject(map));
				}
			} else {
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "该手机号尚未注册");
				Map<String, Object> map = new HashMap<>();
				map.put("state", 0);
				resultJson.put("data", new JSONObject(map));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "内部异常");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 登出系统
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/logout.jspx")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			HttpSession session = request.getSession();
			session.invalidate();
		} catch (Exception e) {
		}
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "注销成功");
		resultJson.put("data", new JSONObject());
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getPhoneUrl.jspx")
	public void getPhoneUrl(String roomId,String fileId ,HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		JSONObject parasParamJsonObj = new JSONObject();
		String othereturn=" "+roomId+" "+fileId;
		String returnURL=ConfigUtils.get("free_return_url_phone")+othereturn;
		System.out.println(returnURL);
		parasParamJsonObj.put("timeStamp", System.currentTimeMillis());
		parasParamJsonObj.put("returnURL", returnURL);
		if (!StringUtil.isEmpty(LoginboxConfig.LOGIN_TYPE))
			parasParamJsonObj.put("loginType", LoginboxConfig.LOGIN_TYPE);
		if (!StringUtil.isEmpty(LoginboxConfig.QA_URL))
			parasParamJsonObj.put("qaUrl", LoginboxConfig.QA_URL);
		if (!StringUtil.isEmpty(LoginboxConfig.OTHER_LOGIN_URL))
			parasParamJsonObj.put("otherLoginUrl",
					LoginboxConfig.OTHER_LOGIN_URL);
		// TODO 更多可选参数，请咨询相关人员

		String paras = "";
		try {
			paras = XXTea.encrypt(FormatUtil.json2UrlParam(
					parasParamJsonObj.toString(), false, null),
					ConfigUtils.get("free_charset"), StringUtil.toHex(
							ConfigUtils.get("free_app_secret"), ConfigUtils.get("free_charset")));
		} catch (Exception e) {
		}
System.out.println(paras);
		String sign = "";
		try {
			sign = HMACSHA1.getSignature(
					ConfigUtils.get("free_app_id") + ConfigUtils.get("free_client_type_wap")
							+ ConfigUtils.get("free_format")
							+ ConfigUtils.get("free_version_wap") + paras,
							ConfigUtils.get("free_app_secret")).toUpperCase();
		} catch (Exception e) {
		}

		JSONObject reqJsonObj = new JSONObject();
		reqJsonObj.put("appId", ConfigUtils.get("free_app_id"));
		reqJsonObj.put("clientType", ConfigUtils.get("free_client_type_wap"));
		reqJsonObj.put("format", ConfigUtils.get("free_format"));
		reqJsonObj.put("version", ConfigUtils.get("free_version_wap"));
		reqJsonObj.put("paras", paras);
		reqJsonObj.put("sign", sign);

		StringBuffer autoLoginUrlStrBuffer = new StringBuffer();
		autoLoginUrlStrBuffer.append(ConfigUtils.get("free_auto_login_url"));
		autoLoginUrlStrBuffer.append("?");
		autoLoginUrlStrBuffer.append(FormatUtil.json2UrlParam(
				reqJsonObj.toString(), false, null));
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "获取成功");
		resultJson.put("url", autoLoginUrlStrBuffer.toString());
		resultJson.put("data", new JSONObject());
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getPcUrl.jspx")
	public void getPcUrl(String roomId,String fileId ,HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		JSONObject parasParamJsonObj = new JSONObject();
		String othereturn=" "+roomId+" "+fileId;
		String returnURL=ConfigUtils.get("free_return_url_pc")+othereturn;
		System.out.println(returnURL);
		parasParamJsonObj.put("timeStamp", System.currentTimeMillis());
		parasParamJsonObj.put("returnURL", returnURL);

		String paras = "";
		try {
			paras = XXTea.encrypt(FormatUtil.json2UrlParam(
					parasParamJsonObj.toString(), false, null),
					ConfigUtils.get("free_charset"), StringUtil.toHex(
							ConfigUtils.get("free_app_secret"), ConfigUtils.get("free_charset")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(paras+"");
//sign=HMAC-SHA1(appId+clientType+format+version+paras,appSecret).toUpperCase()
		String sign = "";
		try {
			sign = HMACSHA1.getSignature(
					ConfigUtils.get("free_app_id") + ConfigUtils.get("free_client_type_web")
							+ ConfigUtils.get("free_format")
							+ ConfigUtils.get("free_version_web") + paras,
							ConfigUtils.get("free_app_secret")).toUpperCase();
		} catch (Exception e) {
		}

		JSONObject reqJsonObj = new JSONObject();
		reqJsonObj.put("appId", ConfigUtils.get("free_app_id"));
		reqJsonObj.put("clientType", ConfigUtils.get("free_client_type_web"));
		reqJsonObj.put("format", ConfigUtils.get("free_format"));
		reqJsonObj.put("version", ConfigUtils.get("free_version_web"));
		reqJsonObj.put("paras", paras);
		reqJsonObj.put("sign", sign);

		StringBuffer unifyAccountLoginUrlStrBuffer = new StringBuffer();
		unifyAccountLoginUrlStrBuffer
				.append(ConfigUtils.get("free_unify_account_login_url"));
		unifyAccountLoginUrlStrBuffer.append("?");
		unifyAccountLoginUrlStrBuffer.append(FormatUtil.json2UrlParam(
				reqJsonObj.toString(), false, null));
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "获取成功");
		resultJson.put("url", unifyAccountLoginUrlStrBuffer.toString());
		resultJson.put("data", new JSONObject());
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 天翼免密登录回调
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = "/log_notify.jspx")
	public void WebLoginbox(HttpServletRequest request,String callback,String appId,String paras,String sign, HttpServletResponse response) throws Exception {
		System.out.println("免密登录回调");
		JSONObject resultJson = new JSONObject();
		try {
			Cache cacheInfo = null;
			String phoneNum= null;
			//获取回调返回的paras并对其进行解密
			 paras=XXTea.decrypt(paras,ConfigUtils.get("free_charset"), StringUtil.toHex(
					 ConfigUtils.get("free_app_secret"), ConfigUtils.get("free_charset")));;
			String[] rmsg=paras.split("&");
			String[] typs=callback.split(" "); //0 登陆类型（1 pc 2 phone） 1  roomid 2 filid 
			Integer roomId=Integer.parseInt(typs[1]);
			String code=rmsg[2].substring(5);
			String format="json";
			String grantType="authorization_login";
			//首先通过code获取accessToken
			Map<String, String> map = new HashMap<String, String>();
			map.put("appId",ConfigUtils.get("free_app_id") );
			map.put("grantType",grantType);
			map.put("code",code);
			String encryValue=appId+code+format+grantType;
			String sign1=HMACSHA1.getSignature(encryValue, ConfigUtils.get("free_app_secret")).toUpperCase();
			map.put("format",format);
			map.put("sign", sign1);
			String accessUrl=ConfigUtils.get("free_access_url");
			String postJson = HttpUtils.sendPost(accessUrl, map, "UTF-8");
			JSONObject jsonObject = new JSONObject(postJson);
			int  result=jsonObject.getInt("result");
			if(result==10000) {
				String accessToken=jsonObject.getString("accessToken");
				Map<String, String> map1 = new HashMap<String, String>();
				UserBean userBean = new UserBean();
				map1.put("clientId", ConfigUtils.get("free_app_id"));
				map1.put("timeStamp", rmsg[1].substring(9));
				map1.put("accessToken",accessToken);
				map1.put("version", ConfigUtils.get("free_version_web"));
				map1.put("clientIp",InetAddress.getLocalHost().getHostAddress());
				String infoUrl=ConfigUtils.get("free_info_url");
				String postJson1 = HttpUtils.sendPost(infoUrl, map1, "UTF-8");
				JSONObject jsonObject1 = new JSONObject(postJson1);
				phoneNum=jsonObject1.getString("mobileName");
				Integer result1=jsonObject1.getInt("result");
				ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
				if(result1==0) {
					if (iLiveMangerByMobile == null) {
						long registeredManagerId = iLiveManagerMng.registeredManager(phoneNum, null, 0, 2,
								null);
						iLiveMangerByMobile = iLiveManagerMng.getILiveManager(registeredManagerId);
						ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
						IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise, request);
						String loginToken = this.buildLoginToken();
						retInfo.setLoginToken(loginToken);
						iLiveMangerByMobile.setLoginToken(loginToken);
						iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "登录成功");
						resultJson.put("data", new JSONObject(retInfo));
						retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
						userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
						userBean.setLoginToken(loginToken);
						userBean.setUsername(iLiveMangerByMobile.getMobile());
						userBean.setNickname(iLiveMangerByMobile.getNailName());
						userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
						userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
						userBean.setLevel(iLiveMangerByMobile.getLevel()==null?0:iLiveMangerByMobile.getLevel());
						ILiveUtils.setAppUser(request, userBean);
						logger.info("登录时返回的sessionId->" + request.getSession().getId());
					    
					} else {
						ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
						IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise , request);
						String loginToken = this.buildLoginToken();
						retInfo.setLoginToken(loginToken);
						iLiveMangerByMobile.setLoginToken(loginToken);
						iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "登录成功");
						resultJson.put("data", new JSONObject(retInfo));
						retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
						userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
						userBean.setLoginToken(loginToken);
						userBean.setUsername(iLiveMangerByMobile.getMobile());
						userBean.setNickname(iLiveMangerByMobile.getNailName());
						userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
						userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
						ILiveUtils.setAppUser(request, userBean);
						CacheManager.cacheExpired(cacheInfo);
						logger.info("登录时返回的sessionId->" + request.getSession().getId());
					}
					
						if(Integer.parseInt(typs[2])==0) {
							
							if(Integer.parseInt(typs[0])==1) {
								
								String url=ConfigUtils.get("free_login_pc_play")+"?userId="+String.valueOf(iLiveMangerByMobile.getUserId())+"&roomId="+typs[1];
								String text="<script>window.top.location.href='"+url+"'</script>";
								ResponseUtils.renderHtml(response, text);
								return;
							}else {
								String url=ConfigUtils.get("free_login_phone_live")+"?userId="+String.valueOf(iLiveMangerByMobile.getUserId())+"&roomId="+typs[1];
								String text="<script>window.top.location.href='"+url+"'</script>";
								ResponseUtils.renderHtml(response, text);
								return;
							}
						

					}else {
						  // System.out.println("进入回看页面，回看id："+typs[2]);
							if(Integer.parseInt(typs[0])==1) {
								String url=ConfigUtils.get("free_login_review")+"/pc/review.html?userId="+String.valueOf(iLiveMangerByMobile.getUserId())+"&roomId="+typs[1]+"&fileId="+typs[2];
								String text="<script>window.top.location.href='"+url+"'</script>";
								ResponseUtils.renderHtml(response, text);
								return;
							}else {
								String url=ConfigUtils.get("free_login_review")+"/phone/review.html?userId="+String.valueOf(iLiveMangerByMobile.getUserId())+"&roomId="+typs[1]+"&fileId="+typs[2];
								System.out.println("手机会看页地址："+url);
								String text="<script>window.top.location.href='"+url+"'</script>";
								ResponseUtils.renderHtml(response, text);
								return;
							}
						}
						
					


				}else {
					String  msg1=jsonObject1.getString("msg");
					//System.out.println("错误信息1："+msg1);
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", msg1);
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			}else {
				String  msg=jsonObject.getString("msg");
				//System.out.println("错误信息2："+msg);
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", msg);
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "免密登录失败");
			Map<String, Object> map = new HashMap<>();
			map.put("state", 0);
			resultJson.put("data", new JSONObject(map));
		}
	
	
		
		
	}
	
	/**
	 * 登录接口 1是账号密码登录 2是验证码登录
	 * 
	 * @param username
	 * @param password
	 * @param vpassword
	 * @param jpushId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/tokenlogin.jspx")
	public void loginByToken(Long userId, String loginToken, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			if (loginToken == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "loginToken不能为空");
				resultJson.put("data", new JSONObject());
			} else if (userId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "userId不能为空");
				resultJson.put("data", new JSONObject());
			} else {
				ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByLoginToken(userId, loginToken);
				if (iLiveMangerByMobile == null) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户不存在");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
				ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
				IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise , request);
				String newloginToken = this.buildLoginToken();
				retInfo.setLoginToken(newloginToken);
				iLiveMangerByMobile.setLoginToken(newloginToken);
				iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "登录成功");
				resultJson.put("data", new JSONObject(retInfo));
				retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
				UserBean userBean = new UserBean();
				userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
				userBean.setLoginToken(loginToken);
				userBean.setUsername(iLiveMangerByMobile.getMobile());
				userBean.setNickname(iLiveMangerByMobile.getNailName());
				userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
				userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
				// request.getSession().setAttribute("appUser", userBean);
				userBean.setLevel(iLiveMangerByMobile.getLevel()==null?0:iLiveMangerByMobile.getLevel());
				ILiveUtils.setAppUser(request, userBean);
				logger.info("自动登录时返回的sessionId->" + request.getSession().getId());
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "登录失败,请确认loginToken是否正确");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	@RequestMapping(value = "/getGuideUrl.jspx")
	public void guideUrl(Integer roomId, Integer orginal, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (roomId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "");
			resultJson.put("data", new JSONObject());
		} else {
			ILiveLiveRoom iliveRoom = liveRoomMng.getIliveRoom(roomId);
			if (iliveRoom != null) {
				String guideAddr = iliveRoom.getLiveEvent().getGuideAddr();
				guideAddr = guideAddr == null ? "" : guideAddr;
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "");
				resultJson.put("roomTitle", iliveRoom.getLiveEvent().getLiveTitle());
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("imgUrl", guideAddr);
				Integer openGuideSwitch = iliveRoom.getLiveEvent().getOpenGuideSwitch();
				if (openGuideSwitch == null) {
					openGuideSwitch = 0;
				}
				jsonObject.put("openGuideSwitch", openGuideSwitch);
				resultJson.put("data", jsonObject);
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "");
				resultJson.put("data", new JSONObject());
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	@RequestMapping(value = "/enterGuide.jspx")
	public void enterGuideUrl(Integer orginal, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (orginal != null) {
			TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
			if (tempLoginInfo == null) {
				TempLoginInfo tempLoginInfo2 = new TempLoginInfo();
				tempLoginInfo2.setGetGuideAddr(true);
				request.getSession().setAttribute("tempLoginInfo", tempLoginInfo2);
			} else {
				tempLoginInfo.setGetGuideAddr(true);
				request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
			}
		}
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "");
		resultJson.put("data", new JSONObject());
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 注册接口 登录接口 1是账号密码登录 2是验证码登录
	 * 
	 * @param username
	 * @param password
	 * @param vpassword
	 * @param jpushId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/reg.jspx")
	public void reg(String phoneNum, String password, Integer orginal, String vpassword, Integer type,
			String terminalType, HttpServletRequest request, HttpServletResponse response) {
		if (orginal != null) {
			TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
			if (tempLoginInfo == null) {
				TempLoginInfo tempLoginInfo2 = new TempLoginInfo();
				tempLoginInfo2.setGetGuideAddr(true);
				request.getSession().setAttribute("tempLoginInfo", tempLoginInfo2);
			} else {
				tempLoginInfo.setGetGuideAddr(true);
				request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
			}
		}
		JSONObject resultJson = new JSONObject();
		if (phoneNum == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "手机号必须输入");
			resultJson.put("data", new JSONObject());
		} else if (type == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "类型不存在");
			resultJson.put("data", new JSONObject());
		} else if (type == 1 && password == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "未填写密码");
			resultJson.put("data", new JSONObject());
		} else if (type == 2 && vpassword == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "未填写验证码");
			resultJson.put("data", new JSONObject());
		} else {
			
			Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "reg_" + phoneNum);
			String defaultVpassword = ConfigUtils.get("defaultVpassword");
			if (defaultVpassword.equals(vpassword)) {
			} else if (cacheInfo == null) {
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
			} else if (!vpassword.equals(cacheInfo.getValue())) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码校验不通过");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			
			ILiveManager iLiveManger = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
			if (iLiveManger != null) {
				//新加需求 若有新的密码过来，需将原先的密码更换为最新的
				String salt = String.valueOf(new Random().nextInt(2000));
				if (password != null) {
					String md5Pwd = SystemMd5Util.md5(password, salt);
					iLiveManger.setUserPwd(md5Pwd);
					iLiveManger.setSalt(salt);
					iLiveManagerMng.updateLiveManager(iLiveManger);
				}
				ILiveEnterprise enterPrise = iLiveManger.getEnterPrise();
				IliveAppRetInfo retInfo = this.buildAppRet(iLiveManger, enterPrise , request);
				String loginToken = this.buildLoginToken();
				retInfo.setLoginToken(loginToken);
				iLiveManger.setLoginToken(loginToken);
				//iLiveManagerMng.updateLiveManager(iLiveManger);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "登录成功");
				resultJson.put("data", new JSONObject(retInfo));
				retInfo.setJpushId(iLiveManger.getJpushId());
				UserBean userBean = new UserBean();
				userBean.setUserId(String.valueOf(iLiveManger.getUserId()));
				userBean.setLoginToken(loginToken);
				userBean.setNickname(iLiveManger.getNailName());
				userBean.setUserThumbImg(iLiveManger.getUserImg());
				userBean.setUsername(iLiveManger.getMobile());
				if(CacheManager.cacheExpired(cacheInfo)){
					CacheManager.clearOnly(CacheManager.mobile_token_ + "reg_" + phoneNum);
				}
				userBean.setCertStatus(iLiveManger.getCertStatus());
				// request.getSession().setAttribute("appUser", userBean);
				userBean.setLevel(iLiveManger.getLevel()==null?0:iLiveManger.getLevel());
				ILiveUtils.setAppUser(request, userBean);
				logger.info("reg登录时返回的sessionId->" + request.getSession().getId());
			} else {
				// 0标识手机号注册
				try {
					//
					long registeredManagerId = iLiveManagerMng.registeredManager(phoneNum, password, 0, type,
							terminalType);
					ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveManager(registeredManagerId);
					ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
					IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise , request);
					String loginToken = this.buildLoginToken();
					retInfo.setLoginToken(loginToken);
					iLiveMangerByMobile.setLoginToken(loginToken);
					iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "登录成功");
					resultJson.put("data", new JSONObject(retInfo));
					retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
					UserBean userBean = new UserBean();
					userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
					userBean.setLoginToken(loginToken);
					userBean.setUsername(iLiveMangerByMobile.getMobile());
					userBean.setNickname(iLiveMangerByMobile.getNailName());
					userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
					userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
					// request.getSession().setAttribute("appUser", userBean);
					ILiveUtils.setAppUser(request, userBean);
					if(CacheManager.cacheExpired(cacheInfo)){
						CacheManager.clearOnly(CacheManager.mobile_token_ + "reg_" + phoneNum);
					}
					logger.info("reg登录时返回的sessionId->" + request.getSession().getId());
				} catch (Exception e) {
					CacheManager.clearOnly(CacheManager.mobile_token_ + "reg_" + phoneNum);
					e.printStackTrace();
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "注册时发生异常");
					resultJson.put("data", new JSONObject());
					// CacheManager.cacheExpired(cacheInfo);
				}
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 用户信息修改
	 * 
	 * @param userInfo
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/useinfo/update.jspx")
	public void updateUserInfo(AppUserInfo userInfo, HttpServletRequest request, HttpServletResponse response) {
		Long userId = userInfo.getUserId();
		JSONObject resultJson = new JSONObject();
		ILiveManager iLiveMangerByLoginToken = iLiveManagerMng.getILiveMangerByLoginToken(userId,
				userInfo.getLoginToken());
		if (iLiveMangerByLoginToken == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "请求用户不合法");
			resultJson.put("data", new JSONObject());
		} else {
			// iLiveMangerByLoginToken.setNailName(userInfo.getNailName());
			if (userInfo.getNailName() != null && !userInfo.getNailName().equals("")) {
				iLiveMangerByLoginToken.setNailName(userInfo.getNailName());
			}
			if (userInfo.getUserImg() != null && !userInfo.getUserImg().equals("")) {
				iLiveMangerByLoginToken.setUserImg(userInfo.getUserImg());
			}
			Set<String> sensitiveWord = sentitivewordFilterMng.getSensitiveWord(userInfo.getNailName());
			Set<String> sensitiveWord2 = new HashSet<>();
			if (userInfo.getNailName()!=null&&!userInfo.getNailName().equals("")) {
				sensitiveWord2 = sentitivewordFilterMng.getSensitiveWord(userInfo.getNailName());
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
			iLiveManagerMng.updateLiveManager(iLiveMangerByLoginToken);
			ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveManager(iLiveMangerByLoginToken.getUserId());
			ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
			IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise , request);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "登录成功");
			resultJson.put("data", new JSONObject(retInfo));
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());

	}

	/**
	 * 获取报名
	 */
	@RequestMapping(value = "/diyform")
	public void getDiyForm(Integer roomId, HttpServletRequest request, HttpServletResponse response) {
		ILiveLiveRoom iliveRoom = liveRoomMng.getIliveRoom(roomId);
		BBSDiyform diyform = bbsDiyformMng.findById(iliveRoom.getLiveEvent().getFormId());
		int diyformId = diyform.getDiyformId();
		List<BBSDiymodel> listByDiyformId = bbsDiymodelMng.getListByDiyformId(diyformId);
		AppDiyFormVo formVo = this.convert2AppDiyFormVo(iliveRoom, diyform, listByDiyformId);
		Gson gson = new Gson();
		String json = gson.toJson(formVo);
		ResponseUtils.renderJson(request, response, json);
	}

	private AppDiyFormVo convert2AppDiyFormVo(ILiveLiveRoom iliveRoom, BBSDiyform diyform,
			List<BBSDiymodel> listByDiyformId) {
		AppDiyFormVo formVo = new AppDiyFormVo();
		DiyFormVo diyformVo = new DiyFormVo();
		diyformVo.setFormDesc(iliveRoom.getLiveEvent().getLiveTitle());
		diyformVo.setFormName(iliveRoom.getLiveEvent().getLiveTitle());
		diyformVo.setFormId(diyform.getDiyformId());
		formVo.setDiyformVo(diyformVo);
		List<DiyFormModelVo> modelVoList = new ArrayList<>();
		DiyFormModelVo modelVo = null;
		for (BBSDiymodel diyformModel : listByDiyformId) {
			modelVo = new DiyFormModelVo();
			modelVo.setIsNeed(diyformModel.getNeedAnswer());
			modelVo.setNeedMsgValid(diyformModel.getNeedMsgValid() == null ? 0 : diyformModel.getNeedMsgValid());
			modelVo.setQuestionName(diyformModel.getDiymodelTitle());
			modelVo.setQuestionType((int) diyformModel.getDiymodelType());
			String defValue = diyformModel.getOptValue();
			defValue = defValue == null ? "" : defValue;
			List<String> defValueList = new ArrayList<>();
			if (defValue.indexOf("@;@") > -1) {
				String[] splitOpts = defValue.split("@;@");
				for (String opt : splitOpts) {
					defValueList.add(opt);
				}
			} else {
				defValueList.add(defValue);
			}
			modelVo.setOptionName(defValueList);
			modelVo.setModelId(diyformModel.getDiymodelId());
			modelVo.setQuestionOrder(diyformModel.getDiyOrder());
			modelVoList.add(modelVo);
		}
		formVo.setModelList(modelVoList);
		return formVo;
	}

	/**
	 * 提交表单数据
	 * 
	 * @param diyFormData
	 * @param userId
	 * @param orginal
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "postformdata")
	public void postDiyformData(String diyFormData, String userId, Integer orginal, HttpServletRequest request,
			HttpServletResponse response) {
		String formId = request.getParameter("formId");
		JSONObject json = new JSONObject();
		UserBean userBean = ILiveUtils.getAppUser(request);
		if (userBean != null) {
			userId = userBean.getUserId();
		}
		if(userId == null || userId.equals("null")){
			userId = "-1";
		}
		//报名不再校验用户了
		//if (userId == null) {
		if (false) {
			json.put("code", "0");
			json.put("message", "用户Id不存在");
			json.put("data", new JSONObject());
		} else {
			try {
				/*报名不再校验用户了
				boolean checkUserSignUp = dataMng.checkUserSignUp(userId, Integer.parseInt(formId));
				if (checkUserSignUp) {
					json.put("code", "1");
					json.put("message", "提交成功");
					json.put("data", new JSONObject());
				}
				*/
				Gson gson = new Gson();
				List<AppDiyformReceiver> receiversList = gson.fromJson(
						new String(diyFormData.getBytes("ISO-8859-1"), "UTF-8"),
						new TypeToken<List<AppDiyformReceiver>>() {
						}.getType());
				boolean msgValid = true;
				String msgText = "";
				if (receiversList != null) {
					List<BBSDiyformData> diyformDatas = new ArrayList<>();
					BBSDiyformData formData = null;
					BBSDiyform diyform = bbsDiyformMng.findById(Integer.parseInt(formId));
					for (AppDiyformReceiver receiver : receiversList) {
						Integer needMsgValid = receiver.getNeedMsgValid();
						if (needMsgValid != null && needMsgValid.intValue() == 1) {
							String dataValue = receiver.getDataValue();
							String subValue = receiver.getSubValue();
							Cache cacheInfo = CacheManager
									.getCacheInfo(CacheManager.mobile_token_ + "form_" + dataValue);
							if (cacheInfo == null) {
								msgText = "请先发送验证码";
								msgValid = false;
								break;
							} else if (cacheInfo.isExpired()) {
								msgText = "请重新发送送验证码";
								msgValid = false;
								break;
							} else if (!cacheInfo.getValue().equals(subValue)) {
								msgValid = false;
								msgText = "验证码校验不合法";
								break;
							}
							CacheManager.clearOnly(CacheManager.mobile_token_ + "form_" + dataValue);
						}
						formData = new BBSDiyformData();
						formData.setCreateTime(new Timestamp(System.currentTimeMillis()));
						formData.setDataOrder(receiver.getDataOrder());
						formData.setDataTitle(receiver.getDataTitle());
						formData.setDataValue(receiver.getDataValue());
						if (userBean != null) {
							formData.setManagerId(Long.parseLong(userBean.getUserId()));
						} else {
							formData.setManagerId(Long.parseLong(userId));
						}
						formData.setModelId(receiver.getDataModelId());
						formData.setBbsDiyform(diyform);
						diyformDatas.add(formData);
					}
					if (!msgValid) {
						json.put("code", "0");
						json.put("message", msgText);
						json.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, json.toString());
						return;
					}
					dataMng.saveBBSDiyfromData(diyformDatas);
					
					//在session记录此用户提交过表单了
					java.util.HashSet<String> submitForm = (java.util.HashSet)request.getSession().getAttribute("submitForm");
					if(submitForm == null){
						submitForm = new java.util.HashSet();
					}
					submitForm.add(formId);
					request.getSession().setAttribute("submitForm", submitForm);
					
					
					if (orginal != null) {
						TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession()
								.getAttribute("tempLoginInfo");
						if (tempLoginInfo == null) {
							TempLoginInfo tempLoginInfo2 = new TempLoginInfo();
							tempLoginInfo2.setGetGuideAddr(true);
							request.getSession().setAttribute("tempLoginInfo", tempLoginInfo2);
						} else {
							tempLoginInfo.setGetGuideAddr(true);
							request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
						}
					}
					
					json.put("code", "1");
					json.put("message", "提交成功");
					json.put("data", new JSONObject());
				}
			} catch (Exception e) {
				json.put("code", "0");
				json.put("message", "提交失败");
				json.put("data", new JSONObject());
				e.printStackTrace();
			}
		}
		ResponseUtils.renderJson(request, response, json.toString());
	}

	// /**
	// * 直播间密码校验
	// *
	// * @param roomId
	// * @param roomPassword
	// * @param request
	// * @param response
	// */
	// @RequestMapping(value = "checkRoomPassword.jspx")
	// public void checkRoomPassword(Integer roomId, Integer orginal, String
	// roomPassword, HttpServletRequest request,
	// HttpServletResponse response) {
	// ILiveLiveRoom room = liveRoomMng.getIliveRoom(roomId);
	// JSONObject resultJson = new JSONObject();
	// if (orginal != null) {
	// TempLoginInfo tempLoginInfo = (TempLoginInfo)
	// request.getSession().getAttribute("tempLoginInfo");
	// if (tempLoginInfo == null) {
	// TempLoginInfo tempLoginInfo2 = new TempLoginInfo();
	// tempLoginInfo2.setGetGuideAddr(true);
	// request.getSession().setAttribute("tempLoginInfo", tempLoginInfo2);
	// } else {
	// tempLoginInfo.setGetGuideAddr(true);
	// request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
	// }
	// }
	// if (room == null) {
	// resultJson.put("code", ERROR_STATUS);
	// resultJson.put("message", "直播间不存在");
	// resultJson.put("data", "");
	// } else if (roomPassword == null) {
	// resultJson.put("code", ERROR_STATUS);
	// resultJson.put("message", "没有输入房间密码");
	// resultJson.put("data", "");
	// } else {
	// String viewPassword = room.getLiveEvent().getViewPassword();
	// if (roomPassword.equals(viewPassword)) {
	// UserBean userBean = (UserBean)
	// request.getSession().getAttribute("appUser");
	// if (userBean != null) {
	// userBean.setPasswdCheckResult(true);
	// resultJson.put("code", SUCCESS_STATUS);
	// resultJson.put("message", "密码输入正确");
	// resultJson.put("data", "");
	// }
	// } else {
	// resultJson.put("code", ERROR_STATUS);
	// resultJson.put("message", "密码不正确");
	// resultJson.put("data", "");
	// }
	//
	// }
	// ResponseUtils.renderJson(request, response, resultJson.toString());
	// }

	/**
	 * 构建成appRet 企业对象
	 * 
	 * @param iLiveMangerByMobile
	 * @param enterPrise
	 * @return
	 */
	private IliveAppRetInfo buildAppRet(ILiveManager iLiveMangerByMobile, ILiveEnterprise enterPrise , HttpServletRequest request ) {
		IliveAppRetInfo retInfo = new IliveAppRetInfo();
		retInfo.setUserId(iLiveMangerByMobile.getUserId());
		retInfo.setMobile(iLiveMangerByMobile.getMobile());
		retInfo.setNailName(iLiveMangerByMobile.getNailName());
		retInfo.setJpushId(StringPatternUtil.convertEmpty(iLiveMangerByMobile.getJpushId()));
		retInfo.setLoginToken(iLiveMangerByMobile.getLoginToken());
		retInfo.setUserName(StringPatternUtil.convertEmpty(iLiveMangerByMobile.getUserName()));
		retInfo.setCertStatus(iLiveMangerByMobile.getCertStatus() == null ? 0 : iLiveMangerByMobile.getCertStatus());
		retInfo.setPhoto(iLiveMangerByMobile.getUserImg() == null ? "" : iLiveMangerByMobile.getUserImg());
		String ipAddress = request.getHeader("x-forwarded-for");
	     
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
		ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknow".equalsIgnoreCase(ipAddress)) {
		ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknow".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteHost();
		}
		if(ipAddress.indexOf(":")>0){
			retInfo.setIsIpV6(1);
		}else{
			retInfo.setIsIpV6(0);
		}
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
	 * 构建登录token
	 * 
	 * @return
	 */
	private String buildLoginToken() {
		UUID uuid = UUID.randomUUID();
		String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
		return token;
	}

	/**
	 * 加密
	 * 
	 * @param salt
	 * @param password
	 * @return
	 */
	public static String md5(String pass, String saltSource) {
		Object salt = new Md5Hash(saltSource);
		int hashIterations = 1024;
		Object result = new SimpleHash("MD5", pass, salt, hashIterations);
		String password = result.toString();
		return password;
	}

	// /**
	// * 验证码验证
	// */
	// @RequestMapping(value = "/msg.jspx")
	// public String validSendMsg(String phoneNum, String code, String msg,
	// HttpServletRequest request,
	// HttpServletResponse response) {
	// Map<String, Object> sendParam = new HashMap<>();
	// String biztype_mode_valid = ConfigUtils.get("biztype_mode_valid");
	// sendParam.put("biztype", Integer.parseInt(biztype_mode_valid));
	// sendParam.put("params", "[{\"Name\":\"{vcode}\",\"Val\":\"" + code +
	// "\"}]");
	// if (msg != null) {
	// sendParam.put("msg", msg);
	// }
	// try {
	// Map<String, Object> sendMsg = ILiveSmgServerCenter.sendMsg(phoneNum,
	// sendParam);
	// JSONObject json = new JSONObject(sendMsg);
	// ResponseUtils.renderJson(request, response, json.toString());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	@RequestMapping(value = "/sendmsg.jspx")
	public void validSendMsg(String phoneNum, String type, HttpServletRequest request, HttpServletResponse response) {
		Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + type + "_" + phoneNum);
		Map<String, Object> map = new HashMap<>();
		if (cacheInfo == null || cacheInfo.isExpired() || cacheInfo.canRePub()) {
			String code = this.generatorCode();
			System.out.println("code======"+code);
			if (type != null && type.equals("form")) {
				CacheManager.putCacheInfo(CacheManager.mobile_token_ + type + "_" + phoneNum, code, 5 * 60 * 1000);
			} else {
				CacheManager.putCacheInfo(CacheManager.mobile_token_ + type + "_" + phoneNum, code, 10 * 60 * 1000);
			}
			Map<String, Object> sendParam = new HashMap<>();
			List<String> codelist=new ArrayList<String>();
			codelist.add(code);
			sendParam.put("biztype", 248612);
			sendParam.put("params", codelist);
			List<String> phonelist=new ArrayList<String>();
			phonelist.add(phoneNum);
			try {
				Map<String, Object> sendMsg = ILiveSmgServerCenter.sendMsgNew(phonelist, sendParam);
				JSONObject json = new JSONObject(sendMsg);
				System.out.println("json========="+json);
				if (json.getInt("code") == 0) {
					// if (true) {
					map.put("code", 1);
					map.put("message", "验证码已发送,请注意查收!");
					map.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
				}
			} catch (Exception e) {
				map.put("code", 0);
				map.put("message", "发送异常，请稍后再试!");
				map.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
				e.printStackTrace();
			}
		} else {
			map.put("code", 0);
			map.put("message", "验证码发送过于频繁，请稍后再试!");
			map.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
		}
	}
	@RequestMapping(value = "/sendmsg1.jspx")
	public void validSendMsg1(String phoneNum, String type, HttpServletRequest request, HttpServletResponse response) {
		Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + type + "_" + phoneNum);
		Map<String, Object> map = new HashMap<>();
		if (cacheInfo == null || cacheInfo.isExpired() || cacheInfo.canRePub()) {
			String code = this.generatorCode();
			System.out.println("code======"+code);
			if (type != null && type.equals("form")) {
				CacheManager.putCacheInfo(CacheManager.mobile_token_ + type + "_" + phoneNum, code, 5 * 60 * 1000);
			} else {
				CacheManager.putCacheInfo(CacheManager.mobile_token_ + type + "_" + phoneNum, code, 10 * 60 * 1000);
			}
			Map<String, Object> sendParam = new HashMap<>();
			List<String> codelist=new ArrayList<String>();
			codelist.add(code);
			sendParam.put("biztype", 248612);
			sendParam.put("params", codelist);
			List<String> phonelist=new ArrayList<String>();
			phonelist.add(phoneNum);
			try {
				Map<String, Object> sendMsg = ILiveSmgServerCenter.sendMsgNew(phonelist, sendParam);
				JSONObject json = new JSONObject(sendMsg);
				System.out.println("json========="+json);
				if (json.getInt("code") == 0) {
					// if (true) {
					map.put("code", 1);
					map.put("message", "验证码已发送,请注意查收!");
					map.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
				}else{
					map.put("code", 0);
					map.put("message", "验证码发送失败!"+json.getString("msg"));
					map.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
				}
			} catch (Exception e) {
				map.put("code", 0);
				map.put("message", "发送异常，请稍后再试!");
				map.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
				e.printStackTrace();
			}
		} else {
			map.put("code", 0);
			map.put("message", "验证码发送过于频繁，请稍后再试!");
			map.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
		}
	}
	/**
	 * 个人密码修改
	 */
	@RequestMapping(value = "/initpwd.jspx")
	public void updateUserPassword(String password, String phoneNum, String vpassword, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (StringUtils.isEmpty(phoneNum) || StringUtils.isEmpty(password) || StringUtils.isEmpty(vpassword)) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "参数输入不合法");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "updatepwd_" + phoneNum);
		if (cacheInfo == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "验证码不合法");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		if (cacheInfo.isExpired()) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "验证码过期");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		ILiveManager iLiveManager = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
		if (iLiveManager == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户不存在");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			String salt = String.valueOf(new Random().nextInt(2000));
			String md5Pwd = SystemMd5Util.md5(password, salt);
			iLiveManager.setSalt(salt);
			iLiveManager.setUserPwd(md5Pwd);
			iLiveManagerMng.updateLiveManager(iLiveManager);
			IliveAppRetInfo retInfo = this.buildAppRet(iLiveManager, iLiveManager.getEnterPrise() , request);
			cacheInfo.setExpired(true);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "密码修改成功");
			resultJson.put("data", new JSONObject(retInfo));
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作异常");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
		return;
	}

	/**
	 * 企业关注接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "enterprise/concern.jspx")
	public void concernEnterprise(Integer enterpriseId, Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		try {
			boolean ret = enterpriseFansMng.addEnterpriseConcern(enterpriseId, userId);
			if (ret) {
				map.put("code", SUCCESS_STATUS);
				map.put("message", "关注企业成功");
				map.put("data", new JSONObject());
			} else {
				map.put("code", ERROR_STATUS);
				map.put("message", "关注企业失败");
				map.put("data", new JSONObject());
			}
		} catch (Exception e) {
			map.put("code", ERROR_STATUS);
			map.put("message", "关注企业失败");
			map.put("data", new JSONObject());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
	}

	private String generatorCode() {
		String str[] = new String[] { "5", "1", "2", "4", "7", "3", "6", "0", "9", "8" };
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			sb.append(str[random.nextInt(9)]);
		}
		return sb.toString();
	}
	
	@RequestMapping(value = "/getIdentityCode.jspx")
	public void getIdentityCode(String phoneNum, String type,String identityCode, HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + type + "_" + phoneNum);
			if (cacheInfo == null|| cacheInfo.isExpired()) {
				result.put("code", "1");
				result.put("msg", "验证码已过期");
			}else if(!identityCode.equals(cacheInfo.getValue())){
				result.put("code", "1");
				result.put("msg", "验证码错误");
			}else {
				cacheInfo.setExpired(true);
				result.put("code", "0");
				result.put("msg", "验证成功");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "1");
			result.put("msg", "验证出错");
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	
	/**
	 * 获取用户是否登录
	 */
	@RequestMapping(value = "/checkIfLogin.jspx")
     public void checkLogin(HttpServletRequest request, HttpServletResponse response){
    	 JSONObject result = new JSONObject();
    	 UserBean userBean = ILiveUtils.getUser(request);
    	 JSONArray  array= new JSONArray();
    	 if(userBean==null){
    		 String formal_play_address = ConfigUtils.get("mpSystemAddr")+"/admin/login.do";
    		 result.put("loginUrl",formal_play_address);
    		 result.put("code", "0");
			 result.put("msg", "用户未登录"); 
			 result.put("data", array);
    	 }else{
    		 result.put("userId",userBean.getUserId());
    		 result.put("username", userBean.getUsername());
    		 result.put("level", userBean.getLevel());
    		 result.put("nickname", userBean.getNickname());
    		 result.put("enterpriseId", userBean.getEnterpriseId());
    		 result.put("certStatus", userBean.getCertStatus());
    		 result.put("code", "1");
			 result.put("msg", "用户已登录"); 
			 result.put("data", array);
    	 }
    	 ResponseUtils.renderJson(request, response, result.toString());
     }
}
