package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveAppointment;

public interface ILiveAppointmentMng {

	//保存预约信息
	public ILiveAppointment saveILiveAppointment(String userid,Integer roomId);
	
	//获取预约列表
	public List<ILiveAppointment> getAppointmentList(String userid);
	public List<ILiveAppointment> getAppointmentListByUserIdAndRoomId(String userid,Integer roomId);
	//获取预约列表
	public List<ILiveAppointment> getAppointmentList(String userid,Integer pageNo,Integer pageSize);
	
	Pagination getAppointmentPage(String userid,Integer roomId,Integer pageNo,Integer pageSize);
	
	//根据userid和eventid获取预约信息
	public ILiveAppointment getAppointmentByUseridAndEventid(String userid,Long eventid);
	
	//取消预约
	public void cancelAppointment(String userid,Integer roomId);
	
	//该直播间是否预约
	public boolean isAppointment(String userid,Long eventid);
	
	//根据场次id获取所有预约信息
	public List<ILiveAppointment> getListByEventId(Long eventId);
	//根据场次id获取所有预约人员ID
	public List<String> getUserListByEventId(Long eventId);
	
	Pagination getPage(String name,Long eventId,Integer pageNo,Integer pageSize);
}
