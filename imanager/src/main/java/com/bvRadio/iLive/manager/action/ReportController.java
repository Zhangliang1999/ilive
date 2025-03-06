package com.bvRadio.iLive.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.manager.entity.ReportAndComplain;
import com.bvRadio.iLive.manager.manager.ReportAndComplainService;

@RequestMapping(value="report")
@Controller
public class ReportController {
	
	@Autowired
	private ReportAndComplainService reportAndComplainService;
	
	/**
	 * 举报管理
	 * @param model
	 * @param id
	 * @param username
	 * @param roomId
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="reportlist")
	public String report(Model model,Long freportid,Long fusername,Integer froomid
			,Integer type,Integer pageNo,Integer pageSize) {
		Pagination page = reportAndComplainService.getPage(freportid, fusername, froomid, type
				, pageNo==null?1:pageNo, 10);
		model.addAttribute("page", page);
		model.addAttribute("freportid", freportid);
		model.addAttribute("fusername", fusername);
		model.addAttribute("froomid", froomid);
		model.addAttribute("type", type);
		model.addAttribute("leftActive", type==1?'5':'6');
		model.addAttribute("topActive", "6");
		
		return type==1?"report/reportlist":"report/complain";
	}
	
	/**
	 * 举报、投诉详情
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="detail")
	public String detail(Long id,Model model) {
		ReportAndComplain reportAndComplain = reportAndComplainService.getById(id);
		model.addAttribute("report", reportAndComplain);
		model.addAttribute("leftActive", reportAndComplain.getType()==1?'5':'6');
		model.addAttribute("topActive", "6");
		return reportAndComplain.getType()==1?"report/reportdetail":"report/complaindetail";
	}
	
}
