package com.jwzt.billing.action.ilive;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.billing.entity.bo.UserBO;
import com.jwzt.billing.utils.ConfigUtils;
import com.jwzt.billing.utils.SessionUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class WelcomeAct {

	@RequestMapping("/frame/top")
	public String index(String token, HttpServletRequest request, ModelMap mp) {
		UserBO currentUser = SessionUtils.getUser(request);
		if (null != currentUser) {
			String iliveHomeUrl = ConfigUtils.get("ilive_home_url");
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("currentUser", currentUser);
			dataMap.put("iliveHomeUrl", iliveHomeUrl);
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} else {
			RenderJsonUtils.addError(mp, "");
			return "renderJson";
		}
	}

}
