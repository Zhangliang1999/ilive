package com.jwzt.common.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 执行时间过滤器
 */
public class ProcessTimeFilter implements Filter {
	protected final Logger log = LogManager.getLogger();
	/**
	 * 请求执行开始时间
	 */
	public static final String START_TIME = "_start_time";

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		long time = System.currentTimeMillis();
		request.setAttribute(START_TIME, time);
		chain.doFilter(request, response);
		time = System.currentTimeMillis() - time;
		log.trace("process in {} ms: {}", time, request.getRequestURI());
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
