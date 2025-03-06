package com.jwzt.billing.action.ilive;

import static com.jwzt.common.page.SimplePage.checkPageNo;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jwzt.billing.entity.SettlementLog;
import com.jwzt.billing.entity.bo.UserBO;
import com.jwzt.billing.manager.SettlementLogMng;
import com.jwzt.billing.utils.SessionUtils;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.web.CookieUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class SettlementLogAct {

	@RequestMapping(value = "/settlement/page", method = RequestMethod.GET)
	public String page(String queryContent, @RequestParam(value = "status", required = false) Integer[] status,
			Integer invoiceStatus, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp,
			Integer pageNo) {
		UserBO user = SessionUtils.getUser(request);
		Pagination page;
		Integer enterpriseId = user.getEnterpriseId();
		if (null == enterpriseId) {
			page = new Pagination(checkPageNo(pageNo), CookieUtils.getPageSize(request), 0,
					new ArrayList<SettlementLog>());
		} else {
			String enterpriseName = null;
			try {
				if (null != queryContent) {
					enterpriseName = new String(queryContent.getBytes("iso-8859-1"), "utf-8");
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
			page = settlementLogMng.pageByParams(enterpriseId, enterpriseName, status, invoiceStatus, startTime,
					endTime, checkPageNo(pageNo), CookieUtils.getPageSize(request));
		}
		RenderJsonUtils.addSuccess(mp, page);
		return "renderJson";
	}

	@RequestMapping(value = "/settlement/info", method = RequestMethod.GET)
	public String info(Long id, HttpServletRequest request, ModelMap mp) {
		SettlementLog info = settlementLogMng.getBeanById(id);
		RenderJsonUtils.addSuccess(mp, info);
		return "renderJson";
	}

	@Autowired
	private SettlementLogMng settlementLogMng;
}
