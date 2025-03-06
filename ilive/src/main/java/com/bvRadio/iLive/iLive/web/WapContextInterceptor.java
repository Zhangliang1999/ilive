package com.bvRadio.iLive.iLive.web;

import static com.bvRadio.iLive.iLive.Constants.NOT_BIND_MOBILE;
import static com.bvRadio.iLive.iLive.Constants.UN_LOGIN;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;

public class WapContextInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		String callback = request.getParameter("callback");
		String terminalType = request.getParameter("terminalType");
		if (callback == null && ("android".equalsIgnoreCase(terminalType) || "ios".equalsIgnoreCase(terminalType))) {
			// APP的请求
			return true;
		} else {
			String requestUri = request.getRequestURI();
			boolean needMobile = isMacthURI(needMobileUrls, requestUri);
			boolean needLogin = isMacthURI(needLoginUrls, requestUri);
			String roomId = request.getParameter("roomId");
			try {
				// 聊天特殊处理
				if(requestUri.endsWith("/sendMessage.jspx")) {
					ILiveLiveRoom liveRoom = liveRoomMng.findByroomId(Integer.parseInt(roomId));
					Integer openNeedMobile = liveRoom.getOpenNeedMobile();
					if (null != openNeedMobile && openNeedMobile == 1) {
						needMobile = true;
					}else {
						needMobile = false;
					}
				}
			} catch (Exception e) {
			}
			if (needLogin || needMobile) {
				UserBean appUser = ILiveUtils.getAppUser(request);
				if (appUser == null) {
					JSONObject resultJson = new JSONObject();
					resultJson.put("code", UN_LOGIN);
					resultJson.put("message", "长时间未活动,请重新登录");
					resultJson.put("data", "{}");
					ResponseUtils.renderJson(request,response, resultJson.toString());
					return false;
				}
				if (needMobile) {
					try {
						String userId = appUser.getUserId();
						ILiveManager liveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
						String mobile = liveManager.getMobile();
						if (StringUtils.isBlank(mobile)) {
							throw new Exception("手机号为空");
						}
					} catch (Exception e) {
						JSONObject resultJson = new JSONObject();
						resultJson.put("code", NOT_BIND_MOBILE);
						resultJson.put("message", "需要绑定手机号");
						resultJson.put("data", "{}");
						ResponseUtils.renderJson(request,response, resultJson.toString());
						return false;
					}
				}
			}

			return true;
		}
	}

	private boolean isMacthURI(String[] matchUrls, String requestUri) {
		for (String match : matchUrls) {
			if (requestUri.endsWith(match)) {
				return true;
			}
		}
		return false;
	}

	private String[] needLoginUrls;

	private String[] needMobileUrls;

	public String[] getNeedLoginUrls() {
		return needLoginUrls;
	}

	public void setNeedLoginUrls(String[] needLoginUrls) {
		this.needLoginUrls = needLoginUrls;
	}

	public String[] getNeedMobileUrls() {
		return needMobileUrls;
	}

	public void setNeedMobileUrls(String[] needMobileUrls) {
		this.needMobileUrls = needMobileUrls;
	}

	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private ILiveLiveRoomMng liveRoomMng;
}