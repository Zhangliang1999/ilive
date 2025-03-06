package com.jwzt.statistic.pool;
/**
* @author ysf
*/

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jwzt.statistic.task.MinuteStatisticTask;

public class MinuteStatisticPool {

	public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(3, 100, 0L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	public static void execute(Long liveEventId) {
		MinuteStatisticTask task = new MinuteStatisticTask(liveEventId);
		THREAD_POOL_EXECUTOR.execute(task);

	}
}
