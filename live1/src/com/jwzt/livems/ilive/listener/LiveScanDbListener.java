package com.jwzt.livems.ilive.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.jwzt.DB.soms.live.task.ILiveMakeScreenRetryTask;
import com.jwzt.DB.soms.live.task.ILiveMakeScreenRetryTaskMgr;
import com.jwzt.livems.ilive.FFmpegUtils;
import com.jwzt.livems.ilive.ILiveMediaFileMgr;
import com.jwzt.livems.ilive.MediaFile;
import com.jwzt.livems.ilive.exception.DbUpdaterException;
import com.jwzt.livems.ilive.exception.FileScreenException;

public class LiveScanDbListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			String realPath = arg0.getServletContext().getRealPath("/cutpictask");
			File file = new File(realPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			ScheduledExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
			newSingleThreadScheduledExecutor.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					MediaFile iLiveMediaFile = null;
					try {
						iLiveMediaFile = ILiveMediaFileMgr.INSTANCE.getILiveMediaFile();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					if (iLiveMediaFile != null) {
						boolean dealProcess = dealProcess(iLiveMediaFile);
						if(!dealProcess) {
							try {
								ILiveMediaFileMgr.INSTANCE.abandonILiveMediaFile(iLiveMediaFile);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}, 10, 15, TimeUnit.SECONDS);

			Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
				public void run() {
					List<Long> retryTasks = ILiveMakeScreenRetryTaskMgr.INSTANCE.getRetryTask();
					if (retryTasks != null && retryTasks.size() != 0) {
						for (Long fileId : retryTasks) {
							MediaFile mediaFile = ILiveMediaFileMgr.INSTANCE.getILiveMediaFileByFileId(fileId);
							if (mediaFile != null) {
								boolean dealProcess = dealProcess(mediaFile);
								if (dealProcess) {
									ILiveMakeScreenRetryTask retryTask = new ILiveMakeScreenRetryTask();
									retryTask.setDealResult(1);
									retryTask.setTaskCreateTime(new Timestamp(System.currentTimeMillis()));
									retryTask.setFileId(mediaFile.getFileId());
									retryTask.setTaskNode("文件重试成功");
									ILiveMakeScreenRetryTaskMgr.INSTANCE.updateTask(retryTask);
								}
							}
						}
					}

				}
			}, 10, 30, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean dealProcess(MediaFile iLiveMediaFile) {
		boolean ret = false;
		System.out.println("进入线程，开始执行！！！！");
		try {
			System.out.println("开始执行进程========process！");
			boolean process = FFmpegUtils.INSTANCE.process(iLiveMediaFile);
			System.out.println("执行进程process的结果是："+process);
			ret = process;
		} catch (FileNotFoundException e) {
			// 处理文件不存在
			System.out.println("处理文件不存在!!!!");
			// e.printStackTrace();
			ILiveMakeScreenRetryTask retryTask = new ILiveMakeScreenRetryTask();
			retryTask.setDealResult(0);
			retryTask.setTaskCreateTime(new Timestamp(System.currentTimeMillis()));
			retryTask.setFileId(iLiveMediaFile.getFileId());
			retryTask.setTaskNode("文件无法获取");
			ILiveMakeScreenRetryTaskMgr.INSTANCE.updateTask(retryTask);
		} catch (DbUpdaterException e) {
			// 处理数据库最终更新状态异常
			System.out.println("处理数据库最终更新状态异常!!!!");
			ILiveMakeScreenRetryTask retryTask = new ILiveMakeScreenRetryTask();
			retryTask.setDealResult(0);
			retryTask.setTaskCreateTime(new Timestamp(System.currentTimeMillis()));
			retryTask.setFileId(iLiveMediaFile.getFileId());
			retryTask.setTaskNode("处理数据库最终更新状态异常");
			ILiveMakeScreenRetryTaskMgr.INSTANCE.updateTask(retryTask);
			// e.printStackTrace();
		} catch (FileScreenException e) {
			// 处理获得文件信息的异常
			System.out.println("处理获得文件信息的异常!!!!");
			ILiveMakeScreenRetryTask retryTask = new ILiveMakeScreenRetryTask();
			retryTask.setDealResult(0);
			retryTask.setTaskCreateTime(new Timestamp(System.currentTimeMillis()));
			retryTask.setFileId(iLiveMediaFile.getFileId());
			retryTask.setTaskNode("处理获得文件信息的异常");
			ILiveMakeScreenRetryTaskMgr.INSTANCE.updateTask(retryTask);
			// e.printStackTrace();
		}
		return ret;
	}

}
