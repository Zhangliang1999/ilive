package com.jwzt.billing.action.admin;

import static com.jwzt.common.page.SimplePage.checkPageNo;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jwzt.billing.constants.RenderJsonConstants;
import com.jwzt.billing.entity.SettlementLog;
import com.jwzt.billing.exception.ErrorPlatformRateException;
import com.jwzt.billing.exception.FlowNotFoundException;
import com.jwzt.billing.exception.FlowSettlementStatusErrorException;
import com.jwzt.billing.exception.NotSameEnterpriseException;
import com.jwzt.billing.manager.SettlementLogMng;
import com.jwzt.common.entity.Config;
import com.jwzt.common.manager.ConfigMng;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.web.CookieUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class SettlementLogAct {

	private final static Logger log = LogManager.getLogger();

	@RequestMapping(value = "/settlement/page", method = RequestMethod.GET)
	public String page(String queryContent, @RequestParam(value = "status", required = false) Integer[] status,
			Integer invoiceStatus, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp,
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

		Pagination page = settlementLogMng.pageByParams(enterpriseId, enterpriseName, status, invoiceStatus, startTime,
				endTime, checkPageNo(pageNo), CookieUtils.getPageSize(request));
		RenderJsonUtils.addSuccess(mp, page);
		return "renderJson";
	}

	@RequestMapping(value = "/settlement/info", method = RequestMethod.GET)
	public String info(Long id, HttpServletRequest request, ModelMap mp) {
		SettlementLog info = settlementLogMng.getBeanById(id);
		RenderJsonUtils.addSuccess(mp, info);
		return "renderJson";
	}

	@RequestMapping(value = "/settlement/save", method = RequestMethod.POST)
	public String save(Integer enterpriseId, String enterpriseName,
			@RequestParam(value = "revenueFlowIds", required = true) Long[] revenueFlowIds, HttpServletRequest request,
			ModelMap mp) {
		try {
			String rateStr = configMng.getValue(Config.PLATFORM_RATE);
			double platformRate = Double.parseDouble(rateStr);
			SettlementLog settlementLog = settlementLogMng.save(enterpriseId, enterpriseName, revenueFlowIds,
					platformRate);
			RenderJsonUtils.addSuccess(mp, settlementLog);
		} catch (NotSameEnterpriseException e) {
			List<Long> errorFlowIdList = e.getErrorFlowIdList();
			RenderJsonUtils.addError(mp, RenderJsonConstants.CODE_FLOW_NOT_SAME_ENTERPRISE, "不是一个企业的流水",
					errorFlowIdList);
		} catch (ErrorPlatformRateException e) {
			log.info("SettlementLogAct save error.", e);
			RenderJsonUtils.addError(mp, "平台费率异常");
		} catch (FlowNotFoundException e) {
			log.info("SettlementLogAct save error.", e);
			List<Long> errorFlowIdList = e.getErrorFlowIdList();
			RenderJsonUtils.addError(mp, RenderJsonConstants.CODE_FLOW_NOT_FOUND, "收益流水不存在", errorFlowIdList);
		} catch (FlowSettlementStatusErrorException e) {
			log.info("SettlementLogAct save error.", e);
			List<Long> errorFlowIdList = e.getErrorFlowIdList();
			RenderJsonUtils.addError(mp, RenderJsonConstants.CODE_FLOW_SETTLED, "收益流水已结算", errorFlowIdList);
		} catch (Exception e) {
			log.info("SettlementLogAct save error.", e);
			RenderJsonUtils.addError(mp, "结算异常");
		}
		return "renderJson";
	}

	@RequestMapping(value = "/settlement/cancel", method = RequestMethod.POST)
	public String cancel(Long id, HttpServletRequest request, ModelMap mp) {
		try {
			SettlementLog settlementLog = settlementLogMng.cancelById(id);
			RenderJsonUtils.addSuccess(mp, settlementLog);
		} catch (Exception e) {
			log.info("SettlementLogAct cancel error.", e);
			RenderJsonUtils.addError(mp, "结算单作废异常");
		}
		return "renderJson";
	}
	@RequestMapping(value = "/settlement/update", method = RequestMethod.POST)
	public String update(Long id,Integer invoiceStatus, HttpServletRequest request, ModelMap mp) {
		try {
			SettlementLog settlementLog = settlementLogMng.updateInvoiceStatus(id, invoiceStatus);
			RenderJsonUtils.addSuccess(mp, settlementLog);
		} catch (Exception e) {
			log.info("SettlementLogAct cancel error.", e);
			RenderJsonUtils.addError(mp, "结算单作废异常");
		}
		return "renderJson";
	}

	@Autowired
	private SettlementLogMng settlementLogMng;
	@Autowired
	private ConfigMng configMng;
}
