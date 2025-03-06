package com.jwzt.statistic.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.jwzt.common.utils.JsonUtils;
import com.jwzt.common.utils.Md5Util;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.constants.RenderJsonConstants;
import com.jwzt.statistic.entity.bo.UserBO;
import com.jwzt.statistic.utils.SessionUtils;

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
		String requestURI = getURI(request);
		if (exclude(requestURI)) {
			return true;
		}
		String authString = request.getParameter("authString");
		HttpSession session = request.getSession();
		if (authString==null) {
			authString = (String) session.getAttribute("authString");
		}else {
			session.setAttribute("authString", authString);
		}
		
		if (StringUtils.isNotBlank(authString)) {
			AuthBean authBean = checkAuthString(authString);
			// authBean.setResult(true);
			if (null != authBean && null != authBean.getResult() && authBean.getResult()) {
				String paramString = authBean.getParamString();
				UserBO userBean = new UserBO();
				userBean.setId(-10000L);
				// userBean.setAdmin(true);
				userBean.setAdmin(false);
				userBean.setEnterpriseIds(getQueryString(paramString, "enterpriseId"));
				userBean.setRoomIds(getQueryString(paramString, "roomId"));
				SessionUtils.setUser(request, userBean);
				return true;
			}
		}
		UserBO currentUser = SessionUtils.getUser(request);
		if (null == currentUser) {
			ModelMap model = new ModelMap();
			request.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			RenderJsonUtils.addError(model, RenderJsonConstants.NOT_LOGIN_STATUS_ERROR, "用户未登录", null);
			render(request, response, model);
			return false;
		}
		Boolean admin = currentUser.getAdmin();
		if (null == admin || !admin) {
			boolean noPermission = true;
			try {
				String enterpriseId = request.getParameter("enterpriseId");
				String enterpriseIds = currentUser.getEnterpriseIds();
				if (null != enterpriseIds && (enterpriseIds.indexOf("," + enterpriseId + ",") != -1
						|| enterpriseIds.equals(enterpriseId))) {
					noPermission = false;
				}
				String roomId = request.getParameter("roomId");
				String roomIds = currentUser.getRoomIds();
				if (null != roomIds && (roomIds.indexOf("," + roomId + ",") != -1 || roomIds.equals(roomId))) {
					noPermission = false;
				}
			} catch (Exception e) {
			}
			if (noPermission) {
				ModelMap model = new ModelMap();
				request.setCharacterEncoding("UTF-8");
				response.setContentType("application/json;charset=UTF-8");
				RenderJsonUtils.addError(model, RenderJsonConstants.NOT_PERMISSION_ERROR, "用户没有权限", null);
				render(request, response, model);
				return false;
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		if (null != mav) {
		}
	}

	private String getQueryString(String parmas, String name) {
		String value = null;
		if (null != parmas && null != name) {
			String str;
			if (parmas.indexOf("?") != -1) {
				str = parmas.substring(parmas.indexOf("?") + 1);
			} else {
				str = parmas;
			}
			String[] strs = str.split("&");
			for (int i = 0; i < strs.length; i++) {
				if (name.equals(strs[i].split("=")[0])) {
					value = strs[i].split("=")[1];
					break;
				}
			}
		}
		return value;
	}

	private AuthBean checkAuthString(String authString) {
		AuthBean authBean = new AuthBean();
		if (StringUtils.isNotBlank(authString)) {
			try {
				String[] array = authString.split("@");
				String timeStr = array[0].trim().substring(4, array[0].length() - 4);
				long warpnum = 5 * 60;
				long time = Long.parseLong(timeStr);
				authBean.setTime(time);
				long currentTimeMillis = System.currentTimeMillis() / 1000;
				if (time < (currentTimeMillis - warpnum) || time > (currentTimeMillis + warpnum)) {
					authBean.setResult(false);
					return authBean;
				}
				String paramString = array[1].trim();
				authBean.setParamString(paramString);
				String md5Str = array[2].trim();
				String encodeMd5Str = Md5Util.encode(timeStr + "_chinanet_2018_jwzt_" + paramString);
				if (encodeMd5Str.equalsIgnoreCase(md5Str)) {
					authBean.setResult(true);
					return authBean;
				}
			} catch (Exception e) {
				e.printStackTrace();
				authBean.setResult(false);
				return authBean;
			}
		}
		authBean.setResult(false);
		return authBean;
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

	/**
	 * 获得第三个路径分隔符的位置
	 * 
	 * @param request
	 * @throws IllegalStateException
	 *             访问路径错误，没有三(四)个'/'
	 */
	private String getURI(HttpServletRequest request) throws IllegalStateException {
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

	private void render(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model)
			throws IOException {
		String json = JsonUtils.objToJson(model);
		PrintWriter respWriter = response.getWriter();
		String jsoncallback = request.getParameter("jsoncallback");
		if (!StringUtils.isBlank(jsoncallback)) {
			respWriter.print(jsoncallback + "(" + json + ");");
		} else {
			respWriter.print(json);
		}
		respWriter.flush();
		respWriter.close();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	class AuthBean {
		private Boolean result;
		private String paramString;
		private Long time;

		public Boolean getResult() {
			return result;
		}

		public void setResult(Boolean result) {
			this.result = result;
		}

		public String getParamString() {
			return paramString;
		}

		public void setParamString(String paramString) {
			this.paramString = paramString;
		}

		public Long getTime() {
			return time;
		}

		public void setTime(Long time) {
			this.time = time;
		}

	}
}