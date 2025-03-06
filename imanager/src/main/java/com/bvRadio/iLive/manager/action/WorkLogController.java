package com.bvRadio.iLive.manager.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.manager.entity.WorkLog;
import com.bvRadio.iLive.manager.manager.WorkLogMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RequestMapping(value="worklog")
@Controller
public class WorkLogController {

	@Autowired
	private WorkLogMng workLogMng;
	
	@RequestMapping(value="getList")
	public String getList(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime,Integer pageNo,Integer pageSize,Model model) {
		//List<WorkLog> list = workLogMng.listByParams(systemId, modelId, contentId, userId, startTime, endTime,pageNo,pageSize);
		Map< ?, ?> map = workLogMng.getList(systemId, modelId, contentId, userId, startTime, endTime, pageNo, pageSize);
		@SuppressWarnings("unchecked")
		List<WorkLog> list = (List<WorkLog>)map.get("list");
		int pagenum = (Integer) map.get("totalPage");
		int total=list.size();
		model.addAttribute("total",total);
		model.addAttribute("pagenum",pagenum);
		model.addAttribute("list", list);
		model.addAttribute("nowpage", pageNo==null?1:pageNo);
		model.addAttribute("topActive","1");
		model.addAttribute("leftActive", "4");
		return "managercontrol/worklog";
	}
	
	@ResponseBody
	@RequestMapping(value="getpage")
	public void getPage(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime,Integer pageNo,Integer pageSize,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			if (contentId.equals("")) {
				contentId = null;
			}
			Map< ?, ?> map = workLogMng.getList(systemId, modelId, contentId, userId, startTime, endTime, pageNo, pageSize);
			@SuppressWarnings("unchecked")
			List<WorkLog> list = (List<WorkLog>)map.get("list");
			int pagenum = (Integer) map.get("totalPage");
			result.put("code", 1);
			result.put("data", JSONArray.fromObject(list));
			result.put("pagenum", pagenum);
			result.put("nowpage", pageNo==null?1:pageNo);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 0);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
}
