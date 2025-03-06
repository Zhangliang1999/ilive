package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.LIVE_MSG_TYPE_INTERACT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.manager.ILiveLiveMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;

@Controller
public class ILiveInteractAct {

	@RequestMapping("/interact/v_list.do")
	public String list(String queryLiveName, Integer pageNo, ModelMap model) {
		Pagination pagination = liveMng.getPage(null, queryLiveName, null, false, cpn(pageNo), 20,null,null);
		model.addAttribute("pagination", pagination);
		model.addAttribute("leftActive", "7_4");
		model.addAttribute("topActive", "1");
		return "interact/list";
	}

	@RequestMapping("/interact/v_msg_details.do")
	public String msgList(Integer liveId, String queryMsgContent, Integer pageNo,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Pagination pagination = messageMng.getPage(liveId, LIVE_MSG_TYPE_INTERACT, null, null, null,
				null, queryMsgContent, null, null, null, 1, cpn(pageNo), 20);
		ILiveEvent live = liveMng.findById(liveId);
		model.addAttribute("live", live);
		model.addAttribute("pagination", pagination);
		model.addAttribute("leftActive", "7_4");
		model.addAttribute("topActive", "1");
		return "interact/msgDetails";
	}

	@Autowired
	private ILiveLiveMng liveMng;
	@Autowired
	private ILiveMessageMng messageMng;
	
	
}
