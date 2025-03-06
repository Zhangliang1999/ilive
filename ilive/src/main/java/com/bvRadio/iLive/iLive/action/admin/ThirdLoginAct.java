package com.bvRadio.iLive.iLive.action.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
public class ThirdLoginAct {
	@RequestMapping(value = "/third/userinfo.do", method = RequestMethod.GET)
	public void login(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		UserBean currentUser = ILiveUtils.getUser(request);
		String userId = currentUser.getUserId();
		ILiveManager manager = iLiveManagerMng.selectILiveManagerById(Long.parseLong(userId));
		String userName = manager.getMobile();
		String userImg = manager.getUserImg();
		String nailName = manager.getNailName();
		Integer enterpriseId = manager.getEnterPrise().getEnterpriseId();
		Integer certStatus = manager.getCertStatus();
		Integer level=manager.getLevel();
		List subTop=currentUser.getSubTop();
		Integer subAccountManagement=currentUser.getSubAccountManagement();
		Integer getIfPassRevenue=currentUser.getGetIfPassRevenue();
		JSONObject json = new JSONObject();
		json.put("getIfPassRevenue", getIfPassRevenue);
		json.put("subAccountManagement", subAccountManagement);
		json.put("subTop", subTop);
		json.put("level", level);
		json.put("userId", userId);
		json.put("userName", userName);
		json.put("nickName", nailName);
		json.put("enterpriseId", enterpriseId);
		json.put("userImg", userImg);
		json.put("certStatus", certStatus);
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequestMapping(value = "/third/billing.do", method = RequestMethod.GET)
	public void billing(String account,String menu, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//此处需添加校检，如果没有认证或者认证没通过，需要先进行认证再购买
		UserBean currentUser = ILiveUtils.getUser(request);
		Integer status = currentUser.getCertStatus();
		if(status!=4&&"0".equals(account)) {
			//如果不是已认证的用户直接跳转到认证页面
			try {
				response.sendRedirect("/ilive/admin/cert/enterprise.do");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			String billingHomeUrl = ConfigUtils.get("billing_home_url");
			try {
				String sessionId = request.getSession().getId();
				String[] a=request.getSession().getValueNames();
				System.out.println(a.toString());
				billingHomeUrl = StringUtils.replace(billingHomeUrl, ":token", sessionId);
				billingHomeUrl += "&redirectUrl=" + menu;
				response.sendRedirect(billingHomeUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Autowired
	private ILiveManagerMng iLiveManagerMng;
}
