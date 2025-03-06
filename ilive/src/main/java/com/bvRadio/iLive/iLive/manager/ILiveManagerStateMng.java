package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveManagerState;

public interface ILiveManagerStateMng {

	ILiveManagerState getIliveManagerState(Long managerId);

	void saveIliveManagerState(ILiveManagerState managerState);

	void updateIliveManagerState(ILiveManagerState managerState);

}
