package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveManagerState;

/**
 * 用户状态控制类
 * @author administrator
 */
public interface ILiveManagerStateDao {
	
	ILiveManagerState getIliveManagerState(Long managerId);
	
	void saveIliveManagerState(ILiveManagerState managerState);

	void updateIliveManagerState(ILiveManagerState managerState);
}
