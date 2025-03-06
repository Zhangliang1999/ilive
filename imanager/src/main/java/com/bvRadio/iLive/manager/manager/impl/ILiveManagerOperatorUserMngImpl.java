package com.bvRadio.iLive.manager.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bvRadio.iLive.manager.dao.ILiveManagerOperatorUserDao;
import com.bvRadio.iLive.manager.entity.IliveOperationUser;
import com.bvRadio.iLive.manager.manager.ILiveManagerOperatorUserMng;

public class ILiveManagerOperatorUserMngImpl implements ILiveManagerOperatorUserMng {
	
	@Autowired
	private ILiveManagerOperatorUserDao operatorUserDao;

	@Override
	public IliveOperationUser getUserByUserName(String principal) {
		return operatorUserDao.getUserByUserName(principal);
	}

	@Override
	public IliveOperationUser getUserById(Long userId) {
		return operatorUserDao.getUserById(userId);
	}
	@Override
	public List<IliveOperationUser> listByParams(){
		return operatorUserDao.listByParams();
	}

	
	
}
