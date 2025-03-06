package com.jwzt.statistic.action.api;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jwzt.common.page.Pagination;
import com.jwzt.common.page.SimplePage;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.WorkingLog;
import com.jwzt.statistic.manager.WorkingLogManager;

@Controller
public class WorkingLogAct {
	
	private static final Logger log = LogManager.getLogger();
	@Autowired
	private WorkingLogManager workingLogManager;
	/**
	 * 新增日志
	 * @param systemId 系统ID
	 * @param modelId 模块ID
	 * @param contentId 内容ID
	 * @param content 内容
	 * @param contentName 内容名称
	 * @param userId 用户ID
	 * @param userName 用户名称
	 * @param terminal 终端
	 * @param mp
	 * @return
	 */
	@RequestMapping(value="/log/save",method=RequestMethod.POST)
	public String save(Integer systemId, Integer modelId,String contentId, String content, String contentName,
			Integer userId, String userName, String terminal,ModelMap mp) {
		try {
			log.debug("WorkingLogAct.save");
			System.out.println("modelId:"+modelId+",contentId:"+contentId+",content:"+content+",contentName:"+contentName+",userId:"+userId+",userName"+userName);
			WorkingLog workingLog = new WorkingLog();
			if(content!=null){
				content = URLDecoder.decode(content, "utf-8");
			}else{
				content = "";
			}
			workingLog.setContent(content);
			workingLog.setContentId(contentId);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String creatTime = format.format(new Date());
			workingLog.setCreateTime(Timestamp.valueOf(creatTime));
			workingLog.setModelId(modelId);
			workingLog.setSystemId(systemId);
			workingLog.setUserId(userId);
			if(userName!=null){
				userName = URLDecoder.decode(userName, "utf-8");
			}else{
				userName = "";
			}
			workingLog.setUserName(userName);
			if(contentName!=null){
				contentName = URLDecoder.decode(contentName, "utf-8");
			}else{
				contentName = "";
			}
			workingLog.setContentName(contentName);
			workingLog.setTerminal(terminal);
			workingLogManager.addWorkingLog(workingLog);
		} catch (Exception e) {
			log.debug("WorkingLogAct.save error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}
	/**
	 * 获取日志数据
	 * @param systemId 系统ID
	 * @param modelId 模块ID
	 * @param contentId 内容ID
	 * @param content 内容
	 * @param contentName 内容名称
	 * @param userId 用户ID
	 * @param userName 用户名称
	 * @param terminal 终端
	 * @param startTime 开始时间（时间段查询,如果结束时间为空时 以当前时间为结束时间）'yyyy-MM-dd HH:mm:ss'
	 * @param endTime   结束时间（时间段查询，如果开始时间为空时，获取结束时间之前所有数据）'yyyy-MM-dd HH:mm:ss'
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping(value="/log/list",method=RequestMethod.POST)
	public String getList(Integer systemId, Integer modelId,String contentId, String content, String contentName,
			Integer userId, String userName, String terminal,String startTime,String endTime,Integer pageNo,Integer pageSize,HttpServletRequest request,ModelMap mp) {
		try {
			log.debug("WorkingLogAct.list  获取日志信息");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = null;
			if(startTime!=null){
				startDate = format.parse(startTime);
			}
			Date endDate = null;
			if(endTime!=null){
				endDate = format.parse(endTime);
			}
			if(content!=null){
				content = URLDecoder.decode(content, "utf-8");
			}
			if(userName!=null){
				userName = URLDecoder.decode(userName, "utf-8");
			}
			if(contentName!=null){
				contentName = URLDecoder.decode(contentName, "utf-8");
			}
			Pagination pagination = workingLogManager.selectWorkingLogAll(systemId,modelId,contentId,content,contentName,userId,userName,terminal,startDate,endDate,SimplePage.checkPageNo(pageNo),pageSize);
			RenderJsonUtils.addSuccess(mp, pagination);
		} catch (Exception e) {
			log.debug("WorkingLogAct.list error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
		return "renderJson";
	}
	
}
