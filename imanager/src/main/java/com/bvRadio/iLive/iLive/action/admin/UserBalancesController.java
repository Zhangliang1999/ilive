package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.manager.UserBalancesMng;

/**
 * 用户账户
 * @author YanXL
 *
 */
@Controller
public class UserBalancesController {
	
	@Autowired
	private UserBalancesMng userBalancesMng;
	
	@RequestMapping(value="/finan/getUserPage")
	public String getPaginationByPage(Integer pageNo,String userName,ModelMap model){
		Pagination pagination = userBalancesMng.selectPaginationBypage(cpn(pageNo), 20, userName);
		model.addAttribute("pagination", pagination);
		model.addAttribute("leftActive", "5_4");
		model.addAttribute("userName", userName);
		model.addAttribute("topActive", "1");
		return "finan/user_list";
	}
	
}
