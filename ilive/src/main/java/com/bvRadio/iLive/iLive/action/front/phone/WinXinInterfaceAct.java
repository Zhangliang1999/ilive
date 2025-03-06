package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.web.util.WebUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.TempLoginInfo;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileShareConfig;
import com.bvRadio.iLive.iLive.entity.ILiveRoomShareConfig;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryShareMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileShareConfigMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomShareConfigMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.jwzt.jssdk.JSSDKInfo;
import com.jwzt.jssdk.JSSDKMgr;
import com.jwzt.jssdk.WechatUserInfo;

/**
 * 微信接口登录页
 * 
 * @author administrator
 */

@Controller
@RequestMapping(value = "app/wx")
public class WinXinInterfaceAct {
	public static final String USER_SIGN_USERID = "_user_sign_userid";
	public static final String USER_SIGN_USERIMG = "_user_sign_userimg";
	private static final String wxAppbasePath = "https://open.weixin.qq.com/connect/oauth2/authorize?";

	Logger logger = LoggerFactory.getLogger(WinXinInterfaceAct.class);

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private ILiveRoomShareConfigMng iLiveRoomShareConfigMng;
	@Autowired
	private ILiveMediaFileShareConfigMng iLiveMediaFileShareConfigMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;
	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;

	
	@RequestMapping(value = "h5.jspx")
	public void getWinXinH5Id(HttpServletRequest request, HttpServletResponse response,Integer isAjax,Integer signId) {
		JSONObject resultJson = new JSONObject();
		ILiveManager manager = null;
		WechatUserInfo wechatUserInfo = null;
		try {
			if(isAjax==null) {
				String appId = ConfigUtils.get("wxAppId");
				String appSecret = ConfigUtils.get("wxAppSecret");
				JSSDKMgr jssdkMgr = new JSSDKMgr(appId, appSecret);
				wechatUserInfo = jssdkMgr.getWechatUserInfo(request);
				manager = iLiveManagerMng.getILiveMangerByWxUnionId(wechatUserInfo.getUnionId());
				
				if (manager != null) {
					// 直接登录
					UserBean userBean = new UserBean();
					userBean.setUserId(String.valueOf(manager.getUserId()));
					userBean.setUsername(manager.getMobile());
					userBean.setNickname(manager.getNailName());
					userBean.setUserThumbImg(manager.getUserImg());
					userBean.setEnterpriseId(manager.getEnterPrise().getEnterpriseId());
					userBean.setCertStatus(manager.getCertStatus());
					ILiveUtils.setAppUser(request, userBean);
					HttpSession session = WebUtils.toHttp(request).getSession();
					session.setAttribute(USER_SIGN_USERID, String.valueOf(manager.getUserId()));
					session.setAttribute(USER_SIGN_USERIMG, manager.getUserImg());
					response.sendRedirect(ConfigUtils.get("sign_in_phone_sign")+"?signId="+signId);
					
				} else {
					try {
						// 注册登录
						ILiveManager manger = iLiveManagerMng.registeredManagerByWx(wechatUserInfo);
						if (manger != null) {
							UserBean userBean = new UserBean();
							userBean.setUserId(String.valueOf(manger.getUserId()));
							userBean.setUsername(manger.getMobile());
							userBean.setNickname(manger.getNailName());
							userBean.setUserThumbImg(manger.getUserImg());
							userBean.setEnterpriseId(manger.getEnterPrise().getEnterpriseId());
							userBean.setCertStatus(manger.getCertStatus());
							ILiveUtils.setAppUser(request, userBean);
							
						}
						HttpSession session = WebUtils.toHttp(request).getSession();
						session.setAttribute(USER_SIGN_USERID, String.valueOf(manger.getUserId()));
						session.setAttribute(USER_SIGN_USERIMG, manger.getUserImg());
						response.sendRedirect(ConfigUtils.get("sign_in_phone_sign")+"?signId="+signId);
					} catch (Exception e) {
						logger.error(e.toString());
						e.printStackTrace();
					}
					
				}
				
			}else {
				String requestPath = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
				String wxLoginUrl=requestPath + "/app/wx/h5.jspx?signId="+signId;
				System.out.println(wxLoginUrl);
				wxLoginUrl = URLEncoder.encode(wxLoginUrl, "utf-8");
				String wxAppPath = wxAppbasePath + "appid=" + ConfigUtils.get("wxAppId") + "&redirect_uri="
						+wxLoginUrl+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
				resultJson.put("loginWx", 1);
				resultJson.put("loginUrl", wxAppPath);
				ResponseUtils.renderJson(request, response, resultJson.toString());
			}
		} catch (Exception e) {
			resultJson.put("loginWx", -1);
			ResponseUtils.renderJson(request, response, resultJson.toString());
			e.printStackTrace();
			logger.error(e.toString());
		}
		
	}
	
	
	
