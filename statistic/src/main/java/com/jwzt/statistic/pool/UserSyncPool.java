package com.jwzt.statistic.pool;
/**
* @author ysf
*/

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jwzt.statistic.task.UserSyncTask;

public class UserSyncPool {

	private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(3, 100, 0L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	public static void execute(List<Long> userIdList) {
		UserSyncTask task = new UserSyncTask(userIdList);
		THREAD_POOL_EXECUTOR.execute(task);
	}
}
