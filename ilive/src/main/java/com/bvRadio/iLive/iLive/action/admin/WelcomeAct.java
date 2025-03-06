package com.bvRadio.iLive.iLive.action.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.RequestUtils;
import com.bvRadio.iLive.core.manager.ConfigMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

@Controller
public class WelcomeAct {
	@RequestMapping("/index.do")
	public String index(ModelMap model) {
//		String smgIP = ConfigUtils.get(ConfigUtils.SMG_IP);
//		model.addAttribute("smgIP", smgIP);
		return "redirect:" + "/admin/room/list.do";
	}

	@RequestMapping("/BBS.do")
	public String BBS(HttpServletRequest request, ModelMap model) {
		String locationUrl = RequestUtils.getLocation(request);
		String bbsRootUrl = ConfigUtils.get(ConfigUtils.BBS_ROOT_HTTP_URL);
		if (locationUrl.indexOf("https://") != -1) {
			bbsRootUrl = ConfigUtils.get(ConfigUtils.BBS_ROOT_HTTPS_URL);
		}
		return "redirect:" + bbsRootUrl + "/bbs/admin/index.do";
	}
	
	@Autowired
	private ConfigMng cfMng;
	
	@RequestMapping("/testHm")
	public String testHm() {
		String value = cfMng.getValue("111");
		return value;
	}

}
