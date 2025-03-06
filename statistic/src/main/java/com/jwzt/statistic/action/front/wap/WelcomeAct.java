package com.jwzt.statistic.action.front.wap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class WelcomeAct {
	@RequestMapping("/index")
	public String index(ModelMap mp) {
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

}
