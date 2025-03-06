package com.jwzt.livems.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.hibernate.Session;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.common.SomsConfigInfo;
import com.jwzt.filemonitor.FileDirMonitor;
import com.jwzt.livems.live.RecordLiveVodMgr;

/**
 * 定时自动通知JAVA录制程序对直播进行录制
 * @author gstars
 *
 */
public class TaskThred extends Thread{
	public void run()
    {
		try{
			
				File[] fileArr = getAllTask();
				if(fileArr!=null&&fileArr.length>0){
					for(File file : fileArr){
						BufferedReader br = new BufferedReader(new FileReader(file));
						String url = br.readLine();
						String fileName = file.getName();
						String uuid = fileName.substring(0, fileName.lastIndexOf("."));
						FilePathPo po = new FilePathPo();
						po.setPath(url);
						po.setRepeatNum(0);
						po.setUuid(uuid);
						FileDirMonitor.queue.offer(po);
					}
				}
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
    }
	public File[] getAllTask(){
		String dir = SomsConfigInfo.getHomePath()+"/filequeueTask/";
		File livemsTaskDir = new File(dir);
		return livemsTaskDir.listFiles();
	}
}
