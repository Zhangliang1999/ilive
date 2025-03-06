package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveConvertTask;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.base.BaseILiveMediaFile;

public interface ILiveConvertTaskMng {

	public boolean addConvertTask(ILiveConvertTask iLiveConvertTask);
	public List<ILiveConvertTask> getConvertTaskByState(Integer state);
	public ILiveConvertTask getConvertTask(Integer taskId);
	public void updateConvertTask(ILiveConvertTask iLiveConvertTask);
	public ILiveConvertTask createConvertTask(BaseILiveMediaFile selectFile , ILiveServerAccessMethod accessMethod );
}
