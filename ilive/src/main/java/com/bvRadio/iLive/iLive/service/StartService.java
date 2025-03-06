package com.bvRadio.iLive.iLive.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.bvRadio.iLive.iLive.task.AuditMessageTimer;
import com.bvRadio.iLive.iLive.task.FlushallTask;
import com.bvRadio.iLive.iLive.task.ILiveStaticUserTask;
import com.bvRadio.iLive.iLive.task.MediaDeleteTask;
import com.bvRadio.iLive.iLive.task.MediaTask;
import com.bvRadio.iLive.iLive.task.PayOrderTask;
import com.bvRadio.iLive.iLive.task.SendMessageTask;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.iLive.util.TimeUtils;
import com.bvRadio.iLive.iLive.util.TimerUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.mina.MinaServer;

public class StartService implements ApplicationListener<ContextRefreshedEvent> {
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")) {
			new ApplicationContextUtil().setApplicationContext(event.getApplicationContext());
			// System.out.println("启动socket 发送消息任务");
			if(ConfigUtils.get("socketSwitch").equals("1")) {
				MinaServer.runServer();
				SendMessageTask sendMessageTask = new SendMessageTask();
				TimerUtils.startTimer(sendMessageTask, "500");
				//消息审核定时器
				
				//消息审核定时器
				AuditMessageTimer auditMessageTimer = new AuditMessageTimer();
				TimerUtils.startTimer(auditMessageTimer, "1000");
				
				//订单处理器
				PayOrderTask payOrderTask = new PayOrderTask();
				TimerUtils.startTimer(payOrderTask, "1000");
				
				// 用户在线数计时器
				ILiveStaticUserTask userTask = new ILiveStaticUserTask();
				TimerUtils.startTimer(userTask, "80000");
				
				// 断流直播间清定时器
				
				//媒体文件存储定时器
				long dayCountdown = TimeUtils.getDayCountdown("23:30:00");
				ScheduledThreadPoolExecutor everyDayScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
				MediaTask mediaTask = new MediaTask();
				everyDayScheduledThreadPoolExecutor.scheduleAtFixedRate(mediaTask, dayCountdown,
						24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
//				TimerUtils.startTimer(mediaTask, "43200000");
				//暂存媒体文件删除定时器
				long dayCountdown1 = TimeUtils.getDayCountdown("23:59:00");
				MediaDeleteTask mediaDeleteTask = new MediaDeleteTask();
				everyDayScheduledThreadPoolExecutor.scheduleAtFixedRate(mediaDeleteTask, dayCountdown1,
						24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
				//定时清除redis
				long flushalldb = TimeUtils.getDayCountdown("04:00:00");
				FlushallTask flushallTask = new FlushallTask();
				everyDayScheduledThreadPoolExecutor.scheduleAtFixedRate(flushallTask, flushalldb,
						24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
//				TimerUtils.startTimer(mediaDeleteTask, "72000000");
			}
		}
	}
}
