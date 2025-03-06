package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveMeetRequest;

public interface ILiveMeetRequestMng {
	
	void save(ILiveMeetRequest ILiveMeetRequest);
	
	void update(ILiveMeetRequest ILiveMeetRequest);
	
	
	ILiveMeetRequest getById(String id);
	
	
	ILiveMeetRequest getByMeetId(Integer roomId);

	ILiveMeetRequest getHostByMeetId(Integer roomId);

	ILiveMeetRequest getStudentByMeetId(Integer roomId);


}
