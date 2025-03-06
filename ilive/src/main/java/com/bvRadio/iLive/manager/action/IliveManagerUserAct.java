package com.bvRadio.iLive.manager.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author administrator 平台用户控制器 包含web用户 管理用户 等
 */
@Controller
@RequestMapping(value = "/user")
public class IliveManagerUserAct {

	@RequestMapping(value = "/overview.do")
	public String userOverView() {
		
		return "";
	}

}
