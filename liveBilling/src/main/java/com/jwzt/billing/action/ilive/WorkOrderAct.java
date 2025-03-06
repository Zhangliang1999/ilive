package com.jwzt.billing.action.ilive;

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
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class WorkOrderAct {

	@RequestMapping(value = "/workOrder/list", method = RequestMethod.GET)
	public String list(Integer workOrderType, Integer status, Date startTime, Date endTime, ModelMap mp,
			HttpServletRequest request) {
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
		UserBO user = SessionUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		List<WorkOrder> list = workOrderMng.listByParams(enterpriseId, workOrderType, status, startTime, endTime, true);
		RenderJsonUtils.addSuccess(mp, list);
		return "renderJson";
	}

	@RequestMapping(value = "/workOrder/save", method = RequestMethod.POST)
	public String save(WorkOrder bean, ModelMap mp, HttpServletRequest request) {
		if (null == bean) {
			RenderJsonUtils.addError(mp, "内容不能为空");
			return "renderJson";
		}
		UserBO user = SessionUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		bean.setEnterpriseId(enterpriseId);
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

	@Autowired
	private WorkOrderMng workOrderMng;
	@Autowired
	private WorkLogMng workLogMng;
}
