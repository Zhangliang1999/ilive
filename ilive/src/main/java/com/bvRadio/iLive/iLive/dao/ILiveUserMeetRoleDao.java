package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveUserMeetRole;

public interface ILiveUserMeetRoleDao {

	
	void save(ILiveUserMeetRole ILiveUserMeetRole);
	
	void update(ILiveUserMeetRole ILiveUserMeetRoleo);
	
	
	ILiveUserMeetRole getById(Long id);

	
	ILiveUserMeetRole getByUserMeetId(Integer roomId,String userId);
	List<ILiveUserMeetRole> getListByType(Integer roomId, Integer type);

	void delete(Long id);

}
