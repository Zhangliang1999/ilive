package com.bvRadio.iLive.iLive.action.front.web;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.dao.ILiveMessageDao;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;;

@Controller
@RequestMapping(value = "/thirdAPI")
@Transactional
public class ILiveThirdAPIAct {

	@Autowired
	private ILiveMessageMng iLiveMessageMng;// 消息管理
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 房间管理
	
	@Autowired
	private ILiveMessageDao iLiveMessageDao;// 房间管理
	
	@RequestMapping(value = "getMessage.jspx")
	@Transactional(readOnly = true)
	public void getMessage(Integer roomId , Integer pageNo , HttpServletRequest request, HttpServletResponse response){
		//提供给第三方系统查询聊天数据
		JSONObject resultJson = new JSONObject();
		List<JSONObject> data = new ArrayList<JSONObject>();
		ILiveLiveRoom iLiveLiveRoom  =  iLiveLiveRoomMng.getIliveRoom(roomId);
		
		List<ILiveMessage> messageList = iLiveMessageDao.selectILiveMessageMngByEventIdAndType(iLiveLiveRoom.getLiveEvent().getLiveEventId(), Integer.valueOf(2), pageNo , 10 , 1);
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		
		for (ILiveMessage iLiveMessage : messageList) {
			JSONObject putMessageInJson = iLiveMessage.putMessageInJson(null);
			putMessageInJson.put("sendtime", sf.format(iLiveMessage.getCreateTime()));
			data.add(putMessageInJson);
		}
	
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "获取数据成功");
		resultJson.put("data",  data);

		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	
	@RequestMapping(value = "getMessageCount.jspx")
	@Transactional(readOnly = true)
	public void getMessage(Integer roomId , HttpServletRequest request, HttpServletResponse response){
		//提供给第三方系统查询聊天数据
		JSONObject resultJson = new JSONObject();
		JSONObject data = new JSONObject();
		ILiveLiveRoom iLiveLiveRoom  =  iLiveLiveRoomMng.getIliveRoom(roomId);
		
		int count = iLiveMessageDao.getNumByEventIdAndType(iLiveLiveRoom.getLiveEvent().getLiveEventId(), Integer.valueOf(2), true);

		data.put("messageCount",count);
		
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "获取数据成功");
		resultJson.put("data",  data);

		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
}
