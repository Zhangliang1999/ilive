package com.jwzt.billing.action.admin;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.billing.entity.bo.UserBO;
import com.jwzt.billing.utils.ConfigUtils;
import com.jwzt.billing.utils.SessionUtils;
import com.jwzt.common.utils.HttpUtils;
import com.jwzt.common.utils.JsonUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class LoginAct {
	private static final Logger log = LogManager.getLogger();

	@RequestMapping("/login")
	public String login(String token, String redirectUrl, HttpServletRequest request, HttpServletResponse response,
			ModelMap mp) {
		try {
			String url = ConfigUtils.get("user_login_url");
			url = StringUtils.replace(url, ":token", token);
			String userJson = HttpUtils.sendGet(url, "utf-8");
			Map<?, ?> resultMap = JsonUtils.jsonToMap(userJson);
			Map<?, ?> dataMap = (Map<?, ?>) resultMap.get("data");
			Object userIdObj = dataMap.get("userId");
			Long userId;
			if (null != userIdObj && Long.class.isInstance(userIdObj)) {
				userId = (Long) userIdObj;
			} else {
				userId = ((Integer) userIdObj).longValue();
			}
			String nickname = (String) dataMap.get("nickName");
			String userImg = (String) dataMap.get("userImg");
			String userName = (String) dataMap.get("userName");
			UserBO userBean = new UserBO();
			userBean.setId(userId);
			userBean.setNickname(nickname);
			userBean.setUserImg(userImg);
			userBean.setUsername(userName);
			userBean.setAdmin(true);
			SessionUtils.setUser(request, userBean);
			String contextPath = request.getContextPath();
			if (StringUtils.isNotBlank(redirectUrl)) {
				redirectUrl = URLDecoder.decode(redirectUrl, "utf-8");
				if (redirectUrl.startsWith("/")) {
					response.sendRedirect(contextPath + redirectUrl);
				} else {
					response.sendRedirect(contextPath + "/" + redirectUrl);
				}
			} else {
				response.sendRedirect(contextPath + "/web/imanager/index.html");
			}
		} catch (IOException e) {
			log.debug("登录出错", e);
			SessionUtils.logout(request);
		}
		return null;
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, ModelMap mp) {
		SessionUtils.logout(request);
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

}
