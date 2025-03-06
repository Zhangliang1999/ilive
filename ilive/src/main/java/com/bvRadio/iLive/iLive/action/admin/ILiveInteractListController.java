package com.bvRadio.iLive.iLive.action.admin;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveAppointment;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveRoomRegister;
import com.bvRadio.iLive.iLive.manager.ILiveAppointmentMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomRegisterService;

@Controller
@RequestMapping(value="interactlist")
public class ILiveInteractListController {

	
	@Autowired
	private ILiveRoomRegisterService registerService;	//签到
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	@Autowired
	private ILiveAppointmentMng iLiveAppointmentMng; // 预约
	@Autowired
	private ILiveManagerMng iLiveManagerMng;	//人员管理
	
	
	/**
	 * 签到列表页
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="signlist.do",method=RequestMethod.GET)
	public String signList(Integer roomId,Model model,String name,Integer pageNo,Integer pageSize) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		Pagination page = registerService.querySignByRoomId(roomId, liveEvent.getLiveEventId(), name, 0, pageNo==null?1:pageNo, 10);
		List<ILiveRoomRegister> list = (List<ILiveRoomRegister>) page.getList();
		
		Iterator<ILiveRoomRegister> iterator = list.iterator();
		while (iterator.hasNext()) {
			ILiveRoomRegister iLiveRoomRegister = (ILiveRoomRegister) iterator.next();
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(iLiveRoomRegister.getUserId()));
			if (iLiveManager == null) {
				iterator.remove();
			}else {
				iLiveRoomRegister.setiLiveManager(iLiveManager);
			}
		}
		
		model.addAttribute("page", page);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("leftActive", "4_6");
		model.addAttribute("searchname", name);
		model.addAttribute("topActive", "1");
		return "interact/signList";
	}
	
	/**
	 * 点赞列表页
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="praiselist.do")
	public String praiseList(Integer roomId,Model model,String name,Integer pageNo,Integer pageSize) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		Pagination page = registerService.querySignByRoomId(roomId, liveEvent.getLiveEventId(), name, 1, pageNo==null?1:pageNo, 10);
		List<ILiveRoomRegister> list = (List<ILiveRoomRegister>) page.getList();
		
		Iterator<ILiveRoomRegister> iterator = list.iterator();
		while (iterator.hasNext()) {
			ILiveRoomRegister iLiveRoomRegister = (ILiveRoomRegister) iterator.next();
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(iLiveRoomRegister.getUserId()));
			if (iLiveManager == null) {
				iterator.remove();
			}else {
				iLiveRoomRegister.setiLiveManager(iLiveManager);
			}
		}
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page.getTotalPage());
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("leftActive", "4_7");
		model.addAttribute("searchname", name);
		model.addAttribute("topActive", "1");
		return "interact/praiseList";
	}
	
	/**
	 * 预约列表页
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="appointmentlist.do")
	public String appointment(Integer roomId,Model model,String name,Integer pageNo,Integer pageSize) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		Pagination page = iLiveAppointmentMng.getPage(name, liveEvent.getLiveEventId(), pageNo==null?1:pageNo, 10);
//		List<ILiveAppointment> list = (List<ILiveAppointment>) page.getList();
//		
//		Iterator<ILiveAppointment> iterator = list.iterator();
//		while (iterator.hasNext()) {
//			ILiveAppointment iLiveAppointment = (ILiveAppointment) iterator.next();
//			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(iLiveAppointment.getUserid()));
//			if (iLiveManager == null) {
//				iterator.remove();
//			}else {
//				iLiveAppointment.setiLiveManager(iLiveManager);
//			}
//		}
//		
//		for (ILiveAppointment iLiveAppointment : list) {
//			iLiveAppointment.setiLiveManager(iLiveManagerMng.getILiveManager(Long.parseLong(iLiveAppointment.getUserid())));
//		}
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page.getTotalPage());
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("leftActive", "4_8");
		model.addAttribute("searchname", name);
		model.addAttribute("topActive", "1");
		return "interact/appointment";
	}
}
