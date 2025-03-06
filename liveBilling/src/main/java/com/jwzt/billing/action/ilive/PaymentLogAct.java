package com.jwzt.billing.action.ilive;

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

import com.jwzt.billing.entity.PackageInfo;
import com.jwzt.billing.entity.PaymentLog;
import com.jwzt.billing.entity.bo.UserBO;
import com.jwzt.billing.entity.bo.WorkLog;
import com.jwzt.billing.manager.PackageInfoMng;
import com.jwzt.billing.manager.PaymentLogMng;
import com.jwzt.billing.manager.WorkLogMng;
import com.jwzt.billing.utils.SessionUtils;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.web.CookieUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class PaymentLogAct {

	@RequestMapping(value = "/payment/page", method = RequestMethod.GET)
	public String page(Integer paymentType, Integer status, Integer channelType, Date startTime, Date endTime,
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
		UserBO user = SessionUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		Pagination page = paymentMng.pageByParams(null,enterpriseId, paymentType, status, channelType, startTime, endTime,
				checkPageNo(pageNo), CookieUtils.getPageSize(request), true);
		RenderJsonUtils.addSuccess(mp, page);
		return "renderJson";
	}

	@RequestMapping(value = "/payment/info", method = RequestMethod.GET)
	public String info(Long id, HttpServletRequest request, ModelMap mp) {
		if (null == id) {
			RenderJsonUtils.addNullParamsError(mp, "id");
			return "renderJson";
		}
		PaymentLog bean = paymentMng.getBeanById(id, true);
		if (null == bean) {
			RenderJsonUtils.addNotExistBeanError(mp, "订单");
			return "renderJson";
		}
		UserBO user = SessionUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		if (null == enterpriseId || !enterpriseId.equals(bean.getEnterpriseId())) {
			RenderJsonUtils.addError(mp, "用户没有查看权限");
			return "renderJson";
		}
		try {
			List<WorkLog> workLogList = workLogMng.listByParams(WorkLog.CURRENT_SYSTEM_ID, WorkLog.MODEL_ID_PAYMENT,
					String.valueOf(id), null, null, null);
			bean.setWorkLogList(workLogList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/payment/save", method = RequestMethod.POST)
	public String save(PaymentLog bean, ModelMap mp, HttpServletRequest request) {
		if (null == bean) {
			RenderJsonUtils.addError(mp, "内容不能为空");
			return "renderJson";
		}
		Integer packageId = bean.getPackageId();
		if (null == packageId) {
			RenderJsonUtils.addNullParamsError(mp, "packageId");
			return "renderJson";
		}
		PackageInfo packageInfo = packageInfoMng.getBeanById(packageId, false);
		if (null == packageInfo) {
			RenderJsonUtils.addNullParamsError(mp, "packageId");
			return "renderJson";
		}
		Double officialPlatformPrice = packageInfo.getOfficialPlatformPrice();
		UserBO currentUser = SessionUtils.getUser(request);
		Long userId = currentUser.getId();
		String username = currentUser.getUsername();
		Integer enterpriseId = currentUser.getEnterpriseId();
		System.out.println("新增企业订单，企业id是："+enterpriseId);
		bean.setEnterpriseId(enterpriseId);
		bean.setChannelType(PaymentLog.CHANNEL_TYPE_OFFICIAL_PLATFORM);
		bean.setPaymentPrice(officialPlatformPrice);
		paymentMng.save(bean);
		try {
			Long id = bean.getId();
			String contentName = "新增订单";
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
	private PaymentLogMng paymentMng;
	@Autowired
	private PackageInfoMng packageInfoMng;
	@Autowired
	private WorkLogMng workLogMng;
}
