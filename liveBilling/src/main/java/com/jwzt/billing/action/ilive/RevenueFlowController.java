package com.jwzt.billing.action.ilive;

import static com.jwzt.common.page.SimplePage.checkPageNo;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.billing.entity.SettlementLog;
import com.jwzt.billing.entity.bo.UserBO;
import com.jwzt.billing.manager.RevenueFlowManager;
import com.jwzt.billing.utils.SessionUtils;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.web.CookieUtils;
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
			String userContent, Date startTime, Date endTime, HttpServletRequest request, ModelMap map) {
		try {
			UserBO user = SessionUtils.getUser(request);
			Pagination page;
			Integer enterpriseId = user.getEnterpriseId();
			if (null == enterpriseId) {
				page = new Pagination(checkPageNo(pageNo), CookieUtils.getPageSize(request), 0,
						new ArrayList<SettlementLog>());
			} else {
				String enterpriseName = null;
				Long userId = null;
				String userName = null;
				try {
					if (null != startTime) {
						startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
								.withMillisOfSecond(0).toDate();
					}
					if (null != endTime) {
						endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
								.withMillisOfSecond(999).toDate();
					}
					if (StringUtils.isNotBlank(enterpriseContent)) {
						enterpriseName = new String(enterpriseContent.getBytes("iso-8859-1"), "utf-8");
					}
					if (StringUtils.isNotBlank(userContent)) {
						userName = new String(userContent.getBytes("iso-8859-1"), "utf-8");
						try {
							userId = Long.parseLong(userContent);
						} catch (Exception e) {
						}
					}
				} catch (Exception e) {
				}
				// 收益流水数据
				page = revenueFlowManager.pageByParams(settlementStatus, flowType, null, enterpriseId, enterpriseName, userId,
						userName, startTime, endTime, checkPageNo(pageNo), CookieUtils.getPageSize(request));
			}
			RenderJsonUtils.addSuccess(map, page);
		} catch (Exception e) {
			e.printStackTrace();
			RenderJsonUtils.addError(map, e.getMessage());
			return "renderJson";
		}
		return "renderJson";
	}
}
