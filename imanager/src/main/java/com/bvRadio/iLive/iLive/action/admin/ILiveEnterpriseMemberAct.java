package com.bvRadio.iLive.iLive.action.admin;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMemberMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

/**
 * @author administrator 会员管理
 */
@Controller
@RequestMapping(value = "member")
public class ILiveEnterpriseMemberAct {

	@Autowired
	private ILiveEnterpriseMemberMng iliveEnterproseMng;

	/**
	 * 白名单
	 */
	@RequestMapping(value = "whitelist.do")
	public String whiteListRouter(Model model, String queryNum, Integer pageNo, Integer pageSize,
			HttpServletRequest request) {
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		if (enterpriseId == null) {
			enterpriseId = 100;
		}
		Pagination page = iliveEnterproseMng.getPage(queryNum, pageNo, 20, enterpriseId);
		model.addAttribute("page", page);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "2");
		return "whitelist/whitebill";
	}

	/**
	 * 终端用户
	 */
	@RequestMapping(value = "terminalUser.do")
	public String terminalUser(Model model, String queryNum, Integer pageNo, Integer pageSize,
			HttpServletRequest request) {
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		if (enterpriseId == null) {
			enterpriseId = 100;
		}
		Pagination page = iliveEnterproseMng.getPage(queryNum, pageNo, 20, enterpriseId);
		model.addAttribute("page", page);
		return "whitelist/whitebill";
	}

	/**
	 * 添加白名单
	 */
	@RequestMapping(value = "addwhite.do")
	public String addWhiteListRouter(Model model, ILiveEnterpriseWhiteBill white, HttpServletRequest request) {
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		if (enterpriseId == null) {
			enterpriseId = 10;
		}
		white.setEnterpriseId(enterpriseId);
		white.setExportPerson(userBean.getUsername());
		white.setExportTime(new Timestamp(System.currentTimeMillis()));
		iliveEnterproseMng.addWhite(white);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "2");
		return "redirect:whitelist.do";
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "deletewhite.do")
	public String deleteWhite(Integer whitebillId) {
		iliveEnterproseMng.deleteWhite(whitebillId);
		return "";
	}

	/**
	 * 黑名单
	 * 
	 * @return
	 */
	// public String blackListRouter(String queryName,Integer pageNo, Integer
	// pageSize, ModelMap map) {
	//
	//
	// return queryName;
	// }

}
