package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveRandomRecordTask;

public interface ILiveRandomRecordTaskMng {

	ILiveRandomRecordTask getTaskByQuery(Long liveEventId, long userId,Integer liveStatus);

	void saveTask(ILiveRandomRecordTask task);

	void updateRecordTask(ILiveRandomRecordTask task);

}
