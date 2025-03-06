package com.bvRadio.iLive.iLive.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@RequestMapping(value="developer")
public class DeveloperController {

	@Autowired
	private ILiveManagerMng IiiveManagerMng;
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	
	@RequestMapping(value="info")
	public String info(Model model,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Long userId = Long.valueOf(user.getUserId());
		ILiveManager iLiveManager = IiiveManagerMng.getILiveManager(userId);
		ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(iLiveManager.getEnterpriseId().intValue());
		
		model.addAttribute("iLiveEnterPrise", iLiveEnterPrise);
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "11_1");
		return "developer/developerinfo";
	}
	
	@RequestMapping(value="doc")
	public String doc(Model model,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Long userId = Long.valueOf(user.getUserId());
		ILiveManager iLiveManager = IiiveManagerMng.getILiveManager(userId);
		ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(iLiveManager.getEnterpriseId().intValue());
		
		model.addAttribute("iLiveEnterPrise", iLiveEnterPrise);
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "11_2");
		return "developer/developerdoc";
	}
}
