package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.bvRadio.iLive.iLive.dao.ILiveServerMountDao;
import com.bvRadio.iLive.iLive.entity.MountInfo;
import com.bvRadio.iLive.iLive.manager.ILiveServerMountMng;

public class ILiveServerMountMngImpl implements ILiveServerMountMng {

	@Autowired
	private ILiveServerMountDao iliveServeerMountDao;

	@Override
	public MountInfo getMountInfoById(Integer mountId) {
		return iliveServeerMountDao.getMountInfoById(mountId);
	}

}
