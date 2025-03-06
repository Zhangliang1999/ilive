package com.bvRadio.iLive.iLive.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.util.DownloadURL;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

@Component
public class Notify {

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;
	
	@Autowired
	private ILiveEventMng iLiveEventMng;

//	public void name() {
//		DownloadURL downloadURL = new DownloadURL();
//		Date now = new Date();
//		com.jwzt.common.Md5 md5 = new com.jwzt.common.Md5();
//		Long timestamp = now.getTime();
//		String token = md5.getMD5Str(timestamp+"liveJudge");
//
//		
//		//String url = "http://192.168.21.52:8989/imagescan/api/piccut/addtask?roomId=1426&liveEventId=3978&distancce=30&pullStreamAddr="+java.net.URLEncoder.encode("rtmp://live01.zbt.tv189.net:1935/live/live1426_tzwj_5000k")+"&token="+token+"&timestamp="+timestamp;
//		
//		String url = "http://192.168.21.52:8989/imagescan/api/piccut/removetask?roomId=1426&token="+token+"&timestamp="+timestamp;
//				
//		System.out.println(	url );
//		System.out.println(DownloadURL.downloadUrl(url));
//
//	}
	
	/**
	 * 开始直播时候的通知
	 * @param roomId
	 * @param pullStreamAddr
	 */
	public void start(Integer roomId,String pullStreamAddr) {
		Date now = new Date();
		com.jwzt.common.Md5 md5 = new com.jwzt.common.Md5();
		Long timestamp = now.getTime();
		String token = md5.getMD5Str(timestamp+"liveJudge");
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		liveEvent.setNotifyToken(token);
		iLiveEventMng.updateILiveEvent(liveEvent);
		String aliyunJianhuang = ConfigUtils.get("aliyun_jianhuang");
		String url = aliyunJianhuang + "/imagescan/api/piccut/addtask?roomId="+roomId+"&liveEventId="+liveEvent.getLiveEventId()+"&distancce=30&pullStreamAddr="+java.net.URLEncoder.encode(""+pullStreamAddr+"")+"&token="+token+"&timestamp="+timestamp;
		DownloadURL.downloadUrl(url);
	}
	
	/**
	 * 结束直播时的通知
	 * @param roomId
	 */
	public void end(Integer roomId) {
		Date now = new Date();
		Long timestamp = now.getTime();
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		String token = liveEvent.getNotifyToken();
		String aliyunJianhuang = ConfigUtils.get("aliyun_jianhuang");
		String url = aliyunJianhuang+"/imagescan/api/piccut/removetask?roomId=1426&token="+token+"&timestamp="+timestamp;
		DownloadURL.downloadUrl(url);
	}
	
}