	@RequestMapping(value = "app.jspx")
	public void getWinXinAppId(HttpServletRequest request, Integer roomId, Integer fileId,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
			if (tempLoginInfo == null) {
				tempLoginInfo = new TempLoginInfo();
				request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
			}
			UserBean appUser = ILiveUtils.getAppUser(request);
			if (appUser != null) {
				resultJson.put("loginWx", 0);
			} else {
				if (tempLoginInfo.getWxLogin() == null || !tempLoginInfo.getWxLogin()) {
					// 引导跳转到微信登录页
					resultJson.put("loginWx", 1);
					/**
					 * 引导跳转到微信地址
					 */
					String requestPath = request.getScheme() + "://" + request.getServerName()
							+ request.getContextPath();
					String wxLoginUrl = "";
					if (fileId != null) {
						wxLoginUrl = requestPath + "/app/wx/login.jspx?roomId=" + roomId + "&fileId=" + fileId;
					} else {
						wxLoginUrl = requestPath + "/app/wx/login.jspx?roomId=" + roomId;
					}
					wxLoginUrl = URLEncoder.encode(wxLoginUrl, "utf-8");
					String wxAppPath = wxAppbasePath + "appid=" + ConfigUtils.get("wxAppId") + "&redirect_uri="
							+ wxLoginUrl + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
					resultJson.put("loginUrl", wxAppPath);
				} else {
					// 直接进入
					resultJson.put("loginWx", 0);
				}
			}
		} catch (Exception e) {
			resultJson.put("loginWx", 0);
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	@RequestMapping(value = "login.jspx")
	public void getWinXinCallback(HttpServletRequest request, Integer roomId, Integer fileId,
			HttpServletResponse response) throws IOException {
		ILiveManager manager = null;
		WechatUserInfo wechatUserInfo = null;
		String redLocation = "";
		Integer serverGroupId = -1;
		if(roomId==null || roomId==0){
			//因为部分文件不会属于任何的直播间，并且其实H5的地址其实是一直的，所以其实用哪个服务器组都没关系
			serverGroupId = 102;
		}else{
			ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
			serverGroupId = iliveRoom.getServerGroupId();
		}
		
		ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
				.getAccessMethodBySeverGroupId(serverGroupId);
		if (fileId == null) {
			redLocation = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone/live.html?roomId=" + roomId;
		} else {
			redLocation = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone/review.html?roomId=" + roomId + "&fileId="
					+ fileId;
		}
		try {
			TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
			if (tempLoginInfo == null) {
				tempLoginInfo = new TempLoginInfo();
			}
			tempLoginInfo.setWxLogin(true);
			String appId = ConfigUtils.get("wxAppId");
			String appSecret = ConfigUtils.get("wxAppSecret");
			JSSDKMgr jssdkMgr = new JSSDKMgr(appId, appSecret);
			wechatUserInfo = jssdkMgr.getWechatUserInfo(request);
			manager = iLiveManagerMng.getILiveMangerByWxUnionId(wechatUserInfo.getUnionId());
			//TempLoginInfo tempLoginInfo = (TempLoginInfo) request.getSession().getAttribute("tempLoginInfo");
			//if (tempLoginInfo == null) {
			//	tempLoginInfo = new TempLoginInfo();
			//}
			//tempLoginInfo.setWxLogin(true);
			request.getSession().setAttribute("tempLoginInfo", tempLoginInfo);
			if (manager != null) {
				// 直接登录
				UserBean userBean = new UserBean();
				userBean.setUserId(String.valueOf(manager.getUserId()));
				userBean.setUsername(manager.getMobile());
				userBean.setNickname(manager.getNailName());
				userBean.setUserThumbImg(manager.getUserImg());
				userBean.setEnterpriseId(manager.getEnterPrise().getEnterpriseId());
				userBean.setCertStatus(manager.getCertStatus());
				ILiveUtils.setAppUser(request, userBean);
				response.sendRedirect(redLocation);
			} else {
				try {
					// 注册登录
					ILiveManager manger = iLiveManagerMng.registeredManagerByWx(wechatUserInfo);
					if (manger != null) {
						UserBean userBean = new UserBean();
						userBean.setUserId(String.valueOf(manger.getUserId()));
						userBean.setUsername(manger.getMobile());
						userBean.setNickname(manger.getNailName());
						userBean.setUserThumbImg(manger.getUserImg());
						userBean.setEnterpriseId(manger.getEnterPrise().getEnterpriseId());
						userBean.setCertStatus(manger.getCertStatus());
						ILiveUtils.setAppUser(request, userBean);
						response.sendRedirect(redLocation);
					}
				} catch (Exception e) {
					logger.error(e.toString());
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			response.sendRedirect(redLocation);
		}
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "share.jspx")
	public void shareThirdSystem(HttpServletRequest request, String shareUrl, HttpServletResponse response) {
		try {
			String appId = ConfigUtils.get("wxAppId");
			String appSecret = ConfigUtils.get("wxAppSecret");
			JSSDKMgr jssdkMgr = new JSSDKMgr(appId, appSecret);
			JSSDKInfo info = jssdkMgr.setShareConfig(shareUrl);
			
			org.json.JSONObject jsonObj = new org.json.JSONObject(info);
			ResponseUtils.renderJsonCros(request, response, jsonObj.toString());
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/shareinfo.jspx")
	public void getShareConfig(Integer roomId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
        if(JedisUtils.exists("shareinfo:"+roomId)) {
        	
			ResponseUtils.renderJson(request, response, JedisUtils.get("shareinfo:"+roomId));
			return;
		}else {

			//boolean needInsertShareConfig = true;
			List<ILiveRoomShareConfig> shareConfigList = iLiveRoomShareConfigMng.getShareConfig(roomId);
			if(null!=shareConfigList && !shareConfigList.isEmpty()) {
				//needInsertShareConfig = false;
				try {
					Iterator<ILiveRoomShareConfig> iterator = shareConfigList.iterator();
					while (iterator.hasNext()) {
						ILiveRoomShareConfig iLiveRoomShareConfig = (ILiveRoomShareConfig) iterator.next();
						if(null!=iLiveRoomShareConfig) {
							Integer openStatus = iLiveRoomShareConfig.getOpenStatus();
							if(null==openStatus||openStatus==0) {
								iterator.remove();
							}
						}
					}
					
				} catch (Exception e) {
				}
			}
			if (shareConfigList == null || shareConfigList.isEmpty()) {
				
				ILiveRoomShareConfig shareConfigBean = new ILiveRoomShareConfig();
				shareConfigBean.setShareType(ILiveRoomShareConfig.FRIEND_SINGLE);
				ILiveRoomShareConfig shareConfigBeanCIRCLE = new ILiveRoomShareConfig();
				shareConfigBeanCIRCLE.setShareType(ILiveRoomShareConfig.FRIEND_CIRCLE);
				
				ILiveLiveRoom iLiveLiveRoom = iLiveRoomMng.getIliveRoom(roomId);
				ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
				shareConfigBean.setLiveTitle(StringPatternUtil.convertEmpty(liveEvent.getLiveTitle()));
				shareConfigBeanCIRCLE.setLiveTitle(StringPatternUtil.convertEmpty(liveEvent.getLiveTitle()));
				String appendDesc = "直播开始时间:";
				String time = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分")
						.format(new Date(liveEvent.getLiveStartTime().getTime()));
				appendDesc = appendDesc + time;
				if (!com.jwzt.comm.StringUtils.isEmpty(liveEvent.getLiveDesc())) {
					appendDesc += ",";
					appendDesc += liveEvent.getLiveDesc();
				}
				shareConfigBean.setLiveDesc(appendDesc);
				shareConfigBeanCIRCLE.setLiveDesc(appendDesc);
				shareConfigBean.setRoomId(roomId);
				shareConfigBeanCIRCLE.setRoomId(roomId);
				
				ILiveServerAccessMethod accessMethodBySever = accessMethodMng
						.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
				String shareUrl = accessMethodBySever.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
				shareConfigBean.setShareUrl(shareUrl);
				shareConfigBeanCIRCLE.setShareUrl(shareUrl);
				Integer enterpriseId = iLiveLiveRoom.getEnterpriseId();
				ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
				String enterpriseImg = iLiveEnterPrise.getEnterpriseImg();
				if (enterpriseImg == null || "".equals(enterpriseImg)) {
					shareConfigBean.setLiveImg(ConfigUtils.get("shareOtherConfig"));
					shareConfigBeanCIRCLE.setLiveImg(ConfigUtils.get("shareOtherConfig"));
				} else {
					shareConfigBean.setLiveImg(enterpriseImg);
					shareConfigBeanCIRCLE.setLiveImg(enterpriseImg);
				}
				//if(needInsertShareConfig){
					//shareConfigList = iLiveRoomShareConfigMng.addIliveRoomShareConfig(shareConfigBean);
				//}
				shareConfigList = new ArrayList<ILiveRoomShareConfig>();
				shareConfigList.add(shareConfigBean);
				shareConfigList.add(shareConfigBeanCIRCLE);
				
				
			}
			JSONObject jsonObject = new JSONObject();
			if (!shareConfigList.isEmpty()) {
				for (ILiveRoomShareConfig share : shareConfigList) {
					if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_SINGLE)) {
						// 朋友
						jsonObject.put("friendSingle", new JSONObject(share));
					} else if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_CIRCLE)) {
						// 朋友圈
						jsonObject.put("friendCircle", new JSONObject(share));
					}
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			resultJson.put("data", jsonObject);
			resultJson.put("sessionId", request.getSession().getId());
			JedisUtils.set("shareinfo:"+roomId, resultJson.toString(), 300);
		}
		
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
			resultJson.put("data", 0);
			logger.error(e.toString());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	@RequestMapping(value = "/sharefileinfo.jspx")
	public void getMediaFileShareConfig(Integer fileId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			//boolean needInsertShareConfig = true;
			List<ILiveMediaFileShareConfig> shareConfigList = iLiveMediaFileShareConfigMng.getShareConfig(fileId);
			if(null!=shareConfigList && !shareConfigList.isEmpty()) {
				//needInsertShareConfig = false;
				try {
					Iterator<ILiveMediaFileShareConfig> iterator = shareConfigList.iterator();
					while (iterator.hasNext()) {
						ILiveMediaFileShareConfig iLiveMediaFileShareConfig = (ILiveMediaFileShareConfig) iterator.next();
						if(null!=iLiveMediaFileShareConfig) {
							Integer openStatus = iLiveMediaFileShareConfig.getOpenStatus();
							if(null==openStatus||openStatus==0) {
								iterator.remove();
							}
						}
					}
					
				} catch (Exception e) {
				}
			}
			if (shareConfigList == null || shareConfigList.isEmpty()) {
				
				ILiveMediaFileShareConfig shareConfigBean = new ILiveMediaFileShareConfig();
				shareConfigBean.setShareType(ILiveMediaFileShareConfig.FRIEND_SINGLE);
				ILiveMediaFileShareConfig shareConfigBeanCIRCLE = new ILiveMediaFileShareConfig();
				shareConfigBeanCIRCLE.setShareType(ILiveMediaFileShareConfig.FRIEND_CIRCLE);
				
				ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaForWeb(fileId.longValue());
				
				shareConfigBean.setMediaFileName(StringPatternUtil.convertEmpty(iLiveMediaFile.getMediaFileName()));
				shareConfigBeanCIRCLE.setMediaFileName(StringPatternUtil.convertEmpty(iLiveMediaFile.getMediaFileName()));
				String appendDesc = "视频收录时间:";
				String time = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分")
						.format(new Date(iLiveMediaFile.getCreateStartTime().getTime()));
				appendDesc = appendDesc + time;
				if (!com.jwzt.comm.StringUtils.isEmpty(iLiveMediaFile.getMediaFileDesc())) {
					appendDesc += ",";
					appendDesc += iLiveMediaFile.getMediaFileDesc();
					shareConfigBean.setMediaFileDesc(iLiveMediaFile.getMediaFileDesc());
				}
				if (com.jwzt.comm.StringUtils.isEmpty(iLiveMediaFile.getMediaFileDesc())) {
					shareConfigBean.setMediaFileDesc("");
				}
				
				//shareConfigBeanCIRCLE.setMediaFileDesc(appendDesc);
				shareConfigBean.setFileId(fileId);
				shareConfigBeanCIRCLE.setFileId(fileId);
				
				Integer serverMountId = iLiveMediaFile.getServerMountId();
				ILiveServerAccessMethod serverAccess = accessMethodMng.getAccessMethodByMountId(serverMountId);
				String shareUrl= serverAccess.getH5HttpUrl() + "/phone" + "/review.html?roomId=0"
				 + "&fileId=" + fileId;
				shareConfigBean.setShareUrl(shareUrl);
				shareConfigBeanCIRCLE.setShareUrl(shareUrl);
				
				String enterpriseImg = iLiveMediaFile.getFileCover();
				if (enterpriseImg == null || "".equals(enterpriseImg)) {
					shareConfigBean.setShareImg(ConfigUtils.get("shareOtherConfig"));
					shareConfigBeanCIRCLE.setShareImg(ConfigUtils.get("shareOtherConfig"));
				} else {
					shareConfigBean.setShareImg(enterpriseImg);
					shareConfigBeanCIRCLE.setShareImg(enterpriseImg);
				}
				//if(needInsertShareConfig){
					//shareConfigList = iLiveRoomShareConfigMng.addIliveRoomShareConfig(shareConfigBean);
				//}
				shareConfigList = new ArrayList<ILiveMediaFileShareConfig>();
				shareConfigList.add(shareConfigBean);
				shareConfigList.add(shareConfigBeanCIRCLE);
				
				
			}
			JSONObject jsonObject = new JSONObject();
			if (!shareConfigList.isEmpty()) {
				for (ILiveMediaFileShareConfig share : shareConfigList) {
					if (share.getShareType().equals(ILiveMediaFileShareConfig.FRIEND_SINGLE)) {
						// 朋友
						jsonObject.put("friendSingle", new JSONObject(share));
					} else if (share.getShareType().equals(ILiveMediaFileShareConfig.FRIEND_CIRCLE)) {
						// 朋友圈
						jsonObject.put("friendCircle", new JSONObject(share));
					}
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			resultJson.put("data", jsonObject);
			resultJson.put("sessionId", request.getSession().getId());
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
			resultJson.put("data", 0);
			logger.error(e.toString());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
}
