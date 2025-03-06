package com.bvRadio.iLive.iLive.manager.impl;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bvRadio.iLive.iLive.dao.ILiveMeetRequestDao;
import com.bvRadio.iLive.iLive.entity.ILiveMeetRequest;
import com.bvRadio.iLive.iLive.manager.ILiveMeetRequestMng;
@Service
@Transactional
public class ILiveMeetRequestMngImpl implements ILiveMeetRequestMng {

	@Autowired
	private ILiveMeetRequestDao managerDao;
	
	
	

	@Override
	public void save(ILiveMeetRequest ILiveMeetRequest) {
		
		managerDao.save(ILiveMeetRequest);
	}

	@Override
	public void update(ILiveMeetRequest ILiveMeetRequest) {
		managerDao.update(ILiveMeetRequest);
	}


	@Override
	public ILiveMeetRequest getById(String id) {
		ILiveMeetRequest doc = managerDao.getById(id);
		if(doc==null) {
			return null;
		}
		
		return doc;
	}

	@Override
	public ILiveMeetRequest getByMeetId(Integer roomId) {
		
		return managerDao.getByMeetId(roomId);
	}

	@Override
	public ILiveMeetRequest getHostByMeetId(Integer roomId) {
		
		return managerDao.getHostByMeetId(roomId);
	}

	@Override
	public ILiveMeetRequest getStudentByMeetId(Integer roomId) {
		
		return managerDao.getStudentByMeetId(roomId);
	}

	

	

	



}
