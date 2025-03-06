package com.jwzt.statistic.task;

import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class UserStatisticTask extends TimerTask {

	private static final Logger log = LogManager.getLogger();

	@Override
	public void run() {
		try {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			if (null == context) {
				return;
			}
			log.debug("UserStatisticTask run.");
		} catch (Exception e) {
			log.error("UserStatisticTask error.", e);
		}
	}
}