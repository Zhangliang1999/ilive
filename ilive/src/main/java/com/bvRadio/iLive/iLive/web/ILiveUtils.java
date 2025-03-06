package com.bvRadio.iLive.iLive.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.web.util.WebUtils;

import com.bvRadio.iLive.iLive.entity.UserBean;

/**
 * 提供一些ILive系统中使用到的共用方法
 * 
 * 比如获得会员信息,获得后台站点信息
 */
public class ILiveUtils {
	/**
	 * 用户KEY
	 */
	public static final String USER_KEY = "_user_key";
	
	/**
	 * 用户KEY
	 */
	public static final String APP_USER_KEY = "appUser";

	/**
	 * 获得用户
	 * 
	 * @param request
	 * @return
	 */
	public static UserBean getUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (UserBean) session.getAttribute(USER_KEY);
	}
	
	/**
	 * 获得用户
	 * 
	 * @param request
	 * @return
	 */
	public static UserBean getAppUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (UserBean) session.getAttribute(APP_USER_KEY);
	}
	
	/**
	 * 设置用户
	 * 
	 * @param request
	 * @param user
	 */
	public static void setAppUser(HttpServletRequest request, UserBean user) {
		HttpSession session = WebUtils.toHttp(request).getSession();
		int enterpriseId = user.getEnterpriseId()==null?0:user.getEnterpriseId();
		user.setEnterpriseId(enterpriseId);
		session.setAttribute(APP_USER_KEY, user);
	}

	/**
	 * 设置用户
	 * 
	 * @param request
	 * @param user
	 */
	public static void setUser(HttpServletRequest request, UserBean user) {
		HttpSession session = WebUtils.toHttp(request).getSession();
		int enterpriseId = user.getEnterpriseId()==null?0:user.getEnterpriseId();
		user.setEnterpriseId(enterpriseId);
		session.setAttribute(USER_KEY, user);
	}

}
