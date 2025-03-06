package com.jwzt.billing.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.billing.entity.PaymentLog;
import com.jwzt.billing.manager.PaymentLogMng;

/**
 * 套餐自动续订定时器
 * 
 * @author Administrator
 *
 */
public class PaymentAutoTask extends TimerTask {
	private static final Integer SYNC_MODE_AUTO = 1;
	private Integer autoMode;

	ExecutorService newFixedThreadPool = null;
	public PaymentAutoTask() {
		super();
		autoMode = SYNC_MODE_AUTO;
		newFixedThreadPool = Executors.newFixedThreadPool(10);
	}
	@Override
	public void run() {
		try {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			if (null == context) {
				context = ContextLoader.getCurrentWebApplicationContext();
			}
			log.info("开始测试自动续订线程");
			log.debug("PaymentAutoTask run. autoMode={}", autoMode);
			if(SYNC_MODE_AUTO.equals(autoMode)){
				PaymentLogMng paymentMng = (PaymentLogMng) context.getBean("PaymentLogMng");
				List<PaymentLog> list = paymentMng.listByParamsByAuto(PaymentLog.PAYMENTAUTO_YES);
				if(list.size()>0){
					runnableMedia(list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static final Logger log = LoggerFactory.getLogger(PaymentAutoTask.class);
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private void runnableMedia(final List<PaymentLog> list) {
		log.info("开始执行自动续订套餐线程！");
		newFixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
					if (null == context) {
						context = ContextLoader.getCurrentWebApplicationContext();
					}
					PaymentLogMng paymentMng = (PaymentLogMng) context.getBean("PaymentLogMng");
					for(PaymentLog paymentLog:list){
						Long time=paymentLog.getVaildEndTime().getTime()-System.currentTimeMillis();
						Long onyDay=(long) (1000*60*60*24);
						if(time<=onyDay){
							PaymentLog newlog=new PaymentLog();
							newlog.setEnterpriseId(paymentLog.getEnterpriseId());
							newlog.setChannelType(paymentLog.getChannelType());
							newlog.setAgentDeductionRate(paymentLog.getAgentDeductionRate());
							newlog.setAgentId(paymentLog.getAgentId());
							newlog.setAgentInfo(paymentLog.getAgentInfo());
							newlog.setContactName(paymentLog.getContactName());
							newlog.setContactNumber(paymentLog.getContactNumber());
							newlog.setCreateTime(new Timestamp(System.currentTimeMillis()));
							newlog.setDiscountRate(paymentLog.getDiscountRate());
							newlog.setEnterpriseBilling(paymentLog.getEnterpriseBilling());
							newlog.setAgentDeductionRate(paymentLog.getAgentDeductionRate());
							newlog.setOriginalPayment(paymentLog.getOriginalPayment());
							newlog.setPackageId(paymentLog.getPackageId());
							newlog.setPackageInfo(paymentLog.getPackageInfo());
							newlog.setPaid(true);
							newlog.setPaymentDesc(paymentLog.getPaymentDesc());
							newlog.setPaymentFlowId(paymentLog.getPaymentFlowId());
							newlog.setPaymentPrice(paymentLog.getPaymentPrice());
							newlog.setPaymentTime(new Timestamp(System.currentTimeMillis()));
							newlog.setPaymentType(paymentLog.getPaymentType());
							newlog.setPaymentWay(paymentLog.getPaymentWay());
							newlog.setSellUserId(paymentLog.getSellUserId());
							newlog.setVaildStartTime(paymentLog.getVaildEndTime());
							paymentMng.save(newlog);
							paymentMng.completeByAuto(newlog, 4, paymentLog.getVaildEndTime());
						}
					}
				
				} catch (Exception e) {
					log.error("文件处理异常 ："+e.toString());
				}
			}
		});
	}
}
