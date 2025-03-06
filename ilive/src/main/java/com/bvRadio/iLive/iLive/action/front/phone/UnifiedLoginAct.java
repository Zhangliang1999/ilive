package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;
import static com.bvRadio.iLive.iLive.Constants.UN_LOGIN;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.InputSource;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.admin.ILiveRoomController;
import com.bvRadio.iLive.iLive.action.front.vo.AppILiveRoom;
import com.bvRadio.iLive.iLive.action.front.vo.AppSubBean;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.action.front.vo.RoomCreateVo;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.AutoLogin;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.IliveSubRoom;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubRoomMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.util.ConvertThread;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.jwzt.ums.api.UmsApi;

import net.sf.json.JSONArray;


@SuppressWarnings("unused")
@RequestMapping(value="UnifiedLogin")
@Controller
public class UnifiedLoginAct {
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	@Autowired
	private WorkLogMng workLogMng;
	@Autowired
	private ILiveSubRoomMng iLiveSubRoomMng;
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	private Logger logger = LoggerFactory.getLogger(UnifiedLoginAct.class);
	
	/**
	 * 统一登录接口
	 * @param appId
	 * @param token
	 * @param time
	 * @param callbacktoken
	 * @param roomId
	 * @param fileId
	 * @param mobile
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="login.jspx")
	public void loginapptw(String token,String time,String  appId,String callbacktoken,Integer roomId,Integer fileId,
			HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		ILiveEnterprise manage= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
		if(manage==null){
			result.put("code", AutoLogin.CODE_ERROR);
			result.put("data", new JSONObject());
			result.put("msg", "无此appId！");
			ResponseUtils.renderJson(request, response, result.toString());
			return;
		}
		try {
			logger.info("开始验证");
			if (true) {
				String md5Hex = DigestUtils.md5Hex(time+"&"+manage.getSecret()+"&"+callbacktoken);
				if (md5Hex.equals(token)) {
					logger.info("token验证完成");
					if (true) {
						String local_host = ConfigUtils.get("local_host");
						//根据appId获取链接
						String infoUrl=manage.getUserInfoUrl();
						if(infoUrl==null){
							result.put("code", AutoLogin.CODE_ERROR);
							result.put("msg", "无法获取用户信息！");
						}else{
							//根據callbacktoken獲取用戶信息
							Map<String, String> map = new HashMap<String, String>();
							map.put("callbacktoken", callbacktoken);
							String postJson = HttpUtils.sendPost(infoUrl, map, "UTF-8");
//							String postJson ="{'message': '成功','data': {'nailName': 'test','userId': 1184,'photo': '','mobile': '18510328589'},'code': 1}";
							JSONObject infoJson=new JSONObject(postJson);
							Integer code=infoJson.getInt("code");
							if(code==0){
								result.put("code", AutoLogin.CODE_ERROR);
								result.put("msg", "获取用户信息失败！");
							}else{
								JSONObject data=infoJson.getJSONObject("data");
//								Integer zhxyuserId=data.getInt("userId");
								String zhxyuserId=data.getString("userId");
								String mobile=data.getString("mobile");
								String nailName=data.getString("nailName");
								//根据智慧校園傳過來的userId和appId判斷用戶是否存在
								String formal_play_address = ConfigUtils.get("formal_play_address");
								ILiveManager manager = iLiveManagerMng.getILiveMangerByPhoneNumber(appId,zhxyuserId.toString());
								if (manager==null) {
									ILiveManager manager1=iLiveManagerMng.getILiveMangerByPhoneNumber(mobile);
									if(manager1!=null){
										manager1.setZhxyappId(appId);
										manager1.setZhxyuserId(zhxyuserId);
										logger.info("当前用户存在，开始登陆");
										ILiveEnterprise enterPrise = manager1.getEnterPrise();
										IliveAppRetInfo retInfo = this.buildAppRet(manager1, enterPrise , request);
										String loginToken = this.buildLoginToken();
										retInfo.setLoginToken(loginToken);
										manager1.setLoginToken(loginToken);
										iLiveManagerMng.updateLiveManager(manager1);
										retInfo.setJpushId(manager1.getJpushId());
										UserBean userBean = new UserBean();
										userBean.setUserId(String.valueOf(manager1.getUserId()));
										userBean.setLoginToken(loginToken);
										userBean.setNickname(manager1.getNailName());
										userBean.setUserThumbImg(manager1.getUserImg());
										userBean.setUsername(manager1.getMobile());
										userBean.setCertStatus(manager1.getCertStatus());
										userBean.setLevel(manager1.getLevel()==null?0:manager1.getLevel());
										logger.info("设置appUser");
										ILiveUtils.setAppUser(request, userBean);
										ILiveUtils.setUser(request, userBean);
										logger.info("reg登录时返回的sessionId->" + request.getSession().getId());
											//判断
											if (fileId!=null) {
												response.sendRedirect(formal_play_address+"/pc/review.html?fileId="+fileId+"&roomId="+roomId+"");
											}else if (roomId!=null) {
												response.sendRedirect(formal_play_address+"/pc/play.html?roomId="+roomId+"");
											}
										
									}else{
										// 注册
											long registeredManagerId = iLiveManagerMng.zhxyregisteredManager(mobile, null, 1,"pc",zhxyuserId,appId,nailName);
											if (registeredManagerId > 0) {
												ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveManager(registeredManagerId);
												ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
												IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise , request);
												String loginToken = this.buildLoginToken();
												retInfo.setLoginToken(loginToken);
												iLiveMangerByMobile.setLoginToken(loginToken);
												iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
												retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
												UserBean userBean = new UserBean();
												userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
												userBean.setLoginToken(loginToken);
												userBean.setUsername(iLiveMangerByMobile.getMobile());
												userBean.setNickname(iLiveMangerByMobile.getNailName());
												userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
												userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
												logger.info("设置appUser");
												ILiveUtils.setAppUser(request, userBean);
												ILiveUtils.setUser(request, userBean);
												logger.info("reg登录时返回的sessionId->" + request.getSession().getId());
													
													//判断
													if (fileId!=null) {
														response.sendRedirect(formal_play_address+"/pc/review.html?fileId="+fileId+"&roomId="+roomId+"");
													}else if (roomId!=null) {
														response.sendRedirect(formal_play_address+"/pc/play.html?roomId="+roomId+"");
													}
											
											}
									}
								}else {
									logger.info("当前用户存在，开始登陆");
									ILiveEnterprise enterPrise = manager.getEnterPrise();
									IliveAppRetInfo retInfo = this.buildAppRet(manager, enterPrise , request);
									String loginToken = this.buildLoginToken();
									retInfo.setLoginToken(loginToken);
									manager.setLoginToken(loginToken);
									iLiveManagerMng.updateLiveManager(manager);
									retInfo.setJpushId(manager.getJpushId());
									UserBean userBean = new UserBean();
									userBean.setUserId(String.valueOf(manager.getUserId()));
									userBean.setLoginToken(loginToken);
									userBean.setNickname(manager.getNailName());
									userBean.setUserThumbImg(manager.getUserImg());
									userBean.setUsername(manager.getMobile());
									userBean.setCertStatus(manager.getCertStatus());
									userBean.setLevel(manager.getLevel()==null?0:manager.getLevel());
									logger.info("设置appUser");
									ILiveUtils.setAppUser(request, userBean);
									ILiveUtils.setUser(request, userBean);
									logger.info("reg登录时返回的sessionId->" + request.getSession().getId());
									//判断
									if (fileId!=null) {
										response.sendRedirect(formal_play_address+"/pc/review.html?fileId="+fileId+"&roomId="+roomId+"");
									}else if (roomId!=null) {
										response.sendRedirect(formal_play_address+"/pc/play.html?roomId="+roomId+"");
									}
								}
							}
								
						}
					}
				}else {
					result.put("code", AutoLogin.CODE_ERROR);
					result.put("msg", "验证失败，token验证不通过");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", AutoLogin.CODE_ERROR);
			result.put("msg", "操作失败,远程地址链接失败");
		}finally {
			ResponseUtils.renderJson(request, response, result.toString());
		}
	}
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
	
	private static final String REGEX_MOBILE = "(134[0-8]\\d{7})" +
            "|(" +
                "((13([0-3]|[5-9]))" +
                    "|149" +
                    "|15([0-3]|[5-9])" +
                    "|166" +
                    "|17(3|[5-8])" +
                    "|18[0-9]" +
                    "|19[8-9]" +
                ")" +
            "\\d{8}" +
            ")";
	private static boolean isMobile(String tel) {
		return Pattern.matches(REGEX_MOBILE, tel);
	}
	
	
	
	
}
