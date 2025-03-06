package com.jwzt.billing.action.api;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jwzt.billing.entity.RevenueFlow;
import com.jwzt.billing.manager.RevenueFlowManager;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.page.SimplePage;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
/**
 * 收益流水
 * @author YanXL
 *
 */
@Controller
public class ApiRevenueFlowController {
	@Autowired
	private RevenueFlowManager revenueFlowManager;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 新增流水
	 * @param flowPrice 金额
	 * @param flowType 收益类型
	 * @param userId 用户ID
	 * @param userName 用户名
	 * @param enterpriseId 企业ID
	 * @param enterpriseName 企业名
	 * @param roomId 直播房间ID
	 * @param evenId 直播场次ID
	 * @param flowDesc 收益事项
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/add/flow.jspx",method=RequestMethod.POST)
	public String addRevenueFlow(Double flowPrice, Integer flowType, Long flagId, Long userId, String userName,
			String mobile, Integer enterpriseId, String enterpriseName, Integer roomId, Long evenId, String flowDesc,
			ModelMap mp){
		System.out.println("新增流水收益，访问 ");
		try {
			RevenueFlow revenueFlow = new RevenueFlow();
			revenueFlow.setEnterpriseId(enterpriseId);
			revenueFlow.setEnterpriseName(URLDecoder.decode(enterpriseName, "utf-8"));
			revenueFlow.setEvenId(evenId);
			revenueFlow.setFlowBalance(0.0);
			revenueFlow.setFlowDesc(URLDecoder.decode(flowDesc, "utf-8"));
			revenueFlow.setFlowPrice(flowPrice);
			revenueFlow.setFlowTime(Timestamp.valueOf(format.format(new Date())));
			revenueFlow.setFlowType(flowType);
			revenueFlow.setFlagId(flagId);
			revenueFlow.setPlatformPrice("");
			revenueFlow.setRoomId(roomId);
			revenueFlow.setSettlementStatus(RevenueFlow.SETTLEMENT_STATUS_NO);
			revenueFlow.setUserId(userId);
			revenueFlow.setUserName(URLDecoder.decode(userName, "utf-8"));
			revenueFlow.setMobile(mobile);
			//新增流水记录
			revenueFlowManager.addRevenueFlow(revenueFlow);
			//修改企业累计收益
		} catch (Exception e) {
			System.out.println("新增流水收益 ： "+e.toString());
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/flow/pageByFlagId.jspx", method = RequestMethod.GET)
	public String pageByFlagId(Integer flowType, Long flagId, Date startTime, Date endTime, Integer pageNo,
			Integer pageSize, ModelMap mp) {
		try {
			if (null == pageSize) {
				pageSize = 20;
			}
			if (pageSize > 200) {
				pageSize = 200;
			}
			if (null == flagId || null == flowType) {
				Pagination page = new Pagination(SimplePage.checkPageNo(pageNo), pageSize, 0, new ArrayList());
				RenderJsonUtils.addSuccess(mp, page);
				return "renderJson";
			}
			Pagination page = revenueFlowManager.pageByParams(null, flowType, flagId, null, null, null, null, startTime,
					endTime, SimplePage.checkPageNo(pageNo), pageSize);
			RenderJsonUtils.addSuccess(mp, page);
			return "renderJson";
		} catch (Exception e) {
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@RequestMapping(value = "/flow/listByFlagId.jspx", method = RequestMethod.GET)
	public String listByFlagId(Integer flowType, Long flagId, Date startTime, Date endTime, Integer pageNo,
			Integer pageSize, ModelMap mp) {
		try {
			if (null == pageSize) {
				pageSize = 20;
			}
			if (pageSize > 200) {
				pageSize = 200;
			}
			if (null == flagId || null == flowType) {
				List<RevenueFlow> list = new ArrayList<RevenueFlow>();
				RenderJsonUtils.addSuccess(mp, list);
				return "renderJson";
			}
			List<RevenueFlow> list = revenueFlowManager.listByParams(null, flowType, flagId, null, null, null, null,
					startTime, endTime);
			RenderJsonUtils.addSuccess(mp, list);
			return "renderJson";
		} catch (Exception e) {
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}
	
	
}
