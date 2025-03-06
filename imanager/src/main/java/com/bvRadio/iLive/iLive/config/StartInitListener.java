package com.bvRadio.iLive.iLive.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartInitListener implements ServletContextListener {

	Logger logger = null;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		String basePath = event.getServletContext().getRealPath("/");
		basePath = basePath.replace("\\", "/");
		System.setProperty("AppBasePath_iLive", basePath);
		System.setProperty("log4jDir", basePath + "/logs/");
		logger = LoggerFactory.getLogger(StartInitListener.class);
		logger.info("此时应用开始进入初始化阶段-----------------------初始化了basepath为:{}",basePath);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("此时应用开始进入初始化阶段-----------------------app destroy!");
	}

}
