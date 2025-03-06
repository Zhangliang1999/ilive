package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveConvertTask;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

public interface ILiveConvertTaskDao {
	public void saveConvertTask(ILiveConvertTask iLiveEnterprise);
	public void updateConvertTask(ILiveConvertTask iLiveEnterprise);
	
	public ILiveConvertTask getConvertTask(Integer taskId);
	public List<ILiveConvertTask> getConvertTaskByState(Integer state);
}
