package com.jwzt.statistic.service;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.task.EnterpriseSyncTask;
import com.jwzt.statistic.task.IPUpdateTask;
import com.jwzt.statistic.task.VideoSyncTask;

public class SyncService implements ApplicationListener<ContextRefreshedEvent> {
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if ("Root WebApplicationContext".equals(event.getApplicationContext().getDisplayName())) {
			ScheduledThreadPoolExecutor everyDayScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
			//VideoSyncTask videoSyncTask = new VideoSyncTask();
			//everyDayScheduledThreadPoolExecutor.scheduleAtFixedRate(videoSyncTask, 10, 60, TimeUnit.SECONDS);
			ScheduledThreadPoolExecutor everyDayScheduledThreadPoolExecutor2 = new ScheduledThreadPoolExecutor(2);
			EnterpriseSyncTask enterpriseSyncTask = new EnterpriseSyncTask();
			everyDayScheduledThreadPoolExecutor2.scheduleAtFixedRate(enterpriseSyncTask, 10, 5*3600,
					TimeUnit.SECONDS);
			
			ScheduledThreadPoolExecutor ipExecutor = new ScheduledThreadPoolExecutor(10);
			//因为做了本地IP库管理关闭补IP功能
			//ipExecutor.scheduleWithFixedDelay(new IPUpdateTask(),10,12,TimeUnit.HOURS);
			
			ExecutorService service = Executors.newSingleThreadExecutor();
			service.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(4000);
						WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
						RequestLogMng requestLogMng = (RequestLogMng) context.getBean("RequestLogMng");
						long startTime = new Date("2019-01-22 13:53:12").getTime();
						long endTime = new Date("2019-01-22 19:53:12").getTime();
						while (startTime<endTime) {
							Integer enter = requestLogMng.getNumByEvent(10252l, 1);
							Integer leave = requestLogMng.getNumByEvent(10252l, 2);
							System.out.println(enter - leave);
							startTime+=1000*60*30;
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			});
		}
	}
}
