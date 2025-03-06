package com.jwzt.statistic.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.common.utils.TimeUtils;
import com.jwzt.statistic.entity.EnterpriseInfo;
import com.jwzt.statistic.entity.VideoInfo;
import com.jwzt.statistic.manager.EnterpriseInfoMng;
import com.jwzt.statistic.manager.VideoInfoMng;
import com.jwzt.statistic.pool.EnterpriseStatisticPool;
import com.jwzt.statistic.pool.MeetingStatisticPool;
import com.jwzt.statistic.pool.TotalStatisticPool;
import com.jwzt.statistic.pool.VideoStatisticPool;
import com.jwzt.statistic.task.SegmentStatisticTask;

public class StartService implements ApplicationListener<ContextRefreshedEvent> {
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if ("Root WebApplicationContext".equals(event.getApplicationContext().getDisplayName())) {
			ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(3);
			SegmentStatisticTask task = new SegmentStatisticTask();
			//scheduledThreadPoolExecutor.scheduleAtFixedRate(task, 30, 60, TimeUnit.SECONDS);
            
			long dayCountdown = TimeUtils.getDayCountdown("00:10:00");
			ScheduledThreadPoolExecutor everyDayScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
			everyDayScheduledThreadPoolExecutor.scheduleAtFixedRate(new TimerTask() {
				private final Logger log = LogManager.getLogger();

				@Override
				public void run() {
					try {
						WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
						if (null == context) {
							throw new Exception("null context.");
						}
						EnterpriseInfoMng enterpriseInfoMng = (EnterpriseInfoMng) context.getBean("EnterpriseInfoMng");
						VideoInfoMng videoInfoMng = (VideoInfoMng) context.getBean("VideoInfoMng");
						Calendar calendar = Calendar.getInstance();
						calendar.add(Calendar.DAY_OF_YEAR, -1);
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						Date startTime = calendar.getTime();
						calendar.set(Calendar.HOUR_OF_DAY, 23);
						calendar.set(Calendar.MINUTE, 59);
						calendar.set(Calendar.SECOND, 59);
						Date endTime = calendar.getTime();
						TotalStatisticPool.execute(startTime, endTime);
						MeetingStatisticPool.execute(startTime, endTime);
						List<EnterpriseInfo> enterpriseInfoList = enterpriseInfoMng.listByParams(null, null, null, null,
								false);
						if (null != enterpriseInfoList) {
							for (EnterpriseInfo enterpriseInfo : enterpriseInfoList) {
								if (null != enterpriseInfo) {
									Integer enterpriseId = enterpriseInfo.getId();
									if (null != enterpriseId) {
										EnterpriseStatisticPool.execute(enterpriseId, startTime, endTime);
									}
								}
							}
						}
						List<VideoInfo> videoInfoList = videoInfoMng.listByParams(null, null, null, null, false);
						for (VideoInfo videoInfo : videoInfoList) {
							if (null != videoInfo) {
								Long videoId = videoInfo.getId();
								if (null != videoId) {
									VideoStatisticPool.execute(videoId, startTime, endTime);
								}
							}
						}
					} catch (Exception e) {
						log.error("everyday statistic error.", e);
					}

				}
			}, dayCountdown, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
		}
	}
}
