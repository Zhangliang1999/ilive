package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveAppointmentDao;
import com.bvRadio.iLive.iLive.entity.ILiveAppointment;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.ILiveAppointmentMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;

@Transactional
@Service
public class ILiveAppointmentMngImple implements ILiveAppointmentMng {

	@Autowired
	private ILiveAppointmentDao iLiveAppointmentDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;	//人员管理
	
	public ILiveAppointment saveILiveAppointment(String userid,Integer roomId) {
		ILiveAppointment iLiveAppointment = new ILiveAppointment();
		Integer nextId = filedIdMng.getNextId("ilive_appointment", "id", 1).intValue();
		iLiveAppointment.setId(nextId);
		iLiveAppointment.setUserid(userid);
		iLiveAppointment.setRoomId(roomId);
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		ILiveEvent event = iliveRoom.getLiveEvent();
		iLiveAppointment.setLiveTitle(event.getLiveTitle());
		iLiveAppointment.setLiveEventId(event.getLiveEventId());
		iLiveAppointment.setStartTime(event.getLiveStartTime());
		iLiveAppointment.setEndTime(event.getLiveEndTime());
		iLiveAppointment.setCreateTime(new Timestamp(new Date().getTime()));
		ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(Long.parseLong(userid));
		iLiveAppointment.setNailName(iLiveManager.getNailName());
		iLiveAppointment.setMobile(iLiveManager.getMobile());
		iLiveAppointmentDao.saveILiveAppointment(iLiveAppointment);
		return iLiveAppointment;
		
	}


	@Override
	public List<ILiveAppointment> getAppointmentList(String userid) {
		return iLiveAppointmentDao.getAppointmentList(userid);
	}


	@Override
	public ILiveAppointment getAppointmentByUseridAndEventid(String userid, Long eventid) {
		return iLiveAppointmentDao.getAppointmentByUseridAndEventid(userid,eventid);
	}


	@Override
	public void cancelAppointment(String userid, Integer roomId) {
		iLiveAppointmentDao.cancelAppointment(userid, roomId);
	}


	@Override
	public boolean isAppointment(String userid, Long eventid) {
		return iLiveAppointmentDao.isAppointment(userid,eventid);
	}


	@Override
	public List<ILiveAppointment> getAppointmentList(String userid, Integer pageNo, Integer pageSize) {
		return iLiveAppointmentDao.getAppointmentList(userid,pageNo,pageSize);
	}


	@Override
	public List<ILiveAppointment> getListByEventId(Long eventId) {
		return iLiveAppointmentDao.getListByEventId(eventId);
	}


	@Override
	public List<String> getUserListByEventId(Long eventId) {
		return iLiveAppointmentDao.getUserListByEventId(eventId);
	}


	@Override
	public Pagination getPage(String name, Long eventId, Integer pageNo, Integer pageSize) {
		return iLiveAppointmentDao.getPage(name, eventId, pageNo, pageSize);
	}
}
