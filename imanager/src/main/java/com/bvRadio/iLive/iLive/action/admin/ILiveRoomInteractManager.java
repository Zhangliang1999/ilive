package com.bvRadio.iLive.iLive.action.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;

@Controller
@RequestMapping(value="interactmanager")
public class ILiveRoomInteractManager {

	@Autowired
	private ILiveEventMng iLiveEventMng;// 场次管理

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private ILiveMessageMng iLiveMessageMng;//消息管理
	
	@RequestMapping(value="interact.do")
	public String interact(Integer roomId,Model model) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		
		//max1
		double count1 = iLiveMessageMng.getNumByEventIdAndType(liveEvent.getLiveEventId(),1);
		int max1 = (int) Math.ceil(count1/10);
		//max2
		double count2 = iLiveMessageMng.getNumByEventIdAndType(liveEvent.getLiveEventId(),2);
		int max2 = (int) Math.ceil(count2/10);
		//max3
		double count3 = iLiveMessageMng.getNumByEventIdAndType(liveEvent.getLiveEventId(),3);
		int max3 = (int) Math.ceil(count3/10);

		List<ILiveMessage> list1 = iLiveMessageMng.selectILiveMessageMngByEventIdAndType(liveEvent.getLiveEventId(), 1,1);
		List<ILiveMessage> list2 = iLiveMessageMng.selectILiveMessageMngByEventIdAndType(liveEvent.getLiveEventId(), 2,3);
		List<ILiveMessage> list3 = iLiveMessageMng.selectILiveMessageMngByEventIdAndType(liveEvent.getLiveEventId(), 3,1);

		model.addAttribute("list1",list1);
		model.addAttribute("list1s",count1);
		model.addAttribute("list2",list2);
		model.addAttribute("list2s",count2);
		model.addAttribute("list3",list3);
		model.addAttribute("list3s",count3);
		model.addAttribute("max1",max1);
		model.addAttribute("max2",max2);
		model.addAttribute("max3",max3);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("leftActive", "4_1");
		model.addAttribute("topActive", "1");
		return "interactManager/interact";
	}
}
