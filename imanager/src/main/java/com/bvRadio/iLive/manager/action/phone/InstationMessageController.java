package com.bvRadio.iLive.manager.action.phone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.manager.manager.InstationMessageService;
import com.bvRadio.iLive.manager.manager.InstationMessageUserService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="instation")
public class InstationMessageController {

	@Autowired
	private InstationMessageService instationMessageService;
	
	@Autowired
	private InstationMessageUserService instationMessageUserService;
	
	
	@RequestMapping(value="getList.jspx")
	public void save(Long userId,Integer pageNo,Integer pageSize,HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			Pagination page = instationMessageUserService.getPageByUserId(userId, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
			resultJson.put("code", 1);
			resultJson.put("message", "获取成功");
			resultJson.put("data", page.getList());
		} catch (Exception e) {
			resultJson.put("code", 0);
			resultJson.put("message", "获取失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response,resultJson.toString());
	}
	
	
}
