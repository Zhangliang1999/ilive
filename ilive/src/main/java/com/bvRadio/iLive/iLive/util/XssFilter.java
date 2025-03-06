package com.bvRadio.iLive.iLive.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * XSS过滤
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-01 10:20
 */
public class XssFilter implements Filter {

	/**
	 * 需要排除的页面
	 */

	private String excludedPages;
	private String[] excludedPageArray;
	
	private String[] excludePackage;

	@Override
	public void init(FilterConfig config) throws ServletException {
		excludedPages = config.getInitParameter("excludedPages");
		if (StringUtils.isNotEmpty(excludedPages)) {
			excludedPageArray = excludedPages.split(",");
		}
		if (config.getInitParameter("excludePackage")!=null) {
			String[] excludePackages = config.getInitParameter("excludePackage").split(",");
			excludePackage = new String[excludePackages.length];
			for(int i=0;i<excludePackages.length;i++) {
				excludePackage[i] = excludePackages[i].trim();
			}
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		boolean isExcludedPage = false;
		for (String page : excludedPageArray) {// 判断是否在过滤url之外
			HttpServletRequest requestHttp = (HttpServletRequest) request;
			String tempPath = requestHttp.getRequestURI();
			if (tempPath.equals(page.trim())) {
				isExcludedPage = true;
				break;
			}
		}
		if (excludePackage!=null) {
			for(String s:excludePackage) {
				HttpServletRequest requestHttp = (HttpServletRequest) request;
				String tempPath = requestHttp.getRequestURI();
				if (tempPath.length()>s.length()
						&&tempPath.substring(0, s.length()).equals(s)) {
					isExcludedPage = true;
					break;
				}
			}
		}
		if (isExcludedPage) {
			chain.doFilter(request, response);
		} else {
			XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
			chain.doFilter(xssRequest, response);
		}
	}

	@Override
	public void destroy() {
	}

}