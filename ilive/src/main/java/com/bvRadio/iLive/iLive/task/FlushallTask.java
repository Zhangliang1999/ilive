package com.bvRadio.iLive.iLive.task;

import java.util.TimerTask;

import com.bvRadio.iLive.iLive.util.JedisUtils;



/**
 * 文件存储定时器
 * 
 * @author Administrator
 *
 */
public class FlushallTask extends TimerTask {
	public FlushallTask() {
		super();
	}
	@Override
	public void run() {
		try {
			JedisUtils.flushall();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
