package com.bvRadio.iLive.iLive.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.bvRadio.iLive.common.web.RequestUtils;
import com.bvRadio.iLive.iLive.entity.UserBean;

/**
 * ILive上下文信息拦截器
 * 
 * 包括登录信息、权限信息、站点信息
 */
public class TlapAdminContextInterceptor extends HandlerInterceptorAdapter {

	public static final String PERMISSION_MODEL = "_permission_key";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String uri = getURI(request);
		if (uri.equals("/BBSLogin.do")) {
			return true;
		}
		UserBean user = ILiveUtils.getUser(request);
		System.out.println("ILIVE拿到了user为------------------------->" + user);
		if (user == null) {
			String locationUrl = RequestUtils.getLocation(request);
			String bbsLoginUrl = ConfigUtils.get(ConfigUtils.BBS_TLAP_ROOT_HTTP_URL);
			if (locationUrl.indexOf("https://") != -1) {
				bbsLoginUrl = ConfigUtils.get(ConfigUtils.BBS_TLAP_ROOT_HTTPS_URL);
			}
			response.sendRedirect(bbsLoginUrl);
			return false;
		}
//		Integer userType = user.getUserType();
//		System.out.println("ILIVE拿到了userType为------------------------->" + userType);
//		userType = userType == null ? 0 : userType;
		// System.out.println("拿到了user为------------------------->userType:"+userType);
//		Integer userType2 = com.jwzt.sso.handler.UserTypeConstant.AppUser_PCUser.getUserType();
//		if (!userType.equals(userType2)) {
//			String locationUrl = RequestUtils.getLocation(request);
//			String bbsLoginUrl = ConfigUtils.get(ConfigUtils.BBS_TLAP_ROOT_HTTP_URL);
//			if (locationUrl.indexOf("https://") != -1) {
//				bbsLoginUrl = ConfigUtils.get(ConfigUtils.BBS_TLAP_ROOT_HTTPS_URL);
//			}
//			response.sendRedirect(bbsLoginUrl);
//			return false;
//		}
//		System.out.println("#################ILIVE拿到了setUser##################");
		ILiveUtils.setUser(request, user);
		return true;
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