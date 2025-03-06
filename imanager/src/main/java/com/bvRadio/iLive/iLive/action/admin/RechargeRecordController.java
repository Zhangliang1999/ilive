package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.manager.RechargeRecordMng;
/**
 * 充值记录 视图
 * @author YanXL
 *
 */
@Controller
public class RechargeRecordController {
	
	@Autowired
	private RechargeRecordMng rechargeRecordMng;
	/**
	 * 
	 * @param pageNo 页码
	 * @param model
	 * @param payment_type  充值类型值
	 * @param payment_status 支付状态值
	 * @param keyword  关键数（用户ID、商户订单号）
	 * @return
	 */
	@RequestMapping(value="/finan/getRecharge")
	public String findRechargeRecordByPage(Integer pageNo, ModelMap model,Integer payment_status,Integer payment_type,Integer keyword){
		Pagination page = rechargeRecordMng.getPage(cpn(pageNo), 20,payment_status, payment_type, keyword);
		model.addAttribute("pagination", page);
		model.addAttribute("leftActive", "5_1");
		model.addAttribute("payment_status", payment_status);
		model.addAttribute("payment_type", payment_type);
		model.addAttribute("keyword", keyword);
		model.addAttribute("topActive", "1");
		return "finan/recharge_list";
	}

}
