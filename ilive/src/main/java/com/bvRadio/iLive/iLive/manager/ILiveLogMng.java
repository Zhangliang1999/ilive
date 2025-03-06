package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveLog;

public interface ILiveLogMng {

	public ILiveLog findById(Integer liveId);

	public ILiveLog save(ILiveLog bean);

	public ILiveLog deleteById(Integer liveId);
	
	public ILiveLog update(ILiveLog bean);

}
