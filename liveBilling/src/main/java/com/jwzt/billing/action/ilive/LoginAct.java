package com.jwzt.billing.action.ilive;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
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
			String url = ConfigUtils.get("ilive_user_login_url");
			String userJson = HttpUtils.sendGet(url, "utf-8", token);
			Map<?, ?> resultMap = JsonUtils.jsonToMap(userJson);
			Object userIdObj = resultMap.get("userId");
			Long userId;
			if (null != userIdObj && Long.class.isInstance(userIdObj)) {
				userId = (Long) userIdObj;
			} else if (null != userIdObj && Integer.class.isInstance(userIdObj)) {
				userId = ((Integer) userIdObj).longValue();
			} else {
				userId = Long.parseLong(userIdObj.toString());
			}
			String nickname = (String) resultMap.get("nickName");
			String userImg = (String) resultMap.get("userImg");
			String userName = (String) resultMap.get("userName");
			Integer enterpriseId = (Integer) resultMap.get("enterpriseId");
			Integer certStatus = (Integer) resultMap.get("certStatus");
			Integer getIfPassRevenue = (Integer) resultMap.get("getIfPassRevenue");
			Integer subAccountManagement = (Integer) resultMap.get("subAccountManagement");
			System.out.println("subAccountManagement====="+subAccountManagement);
			Integer level = (Integer) resultMap.get("level");
			if(level==null) {
				level=0;
			}
			System.out.println("level======="+level);
			List subTop=(List)resultMap.get("subTop");
			UserBO userBean = new UserBO();
			userBean.setId(userId);
			userBean.setNickname(nickname);
			userBean.setUserImg(userImg);
			userBean.setUsername(userName);
			userBean.setEnterpriseId(enterpriseId);
			userBean.setCertStatus(certStatus);
			userBean.setAdmin(false);
			userBean.setGetIfPassRevenue(getIfPassRevenue);
			userBean.setSubAccountManagement(subAccountManagement);
			userBean.setLevel(level);
			userBean.setSubTop(subTop);
			boolean a=userBean.message(level, subTop);
			if(a) {
				userBean.setEnterPriseMessage(1);
			}else {
				userBean.setEnterPriseMessage(0);
			}
			boolean b=userBean.sub(level, subTop, subAccountManagement);
			if(b) {
				userBean.setSub(1);
			}else {
				userBean.setSub(0);
			}
			boolean c=userBean.figure(level, subTop);
			if(c) {
				userBean.setFigure(1);
			}else {
				userBean.setFigure(0);
			}
			boolean d=userBean.vip(level, subTop);
			if(d) {
				userBean.setVip(1);
			}else {
				userBean.setVip(0);
			}
			boolean e=userBean.fans(level, subTop);
			if(e) {
				userBean.setFans(1);
			}else {
				userBean.setFans(0);
			}
			boolean f=userBean.account(level, subTop,getIfPassRevenue);
			if(f) {
				userBean.setAccount(1);
			}else {
				userBean.setAccount(0);
			}
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
				response.sendRedirect(contextPath + "/web/ilive/index.html#!/flow/list");
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
