package com.bvRadio.iLive.iLive.dao;

import java.util.Map;

import com.bvRadio.iLive.iLive.entity.ILiveRandomRecordTask;

public interface ILiveRandomRecordTaskDao {
	
	
	public void addTask(ILiveRandomRecordTask task);
	
	
	public void updateTask(ILiveRandomRecordTask task);
	
	public ILiveRandomRecordTask getTask(Map<String,Object> sqlParam);


	public void saveTask(ILiveRandomRecordTask task);


	public void updateRecordTask(ILiveRandomRecordTask task);

}
