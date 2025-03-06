package com.jwzt.statistic.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.jwzt.common.utils.Md5Util;

/**
 * api上下文信息拦截器
 * 
 */
public class ApiContextInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = getURI(request);
		if (exclude(requestURI)) {
			return true;
		}
		String time = request.getParameter("time");
		String sign = request.getParameter("sign");
		if (StringUtils.isNotBlank(time) && StringUtils.isNotBlank(sign)) {
			try {
				long parseLong = Long.parseLong(time);
				long currentTimeMillis = System.currentTimeMillis();
				if (parseLong > currentTimeMillis - 5 * 60 * 1000 || parseLong < currentTimeMillis + 5 * 60 * 1000) {
					String encode = Md5Util.encode(time + "_jwzt_chinaNet");
					if (sign.equalsIgnoreCase(encode)) {
						return true;
					}
				}
			} catch (Exception e) {
				return false;
			}

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
		return true;
	}

	private String[] excludeUrls;

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		if (null != mav) {
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