package com.bvRadio.iLive.manager.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng;

@Controller
//@RequestMapping(value = "enterprise")
public class EnterpriseAct {

	@Autowired
	private ILiveEnterpriseMng enterpriseMng;

	/**
	 * 企业概览
	 * 
	 * @return
	 */
	@RequestMapping(value = "overview.do")
	public String overview(ModelMap modelMap,HttpServletRequest request) {
		modelMap.addAttribute("topActive", "3");
		return "enterprise/overview";
	}

	/**
	 * 企业管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "manager.do")
	public String manager(Model model, Integer pageNo, String content, String enterprisetype) {
		int pass = 1;
		Pagination pagination = enterpriseMng.getPage(enterprisetype, content, pass, pageNo, 10);
		model.addAttribute("pagination", pagination);
		model.addAttribute("passtype", 1);
		return "enterprise/manager";
	}

	/**
	 * 企业认证详细页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "cert.do")
	public String cert(Model model, Integer enterpriseId, HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		System.out.println(user);
		ILiveEnterprise enterprise = enterpriseMng.getILiveEnterpriseById(enterpriseId);
		// System.out.println(enterprise.getEnterpriseInfo());
		model.addAttribute("enterprise", enterprise);
		return "enterprise/cert";
	}

	/**
	 * 企业认证页面
	 * @return
	 */
//	@RequestMapping(value = "ecert.do")
//	public String ecert(Model model, String enterprisetype, Integer pageNo, String content) {
//		int pass = 0;
//		Pagination pagination = enterpriseMng.getPage(enterprisetype, content, pass, pageNo, 10);
//		model.addAttribute("pagination", pagination);
//		model.addAttribute("passtype", 0);
//		return "enterprise/ecert";
//	}

	/**
	 * 企业审核
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "validate.do")
	public String validate(Integer enterpriseId, Integer pass) {
		ILiveEnterprise enterprise = enterpriseMng.getILiveEnterpriseById(enterpriseId);
		if (pass == 2) {
			enterprise.setCertStatus(2);
		} else {
			enterprise.setCertStatus(3);
		}
		enterpriseMng.update(enterprise);
		return "";
	}

	/**
	 * 删除申请信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "remove.do")
	public String remove(Integer enterpriseId) {
		enterpriseMng.del(enterpriseId);
		return "";
	}

}
