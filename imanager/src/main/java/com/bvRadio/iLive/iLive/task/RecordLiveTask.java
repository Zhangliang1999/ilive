package com.bvRadio.iLive.iLive.task;

import java.text.ParseException;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bvRadio.iLive.iLive.manager.ILiveLiveMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;

public class RecordLiveTask extends TimerTask {
	private static final Logger log = LoggerFactory.getLogger(RecordLiveTask.class);
	private Integer id;
	private long startTime;
	private long endTime;

	public RecordLiveTask() {
		super();
	}

	public RecordLiveTask(Integer id,long startTime,long endTime) {
		super();
		this.id=id;
		this.startTime=startTime;
		this.endTime=endTime;
	}

	public void run() {
		
			ILiveLiveMng iLiveLiveMng = (ILiveLiveMng) ApplicationContextUtil
					.getBean("iLiveLiveMng");
			try {
				iLiveLiveMng.RecordLive(id, startTime, endTime);
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			
	
	
	}

}