package com.jwzt.livems.listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.jwzt.common.SomsConfigInfo;

/**
 * 监听发布点
 * @author 郭云飞
 *
 */
public class NotifyVodListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent event) {

	}

	public void contextInitialized(ServletContextEvent event) {
		String mstype = SomsConfigInfo.get("mstype");
		if(mstype==null){
			mstype="vod";
		}
		if(mstype.equals("vod")){
			//获取上次未完成的任务
				TaskThred taskThred = new TaskThred();
			 		taskThred.start();
					//开启监听发布点 线程
					NotifyVodThread notifyVodThread = new NotifyVodThread(); 
					 notifyVodThread.start();
					 //开启定时扫描 发布点新上传文件队列 定时器
					 ScheduledExecutorService schedule = Executors.newScheduledThreadPool(5);
					 for(int i=1;i<=5;i++){
						 schedule.scheduleAtFixedRate(new FileQueueTask(), 2*i,(long)(10*1), TimeUnit.SECONDS);
					 }
		}else{
			 //以下内容点播时注释掉
			 //开启定时更新虚拟直播m3u8文件更新任务 定时器
			 ScheduledExecutorService fileProgramSchedule = Executors.newScheduledThreadPool(1);
			 //每五秒执行一次
			 fileProgramSchedule.scheduleAtFixedRate(new FileProgramM3U8Task(), 5,(long)(10*1), TimeUnit.SECONDS);
			 //开启定时
			 new FileProgramTimer();
		}

		
	}

}
