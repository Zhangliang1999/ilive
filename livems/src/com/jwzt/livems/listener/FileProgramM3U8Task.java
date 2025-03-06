package com.jwzt.livems.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import org.hibernate.Session;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.soms.live.liveChannel.ChannelInfo;
import com.jwzt.DB.soms.live.liveChannel.ChannelMgr;
import com.jwzt.common.Logger;
import com.jwzt.common.SomsConfigInfo;
import com.jwzt.filemonitor.FileDirMonitor;
import com.jwzt.livems.live.RecordLiveMgr;
import com.jwzt.livems.live.RecordLiveVodMgr;

/**
 * 每天23:00 定时生成第二天的虚拟直播切片
 * @author 郭云飞
 *
 */
public class FileProgramM3U8Task extends TimerTask{
	public static List  channelList = null;
	public void run()
    {
		try{
			ChannelMgr channelMgr = new ChannelMgr();
				if(channelList==null){
					  channelList = channelMgr.getAllChannelList(" channel_type=1");
				}
				RecordLiveMgr liveMgr = new RecordLiveMgr();
				for(int i=0;i<channelList.size();i++){
					ChannelInfo channelInfo = (ChannelInfo) channelList.get(0);
					liveMgr.updateFileProgramM3U8(channelInfo);
				}

	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
    }
}
