package com.bvRadio.iLive.iLive.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;

@Controller
@RequestMapping(value="worklog")
public class WorkLogController {

	@Autowired
	private WorkLogMng workLogMng;
	
	@ResponseBody
	@RequestMapping(value="save.do",method=RequestMethod.GET)
	public String save(WorkLog workLog,HttpServletResponse response) {
		workLog = new WorkLog(100, "156", "123", "123", 156l, "laowang", "");
		String save = workLogMng.save(workLog);
		return save;
	}
	
	@ResponseBody
	@RequestMapping(value="getList",method=RequestMethod.GET)
	public List<WorkLog> getList() {
		workLogMng.listByParams(100, null, null, null, null, null);
		return null;
	}
	
}
