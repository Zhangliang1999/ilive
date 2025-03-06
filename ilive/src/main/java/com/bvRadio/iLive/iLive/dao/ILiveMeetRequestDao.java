package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveMeetRequest;

public interface ILiveMeetRequestDao {

	
	void save(ILiveMeetRequest ILiveMeetRequest);
	
	void update(ILiveMeetRequest ILiveMeetRequesto);
	
	
	ILiveMeetRequest getById(String id);

	
	ILiveMeetRequest getByMeetId(Integer roomId);
	
	ILiveMeetRequest getHostByMeetId(Integer roomId);

	ILiveMeetRequest getStudentByMeetId(Integer roomId);
	
}
