package com.bvRadio.iLive.iLive.action.admin;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@RequestMapping(value="cert")
@Controller
public class IliveCertController {

	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	/**
	 * 去认证界面
	 * @param userId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="cert")
	public String cert(Long userId,Model model,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		if(user!=null) {
			userId = Long.valueOf(user.getUserId());
		}
		
		ILiveManager manager = iLiveManagerMng.getILiveManager(userId);
		UserBean userBean = new UserBean();
		userBean.setUserId(String.valueOf(manager.getUserId()));
		userBean.setUsername(manager.getMobile());
		userBean.setNickname(manager.getNailName());
		userBean.setUserThumbImg(manager.getUserImg());
		userBean.setEnterpriseId(manager.getEnterPrise().getEnterpriseId());
		userBean.setCertStatus(manager.getCertStatus());
		ILiveUtils.setUser(request, userBean);
		model.addAttribute("userId",userId);
		return "cert/cert";
	}
	/**
	 * 去登录界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="login.do")
//	@ResponseBody
	public String login(String zhxyuserId,String appId,Model model,HttpServletRequest request) {
		model.addAttribute("zhxyuserId", zhxyuserId);
		model.addAttribute("appId",appId);
		return "cert/vcodeLogin";
	}
	/**
	 * 去认证成功界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="cerSuccess")
//	@ResponseBody
	public String cerSuccess(Model model,HttpServletRequest request) {
		return "cert/cerSuccess";
	}
	/**
	 * 去认证中界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="cerProgress")
//	@ResponseBody
	public String cerProgress(Model model,HttpServletRequest request) {
		return "cert/cerProgress";
	}
}
