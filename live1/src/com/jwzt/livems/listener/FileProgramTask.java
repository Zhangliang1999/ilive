package com.jwzt.livems.listener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import com.jwzt.DB.soms.live.liveChannel.ChannelInfo;
import com.jwzt.DB.soms.live.liveChannel.ChannelMgr;
import com.jwzt.common.Logger;
import com.jwzt.livems.live.RecordLiveMgr;

/**
 * 每天23:00 定时生成第二天的虚拟直播切片
 * @author 郭云飞
 *
 */
public class FileProgramTask extends TimerTask{
	public void run()
    {
		try{
				ChannelMgr channelMgr = new ChannelMgr();
				RecordLiveMgr liveMgr = new RecordLiveMgr();
				List  channelList = channelMgr.getAllChannelList(" channel_type=1");
				for(int i=0;i<channelList.size();i++){
					ChannelInfo channelInfo = (ChannelInfo) channelList.get(0);
					String ret = liveMgr.FileProgram(channelInfo.getChannel_id(), getTomorrow());
					Logger.log(" 定时生成第二天的虚拟直播切片 频道【"+channelInfo.getChannel_id()+"】 返回值=="+ret, 2);
				}

	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
    }
	public String getTomorrow(){
			Calendar   cal   =   Calendar.getInstance();
			 cal.add(Calendar.DATE,   1);
			 String Tomorrow = new SimpleDateFormat( "yyyyMMdd").format(cal.getTime());
			 return Tomorrow;
	}
	
}
