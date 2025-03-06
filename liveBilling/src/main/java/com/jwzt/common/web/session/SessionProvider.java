package com.jwzt.common.web.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Session提供者
 */
public interface SessionProvider {
	/**
	 * 获取属性
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public Serializable getAttribute(HttpServletRequest request, String name);

	/**
	 * 设置属性
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 */
	public void setAttribute(HttpServletRequest request, HttpServletResponse response, String name, Serializable value);

	/**
	 * 获取sessionId
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String getSessionId(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 登出
	 * 
	 * @param request
	 * @param response
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response);
}
