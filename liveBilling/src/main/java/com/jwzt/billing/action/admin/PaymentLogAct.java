package com.jwzt.billing.action.admin;

import static com.jwzt.common.page.SimplePage.checkPageNo;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
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
	public String page(String queryContent,Integer enterpriseId, Integer paymentType, Integer status, Integer channelType, Date startTime,
			Date endTime, ModelMap mp, HttpServletRequest request, Integer pageNo) {
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
		if(queryContent!=null){
			try {
				queryContent=new String(queryContent.trim().getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		Pagination page = paymentMng.pageByParams(queryContent,enterpriseId, paymentType, status, channelType, startTime, endTime,
				checkPageNo(pageNo), CookieUtils.getPageSize(request), true);
		RenderJsonUtils.addSuccess(mp, page);
		return "renderJson";
	}

	@RequestMapping(value = "/payment/info", method = RequestMethod.GET)
	public String info(Long id, ModelMap mp) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		PaymentLog bean = paymentMng.getBeanById(id, true);
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

	@RequestMapping(value = "/payment/save", method = RequestMethod.POST)
	public String save(PaymentLog bean, ModelMap mp, HttpServletRequest request) {
		if (null == bean) {
			RenderJsonUtils.addError(mp, "内容不能为空");
			return "renderJson";
		}
		//獲取套餐的周期
		Integer packageid=bean.getPackageId();
		PackageInfo packagebean = packageMng.getBeanById(packageid, false);
		Integer vaildDurationUnit =  packagebean.getVaildDurationUnit();
		Integer vaildDurationValue = packagebean.getVaildDurationValue();
		//計算套餐的截止日期
		Timestamp vaildEndTime=null;
		if (null != vaildDurationUnit && null != vaildDurationValue) {
			if (PackageInfo.VAILD_DURATION_UNIT_DAY.equals(vaildDurationUnit)) {
				vaildEndTime = new Timestamp(new DateTime(bean.getVaildStartTime()).plusDays(vaildDurationValue).getMillis());
			} else if (PackageInfo.VAILD_DURATION_UNIT_MONTH.equals(vaildDurationUnit)) {
				vaildEndTime = new Timestamp(new DateTime(bean.getVaildStartTime()).plusMonths(vaildDurationValue).getMillis());
			} else if (PackageInfo.VAILD_DURATION_UNIT_YEAR.equals(vaildDurationUnit)) {
				vaildEndTime = new Timestamp(new DateTime(bean.getVaildStartTime()).plusYears(vaildDurationValue).getMillis());
			}
		}
		bean.setVaildEndTime(vaildEndTime);
		paymentMng.save(bean);
		try {
			Long id = bean.getId();
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
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

	@RequestMapping(value = "/payment/update", method = RequestMethod.POST)
	public String update(Long id, Integer agentId, Integer sellUserId, Boolean paid, Integer paymentWay,Integer paymentAuto,
			String paymentFlowId, Timestamp paymentTime, Double paymentPrice, Double agentDeductionRate,String paymentDesc, ModelMap mp,
			HttpServletRequest request) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		PaymentLog oldBean = paymentMng.getBeanById(id, false);
		if (null == oldBean) {
			RenderJsonUtils.addError(mp, "订单不存在");
			return "renderJson";
		}
		Integer status = oldBean.getStatus();
		if (!(PaymentLog.STATUS_NEW.equals(status) || PaymentLog.STATUS_PROCESSING.equals(status))) {
			RenderJsonUtils.addError(mp, "订单状态非法，不能修改");
			return "renderJson";
		}
		PaymentLog bean = paymentMng.update(id, agentId, sellUserId, paid, paymentWay, paymentFlowId, paymentTime,
				paymentPrice, agentDeductionRate,paymentDesc,paymentAuto);
		try {
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "修改订单";
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

	@RequestMapping(value = "/payment/process", method = RequestMethod.POST)
	public String process(Long id, ModelMap mp, HttpServletRequest request) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		PaymentLog bean = paymentMng.getBeanById(id, false);
		if (null == bean) {
			RenderJsonUtils.addError(mp, "订单不存在");
			return "renderJson";
		}
		Integer status = bean.getStatus();
		if (!PaymentLog.STATUS_NEW.equals(status)) {
			RenderJsonUtils.addError(mp, "订单状态非法，不能变为处理中");
			return "renderJson";
		}
		paymentMng.processById(id);
		try {
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "修改订单-处理中";
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

	@RequestMapping(value = "/payment/complete", method = RequestMethod.POST)
	public String complete(Long id, ModelMap mp, HttpServletRequest request) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		PaymentLog bean = paymentMng.getBeanById(id, false);
		if (null == bean) {
			RenderJsonUtils.addError(mp, "订单不存在");
			return "renderJson";
		}
		Integer status = bean.getStatus();
		if (!PaymentLog.STATUS_PROCESSING.equals(status)) {
			RenderJsonUtils.addError(mp, "订单状态非法，不能完成");
			return "renderJson";
		}
		UserBO currentUser = SessionUtils.getUser(request);
		Integer certStatus=currentUser.getCertStatus();
		//获取订单生效日期
		PaymentLog paymentbean = paymentMng.getBeanById(id, false);
		Timestamp startTime=paymentbean.getVaildStartTime();
		paymentMng.completeById(id,certStatus,startTime);
		try {
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "修改订单-完成";
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
	/**
	 * 和营业厅对接，当所有环节验证通过后，执行这个方法
	 * @param bean
	 * @param mp
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/payment/yytcomplete", method = RequestMethod.POST)
	public String yytComplete(String enterpriseId,String packageId,String paymentPrice,String contactName,String contactNumber,String CustID,String startTime, ModelMap mp, HttpServletRequest request) {
		System.out.println("开始调用营业厅购买接口！");
		System.out.println("购买套餐id：======"+packageId);
		PaymentLog bean=new PaymentLog();
		bean.setEnterpriseId(Integer.parseInt(enterpriseId));
		bean.setPackageId(Integer.parseInt(packageId));
		bean.setPaymentPrice(Double.parseDouble(paymentPrice));
		bean.setContactName(contactName);
		bean.setContactNumber(contactNumber);
		bean.setCustID(CustID);
		bean.setChannelType(3);
		bean.setPaymentType(1);
		bean.setPaymentWay(5);
		bean.setStatus(3);
		bean.setPaymentAuto(PaymentLog.PAYMENTAUTO_YES);
		if(startTime==null){
			bean.setVaildStartTime(new Timestamp(System.currentTimeMillis()));
		}else{
			bean.setVaildStartTime(Timestamp.valueOf(startTime));
		}
		PackageInfo packagebean = packageMng.getBeanById(Integer.parseInt(packageId), false);
		Integer vaildDurationUnit =  packagebean.getVaildDurationUnit();
		Integer vaildDurationValue = packagebean.getVaildDurationValue();
		Timestamp vaildEndTime=null;
		if (null != vaildDurationUnit && null != vaildDurationValue) {
			if (PackageInfo.VAILD_DURATION_UNIT_DAY.equals(vaildDurationUnit)) {
				vaildEndTime = new Timestamp(new DateTime(bean.getVaildStartTime()).plusDays(vaildDurationValue).getMillis());
			} else if (PackageInfo.VAILD_DURATION_UNIT_MONTH.equals(vaildDurationUnit)) {
				vaildEndTime = new Timestamp(new DateTime(bean.getVaildStartTime()).plusMonths(vaildDurationValue).getMillis());
			} else if (PackageInfo.VAILD_DURATION_UNIT_YEAR.equals(vaildDurationUnit)) {
				vaildEndTime = new Timestamp(new DateTime(bean.getVaildStartTime()).plusYears(vaildDurationValue).getMillis());
			}
		}
		bean.setVaildEndTime(vaildEndTime);
		paymentMng.save(bean);
		paymentMng.yytcompleteById(bean,4);
		try {
			Long userId = 1263L;
			String username = "admin";
			String contentName = "营业厅订单-完成";
			String content = bean.toString();
			WorkLog workLog = new WorkLog(WorkLog.MODEL_ID_PAYMENT, String.valueOf(bean.getId()), content, contentName, userId,
					username, null);
			workLogMng.save(workLog);
			System.out.println("保存完营业厅订单日志！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		RenderJsonUtils.addSuccess(mp,1, bean);
		return "renderJson";
	}
	@RequestMapping(value = "/payment/yytupdate", method = RequestMethod.POST)
	public String yytBillingUpdat(String enterpriseId,ModelMap mp,HttpServletRequest request){
		System.out.println("开始调用营业厅修改是否自动续订接口！");
		//查询
		List<PaymentLog> list = paymentMng.listByParams(Integer.parseInt(enterpriseId), null, null, null, null, null, false);
		if(list.size()>0&&list!=null){
			PaymentLog paymentLog=list.get(0);
			Long time=paymentLog.getVaildEndTime().getTime()-System.currentTimeMillis();
			Long twoDay=(long) (1000*60*60*24*2);
			if(time<twoDay){
				RenderJsonUtils.addError(mp, "距离有效期结束两天内不允许修改！");
			}else{
				
				paymentLog.setPaymentAuto(PaymentLog.PAYMENTAUTO_NO);
				paymentMng.update(paymentLog);
				RenderJsonUtils.addSuccess(mp,1, paymentLog);
			}
		}else{
			RenderJsonUtils.addError(mp, "此账户下无订单需要修改！");
		}
		return "renderJson";	
	}
	@RequestMapping(value = "/payment/cancel", method = RequestMethod.POST)
	public String cancel(Long id, String cancelReason, ModelMap mp, HttpServletRequest request) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		PaymentLog bean = paymentMng.getBeanById(id, false);
		if (null == bean) {
			RenderJsonUtils.addError(mp, "订单不存在");
			return "renderJson";
		}
		Integer status = bean.getStatus();
		if (!(PaymentLog.STATUS_PROCESSING.equals(status) || PaymentLog.STATUS_NEW.equals(status))) {
			RenderJsonUtils.addError(mp, "订单状态非法，不能取消");
			return "renderJson";
		}
		paymentMng.cancelById(id, cancelReason);
		try {
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "修改订单-取消";
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
	private WorkLogMng workLogMng;
	@Autowired
	private PackageInfoMng packageMng;
}
