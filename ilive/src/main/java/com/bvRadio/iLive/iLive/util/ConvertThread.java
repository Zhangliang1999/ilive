package com.bvRadio.iLive.iLive.util;

import java.io.StringReader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

public class ConvertThread extends Thread{
	private Integer roomId;
	private ILiveLiveRoomMng iLiveRoomMng;
	private Integer taskId;


	public ConvertThread(Integer roomId, ILiveLiveRoomMng iLiveRoomMng, Integer taskId) {
		super();
		this.roomId = roomId;
		this.iLiveRoomMng = iLiveRoomMng;
		this.taskId = taskId;
	}


	public Integer getTaskId() {
		return taskId;
	}


	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}


	public Integer getRoomId() {
		return roomId;
	}


	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}


	public ILiveLiveRoomMng getiLiveRoomMng() {
		return iLiveRoomMng;
	}


	public void setiLiveRoomMng(ILiveLiveRoomMng iLiveRoomMng) {
		this.iLiveRoomMng = iLiveRoomMng;
	}


	@Override
	public void run() {
		
		String converTaskLogo=ConfigUtils.get("conver_task_logo");
		
            	
		
	           
	            
						try {
							Thread.sleep(10000);
							String data=HttpUtils.doDelete("", converTaskLogo+"/api/task/"+taskId);
							ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
							iliveRoom.setConvertTaskId(-1);
							iLiveRoomMng.update(iliveRoom);
						} catch (Exception e) {
							
							e.printStackTrace();
						}
						
					
	            	
	            
		
		
	}
		
	

}
