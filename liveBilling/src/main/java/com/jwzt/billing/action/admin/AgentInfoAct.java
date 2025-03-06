package com.jwzt.billing.action.admin;

import static com.jwzt.common.page.SimplePage.checkPageNo;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jwzt.billing.entity.AgentInfo;
import com.jwzt.billing.manager.AgentInfoMng;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.web.CookieUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class AgentInfoAct {

	@RequestMapping(value = "/agent/page", method = RequestMethod.GET)
	public String page(String queryContent, Integer status, Date startTime, Date endTime, ModelMap mp,
			HttpServletRequest request, Integer pageNo) {
		String agentName = null;
		try {
			if (null != queryContent) {
				agentName = new String(queryContent.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
		} catch (Exception e) {
		}
		Pagination page = agentMng.pageByParams(agentName, status, startTime, endTime, checkPageNo(pageNo),
				CookieUtils.getPageSize(request));
		RenderJsonUtils.addSuccess(mp, page);
		return "renderJson";
	}

	@RequestMapping(value = "/agent/list", method = RequestMethod.GET)
	public String list(String queryContent, Integer status, Date startTime, Date endTime, ModelMap mp,
			HttpServletRequest request, Integer pageNo) {
		String agentName = null;
		try {
			if (null != queryContent) {
				agentName = new String(queryContent.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
		} catch (Exception e) {
		}
		List<AgentInfo> list = agentMng.listByParams(agentName, status, startTime, endTime);
		RenderJsonUtils.addSuccess(mp, list);
		return "renderJson";
	}

	@RequestMapping(value = "/agent/info", method = RequestMethod.GET)
	public String info(Integer id, ModelMap mp) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		AgentInfo bean = agentMng.getBeanById(id);
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/agent/save", method = RequestMethod.POST)
	public String save(AgentInfo bean, ModelMap mp, HttpServletRequest request) {
		if (null == bean) {
			RenderJsonUtils.addError(mp, "内容不能为空");
			return "renderJson";
		}
		String agentName = bean.getAgentName();
		if (null == agentName) {
			RenderJsonUtils.addError(mp, "代理商名称不能为空");
			return "renderJson";
		}
		agentMng.save(bean);
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/agent/update", method = RequestMethod.POST)
	public String update(AgentInfo bean, ModelMap mp, HttpServletRequest request) {
		if (null == bean) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		agentMng.update(bean);
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/agent/online", method = RequestMethod.POST)
	public String online(Integer id, ModelMap mp) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		AgentInfo bean = agentMng.getBeanById(id);
		if (null == bean) {
			RenderJsonUtils.addError(mp, "代理商不存在");
			return "renderJson";
		}
		Integer status = bean.getStatus();
		if (!(AgentInfo.STATUS_NEW.equals(status) || AgentInfo.STATUS_OFFLINE.equals(status))) {
			RenderJsonUtils.addError(mp, "代理商状态非法，不能生效");
			return "renderJson";
		}
		bean.setStatus(AgentInfo.STATUS_ONLINE);
		agentMng.update(bean);
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/agent/offline", method = RequestMethod.POST)
	public String offline(Integer id, ModelMap mp) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		AgentInfo bean = agentMng.getBeanById(id);
		if (null == bean) {
			RenderJsonUtils.addError(mp, "代理商不存在");
			return "renderJson";
		}
		Integer status = bean.getStatus();
		if (!AgentInfo.STATUS_ONLINE.equals(status)) {
			RenderJsonUtils.addError(mp, "代理商状态非法，不能失效");
			return "renderJson";
		}
		bean.setStatus(AgentInfo.STATUS_OFFLINE);
		agentMng.update(bean);
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@Autowired
	private AgentInfoMng agentMng;
}
