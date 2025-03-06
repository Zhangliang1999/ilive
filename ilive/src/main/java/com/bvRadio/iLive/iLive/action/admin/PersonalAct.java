package com.bvRadio.iLive.iLive.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.core.manager.UnifiedUserMng;
import com.bvRadio.iLive.iLive.entity.PersonalBean;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.iLive.web.PostMan;
import com.google.gson.Gson;

@Controller
public class PersonalAct {

	@RequestMapping("/personal/v_pwd_edit.do")
	public String pwdEdit(HttpServletRequest request, ModelMap model) {
		UserBean userBean = ILiveUtils.getUser(request);
		model.addAttribute("currUser", userBean);
		model.addAttribute("jsessionId", request.getSession().getId());
		return "personal/pwdEdit";
	}

	@SuppressWarnings("static-access")
	@RequestMapping("/personal/o_update.do")
	public String update(PersonalBean personalBean) {
		Gson gson = new Gson();
		PostMan postMan = new PostMan();
		String personal_pwd_url = ConfigUtils.get(ConfigUtils.personal_pwd_url);
		String postJson = postMan.postJson(personal_pwd_url, "POST", "UTF-8", gson.toJson(personalBean));
		// System.out.println(postJson);
		return "redirect:v_pwd_edit.do";
	}
	@RequestMapping("/personal/v_checkPwd.do")
	public void checkPwd(String origPwd, HttpServletRequest request, HttpServletResponse response) {
		UserBean userBean = ILiveUtils.getUser(request);
		long userId = Long.valueOf(userBean.getUserId());
		boolean pass = unifiedUserMng.isPasswordValid((int)userId, origPwd);
		ResponseUtils.renderJson(response, pass ? "{\"status\":true}" : "{\"status\":false}");
	}
	@Autowired
	private UnifiedUserMng unifiedUserMng;
}
