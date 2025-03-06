package com.jwzt.billing.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jwzt.billing.entity.bo.UserBO;

/**
 * @author ysf
 */
public class SessionUtils {
	public static final String CURRENT_USER = "current_user";

	public static final void setUser(HttpServletRequest request, UserBO userBean) {
		HttpSession session = request.getSession();
		session.setAttribute(CURRENT_USER, userBean);
	}

	public static final UserBO getUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (null != session) {
			UserBO userBean = (UserBO) session.getAttribute(CURRENT_USER);
			return userBean;
		}
		return null;
	}

	public static final Long getUserId(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (null != session) {
			UserBO userBean = (UserBO) session.getAttribute(CURRENT_USER);
			if (null != userBean) {
				return userBean.getId();
			}
		}
		return null;
	}

	public static final void logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		try {
			if (session != null) {
				session.invalidate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
