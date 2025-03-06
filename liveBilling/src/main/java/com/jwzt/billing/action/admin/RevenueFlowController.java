package com.jwzt.billing.action.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jwzt.billing.constants.RenderJsonConstants;
import com.jwzt.billing.entity.RevenueFlow;
import com.jwzt.billing.exception.FlowNotFoundException;
import com.jwzt.billing.exception.FlowSettlementStatusErrorException;
import com.jwzt.billing.exception.NotSameEnterpriseException;
import com.jwzt.billing.manager.RevenueFlowManager;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.page.SimplePage;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

/**
 * 收益流水
 * 
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value = "flow")
public class RevenueFlowController {

	@Autowired
	private RevenueFlowManager revenueFlowManager;

	/**
	 * 播管系统 获取收益记录
	 * 
	 * @param pageNo
	 *            页码
	 * @param settlementStatus
	 *            结算状态
	 * @param flowType
	 *            收益类型
	 * @param enterpriseContent
	 *            企业信息
	 * @param userContent
	 *            用户信息
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/page")
	public String page(Integer pageNo, Integer settlementStatus, Integer flowType, String enterpriseContent,
			String userContent, Date startTime, Date endTime, ModelMap map) {
		try {
			try {
				if (null != startTime) {
					startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
							.withMillisOfSecond(0).toDate();
				}
				if (null != endTime) {
					endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
							.withMillisOfSecond(999).toDate();
				}
				if (null != enterpriseContent) {
					enterpriseContent = new String(enterpriseContent.getBytes("iso-8859-1"), "utf-8");
				}
				if (null != userContent) {
					userContent = new String(userContent.getBytes("iso-8859-1"), "utf-8");
				}
			} catch (Exception e) {
			}
			// 收益流水数据
			Pagination pagination = revenueFlowManager.selectRevenueFlowByPage(SimplePage.checkPageNo(pageNo),
					settlementStatus, flowType, enterpriseContent, userContent, startTime, endTime);
			RenderJsonUtils.addSuccess(map, pagination);
		} catch (Exception e) {
			e.printStackTrace();
			RenderJsonUtils.addError(map, e.getMessage());
			return "renderJson";
		}
		return "renderJson";
	}

	@RequestMapping(value = "/listForSettlement")
	public String listForSettlement(Integer enterpriseId,
			@RequestParam(value = "revenueFlowIds", required = false) Long[] revenueFlowIds, Integer settlementStatus,
			Date startTime, Date endTime, ModelMap map) {
		try {
			List<RevenueFlow> list = new ArrayList<RevenueFlow>();
			if (null != revenueFlowIds && revenueFlowIds.length > 0) {
				list = revenueFlowManager.listRevenueFlowForSettlementByIds(enterpriseId, revenueFlowIds);
			} else if (null != enterpriseId) {
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
				list = revenueFlowManager.listRevenueFlowForSettlement(enterpriseId, settlementStatus, startTime,
						endTime);
			}
			RenderJsonUtils.addSuccess(map, list);
		} catch (NotSameEnterpriseException e) {
			List<Long> errorFlowIdList = e.getErrorFlowIdList();
			RenderJsonUtils.addError(map, RenderJsonConstants.CODE_FLOW_NOT_SAME_ENTERPRISE, "不是一个企业的流水",
					errorFlowIdList);
		} catch (FlowNotFoundException e) {
			List<Long> errorFlowIdList = e.getErrorFlowIdList();
			RenderJsonUtils.addError(map, RenderJsonConstants.CODE_FLOW_NOT_FOUND, "收益流水不存在", errorFlowIdList);
		} catch (FlowSettlementStatusErrorException e) {
			List<Long> errorFlowIdList = e.getErrorFlowIdList();
			RenderJsonUtils.addError(map, RenderJsonConstants.CODE_FLOW_SETTLED, "收益流水已结算", errorFlowIdList);
		} catch (Exception e) {
			RenderJsonUtils.addError(map, "结算异常");
		}
		return "renderJson";
	}
}
