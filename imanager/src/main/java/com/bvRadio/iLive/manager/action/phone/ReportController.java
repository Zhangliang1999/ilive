package com.bvRadio.iLive.manager.action.phone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.manager.entity.ReportAndComplain;
import com.bvRadio.iLive.manager.manager.ReportAndComplainService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="report")
public class ReportController {

	@Autowired
	private ReportAndComplainService reportAndComplainService;
	
	@Autowired
	private ILiveManagerMng ILiveManagerMng;
	
	/**
	 * 举报
	 * @param response
	 * @param request
	 * @param userId
	 * @param roomId
	 * @param curRoomId
	 * @param roomName
	 * @param userContact
	 * @param reportReason
	 * @param reportImg
	 */
	@RequestMapping(value="report.jspx")
	public void report(HttpServletResponse response,HttpServletRequest request,Long userId,
			Integer roomId,Integer curRoomId,String roomName,String userContact,
			String reportReason,String reportImg) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveManager iLiveManager = ILiveManagerMng.getILiveManager(userId);
			ReportAndComplain reportAndComplain = new ReportAndComplain();
			reportAndComplain.setUserId(userId);
			reportAndComplain.setSubmitUser(iLiveManager.getMobile());
			reportAndComplain.setRoomId(roomId);
			reportAndComplain.setCurRoomId(curRoomId);
			reportAndComplain.setNowRoomName(roomName);
			reportAndComplain.setUserContact(userContact);
			reportAndComplain.setReportReason(reportReason);
			reportAndComplain.setReportImg(reportImg);
			reportAndComplain.setType(1);
			reportAndComplainService.save(reportAndComplain);
			resultJson.put("code", 1);
			resultJson.put("message", "成功");
			resultJson.put("data", new JSONObject());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 0);
			resultJson.put("message", "失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	@RequestMapping(value="complain.jspx")
	public void complain(HttpServletResponse response,HttpServletRequest request,Long userId,
			String userContact,String suggest) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveManager iLiveManager = ILiveManagerMng.getILiveManager(userId);
			ReportAndComplain reportAndComplain = new ReportAndComplain();
			reportAndComplain.setSubmitUser(iLiveManager.getMobile());
			reportAndComplain.setUserId(userId);
			reportAndComplain.setSuggest(suggest);
			
			reportAndComplainService.save(reportAndComplain);
			resultJson.put("code", 1);
			resultJson.put("message", "成功");
			resultJson.put("data", new JSONObject());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 0);
			resultJson.put("message", "失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
}
