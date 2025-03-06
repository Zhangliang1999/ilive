package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveAppointment;

public interface ILiveAppointmentDao {

	public void saveILiveAppointment(ILiveAppointment iLiveAppointment);
	
	public List<ILiveAppointment> getAppointmentList(String userid);
	public List<ILiveAppointment> getAppointmentList(String userid, Integer pageNo, Integer pageSize);
	
	public ILiveAppointment getAppointmentByUseridAndEventid(String userid, Long eventid);
	
	public void cancelAppointment(String userid, Integer roomId);
	
	public boolean isAppointment(String userid, Long eventId);
	
	public List<ILiveAppointment> getListByEventId(Long eventId);
	
	public List<String> getUserListByEventId(Long eventId);
	
	public Pagination getPage(String name, Long eventId, Integer pageNo, Integer pageSize);
}
