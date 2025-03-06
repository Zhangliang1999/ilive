package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
@Transactional
public class ILiveFieldIdManagerMngImpl implements ILiveFieldIdManagerMng {

	@Override
	public Long getNextId(String tableName, String fieldName, Integer step) {
		long nextId = iLiveFieldIdManagerDao.getNextId(tableName, fieldName, step);
		return nextId;
	}

	@Autowired
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao;

}
