package com.jwzt.statistic.pool;
/**
* @author ysf
*/

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jwzt.statistic.task.MeetingStatisticTask;
import com.jwzt.statistic.task.TotalStatisticTask;

public class MeetingStatisticPool {

	private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(3, 100, 0L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	public static void execute(final Date startTime, final Date endTime) {
		MeetingStatisticTask task = new MeetingStatisticTask(startTime, endTime);
		THREAD_POOL_EXECUTOR.execute(task);
	}
}
