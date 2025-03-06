package com.jwzt.statistic.pool;
/**
* @author ysf
*/

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jwzt.statistic.task.LiveEndStatisticTask;

public class LiveEndStatisticPool {

	private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(3, 100, 0L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	public static void execute(Long liveEventId) {
		LiveEndStatisticTask task = new LiveEndStatisticTask(liveEventId);
		THREAD_POOL_EXECUTOR.execute(task);
	}
}
