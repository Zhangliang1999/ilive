package com.bvRadio.iLive.iLive.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.bvRadio.iLive.iLive.timer.ILiveAppointmentNotifyJob;
import com.bvRadio.iLive.iLive.timer.TimerJob;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

public class AppointmentScheduler implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ILiveAppointmentNotifyJob job;

	@Autowired
	private TimerJob timerJob;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentScheduler.class);
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(20);
		if (ConfigUtils.get("openMessageSend").equals("1")) {
			if (event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")) {
				
				executorService.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						System.out.println("开始执行发短信线程");
						job.execute();
					}
				}, 0, 1, TimeUnit.MINUTES);
			}
		}
		
		
		//直播间定时查询
		if (ConfigUtils.get("open_check_startroom").equals("1") && 
				event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")) {
			String period = ConfigUtils.get("period");
			executorService.scheduleAtFixedRate(new Runnable() {
				
				@Override
				public void run() {
					timerJob.checkRoom();
				}
			}, 5, Integer.valueOf(period).intValue(), TimeUnit.SECONDS);
		}
		
	}

}
