package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveUserMeetRole;

public interface ILiveUserMeetRoleMng {
	
	Long save(ILiveUserMeetRole ILiveUserMeetRole);
	
	void update(ILiveUserMeetRole ILiveUserMeetRole);
	
	
	ILiveUserMeetRole getById(Long id);
	
	
	ILiveUserMeetRole getByUserMeetId(Integer roomId,String userId);

	List<ILiveUserMeetRole> getListByType(Integer roomId, Integer type);

	void delete(List<ILiveUserMeetRole> role);


}
