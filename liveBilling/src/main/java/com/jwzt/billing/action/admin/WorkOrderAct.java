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

import com.jwzt.billing.entity.WorkOrder;
import com.jwzt.billing.entity.bo.UserBO;
import com.jwzt.billing.entity.bo.WorkLog;
import com.jwzt.billing.manager.WorkLogMng;
import com.jwzt.billing.manager.WorkOrderMng;
import com.jwzt.billing.utils.SessionUtils;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.web.CookieUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class WorkOrderAct {

	@RequestMapping(value = "/workOrder/page", method = RequestMethod.GET)
	public String page(Integer enterpriseId, Integer workOrderType, Integer status, Date startTime, Date endTime,
			ModelMap mp, HttpServletRequest request, Integer pageNo) {
		try {
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
		Pagination page = workOrderMng.pageByParams(enterpriseId, workOrderType, status, startTime, endTime,
				checkPageNo(pageNo), CookieUtils.getPageSize(request), true);
		RenderJsonUtils.addSuccess(mp, page);
		return "renderJson";
	}

	@RequestMapping(value = "/workOrder/info", method = RequestMethod.GET)
	public String info(Long id, ModelMap mp) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		WorkOrder bean = workOrderMng.getBeanById(id, true);
		if (null != bean) {
			try {
				List<WorkLog> workLogList = workLogMng.listByParams(WorkLog.CURRENT_SYSTEM_ID, WorkLog.MODEL_ID_PAYMENT,
						String.valueOf(id), null, null, null);
				bean.setWorkLogList(workLogList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/workOrder/save", method = RequestMethod.POST)
	public String save(WorkOrder bean, ModelMap mp, HttpServletRequest request) {
		if (null == bean) {
			RenderJsonUtils.addError(mp, "内容不能为空");
			return "renderJson";
		}
		workOrderMng.save(bean);
		try {
			Long id = bean.getId();
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "新增工单";
			String content = bean.toString();
			WorkLog workLog = new WorkLog(WorkLog.MODEL_ID_PAYMENT, String.valueOf(id), content, contentName, userId,
					username, null);
			workLogMng.save(workLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/workOrder/process", method = RequestMethod.POST)
	public String process(Long id, ModelMap mp, HttpServletRequest request) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		WorkOrder bean = workOrderMng.getBeanById(id, false);
		if (null == bean) {
			RenderJsonUtils.addError(mp, "工单不存在");
			return "renderJson";
		}
		Integer status = bean.getStatus();
		if (!WorkOrder.STATUS_NEW.equals(status)) {
			RenderJsonUtils.addError(mp, "工单状态非法，不能变为处理中");
			return "renderJson";
		}
		workOrderMng.processById(id);
		try {
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "修改工单-处理中";
			String content = bean.toString();
			WorkLog workLog = new WorkLog(WorkLog.MODEL_ID_PAYMENT, String.valueOf(id), content, contentName, userId,
					username, null);
			workLogMng.save(workLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/workOrder/complete", method = RequestMethod.POST)
	public String complete(Long id, ModelMap mp, HttpServletRequest request) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		WorkOrder bean = workOrderMng.getBeanById(id, false);
		if (null == bean) {
			RenderJsonUtils.addError(mp, "工单不存在");
			return "renderJson";
		}
		Integer status = bean.getStatus();
		if (!WorkOrder.STATUS_PROCESSING.equals(status)) {
			RenderJsonUtils.addError(mp, "工单状态非法，不能完成");
			return "renderJson";
		}
		workOrderMng.completeById(id);
		try {
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "修改工单-完成";
			String content = bean.toString();
			WorkLog workLog = new WorkLog(WorkLog.MODEL_ID_PAYMENT, String.valueOf(id), content, contentName, userId,
					username, null);
			workLogMng.save(workLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/workOrder/cancel", method = RequestMethod.POST)
	public String cancel(Long id, String cancelReason, ModelMap mp, HttpServletRequest request) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		WorkOrder bean = workOrderMng.getBeanById(id, false);
		if (null == bean) {
			RenderJsonUtils.addError(mp, "工单不存在");
			return "renderJson";
		}
		Integer status = bean.getStatus();
		if (!(WorkOrder.STATUS_PROCESSING.equals(status) || WorkOrder.STATUS_NEW.equals(status))) {
			RenderJsonUtils.addError(mp, "工单状态非法，不能取消");
			return "renderJson";
		}
		workOrderMng.cancelById(id, cancelReason);
		try {
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "修改工单-取消";
			String content = bean.toString();
			WorkLog workLog = new WorkLog(WorkLog.MODEL_ID_PAYMENT, String.valueOf(id), content, contentName, userId,
					username, null);
			workLogMng.save(workLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@Autowired
	private WorkOrderMng workOrderMng;
	@Autowired
	private WorkLogMng workLogMng;
}
