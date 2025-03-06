package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.manager.RedRecordingBeanService;
/**
 * 红包  管理
 * @author Administrator
 *
 */
@Controller
public class RedRecordingBeanController {
	
	@Autowired
	private RedRecordingBeanService redRecordingBeanService;
	/**
	 * 分页红包数据
	 * @param pageNo 页码
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/finan/getRedPage")
	public String getRedRecordingBeanPage(Integer pageNo,ModelMap model){
		Pagination pagination = redRecordingBeanService.selectRedRecordingByPage(cpn(pageNo), 20);
		model.addAttribute("pagination", pagination);
		model.addAttribute("leftActive", "5_9");
		model.addAttribute("topActive", "1");
		return "finan/red_list";
	}
}
