package com.jwzt.statistic.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.jwzt.statistic.task.OnloadFileTask;
import com.jwzt.statistic.utils.ApplicationContextUtil;
import com.jwzt.statistic.utils.ConfigUtils;

public class StartService implements ApplicationListener<ContextRefreshedEvent> {
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if ("Root WebApplicationContext".equals(event.getApplicationContext().getDisplayName())) {
			new ApplicationContextUtil().setApplicationContext(event.getApplicationContext());
			Integer onload_day_hours = 3;
			try {
				onload_day_hours = Integer.valueOf(ConfigUtils.get("onload_day_hours"));
			} catch (Exception e) {
				System.out.println("=====================自定义定时错误采用默认时间见凌晨三点进行文件迁移=======================");
			}
			System.out.println("========================================日志文件迁移时间为，每天："+onload_day_hours+"点");
			long daySpan = 24 * 60 * 60 * 1000;
			Calendar calendar = Calendar.getInstance();  
	        calendar.set(Calendar.HOUR_OF_DAY, onload_day_hours); //凌晨3点  
	        calendar.set(Calendar.MINUTE, 0);  
	        calendar.set(Calendar.SECOND, 0);  
	        Date date=calendar.getTime(); //第一次执行定时任务的时间  
	        //如果第一次执行定时任务的时间 小于当前的时间  
	        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。  
	        if (date.before(new Date())) {  
	            Calendar startDT = Calendar.getInstance();  
	            startDT.setTime(date);  
	            startDT.add(Calendar.DAY_OF_MONTH, onload_day_hours);  
	            date =  startDT.getTime();  
	        } 
			OnloadFileTask task = new OnloadFileTask();
			Timer timer = new Timer();
			timer.schedule(task,date,daySpan); 
			//timer.schedule(task,20000,100000000);
		}
	}
}
