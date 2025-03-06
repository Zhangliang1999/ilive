package com.jwzt.statistic.pool;
/**
* @author ysf
*/

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jwzt.statistic.task.EnterpriseStatisticTask;

public class EnterpriseStatisticPool {

	private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(3, 100, 0L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	public static void execute(final Integer enterpriseId, final Date startTime, final Date endTime) {
		EnterpriseStatisticTask task = new EnterpriseStatisticTask(enterpriseId, startTime, endTime);
		THREAD_POOL_EXECUTOR.execute(task);
	}
}
