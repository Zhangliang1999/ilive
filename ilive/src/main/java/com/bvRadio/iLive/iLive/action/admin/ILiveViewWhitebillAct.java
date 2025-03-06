package com.bvRadio.iLive.iLive.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMemberMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewAuthBillMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "viewwhite")
public class ILiveViewWhitebillAct {

	@Autowired
	private ILiveViewWhiteBillMng viewWhiteMng;

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;

	@Autowired
	private ILiveEventMng iLiveEventMng;
	
	@Autowired
	private ILiveViewAuthBillMng iLiveViewAuthBillMng;
	

	@RequestMapping(value = "/add.do")
	public void viewWhiteBill( Integer roomId, Long iLiveEventId, HttpServletRequest request,
			HttpServletResponse response) {
		
		try{
			
			JSONObject jsonObject = new JSONObject();
			if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
				JedisUtils.delObject("roomInfo:"+roomId);
			}
				ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
				ILiveEvent liveEvent = iliveRoom.getLiveEvent();
				
				// 白名单观看
				liveEvent.setViewAuthorized(4);
				iLiveEventMng.updateILiveEvent(liveEvent);
				iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveEventId(liveEvent.getLiveEventId());
				ResponseUtils.renderJson(response, jsonObject.toString());
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
