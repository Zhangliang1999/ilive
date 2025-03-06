package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
public class ILiveFieldIdManagerMngImpl implements ILiveFieldIdManagerMng {

	@Override
	public synchronized Long getNextId(String tableName, String fieldName, Integer step) {
		long nextId = iLiveFieldIdManagerDao.getNextId(tableName, fieldName, step);
		return nextId;
	}

	@Autowired
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao;

}
