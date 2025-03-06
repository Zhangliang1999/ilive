package com.jwzt.statistic.task;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.pool.MinuteStatisticPool;

/**
 * 检测哪些正在直播的房间，启动监控统计线程
 * @author gstars
 *
 */
public class SegmentStatisticTask extends TimerTask {

	private static final Logger log = LogManager.getLogger();

	public final static CopyOnWriteArraySet<Long> eventSet = new CopyOnWriteArraySet<>();
	
	public SegmentStatisticTask() {
		super();
	}

	@Override
	public void run() {
		try {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			while (null == context) {
				context = ContextLoader.getCurrentWebApplicationContext();
			}
			LiveInfoMng liveInfoMng = (LiveInfoMng) context.getBean("LiveInfoMng");
			List<LiveInfo> list = liveInfoMng.listNeedStatistic();
			for (LiveInfo liveInfo : list) {
				if (null != liveInfo) {
					checkThreadPool();
					Long liveEventId = liveInfo.getLiveEventId();
					if (String.valueOf(liveEventId).equals("10377")) {
						System.out.println("dd");
					}
					if (!eventSet.contains(liveEventId)) {
						eventSet.add(liveEventId);
						liveInfo.setStatisticing(true);
						liveInfoMng.update(liveInfo);
						// 启动分钟统计线程
						MinuteStatisticPool.execute(liveEventId);
					}
				}
			}
		} catch (Exception e) {
			log.error("SegmentStatisticTask error.", e);
		}
	}
	
	private synchronized void checkThreadPool() {
		int activeCount = MinuteStatisticPool.THREAD_POOL_EXECUTOR.getActiveCount();
		if (activeCount==0 &&!eventSet.isEmpty()) {
			eventSet.clear();
		}
	}
}