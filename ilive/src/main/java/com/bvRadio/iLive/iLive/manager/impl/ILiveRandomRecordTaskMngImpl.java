package com.bvRadio.iLive.iLive.manager.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveRandomRecordTaskDao;
import com.bvRadio.iLive.iLive.entity.ILiveRandomRecordTask;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveRandomRecordTaskMng;

public class ILiveRandomRecordTaskMngImpl implements ILiveRandomRecordTaskMng {

	@Autowired
	private ILiveRandomRecordTaskDao iLiveRandomRecordTaskDao;

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;

	@Override
	public ILiveRandomRecordTask getTaskByQuery(Long liveEventId, long userId,Integer liveStatus) {
		Map<String, Object> map = new HashMap<>();
		map.put("liveEventId", liveEventId);
		map.put("userId", userId);
		map.put("liveStatus", liveStatus);
		ILiveRandomRecordTask task = iLiveRandomRecordTaskDao.getTask(map);
		return task;
	}

	@Override
	@Transactional
	public void saveTask(ILiveRandomRecordTask task) {
		Long nextId = iLiveFieldIdManagerMng.getNextId("ilive_record_random_task", "task_id", 1);
		task.setTaskId(nextId);
		iLiveRandomRecordTaskDao.saveTask(task);
	}

	@Override
	@Transactional
	public void updateRecordTask(ILiveRandomRecordTask task) {
		iLiveRandomRecordTaskDao.updateRecordTask(task);
	}

}
