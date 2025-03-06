package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ProgressStatusPoJo;

public interface ProgressStatusPoJoDao {
	public ProgressStatusPoJo getUpdateTask(String taskUUID);
	public void save(ProgressStatusPoJo progressStatusPoJo);
}
