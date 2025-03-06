package com.bvRadio.iLive.iLive.action.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.manager.AccountMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value="account")
public class AccountController {
	
	@Autowired
	private AccountMng accountMng;
	
	/**
	 * 概览
	 * @return
	 */
	@RequestMapping(value="overview.do")
	public String overview(Model model,HttpServletRequest request) {
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "1");
		return "account/overview";
	}

	/**
	 * 企业信息管理
	 * @return
	 */
	@RequestMapping(value="accountmanager.do")
	public String account(Model model,HttpServletRequest request) {
		ILiveEnterprise enterprise = accountMng.getEnterpriseById(ILiveUtils.getUser(request).getEnterpriseId());
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("enterprise", enterprise);
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "6");
		return "account/enterprise_info";
	}
	
	private Integer selectServerGroup() {
		return 100;
	}
	/**
	 * 账号管理
	 * @return
	 */
	@RequestMapping(value="accountmng.do")
	public String accountmng(Model model) {
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "5");
		return "account/account_manager";
	}
	
	/**
	 * 企业信息更新
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveenterprise.do")
	public String saveaccount(ILiveEnterprise iLiveEnterprise) {
		accountMng.saveaccount(iLiveEnterprise);
		return "account/enterprise_info";
	}
	
}
