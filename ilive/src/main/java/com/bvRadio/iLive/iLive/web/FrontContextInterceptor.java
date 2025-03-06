package com.bvRadio.iLive.iLive.web;

import static com.bvRadio.iLive.iLive.Constants.UN_LOGIN;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.UserBean;

public class FrontContextInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		String callback = request.getParameter("callback");
		if (callback == null) {
			// 走手机app的拦截
			UserBean appUser = ILiveUtils.getAppUser(request);
			if (appUser == null) {
				JSONObject resultJson = new JSONObject();
				resultJson.put("code", UN_LOGIN);
				resultJson.put("message", "长时间未活动,请重新登录");
				resultJson.put("data", "{}");
				ResponseUtils.renderJson(response, resultJson.toString());
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	/**
	 * 例外uri
	 * 
	 * @param request
	 * @return
	 */
	private boolean excludeURI(HttpServletRequest request) {
		String requestUrl = request.getRequestURI();
		for (String exc : excludeUrls) {
			if (requestUrl.indexOf(exc) == -1) {
				return false;
			}
		}
		return true;
	}

	private String[] excludeUrls;

	public String[] getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

}