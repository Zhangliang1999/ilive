package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.RechargeRulesBean;
import com.bvRadio.iLive.iLive.manager.RechargeRulesMng;

/**
 * 充值规则 视图访问层
 * @author YanXL
 *
 */
@Controller
public class RechargeRulesController {
	
	@Autowired
	private RechargeRulesMng rechargeRulesMng;
	/**
	 * 列表页信息
	 * @param pageNo 页码
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/finan/getRulesPage")
	public String getRechargeRulesByPage(Integer pageNo, ModelMap model,Integer queryRmb){
		Pagination pagination = rechargeRulesMng.getPaginationByPage(cpn(pageNo), 20,queryRmb);
		model.addAttribute("pagination", pagination);
		model.addAttribute("leftActive", "5_5");
		model.addAttribute("queryRmb", queryRmb);
		model.addAttribute("topActive", "1");
		return "finan/rules_list";
	}
	/**
	 * 跳转至添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/finan/addRules")
	public String addRechargeRuleHtml(ModelMap model){
		Integer rmb_price_max = rechargeRulesMng.getRechargeRuleMaxRmb();
		model.addAttribute("maxRmb", rmb_price_max);
		model.addAttribute("leftActive", "5_5");
		model.addAttribute("topActive", "1");
		return "finan/rules_add";
	}
	/**
	 * 添加数据
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/finan/saveRules")
	public String saveRechargeRule(RechargeRulesBean bean, HttpServletRequest request, HttpServletResponse response){
		rechargeRulesMng.saveRechargeRuleBean(bean);
		return "redirect:getRulesPage.do";
	}
	/**
	 * 删除
	 * @param rules_id 主键ID
	 * @return
	 */
	@RequestMapping(value="/finan/deleteRules")
	public @ResponseBody String deleteRechargeRule(Integer rules_id){
		String msg = rechargeRulesMng.deleteRechargeRule(rules_id);
		return msg;
	}
	/**
	 * 批量删除
	 * @param rules_ids 主键ID，主键
	 * @return
	 */
	@RequestMapping(value="/finan/deleteRulesAll")
	public @ResponseBody String deleteRechargeRuleAll(String rules_ids){
		String msg = rechargeRulesMng.deleteRechargeRuleAll(rules_ids);
		return msg;
	}
	/**
	 * 跳转至修改页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/finan/editRules")
	public String editRechargeRule(Integer rules_id,ModelMap model){
		RechargeRulesBean bean= rechargeRulesMng.findRechargeRulesBeanByID(rules_id);
		model.addAttribute("rechargeRulesBean", bean);
		model.addAttribute("leftActive", "5_5");
		model.addAttribute("topActive", "1");
		return "finan/rules_update";
	}
	
	/**
	 * 修改
	 * @param bean
	 * @return
	 */
	@RequestMapping(value="/finan/updateRules")
	public String updateRechargeRule(RechargeRulesBean bean){
		rechargeRulesMng.updateRechargeRule(bean);
		return "redirect:getRulesPage.do";
	}
	
}
