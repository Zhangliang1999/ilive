package com.bvRadio.iLive.common.web;

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
	private String XssFilterOff;
	FilterConfig filterConfig = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterChar = filterConfig.getInitParameter("FilterChar");
		this.replaceChar = filterConfig.getInitParameter("ReplaceChar");
		this.splitChar = filterConfig.getInitParameter("SplitChar");
		this.XssFilterOff = filterConfig.getInitParameter("XssFilterOff");
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,

			FilterChain chain) throws IOException, ServletException {
		if (!XssFilterOff.equals("true")) {
			chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request, filterChar, replaceChar, splitChar), response);
		}
	}

}
