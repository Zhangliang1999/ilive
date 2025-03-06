package com.bvRadio.iLive.test;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class FilterTest implements Filter{

	private FilterConfig config;
	
	private String[] str;
	
	private String[] allStr;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("进入到FilterConfig");
		config = filterConfig;
		String initParameter = filterConfig.getInitParameter("test1");
		if (initParameter!=null) {
			String[] split = initParameter.split(",");
			str = new String[split.length];
			for(int i=0;i<split.length;i++) {
				str[i] = split[i].trim();
			}
		}
		String initParameter2 = filterConfig.getInitParameter("allpackage");
		if (initParameter2!=null) {
			String[] split2 = initParameter2.split(",");
			allStr = new String[split2.length];
			for(int i=0;i<split2.length;i++) {
				allStr[i] = split2[i].trim();
			}
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("进入到filterTest的dofilter方法");
		System.out.println("filterTest参数test1");
		for(String s:str) {
			System.out.println(s);
		}
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		System.out.println(servletRequest.getRequestURI());
		System.out.println("dofilter");
		for(String s:str) {
			if (servletRequest.getRequestURI().equals(s)) {
				System.out.println(s+"不用过滤");
			}
		}
		
		for(String s:allStr) {
			if (s.length()<servletRequest.getRequestURI().length()
					&&s.equals(servletRequest.getRequestURI().substring(0, s.length()))) {
				System.out.println(servletRequest.getRequestURI()+"被过滤掉了");
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		this.config = null;
	}

}
