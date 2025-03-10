package com.jwzt.common.web;

/**
 * @author Tom
 */
import java.io.IOException;

import javax.servlet.Filter;

import javax.servlet.FilterChain;

import javax.servlet.FilterConfig;

import javax.servlet.ServletException;

import javax.servlet.ServletRequest;

import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;

public class XssFilter implements Filter {
	private String filterChar;
	private String replaceChar;
	private String splitChar;
	private String xssFilterOff;
	FilterConfig filterConfig = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterChar = filterConfig.getInitParameter("FilterChar");
		this.replaceChar = filterConfig.getInitParameter("ReplaceChar");
		this.splitChar = filterConfig.getInitParameter("SplitChar");
		this.xssFilterOff = filterConfig.getInitParameter("xssFilterOff");
		this.filterConfig = filterConfig;
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,

			FilterChain chain) throws IOException, ServletException {
		if (!"true".equals(xssFilterOff)) {
			chain.doFilter(
					new XssHttpServletRequestWrapper((HttpServletRequest) request, filterChar, replaceChar, splitChar),
					response);
		}
	}

}
