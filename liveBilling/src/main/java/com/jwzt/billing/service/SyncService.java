package com.jwzt.billing.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


import com.jwzt.billing.task.EnterpriseSyncTask;
import com.jwzt.billing.task.PaymentAutoTask;
import com.jwzt.billing.utils.TimerUtils;
import com.jwzt.common.utils.TimeUtils;

public class SyncService implements ApplicationListener<ContextRefreshedEvent> {
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if ("Root WebApplicationContext".equals(event.getApplicationContext().getDisplayName())) {
			long dayCountdown = TimeUtils.getDayCountdown("23:30:00");
			ScheduledThreadPoolExecutor everyDayScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
			EnterpriseSyncTask enterpriseSyncTask = new EnterpriseSyncTask();
			everyDayScheduledThreadPoolExecutor.scheduleAtFixedRate(enterpriseSyncTask, dayCountdown,
					24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
			
			long autoCountdown = TimeUtils.getDayCountdown("23:00:00");
			ScheduledThreadPoolExecutor everyDayautoScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
			PaymentAutoTask paymentAutoTask=new PaymentAutoTask();
			everyDayautoScheduledThreadPoolExecutor.scheduleAtFixedRate(paymentAutoTask, autoCountdown,
					24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
			
		}
	}
}
