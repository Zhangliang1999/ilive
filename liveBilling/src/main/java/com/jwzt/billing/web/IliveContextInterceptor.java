package com.jwzt.billing.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.jwzt.billing.constants.RenderJsonConstants;
import com.jwzt.billing.entity.bo.UserBO;
import com.jwzt.billing.utils.SessionUtils;
import com.jwzt.common.utils.JsonUtils;
import com.jwzt.common.web.RequestUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

/**
 * ILive上下文信息拦截器
 * 
 * 包括登录信息、权限信息、站点信息
 */
public class IliveContextInterceptor extends HandlerInterceptorAdapter {

	public static final String PERMISSION_MODEL = "_permission_key";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = getURI(request);
		if (exclude(requestURI)) {
			return true;
		}
		/*
		String ip = RequestUtils.getIpAddr(request);
		if ("127.0.0.1".equals(ip)) {
			UserBO userBean = new UserBO();
			userBean.setId(-1L);
			userBean.setNickname("");
			userBean.setUserImg("");
			userBean.setUsername("137");
			userBean.setEnterpriseId(100);
			userBean.setAdmin(false);
			userBean.setCertStatus(4);
			SessionUtils.setUser(request, userBean);
		}
		*/
		UserBO currentUser = SessionUtils.getUser(request);
		if (null == currentUser) {
			ModelMap model = new ModelMap();
			request.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			RenderJsonUtils.addError(model, RenderJsonConstants.CODE_NOT_LOGIN, "用户未登录", null);
			render(request, response, model);
			return false;
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		if (null != mav) {
		}
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
			throw new IllegalStateException("admin access path not like '/ilive/...' pattern: " + uri);
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

}