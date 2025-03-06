package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.PresentRecordBean;
import com.bvRadio.iLive.iLive.entity.UserBalancesBean;
import com.bvRadio.iLive.iLive.manager.PresentRecordMng;
import com.bvRadio.iLive.iLive.manager.UserBalancesMng;

/**
 * 提现视图
 * @author YanXL
 *
 */
@Controller
public class PresentRecordController {
	
	@Autowired
	private PresentRecordMng presentRecordMng;
	
	@Autowired
	private UserBalancesMng userBalancesMng;
	/**
	 * 提现分页
	 * @param pageNo 页码
	 * @param model
	 * @param present_type 提现状态值
	 * @param keyword 关键值（用户ID、订单号）
	 * @return
	 */
	@RequestMapping(value="/finan/getPresent")
	public String  getPresentRecordByPage(Integer pageNo, ModelMap model,Integer present_type,Integer keyword){
		Pagination page = presentRecordMng.getPage(cpn(pageNo), 20,present_type,keyword);
		model.addAttribute("pagination", page);
		model.addAttribute("leftActive", "5_2");
		model.addAttribute("present_type", present_type);
		model.addAttribute("keyword", keyword);
		model.addAttribute("topActive", "1");
		return "finan/present_list";
	}
	/**
	 * 跳转至审核页
	 * @param present_id 提现ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/finan/findPresent.do")
	public String findPresentByPresentId(long present_id,ModelMap model){
		PresentRecordBean presentRecordBean = presentRecordMng.findPresentRecordByPresentId(present_id);
		model.addAttribute("presentRecordBean", presentRecordBean);
		Integer user_id = presentRecordBean.getUser_id();
		UserBalancesBean userBalancesBean = userBalancesMng.selectUserBalanceByUserId(user_id);
		model.addAttribute("userBalancesBean", userBalancesBean);
		model.addAttribute("leftActive", "5_2");
		model.addAttribute("topActive", "1");
		return "finan/present_find";
	}
	/**
	 * 提现审核结果修改
	 * @param present_type 1：拒绝提现   2：提现成功
	 * @param present_id 提现信息ID
	 * @return
	 */
	@RequestMapping(value="/finan/presentUpate",method=RequestMethod.POST)
	public @ResponseBody String updatePresentRecordBeanBypresent_type(Integer present_type,long present_id){
		String msg = presentRecordMng.updatePresentRecordBeanBypresent_type(present_type,present_id);
		return msg;
	}
}
