package com.bvRadio.iLive.iLive.service;

import java.util.TimerTask;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.bvRadio.iLive.iLive.factory.MsgIdFactoryBean;
import com.bvRadio.iLive.iLive.factory.VisitorUserIdFactoryBean;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.iLive.util.TimerUtils;

public class MsgIdResetService implements ApplicationListener<ContextRefreshedEvent> {
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")) {
			new ApplicationContextUtil().setApplicationContext(event.getApplicationContext());
			// 订单编号重置定时器
			TimerUtils.startTimer(new TimerTask() {
				@Override
				public void run() {
					MsgIdFactoryBean.reset();
					VisitorUserIdFactoryBean.reset();
				}
			}, "00:00:00", "86400000");
		}
	}
}
