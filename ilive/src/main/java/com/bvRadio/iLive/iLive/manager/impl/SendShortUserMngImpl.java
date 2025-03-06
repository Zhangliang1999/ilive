package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.SendShortUserDao;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.SendShortMessaheRecord;
import com.bvRadio.iLive.iLive.entity.SendShortUser;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.SendShortMessaheRecordMng;
import com.bvRadio.iLive.iLive.manager.SendShortUserMng;
import com.bvRadio.iLive.iLive.service.PushJson;
import com.bvRadio.iLive.sms.ILiveSmgServerCenter;

@Service
@Transactional
public class SendShortUserMngImpl implements SendShortUserMng {

	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Autowired
	private SendShortUserDao sendShortUserDao;
	
	@Autowired
	private SendShortMessaheRecordMng sendShortMessaheRecordMng;
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private ThreadPoolTaskExecutor execute;
	
	@Override
	public Pagination getPage(Integer roomId, Long id, String sendUser, Integer pageNo, Integer pageSize) {
		return sendShortUserDao.getPage(roomId, id, sendUser, pageNo, pageSize);
	}

	@Override
	public SendShortUser getById(Long id) {
		return sendShortUserDao.getById(id);
	}

	@Override
	public Long save(SendShortUser sendShortUser) {
		Long nextId = fieldIdMng.getNextId("send_short_user", "id", 1);
		sendShortUser.setId(nextId);
		sendShortUser.setCreateTime(new Timestamp(new Date().getTime()));
		sendShortUserDao.save(sendShortUser);
		return nextId;
	}

	@Override
	@Transactional
	public Long operatorSend(SendShortUser sendShortUser, HashMap<String, String> userMessage,String url) {
		Long id = null;
		try {	
			id = this.save(sendShortUser);
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(sendShortUser.getRoomId());
			ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
			final List<JSONObject> list = new ArrayList<>();
			String string = "亲,天翼直播邀请您观看将于"+liveEvent.getLiveStartTime()+"开始的直播"+liveEvent.getLiveTitle()+".直播地址:"+url;
			for(Map.Entry<String, String> entry:userMessage.entrySet()) {
				SendShortMessaheRecord record = new SendShortMessaheRecord();
				record.setReceiveUserName(entry.getKey());
				record.setReceiveUserPhone(entry.getValue());
				record.setRecordId(id);
				record.setSendUserId(sendShortUser.getUserId());
				record.setSendUserName(sendShortUser.getUserName());
				record.setEnterpriseId(sendShortUser.getEnterpriseId());
				sendShortMessaheRecordMng.save(record);
				
				HashMap<String, Object> sendParam = new HashMap<>();
				sendParam.put("biztype", 43);
				sendParam.put("params",
						"[{\"Name\":\"{user}\",\"Val\":\"" + liveEvent.getLiveTitle()
								+ "\"},{\"Name\":\"{time}\",\"Val\":\"" + liveEvent.getLiveStartTime()
								+ "\"},{\"Name\":\"{url}\",\"Val\":\"" + url + "\"}]");
				
				JSONObject obj = new JSONObject();
				obj.put("ID", liveEvent.getRoomId());
				obj.put("TITLE",string);
				obj.put("TYPE", 1);
				obj.put("USERID", "");
				list.add(obj);
				ILiveSmgServerCenter.sendMsg(entry.getValue(), sendParam);
			} 
			execute.execute(new Runnable() {
				
				@Override
				public void run() {
					PushJson.pushMany(list);
				}
			});
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	@Override
	@Transactional
	public Long operatorSend1(SendShortUser sendShortUser, HashMap<String, String> userMessage,String url) {
		Long id = null;
		try {	
			id = this.save(sendShortUser);
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(sendShortUser.getRoomId());
			ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
			final List<JSONObject> list = new ArrayList<>();
//			String string = "亲,天翼直播邀请您观看将于"+liveEvent.getLiveStartTime()+"开始的直播"+liveEvent.getLiveTitle()+".直播地址:"+url;
			HashMap<String, Object> sendParam = new HashMap<>();
			List<String> phonelist=new ArrayList<String>();
			for(Map.Entry<String, String> entry:userMessage.entrySet()) {
				SendShortMessaheRecord record = new SendShortMessaheRecord();
				record.setReceiveUserName(entry.getKey());
				record.setReceiveUserPhone(entry.getValue());
				record.setRecordId(id);
				record.setSendUserId(sendShortUser.getUserId());
				record.setSendUserName(sendShortUser.getUserName());
				record.setEnterpriseId(sendShortUser.getEnterpriseId());
				sendShortMessaheRecordMng.save(record);
				List<Object> paramslist=new ArrayList<Object>();
				paramslist.add("天翼直播");
				paramslist.add(liveEvent.getLiveStartTime());
				paramslist.add(liveEvent.getLiveTitle()+".直播地址:"+url);
				sendParam.put("biztype", 248608);
				sendParam.put("params",paramslist);
				 for (Entry<String, String> entry1 : userMessage.entrySet()) {
					 phonelist.add(entry1.getValue());
				    }
//				JSONObject obj = new JSONObject();
//				obj.put("ID", liveEvent.getRoomId());
//				obj.put("TITLE",string);
//				obj.put("TYPE", 1);
//				obj.put("USERID", "");
//				list.add(obj);
			} 
			ILiveSmgServerCenter.sendMsgNew(phonelist, sendParam);
			execute.execute(new Runnable() {
				
				@Override
				public void run() {
					PushJson.pushMany(list);
				}
			});
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public Pagination getPage(Integer roomId, Long id, String sendUser, Integer pageNo, Integer pageSize,
			Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return sendShortUserDao.getPage(roomId, id, sendUser, pageNo, pageSize,startTime,endTime);
	}

}
