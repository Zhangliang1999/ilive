package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveGift;
import com.bvRadio.iLive.iLive.manager.ILiveGiftMng;

@Controller
public class ILiveGiftAct {

	@Autowired
	private ILiveGiftMng iLiveGiftMng;

	@RequestMapping({ "/iLiveGift/list.do" })
	public String list(ModelMap model, Integer pageNo, Integer pageSize, HttpServletRequest request) {
		Pagination pagination = this.iLiveGiftMng.getPage(cpn(pageNo), 20);
		model.put("pagination", pagination);
		model.addAttribute("leftActive", "5_8");
		model.addAttribute("jsessionId", request.getSession().getId());
		model.addAttribute("topActive", "1");
		return "gift/list";
	}

	@RequestMapping({ "/iLiveGift/add.do" })
	public String add(String jsessionId, ModelMap model) {
		model.addAttribute("jsessionId", jsessionId);
		model.addAttribute("leftActive", "5_8");
		model.addAttribute("topActive", "1");
		return "gift/add";
	}

	@RequestMapping("/iLiveGift/save.do")
	public String save(Integer liveType, Integer type, String pic, Integer num, String name) {
		ILiveGift gift = new ILiveGift();
		gift.setCreatTime(new Timestamp(new Date().getTime()));
		gift.setLiveType(liveType);
		gift.setName(name);
		gift.setNum(num);
		gift.setPic(pic);
		gift.setType(type);
		iLiveGiftMng.save(gift);
		return "redirect:list.do";
	}

	@RequestMapping("/iLiveGift/update.do")
	public String update(Integer giftId, ModelMap model, String jsessionId) {
		ILiveGift gift = iLiveGiftMng.findById(giftId);
		model.addAttribute("gift", gift);
		model.addAttribute("leftActive", "5_8");
		model.addAttribute("jsessionId", jsessionId);
		model.addAttribute("topActive", "1");
		return "gift/update";
	}

	@RequestMapping("/iLiveGift/update_save.do")
	public String update_save(ILiveGift bean) {
		bean.setCreatTime(new Timestamp(new Date().getTime()));
		bean.setId(bean.getId());
		bean.setLiveType(bean.getLiveType());
		bean.setNum(bean.getNum());
		bean.setPic(bean.getPic());
		bean.setType(bean.getType());
		iLiveGiftMng.update(bean);
		return "redirect:list.do";
	}

	@RequestMapping("/iLiveGift/delete.do")
	public void delete(Integer giftId, HttpServletResponse response) {
		iLiveGiftMng.delete(giftId);
		ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
	}

	@RequestMapping("/iLiveGift/deletes.do")
	public void deletes(@RequestParam(value = "ids[]") Integer[] ids, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		iLiveGiftMng.deleteByIds(ids);
		ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
	}
}
