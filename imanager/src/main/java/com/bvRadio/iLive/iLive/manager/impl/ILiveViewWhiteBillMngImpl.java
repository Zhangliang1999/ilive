package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bvRadio.iLive.iLive.dao.ILiveViewWhiteBillDao;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;

public class ILiveViewWhiteBillMngImpl implements ILiveViewWhiteBillMng {
	
	@Autowired
	private ILiveViewWhiteBillDao iLiveViewWhiteBillDao;

	@Override
	public boolean checkUserInWhiteList(String userId, Long liveEventId) {
		// TODO Auto-generated method stub
		return iLiveViewWhiteBillDao.checkUserInWhiteList(userId,liveEventId);
	}

	@Override
	public List<ILiveViewWhiteBill> getAllViewWhiteBill(Long liveEventId) {
		return iLiveViewWhiteBillDao.getAllViewWhiteBill(liveEventId);
	}
	
	
	

}
