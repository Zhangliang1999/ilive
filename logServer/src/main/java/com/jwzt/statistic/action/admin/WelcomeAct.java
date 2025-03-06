package com.jwzt.statistic.action.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.bo.UserBO;
import com.jwzt.statistic.utils.SessionUtils;

@Controller
public class WelcomeAct {

	@RequestMapping("/frame/top")
	public String index(String token, HttpServletRequest request, ModelMap mp) {
		UserBO currentUser = SessionUtils.getUser(request);
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("currentUser", currentUser);
		RenderJsonUtils.addSuccess(mp, dataMap);
		return "renderJson";
	}

}
