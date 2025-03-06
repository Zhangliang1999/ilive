package com.bvRadio.iLive.iLive.service;

import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.bvRadio.iLive.iLive.manager.ILiveConvertTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.timer.ConvertTaskTimer;
import com.bvRadio.iLive.iLive.util.TimerUtils;

//开启所有定时器
public class StartTimer implements ApplicationListener<ContextRefreshedEvent> {
	
	
	@Autowired
	private ILiveConvertTaskMng iLiveConvertTaskMng;
	
	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//检测转码程序自动完成转码状态
		ConvertTaskTimer convertTaskTimer = new ConvertTaskTimer(iLiveConvertTaskMng , iLiveMediaFileMng, iLiveServerAccessMethodMng);
		TimerUtils.startTimer(convertTaskTimer, "30000");
	}
	
}
