package com.bvRadio.iLive.iLive.action.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.TerminalUserMng;

@Controller
@RequestMapping(value = "terminal")
public class TerminalUserController {

	@Autowired
	private TerminalUserMng userMng;
	@Autowired
	private ILiveEnterpriseFansMng fansMng;

	/**
	 * 终端粉丝管理
	 * @return
	 */
	@RequestMapping(value = "usermanager.do")
	public String uersmanager(Model model, String queryNum, Integer pageNo) {
		Pagination page = fansMng.getPage(queryNum, pageNo, 10);
		
		model.addAttribute("page", page);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "1");
		return "user/terminal_user_manager";
	}
	/**
	 * 黑名单管理
	 * @return
	 */
	@RequestMapping(value = "blacklist.do")
	public String blacklist(Model model, String queryNum, Integer pageNo) {
		Pagination page = fansMng.getPageBlack(queryNum, pageNo, 10);
		
		
		model.addAttribute("page", page);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "3");
		return "user/blacklist";
	}

	/**
	 * 终端用户详情  修改
	 * 
	 * @return
	 */
	@RequestMapping(value = "userdetail.do")
	public String uersdetail(Model model,Long userId) {
		ILiveManager manager = userMng.getById(userId);
		model.addAttribute("user", manager);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "1");
		return "user/terminal_user_detail";
	}
	
	/**
	 * 终端用户详情 查看
	 * 
	 * @return
	 */
	@RequestMapping(value = "seeuser.do")
	public String seeuser(Model model,Long userId) {
		ILiveManager manager = userMng.getById(userId);
		model.addAttribute("user", manager);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "1");
		return "user/terminal_user_see";
	}

	/**
	 * 删除终端用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "removeuser.do")
	public String removeuser(Long id) {
		fansMng.delFans(id);
		return "";
	}
	
	/**
	 * 拉黑粉丝用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "letblack.do")
	public String letblack(Long id) {
		fansMng.letblack(id);
		return "";
	}
	
	/**
	 * 保存终端用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveUser.do")
	public String saveUser(ILiveManager user) {
		ILiveManager primaryUser = userMng.getById(user.getUserId());
		userMng.updateUser(user);
		return "";
	}
}
