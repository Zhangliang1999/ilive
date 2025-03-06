package com.bvRadio.iLive.iLive.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.bvRadio.iLive.iLive.entity.UserBean;

/**
 * ILive上下文信息拦截器
 * 
 * 包括登录信息、权限信息、站点信息
 */
public class AdminContextInterceptor extends HandlerInterceptorAdapter {

	public static final String PERMISSION_MODEL = "_permission_key";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		@SuppressWarnings("static-access")
		String requestURI = this.getURI(request);
		if (exclude(requestURI)) {
			if (requestURI.indexOf("opentrial.do") > -1) {
				UserBean user = ILiveUtils.getUser(request);
				if (user != null) {
					return true;
				} else {
					response.sendRedirect("/ilive/admin/login.do");
					return false;
				}
			}
			return true;
		}
		UserBean user = ILiveUtils.getUser(request);
		if (user != null) {
			Integer certStatus = user.getCertStatus();
			certStatus = certStatus == null ? 0 : certStatus;
			if (certStatus == 0) {
				response.sendRedirect("/ilive/admin/opentrial.do");
				return false;
			}
			return true;
		} else {
			response.sendRedirect("/ilive/admin/login.do");
		}
		return false;
	}

	private boolean exclude(String uri) {
		if (excludeUrls != null) {
			for (String exc : excludeUrls) {
				if (uri.endsWith(exc)) {
					return true;
				}
			}
		}
		return false;
	}

	private String[] excludeUrls;

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		if (null != mav) {
			ModelMap model = mav.getModelMap();
			/**
			 * 处理顶部框架用户信息显示的通用信息
			 */
			UserBean topUser = ILiveUtils.getUser(request);
			if (null != topUser) {
				model.put("topUser", topUser);
			}
		}
	}

	/**
	 * 获得第三个路径分隔符的位置
	 * 
	 * @param request
	 * @throws IllegalStateException
	 *             访问路径错误，没有三(四)个'/'
	 */
	private static String getURI(HttpServletRequest request) throws IllegalStateException {
		UrlPathHelper helper = new UrlPathHelper();
		String uri = helper.getOriginatingRequestUri(request);
		String ctxPath = helper.getOriginatingContextPath(request);
		int start = 0, i = 0, count = 1;
		if (!StringUtils.isBlank(ctxPath)) {
			count++;
		}
		while (i < count && start != -1) {
			start = uri.indexOf('/', start + 1);
			i++;
		}
		if (start <= 0) {
			throw new IllegalStateException("admin access path not like '/admin/...' pattern: " + uri);
		}
		return uri.substring(start);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}