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

import com.jwzt.billing.entity.EnterpriseBilling;
import com.jwzt.billing.manager.EnterpriseBillingMng;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.web.CookieUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class EnterpriseBillingAct {

	@RequestMapping(value = "/enterprise/total", method = RequestMethod.GET)
	public String total(HttpServletRequest request, ModelMap mp) {
		EnterpriseBilling bean = enterpriseBillingMng.sumTotal();
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/enterprise/page", method = RequestMethod.GET)
	public String page(String queryContent, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp,
			Integer pageNo) {
		Integer enterpriseId = null;
		try {
			enterpriseId = Integer.parseInt(queryContent);
		} catch (Exception e) {
		}
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
		Pagination page = enterpriseBillingMng.pageByParams(enterpriseId, enterpriseName, startTime, endTime,
				checkPageNo(pageNo), CookieUtils.getPageSize(request), true);
		RenderJsonUtils.addSuccess(mp, page);
		return "renderJson";
	}

	@RequestMapping(value = "/enterprise/list", method = RequestMethod.GET)
	public String list(String queryContent, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		Integer enterpriseId = null;
		try {
			enterpriseId = Integer.parseInt(queryContent);
		} catch (Exception e) {
		}
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
		List<EnterpriseBilling> list = enterpriseBillingMng.listByParams(enterpriseId, enterpriseName, startTime,
				endTime, true);
		RenderJsonUtils.addSuccess(mp, list);
		return "renderJson";
	}

	@Autowired
	private EnterpriseBillingMng enterpriseBillingMng;
}
