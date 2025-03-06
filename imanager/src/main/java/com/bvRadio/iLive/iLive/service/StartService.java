package com.bvRadio.iLive.iLive.service;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;

public class StartService implements ApplicationListener<ContextRefreshedEvent> {
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")) {
//			System.out.println("启动socket 发送消息任务");
			new ApplicationContextUtil().setApplicationContext(event.getApplicationContext());
//			MinaServer.runServer();
//			SendMessageTask sendMessageTask = new SendMessageTask();
//			TimerUtils.startTimer(sendMessageTask, "500");
//			//消息审核定时器
//			
//			//消息审核定时器
//			AuditMessageTimer auditMessageTimer = new AuditMessageTimer();
//			TimerUtils.startTimer(auditMessageTimer, "1000");
		}
	}
}
